package barbeiro.controller;

import barbeiro.dao.AgendamentoDao;
import barbeiro.model.Agendamento;
import barbeiro.model.Cliente;
import barbeiro.model.Servico;
import barbeiro.model.Funcionario;
import barbeiro.utils.ColumnFormatter;
import barbeiro.utils.ComboBoxLists;
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
import javafx.scene.layout.HBox;
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
    @FXML
    private ComboBox<?> cbPesquisa;

    @FXML
    private TextField textFieldPesquisa;
    @FXML
    private HBox hBoxPesquisa;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbPesquisa.setItems(ComboBoxLists.gerarPesquisaAgendamento());
        cbPesquisa.getSelectionModel().selectFirst();
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
        try {
            agendamentoDao.excluir(agendamentoSelecionado);
            JOptionPane.showMessageDialog(null, "Servi??o excluido com sucesso");
        }catch (Exception ex){
            JOptionPane.showMessageDialog(null, "O registro possui rela????es dependentes","Erro ao excluir",JOptionPane.ERROR_MESSAGE);

        }
        atualizarTabela();
    }

    @Override
    public void criarColunasTabela() {
        tableView.getColumns().clear();
        TableColumn<Agendamento, Long> colunaId = new TableColumn<>("Id");
        TableColumn<Agendamento,LocalDate> colunaData = new TableColumn<>("Data");
        TableColumn<Agendamento,LocalTime> colunaInicio = new TableColumn<>("Hor??rio inicio");
        TableColumn<Agendamento,LocalTime> colunaFim = new TableColumn<>("Hor??rio Fim");
        TableColumn<Agendamento, String> colunaCliente = new TableColumn<>("Cliente");
        TableColumn<Agendamento, String> colunaUsuario = new TableColumn<>("Funcion??rio");
        TableColumn<Agendamento, String> colunaServico= new TableColumn<>("Servi??o");
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
                listaAgendamentos = agendamentoDao.consultarPorFuncionario(textFieldPesquisa.getText());
                break;
            case 1:
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
    private void mudarPesquisa(){
        String res = cbPesquisa.getSelectionModel().getSelectedItem().toString();
        switch (res){
            case "Nome do funcion??rio":
                hBoxPesquisa.setVisible(false);
                hBoxPesquisa.setDisable(true);
                textFieldPesquisa.setVisible(true);
                textFieldPesquisa.setDisable(false);
                textFieldPesquisa.setPromptText("Pesquisar funcion??rio");
                pesquisaSelecionada = 0;
                break;
            case "Data Espec??fica":
                hBoxPesquisa.setVisible(true);
                hBoxPesquisa.setDisable(false);
                textFieldPesquisa.setVisible(false);
                textFieldPesquisa.setDisable(true);
                pesquisaSelecionada = 1;
                break;
        }
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
