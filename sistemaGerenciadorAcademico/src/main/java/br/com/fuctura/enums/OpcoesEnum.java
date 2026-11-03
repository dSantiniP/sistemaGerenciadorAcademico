package br.com.fuctura.enums;

import java.util.Optional;

public enum OpcoesEnum {

	CADASTRAR("1"),
	LISTAR("2"),
	EXCLUIR("3"),
	VOLTAR("4");

	private final String opcaoSelecionada;

	private OpcoesEnum(String opcaoSelecionada) {
		this.opcaoSelecionada = opcaoSelecionada;
	}

	public String getOpcaoSelecionada() {
		return opcaoSelecionada;
	}

	// devolve a opcao correspondente ao texto digitado, ou vazio se nao existir
	public static Optional<OpcoesEnum> from(String opcaoSelecionada) {
		for (OpcoesEnum opcao : values()) {
			if (opcao.getOpcaoSelecionada().equals(opcaoSelecionada)) {
				return Optional.of(opcao);
			}
		}
		return Optional.empty();
	}
}