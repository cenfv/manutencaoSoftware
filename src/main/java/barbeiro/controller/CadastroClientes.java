package barbeiro.controller;

import barbeiro.dao.ClienteDao;
import barbeiro.model.Cliente;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

import br.com.caelum.stella.validation.CPFValidator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;


import javax.swing.*;

public class CadastroClientes implements Initializable {
    private ClienteDao clienteDao = new ClienteDao();
    public static int ALTERAR = 0;
    @FXML
    private Button btnSalvar;
    @FXML
    private Button btnCancelar;
    @FXML
    private TextField textFieldNome;
    @FXML
    private DatePicker datePickerData;
    @FXML
    private TextField textFieldCpf;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregarDados();
    }

    private void carregarDados() {
        if (ALTERAR == 1) {
            Cliente cliente = ControleClientes.clienteSelecionado;
            textFieldNome.setText(cliente.getNome());
            textFieldCpf.setText(cliente.getCpf());
            datePickerData.setValue(cliente.getDataNascimento());
        }
    }

    @FXML
    private void contato(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/ContatoCliente.fxml"));
        Scene scene = new Scene(root);

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Contato");
        stage.initModality(Modality.APPLICATION_MODAL); 
        stage.show();

    }

    @FXML
    private void endereco(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/EnderecoCliente.fxml"));
        Scene scene = new Scene(root);

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Endereço");
        stage.initModality(Modality.APPLICATION_MODAL); 
        stage.show();
    }

    private boolean validarCPF(String cpf) {
        CPFValidator cpfValidator = new CPFValidator();

        try{ cpfValidator.assertValid(cpf);
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean validarDataNasc(LocalDate data){
        LocalDate dataAtual = LocalDate.now();
        if (data.compareTo(dataAtual) > 0) {
            return false;
        }
        return true;
    }

    @FXML
    private void salvar(ActionEvent event) {
        if (textFieldNome.getText().trim().isEmpty() || textFieldCpf.getText().trim().isEmpty()){
            if (textFieldNome.getText().trim().isEmpty()){
                textFieldNome.requestFocus();
                JOptionPane.showMessageDialog(null,"O campo nome é obrigatório");
            }else {
                textFieldCpf.requestFocus();
                JOptionPane.showMessageDialog(null,"O campo CPF é obrigatório");
            }
        }else {
            boolean validacaoCPF = validarCPF(textFieldCpf.getText());
            if(!validacaoCPF){
                textFieldCpf.requestFocus();
                        JOptionPane.showMessageDialog(null,"O CPF inserido deve ser válido!");
                return;
            }
            boolean validacaoDataNasc = validarDataNasc(datePickerData.getValue());
            if(!validacaoDataNasc){
                JOptionPane.showMessageDialog(null,"A data de nascimento é inválida !");
                return;
            }
            if (ALTERAR == 1) {
                ControleClientes.clienteSelecionado.setNome(textFieldNome.getText());
                ControleClientes.clienteSelecionado.setCpf(textFieldCpf.getText());
                ControleClientes.clienteSelecionado.setDataNascimento(datePickerData.getValue());
                clienteDao.salvar(ControleClientes.clienteSelecionado);

                Stage thisStage = (Stage) btnSalvar.getScene().getWindow();
                thisStage.close();
                JOptionPane.showMessageDialog(null,"Cliente atualizado com sucesso!");
            }else{
                ControleClientes.novoCliente.setNome(textFieldNome.getText());
                ControleClientes.novoCliente.setCpf(textFieldCpf.getText());
                ControleClientes.novoCliente.setDataNascimento(datePickerData.getValue());
                LocalDate date = LocalDate.now();
                ControleClientes.novoCliente.setDataCadastro(date);
                clienteDao.salvar(ControleClientes.novoCliente);

                Stage thisStage = (Stage) btnSalvar.getScene().getWindow();
                thisStage.close();
                JOptionPane.showMessageDialog(null,"Cliente cadastrado com sucesso!");

            }
        }


    }

    @FXML
    private void cancelar(ActionEvent event) {
        Stage thisStage = (Stage) btnCancelar.getScene().getWindow();
        thisStage.close();
    }

}