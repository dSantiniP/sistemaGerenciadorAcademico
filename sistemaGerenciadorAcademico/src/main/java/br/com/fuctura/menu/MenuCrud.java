package br.com.fuctura.menu;

import java.util.Optional;
import java.util.Scanner;

import br.com.fuctura.enums.OpcoesEnum;

// base dos menus de cadastro: o laco e o tratamento de erros ficam aqui,
// e cada menu filho implementa so o que e especifico dele
public abstract class MenuCrud {

	public void exibir(Scanner entradaDeDados) {
		OpcoesEnum opcaoSelecionada = null;

		do {
			System.out.println("---Menu " + getTitulo() + "---");
			System.out.println("1 - Cadastrar");
			System.out.println("2 - Listar");
			System.out.println("3 - Excluir");
			System.out.println("4 - Voltar");
			System.out.println("Digite: ");
			String opcaoDigitada = entradaDeDados.nextLine();

			Optional<OpcoesEnum> opcao = OpcoesEnum.from(opcaoDigitada);
			if (opcao.isEmpty()) {
				System.out.println("Opcao invalida. Tente novamente.");
				continue;
			}
			opcaoSelecionada = opcao.get();

			try {
				if (opcaoSelecionada == OpcoesEnum.CADASTRAR) {
					cadastrar(entradaDeDados);
				} else if (opcaoSelecionada == OpcoesEnum.LISTAR) {
					listar();
				} else if (opcaoSelecionada == OpcoesEnum.EXCLUIR) {
					excluir(entradaDeDados);
				}
			} catch (IllegalArgumentException | IllegalStateException e) {
				System.out.println("Erro: " + e.getMessage());

				// mostra o erro original do banco, quando existir
				if (e.getCause() != null) {
					System.out.println("Detalhe: " + e.getCause().getMessage());
				}
			}

		} while (opcaoSelecionada != OpcoesEnum.VOLTAR);
	}

	// titulo exibido no topo do menu, por exemplo "Aluno"
	protected abstract String getTitulo();

	protected abstract void cadastrar(Scanner entradaDeDados);

	protected abstract void listar();

	protected abstract void excluir(Scanner entradaDeDados);
}