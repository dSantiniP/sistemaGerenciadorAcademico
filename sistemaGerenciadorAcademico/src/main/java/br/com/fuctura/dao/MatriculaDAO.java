package br.com.fuctura.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.fuctura.entity.Matricula;

public class MatriculaDAO {

	public void salvar(Connection connection, Matricula matricula) throws SQLException {

		// o id e gerado pelo banco, por isso nao entra no INSERT
		String comandoSQL = "INSERT INTO matricula (data_matricula, aluno_matricula, disciplina_codigo) VALUES (?, ?, ?)";

		try (PreparedStatement statement = connection.prepareStatement(comandoSQL)) {
			// LocalDate precisa ser convertido para java.sql.Date
			statement.setDate(1, Date.valueOf(matricula.getDataMatricula()));
			statement.setString(2, matricula.getAlunoMatricula());
			statement.setString(3, matricula.getDisciplinaCodigo());
			statement.execute();
		}
	}

	public List<Matricula> listarTodos(Connection connection) throws SQLException {

		String comandoSQL = "SELECT id, data_matricula, aluno_matricula, disciplina_codigo FROM matricula ORDER BY id";
		List<Matricula> matriculas = new ArrayList<>();

		try (PreparedStatement statement = connection.prepareStatement(comandoSQL);
				ResultSet resultadoConsulta = statement.executeQuery()) {

			while (resultadoConsulta.next()) {
				Matricula matricula = new Matricula();
				matricula.setId(resultadoConsulta.getInt("id"));

				// java.sql.Date precisa voltar a ser LocalDate
				Date dataMatricula = resultadoConsulta.getDate("data_matricula");
				if (dataMatricula != null) {
					matricula.setDataMatricula(dataMatricula.toLocalDate());
				}

				matricula.setAlunoMatricula(resultadoConsulta.getString("aluno_matricula"));
				matricula.setDisciplinaCodigo(resultadoConsulta.getString("disciplina_codigo"));
				matriculas.add(matricula);
			}
		}

		return matriculas;
	}

	public boolean excluir(Connection connection, int id) throws SQLException {

		String comandoSQL = "DELETE FROM matricula WHERE id = ?";

		try (PreparedStatement statement = connection.prepareStatement(comandoSQL)) {
			statement.setInt(1, id);
			return statement.executeUpdate() > 0;
		}
	}
}