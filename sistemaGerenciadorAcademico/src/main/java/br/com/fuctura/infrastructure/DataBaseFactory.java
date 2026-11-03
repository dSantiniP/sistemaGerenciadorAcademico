package br.com.fuctura.infrastructure;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DataBaseFactory {

	private static final int TEMPO_LIMITE_CONEXAO_SEGUNDOS = 10;

	// carregado na primeira conexao, e nao quando a classe e carregada
	private static Properties propriedades;

	// cada chamada abre uma conexao NOVA; quem chama e responsavel por fechar
	public static Connection getConnection() throws SQLException {
		if (propriedades == null) {
			propriedades = carregarPropriedades();
		}

		// sem isso, com o banco fora do ar o programa pode ficar muito tempo esperando
		DriverManager.setLoginTimeout(TEMPO_LIMITE_CONEXAO_SEGUNDOS);

		return DriverManager.getConnection(
				propriedades.getProperty("db.url"),
				propriedades.getProperty("db.user"),
				propriedades.getProperty("db.password"));
	}

	private static Properties carregarPropriedades() throws SQLException {
		try (InputStream input = DataBaseFactory.class.getResourceAsStream("/application.properties")) {

			if (input == null) {
				throw new SQLException("Arquivo application.properties nao encontrado em src/main/resources.");
			}

			Properties props = new Properties();
			props.load(input);
			return props;

		} catch (IOException e) {
			throw new SQLException("Erro ao ler o arquivo application.properties.", e);
		}
	}
}