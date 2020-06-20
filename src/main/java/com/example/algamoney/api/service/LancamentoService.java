package com.example.algamoney.api.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.example.algamoney.api.model.Lancamento;
import com.example.algamoney.api.model.Pessoa;
import com.example.algamoney.api.repository.LancamentoRepository;
import com.example.algamoney.api.repository.PessoaRepository;
import com.example.algamoney.api.service.exception.PessoaInexistenteOuInativaException;

@Service
public class LancamentoService {
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private LancamentoRepository lancamentoRepository;

	/**
	 * Método para Salvar um Lancamento.
	 * @param lancamento a ser salvo
	 * @return Lancamento
	 */
	public Lancamento salvar(Lancamento lancamento) {
		
		Pessoa pessoa = pessoaRepository.findOne(lancamento.getPessoa().getCodigo());
		
		if(pessoa == null || pessoa.isInativo()) {
			throw new PessoaInexistenteOuInativaException();
		}
		
		return lancamentoRepository.save(lancamento);
	}

	/**
	 * Método destinado para autalizar um lançamento
	 *
	 * @param codigo
	 * @param lancamento
	 * @return Lancamento
	 */
	public Lancamento atualizar(Long codigo, Lancamento lancamento) {

		Lancamento lancamentoSalvo = this.buscaLancamentoPeloCodigo(codigo);
		BeanUtils.copyProperties(lancamento, lancamentoSalvo, "codigo"); // caso positivo, o objeto lancamento é copiado

		return lancamentoRepository.save(lancamentoSalvo);
	}

	/**
	 * Método que pesquisa um lancamento pelo código
	 * Caso não encontre, uma excessão é lançada.
	 * @param codigo
	 * @return Lancamento
	 */
	public Lancamento buscaLancamentoPeloCodigo(Long codigo) {
		Lancamento lancamento = lancamentoRepository.findOne(codigo);

		if(lancamento == null) {
			throw new EmptyResultDataAccessException(1);
		}

		return lancamento;
	}
}