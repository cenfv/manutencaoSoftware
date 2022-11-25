package barbeiro.utils;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class ValidacaoTest {

    @Test
    void emailValidoGmail() {
        assertTrue(Validacao.validarEmail("carlosnfreitasv@gmail.com"));
    }
    @Test
    void emailValidoHotmail() {
        assertTrue(Validacao.validarEmail("carlosnfreitasv@hotmail.com"));
    }
    @Test
    void emailInvalido() {
        assertFalse(Validacao.validarEmail("carlos2510"));
    }
    @Test
    void emailInvalidoComPontoCom() {
        assertFalse(Validacao.validarEmail("carlos.com"));
    }
    @Test
    void emailInvalidoComArroba() {
        assertFalse(Validacao.validarEmail("carlos@"));
    }

    @Test
    void validarTelefoneValido() {
        assertTrue(Validacao.validarTelefone("(11) 99999-9999"));
    }

    @Test
    void validarTelefoneInvalido() {
        assertFalse(Validacao.validarTelefone("(11) 99999-999"));
    }

    @Test
    void validarTelefoneInvalidoSemDDD() {
        assertFalse(Validacao.validarTelefone("() 99999-999"));
    }

    @Test
    void validarCPFValido() {
        assertTrue(Validacao.validarCPF("475.961.698-56"));
    }

    @Test
    void validarCPFInvalido() {
        assertFalse(Validacao.validarCPF("475.961.698-55"));
    }

    @Test
    void validarCPFInvalidoComLetras() {
        assertFalse(Validacao.validarCPF("475.961.698-5a"));
    }
    @Test
    void validarDataNascValida() {
        assertTrue(Validacao.validarDataNasc(LocalDate.of(1999, 10, 25)));
    }
    @Test
    void validarDataNascInvalida() {
        assertFalse(Validacao.validarDataNasc(LocalDate.of(2026, 10, 25)));
    }
    @Test
    void validaAgendamentoValido() {
        assertTrue(Validacao.validaAgendamento(LocalDate.of(2026, 10, 25), LocalTime.of(10, 0)));
    }
    @Test
    void validaAgendamentoInvalido() {
        assertFalse(Validacao.validaAgendamento(LocalDate.of(2022, 10, 25), LocalTime.of(10, 0)));
    }
}