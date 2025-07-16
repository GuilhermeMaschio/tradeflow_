package br.com.tradeflow.dto;

import lombok.Data;

@Data
public class ParametroDTO {

	private Long id;
	private String ambiente;
	private String chave;
	private String valor;
}
