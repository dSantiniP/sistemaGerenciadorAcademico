package br.com.fuctura.service;

import static br.com.fuctura.util.Validacao.limpar;
import static br.com.fuctura.util.Validacao.limparCodigo;
import static br.com.fuctura.util.Validacao.validarObrigatorio;
import static br.com.fuctura.util.Validacao.validarTamanhoMaximo;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import br.com.fuctura.dao.ProfessorDAO;
import br.com.fuctura.entity.Professor;
import br.com.fuctura.infrastructure.DataBaseFactory;

public class ProfessorService {

	// os limites espelham o tamanho das colunas no schema.sql
	private static final int TAMANHO_MAXIMO_MATRICULA = 20;
	private static final int TAMANHO_MAXIMO_NOME = 100;
	private static final int TAMANHO_MAXIMO_TELEFONE = 15;

	// codigo do PostgreSQL para chave duplicada
	private static final String CHAVE_DUPLICADA = "23505";

	private final ProfessorDAO dao = new ProfessorDAO();

	public void salvar(Professor professor) {

		// padroniza antes de validar
		professor.setMatricula(limparCodigo(professor.getMatricula()));
		professor.setNome(limpar(professor.getNome()));
		professor.setTelefone(limpar(professor.getTelefone()));

		// validacoes antes de abrir a conexao
		validarObrigatorio(professor.getMatricula(), "matricula");
		validarTamanhoMaximo(professor.getMatricula(), "matricula", TAMANHO_MAXIMO_MATRICULA);
		validarObrigatorio(professor.getNome(), "nome");
		validarTamanhoMaximo(professor.getNome(), "nome", TAMANHO_MAXIMO_NOME);
		validarTamanhoMaximo(professor.getTelefone(), "telefone", TAMANHO_MAXIMO_TELEFONE);

		try (Connection connection = DataBaseFactory.getConnection()) {
			dao.salvar(connection, professor);
		} catch (SQLException e) {
			if (CHAVE_DUPLICADA.equals(e.getSQLState())) {
				throw new IllegalArgumentException(
						"Ja existe um professor com a matricula " + professor.getMatricula() + ".");
			}
			throw new IllegalStateException(
					"Nao foi possivel cadastrar o professor. Verifique a conexao com o banco de dados.", e);
		}
	}

	public List<Professor> listarTodos() {
		try (Connection connection = DataBaseFactory.getConnection()) {
			return dao.listarTodos(connection);
		} catch (SQLException e) {
			throw new IllegalStateException(
					"Nao foi possivel listar os professores. Verifique a conexao com o banco de dados.", e);
		}
	}

	public void excluir(String matricula) {
		String matriculaLimpa = limparCodigo(matricula);
		validarObrigatorio(matriculaLimpa, "matricula");

		try (Connection connection = DataBaseFactory.getConnection()) {
			if (!dao.excluir(connection, matriculaLimpa)) {
				throw new IllegalArgumentException(
						"Nenhum professor encontrado com a matricula " + matriculaLimpa + ".");
			}
		} catch (SQLException e) {
			throw new IllegalStateException(
					"Nao foi possivel excluir o professor. Verifique a conexao com o banco de dados.", e);
		}
	}
}