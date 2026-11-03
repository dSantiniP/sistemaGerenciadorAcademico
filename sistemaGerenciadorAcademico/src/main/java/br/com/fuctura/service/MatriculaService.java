package br.com.fuctura.service;

import static br.com.fuctura.util.Validacao.limparCodigo;
import static br.com.fuctura.util.Validacao.validarObrigatorio;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import br.com.fuctura.dao.AlunoDAO;
import br.com.fuctura.dao.DisciplinaDAO;
import br.com.fuctura.dao.MatriculaDAO;
import br.com.fuctura.entity.Matricula;
import br.com.fuctura.infrastructure.DataBaseFactory;

public class MatriculaService {

	// codigo do PostgreSQL para chave duplicada (aluno ja matriculado na disciplina)
	private static final String CHAVE_DUPLICADA = "23505";

	private final MatriculaDAO dao = new MatriculaDAO();
	private final AlunoDAO alunoDAO = new AlunoDAO();
	private final DisciplinaDAO disciplinaDAO = new DisciplinaDAO();

	public void salvar(Matricula matricula) {

		// padroniza antes de validar
		matricula.setAlunoMatricula(limparCodigo(matricula.getAlunoMatricula()));
		matricula.setDisciplinaCodigo(limparCodigo(matricula.getDisciplinaCodigo()));

		// validacoes antes de abrir a conexao
		validarObrigatorio(matricula.getAlunoMatricula(), "matricula do aluno");
		validarObrigatorio(matricula.getDisciplinaCodigo(), "codigo da disciplina");

		// se ninguem informou a data, a matricula e feita com a data de hoje
		if (matricula.getDataMatricula() == null) {
			matricula.setDataMatricula(LocalDate.now());
		}

		try (Connection connection = DataBaseFactory.getConnection()) {

			if (!alunoDAO.existePorId(connection, matricula.getAlunoMatricula())) {
				throw new IllegalArgumentException(
						"Nao existe aluno com a matricula " + matricula.getAlunoMatricula() + ".");
			}
			if (!disciplinaDAO.existePorId(connection, matricula.getDisciplinaCodigo())) {
				throw new IllegalArgumentException(
						"Nao existe disciplina com o codigo " + matricula.getDisciplinaCodigo() + ".");
			}

			dao.salvar(connection, matricula);

		} catch (SQLException e) {
			if (CHAVE_DUPLICADA.equals(e.getSQLState())) {
				throw new IllegalArgumentException("Este aluno ja esta matriculado nesta disciplina.");
			}
			throw new IllegalStateException(
					"Nao foi possivel realizar a matricula. Verifique a conexao com o banco de dados.", e);
		}
	}

	public List<Matricula> listarTodos() {
		try (Connection connection = DataBaseFactory.getConnection()) {
			return dao.listarTodos(connection);
		} catch (SQLException e) {
			throw new IllegalStateException(
					"Nao foi possivel listar as matriculas. Verifique a conexao com o banco de dados.", e);
		}
	}

	public void excluir(int id) {
		if (id <= 0) {
			throw new IllegalArgumentException("O id da matricula deve ser maior que zero.");
		}

		try (Connection connection = DataBaseFactory.getConnection()) {
			if (!dao.excluir(connection, id)) {
				throw new IllegalArgumentException("Nenhuma matricula encontrada com o id " + id + ".");
			}
		} catch (SQLException e) {
			throw new IllegalStateException(
					"Nao foi possivel excluir a matricula. Verifique a conexao com o banco de dados.", e);
		}
	}
}