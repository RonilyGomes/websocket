package br.edu.ifpb.tsi.pd.websocket;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LinhaTelefonica {
    private String numero;
    private Double saldo;
    private Map<String, List<String>> filaDeMensagem = 
            new HashMap<String, List<String>>();
    private Map<String, List<Noticacao>> filaDeNotificacao = 
            new HashMap<String, List<Noticacao>>();
    
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

    public Map<String, List<String>> getFilaDeMensagem() {
        return filaDeMensagem;
    }

    public void setFilaDeMensagem(Map<String, List<String>> filaDeMensagem) {
        this.filaDeMensagem = filaDeMensagem;
    }

    public Map<String, List<Noticacao>> getFilaDeNotificacao() {
        return filaDeNotificacao;
    }

    public void setFilaDeNotificacao(Map<String, List<Noticacao>> filaDeNotificacao) {
        this.filaDeNotificacao = filaDeNotificacao;
    }
    
    
}
