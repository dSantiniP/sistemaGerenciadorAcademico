package br.com.fuctura.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.fuctura.entity.Disciplina;

public class DisciplinaDAO {

	public void salvar(Connection connection, Disciplina disciplina) throws SQLException {

		String comandoSQL = "INSERT INTO disciplina (codigo, nome, carga_horaria) VALUES (?, ?, ?)";

		try (PreparedStatement statement = connection.prepareStatement(comandoSQL)) {
			statement.setString(1, disciplina.getCodigo());
			statement.setString(2, disciplina.getNome());
			statement.setInt(3, disciplina.getCargaHoraria());
			statement.execute();
		}
	}

	public List<Disciplina> listarTodos(Connection connection) throws SQLException {

		String comandoSQL = "SELECT codigo, nome, carga_horaria FROM disciplina ORDER BY nome";
		List<Disciplina> disciplinas = new ArrayList<>();

		try (PreparedStatement statement = connection.prepareStatement(comandoSQL);
				ResultSet resultadoConsulta = statement.executeQuery()) {

			while (resultadoConsulta.next()) {
				Disciplina disciplina = new Disciplina();
				disciplina.setCodigo(resultadoConsulta.getString("codigo"));
				disciplina.setNome(resultadoConsulta.getString("nome"));
				disciplina.setCargaHoraria(resultadoConsulta.getInt("carga_horaria"));
				disciplinas.add(disciplina);
			}
		}

		return disciplinas;
	}

	public boolean existePorId(Connection connection, String codigo) throws SQLException {

		String comandoSQL = "SELECT 1 FROM disciplina WHERE codigo = ?";

		try (PreparedStatement statement = connection.prepareStatement(comandoSQL)) {
			statement.setString(1, codigo);

			try (ResultSet resultado = statement.executeQuery()) {
				return resultado.next();
			}
		}
	}

	public boolean excluir(Connection connection, String codigo) throws SQLException {

		String comandoSQL = "DELETE FROM disciplina WHERE codigo = ?";

		try (PreparedStatement statement = connection.prepareStatement(comandoSQL)) {
			statement.setString(1, codigo);
			return statement.executeUpdate() > 0;
		}
	}
}