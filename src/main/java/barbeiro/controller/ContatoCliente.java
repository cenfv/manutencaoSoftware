package barbeiro.controller;

import barbeiro.dao.ContatoDao;
import barbeiro.model.Cliente;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.swing.*;

public class ContatoCliente implements Initializable {

    private ContatoDao contatoDao = new ContatoDao();
    @FXML
    private Button btnSalvar;
    @FXML
    private Button btnCancelar;
    @FXML
    private TextField TextFieldEmail;
    @FXML
    private TextField TextFieldTelefoneC;
    @FXML
    private TextField TextFieldTelefoneF;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregarDados();
    }

    private boolean validarEmail(String email) {
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

    @FXML
    private void salvar(ActionEvent event) throws IOException {
        if(TextFieldEmail.getText().trim().isEmpty() && TextFieldTelefoneC.getText().trim().isEmpty() && TextFieldTelefoneF.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(null,"Por favor, preencha ao menos um contato!");

        }else {
            if (!validarEmail(TextFieldEmail.getText())){
                JOptionPane.showMessageDialog(null,"Por favor, insira um email válido");
                return;
            }
            if (!validarTelefone(TextFieldTelefoneC.getText())){
                JOptionPane.showMessageDialog(null,"Por favor, insira um Telefone celular válido");
                return;
            }
            if (!validarTelefone(TextFieldTelefoneF.getText())){
                JOptionPane.showMessageDialog(null,"Por favor, insira um Telefone fixo válido");
                return;
            }
            if (CadastroClientes.ALTERAR == 1) {

                ControleClientes.clienteSelecionado.getContato().setEmail(TextFieldEmail.getText());
                ControleClientes.clienteSelecionado.getContato().setTelefoneCelular(TextFieldTelefoneC.getText());
                ControleClientes.clienteSelecionado.getContato().setTelefoneFixo(TextFieldTelefoneF.getText());
                contatoDao.salvar(ControleClientes.clienteSelecionado.getContato());

                Stage thisStage = (Stage) btnSalvar.getScene().getWindow();
                thisStage.close();
                JOptionPane.showMessageDialog(null,"Contato atualizado com sucesso!");

            } else {
                ControleClientes.novoCliente.getContato().setEmail(TextFieldEmail.getText());
                ControleClientes.novoCliente.getContato().setTelefoneCelular(TextFieldTelefoneC.getText());
                ControleClientes.novoCliente.getContato().setTelefoneFixo(TextFieldTelefoneF.getText());

                Stage thisStage = (Stage) btnSalvar.getScene().getWindow();
                thisStage.close();
                JOptionPane.showMessageDialog(null,"Salve o cliente para que as alterações tenham efeito");
            }

        }
    }

    private void carregarDados() {
        if (CadastroClientes.ALTERAR == 1) {
            Cliente cliente = ControleClientes.clienteSelecionado;
            TextFieldEmail.setText(cliente.getContato().getEmail());
            TextFieldTelefoneC.setText(cliente.getContato().getTelefoneCelular());
            TextFieldTelefoneF.setText(cliente.getContato().getTelefoneFixo());
        }
    }

    @FXML
    private void Cancelar(ActionEvent event) {
        Stage thisStage = (Stage) btnCancelar.getScene().getWindow();
        thisStage.close();
    }

}
