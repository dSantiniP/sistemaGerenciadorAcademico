package br.com.fuctura.service;

import static br.com.fuctura.util.Validacao.limpar;
import static br.com.fuctura.util.Validacao.limparCodigo;
import static br.com.fuctura.util.Validacao.validarObrigatorio;
import static br.com.fuctura.util.Validacao.validarTamanhoMaximo;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import br.com.fuctura.dao.DisciplinaDAO;
import br.com.fuctura.entity.Disciplina;
import br.com.fuctura.infrastructure.DataBaseFactory;

public class DisciplinaService {

	// os limites espelham o tamanho das colunas no schema.sql
	private static final int TAMANHO_MAXIMO_CODIGO = 20;
	private static final int TAMANHO_MAXIMO_NOME = 100;

	// codigos do PostgreSQL
	private static final String CHAVE_DUPLICADA = "23505";
	private static final String VIOLACAO_CHAVE_ESTRANGEIRA = "23503";

	private final DisciplinaDAO dao = new DisciplinaDAO();

	public void salvar(Disciplina disciplina) {

		// padroniza antes de validar
		disciplina.setCodigo(limparCodigo(disciplina.getCodigo()));
		disciplina.setNome(limpar(disciplina.getNome()));

		// validacoes antes de abrir a conexao
		validarObrigatorio(disciplina.getCodigo(), "codigo");
		validarTamanhoMaximo(disciplina.getCodigo(), "codigo", TAMANHO_MAXIMO_CODIGO);
		validarObrigatorio(disciplina.getNome(), "nome");
		validarTamanhoMaximo(disciplina.getNome(), "nome", TAMANHO_MAXIMO_NOME);

		if (disciplina.getCargaHoraria() <= 0) {
			throw new IllegalArgumentException("A carga horaria deve ser maior que zero.");
		}

		try (Connection connection = DataBaseFactory.getConnection()) {
			dao.salvar(connection, disciplina);
		} catch (SQLException e) {
			if (CHAVE_DUPLICADA.equals(e.getSQLState())) {
				throw new IllegalArgumentException(
						"Ja existe uma disciplina com o codigo " + disciplina.getCodigo() + ".");
			}
			throw new IllegalStateException(
					"Nao foi possivel cadastrar a disciplina. Verifique a conexao com o banco de dados.", e);
		}
	}

	public List<Disciplina> listarTodos() {
		try (Connection connection = DataBaseFactory.getConnection()) {
			return dao.listarTodos(connection);
		} catch (SQLException e) {
			throw new IllegalStateException(
					"Nao foi possivel listar as disciplinas. Verifique a conexao com o banco de dados.", e);
		}
	}

	public void excluir(String codigo) {
		String codigoLimpo = limparCodigo(codigo);
		validarObrigatorio(codigoLimpo, "codigo");

		try (Connection connection = DataBaseFactory.getConnection()) {
			if (!dao.excluir(connection, codigoLimpo)) {
				throw new IllegalArgumentException("Nenhuma disciplina encontrada com o codigo " + codigoLimpo + ".");
			}
		} catch (SQLException e) {
			if (VIOLACAO_CHAVE_ESTRANGEIRA.equals(e.getSQLState())) {
				throw new IllegalArgumentException("A disciplina possui matriculas. Exclua as matriculas antes.");
			}
			throw new IllegalStateException(
					"Nao foi possivel excluir a disciplina. Verifique a conexao com o banco de dados.", e);
		}
	}
}