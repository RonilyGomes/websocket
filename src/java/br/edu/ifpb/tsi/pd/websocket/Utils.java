package br.edu.ifpb.tsi.pd.websocket;

public class Utils {    
    private final static Double TARIFA_MENSAGEM = 0.5;
    
    public static Boolean estaCreditando(String messagem) {
        return messagem.trim().split(" ")[0].equalsIgnoreCase("creditar");
    }
    
    public static Double extrairCredito(String mensagem) {
        String[] valor = mensagem.trim().split(" ");
        
        return Double.parseDouble(valor[valor.length-1]);
    }
    
    public static void adcionarCredito(Double credito, LinhaTelefonica lt) {
        lt.setSaldo(lt.getSaldo() + credito); 
    }
    
    public static void debitarMensagem(LinhaTelefonica lt) {
        lt.setSaldo(lt.getSaldo() - TARIFA_MENSAGEM); 
    }
    
    public static Boolean estaConsultandoSaldo(String messagem) {
        return messagem.trim().equalsIgnoreCase("saldo");
    }
    
    public static String DoubleToString(Double numero) {
        return String.valueOf(numero);
    }
    
    public static Boolean temSaldo(LinhaTelefonica lt) {
        return lt.getSaldo() >= 0.5;
    }
    
    public static String extrarDestinatario(String mensagem) {
        String[] destinatario = mensagem.trim().split(" ");
        
        return destinatario[destinatario.length - 1];
    }
    
    public static String montarMensagem(String mensagem, String numero) {
        return mensagem + " | " + numero;
    }
}
