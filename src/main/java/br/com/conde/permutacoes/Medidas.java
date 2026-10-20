package br.com.conde.permutacoes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * AS MEDIDAS: todas as permutações de cada tamanho, até 9 elementos, que são 362.880.
 */
public final class Medidas {

    private Medidas() {}

    public static final int ATE = 9;

    /* ===================== 1. correcao ===================== */

    public record Correcao(int n, long geradas, long fatorial, int repetidas) {

        public boolean bate() {
            return geradas == fatorial && repetidas == 0;
        }
    }

    private static List<Correcao> correcao;

    /**
     * O GERADOR PRODUZ n! PERMUTAÇÕES, TODAS DIFERENTES.
     *
     * <p>Se ele repetisse ou pulasse, toda contagem deste repositório estaria medindo outra
     * coisa, e nada acusaria: as médias continuariam parecendo razoáveis.
     */
    public static List<Correcao> correcao() {
        if (correcao != null) return correcao;

        List<Correcao> saida = new ArrayList<>();

        for (int n = 1; n <= 8; n++) {
            java.util.Set<String> distintas = new java.util.HashSet<>();

            long[] geradas = {0};

            Permutacao.cada(n, p -> {
                geradas[0]++;
                distintas.add(p.toString());
            });

            saida.add(new Correcao(n, geradas[0], Permutacao.fatorial(n),
                    (int) (geradas[0] - distintas.size())));
        }

        return correcao = List.copyOf(saida);
    }

    /* ===================== 2. desarranjos ===================== */

    public record Desarranjo(int n, long permutacoes, long desarranjos) {

        public double fracao() {
            return desarranjos / (double) permutacoes;
        }

        public double distanciaDeUmSobreE() {
            return Math.abs(fracao() - 1 / Math.E);
        }
    }

    private static List<Desarranjo> desarranjos;

    /**
     * O AMIGO OCULTO QUE DÁ CERTO, e a surpresa de ele não depender do tamanho.
     *
     * <p>Um desarranjo é uma permutação em que ninguém foi parar no próprio lugar: no sorteio,
     * ninguém tirou o próprio nome.
     *
     * <p>A fração deles converge para {@code 1/e}, uns 36,8%, e a parte que surpreende é a
     * VELOCIDADE: com 6 pessoas o valor já está a cinco casas decimais do limite. Convidar
     * mais gente não muda a chance de o sorteio dar certo.
     */
    public static List<Desarranjo> desarranjos() {
        if (desarranjos != null) return desarranjos;

        List<Desarranjo> saida = new ArrayList<>();

        for (int n = 1; n <= ATE; n++) {
            long[] conta = {0, 0};

            Permutacao.cada(n, p -> {
                conta[0]++;

                if (p.desarranjo()) conta[1]++;
            });

            saida.add(new Desarranjo(n, conta[0], conta[1]));
        }

        return desarranjos = List.copyOf(saida);
    }

    public static Desarranjo desarranjo(int n) {
        return desarranjos().stream().filter(x -> x.n() == n).findFirst().orElseThrow();
    }

    /** A partir de que tamanho a fração já está a menos de 0,001 de 1/e. */
    public static int ondeEstaciona() {
        for (Desarranjo linha : desarranjos())
            if (linha.distanciaDeUmSobreE() < 0.001) return linha.n();

        return -1;
    }

    /* ===================== 3. ciclos ===================== */

    public record Ciclo(int n, double mediaDeCiclos, double harmonico,
            Map<Integer, Integer> porQuantidade) {

        public boolean batemExato() {
            return Math.abs(mediaDeCiclos - harmonico) < 1e-12;
        }
    }

    private static List<Ciclo> ciclos;

    /**
     * A MÉDIA DE CICLOS É O NÚMERO HARMÔNICO, exatamente.
     *
     * <p>Toda permutação se quebra em ciclos, porque seguir destinos num conjunto finito sempre
     * volta ao começo. A média do número de ciclos, sobre todas as permutações de n elementos,
     * é {@code 1 + 1/2 + 1/3 + ... + 1/n}.
     *
     * <p>Não é aproximadamente: é igual, e a medida confere com tolerância de um trilionésimo.
     */
    public static List<Ciclo> ciclos() {
        if (ciclos != null) return ciclos;

        List<Ciclo> saida = new ArrayList<>();

        for (int n = 1; n <= 8; n++) {
            Map<Integer, Integer> porQuantidade = new TreeMap<>();

            long[] soma = {0, 0};

            Permutacao.cada(n, p -> {
                int quantos = p.quantosCiclos();

                porQuantidade.merge(quantos, 1, Integer::sum);

                soma[0] += quantos;
                soma[1]++;
            });

            double harmonico = 0;

            for (int k = 1; k <= n; k++) harmonico += 1.0 / k;

            saida.add(new Ciclo(n, soma[0] / (double) soma[1], harmonico,
                    Map.copyOf(porQuantidade)));
        }

        return ciclos = List.copyOf(saida);
    }

