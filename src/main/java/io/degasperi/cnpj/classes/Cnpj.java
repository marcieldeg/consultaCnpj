package io.degasperi.cnpj.classes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class Cnpj {
	private String inscricao;
	private boolean matriz;
	@JsonSerialize(using = LocalDateSerializer.class)
	private LocalDate dataAbertura;
	private String nomeEmpresarial;
	private String nomeFantasia;
	private String porte;
	private Atividade atividadePrincipal;
	private List<Atividade> atividadesSecundarias = new ArrayList<>();
	private NaturezaJuridica naturezaJuridica;
	private Endereco endereco;
	private String email;
	private List<String> telefones = new ArrayList<>();
	private String efr;
	private SituacaoCadastral situacaoCadastral;
	private SituacaoEspecial situacaoEspecial;
	private BigDecimal capitalSocial;
	private List<Socio> qsa = new ArrayList<>();
	private byte[] pdf;

	public String getInscricao() {
		return inscricao;
	}

	public void setInscricao(String inscricao) {
		this.inscricao = inscricao;
	}

	public boolean isMatriz() {
		return matriz;
	}

	public void setMatriz(boolean matriz) {
		this.matriz = matriz;
	}

	public LocalDate getDataAbertura() {
		return dataAbertura;
	}

	public void setDataAbertura(LocalDate dataAbertura) {
		this.dataAbertura = dataAbertura;
	}

	public String getNomeEmpresarial() {
		return nomeEmpresarial;
	}

	public void setNomeEmpresarial(String nomeEmpresarial) {
		this.nomeEmpresarial = nomeEmpresarial;
	}

	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}

	public String getPorte() {
		return porte;
	}

	public void setPorte(String porte) {
		this.porte = porte;
	}

	public Atividade getAtividadePrincipal() {
		return atividadePrincipal;
	}

	public void setAtividadePrincipal(Atividade atividadePrincipal) {
		this.atividadePrincipal = atividadePrincipal;
	}

	public List<Atividade> getAtividadesSecundarias() {
		return atividadesSecundarias;
	}

	public void setAtividadesSecundarias(List<Atividade> atividadesSecundarias) {
		this.atividadesSecundarias = atividadesSecundarias;
	}

	public NaturezaJuridica getNaturezaJuridica() {
		return naturezaJuridica;
	}

	public void setNaturezaJuridica(NaturezaJuridica naturezaJuridica) {
		this.naturezaJuridica = naturezaJuridica;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<String> getTelefones() {
		return telefones;
	}

	public void setTelefones(List<String> telefones) {
		this.telefones = telefones;
	}

	public String getEfr() {
		return efr;
	}

	public void setEfr(String efr) {
		this.efr = efr;
	}

	public SituacaoCadastral getSituacaoCadastral() {
		return situacaoCadastral;
	}

	public void setSituacaoCadastral(SituacaoCadastral situacaoCadastral) {
		this.situacaoCadastral = situacaoCadastral;
	}

	public SituacaoEspecial getSituacaoEspecial() {
		return situacaoEspecial;
	}

	public void setSituacaoEspecial(SituacaoEspecial situacaoEspecial) {
		this.situacaoEspecial = situacaoEspecial;
	}

	public BigDecimal getCapitalSocial() {
		return capitalSocial;
	}

	public void setCapitalSocial(BigDecimal capitalSocial) {
		this.capitalSocial = capitalSocial;
	}

	public List<Socio> getQsa() {
		return qsa;
	}

	public void setQsa(List<Socio> qsa) {
		this.qsa = qsa;
	}

	public byte[] getPdf() {
		return pdf;
	}

	public void setPdf(byte[] pdf) {
		this.pdf = pdf;
	}

	@Override
	public String toString() {
		return "Cnpj [inscricao=" + inscricao + ", matriz=" + matriz + ", dataAbertura=" + dataAbertura
				+ ", nomeEmpresarial=" + nomeEmpresarial + ", nomeFantasia=" + nomeFantasia + ", porte=" + porte
				+ ", atividadePrincipal=" + atividadePrincipal + ", atividadesSecundarias=" + atividadesSecundarias
				+ ", naturezaJuridica=" + naturezaJuridica + ", endereco=" + endereco + ", email=" + email
				+ ", telefones=" + telefones + ", efr=" + efr + ", situacaoCadastral=" + situacaoCadastral
				+ ", situacaoEspecial=" + situacaoEspecial + ", capitalSocial=" + capitalSocial + ", qsa=" + qsa + "]";
	}

	public static class Atividade {
		private String codigo;
		private String descricao;

		public String getCodigo() {
			return codigo;
		}

		public void setCodigo(String codigo) {
			this.codigo = codigo;
		}

		public String getDescricao() {
			return descricao;
		}

		public void setDescricao(String descricao) {
			this.descricao = descricao;
		}

		@Override
		public String toString() {
			return "Atividade [codigo=" + codigo + ", descricao=" + descricao + "]";
		}
	}

	public static class NaturezaJuridica {
		private String codigo;
		private String descricao;

		public String getCodigo() {
			return codigo;
		}

		public void setCodigo(String codigo) {
			this.codigo = codigo;
		}

		public String getDescricao() {
			return descricao;
		}

		public void setDescricao(String descricao) {
			this.descricao = descricao;
		}

		@Override
		public String toString() {
			return "NaturezaJuridica [codigo=" + codigo + ", descricao=" + descricao + "]";
		}
	}

	public static class Endereco {
		private String logradouro;
		private String numero;
		private String complemento;
		private String cep;
		private String bairro;
		private String municipio;
		private String uf;

		public String getLogradouro() {
			return logradouro;
		}

		public void setLogradouro(String logradouro) {
			this.logradouro = logradouro;
		}

		public String getNumero() {
			return numero;
		}

		public void setNumero(String numero) {
			this.numero = numero;
		}

		public String getComplemento() {
			return complemento;
		}

		public void setComplemento(String complemento) {
			this.complemento = complemento;
		}

		public String getCep() {
			return cep;
		}

		public void setCep(String cep) {
			this.cep = cep;
		}

		public String getBairro() {
			return bairro;
		}

		public void setBairro(String bairro) {
			this.bairro = bairro;
		}

		public String getMunicipio() {
			return municipio;
		}

		public void setMunicipio(String municipio) {
			this.municipio = municipio;
		}

		public String getUf() {
			return uf;
		}

		public void setUf(String uf) {
			this.uf = uf;
		}

		@Override
		public String toString() {
			return "Endereco [logradouro=" + logradouro + ", numero=" + numero + ", complemento=" + complemento
					+ ", cep=" + cep + ", bairro=" + bairro + ", municipio=" + municipio + "]";
		}
	}

	public static class SituacaoCadastral {
		private String situacao;
		@JsonSerialize(using = LocalDateSerializer.class)
		private LocalDate data;
		private String motivo;

		public String getSituacao() {
			return situacao;
		}

		public void setSituacao(String situacao) {
			this.situacao = situacao;
		}

		public LocalDate getData() {
			return data;
		}

		public void setData(LocalDate data) {
			this.data = data;
		}

		public String getMotivo() {
			return motivo;
		}

		public void setMotivo(String motivo) {
			this.motivo = motivo;
		}

		@Override
		public String toString() {
			return "SituacaoCadastral [situacao=" + situacao + ", data=" + data + ", motivo=" + motivo + "]";
		}
	}

	public static class SituacaoEspecial {
		private String situacao;
		@JsonSerialize(using = LocalDateSerializer.class)
		private LocalDate data;

		public String getSituacao() {
			return situacao;
		}

		public void setSituacao(String situacao) {
			this.situacao = situacao;
		}

		public LocalDate getData() {
			return data;
		}

		public void setData(LocalDate data) {
			this.data = data;
		}

		@Override
		public String toString() {
			return "SituacaoEspecial [situacao=" + situacao + ", data=" + data + "]";
		}
	}

	public static class Socio {
		private String nome;
		private String qualificacao;

		public String getNome() {
			return nome;
		}

		public void setNome(String nome) {
			this.nome = nome;
		}

		public String getQualificacao() {
			return qualificacao;
		}

		public void setQualificacao(String qualificacao) {
			this.qualificacao = qualificacao;
		}

		@Override
		public String toString() {
			return "Socio [nome=" + nome + ", qualificacao=" + qualificacao + "]";
		}
	}
}
