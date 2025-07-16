package br.com.tradeflow.dto;

import lombok.Data;

@Data
public class EnderecoCepDTO {

	private Long id;
	private String cep;
	private String uf;
	private String cidade;
	private String bairro;
	private String logradouro;
	private String servico;
	private String localizacao;
}
