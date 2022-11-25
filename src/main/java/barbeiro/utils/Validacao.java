package barbeiro.utils;

import br.com.caelum.stella.validation.CPFValidator;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validacao {
    public static boolean validarEmail(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }
    public static boolean validarTelefone(String telefone)
    {
        Pattern p = Pattern.compile("^\\(?(?:[14689][1-9]|2[12478]|3[1234578]|5[1345]|7[134579])\\)? ?(?:[2-8]|9[1-9])[0-9]{3}\\-?[0-9]{4}$");
        Matcher m = p.matcher(telefone);
        return (m.matches());
    }

    public static boolean validarCPF(String cpf) {
        CPFValidator cpfValidator = new CPFValidator();
        try{ cpfValidator.assertValid(cpf);
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static boolean validarDataNasc(LocalDate data){
        LocalDate dataAtual = LocalDate.now();
        if (data.compareTo(dataAtual) > 0) {
            return false;
        }
        return true;
    }

    public static boolean validaAgendamento(LocalDate data, LocalTime hora){
        hora = hora.plusMinutes(1);
        LocalDateTime dataAtual = LocalDateTime.now();
        LocalDateTime dataHora = LocalDateTime.of(data,hora);

        if (dataHora.compareTo(dataAtual) < 0 ) {
            return false;
        }

        return true;
    }

}
