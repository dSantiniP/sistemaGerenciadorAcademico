package br.com.fuctura.menu;

import java.util.List;
import java.util.Scanner;

import br.com.fuctura.entity.Matricula;
import br.com.fuctura.service.MatriculaService;

public class MatriculaMenu extends MenuCrud {

	private final MatriculaService service = new MatriculaService();

	@Override
	protected String getTitulo() {
		return "Matricula";
	}

	@Override
	protected void cadastrar(Scanner entradaDeDados) {
		// o id e gerado pelo banco e a data e preenchida pelo service
		System.out.println("Digite a matricula do aluno:");
		String alunoMatricula = entradaDeDados.nextLine();
		System.out.println("Digite o codigo da disciplina:");
		String disciplinaCodigo = entradaDeDados.nextLine();

		Matricula matricula = new Matricula();
		matricula.setAlunoMatricula(alunoMatricula);
		matricula.setDisciplinaCodigo(disciplinaCodigo);

		service.salvar(matricula);
		System.out.println("Matricula realizada com sucesso!");
	}

	@Override
	protected void listar() {
		List<Matricula> matriculas = service.listarTodos();

		if (matriculas.isEmpty()) {
			System.out.println("Nenhuma matricula cadastrada.");
		}

		for (Matricula matricula : matriculas) {
			System.out.println("Id: " + matricula.getId());
			System.out.println("Data: " + matricula.getDataMatricula());
			System.out.println("Aluno: " + matricula.getAlunoMatricula());
			System.out.println("Disciplina: " + matricula.getDisciplinaCodigo());
			System.out.println("-----");
		}
	}

	@Override
	protected void excluir(Scanner entradaDeDados) {
		System.out.println("Digite o id da matricula: ");
		String idDigitado = entradaDeDados.nextLine();

		try {
			service.excluir(Integer.parseInt(idDigitado.trim()));
			System.out.println("Matricula excluida com sucesso!");
		} catch (NumberFormatException e) {
			System.out.println("Id invalido. Digite apenas numeros.");
		}
	}
}