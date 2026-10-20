package br.com.conde.permutacoes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Os números que aparecem escritos no README, travados em teste. */
class MedidasTest {

    @Test
    @DisplayName("o gerador bate com o fatorial em todo tamanho")
    void correcao() {
        for (Medidas.Correcao c : Medidas.correcao())
            assertTrue(c.bate(), "n = " + c.n());

        assertEquals(40_320L, Medidas.correcao().get(7).geradas());
    }

    @Test
    @DisplayName("os desarranjos conhecidos: 0, 1, 2, 9, 44, 265, 1854")
    void desarranjosConhecidos() {
        long[] esperados = {0, 1, 2, 9, 44, 265, 1854, 14833, 133496};

        for (Medidas.Desarranjo d : Medidas.desarranjos())
            assertEquals(esperados[d.n() - 1], d.desarranjos(), "n = " + d.n());
    }

    @Test
    @DisplayName("A FRACAO PARA DE MUDAR em n = 6, e fica em 1/e")
    void paraEmSeis() {
        assertEquals(6, Medidas.ondeEstaciona());

        assertEquals(1 / Math.E, Medidas.desarranjo(9).fracao(), 1e-6);

        /* Com 6 ja esta a menos de um milesimo, e com 9 a menos de um milionesimo. */
        assertTrue(Medidas.desarranjo(6).distanciaDeUmSobreE() < 0.001);
        assertTrue(Medidas.desarranjo(9).distanciaDeUmSobreE() < 0.000001);
    }

    @Test
    @DisplayName("A MEDIA DE CICLOS E O HARMONICO, exatamente")
    void mediaEhHarmonico() {
        for (Medidas.Ciclo c : Medidas.ciclos())
            assertTrue(c.batemExato(), "n = " + c.n() + ": " + c.mediaDeCiclos()
                    + " contra " + c.harmonico());

        assertEquals(2.45, Medidas.ciclo(6).mediaDeCiclos(), 1e-12);
    }

    @Test
    @DisplayName("e a distribuicao por numero de ciclos soma n!")
    void distribuicaoFecha() {
        for (Medidas.Ciclo c : Medidas.ciclos()) {
            long soma = 0;

            for (int quantas : c.porQuantidade().values()) soma += quantas;

            assertEquals(Permutacao.fatorial(c.n()), soma, "n = " + c.n());
        }

        /* Com 6 elementos, 120 permutacoes tem um ciclo so: e (n-1)!. */
        assertEquals(120, Medidas.ciclo(6).porQuantidade().get(1));
    }

    @Test
    @DisplayName("A ORDEM MAXIMA NAO CRESCE SEMPRE: fica parada em n = 6")
    void ordemNaoCresceSempre() {
        assertEquals(List.of(6), Medidas.ondeNaoCresceu());

        assertEquals(6, Medidas.ordem(5).maxima());
        assertEquals(6, Medidas.ordem(6).maxima());
        assertEquals(12, Medidas.ordem(7).maxima());
    }

    @Test
    @DisplayName("e os valores de Landau conhecidos: 1, 2, 3, 4, 6, 6, 12, 15, 20")
    void valoresDeLandau() {
        long[] esperados = {1, 2, 3, 4, 6, 6, 12, 15, 20};

        for (Medidas.Ordem o : Medidas.ordem())
            assertEquals(esperados[o.n() - 1], o.maxima(), "n = " + o.n());
    }

    @Test
    @DisplayName("A MEDIA DE INVERSOES E METADE DO MAXIMO, exatamente")
    void mediaEhMetade() {
        for (Medidas.Inversao i : Medidas.inversoes()) {
            assertTrue(i.mediaEhMetade(), "n = " + i.n());

            assertEquals(i.n() * (i.n() - 1) / 2, i.maximo(), "n = " + i.n());
        }

        assertEquals(14.0, Medidas.inversao(8).media(), 1e-12);
    }

    @Test
    @DisplayName("e pares e impares sao metades exatas, de n = 2 em diante")
    void metadesExatas() {
        for (Medidas.Inversao i : Medidas.inversoes()) {
            if (i.n() == 1) {
                assertEquals(1, i.pares());
                assertEquals(0, i.impares());

                continue;
            }

            assertTrue(i.paresIgualImpares(), "n = " + i.n());

            assertEquals(Permutacao.fatorial(i.n()) / 2, i.pares(), "n = " + i.n());
        }
    }
}
