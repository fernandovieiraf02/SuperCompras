import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ExemploData {
    public static void main(String[] args) {
        // Data para teste
        Date data = new Date();
        Locale ptBR = new Locale("pt", "BR");
        
        // Diferentes formatos do dia da semana
        System.out.println("Formatos do dia da semana:");
        System.out.println("E    -> " + new SimpleDateFormat("E", ptBR).format(data));
        System.out.println("EE   -> " + new SimpleDateFormat("EE", ptBR).format(data));
        System.out.println("EEE  -> " + new SimpleDateFormat("EEE", ptBR).format(data));
        System.out.println("EEEE -> " + new SimpleDateFormat("EEEE", ptBR).format(data));
    }
} 