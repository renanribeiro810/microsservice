package br.com.comida.dto;

import java.math.BigDecimal;

import br.com.comida.model.Status;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ComidaPagamentoDTO {
	
	private Long id;
	private BigDecimal valor;
	private String nome;
	private String numero;
	private String expiracao;
	private String codigo;
	private Status status;
	private Long formaDePagamentoId;
	private Long pedidoId;

}
