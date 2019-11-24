package com.example.algamoney.api.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.example.algamoney.api.model.Pessoa;
import com.example.algamoney.api.repository.PessoaRepository;

@Service
public class PessoaService {
	
	@Autowired
	PessoaRepository pessoaRepository;
	
	public Pessoa atualizar (Long codigo, Pessoa pessoa) {
		
		Pessoa pessoaSalva = buscaPessoaPeloCodigo(codigo);
		
		BeanUtils.copyProperties(pessoa, pessoaSalva, "codigo"); // caso positivo, o objeto de pessoa é copiado
		
		return pessoaRepository.save(pessoaSalva); // pessoa é salva no BD
	}
	
	public void atualizarPropriedadeAtivo(Long codigo, Boolean ativo) {
		Pessoa pessoaSalva = buscaPessoaPeloCodigo(codigo);
		pessoaSalva.setAtivo(ativo);
		pessoaRepository.save(pessoaSalva);
	}

	private Pessoa buscaPessoaPeloCodigo(Long codigo) {
		
		Pessoa pessoaSalva = pessoaRepository.findOne(codigo); // buscando a pessoa no BD
		
		// Regra - caso a pessoa não seja encontrada, é lançada uma exceção 
		if(pessoaSalva == null) { 
			throw new EmptyResultDataAccessException(1);
		}
		
		return pessoaSalva;
	}
}