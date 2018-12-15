package br.edu.ifpb.tsi.pd.websocket;

import java.text.SimpleDateFormat;
import java.util.Date;
public class Utils {    
    private static SimpleDateFormat formatter = new SimpleDateFormat("HH:mm dd/MM/yyyy"); 

    public static Double extrairCredito(String mensagem) {
        String[] valor = mensagem.trim().split("__");
        
        return Double.parseDouble(valor[valor.length-1]);
    }
    
    public static String extrairDestinatario(String mensagem) {
        String[] destinatario = mensagem.trim().split("__");
        
        return destinatario[destinatario.length - 1];
    }
    
    public static String montarMensagem(String mensagem, String numero) {
        return "numero(" + numero + ") " + "enviou: " + extrairMensagem(mensagem);
    }
    
    public static String extrairMensagem(String mensagem) {
        String[] resultado = mensagem.trim().split("__");
        
        return resultado[0];
    }
    
    public static String DoubleToString(Double numero) {
        return String.valueOf(numero);
    }

    public static String montarNotificacao(String destinatario) {
        Date date = new Date();
        return "Mensagem a " + destinatario+ " entregue em: " 
                + formatter.format(date);
    }
    
    public static String helper(String numero) {
        String help = "Seja bem vindo: " + numero + "\n";
        help += "Comandos da operadora: \n";
        help += "Obter saldo: 'saldo'\n";
        help += "Adicionar crédito: 'creditar __<valor>'\n";
        help += "Enviar mensagem: '<mensagem> __<destinatario>'\n";
        help += "Caso informe um comando não listado acima, pode ocorrer erros inesperados!\n";
                
        return help;        
    }
}