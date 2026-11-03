package br.com.fuctura.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.fuctura.entity.Aluno;

public class AlunoDAO {

	public void salvar(Connection connection, Aluno aluno) throws SQLException {

		// a matricula e gerada pelo banco atraves da funcao gerar_matricula()
		String comandoSQL = "INSERT INTO aluno (matricula, nome, idade, celular) VALUES (gerar_matricula(), ?, ?, ?)";

		try (PreparedStatement statement = connection.prepareStatement(comandoSQL)) {
			statement.setString(1, aluno.getNome());
			statement.setInt(2, aluno.getIdade());
			statement.setString(3, aluno.getCelular());
			statement.execute();
		}
	}

	public List<Aluno> listarTodos(Connection connection) throws SQLException {

		String comandoSQL = "SELECT matricula, nome, idade, celular FROM aluno ORDER BY nome";
		List<Aluno> alunos = new ArrayList<>();

		try (PreparedStatement statement = connection.prepareStatement(comandoSQL);
				ResultSet resultadoConsulta = statement.executeQuery()) {

			while (resultadoConsulta.next()) {
				Aluno aluno = new Aluno();
				aluno.setMatricula(resultadoConsulta.getString("matricula"));
				aluno.setNome(resultadoConsulta.getString("nome"));
				aluno.setIdade(resultadoConsulta.getInt("idade"));
				aluno.setCelular(resultadoConsulta.getString("celular"));
				alunos.add(aluno);
			}
		}

		return alunos;
	}

	public boolean existePorId(Connection connection, String matricula) throws SQLException {

		String comandoSQL = "SELECT 1 FROM aluno WHERE matricula = ?";

		try (PreparedStatement statement = connection.prepareStatement(comandoSQL)) {
			statement.setString(1, matricula);

			try (ResultSet resultado = statement.executeQuery()) {
				return resultado.next();
			}
		}
	}

	public boolean excluir(Connection connection, String matricula) throws SQLException {

		String comandoSQL = "DELETE FROM aluno WHERE matricula = ?";

		try (PreparedStatement statement = connection.prepareStatement(comandoSQL)) {
			statement.setString(1, matricula);
			return statement.executeUpdate() > 0;
		}
	}
}