    public static Ciclo ciclo(int n) {
        return ciclos().stream().filter(x -> x.n() == n).findFirst().orElseThrow();
    }

    /* ===================== 4. ordem ===================== */

    public record Ordem(int n, long maxima, String exemplo) {}

    private static List<Ordem> ordem;

    /**
     * QUANTAS VEZES EMBARALHAR ATÉ TUDO VOLTAR AO LUGAR.
     *
     * <p>Aplicar a mesma permutação repetidamente acaba devolvendo tudo ao começo, e o número
     * de vezes é o mínimo múltiplo comum dos tamanhos dos ciclos.
     *
     * <p>A maior ordem possível com n elementos é a função de Landau, e ela NÃO cresce sempre:
     * com 5 e com 6 elementos a maior ordem é a mesma, 6.
     */
    public static List<Ordem> ordem() {
        if (ordem != null) return ordem;

        List<Ordem> saida = new ArrayList<>();

        for (int n = 1; n <= ATE; n++) {
            long[] maior = {0};

            String[] exemplo = {""};

            Permutacao.cada(n, p -> {
                long atual = p.ordem();

                if (atual > maior[0]) {
                    maior[0] = atual;
                    exemplo[0] = p.toString();
                }
            });

            saida.add(new Ordem(n, maior[0], exemplo[0]));
        }

        return ordem = List.copyOf(saida);
    }

    public static Ordem ordem(int n) {
        return ordem().stream().filter(x -> x.n() == n).findFirst().orElseThrow();
    }

    /** Os tamanhos em que a ordem máxima não cresceu. */
    public static List<Integer> ondeNaoCresceu() {
        List<Integer> saida = new ArrayList<>();

        List<Ordem> tabela = ordem();

        for (int indice = 1; indice < tabela.size(); indice++)
            if (tabela.get(indice).maxima() == tabela.get(indice - 1).maxima())
                saida.add(tabela.get(indice).n());

        return saida;
    }

    /* ===================== 5. inversoes ===================== */

    public record Inversao(int n, double media, int maximo, long pares, long impares) {

        public double metadeDoMaximo() {
            return maximo / 2.0;
        }

        public boolean mediaEhMetade() {
            return Math.abs(media - metadeDoMaximo()) < 1e-12;
        }

        public boolean paresIgualImpares() {
            return pares == impares;
        }
    }

    private static List<Inversao> inversoes;

    /**
     * AS INVERSÕES, que são o trabalho da ordenação por inserção.
     *
     * <p>Uma inversão é um par fora de ordem, e ordenar por inserção custa exatamente uma
     * troca por inversão. A média sobre todas as permutações é exatamente METADE do máximo,
     * que é o que se espera por simetria: toda permutação tem uma parceira ao contrário.
     *
     * <p>E a mesma simetria divide pares e ímpares em metades exatas, de n igual a 2 em
     * diante.
     */
    public static List<Inversao> inversoes() {
        if (inversoes != null) return inversoes;

        List<Inversao> saida = new ArrayList<>();

        for (int n = 1; n <= 8; n++) {
            long[] soma = {0, 0, 0, 0};

            int[] maior = {0};

            Permutacao.cada(n, p -> {
                int quantas = p.inversoes();

                soma[0] += quantas;
                soma[1]++;

                maior[0] = Math.max(maior[0], quantas);

                if (p.par()) soma[2]++;
                else soma[3]++;
            });

            saida.add(new Inversao(n, soma[0] / (double) soma[1], maior[0], soma[2], soma[3]));
        }

        return inversoes = List.copyOf(saida);
    }

    public static Inversao inversao(int n) {
        return inversoes().stream().filter(x -> x.n() == n).findFirst().orElseThrow();
    }
}
