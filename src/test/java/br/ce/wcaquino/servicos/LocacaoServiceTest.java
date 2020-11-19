package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

import java.util.Date;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoServiceTest {
	@Rule
	public ErrorCollector error = new ErrorCollector();

	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Test
	public void teste() throws Exception {
		// cenario
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 2, 4.0);

		// açao
		Locacao locacao = service.alugarFilme(usuario, filme);

		// verificacao
		error.checkThat(locacao.getValor(), is(equalTo(4.0)));
		error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		error.checkThat(isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), is(true));
	}

	//forma elegante de teste de exceçao
	@Test(expected=Exception.class)
	public void testLocacao_filmeSemEstoque() throws Exception {
		// cenario
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 2, 5.0);

		// açao
		service.alugarFilme(usuario, filme);
	}
	
	//forma robusta do teste de excecao
	@Test
	public void testLocacao_filmeSemEstoque_2(){
		// cenario
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 2, 5.0);

		// açao
		try {
			service.alugarFilme(usuario, filme);
			Assert.fail("Deveria ser lancada uma excecao");
		} catch (Exception e) {
			Assert.assertThat(e.getMessage(), is("Filme sem estoque"));
		}
	}
	
	//forma mais recente de teste de exceçao
	@Test
	public void testLocacao_filmeSemEstoque_3() throws Exception {
		// cenario
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 0, 5.0);
		
		exception.expect(Exception.class);
		exception.expectMessage("Filme sem estoque");

		// açao
		service.alugarFilme(usuario, filme);
	}
}
