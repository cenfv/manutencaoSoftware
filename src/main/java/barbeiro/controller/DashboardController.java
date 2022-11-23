package barbeiro.controller;

import barbeiro.dao.AgendamentoDao;
import barbeiro.dao.ClienteDao;
import barbeiro.dao.FuncionarioDao;
import barbeiro.dao.ServicoDao;
import barbeiro.model.Agendamento;
import barbeiro.utils.ComboBoxLists;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    @FXML
    private Text TextAgendamentos;
    @FXML
    private Text TextClientes;
    @FXML
    private Text TextServicos;
    @FXML
    private Text TextFuncionarios;
    @FXML
    private Text TextAgendamentosHoje;
    @FXML
    private Text TextPagamentosPendentes;

    @FXML
    private BarChart<String, Double> barChartClientes;
    @FXML
    private BarChart<String, Double> barChartLucro;
    @FXML
    private BarChart<String, Double> barChartAgendamentos;
    private AgendamentoDao agendamentoDao = new AgendamentoDao();
    private FuncionarioDao usuarioDao = new FuncionarioDao();
    private ClienteDao clienteDao = new ClienteDao();
    private ServicoDao servicoDao = new ServicoDao();
    private int ano = 2022;
    @FXML
    private ComboBox<?> cbPesquisa;
    @FXML
    private Spinner<Integer> spinnerData;
    private SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,9999);
    @FXML
    private HBox boxPeriodo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        valueFactory.setValue(LocalDate.now().getYear());
        spinnerData.setValueFactory(valueFactory);
        spinnerData.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                spinnerData.increment(0); // para atualizar o valor do spinner
            }
        });

        valueFactory.valueProperty().addListener((observable, oldValue, newValue) -> {
           atualizarAno();
        });

        cbPesquisa.setItems(ComboBoxLists.gerarPesquisaDashboard());
        cbPesquisa.getSelectionModel().selectFirst();

        TextAgendamentos.setText(String.valueOf(agendamentoDao.consultarTodosNRegistros()));
        TextAgendamentos.setText(String.valueOf(agendamentoDao.consultarTodosNRegistros()));
        TextClientes.setText(String.valueOf(clienteDao.consultarTodosNRegistros()));
        TextFuncionarios.setText(String.valueOf(usuarioDao.consultarTodosNRegistros()));
        List<Agendamento> list = agendamentoDao.consultarData(LocalDate.now());
        int qtd = list.toArray().length;
        TextAgendamentosHoje.setText(String.valueOf(qtd));
        TextPagamentosPendentes.setText(String.valueOf(agendamentoDao.consultarNPagamentosPendentes()));
        TextServicos.setText(String.valueOf(servicoDao.consultarTodosNRegistros()));
        atualizarGraficos();
    }
    public void atualizarGraficos(){
        carregarDadosChartAgendamentos();
        carregarDadosChartClientes();
        carregarDadosChartLucro();
    }
    public void atualizarAno(){
        ano = valueFactory.getValue();
        atualizarGraficos();
    }
    public void mudarPesquisa(){
        String res = cbPesquisa.getSelectionModel().getSelectedItem().toString();
        switch (res){
            case "Ano":
                spinnerData.setDisable(false);
                spinnerData.setVisible(true);
                boxPeriodo.setDisable(true);
                boxPeriodo.setVisible(false);

                break;
            case "Intervalo de datas":
                spinnerData.setDisable(true);
                spinnerData.setVisible(false);
                boxPeriodo.setDisable(false);
                boxPeriodo.setVisible(true);
                break;
        }

    }

    private void carregarDadosChartAgendamentos(){
        barChartAgendamentos.getData().clear();
        barChartAgendamentos.layout();
        XYChart.Series<String,Double> series_01 = new XYChart.Series();
        series_01.setName("Ano "+ano);
        series_01.getData().add(new XYChart.Data("Jan",agendamentoDao.consultarNRegistros(1,ano)));
        series_01.getData().add(new XYChart.Data("Fev",agendamentoDao.consultarNRegistros(2,ano)));
        series_01.getData().add(new XYChart.Data("Mar",agendamentoDao.consultarNRegistros(3,ano)));
        series_01.getData().add(new XYChart.Data("Abr",agendamentoDao.consultarNRegistros(4,ano)));
        series_01.getData().add(new XYChart.Data("Mai",agendamentoDao.consultarNRegistros(5,ano)));
        series_01.getData().add(new XYChart.Data("Jun",agendamentoDao.consultarNRegistros(6,ano)));
        series_01.getData().add(new XYChart.Data("Jul",agendamentoDao.consultarNRegistros(7,ano)));
        series_01.getData().add(new XYChart.Data("Ago",agendamentoDao.consultarNRegistros(8,ano)));
        series_01.getData().add(new XYChart.Data("Set",agendamentoDao.consultarNRegistros(9,ano)));
        series_01.getData().add(new XYChart.Data("Out",agendamentoDao.consultarNRegistros(10,ano)));
        series_01.getData().add(new XYChart.Data("Nov",agendamentoDao.consultarNRegistros(11,ano)));
        series_01.getData().add(new XYChart.Data("Dez",agendamentoDao.consultarNRegistros(12,ano)));
        barChartAgendamentos.getData().add(series_01);
    }

    private void carregarDadosChartClientes(){
        barChartClientes.getData().clear();
        barChartClientes.layout();
        XYChart.Series<String,Double> series_01 = new XYChart.Series();
        series_01.setName("Ano "+ano);
        series_01.getData().add(new XYChart.Data("Jan",clienteDao.consultarNRegistros(1,ano)));
        series_01.getData().add(new XYChart.Data("Fev",clienteDao.consultarNRegistros(2,ano)));
        series_01.getData().add(new XYChart.Data("Mar",clienteDao.consultarNRegistros(3,ano)));
        series_01.getData().add(new XYChart.Data("Abr",clienteDao.consultarNRegistros(4,ano)));
        series_01.getData().add(new XYChart.Data("Mai",clienteDao.consultarNRegistros(5,ano)));
        series_01.getData().add(new XYChart.Data("Jun",clienteDao.consultarNRegistros(6,ano)));
        series_01.getData().add(new XYChart.Data("Jul",clienteDao.consultarNRegistros(7,ano)));
        series_01.getData().add(new XYChart.Data("Ago",clienteDao.consultarNRegistros(8,ano)));
        series_01.getData().add(new XYChart.Data("Set",clienteDao.consultarNRegistros(9,ano)));
        series_01.getData().add(new XYChart.Data("Out",clienteDao.consultarNRegistros(10,ano)));
        series_01.getData().add(new XYChart.Data("Nov",clienteDao.consultarNRegistros(11,ano)));
        series_01.getData().add(new XYChart.Data("Dez",clienteDao.consultarNRegistros(12,ano)));
        barChartClientes.getData().add(series_01);
    }

    private void carregarDadosChartLucro(){
        barChartLucro.getData().clear();
        barChartLucro.layout();
        XYChart.Series<String,Double> series_01 = new XYChart.Series();
        series_01.setName("Ano "+ano);

        series_01.getData().add(new XYChart.Data("Jan",agendamentoDao.consultarLucro(1,ano)));
        series_01.getData().add(new XYChart.Data("Fev",agendamentoDao.consultarLucro(2,ano)));
        series_01.getData().add(new XYChart.Data("Mar",agendamentoDao.consultarLucro(3,ano)));
        series_01.getData().add(new XYChart.Data("Abr",agendamentoDao.consultarLucro(4,ano)));
        series_01.getData().add(new XYChart.Data("Mai",agendamentoDao.consultarLucro(5,ano)));
        series_01.getData().add(new XYChart.Data("Jun",agendamentoDao.consultarLucro(6,ano)));
        series_01.getData().add(new XYChart.Data("Jul",agendamentoDao.consultarLucro(7,ano)));
        series_01.getData().add(new XYChart.Data("Ago",agendamentoDao.consultarLucro(8,ano)));
        series_01.getData().add(new XYChart.Data("Set",agendamentoDao.consultarLucro(9,ano)));
        series_01.getData().add(new XYChart.Data("Out",agendamentoDao.consultarLucro(10,ano)));
        series_01.getData().add(new XYChart.Data("Nov",agendamentoDao.consultarLucro(11,ano)));
        series_01.getData().add(new XYChart.Data("Dez",agendamentoDao.consultarLucro(12,ano)));

        barChartLucro.getData().add(series_01);
    }
}
