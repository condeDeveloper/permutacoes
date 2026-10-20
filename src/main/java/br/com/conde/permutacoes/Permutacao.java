package br.com.conde.permutacoes;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * UMA PERMUTAÇÃO: para onde cada posição vai.
 *
 * <p>Guardada como um vetor onde {@code vai[i]} diz o destino do elemento {@code i}. É a mesma
 * coisa que um sorteio de amigo oculto, uma ordem de cartas embaralhadas, ou uma maneira de
 * renomear n coisas.
 *
 * <p>Tudo que este repositório mede é enumerado: todas as {@code n!} permutações de cada
 * tamanho, até onde o fatorial deixa. Com 9 elementos são 362.880, e nada é sorteado.
 */
public record Permutacao(int[] vai) {

    public Permutacao {
        vai = vai.clone();

        boolean[] visto = new boolean[vai.length];

        for (int valor : vai) {
            if (valor < 0 || valor >= vai.length)
                throw new IllegalArgumentException("destino fora da faixa: " + valor);

            if (visto[valor]) throw new IllegalArgumentException("destino repetido: " + valor);

            visto[valor] = true;
        }
    }

    public int tamanho() {
        return vai.length;
    }

    public int destinoDe(int posicao) {
        return vai[posicao];
    }

    /** A identidade: cada um fica onde está. */
    public static Permutacao identidade(int tamanho) {
        int[] vai = new int[tamanho];

        for (int posicao = 0; posicao < tamanho; posicao++) vai[posicao] = posicao;

        return new Permutacao(vai);
    }

    /**
     * UM PONTO FIXO: alguém que foi parar no próprio lugar.
     *
     * <p>No amigo oculto, é quem tirou o próprio nome.
     */
    public int pontosFixos() {
        int quantos = 0;

        for (int posicao = 0; posicao < vai.length; posicao++)
            if (vai[posicao] == posicao) quantos++;

        return quantos;
    }

    /** Um desarranjo é uma permutação sem nenhum ponto fixo. */
    public boolean desarranjo() {
        return pontosFixos() == 0;
    }

    /**
     * OS CICLOS: seguindo para onde cada um vai, sempre se volta ao começo.
     *
     * <p>Num conjunto finito, seguir destinos acaba repetindo, e o primeiro repetido é o
     * próprio começo, porque cada elemento tem um antecessor só. Então toda permutação é um
     * conjunto de ciclos, sem exceção.
     *
     * <p>Devolve o tamanho de cada ciclo, em ordem decrescente.
     */
    public int[] ciclos() {
        boolean[] visto = new boolean[vai.length];

        List<Integer> tamanhos = new ArrayList<>();

        for (int inicio = 0; inicio < vai.length; inicio++) {
            if (visto[inicio]) continue;

            int tamanho = 0;
            int atual = inicio;

            while (!visto[atual]) {
                visto[atual] = true;

                atual = vai[atual];

                tamanho++;
            }

            tamanhos.add(tamanho);
        }

        tamanhos.sort((a, b) -> Integer.compare(b, a));

        int[] saida = new int[tamanhos.size()];

        for (int indice = 0; indice < saida.length; indice++) saida[indice] = tamanhos.get(indice);

        return saida;
    }

    public int quantosCiclos() {
        return ciclos().length;
    }

    /**
     * A ORDEM: quantas vezes aplicar até tudo voltar ao lugar.
     *
     * <p>É o mínimo múltiplo comum dos tamanhos dos ciclos, porque cada ciclo volta ao começo
     * no seu próprio tamanho, e tudo volta junto quando todos voltam.
     *
     * <p>A quarta medida mostra que esse número cresce muito mais rápido do que se espera: com
     * 9 elementos já existe permutação de ordem 20.
     */
    public long ordem() {
        long resultado = 1;

        for (int ciclo : ciclos()) resultado = mmc(resultado, ciclo);

        return resultado;
    }

    private static long mmc(long a, long b) {
        return a / mdc(a, b) * b;
    }

    private static long mdc(long a, long b) {
        while (b != 0) {
            long resto = a % b;

            a = b;
            b = resto;
        }

        return a;
    }

    /**
     * AS INVERSÕES: pares que estão fora de ordem.
     *
     * <p>É quantas trocas de vizinhos seriam necessárias para ordenar, e é também o número de
     * comparações que a ordenação por inserção faz além do mínimo.
     */
    public int inversoes() {
        int quantas = 0;

        for (int a = 0; a < vai.length; a++)
            for (int b = a + 1; b < vai.length; b++)
                if (vai[a] > vai[b]) quantas++;

        return quantas;
    }

    /** Par é quando o número de inversões é par. É o sinal da permutação. */
    public boolean par() {
        return inversoes() % 2 == 0;
    }

    /** Aplica a permutação uma vez num vetor. */
    public int[] aplicar(int[] entrada) {
        int[] saida = new int[entrada.length];

        for (int posicao = 0; posicao < entrada.length; posicao++)
            saida[vai[posicao]] = entrada[posicao];

        return saida;
    }

    @Override
    public String toString() {
        StringBuilder saida = new StringBuilder();

        for (int valor : vai) saida.append(valor);

        return saida.toString();
    }

    /** Todas as permutações de n elementos, geradas uma a uma. */
    public static void cada(int tamanho, Consumer<Permutacao> aoAchar) {
        int[] atual = new int[tamanho];

        for (int posicao = 0; posicao < tamanho; posicao++) atual[posicao] = posicao;

        gerar(atual, 0, aoAchar);
    }

    private static void gerar(int[] atual, int posicao, Consumer<Permutacao> aoAchar) {
        if (posicao == atual.length) {
            aoAchar.accept(new Permutacao(atual));

            return;
        }

        for (int troca = posicao; troca < atual.length; troca++) {
            int guarda = atual[posicao];

            atual[posicao] = atual[troca];
            atual[troca] = guarda;

            gerar(atual, posicao + 1, aoAchar);

            guarda = atual[posicao];

            atual[posicao] = atual[troca];
            atual[troca] = guarda;
        }
    }

    public static List<Permutacao> todas(int tamanho) {
        List<Permutacao> saida = new ArrayList<>();

        cada(tamanho, saida::add);

        return saida;
    }

    public static long fatorial(int n) {
        long total = 1;

        for (int passo = 2; passo <= n; passo++) total *= passo;

        return total;
    }
}
