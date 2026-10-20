package br.com.conde.permutacoes;

import java.util.Locale;

/** O MEDIDOR: roda as varreduras e imprime as tabelas do README. */
public final class Medidor {

    private Medidor() {}

    public static void main(String[] argumentos) {
        Locale.setDefault(Locale.US);

        String passo = argumentos.length > 0 ? argumentos[0] : "tudo";

        switch (passo) {
            case "correcao" -> correcao();
            case "desarranjos" -> desarranjos();
            case "ciclos" -> ciclos();
            case "ordem" -> ordem();
            case "inversoes" -> inversoes();
            default -> {
                correcao();
                desarranjos();
                ciclos();
                ordem();
                inversoes();
            }
        }
    }

    private static void titulo(String texto) {
        System.out.println();
        System.out.println(texto);
        System.out.println("=".repeat(texto.length()));
        System.out.println();
    }

    static void correcao() {
        titulo("1. o gerador produz n! permutacoes, todas diferentes");

        System.out.println(" n  geradas  n!       repetidas");

        for (Medidas.Correcao c : Medidas.correcao())
            System.out.printf("%2d  %7d  %7d  %9d%s%n", c.n(), c.geradas(), c.fatorial(),
                    c.repetidas(), c.bate() ? "" : "   <<< DIFERE");

        System.out.println();
        System.out.println("se ele repetisse ou pulasse, toda contagem deste repositorio estaria");
        System.out.println("medindo outra coisa, e as medias continuariam parecendo razoaveis.");
    }

    static void desarranjos() {
        titulo("2. o amigo oculto que da certo");

        System.out.println(" n  permutacoes  desarranjos    fracao    distancia de 1/e");

        for (Medidas.Desarranjo d : Medidas.desarranjos())
            System.out.printf("%2d  %11d  %11d  %.6f  %18.8f%n", d.n(), d.permutacoes(),
                    d.desarranjos(), d.fracao(), d.distanciaDeUmSobreE());

        System.out.printf("%n1/e = %.6f%n", 1 / Math.E);

        System.out.println();
        System.out.println("um desarranjo e uma permutacao em que ninguem foi parar no proprio");
        System.out.println("lugar: no sorteio, ninguem tirou o proprio nome.");
        System.out.println();
        System.out.printf("a fracao converge para 1/e, uns 36,8%%, e a partir de n = %d ela ja%n",
                Medidas.ondeEstaciona());
        System.out.println("esta a menos de um milesimo do limite. convidar mais gente NAO muda");
        System.out.println("a chance de o sorteio dar certo.");
    }

    static void ciclos() {
        titulo("3. a media de ciclos e o numero harmonico");

        System.out.println(" n  media de ciclos  1 + 1/2 + ... + 1/n  exatamente igual");

        for (Medidas.Ciclo c : Medidas.ciclos())
            System.out.printf("%2d  %15.10f  %19.10f  %17s%n", c.n(), c.mediaDeCiclos(),
                    c.harmonico(), c.batemExato() ? "sim" : "NAO");

        System.out.println();
        System.out.println("quantas permutacoes tem cada numero de ciclos, com 6 elementos:");
        System.out.println("  " + new java.util.TreeMap<>(Medidas.ciclo(6).porQuantidade()));

        System.out.println();
        System.out.println("toda permutacao se quebra em ciclos, porque seguir destinos num");
        System.out.println("conjunto finito sempre volta ao comeco.");
        System.out.println();
        System.out.println("a media nao e APROXIMADAMENTE o harmonico: e igual, e a medida");
        System.out.println("confere com tolerancia de um trilionesimo.");
    }

    static void ordem() {
        titulo("4. quantas vezes embaralhar ate tudo voltar ao lugar");

        System.out.println(" n  ordem maxima  exemplo");

        for (Medidas.Ordem o : Medidas.ordem())
            System.out.printf("%2d  %12d  %s%n", o.n(), o.maxima(), o.exemplo());

        System.out.println();
        System.out.printf("a ordem maxima NAO cresce sempre: ela ficou parada em n = %s.%n",
                Medidas.ondeNaoCresceu());

        System.out.println();
        System.out.println("aplicar a mesma permutacao repetidamente acaba devolvendo tudo ao");
        System.out.println("comeco, e o numero de vezes e o minimo multiplo comum dos tamanhos");
        System.out.println("dos ciclos. com 5 e com 6 elementos o maior mmc possivel e o mesmo.");
    }

    static void inversoes() {
        titulo("5. as inversoes, que sao o trabalho da ordenacao por insercao");

        System.out.println(" n   media  maximo  metade do maximo  pares  impares  metades iguais");

        for (Medidas.Inversao i : Medidas.inversoes())
            System.out.printf("%2d  %6.2f  %6d  %16.1f  %5d  %7d  %15s%n", i.n(), i.media(),
                    i.maximo(), i.metadeDoMaximo(), i.pares(), i.impares(),
                    i.paresIgualImpares() ? "sim" : "nao");

        System.out.println();
        System.out.println("uma inversao e um par fora de ordem, e ordenar por insercao custa");
        System.out.println("exatamente uma troca por inversao.");
        System.out.println();
        System.out.println("a media e exatamente METADE do maximo, e isso sai da simetria: toda");
        System.out.println("permutacao tem uma parceira ao contrario, e as duas somam o maximo.");
        System.out.println();
        System.out.println("a mesma simetria divide pares e impares em metades exatas, de n = 2");
        System.out.println("em diante. com n = 1 ha uma so, e ela e par.");
    }
}
