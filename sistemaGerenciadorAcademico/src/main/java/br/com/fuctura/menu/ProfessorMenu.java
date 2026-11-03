package br.com.fuctura.menu;

import java.util.List;
import java.util.Scanner;

import br.com.fuctura.entity.Professor;
import br.com.fuctura.service.ProfessorService;

public class ProfessorMenu extends MenuCrud {

	private final ProfessorService service = new ProfessorService();

	@Override
	protected String getTitulo() {
		return "Professor";
	}

	@Override
	protected void cadastrar(Scanner entradaDeDados) {
		System.out.println("Digite matricula:");
		String matricula = entradaDeDados.nextLine();
		System.out.println("Digite nome:");
		String nome = entradaDeDados.nextLine();
		System.out.println("Digite telefone:");
		String telefone = entradaDeDados.nextLine();

		Professor professor = new Professor();
		professor.setMatricula(matricula);
		professor.setNome(nome);
		professor.setTelefone(telefone);

		service.salvar(professor);
		System.out.println("Professor cadastrado com sucesso!");
	}

	@Override
	protected void listar() {
		List<Professor> professores = service.listarTodos();

		if (professores.isEmpty()) {
			System.out.println("Nenhum professor cadastrado.");
		}

		for (Professor professor : professores) {
			System.out.println("Matricula: " + professor.getMatricula());
			System.out.println("Nome: " + professor.getNome());
			System.out.println("Telefone: " + professor.getTelefone());
			System.out.println("-----");
		}
	}

	@Override
	protected void excluir(Scanner entradaDeDados) {
		System.out.println("Digite a matricula: ");
		String matricula = entradaDeDados.nextLine();

		service.excluir(matricula);
		System.out.println("Professor excluido com sucesso!");
	}
}