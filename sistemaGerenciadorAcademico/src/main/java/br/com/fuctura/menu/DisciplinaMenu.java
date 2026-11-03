package br.com.fuctura.menu;

import java.util.List;
import java.util.Scanner;

import br.com.fuctura.entity.Disciplina;
import br.com.fuctura.service.DisciplinaService;

public class DisciplinaMenu extends MenuCrud {

	private final DisciplinaService service = new DisciplinaService();

	@Override
	protected String getTitulo() {
		return "Disciplina";
	}

	@Override
	protected void cadastrar(Scanner entradaDeDados) {
		System.out.println("Digite codigo:");
		String codigo = entradaDeDados.nextLine();
		System.out.println("Digite nome:");
		String nome = entradaDeDados.nextLine();
		System.out.println("Digite carga horaria:");
		String cargaDigitada = entradaDeDados.nextLine();

		try {
			Disciplina disciplina = new Disciplina();
			disciplina.setCodigo(codigo);
			disciplina.setNome(nome);
			disciplina.setCargaHoraria(Integer.parseInt(cargaDigitada.trim()));

			service.salvar(disciplina);
			System.out.println("Disciplina cadastrada com sucesso!");
		} catch (NumberFormatException e) {
			System.out.println("Carga horaria invalida. Digite apenas numeros.");
		}
	}

	@Override
	protected void listar() {
		List<Disciplina> disciplinas = service.listarTodos();

		if (disciplinas.isEmpty()) {
			System.out.println("Nenhuma disciplina cadastrada.");
		}

		for (Disciplina disciplina : disciplinas) {
			System.out.println("Codigo: " + disciplina.getCodigo());
			System.out.println("Nome: " + disciplina.getNome());
			System.out.println("Carga horaria: " + disciplina.getCargaHoraria());
			System.out.println("-----");
		}
	}

	@Override
	protected void excluir(Scanner entradaDeDados) {
		System.out.println("Digite o codigo: ");
		String codigo = entradaDeDados.nextLine();

		service.excluir(codigo);
		System.out.println("Disciplina excluida com sucesso!");
	}
}