package br.com.comida.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.comida.model.ComidaPagamento;

public interface ComidaPagamentoRepository extends JpaRepository<ComidaPagamento,Long>{

}
