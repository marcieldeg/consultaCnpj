package io.degasperi.cnpj.classes;

public class Utils {
	public static String geraCnpj(String root, int index) {
		final String cnpjSemDv = root + String.format("%04d", index);

		String result = cnpjSemDv;

		int sm = 0;
		int peso = 2;
		for (int i = 11; i >= 0; i--) {
			int num = result.charAt(i) - 48;
			sm = sm + (num * peso);
			peso++;
			if (peso == 10)
				peso = 2;
		}

		int r = sm % 11;
		if ((r == 0) || (r == 1))
			result = result + '0';
		else
			result = result + (char) ((11 - r) + 48);

		sm = 0;
		peso = 2;
		for (int i = 12; i >= 0; i--) {
			int num = result.charAt(i) - 48;
			sm = sm + (num * peso);
			peso++;
			if (peso == 10)
				peso = 2;
		}

		r = sm % 11;
		if ((r == 0) || (r == 1))
			result = result + '0';
		else
			result = result + (char) ((11 - r) + 48);

		return result;
	}
}
