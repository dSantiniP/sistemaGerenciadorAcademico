package br.com.fuctura.service;

import static br.com.fuctura.util.Validacao.limpar;
import static br.com.fuctura.util.Validacao.limparCodigo;
import static br.com.fuctura.util.Validacao.validarObrigatorio;
import static br.com.fuctura.util.Validacao.validarTamanhoMaximo;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import br.com.fuctura.dao.AlunoDAO;
import br.com.fuctura.entity.Aluno;
import br.com.fuctura.infrastructure.DataBaseFactory;

public class AlunoService {

	// os limites espelham o tamanho das colunas no schema.sql
	private static final int TAMANHO_MAXIMO_NOME = 100;
	private static final int TAMANHO_MAXIMO_CELULAR = 15;
	private static final int IDADE_MINIMA = 1;
	private static final int IDADE_MAXIMA = 120;

	// codigo do PostgreSQL para violacao de chave estrangeira
	private static final String VIOLACAO_CHAVE_ESTRANGEIRA = "23503";

	private final AlunoDAO dao = new AlunoDAO();

	public void salvar(Aluno aluno) {

		// padroniza antes de validar
		aluno.setNome(limpar(aluno.getNome()));
		aluno.setCelular(limpar(aluno.getCelular()));

		// validacoes antes de abrir a conexao
		validarObrigatorio(aluno.getNome(), "nome");
		validarTamanhoMaximo(aluno.getNome(), "nome", TAMANHO_MAXIMO_NOME);
		validarTamanhoMaximo(aluno.getCelular(), "celular", TAMANHO_MAXIMO_CELULAR);

		if (aluno.getIdade() < IDADE_MINIMA || aluno.getIdade() > IDADE_MAXIMA) {
			throw new IllegalArgumentException(
					"Idade invalida. Informe um valor entre " + IDADE_MINIMA + " e " + IDADE_MAXIMA + ".");
		}

		try (Connection connection = DataBaseFactory.getConnection()) {
			dao.salvar(connection, aluno);
		} catch (SQLException e) {
			throw new IllegalStateException(
					"Nao foi possivel cadastrar o aluno. Verifique a conexao com o banco de dados.", e);
		}
	}

	public List<Aluno> listarTodos() {
		try (Connection connection = DataBaseFactory.getConnection()) {
			return dao.listarTodos(connection);
		} catch (SQLException e) {
			throw new IllegalStateException(
					"Nao foi possivel listar os alunos. Verifique a conexao com o banco de dados.", e);
		}
	}

	public void excluir(String matricula) {
		String matriculaLimpa = limparCodigo(matricula);
		validarObrigatorio(matriculaLimpa, "matricula");

		try (Connection connection = DataBaseFactory.getConnection()) {
			if (!dao.excluir(connection, matriculaLimpa)) {
				throw new IllegalArgumentException("Nenhum aluno encontrado com a matricula " + matriculaLimpa + ".");
			}
		} catch (SQLException e) {
			if (VIOLACAO_CHAVE_ESTRANGEIRA.equals(e.getSQLState())) {
				throw new IllegalArgumentException("O aluno possui matriculas. Exclua as matriculas antes.");
			}
			throw new IllegalStateException(
					"Nao foi possivel excluir o aluno. Verifique a conexao com o banco de dados.", e);
		}
	}
}