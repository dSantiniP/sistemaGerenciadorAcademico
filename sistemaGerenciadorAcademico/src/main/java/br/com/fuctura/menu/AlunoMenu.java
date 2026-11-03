package br.com.fuctura.menu;

import java.util.List;
import java.util.Scanner;

import br.com.fuctura.entity.Aluno;
import br.com.fuctura.service.AlunoService;

public class AlunoMenu extends MenuCrud {

	private final AlunoService service = new AlunoService();

	@Override
	protected String getTitulo() {
		return "Aluno";
	}

	@Override
	protected void cadastrar(Scanner entradaDeDados) {
		// a matricula e gerada pelo banco, por isso nao e pedida
		System.out.println("Digite nome:");
		String nome = entradaDeDados.nextLine();
		System.out.println("Digite idade:");
		String idadeDigitada = entradaDeDados.nextLine();
		System.out.println("Digite celular:");
		String celular = entradaDeDados.nextLine();

		try {
			Aluno aluno = new Aluno();
			aluno.setNome(nome);
			aluno.setIdade(Integer.parseInt(idadeDigitada.trim()));
			aluno.setCelular(celular);

			service.salvar(aluno);
			System.out.println("Aluno cadastrado com sucesso!");
		} catch (NumberFormatException e) {
			System.out.println("Idade invalida. Digite apenas numeros.");
		}
	}

	@Override
	protected void listar() {
		List<Aluno> alunos = service.listarTodos();

		if (alunos.isEmpty()) {
			System.out.println("Nenhum aluno cadastrado.");
		}

		for (Aluno aluno : alunos) {
			System.out.println("Matricula: " + aluno.getMatricula());
			System.out.println("Nome: " + aluno.getNome());
			System.out.println("Idade: " + aluno.getIdade());
			System.out.println("Celular: " + aluno.getCelular());
			System.out.println("-----");
		}
	}

	@Override
	protected void excluir(Scanner entradaDeDados) {
		System.out.println("Digite a matricula: ");
		String matricula = entradaDeDados.nextLine();

		service.excluir(matricula);
		System.out.println("Aluno excluido com sucesso!");
	}
}