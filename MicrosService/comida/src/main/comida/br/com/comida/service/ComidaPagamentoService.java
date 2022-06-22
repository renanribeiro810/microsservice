package br.com.comida.service;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.comida.dto.ComidaPagamentoDTO;
import br.com.comida.http.PedidoClient;
import br.com.comida.model.ComidaPagamento;
import br.com.comida.model.Status;
import br.com.comida.repository.ComidaPagamentoRepository;

@Service
public class ComidaPagamentoService {

	@Autowired
	private ComidaPagamentoRepository repository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private PedidoClient pedido;

	public Page<ComidaPagamentoDTO> obterTodos(Pageable paginacao) {
		return repository.findAll(paginacao).map(p -> modelMapper.map(p, ComidaPagamentoDTO.class));
	}

	public ComidaPagamentoDTO obterPorId(Long id) {
		ComidaPagamento pagamento = repository.findById(id).orElseThrow(() -> new EntityNotFoundException());
		return modelMapper.map(pagamento, ComidaPagamentoDTO.class);
	}

	public ComidaPagamentoDTO criarPagamento(ComidaPagamentoDTO dto) {
		ComidaPagamento pagamento = modelMapper.map(dto, ComidaPagamento.class);
		pagamento.setStatus(Status.CRIADO);
		repository.save(pagamento);

		return modelMapper.map(pagamento, ComidaPagamentoDTO.class);
	}

	public ComidaPagamentoDTO atualizarPagamento(Long id, ComidaPagamentoDTO dto) {
		ComidaPagamento pagamento = modelMapper.map(dto, ComidaPagamento.class);
		pagamento.setId(id);
		pagamento = repository.save(pagamento);
		return modelMapper.map(pagamento, ComidaPagamentoDTO.class);
	}

	public void excluirPagamento(Long id) {
		repository.deleteById(id);
	}

	public void confirmarPagamento(Long id) {
		Optional<ComidaPagamento> pagamento = repository.findById(id);

		if (!pagamento.isPresent()) {
			throw new EntityNotFoundException();
		}

		pagamento.get().setStatus(Status.CONFIRMADO);
		repository.save(pagamento.get());
		pedido.atualizaPagamento(pagamento.get().getPedidoId());
	}

	public void alteraStatus(Long id) {
		Optional<ComidaPagamento> pagamento = repository.findById(id);

		if (!pagamento.isPresent()) {
			throw new EntityNotFoundException();
		}

		pagamento.get().setStatus(Status.CONFIRMADO_SEM_INTEGRACAO);
		repository.save(pagamento.get());
		
	}
}
