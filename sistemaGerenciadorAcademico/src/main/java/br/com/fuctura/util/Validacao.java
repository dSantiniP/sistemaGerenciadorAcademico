package br.com.fuctura.util;

// validacoes de texto usadas por todos os services
public final class Validacao {

	// classe so com metodos estaticos: nao deve ser instanciada
	private Validacao() {
	}

	// tira os espacos das pontas; texto vazio vira null
	public static String limpar(String texto) {
		if (texto == null || texto.isBlank()) {
			return null;
		}
		return texto.trim();
	}

	// codigos e matriculas sao sempre gravados em maiusculas, sem espacos
	public static String limparCodigo(String codigo) {
		String codigoLimpo = limpar(codigo);
		return codigoLimpo == null ? null : codigoLimpo.toUpperCase();
	}

	public static void validarObrigatorio(String valor, String nomeCampo) {
		if (valor == null) {
			throw new IllegalArgumentException("O campo " + nomeCampo + " e obrigatorio.");
		}
	}

	public static void validarTamanhoMaximo(String valor, String nomeCampo, int tamanhoMaximo) {
		if (valor != null && valor.length() > tamanhoMaximo) {
			throw new IllegalArgumentException(
					"O campo " + nomeCampo + " deve ter no maximo " + tamanhoMaximo + " caracteres.");
		}
	}
}