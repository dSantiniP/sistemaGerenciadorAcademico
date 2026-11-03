package br.com.fuctura;

import br.com.fuctura.menu.MenuPrincipal;

public class Aplicacao {

	public static void main(String[] args) {

		try {
			new MenuPrincipal().exibir();
		} catch (RuntimeException e) {
			System.out.println("Ocorreu um erro inesperado e o sistema será encerrado: " + e.getMessage());
		}
	}
}