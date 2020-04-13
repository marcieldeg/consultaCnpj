package io.degasperi.cnpj;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.degasperi.cnpj.classes.SearchResult;
import io.degasperi.cnpj.web.CnpjService;

public class Main {
	public static void main(String[] args) throws Exception {
		if (args.length != 1)
			System.err.println("N�mero incorreto de argumentos");

		SearchResult result;
		if (args[0].length() == 14)
			result = new CnpjService().search(args[0]);
		else if (args[0].length() == 8)
			result = new CnpjService().searchAll(args[0]);
		else {
			System.err.println("Argumento inv�lido");
			return;
		}

		System.out.println(new ObjectMapper().writeValueAsString(result));
	}
}
