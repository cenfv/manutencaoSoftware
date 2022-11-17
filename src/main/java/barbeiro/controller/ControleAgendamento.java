package barbeiro.controller;

import barbeiro.dao.AgendamentoDao;
import barbeiro.model.Agendamento;
import barbeiro.model.Cliente;
import barbeiro.model.Servico;
import barbeiro.model.Funcionario;
import barbeiro.utils.ColumnFormatter;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;


import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class ControleAgendamento implements Initializable, Cadastro {
    private AgendamentoDao agendamentoDao = new AgendamentoDao();
    private ObservableList<Agendamento> observableList = FXCollections.observableArrayList();
    private List<Agendamento> listaAgendamentos;
    public static Agendamento agendamentoSelecionado = new Agendamento();
    public static Agendamento novoAgendamento;
    private int pesquisaSelecionada = 0;
    @FXML
    private TableView<Agendamento> tableView;
    @FXML
    private DatePicker datePickerAgendamento;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        criarColunasTabela();
        atualizarTabela();
    }

    @FXML
    private void adicionar(ActionEvent event) throws IOException {
        novoAgendamento = new Agendamento();
        CadastroAgendamento.ALTERAR = 0;
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/CadastroAgendamento.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Adicionar Agendamento");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        atualizarTabela();
    }

    @FXML
    private void alterar(ActionEvent event) throws IOException {
        CadastroAgendamento.ALTERAR = 1;
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/CadastroAgendamento.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Alterar Agendamento");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        atualizarTabela();
    }

    @FXML
    private void excluir(ActionEvent event) {
        agendamentoDao.excluir(agendamentoSelecionado);
        JOptionPane.showMessageDialog(null,"Serviço excluido com sucesso");
        atualizarTabela();
    }

    @Override
    public void criarColunasTabela() {
        tableView.getColumns().clear();
        TableColumn<Agendamento, Long> colunaId = new TableColumn<>("Id");
        TableColumn<Agendamento,LocalDate> colunaData = new TableColumn<>("Data");
        TableColumn<Agendamento,LocalTime> colunaInicio = new TableColumn<>("Horário inicio");
        TableColumn<Agendamento,LocalTime> colunaFim = new TableColumn<>("Horário Fim");
        TableColumn<Agendamento, String> colunaCliente = new TableColumn<>("Cliente");
        TableColumn<Agendamento, String> colunaUsuario = new TableColumn<>("Funcionário");
        TableColumn<Agendamento, String> colunaServico= new TableColumn<>("Serviço");
        TableColumn<Agendamento, Boolean> colunaPago = new TableColumn<>("Pago");

        tableView.getColumns().addAll(colunaId, colunaData, colunaInicio,colunaFim, colunaCliente,colunaUsuario,colunaServico,colunaPago);
        colunaId.setCellValueFactory(new PropertyValueFactory("id"));
        colunaData.setCellValueFactory(new PropertyValueFactory("data"));
        colunaData.setCellFactory(new ColumnFormatter<>(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        colunaInicio.setCellValueFactory(new PropertyValueFactory("horarioInicio"));
        colunaFim.setCellValueFactory(new PropertyValueFactory("horarioFim"));
        colunaPago.setCellValueFactory(new PropertyValueFactory("pago"));
        colunaUsuario.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsuario().getNome()));
        colunaCliente.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCliente().getNome()));
        colunaServico.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getServico().getNome()));
    }

    @Override
    public void atualizarTabela() {
        observableList.clear();
        switch (pesquisaSelecionada){
            case 0:
                if(datePickerAgendamento.getValue() == null){
                    listaAgendamentos = agendamentoDao.consultar();
                }else{
                    listaAgendamentos = agendamentoDao.consultarData(datePickerAgendamento.getValue());
                }

                break;
        }

        for (Agendamento a : listaAgendamentos) {
            observableList.add(a);
        }
        tableView.getItems().setAll(observableList);
        tableView.getSelectionModel().selectFirst();
        agendamentoSelecionado = tableView.getSelectionModel().getSelectedItem();
    }
    @FXML
    private void limpar(){
        datePickerAgendamento.setValue(null);
    }
    @Override
    public void setCamposFormulario() {

    }

    @Override
    public void limparCamposFormulario() {

    }

    @FXML
    private void atualizarSelecionado(MouseEvent event) {
        agendamentoSelecionado = tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());
    }
}
