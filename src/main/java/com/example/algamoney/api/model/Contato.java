package com.example.algamoney.api.model;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "contato")
public class Contato {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codigo;

	@NotEmpty
	private String nome;

	@Email
	@NotNull
	private String email;

	@NotEmpty
	private String telefone;

	@ManyToOne
	@JoinColumn(name = "codigo_pessoa")
	private Pessoa pessoa;

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Contato contato = (Contato) o;

		if (codigo != null ? !codigo.equals(contato.codigo) : contato.codigo != null) return false;
		if (nome != null ? !nome.equals(contato.nome) : contato.nome != null) return false;
		if (email != null ? !email.equals(contato.email) : contato.email != null) return false;
		if (telefone != null ? !telefone.equals(contato.telefone) : contato.telefone != null) return false;
		return pessoa != null ? pessoa.equals(contato.pessoa) : contato.pessoa == null;
	}

	@Override
	public int hashCode() {
		int result = codigo != null ? codigo.hashCode() : 0;
		result = 31 * result + (nome != null ? nome.hashCode() : 0);
		result = 31 * result + (email != null ? email.hashCode() : 0);
		result = 31 * result + (telefone != null ? telefone.hashCode() : 0);
		result = 31 * result + (pessoa != null ? pessoa.hashCode() : 0);
		return result;
	}
}