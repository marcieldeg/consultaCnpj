package io.degasperi.cnpj.exceptions;

public class InscricaoNaoEncontradaException extends Exception {
	private static final long serialVersionUID = -2115403001908392935L;

	public InscricaoNaoEncontradaException() {
		super();
	}
	
	public InscricaoNaoEncontradaException(String message) {
		super(message);
	}
}
