package io.degasperi.cnpj.web;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;

import io.degasperi.cnpj.classes.Cnpj;
import io.degasperi.cnpj.classes.Cnpj.Atividade;
import io.degasperi.cnpj.classes.Cnpj.Endereco;
import io.degasperi.cnpj.classes.Cnpj.NaturezaJuridica;
import io.degasperi.cnpj.classes.Cnpj.SituacaoCadastral;
import io.degasperi.cnpj.classes.Cnpj.SituacaoEspecial;
import io.degasperi.cnpj.classes.Cnpj.Socio;
import io.degasperi.cnpj.exceptions.InscricaoNaoEncontradaException;

public class HtmlParser {
	public static Cnpj parse(String mainPage, String qsaPage) throws InscricaoNaoEncontradaException {
		final Cnpj cnpj = new Cnpj();
		parseMainPage(cnpj, mainPage);
		parseQsaPage(cnpj, qsaPage);
		makePdf(cnpj, mainPage);
		return cnpj;
	}

	private static void makePdf(Cnpj cnpj, String mainPage) {
		final Document document = Jsoup.parse(mainPage);
		final Element principal = document.getElementById("principal");

		try (ByteArrayInputStream is = new ByteArrayInputStream(principal.toString().getBytes());
				ByteArrayOutputStream os = new ByteArrayOutputStream()) {
			final ConverterProperties properties = new ConverterProperties();
			properties.setCharset(StandardCharsets.ISO_8859_1.name());
			HtmlConverter.convertToPdf(is, os, properties);
			cnpj.setPdf(os.toByteArray());
		} catch (IOException e) {
			//
		}
	}

