package barbeiro.dao;

import barbeiro.model.Agendamento;
import barbeiro.model.Funcionario;
import org.hibernate.Session;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AgendamentoDao {

    public boolean validaAgendamento(Agendamento agendamento, Session session){
        try{

            List agendamentos = new ArrayList<>();
            String queryString = MessageFormat.format("FROM Agendamento WHERE age_data = ''{0}'' AND usu_id = {1} AND (age_horario_inicio BETWEEN ''{2}'' AND " +
                    "''{3}'' or age_horario_fim BETWEEN ''{2}'' AND ''{3}'' )",agendamento.getData(),agendamento.getUsuario().getId(),agendamento.getHorarioInicio(),agendamento.getHorarioFim());
            agendamentos = session.createQuery(queryString).getResultList();
            if(agendamentos.size() > 0 ){
                return false;
            }

        } catch (Exception erro) {
            System.out.println("Ocorreu um erro:" + erro);
        }
        return true;
    }

    public boolean salvar(Agendamento agendamento) {
        try {
            Session session = ConexaoBanco.getSessionFactory().openSession();
            session.beginTransaction();
            agendamento.setHorarioFim(agendamento.getHorarioInicio().plusMinutes(agendamento.getServico().getDuracao()));
            boolean validationRes = validaAgendamento(agendamento,session);
            if(!validationRes) return false;
            session.merge(agendamento);
            session.getTransaction().commit();
            session.close();
            System.out.println("Registro gravado/alterado com sucesso");

        } catch (Exception erro) {
            System.out.println("Ocorreu um erro:" + erro);
            return false;
        }
       return true;
    }
    public List<Agendamento> consultar() {
        List<Agendamento> lista = new ArrayList<>();
        Session session = ConexaoBanco.getSessionFactory().openSession();
        session.beginTransaction();
        lista = session.createQuery("from Agendamento").getResultList();
        session.getTransaction().commit();
        session.close();
        return lista;
    }

    public List<Agendamento> consultarData(LocalDate data) {
        List<Agendamento> lista = new ArrayList<>();
        Session session = ConexaoBanco.getSessionFactory().openSession();
        session.beginTransaction();
        lista = session.createQuery("from Agendamento WHERE age_data = '"+data+"'").getResultList();
        session.getTransaction().commit();
        session.close();
        return lista;
    }
    public List<Agendamento> consultarPorFuncionario(String nome) {
        List<Agendamento> lista = new ArrayList<>();
        Session session = ConexaoBanco.getSessionFactory().openSession();
        session.beginTransaction();
        if (nome.length() == 0) {
            lista = session.createQuery("from Agendamento").getResultList();
        } else {
            lista = session.createQuery("select A from Agendamento as A INNER JOIN Funcionario as B ON B.nome like " + "'" + nome + "%' and A.funcionario = B.id ").getResultList();

        }
        session.getTransaction().commit();
        session.close();
        return lista;
    }
    public List<Agendamento> consultarIntervalosData(LocalDate dataInicio,LocalDate  dataFim) {
        List<Agendamento> lista = new ArrayList<>();
        Session session = ConexaoBanco.getSessionFactory().openSession();
        session.beginTransaction();
        lista = session.createQuery("from Agendamento WHERE age_data BETWEEN '"+dataInicio+"' AND '"+dataFim+"'").getResultList();
        session.getTransaction().commit();
        session.close();
        return lista;
    }

    public void excluir(Agendamento agendamento){
        try (Session session = ConexaoBanco.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.delete(agendamento);
            session.getTransaction().commit();
            session.close();
        }catch(Exception ex){
           throw ex;
        }
    }

    public long consultarNRegistros(int mes, int ano) {
        int mesfim = mes+1;
        long qtd = 0;
        Session session = ConexaoBanco.getSessionFactory().openSession();
        session.beginTransaction();
        if(mes == 12){
            qtd = (long) session.createQuery("select count(id) from Agendamento WHERE age_data BETWEEN '" + ano + "-" + mes + "-01' and '" + ano + "-" + mes + "-31' ").uniqueResult();
        }else{
            qtd = (long) session.createQuery("select count(id) from Agendamento WHERE age_data BETWEEN '" + ano + "-" + mes + "-01' and '" + ano + "-" + mesfim + "-01' ").uniqueResult();
        }

        session.getTransaction().commit();
        session.close();
        return qtd;
    }

    public long consultarTodosNRegistros() {
        long qtd = 0;
        Session session = ConexaoBanco.getSessionFactory().openSession();
        session.beginTransaction();

        qtd = (long) session.createQuery("select count(id) from Agendamento").uniqueResult();


        session.getTransaction().commit();
        session.close();
        return qtd;
    }

    public long consultarNPagamentosPendentes() {
        long qtd = 0;
        Session session = ConexaoBanco.getSessionFactory().openSession();
        session.beginTransaction();

        qtd = (long) session.createQuery("select count(id) from Agendamento WHERE age_pago = 0").uniqueResult();


        session.getTransaction().commit();
        session.close();
        return qtd;
    }
    public double consultarLucro(int mes, int ano) {
        int mesfim = mes+1;
        double qtd = 0;
        Session session = ConexaoBanco.getSessionFactory().openSession();
        session.beginTransaction();
        try {
            if (mes == 12) {
                qtd = (double) session.createQuery("SELECT SUM(B.preco) FROM Agendamento AS A, Servico AS B " +
                        "WHERE age_data BETWEEN '" + ano + "-" + mes + "-01' and '" + ano + "-" + mes + "-31' AND A.servico = B.id").uniqueResult();
            } else {
                qtd = (double) session.createQuery("SELECT SUM(B.preco) FROM Agendamento AS A, Servico AS B " +
                        "WHERE age_data BETWEEN '" + ano + "-" + mes + "-01' and '" + ano + "-" + mesfim + "-01' AND A.servico = B.id ").uniqueResult();
            }
        }catch (Exception ex){
            System.out.println(ex);
        }
        System.out.println(qtd);
        session.getTransaction().commit();
        session.close();
        return qtd;
    }

}
