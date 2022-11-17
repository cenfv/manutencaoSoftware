package barbeiro.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "Agendamento")
public class Agendamento implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "age_data",nullable = false)
    private LocalDate data;
    @Column(name = "age_horario_inicio",nullable = false)
    private LocalTime horarioInicio;

    @Column(name = "age_horario_fim",nullable = false)
    private LocalTime horarioFim;
    @Column(name = "age_pago", nullable = false)
    private boolean pago = false;
    @OneToOne
    @JoinColumn(name = "usu_id",nullable = false)
    private Funcionario funcionario;
    @OneToOne
    @JoinColumn(name = "cli_id",nullable = false)
    private Cliente cliente;
    @OneToOne
    @JoinColumn(name = "ser_id",nullable = false)
    private Servico servico;
    @Column(name = "age_data_cadastro", nullable = true)
    private LocalDate dataCadastro;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public boolean isPago() {
        return pago;
    }

    public void setPago(boolean pago) {
        this.pago = pago;
    }

    public Funcionario getUsuario() {
        return funcionario;
    }

    public void setUsuario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }

    public Cliente getCliente() {return cliente;}

    public void setCliente(Cliente cliente) {this.cliente = cliente;}

    public LocalTime getHorarioInicio() {
        return horarioInicio;
    }

    public void setHorarioInicio(LocalTime horaioInicio) {
        this.horarioInicio = horaioInicio;
    }

    public LocalTime getHorarioFim() {
        return horarioFim;
    }

    public void setHorarioFim(LocalTime horarioFim) {
        this.horarioFim = horarioFim;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }
}
