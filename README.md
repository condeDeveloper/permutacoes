# Permutações

Permutações em Java 21, com **todas** as de cada tamanho enumeradas até 9 elementos, que são 362.880.

A chance de um amigo oculto dar certo, ninguém tirando o próprio nome, é `1/e`. A parte que surpreende é a velocidade: **a partir de 6 pessoas o número para de mudar**. Convidar mais gente não altera nada.

```
mvn -q exec:java -Dexec.args=desarranjos
```

## As medidas

### 1. O gerador produz n! permutações, todas diferentes

| n | geradas | n! | repetidas |
|---:|---:|---:|---:|
| 6 | 720 | 720 | 0 |
| 7 | 5.040 | 5.040 | 0 |
| 8 | 40.320 | 40.320 | 0 |

Se ele repetisse ou pulasse, toda contagem deste repositório estaria medindo outra coisa, e as médias continuariam parecendo razoáveis.

### 2. O amigo oculto que dá certo

| n | permutações | desarranjos | fração | distância de 1/e |
|---:|---:|---:|---:|---:|
| 3 | 6 | 2 | 0,333333 | 0,034546 |
| 4 | 24 | 9 | 0,375000 | 0,007121 |
| 5 | 120 | 44 | 0,366667 | 0,001212 |
| **6** | 720 | 265 | 0,368056 | **0,000176** |
| 9 | 362.880 | 133.496 | 0,367879 | 0,0000001 |

```
1/e = 0,367879
```

Um desarranjo é uma permutação em que ninguém foi parar no próprio lugar.

A fração converge para `1/e`, uns 36,8%, e a partir de **6 pessoas** ela já está a menos de um milésimo do limite. Com 9, bate em seis casas decimais.

### 3. A média de ciclos é o número harmônico

| n | média de ciclos | 1 + 1/2 + ... + 1/n | exatamente igual |
|---:|---:|---:|---|
| 4 | 2,0833333333 | 2,0833333333 | sim |
| 6 | 2,4500000000 | 2,4500000000 | sim |
| 8 | 2,7178571429 | 2,7178571429 | sim |

Toda permutação se quebra em ciclos, porque seguir destinos num conjunto finito sempre volta ao começo.

A média **não é aproximadamente** o harmônico: é igual, e a medida confere com tolerância de um trilionésimo.

Com 6 elementos: 120 permutações têm um ciclo só, 274 têm dois, 225 têm três, 85 têm quatro, 15 têm cinco e 1 tem seis.

### 4. Quantas vezes embaralhar até tudo voltar ao lugar

| n | ordem máxima | exemplo |
|---:|---:|---|
| 4 | 4 | 1230 |
| 5 | **6** | 10342 |
| 6 | **6** | 021453 |
| 7 | 12 | 1204563 |
| 9 | 20 | 123056784 |

Aplicar a mesma permutação repetidamente acaba devolvendo tudo ao começo, e o número de vezes é o mínimo múltiplo comum dos tamanhos dos ciclos.

A ordem máxima **não cresce sempre**: com 5 e com 6 elementos o maior mmc possível é o mesmo, 6. Ganhar um elemento não garante ganhar período.

### 5. As inversões, que são o trabalho da ordenação por inserção

| n | média | máximo | metade do máximo | pares | ímpares |
|---:|---:|---:|---:|---:|---:|
| 4 | 3,00 | 6 | 3,0 | 12 | 12 |
| 6 | 7,50 | 15 | 7,5 | 360 | 360 |
| 8 | 14,00 | 28 | 14,0 | 20.160 | 20.160 |

Uma inversão é um par fora de ordem, e ordenar por inserção custa exatamente uma troca por inversão.

A média é **exatamente metade** do máximo, e isso sai da simetria: toda permutação tem uma parceira ao contrário, e as duas somam o máximo. A mesma simetria divide pares e ímpares em metades exatas.

## O que as medidas me corrigiram

**O amigo oculto não fica mais arriscado com mais gente.** Eu achava que a chance de dar errado mudasse conforme o grupo cresce. Ela estaciona em 36,8% com seis pessoas, e com nove já bate em seis casas decimais.

**A média de ciclos é o harmônico, não "aproximadamente o harmônico".** Era uma relação que eu esperava ver como limite assintótico. Ela é uma igualdade, em todo tamanho, e o teste a exige com tolerância de um trilionésimo.

**Mais elementos não dão mais período.** A ordem máxima de uma permutação de 6 é a mesma de uma de 5. É o mínimo múltiplo comum que manda, e ele não liga para o tamanho e sim para que números cabem na soma.

## Rodar

```bash
mvn -q verify                                # 17 testes
mvn -q exec:java                             # todas as medidas
mvn -q exec:java -Dexec.args=correcao        # 1
mvn -q exec:java -Dexec.args=desarranjos     # 2
mvn -q exec:java -Dexec.args=ciclos          # 3
mvn -q exec:java -Dexec.args=ordem           # 4
mvn -q exec:java -Dexec.args=inversoes       # 5
```

Java 21, sem dependência fora do JUnit. Nenhum número deste README foi digitado à mão: todos saem de `Medidas`, e os testes conferem os que aparecem escritos aqui.
