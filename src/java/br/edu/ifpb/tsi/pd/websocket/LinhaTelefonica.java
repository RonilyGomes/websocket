package br.edu.ifpb.tsi.pd.websocket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LinhaTelefonica {
    private final static Double TARIFA_MENSAGEM = 0.5;
    private String numero;
    private Double saldo;
    private Map<String, List<String>> listaDeMensagem = 
            new HashMap<String, List<String>>();
    private Map<String, List<String>> listaDeNotificacao = 
            new HashMap<String, List<String>>();
    
    public LinhaTelefonica(String numero) {
        this.numero = numero;
        this.saldo = 0.0;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double Saldo) {
        this.saldo = Saldo;
    }

    public Map<String, List<String>> getListaDeMensagem() {
        return listaDeMensagem;
    }

    public Map<String, List<String>> getListaDeNotificacao() {
        return listaDeNotificacao;
    }

    public void setListaDeNotificacao(Map<String, List<String>> listaDeNotificacao) {
        this.listaDeNotificacao = listaDeNotificacao;
    }
    
    public Boolean temListaDeMensagem() {
        return !this.listaDeMensagem.isEmpty();
    } 
    
    public Boolean temListaDeNotificacao() {
        return !this.listaDeNotificacao.isEmpty();
    } 
    
    public void adicionarNotificacao(String destinatario, String notificacao) {
        if (this.listaDeNotificacao.containsKey(destinatario)) {
            this.listaDeNotificacao.get(destinatario).add(notificacao);
        }
        else {
            List<String> notificacoes = new ArrayList<>();
            notificacoes.add(notificacao);
            this.listaDeNotificacao.put(destinatario, notificacoes);
        }
    }
    
    public void adicionarMensagem(String remetente, String mensagem) {
        if (this.listaDeMensagem.containsKey(remetente)) {
            this.listaDeMensagem.get(remetente).add(mensagem);
        }
        else {
            List<String> mensagens = new ArrayList<>();
            mensagens.add(mensagem);
            this.listaDeMensagem.put(remetente, mensagens);
        }
    }
    
    public Boolean estaConsultandoSaldo(String messagem) {
        return messagem.trim().equalsIgnoreCase("saldo");
    }
    
    public Boolean estaCreditando(String messagem) {
        return messagem.trim().split(" ")[0].equalsIgnoreCase("creditar");
    }
    
    public void adcionarCredito(Double credito) {
        this.saldo = saldo + credito; 
    }
    
    public void debitarMensagem() {
        this.saldo = saldo - TARIFA_MENSAGEM;
    }
    
    public Boolean temSaldo() {
        return this.saldo >= 0.5;
    }
    
    public void LimparListaDeMensagem() {
        this.listaDeMensagem.clear();
    }
    
    public void LimparListaDeNotificacao() {
        this.listaDeNotificacao.clear();
    }
}
