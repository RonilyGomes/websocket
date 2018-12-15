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
    public void abrir(Session session, @PathParam("numero")String numero){
        try {     
            conexoesTelefonicas.put(numero, session);
            if (linhasTelefonicas.containsKey(numero)) {
                // TODO
            }
            else {
                linhasTelefonicas.put(numero, new LinhaTelefonica(numero));
                session.getBasicRemote().sendText("Seja bem vindo,"+numero);
            }
        } catch (IOException ex) {
            Logger.getLogger(Operadora.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
            
    @OnMessage
    public String onMessage(String messagem, @PathParam("numero")String numero) {
        LinhaTelefonica remetente = linhasTelefonicas.get(numero);
        
        if(Utils.estaConsultandoSaldo(messagem)) {
            return Utils.DoubleToString(remetente.getSaldo());
        }
        
        if(Utils.estaCreditando(messagem)) {
            Utils.adcionarCredito(Utils.extrairCredito(messagem), remetente);
            
            return "Crédito adicionado!";
        }
        
        if (!Utils.temSaldo(remetente)) {
            return "Saldo insuficiente!";
        }
        
        String destinatario = Utils.extrarDestinatario(messagem);
        
        Utils.debitarMensagem(remetente);
        
        if (!linhasTelefonicas.containsKey(destinatario)) {
            return "Destinatário inexistente!";
        }
        
        if (!conexoesTelefonicas.containsKey(destinatario)) {
            LinhaTelefonica ltDest = linhasTelefonicas.get(destinatario);
            if (ltDest.getFilaDeMensagem().containsKey(numero)) {
                ltDest.getFilaDeMensagem().get(numero)
                    .add(Utils.montarMensagem(messagem, numero));
            }
            else {
                List msgs = new ArrayList<>();
                msgs.add(Utils.montarMensagem(messagem, numero));
                ltDest.getFilaDeMensagem().put(numero, msgs);
            }
        }
        
        try {
            conexoesTelefonicas.get(destinatario).getBasicRemote()
                    .sendText(Utils.montarMensagem(messagem, numero));
            return "Mensagem enviada";
        }
        catch (IOException ex) {
            Logger.getLogger(Operadora.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return "Ocorreu um erro inesperado";
    }
          
    @OnClose
    public void sair(Session ses){
        //usuarios.remove(ses);
    }
}