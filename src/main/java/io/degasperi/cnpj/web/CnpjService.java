package io.degasperi.cnpj.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import io.degasperi.cnpj.captcha.CaptchaSolver;
import io.degasperi.cnpj.classes.Cnpj;
import io.degasperi.cnpj.classes.SearchResult;
import io.degasperi.cnpj.classes.Utils;
import io.degasperi.cnpj.exceptions.InscricaoNaoEncontradaException;

public class CnpjService {
	private Map<String, String> cookies;

	private static final String URL_INICIO = "http://servicos.receita.fazenda.gov.br/Servicos/cnpjreva/Cnpjreva_Solicitacao_CS.asp";
	private static final String URL_CAPTCHA = "https://www.receita.fazenda.gov.br/pessoajuridica/cnpj/cnpjreva/captcha/gerarCaptcha.asp?%d";
	private static final String URL_POST = "http://servicos.receita.fazenda.gov.br/Servicos/cnpjreva/valida.asp";
	private static final String URL_QSA = "http://servicos.receita.fazenda.gov.br/Servicos/cnpjreva/Cnpjreva_qsa.asp";
	private static final String URL_DIRECT = "http://servicos.receita.fazenda.gov.br/Servicos/cnpjreva/Cnpjreva_Vstatus.asp?origem=comprovante&cnpj=%s";

	public CnpjService() {
	}

	private Document getQsa() {
		try {
			return JsoupSSL.connect(URL_QSA).cookies(cookies).get();
		} catch (Exception e) {
			return null;
		}
	}

	private Cnpj searchFirst(String inscricao) throws InscricaoNaoEncontradaException, IOException {
		if (inscricao == null || !inscricao.matches("^[0-9]{14}$"))
			throw new RuntimeException("Informe o CNPJ corretamente (14 números, sem separadores)");

		// início
		cookies = Jsoup.connect(URL_INICIO).execute().cookies();

		// captcha
		final byte[] captcha = JsoupSSL.connect(String.format(URL_CAPTCHA, System.currentTimeMillis())).cookies(cookies)//
				.ignoreContentType(true)//
				.execute()//
				.bodyAsBytes();

		final String captchaText = CaptchaSolver.solve(captcha);

		// enviando
		final Document mainPage = JsoupSSL.connect(URL_POST)//
				.data("origem", "comprovante")//
				.data("cnpj", inscricao)//
				.data("txtTexto_captcha_serpro_gov_br", captchaText)//
				.data("search_type", "cnpj")//
				.cookies(cookies)//
				.post();

		// QSA
		final Document qsaPage = getQsa();

		return HtmlParser.parse(mainPage, qsaPage);
	}

	// só funciona se já passou pelo captcha antes
	private Cnpj searchNext(String inscricao) throws IOException, InscricaoNaoEncontradaException {
		// principal
		final Document mainPage = JsoupSSL.connect(String.format(URL_DIRECT, inscricao)).cookies(cookies).get();

		// QSA
		final Document qsaPage = getQsa();

		return HtmlParser.parse(mainPage, qsaPage);
	}

	/**
	 * Busca dados do CNPJ informado
	 * 
	 * @param inscricao Número do CNPJ (14 caracteres, sem máscara)
	 * @return SearchResult Estrutura com os resultados encontrados
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public SearchResult search(String inscricao) throws IOException {
		try {
			return SearchResult.success(searchFirst(inscricao));
		} catch (InscricaoNaoEncontradaException e) {
			return SearchResult.error(e.getMessage());
		}
	}

	/**
	 * Realiza busca sequencial dos CNPJ's de todas as filiais
	 * 
	 * @param root raiz do CNPJ (8 primeiros caracteres, sem máscara)
	 * @return SearchResult Estrutura com os resultados encontrados
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public SearchResult searchAll(String root) throws IOException {
		try {
			if (root == null || !root.matches("^[0-9]{8}$"))
				throw new RuntimeException("Informe a raiz do CNPJ (os 8 primeiros caracteres)");

			final Map<String, Cnpj> result = new HashMap<>();
			
			// consulta matriz
			final String matriz = Utils.geraCnpj(root, 1);
			try {
				result.put(matriz, searchFirst(matriz));
			} catch (InscricaoNaoEncontradaException e) {
				return SearchResult.error(e.getMessage());
			}

			// consulta filiais
			for (int i = 2; i < 10000; i++)
				try {
					final String filial = Utils.geraCnpj(root, i);
					result.put(filial, searchNext(filial));
				} catch (InscricaoNaoEncontradaException e) {
					break;
				}

			return SearchResult.success(result);
		} catch (Exception e) {
			return SearchResult.error(e.getMessage());
		}
	}
}