	private static void parseMainPage(Cnpj cnpj, String mainPage) throws InscricaoNaoEncontradaException {
		final Document document = Jsoup.parse(mainPage);

		final Elements errors = document.getElementsByClass("alert alert-danger");
		if (!errors.isEmpty())
			throw new InscricaoNaoEncontradaException(errors.get(0).getElementsByTag("p").text());

		final Elements mainTables = document.select("#principal > table:nth-child(1)");
		if (mainTables.isEmpty())
			throw new RuntimeException("Tabela principal não encontrada");
		final Element mainTable = mainTables.get(0);

		final Elements tables = mainTable.select("tbody > tr > td > table");

		Element table = null;
		Elements rows = null;
		Elements cells = null;
		/* tables(0) -> título */

		/* tables(1) -> número / data abertura */
		table = tables.get(1);
		rows = table.getElementsByTag("tr");
		if (rows.size() != 1)
			throw new RuntimeException("Linha 1 em formato inesperado (rows)");
		cells = rows.get(0).getElementsByTag("td");
		if (cells.size() != 3)
			throw new RuntimeException("Linha 1 em formato inesperado (cells)");
		//cells(0) -> número e matriz/filial
		final TitleValues numeroInscr = parseTitleValues(cells.get(0));
		if (!"NÚMERO DE INSCRIÇÃO".equals(numeroInscr.title))
			throw new RuntimeException("Linha 1 / Campo 1 com elemento inesperado (" + numeroInscr.title + ")");
		if (numeroInscr.values.size() != 1)
			throw new RuntimeException("Linha 1 / Campo 1 em formato inesperado");
		final String numeroMatrizFilial = numeroInscr.values.get(0);
		if (numeroMatrizFilial.contains("MATRIZ"))
			cnpj.setMatriz(true);
		cnpj.setInscricao(numeroMatrizFilial.substring(0, 18));
		//cells(1) -> título, ignorado

		//cells(2) -> data da consulta
		final TitleValues dataAbertura = parseTitleValues(cells.get(2));
		if (!"DATA DE ABERTURA".equals(dataAbertura.title))
			throw new RuntimeException("Linha 1 / Campo 3 com elemento inesperado (" + dataAbertura.title + ")");
		if (dataAbertura.values.size() != 1)
			throw new RuntimeException("Linha 1 / Campo 3 em formato inesperado");
		cnpj.setDataAbertura(parseDate(dataAbertura.values.get(0)));

		/* tables(2) -> nome empresarial */
		table = tables.get(2);
		rows = table.getElementsByTag("tr");
		if (rows.size() != 1)
			throw new RuntimeException("Linha 2 em formato inesperado (rows)");
		cells = rows.get(0).getElementsByTag("td");
		if (cells.size() != 1)
			throw new RuntimeException("Linha 2 em formato inesperado (cells)");
		//cells(0) -> nome
		final TitleValues nome = parseTitleValues(cells.get(0));
		if (!"NOME EMPRESARIAL".equals(nome.title))
			throw new RuntimeException("Linha 2 / Campo 1 com elemento inesperado (" + nome.title + ")");
		if (nome.values.size() != 1)
			throw new RuntimeException("Linha 2 / Campo 1 em formato inesperado");
		cnpj.setNomeEmpresarial(nome.values.get(0));

		/* tables(3) -> nome fantasia / porte */
		table = tables.get(3);
		rows = table.getElementsByTag("tr");
		if (rows.size() != 1)
			throw new RuntimeException("Linha 3 em formato inesperado (rows)");
		cells = rows.get(0).getElementsByTag("td");
		if (cells.size() != 3)
			throw new RuntimeException("Linha 3 em formato inesperado (cells)");
		//cells(0) -> nome fantasia
		final TitleValues nomeFantasia = parseTitleValues(cells.get(0));
		if (!"TÍTULO DO ESTABELECIMENTO (NOME DE FANTASIA)".equals(nomeFantasia.title))
			throw new RuntimeException("Linha 3 / Campo 1 com elemento inesperado (" + nomeFantasia.title + ")");
		if (nomeFantasia.values.size() != 1)
			throw new RuntimeException("Linha 3 / Campo 1 em formato inesperado");
		cnpj.setNomeFantasia(nomeFantasia.values.get(0));
		//cells(1) -> espaço
		//cells(2) -> porte
		final TitleValues porte = parseTitleValues(cells.get(2));
		if (!"PORTE".equals(porte.title))
			throw new RuntimeException("Linha 3 / Campo 2 com elemento inesperado (" + porte.title + ")");
		if (porte.values.size() != 1)
			throw new RuntimeException("Linha 3 / Campo 2 em formato inesperado");
		cnpj.setPorte(porte.values.get(0));

		/* tables(4) -> atividade econômica principal */
		table = tables.get(4);
		rows = table.getElementsByTag("tr");
		if (rows.size() != 1)
			throw new RuntimeException("Linha 4 em formato inesperado (rows)");
		cells = rows.get(0).getElementsByTag("td");
		if (cells.size() != 1)
			throw new RuntimeException("Linha 4 em formato inesperado (cells)");
		//cells(0) -> atividade econômica principal
		final TitleValues ativPrinc = parseTitleValues(cells.get(0));
		if (!"CÓDIGO E DESCRIÇÃO DA ATIVIDADE ECONÔMICA PRINCIPAL".equals(ativPrinc.title))
			throw new RuntimeException("Linha 4 / Campo 1 com elemento inesperado (" + ativPrinc.title + ")");
		if (ativPrinc.values.size() != 1)
			throw new RuntimeException("Linha 4 / Campo 1 em formato inesperado");
		cnpj.setAtividadePrincipal(parseAtividade(ativPrinc.values.get(0)));

		/* tables(5) -> atividades secundárias */
		table = tables.get(5);
		rows = table.getElementsByTag("tr");
		if (rows.size() != 1)
			throw new RuntimeException("Linha 5 em formato inesperado (rows)");
		cells = rows.get(0).getElementsByTag("td");
		if (cells.size() != 1)
			throw new RuntimeException("Linha 5 em formato inesperado (cells)");
		//cells(0) -> atividade econômica principal
		final TitleValues ativSec = parseTitleValues(cells.get(0));
		if (!"CÓDIGO E DESCRIÇÃO DAS ATIVIDADES ECONÔMICAS SECUNDÁRIAS".equals(ativSec.title))
			throw new RuntimeException("Linha 5 / Campo 1 com elemento inesperado (" + ativSec.title + ")");
		ativSec.values.forEach(o -> {
			final Atividade atividade = parseAtividade(o);
			if (Objects.nonNull(atividade))
				cnpj.getAtividadesSecundarias().add(atividade);
		});

		/* tables(6) -> natureza jurídica */
		table = tables.get(6);
		rows = table.getElementsByTag("tr");
		if (rows.size() != 1)
			throw new RuntimeException("Linha 6 em formato inesperado (rows)");
		cells = rows.get(0).getElementsByTag("td");
		if (cells.size() != 1)
			throw new RuntimeException("Linha 6 em formato inesperado (cells)");
		//cells(0) -> atividade econômica principal
		final TitleValues natJur = parseTitleValues(cells.get(0));
		if (!"CÓDIGO E DESCRIÇÃO DA NATUREZA JURÍDICA".equals(natJur.title))
			throw new RuntimeException("Linha 6 / Campo 1 com elemento inesperado (" + natJur.title + ")");
		if (natJur.values.size() != 1)
			throw new RuntimeException("Linha 6 / Campo 1 em formato inesperado");
		cnpj.setNaturezaJuridica(parseNatJur(ativPrinc.values.get(0)));

		final Endereco endereco = new Endereco();
		/* tables(7) -> logradouro / número / complemento */
		table = tables.get(7);
		rows = table.getElementsByTag("tr");
		if (rows.size() != 1)
			throw new RuntimeException("Linha 7 em formato inesperado (rows)");
		cells = rows.get(0).getElementsByTag("td");
		if (cells.size() != 5)
			throw new RuntimeException("Linha 7 em formato inesperado (cells)");
		//cells(0) -> logradouro
		final TitleValues logradouro = parseTitleValues(cells.get(0));
		if (!"LOGRADOURO".equals(logradouro.title))
			throw new RuntimeException("Linha 7 / Campo 1 com elemento inesperado (" + logradouro.title + ")");
		if (logradouro.values.size() != 1)
			throw new RuntimeException("Linha 7 / Campo 1 em formato inesperado");
		endereco.setLogradouro(logradouro.values.get(0));
		//cells(1) -> espaço
		//cells(2) -> número
		final TitleValues numero = parseTitleValues(cells.get(2));
		if (!"NÚMERO".equals(numero.title))
			throw new RuntimeException("Linha 7 / Campo 2 com elemento inesperado (" + numero.title + ")");
		if (numero.values.size() != 1)
			throw new RuntimeException("Linha 7 / Campo 2 em formato inesperado");
		endereco.setNumero(numero.values.get(0));
		//cells(3) -> espaço
		//cells(4) -> complemento
		final TitleValues complemento = parseTitleValues(cells.get(4));
		if (!"COMPLEMENTO".equals(complemento.title))
			throw new RuntimeException("Linha 7 / Campo 3 com elemento inesperado (" + complemento.title + ")");
		if (complemento.values.size() != 1)
			throw new RuntimeException("Linha 7 / Campo 3 em formato inesperado");
		endereco.setComplemento(complemento.values.get(0));

		/* tables(8) -> cep / bairro / municipio / uf */
		table = tables.get(8);
		rows = table.getElementsByTag("tr");
		if (rows.size() != 1)
			throw new RuntimeException("Linha 8 em formato inesperado (rows)");
		cells = rows.get(0).getElementsByTag("td");
		if (cells.size() != 7)
			throw new RuntimeException("Linha 8 em formato inesperado (cells)");
		//cells(0) -> cep
		final TitleValues cep = parseTitleValues(cells.get(0));
		if (!"CEP".equals(cep.title))
			throw new RuntimeException("Linha 8 / Campo 1 com elemento inesperado (" + cep.title + ")");
		if (cep.values.size() != 1)
			throw new RuntimeException("Linha 8 / Campo 1 em formato inesperado");
		endereco.setCep(cep.values.get(0));
		//cells(1) -> espaço
		//cells(2) -> bairro
		final TitleValues bairro = parseTitleValues(cells.get(2));
		if (!"BAIRRO/DISTRITO".equals(bairro.title))
			throw new RuntimeException("Linha 8 / Campo 2 com elemento inesperado (" + bairro.title + ")");
		if (bairro.values.size() != 1)
			throw new RuntimeException("Linha 8 / Campo 2 em formato inesperado");
		endereco.setBairro(bairro.values.get(0));
		//cells(3) -> espaço
		//cells(4) -> bairro
		final TitleValues municipio = parseTitleValues(cells.get(4));
		if (!"MUNICÍPIO".equals(municipio.title))
			throw new RuntimeException("Linha 8 / Campo 3 com elemento inesperado (" + municipio.title + ")");
		if (municipio.values.size() != 1)
			throw new RuntimeException("Linha 8 / Campo 3 em formato inesperado");
		endereco.setMunicipio(municipio.values.get(0));
		//cells(5) -> espaço
		//cells(6) -> uf
		final TitleValues estado = parseTitleValues(cells.get(6));
		if (!"UF".equals(estado.title))
			throw new RuntimeException("Linha 8 / Campo 4 com elemento inesperado (" + estado.title + ")");
		if (estado.values.size() != 1)
			throw new RuntimeException("Linha 8 / Campo 4 em formato inesperado");
		endereco.setUf(estado.values.get(0));
		cnpj.setEndereco(endereco);

		/* tables(9) -> email / telefone */
		table = tables.get(9);
		rows = table.getElementsByTag("tr");
		if (rows.size() != 1)
			throw new RuntimeException("Linha 9 em formato inesperado (rows)");
		cells = rows.get(0).getElementsByTag("td");
		if (cells.size() != 3)
			throw new RuntimeException("Linha 9 em formato inesperado (cells)");
		//cells(0) -> email
		final TitleValues email = parseTitleValues(cells.get(0));
		if (!"ENDEREÇO ELETRÔNICO".equals(email.title))
			throw new RuntimeException("Linha 9 / Campo 1 com elemento inesperado (" + email.title + ")");
		if (email.values.size() != 1)
			throw new RuntimeException("Linha 9 / Campo 1 em formato inesperado");
		cnpj.setEmail(email.values.get(0));
		//cells(1) -> espaço
		//cells(2) -> telefone
		final TitleValues telefone = parseTitleValues(cells.get(2));
		if (!"TELEFONE".equals(telefone.title))
			throw new RuntimeException("Linha 9 / Campo 2 com elemento inesperado (" + telefone.title + ")");
		if (telefone.values.size() != 1)
			throw new RuntimeException("Linha 9 / Campo 2 em formato inesperado");
		final String tels = telefone.values.get(0).trim();
		if (tels != null && !"".equals(tels))
			cnpj.setTelefones(Arrays.asList(tels.split("/")).stream().map(String::trim).collect(Collectors.toList()));

		/* tables(10) -> efr */
		table = tables.get(10);
		rows = table.getElementsByTag("tr");
		if (rows.size() != 1)
			throw new RuntimeException("Linha 10 em formato inesperado (rows)");
		cells = rows.get(0).getElementsByTag("td");
		if (cells.size() != 1)
			throw new RuntimeException("Linha 10 em formato inesperado (cells)");
		//cells(0) -> efr
		final TitleValues responsavel = parseTitleValues(cells.get(0));
		if (!"ENTE FEDERATIVO RESPONSÁVEL (EFR)".equals(responsavel.title))
			throw new RuntimeException("Linha 10 / Campo 1 com elemento inesperado (" + responsavel.title + ")");
		if (responsavel.values.size() != 1)
			throw new RuntimeException("Linha 10 / Campo 1 em formato inesperado");
		cnpj.setEfr(responsavel.values.get(0));

		final SituacaoCadastral situacaoCadastral = new SituacaoCadastral();
		/* tables(11) -> situação cadastral e data */
		table = tables.get(11);
		rows = table.getElementsByTag("tr");
		if (rows.size() != 1)
			throw new RuntimeException("Linha 11 em formato inesperado (rows)");
		cells = rows.get(0).getElementsByTag("td");
		if (cells.size() != 3)
			throw new RuntimeException("Linha 11 em formato inesperado (cells)");
		//cells(0) -> situação cadastral
		final TitleValues sitCadastral = parseTitleValues(cells.get(0));
		if (!"SITUAÇÃO CADASTRAL".equals(sitCadastral.title))
			throw new RuntimeException("Linha 11 / Campo 1 com elemento inesperado (" + sitCadastral.title + ")");
		if (sitCadastral.values.size() != 1)
			throw new RuntimeException("Linha 11 / Campo 1 em formato inesperado");
		situacaoCadastral.setSituacao(sitCadastral.values.get(0));
		//cells(1) -> espaço
		//cells(2) -> data
		final TitleValues dataSitCad = parseTitleValues(cells.get(2));
		if (!"DATA DA SITUAÇÃO CADASTRAL".equals(dataSitCad.title))
			throw new RuntimeException("Linha 11 / Campo 2 com elemento inesperado (" + dataSitCad.title + ")");
		if (dataSitCad.values.size() != 1)
			throw new RuntimeException("Linha 11 / Campo 2 em formato inesperado");
		situacaoCadastral.setData(parseDate(dataSitCad.values.get(0)));

		/* tables(12) -> motivo da situação cadastral */
		table = tables.get(12);
		rows = table.getElementsByTag("tr");
		if (rows.size() != 1)
			throw new RuntimeException("Linha 12 em formato inesperado (rows)");
		cells = rows.get(0).getElementsByTag("td");
		if (cells.size() != 1)
			throw new RuntimeException("Linha 12 em formato inesperado (cells)");
		//cells(0) -> situação cadastral
		final TitleValues motSitCad = parseTitleValues(cells.get(0));
		if (!"MOTIVO DE SITUAÇÃO CADASTRAL".equals(motSitCad.title))
			throw new RuntimeException("Linha 12 / Campo 1 com elemento inesperado (" + motSitCad.title + ")");
		if (motSitCad.values.size() != 1)
			throw new RuntimeException("Linha 12 / Campo 1 em formato inesperado");
		situacaoCadastral.setMotivo(motSitCad.values.get(0));
		cnpj.setSituacaoCadastral(situacaoCadastral);

		final SituacaoEspecial situacaoEspecial = new SituacaoEspecial();
		/* tables(13) -> situação cadastral e data */
		table = tables.get(13);
		rows = table.getElementsByTag("tr");
		if (rows.size() != 1)
			throw new RuntimeException("Linha 13 em formato inesperado (rows)");
		cells = rows.get(0).getElementsByTag("td");
		if (cells.size() != 3)
			throw new RuntimeException("Linha 13 em formato inesperado (cells)");
		//cells(0) -> situação cadastral
		final TitleValues sitEspecial = parseTitleValues(cells.get(0));
		if (!"SITUAÇÃO ESPECIAL".equals(sitEspecial.title))
			throw new RuntimeException("Linha 13 / Campo 1 com elemento inesperado (" + sitEspecial.title + ")");
		if (sitEspecial.values.size() != 1)
			throw new RuntimeException("Linha 13 / Campo 1 em formato inesperado");
		situacaoEspecial.setSituacao(sitEspecial.values.get(0));
		//cells(1) -> espaço
		//cells(2) -> data
		final TitleValues dataSitEsp = parseTitleValues(cells.get(2));
		if (!"DATA DA SITUAÇÃO ESPECIAL".equals(dataSitEsp.title))
			throw new RuntimeException("Linha 13 / Campo 2 com elemento inesperado (" + dataSitEsp.title + ")");
		if (dataSitEsp.values.size() != 1)
			throw new RuntimeException("Linha 13 / Campo 2 em formato inesperado");
		situacaoEspecial.setData(parseDate(dataSitEsp.values.get(0)));
		cnpj.setSituacaoEspecial(situacaoEspecial);
	}

