# consultaCnpj
Consulta CNPJ diretamente ao site da Receita.

Retorna todas as informações disponíveis no site, inclusive QSA, Capital Social e um PDF da página para arquivamento.

Também permite que se consulte todas as filiais, informando a raiz do CNPJ (8 primeiros dígitos).

Exemplo de retorno:
<pre>
{
	"status": "SUCCESS",
	"message": "OK",
	"result": {
		"00000000000191": {
			"inscricao": "00.000.000/0001-91",
			"matriz": true,
			"dataAbertura": "1966-08-01",
			"nomeEmpresarial": "BANCO DO BRASIL SA",
			"nomeFantasia": "DIRECAO GERAL",
			"porte": "DEMAIS",
			"atividadePrincipal": {
				"codigo": "64.22-1-00",
				"descricao": "Bancos múltiplos, com carteira comercial"
			},
			"atividadesSecundarias": [{
					"codigo": "64.99-9-99",
					"descricao": "Outras atividades de serviços financeiros não especificadas anteriormente"
				}
			],
			"naturezaJuridica": {
				"codigo": "64.22-1-00",
				"descricao": "Bancos múltiplos, com carteira comercial"
			},
			"endereco": {
				"logradouro": "Q SAUN QUADRA 5 LOTE B TORRES I, II E III",
				"numero": "SN",
				"complemento": "ANDAR 1 A 16 SALA 101 A 1601 ANDAR 1 A 16 SALA 101 A 1601 ANDAR 1 A 16 SALA 101 A 1601",
				"cep": "70.040-912",
				"bairro": "ASA NORTE",
				"municipio": "BRASILIA",
				"uf": "DF"
			},
			"email": "SECEX@BB.COM.BR",
			"telefones": ["(61) 3493-9002"],
			"efr": "UNIÃO",
			"situacaoCadastral": {
				"situacao": "ATIVA",
				"data": "2005-11-03",
				"motivo": ""
			},
			"situacaoEspecial": {
				"situacao": "********",
				"data": null
			},
			"capitalSocial": 67000000000.00,
			"qsa": [{
					"nome": "MARCIO HAMILTON FERREIRA",
					"qualificacao": "10-Diretor"
				}, {
					"nome": "NILSON MARTINIANO MOREIRA",
					"qualificacao": "10-Diretor"
				}, {
					"nome": "WALTER MALIENI JUNIOR",
					"qualificacao": "10-Diretor"
				}, {
					"nome": "CARLOS ALBERTO ARAUJO NETTO",
					"qualificacao": "10-Diretor"
				}, {
					"nome": "ANTONIO MAURICIO MAURANO",
					"qualificacao": "10-Diretor"
				}, {
					"nome": "MARCELO AUGUSTO DUTRA LABUTO",
					"qualificacao": "10-Diretor"
				}, {
					"nome": "ROGERIO MAGNO PANCA",
					"qualificacao": "10-Diretor"
				}, {
					"nome": "SIMAO LUIZ KOVALSKI",
					"qualificacao": "10-Diretor"
				}, {
					"nome": "TARCISIO HUBNER",
					"qualificacao": "10-Diretor"
				}, {
					"nome": "EDUARDO CESAR PASA",
					"qualificacao": "10-Diretor"
				}, {
					"nome": "MARCIO LUIZ MORAL",
					"qualificacao": "10-Diretor"
				}, {
					"nome": "JOSE EDUARDO MOREIRA BERGO",
					"qualificacao": "10-Diretor"
				}, {
					"nome": "ALEXANDRE ALVES DE SOUZA",
					"qualificacao": "10-Diretor"
				}, {
					"nome": "CICERO PRZENDSIUK",
					"qualificacao": "10-Diretor"
				}, {
					"nome": "EDSON ROGERIO DA COSTA",
					"qualificacao": "10-Diretor"
				}, {
					"nome": "FABIANO MACANHAN FONTES",
					"qualificacao": "10-Diretor"
				}, {
					"nome": "FERNANDO FLORENCIO CAMPOS",
					"qualificacao": "10-Diretor"
				}, {
					"nome": "GUSTAVO DE SOUZA FOSSE",
					"qualificacao": "10-Diretor"
				}, {
					"nome": "MARCO TULIO DE OLIVEIRA MENDONCA",
					"qualificacao": "10-Diretor"
				}, {
					"nome": "MARVIO MELO FREITAS",
					"qualificacao": "10-Diretor"
				}, {
					"nome": "REINALDO KAZUFUMI YOKOYAMA",
					"qualificacao": "10-Diretor"
				}, {
					"nome": "JOSE CAETANO DE ANDRADE MINCHILLO",
					"qualificacao": "10-Diretor"
				}, {
					"nome": "CARLOS RENATO BONETTI",
					"qualificacao": "10-Diretor"
				}, {
					"nome": "JOSE EDUARDO PEREIRA FILHO",
					"qualificacao": "10-Diretor"
				}, {
					"nome": "MARCO TULIO MORAES DA COSTA",
					"qualificacao": "10-Diretor"
				}, {
					"nome": "JOAO PINTO RABELO JUNIOR",
					"qualificacao": "10-Diretor"
				}, {
					"nome": "CARLOS HAMILTON VASCONCELOS ARAUJO",
					"qualificacao": "10-Diretor"
				}, {
					"nome": "LUCINEIA POSSAR",
					"qualificacao": "10-Diretor"
				}, {
					"nome": "JOSE RICARDO FAGONDE FORNI",
					"qualificacao": "10-Diretor"
				}, {
					"nome": "MARCOS RENATO COLTRI",
					"qualificacao": "10-Diretor"
				}, {
					"nome": "CARLA NESI",
					"qualificacao": "10-Diretor"
				}, {
					"nome": "BERNARDO DE AZEVEDO SILVA ROTHE",
					"qualificacao": "10-Diretor"
				}, {
					"nome": "ANTONIO GUSTAVO MATOS DO VALE",
					"qualificacao": "10-Diretor"
				}, {
					"nome": "LUIZ CLAUDIO BATISTA",
					"qualificacao": "10-Diretor"
				}, {
					"nome": "JOSE AVELAR MATIAS LOPES",
					"qualificacao": "10-Diretor"
				}, {
					"nome": "ANA PAULA TEIXEIRA DE SOUSA",
					"qualificacao": "10-Diretor"
				}, {
					"nome": "DELANO VALENTIM DE ANDRADE",
					"qualificacao": "10-Diretor"
				}, {
					"nome": "GERSON EDUARDO DE OLIVEIRA",
					"qualificacao": "10-Diretor"
				}, {
					"nome": "RUBEM DE FREITAS NOVAES",
					"qualificacao": "16-Presidente"
				}, {
					"nome": "PAULA LUCIANA VIANA DA SILVA LIMA MAZANEK",
					"qualificacao": "10-Diretor"
				}, {
					"nome": "CAMILO BUZZI",
					"qualificacao": "10-Diretor"
				}, {
					"nome": "CARLOS MOTTA DOS SANTOS",
					"qualificacao": "10-Diretor"
				}, {
					"nome": "FABIO AUGUSTO CANTIZANI BARBOSA",
					"qualificacao": "10-Diretor"
				}, {
					"nome": "IVANDRE MONTIEL DA SILVA",
					"qualificacao": "10-Diretor"
				}, {
					"nome": "RONALDO SIMON FERREIRA",
					"qualificacao": "10-Diretor"
				}, {
					"nome": "ENIO MATHIAS FERREIRA",
					"qualificacao": "10-Diretor"
				}, {
					"nome": "DANIEL ANDRE STIELER",
					"qualificacao": "10-Diretor"
				}, {
					"nome": "MAURICIO NOGUEIRA",
					"qualificacao": "10-Diretor"
				}, {
					"nome": "THOMPSON SOARES PEREIRA CESAR",
					"qualificacao": "10-Diretor"
				}, {
					"nome": "ERIK DA COSTA BREYER",
					"qualificacao": "10-Diretor"
				}, {
					"nome": "WALDERY RODRIGUES JUNIOR",
					"qualificacao": "08-Conselheiro de Administração"
				}, {
					"nome": "DEBORA CRISTINA FONSECA",
					"qualificacao": "08-Conselheiro de Administração"
				}, {
					"nome": "LUIZ SERAFIM SPINOLA SANTOS",
					"qualificacao": "08-Conselheiro de Administração"
				}, {
					"nome": "PAULO ROBERTO EVANGELISTA DE LIMA",
					"qualificacao": "08-Conselheiro de Administração"
				}
			],
			"pdf": "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/63c750KZW5kc3RyZWFtCmVuZG9iago0IDAgb2JqCjw8L0NvbnRlbnRzIDUgMCBSL01lZGlhQm94WzAgMCA1OTUgODQyXS9QYXJlbnQgMiAwIFIvUmVzb3VyY2VzPDwvRm9udDw8L0YxIDYgMCBSL0YyIDcgMCBSPj4+Pi9UcmltQm94WzAgMCA1OTUgODQyXS9UeXBlL1BhZ2U+PgplbmRvYmoKMSAwIG9iago8PC9QYWdlcyAyIDAgUi9UeXBlL0NhdGFsb2c+PgplbmRvYmoKMyAwIG9iago8PC9DcmVhdGlvbkRhdGUoRDoyMDIwMDQxMzE2MjAxOC0wMycwMCcpL01vZERhdGUoRDoyMDIwMDQxMzE2MjAxOC0wMycwMCcpL1Byb2R1Y2VyKGlUZXh0riA3LjEuMTAgqTIwMDAtMjAyMCBpVGV4dCBHcm91cCBOViBcKEFHUEwtdmVyc2lvblwpKT4+CmVuZG9iago3IDAgb2JqCjw8L0Jhc2VGb250L1RpbWVzLUJvbGQvRW5jb2RpbmcvV2luQW5zaUVuY29kaW5nL1N1YnR5cGUvVHlwZTEvVHlwZS9Gb250Pj4KZW5kb2JqCjYgMCBvYmoKPDwvQmFzZUZvbnQvVGltZXMtUm9tYW4vRW5jb2RpbmcvV2luQW5zaUVuY29kaW5nL1N1YnR5cGUvVHlwZTEvVHlwZS9Gb250Pj4KZW5kb2JqCjIgMCBvYmoKPDwvQ291bnQgMS9LaWRzWzQgMCBSXS9UeXBlL1BhZ2VzPj4KZW5kb2JqCnhyZWYKMCA4CjAwMDAwMDAwMDAgNjU1MzUgZiAKMDAwMDAwMjg2NSAwMDAwMCBuIAowMDAwMDAzMjQ3IDAwMDAwIG4gCjAwMDAwMDI5MTAgMDAwMDAgbiAKMDAwMDAwMjcyMyAwMDAwMCBuIAowMDAwMDAwMDE1IDAwMDAwIG4gCjAwMDAwMDMxNTcgMDAwMDAgbiAKMDAwMDAwMzA2OCAwMDAwMCBuIAp0cmFpbGVyCjw8L0lEIFs8NmY2YTg2ZjNhYWU2ZjFkOWEyYWQ4MjUzZTNhNTQ3OWM+PDZmNmE4NmYzYWFlNmYxZDlhMmFkODI1M2UzYTU0NzljPl0vSW5mbyAzIDAgUi9Sb290IDEgMCBSL1NpemUgOD4+CiVpVGV4dC03LjEuMTAKc3RhcnR4cmVmCjMyOTgKJSVFT0YK"
		}
	}
}
</pre>
