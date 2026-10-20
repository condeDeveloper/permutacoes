package br.com.conde.permutacoes;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * A permutação e as coisas que se lê nela.
 *
 * <p>O teste mais importante é o do gerador: se ele repetisse ou pulasse permutação, todas as
 * médias deste repositório sairiam erradas e continuariam parecendo razoáveis.
 */
class PermutacaoTest {

    @Test
    @DisplayName("destino repetido ou fora da faixa nao e permutacao")
    void invalidas() {
        assertThrows(IllegalArgumentException.class, () -> new Permutacao(new int[] {0, 0}));
        assertThrows(IllegalArgumentException.class, () -> new Permutacao(new int[] {0, 2}));
        assertThrows(IllegalArgumentException.class, () -> new Permutacao(new int[] {-1, 0}));
    }

    @Test
    @DisplayName("a identidade tem todos os pontos fixos e um ciclo por elemento")
    void identidade() {
        Permutacao id = Permutacao.identidade(5);

        assertEquals(5, id.pontosFixos());
        assertFalse(id.desarranjo());

        assertEquals(5, id.quantosCiclos());
        assertEquals(1, id.ordem());
        assertEquals(0, id.inversoes());
        assertTrue(id.par());
    }

    @Test
    @DisplayName("O GERADOR DA n! PERMUTACOES, todas diferentes")
    void geradorHonesto() {
        for (int n = 1; n <= 8; n++) {
            Set<String> distintas = new HashSet<>();

            long[] quantas = {0};

            Permutacao.cada(n, p -> {
                quantas[0]++;

                distintas.add(p.toString());
            });

            assertEquals(Permutacao.fatorial(n), quantas[0], n + " elementos");
            assertEquals(quantas[0], distintas.size(), n + " elementos: repetiu");
        }
    }

    @Test
    @DisplayName("os ciclos somam o tamanho, em toda permutacao")
    void ciclosSomam() {
        for (int n = 1; n <= 7; n++)
            Permutacao.cada(n, p -> {
                int soma = 0;

                for (int ciclo : p.ciclos()) soma += ciclo;

                assertEquals(p.tamanho(), soma, p.toString());
            });
    }

    @Test
    @DisplayName("A ORDEM FUNCIONA: aplicar esse tanto de vezes devolve tudo ao lugar")
    void ordemDevolveTudo() {
        for (int passo = 1; passo <= 6; passo++) {
            final int n = passo;

            Permutacao.cada(n, p -> {
                int[] atual = new int[n];

                for (int posicao = 0; posicao < n; posicao++) atual[posicao] = posicao;

                int[] inicial = atual.clone();

                for (long vez = 0; vez < p.ordem(); vez++) atual = p.aplicar(atual);

                assertArrayEquals(inicial, atual, p + " nao voltou em " + p.ordem());
            });
        }
    }

    @Test
    @DisplayName("e nao volta antes: a ordem e o MINIMO")
    void ordemEhMinimo() {
        for (int passo = 2; passo <= 6; passo++) {
            final int n = passo;

            Permutacao.cada(n, p -> {
                if (p.ordem() == 1) return;

                int[] atual = new int[n];

                for (int posicao = 0; posicao < n; posicao++) atual[posicao] = posicao;

                int[] inicial = atual.clone();

                for (long vez = 1; vez < p.ordem(); vez++) {
                    atual = p.aplicar(atual);

                    assertFalse(java.util.Arrays.equals(inicial, atual),
                            p + " voltou cedo, em " + vez);
                }
            });
        }
    }

    @Test
    @DisplayName("o desarranjo e a permutacao sem ponto fixo")
    void desarranjo() {
        assertTrue(new Permutacao(new int[] {1, 0}).desarranjo());
        assertTrue(new Permutacao(new int[] {1, 2, 0}).desarranjo());

        assertFalse(new Permutacao(new int[] {0, 2, 1}).desarranjo());
        assertFalse(Permutacao.identidade(3).desarranjo());
    }

    @Test
    @DisplayName("as inversoes contam os pares fora de ordem")
    void inversoes() {
        assertEquals(0, new Permutacao(new int[] {0, 1, 2}).inversoes());
        assertEquals(3, new Permutacao(new int[] {2, 1, 0}).inversoes());
        assertEquals(1, new Permutacao(new int[] {1, 0, 2}).inversoes());

        /* O maximo e n(n-1)/2, na permutacao ao contrario. */
        for (int n = 1; n <= 7; n++) {
            int[] aoContrario = new int[n];

            for (int posicao = 0; posicao < n; posicao++) aoContrario[posicao] = n - 1 - posicao;

            assertEquals(n * (n - 1) / 2, new Permutacao(aoContrario).inversoes(), "n = " + n);
        }
    }
}
