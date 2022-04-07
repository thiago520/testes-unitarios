package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmesSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;
import org.hamcrest.CoreMatchers;
import org.junit.*;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static matchers.MatchersProprios.*;

public class LocacaoServiceTest {

    private LocacaoService service;

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setup() {
        service = new LocacaoService();
    }

    @Test
    public void deveAlugarFilme() throws Exception {
        //nao executa caso o dia for sábado
        Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
        //cenário

        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 5.0));

        //ação
        Locacao locacao = service.alugarFilme(usuario,filmes);

            //verificação
            error.checkThat(locacao.getValor(), CoreMatchers.is(5.0));
            error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()), CoreMatchers.is(true));
            error.checkThat(locacao.getDataLocacao(), ehHoje());
            error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), CoreMatchers.is(true));
            error.checkThat(locacao.getDataRetorno(), ehHojeComDiferencaDias(1));
    }

    @Test(expected = FilmesSemEstoqueException.class)
    public void naoDeveAlugarFilmeSemEstoque() throws Exception {
        //cenário
        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 0, 4.0));

        //ação
        service.alugarFilme(usuario,filmes);

    }

    @Test
    public void naoDeveAlugarFilmeSemUsuario() throws FilmesSemEstoqueException {
        //cenario

        List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 5.0));

        //acao

        try {
            service.alugarFilme(null, filmes);
            Assert.fail();
        } catch (LocadoraException e) {
            Assert.assertThat(e.getMessage(), CoreMatchers.is("Usuario vazio"));
        }
        System.out.println("Forma robusta");

    }

    @Test
    public void naoDeveAlugarFilmeSemFilme() throws LocadoraException, FilmesSemEstoqueException {
        //cenário

        Usuario usuario = new Usuario("Usuario 1");

        exception.expect(LocadoraException.class);
        exception.expectMessage("Filme vazio");

        //ação
        service.alugarFilme(usuario, null);
        System.out.println("Forma Nova");
    }

    @Test
    public void deveDevolverNaSegundaAoAlugarNoSabado() throws LocadoraException, FilmesSemEstoqueException {
        // não executa caso o dia for diferente de sábado
        Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
        //cenario
        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 1, 5.0));

        //acao
        Locacao retorno = service.alugarFilme(usuario, filmes);

        //verificacao
        boolean ehSegunda = DataUtils.verificarDiaSemana(retorno.getDataRetorno(), Calendar.MONDAY);

//       Assert.assertTrue(ehSegunda);
//       Assert.assertThat(retorno.getDataRetorno(), new DiaSemanaMatcher(Calendar.MONDAY));
        Assert.assertThat(retorno.getDataRetorno(), caiEm(Calendar.MONDAY));
        Assert.assertThat(retorno.getDataRetorno(), caiNumaSegunda());
    }
}

