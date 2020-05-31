package com.example.algamoney.api.repository.pessoa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.algamoney.api.model.Pessoa;
import com.example.algamoney.api.repository.filter.PessoaFilter;

public interface PessoaRepositoryQuery {
	public Page<Pessoa> pesquisar(PessoaFilter pessoaFilter, Pageable pageable);
}