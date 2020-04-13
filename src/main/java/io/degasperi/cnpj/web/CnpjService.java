package io.degasperi.cnpj.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import io.degasperi.cnpj.captcha.CaptchaSolver;
import io.degasperi.cnpj.classes.Cnpj;
import io.degasperi.cnpj.classes.SearchResult;
import io.degasperi.cnpj.classes.Utils;
import io.degasperi.cnpj.exceptions.InscricaoNaoEncontradaException;

public class CnpjService {
	private final CloseableHttpClient httpClient;

	private static final String URL_INICIO = "http://servicos.receita.fazenda.gov.br/Servicos/cnpjreva/Cnpjreva_Solicitacao_CS.asp";
	private static final String URL_CAPTCHA = "https://www.receita.fazenda.gov.br/pessoajuridica/cnpj/cnpjreva/captcha/gerarCaptcha.asp?%d";
	private static final String URL_POST = "http://servicos.receita.fazenda.gov.br/Servicos/cnpjreva/valida.asp";
	private static final String URL_QSA = "http://servicos.receita.fazenda.gov.br/Servicos/cnpjreva/Cnpjreva_qsa.asp";
	private static final String URL_DIRECT = "http://servicos.receita.fazenda.gov.br/Servicos/cnpjreva/Cnpjreva_Vstatus.asp?origem=comprovante&cnpj=%s";

	public CnpjService() {
		try {
			httpClient = CloseableHttpClientFactory.build();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private Cnpj searchFirst(String inscricao)
			throws InscricaoNaoEncontradaException, ClientProtocolException, IOException {
		if (inscricao == null || !inscricao.matches("^[0-9]{14}$"))
			throw new RuntimeException("Informe o CNPJ corretamente (14 números, sem separadores)");
		// início
		final HttpGet httpGetPage = new HttpGet(URL_INICIO);
		try (final CloseableHttpResponse response = httpClient.execute(httpGetPage)) {
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
				throw new RuntimeException(response.getStatusLine().toString());
		}

		// captcha
		final HttpGet httpGetCaptcha = new HttpGet(String.format(URL_CAPTCHA, System.currentTimeMillis()));
		String captchaText = "";
		try (final CloseableHttpResponse response = httpClient.execute(httpGetCaptcha)) {
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
				throw new RuntimeException(response.getStatusLine().toString());

			final byte[] captcha = EntityUtils.toByteArray(response.getEntity());
			captchaText = CaptchaSolver.solve(captcha);
		}

		// enviando
		final HttpPost httpPost = new HttpPost(URL_POST);
		final HttpEntity entity = FormEntityBuilder.create()//
				.add("origem", "comprovante")//
				.add("cnpj", inscricao)//
				.add("txtTexto_captcha_serpro_gov_br", captchaText)//
				.add("search_type", "cnpj")//
				.build();
		httpPost.setEntity(entity);

		String mainPage;
		String qsaPage;

		try (final CloseableHttpResponse response = httpClient.execute(httpPost)) {
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
				throw new RuntimeException(response.getStatusLine().toString());

			mainPage = EntityUtils.toString(response.getEntity());
		}

		// QSA
		final HttpGet httpGetQsa = new HttpGet(URL_QSA);
		try (final CloseableHttpResponse response = httpClient.execute(httpGetQsa)) {
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
				throw new RuntimeException(response.getStatusLine().toString());

			qsaPage = EntityUtils.toString(response.getEntity());
		} catch (Exception e) {
			qsaPage = null;
		}

		return HtmlParser.parse(mainPage, qsaPage);
	}

	// só funciona se já passou pelo captcha antes
	private Cnpj nextSearch(String inscricao)
			throws ClientProtocolException, IOException, InscricaoNaoEncontradaException {
		final HttpGet httpGet = new HttpGet(String.format(URL_DIRECT, inscricao));

		String mainPage;
		String qsaPage;

		try (final CloseableHttpResponse response = httpClient.execute(httpGet)) {
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
				throw new RuntimeException(response.getStatusLine().toString());

			mainPage = EntityUtils.toString(response.getEntity());
		}

		// QSA
		final HttpGet httpGetQsa = new HttpGet(URL_QSA);
		try (final CloseableHttpResponse response = httpClient.execute(httpGetQsa)) {
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
				throw new RuntimeException(response.getStatusLine().toString());

			qsaPage = EntityUtils.toString(response.getEntity());
		} catch (Exception e) {
			qsaPage = null;
		}

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
	public SearchResult search(String inscricao) throws ClientProtocolException, IOException {
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
	public SearchResult searchAll(String root) throws ClientProtocolException, IOException {
		if (root == null || !root.matches("^[0-9]{8}$"))
			throw new RuntimeException("Informe a raiz do CNPJ (os 8 primeiros caracteres)");

		final Map<String, Cnpj> result = new HashMap<>();
		final String matriz = Utils.geraCnpj(root, 1);

		try {
			result.put(matriz, searchFirst(matriz));
		} catch (InscricaoNaoEncontradaException e) {
			return SearchResult.error(e.getMessage());
		}

		for (int i = 2; i < 10000; i++)
			try {
				final String filial = Utils.geraCnpj(root, i);
				result.put(filial, nextSearch(filial));
			} catch (InscricaoNaoEncontradaException e) {
				break;
			}

		return SearchResult.success(result);
	}
}