	private static void parseQsaPage(Cnpj cnpj, String qsaPage) {
		if (qsaPage == null || "".equals(qsaPage))
			return;

		final Document document = Jsoup.parse(qsaPage);

		// capital social
		final Element capital = document.getElementById("capital");
		Elements children = capital.children();
		if (children.size() != 3)
			throw new RuntimeException("Capital social em formato inesperado (1)");
		children = children.get(2).children();
		if (children.size() != 2)
			throw new RuntimeException("Capital social em formato inesperado (2)");
		if (!"CAPITAL SOCIAL:".equals(children.get(0).text()))
			throw new RuntimeException("Capital social em formato inesperado (3)");
		final String capitalSocial = children.get(1).text().replaceAll("\\([^\\)+]\\)", "").replace("R$", "").trim();
		cnpj.setCapitalSocial(parseBigDecimal(capitalSocial));

		// sócios
		final Elements socios = document.getElementsByClass("alert alert-warning");
		if (socios.isEmpty())
			return;
		socios.forEach(o -> {
			final Socio socio = parseSocio(o);
			if (Objects.nonNull(socio))
				cnpj.getQsa().add(socio);
		});
	}

	private static LocalDate parseDate(String text) {
		if ("********".equals(text))
			return null;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		return LocalDate.parse(text, formatter);
	}

