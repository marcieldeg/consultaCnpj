# consultaCnpj
Consulta CNPJ diretamente ao site da Receita.

Exemplo de retorno:
`
{
	"status": "SUCCESS",
	"message": "OK",
	"result": {
		"02880511000251": {
			"inscricao": "02.880.511/0002-51",
			"matriz": false,
			"dataAbertura": "2009-04-23",
			"nomeEmpresarial": "COMERCIAL YOHANA LTDA",
			"nomeFantasia": "********",
			"porte": "ME",
			"atividadePrincipal": null,
			"atividadesSecundarias": [],
			"naturezaJuridica": null,
			"endereco": {
				"logradouro": "********",
				"numero": "********",
				"complemento": "********",
				"cep": "********",
				"bairro": "********",
				"municipio": "********",
				"uf": "********"
			},
			"email": "nastel@nastel.com.br",
			"telefones": ["(47) 3339-4988", "(47) 9136-0000"],
			"efr": "*****",
			"situacaoCadastral": {
				"situacao": "BAIXADA",
				"data": "2016-01-28",
				"motivo": "EXTINCAO P/ ENC LIQ VOLUNTARIA"
			},
			"situacaoEspecial": {
				"situacao": "********",
				"data": null
			},
			"capitalSocial": 5000.00,
			"qsa": [{
					"nome": "ARLETE GUSAVA",
					"qualificacao": "49-Sócio-Administrador"
				}, {
					"nome": "DOUGLAS MICHAEL GIRARDI",
					"qualificacao": "49-Sócio-Administrador"
				}
			],
			"pdf": "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YmoKPDwvUGFnZXMgMiAwIFIvVHlwZS9DYXRhbG9nPj4KZW5kb2JqCjMgMCBvYmoKPDwvQ3JlYXRpb25EYXRlKEQ6MjAyMDA0MTMxNjAxNTEtMDMnMDAnKS9Nb2REYXRlKEQ6MjAyMDA0MTMxNjAxNTEtMDMnMDAnKS9Qcm9kdWNlcihpVGV4dK4gNy4xLjEwIKkyMDAwLTIwMjAgaVRleHQgR3JvdXAgTlYgXChBR1BMLXZlcnNpb25cKSk+PgplbmRvYmoKNyAwIG9iago8PC9CYXNlRm9udC9UaW1lcy1Cb2xkL0VuY29kaW5nL1dpbkFuc2lFbmNvZGluZy9TdWJ0eXBlL1R5cGUxL1R5cGUvRm9udD4+CmVuZG9iago2IDAgb2JqCjw8L0Jhc2VGb250L1RpbWVzLVJvbWFuL0VuY29kaW5nL1dpbkFuc2lFbmNvZGluZy9TdWJ0eXBlL1R5cGUxL1R5cGUvRm9udD4+CmVuZG9iagoyIDAgb2JqCjw8L0NvdW50IDEvS2lkc1s0IDAgUl0vVHlwZS9QYWdlcz4+CmVuZG9iagp4cmVmCjAgOAowMDAwMDAwMDAwIDY1NTM1IGYgCjAwMDAwMDI2OTUgMDAwMDAgbiAKMDAwMDAwMzA3NyAwMDAwMCBuIAowMDAwMDAyNzQwIDAwMDAwIG4gCjAwMDAwMDI1NTMgMDAwMDAgbiAKMDAwMDAwMDAxNSAwMDAwMCBuIAowMDAwMDAyOTg3IDAwMDAwIG4gCjAwMDAwMDI4OTggMDAwMDAgbiAKdHJhaWxlcgo8PC9JRCBbPGE2OGYxMDc4Y2MxOGZmZWRmMzVjOWI1MWVjYjlhOTljPjxhNjhmMTA3OGNjMThmZmVkZjM1YzliNTFlY2I5YTk5Yz5dL0luZm8gMyAwIFIvUm9vdCAxIDAgUi9TaXplIDg+PgolaVRleHQtNy4xLjEwCnN0YXJ0eHJlZgozMTI4CiUlRU9GCg=="
		}
	}
}
`
