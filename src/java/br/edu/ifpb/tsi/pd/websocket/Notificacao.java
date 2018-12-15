package br.edu.ifpb.tsi.pd.websocket;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Notificacao {
    private String notificacao;
    private SimpleDateFormat formatter = new SimpleDateFormat("HH:mm dd/MM/yyyy"); 
    private Date date;
    
    public Notificacao(String detinatario) {
        this.date = new Date();
        this.notificacao = this.montarNotificacao(detinatario);
    }

    public String getNotificacao() {
        return notificacao;
    }

    public void setNotificacao(String notificacao) {
        this.notificacao = notificacao;
    }
    
    public String montarNotificacao(String destinatario) {
        return "Mensagem a " + destinatario+ " entregue em:" 
                + formatter.format(this.date);
    }
}
