package br.ce.wcaquino.servicos;

import br.ce.wcaquino.exceptions.NaoPodeDividirPorZeroException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CalculadoraTest {

    private Calculadora calc;

    @Before
    public void setup() {
        calc = new Calculadora();
    }

    @Test
    public void deveSomarDoisValores() {
        //cenario
        int a = 5;
        int b = 3;

        //acao
        int resultado = calc.somar(a, b);

        //verificação
        Assert.assertEquals(8, resultado);
    }

    @Test
    public void deveSubtrairDoisValores() {
        //cenario
        int a = 8;
        int b = 5;

        //ação
        int resultado = calc.subtrair(a, b);

        //verificação
        Assert.assertEquals(3, resultado);
    }

    @Test
    public void deveDividirDoisValores() throws NaoPodeDividirPorZeroException {
        //cenario
        int a = 6;
        int b = 3;

        //ação
        int resultado = calc.divide(a, b);

        //verificação
        Assert.assertEquals(2, resultado);
    }

    @Test(expected = NaoPodeDividirPorZeroException.class)
    public void deveLancarExcecaoAoDividirPorZero() throws NaoPodeDividirPorZeroException {
        int a = 10;
        int b = 0;

        calc.divide(a, b);
    }

    @Test
    public void deveMultiplicarDoisValores() {
        int a = 5;
        int b = 2;

        int resultado = calc.multiplica(a, b);

        Assert.assertEquals(10, resultado);
    }
}
