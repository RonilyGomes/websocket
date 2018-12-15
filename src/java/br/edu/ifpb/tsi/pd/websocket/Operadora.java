package br.edu.ifpb.tsi.pd.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/operadora/{numero}")
public class Operadora {    
    private static Map<String, Session> conexoesTelefonicas = 
            Collections.synchronizedMap(new HashMap<String, Session>());
    
    private static Map<String, LinhaTelefonica> linhasTelefonicas = 
            Collections.synchronizedMap(new HashMap<String, LinhaTelefonica>());
    
    @OnOpen
    public void open(Session sessao, @PathParam("numero")String numero){
        try {    
            sessao.getBasicRemote().sendText(Utils.helper(numero));
            this.adicionarConexao(numero, sessao);
            if (this.numeroCadastrado(numero)) {
                LinhaTelefonica cliente = this.obterLinhaTelefonica(numero);
                Session conexaoCliente = this.obterConexaoTelefonica(numero);
                
                if (cliente.temListaDeMensagem()) {
                    cliente.getListaDeMensagem().entrySet().forEach((mensagens) -> {
                        mensagens.getValue().forEach((mensagem) -> {
                            try {
                                sessao.getBasicRemote().sendText(mensagem);
                            } catch (IOException ex) {
                                Logger.getLogger(Operadora.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            
                            if (this.estaConectado(mensagens.getKey())) {
                                Session s = obterConexaoTelefonica(mensagens.getKey());
                                this.notificarRemetente(s, numero);
                            }
                            else {
                                linhasTelefonicas.get(mensagens.getKey())
                                    .adicionarNotificacao(numero, Utils.montarNotificacao(numero));
                            }
                        });
                    });
                    
                    cliente.LimparListaDeMensagem();
                }
                
                if (cliente.temListaDeNotificacao()) {
                    cliente.getListaDeNotificacao().entrySet().forEach((notificacoes) -> {
                        notificacoes.getValue().forEach((notificacao) -> {
                            this.receberNotificacao(sessao, notificacao);
                        });
                    });
                    
                    cliente.LimparListaDeNotificacao();
                }
            }
            else {
                this.adicionarLinhaTelefonica(numero);
            }
        } catch (Exception e) {
            Logger.getLogger(Operadora.class.getName()).log(Level.SEVERE, null, e);
        }
    }
            
    @OnMessage
    public String onMessage(String mensagem, @PathParam("numero")String numero) {
        LinhaTelefonica remetente = this.obterLinhaTelefonica(numero);
        
        if(remetente.estaConsultandoSaldo(mensagem)) {
            return Utils.DoubleToString(remetente.getSaldo());
        }
        
        if(remetente.estaCreditando(mensagem)) {
            remetente.adcionarCredito(Utils.extrairCredito(mensagem));
            
            return "Crédito adicionado!";
        }
        
        if (!remetente.temSaldo()) {
            return "Saldo insuficiente!";
        }
        
        String destinatario = Utils.extrairDestinatario(mensagem);
        remetente.debitarMensagem();
        
        if (!numeroCadastrado(destinatario)) {
            return "Destinatário inexistente!";
        }
        
        if (!estaConectado(destinatario)) {
            this.obterLinhaTelefonica(destinatario)
                .adicionarMensagem(numero, Utils.montarMensagem(mensagem, numero));
            
            return "Destinatário fora de alcance, quando a mensagem chegar você será noticado.";
        }
        
        try {
            obterConexaoTelefonica(destinatario).getBasicRemote()
                    .sendText(Utils.montarMensagem(mensagem, numero));
            return "Mensagem enviada";
        }
        catch (Exception e) {
            Logger.getLogger(Operadora.class.getName()).log(Level.SEVERE, null, e);
        }
        
        return "Ocorreu um erro inesperado";
    }
          
    @OnClose
    public void onClose(@PathParam("numero")String numero, Session sessao) {
        removerConexao(numero);
    }
    
    private Boolean estaConectado(String numero) {
        return conexoesTelefonicas.containsKey(numero);
    }
    
    private Boolean numeroCadastrado(String numero) {
        return linhasTelefonicas.containsKey(numero);
    }
    
    private LinhaTelefonica obterLinhaTelefonica(String numero) {
        return linhasTelefonicas.get(numero);
    }
    
    private Session obterConexaoTelefonica(String numero) {
        return conexoesTelefonicas.get(numero);
    }
    
    private void adicionarConexao(String numero, Session sessao) {
        conexoesTelefonicas.put(numero, sessao);
    }
    
    private void removerConexao(String numero) {
        conexoesTelefonicas.remove(numero);
    }
    
    private void adicionarLinhaTelefonica(String numero) {
        linhasTelefonicas.put(numero, new LinhaTelefonica(numero));
    }
    
    private void receberNotificacao(Session sessao, String notificacao) {
        try {
            sessao.getBasicRemote().sendText(notificacao);
        } catch (IOException ex) {
            Logger.getLogger(Operadora.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void notificarRemetente(Session remetente, String destinatario) {
        try {
            remetente.getBasicRemote().sendText(Utils.montarNotificacao(destinatario));
        }
        catch (Exception e) {
            Logger.getLogger(Operadora.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}