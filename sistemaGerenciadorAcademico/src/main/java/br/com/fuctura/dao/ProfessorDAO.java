package br.com.fuctura.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.fuctura.entity.Professor;

public class ProfessorDAO {

	public void salvar(Connection connection, Professor professor) throws SQLException {

		String comandoSQL = "INSERT INTO professor (matricula, nome, telefone) VALUES (?, ?, ?)";

		try (PreparedStatement statement = connection.prepareStatement(comandoSQL)) {
			statement.setString(1, professor.getMatricula());
			statement.setString(2, professor.getNome());
			statement.setString(3, professor.getTelefone());
			statement.execute();
		}
	}

	public List<Professor> listarTodos(Connection connection) throws SQLException {

		String comandoSQL = "SELECT matricula, nome, telefone FROM professor ORDER BY nome";
		List<Professor> professores = new ArrayList<>();

		try (PreparedStatement statement = connection.prepareStatement(comandoSQL);
				ResultSet resultadoConsulta = statement.executeQuery()) {

			while (resultadoConsulta.next()) {
				Professor professor = new Professor();
				professor.setMatricula(resultadoConsulta.getString("matricula"));
				professor.setNome(resultadoConsulta.getString("nome"));
				professor.setTelefone(resultadoConsulta.getString("telefone"));
				professores.add(professor);
			}
		}

		return professores;
	}

	public boolean excluir(Connection connection, String matricula) throws SQLException {

		String comandoSQL = "DELETE FROM professor WHERE matricula = ?";

		try (PreparedStatement statement = connection.prepareStatement(comandoSQL)) {
			statement.setString(1, matricula);
			return statement.executeUpdate() > 0;
		}
	}
}