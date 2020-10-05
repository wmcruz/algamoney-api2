package com.example.algamoney.api.service;

import com.example.algamoney.api.dto.LancamentoEstatisticaPessoa;
import com.example.algamoney.api.mail.Mailer;
import com.example.algamoney.api.model.Lancamento;
import com.example.algamoney.api.model.Pessoa;
import com.example.algamoney.api.model.Usuario;
import com.example.algamoney.api.repository.LancamentoRepository;
import com.example.algamoney.api.repository.PessoaRepository;
import com.example.algamoney.api.repository.UsuarioRepository;
import com.example.algamoney.api.service.exception.PessoaInexistenteOuInativaException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class LancamentoService {

	private static final String DESTINATARIOS = "ROLE_PESQUISAR_LANCAMENTO";

	private static final Logger logger = LoggerFactory.getLogger(LancamentoService.class);
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private LancamentoRepository lancamentoRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private Mailer mailer;

	// @Scheduled(fixedDelay = 1000 * 2)
	// @Scheduled(cron = "40 55 22 * * *")
	// @Scheduled(fixedDelay = 1000 * 60 * 30)
	public void avisarSobreLancamentosVencidos() {
		if (logger.isDebugEnabled()) {
			logger.debug("## Preparando o envio de e-mails de aviso de lançamentos vencidos");
		}

		List<Lancamento> vencidos = lancamentoRepository.findByDataVencimentoLessThanEqualAndDataPagamentoIsNull(LocalDate.now());

		if (vencidos.isEmpty()) {
			logger.info("## Sem lançamentos vencidos para aviso.");

			return;
		}

		logger.info("## Existem {} lançamentos vencidos.", vencidos.size());

		List<Usuario> destinatarios = usuarioRepository.findByPermissoesDescricao(DESTINATARIOS);

		if (destinatarios.isEmpty()) {
			logger.warn("## Existem lançamentos vencidos, mas o sistema não encontrou destinatários.");

			return;
		}

		mailer.avisarLancamentosVencidos(vencidos, destinatarios);

		logger.info("## Envio de e-mail de aviso concluído.");
	}

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

	public  byte[] relatorioPorPessoa(LocalDate inicio, LocalDate fim) throws Exception {
		List<LancamentoEstatisticaPessoa> dados = lancamentoRepository.porPessoa(inicio, fim);

		Map<String, Object> parametros = new HashMap<>();
		parametros.put("DT_INICIO", Date.valueOf(inicio));
		parametros.put("DT_FIM", Date.valueOf(fim));
		parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));

		InputStream inputStream = this.getClass().getResourceAsStream(
				"/relatorios/lancamentos-por-pessoa.jasper");

		JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parametros, new JRBeanCollectionDataSource(dados));

		return JasperExportManager.exportReportToPdf(jasperPrint);
	}
}