	private static BigDecimal parseBigDecimal(String text) {
		final DecimalFormat df = (DecimalFormat) NumberFormat.getInstance(new Locale("pt", "BR"));
		df.setParseBigDecimal(true);
		try {
			return (BigDecimal) df.parseObject(text);
		} catch (ParseException e) {
			return null;
		}
	}

	private static Atividade parseAtividade(String text) {
		if (Arrays.asList("Não informada", "********").contains(text))
			return null;
		final Atividade atividade = new Atividade();
		final String[] values = text.split(" - ");
		atividade.setCodigo(values[0]);
		atividade.setDescricao(values[1]);
		return atividade;
	}

	private static NaturezaJuridica parseNatJur(String text) {
		if (Arrays.asList("Não informada", "********").contains(text))
			return null;
		final NaturezaJuridica naturezaJuridica = new NaturezaJuridica();
		final String[] values = text.split(" - ");
		naturezaJuridica.setCodigo(values[0]);
		naturezaJuridica.setDescricao(values[1]);
		return naturezaJuridica;
	}

	private static Socio parseSocio(Element element) {
		final Socio socio = new Socio();
		final Elements children = element.children();
		if (children.size() == 1
				&& "NÃO HÁ INFORMAÇÃO DE QUADRO DE SÓCIOS E ADMINISTRADORES (QSA) NA BASE DE DADOS DO CNPJ"
						.equals(children.get(0).text()))
			return null;
		if (children.size() != 3)
			throw new RuntimeException("Socio em formato inesperado (1)");

		final Elements nome = children.get(0).children();
		if (!"Nome/Nome Empresarial:".equals(nome.get(0).text()))
			throw new RuntimeException("Campo inesperado (" + nome.get(0).text() + ")");
		socio.setNome(nome.get(1).text());

		final Elements qualif = children.get(1).children();
		if (!"Qualificação:".equals(qualif.get(0).text()))
			throw new RuntimeException("Campo inesperado (" + qualif.get(0).text() + ")");
		socio.setQualificacao(qualif.get(1).text());
		return socio;
	}

	private static TitleValues parseTitleValues(Element element) {
		final TitleValues titleValues = new TitleValues();
		final Elements texts = element.getElementsByTag("font");
		titleValues.title = texts.get(0).text();
		for (int i = 1; i < texts.size(); i++)
			titleValues.values.add(texts.get(i).text());
		return titleValues;
	}

	private static class TitleValues {
		private String title;
		private List<String> values = new ArrayList<>();
	}
}
