package br.com.comida.controller;

import java.net.URI;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.comida.dto.ComidaPagamentoDTO;
import br.com.comida.service.ComidaPagamentoService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
@RequestMapping("/pagamento")
public class ComidaPagamentoController {

	@Autowired
	private ComidaPagamentoService service;

	@GetMapping
	public Page<ComidaPagamentoDTO> listar(@PageableDefault(size = 10) Pageable paginacao) {
		return service.obterTodos(paginacao);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ComidaPagamentoDTO> detalhar(@PathVariable @NotNull Long id) {
		ComidaPagamentoDTO dto = service.obterPorId(id);

		return ResponseEntity.ok(dto);
	}

	@PostMapping
	public ResponseEntity<ComidaPagamentoDTO> cadastrar(@RequestBody @Valid ComidaPagamentoDTO dto,
			UriComponentsBuilder uriBuilder) {
		ComidaPagamentoDTO pagamento = service.criarPagamento(dto);
		URI endereco = uriBuilder.path("/pagamentos/{id}").buildAndExpand(pagamento.getId()).toUri();

		return ResponseEntity.created(endereco).body(pagamento);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ComidaPagamentoDTO> atualizar(@PathVariable @NotNull Long id,
			@RequestBody @Valid ComidaPagamentoDTO dto) {
		ComidaPagamentoDTO atualizado = service.atualizarPagamento(id, dto);
		return ResponseEntity.ok(atualizado);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ComidaPagamentoDTO> remover(@PathVariable @NotNull Long id) {
		service.excluirPagamento(id);
		return ResponseEntity.noContent().build();
	}

//	@PatchMapping("/{id}/confirmar")
//	@CircuitBreaker(name = "atualizaPedido", fallbackMethod = "pagamentoAutorizadoComIntegracaoPendente")
//	public void confirmarPagamento(@PathVariable @NotNull Long id) {
//		service.confirmarPagamento(id);
//	}

	public void pagamentoAutorizadoComIntegracaoPendente(Long id, Exception e) {
		service.alteraStatus(id);
	}

}
