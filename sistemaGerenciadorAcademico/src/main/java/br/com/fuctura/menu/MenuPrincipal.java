package br.com.fuctura.menu;

import java.util.Scanner;

public class MenuPrincipal {

	public void exibir() {
		// um unico Scanner para o programa inteiro, repassado aos outros menus
		Scanner entradaDeDados = new Scanner(System.in);
		String opcaoSelecionada = null;

		do {
			System.out.println("---Menu Principal---");
			System.out.println("1 - Gerenciar Aluno");
			System.out.println("2 - Gerenciar Professor");
			System.out.println("3 - Gerenciar Disciplina");
			System.out.println("4 - Gerenciar Matricula");
			System.out.println("5 - Sair");
			System.out.println("Digite: ");
			opcaoSelecionada = entradaDeDados.nextLine();

			if ("1".equals(opcaoSelecionada)) {
				new AlunoMenu().exibir(entradaDeDados);
			} else if ("2".equals(opcaoSelecionada)) {
				new ProfessorMenu().exibir(entradaDeDados);
			} else if ("3".equals(opcaoSelecionada)) {
				new DisciplinaMenu().exibir(entradaDeDados);
			} else if ("4".equals(opcaoSelecionada)) {
				new MatriculaMenu().exibir(entradaDeDados);
			} else if (!"5".equals(opcaoSelecionada)) {
				System.out.println("Opcao invalida. Tente novamente.");
			}

		} while (!"5".equals(opcaoSelecionada));

		System.out.println("Sistema encerrado.");
		entradaDeDados.close();
	}
}