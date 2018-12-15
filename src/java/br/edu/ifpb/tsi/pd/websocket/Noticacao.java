package br.edu.ifpb.tsi.pd.websocket;

import java.util.Date;

public class Noticacao {
    private Date data;
    private String mensagem;
    
    public void Notificacao(String mensagem) {
        this.mensagem = mensagem;
        this.data = new Date();
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
