# MinJavaC #

MinJavaC é um compilador conceito para a linguagem [MiniJava](http://www.cambridge.org/us/features/052182060X/index.html), escrito em Java. Um projeto da disciplina DIM0406 - Compiladores (UFRN), sob orientação do Prof. Marcelo Siqueira.

## Objetivos ##

O design da linguagem MiniJava, um projeto externo já existente, foi feito cuidadosamente para o estudo da teoria e implementação de compiladores em Java. Seu escopo é reduzido o bastante para que um compilador da linguagem, voltado à uma arquitetura comum, possa ser feito ao longo da duração de um curso normal de 1 semestre. Mesmo assim, tal trabalho toca em diversos pontos essenciais da construção de compiladores e serve como uma base teórica para mais estudos na construção de compiladores reais.

Com o MinJavaC, esta base foi aproveitada e foi possível unir a isto o estudo da arquitetura x86-32, e a máquina virtual JVM.


## O Compilador ##

O compilador foi desenvolvido na linguagem Java, tendo como base para o front-end (parser, symboltable e typechecker) a arquitetura de Visitors apresentada [aqui](http://www.cambridge.org/us/features/052182060X/index.html). Seu código fonte está disponível publicamente sobre a GPLv2, [neste repositório](http://code.google.com/p/minjavac/source/checkout). Um pacote zip com o compilador, referente a uma revisão recente do repositório, está disponível [aqui](http://code.google.com/p/minjavac/downloads/list).

O MinJavaC apresenta uma escolha entre dois backends para a geração do código final: _NASM_ ou _JVM_.

A execução se dá da seguinte maneira:

```
  java minjavac <source_name>.java [-backend (nasm | jvm)]
```

Caso o backend não seja especificado, o default será o nasm. No backend nasm, a linha acima gera um executável de nome source\_name. No backend jvm, a invocação cria um arquivo .class para cada classe declarada no fonte. Estes arquivos, então, podem ser executados na JVM (`java Classe`).

O arquivo de texto config.txt distribuído junto com o compilador é utilizado para indicar os caminhos das ferramentas jasmin.jar e nasm (assemblers para bytecode jvm e assembly x86), que serão explicadas nas seções seguintes.


## A Linguagem ##

A linguagem fonte do compilador consiste de algumas adições feitas em cima do projeto inicial da MiniJava ([BNF grammar](http://www.cambridge.org/us/features/052182060X/grammar.html)).

A estrutura básica de um código-fonte em MiniJava pode ser vista com este exemplo:

```
class Factorial {
  public static void main(String[] a) {
    System.out.println(new Fac().ComputeFac(10));
  }
}

class Fac {
  public int ComputeFac(int num) {
    int num_aux;
    
    if (num < 1) {
      num_aux = 1;
    } else {
      num_aux = num * (this.ComputeFac(num - 1));
    }
    return num_aux;
  }
}
```

A primeira classe é sempre considerada a principal. Esta só contêm um método, que será o ponto de partida do programa. Além desta classe, o código fonte pode contar outras classes logo abaixo. Note que o significado de um programa MiniJava é dado pelo seu significado caso ele seja interpretado como um programa Java. Isto é, MiniJava é um subconjunto de Java, compartilhando sua semântica.

### Modificações ###

O projeto original da MiniJava contêm diversas restrições (com relação à Java) para tornar o seu uso mais fácil, didaticamente. Algumas destas restrições fazem com que a linguagem não seja realmente considerada "real". Isto é, certas restrições que tornam a vida do programador _muito_ mais difícil, enquanto facilitam só um pouco a vida do implementador.

Tendo isto em mente, o escopo inicial da linguagem teve as seguintes adições:

  * _Strings_: Suporte à impressão na saída padrão de strings literais, com o intuito de facilitar o processo de debug de um programa MiniJava. Exemplo:
```
  public static void main(String[] a) {
    System.out.print("Statement A");
  }
```

  * _Main_: Relaxamento das restrições nos statements presentes no método main. O projeto original impôe que o método main só contêm uma instrução System.out.println. Exemplo:
```
class VTTest {
  public static void main(String[] args) {
    Tester t = new Tester();
    t.test(new A());
    t.test(new B());
  }
}
```

  * _Declarações_: Possibilidade de declarações de múltiplas variáveis de um mesmo tipo numa mesma unidade, assim como inicialização. Exemplo:
```
 int x, y = 3, z = y + 1;
 int[] v = new int[30];
```

  * _For_: Adição do comando for tradicional. Exemplo:
```
for (b = 0, a = 10; b <= a; b = b + 1, a = a - 1) {
  System.out.println(b);
}
```

  * _Aritmética_: Adição da operação de divisão, assim como o comando de atribuição composto. Exemplo:
```
public static void main(String[] args) {
  a = 20;
  b = 2;
  System.out.println(a/b);
  a += 4;
  a -= 2;
  b *= 2;
  ++a;
  System.out.println(a++);
  System.out.println(++b);
  System.out.println(a);
}
```

  * _Operadores booleanos_: O projeto original só contava com a comparação "menor que" (<) e o "and" lógico (&&). Foram adicionados: <=, >, >=, ==, !=, || (com os significados usuais vistos em Java).

  * _Chamada de método_: A gramática original contêm restrições rígidas para chamadas de método. Entre elas: uma chamada de método não é um comando (somente uma expressão) e métodos na classe atual precisam de this explícito na chamada (this.metodo()). Tais restrições foram removidas. Exemplo:
```
methodA();
obj.methodB(i, j, k);

if (methodC(2)) {
}
else {
}
```

  * _Incremento / decremento_: Adição de expressões tais como: ++x, --y, x++, y--. Também podem ser usadas como comandos. Exemplo:
```
for (x = 0; x <= y; ++x, --y) { }
```


## JVM ##

O backend JVM é responsável pela tradução de programas MiniJava para o bytecode interpretado pela máquina virtual Java tradicional. Esta tradução não é direta. O MinJavaC gera uma representação textual do arquivo .class correspondente ao código fonte MiniJava, em ASCII puro. Este arquivo de texto tem um mapeamento 1 : 1 com o bytecode final. A partir daí, uma ferramenta externa, denominada [Jasmin](http://jasmin.sourceforge.net/), traduz o arquivo de texto para o arquivo binário .class esperado pela máquina virtual.

Como exemplo, considere a classe:
```
class Fac {
  public int ComputeFac(int num) {
    int num_aux;

    if (num < 1)
      num_aux = 1;
    else
      num_aux = num * ComputeFac(num - 1);
    
    return num_aux;
  }
}
```

Um trecho do assembly respectivo segue abaixo:
```
.class public Fac
.super java/lang/Object

.method public <init>()V
   aload_0
   invokenonvirtual java/lang/Object/<init>()V
   return
.end method

.method public ComputeFac(I)I
.limit locals 4

   iload_1
   iconst_1
   if_icmplt if_true
   goto if_false

 if_true:
   iconst_1
   istore_2
   goto if_next

 if_false:
   iload_1
   aload_0
   iload_1
   iconst_1
   isub
   invokevirtual Fac/ComputeFac(I)I
   imul
   istore_2

 if_next:
   iload_2
   ireturn

.limit stack 4
.end method
```

Note que os bytecodes foram projetados especialmente para a linguagem Java, e por isso são um mapeamento fácil vindo da MiniJava (embora dificulte bastante o trabalho de implementadores de outras linguagens para a JVM).

A implementação deste backend foi a mais simples, consistindo essencialmente de um Visitor na AST do programa, realizando uma percurso pós-ordem generalizado (uma boa solução para a geração de código alvo para máquinas de pilha, como a JVM).


## NASM ##

O backend NASM compôe a maior parte da base de código do MinJavaC. Ele é responsável pela geração de código executável para plataformas x86 (32 bits). Ele trabalha gerando um arquivo assembly em formato textual e utilizando o assembler [NASM](http://en.wikipedia.org/wiki/Netwide_Assembler).

### Representação Intermediária ###

Devido a sua complexidade, este componente não faz uma tradução direta. Ele primeiro gera código intermediário sob a forma de [Three address code](http://en.wikipedia.org/wiki/Three_address_code).

O código não é explicitamente mostrado ao usuário, só existe na forma de estruturas de dados especiais na memória do compilador. Um exemplo de parte do código intermediário para o programa Factorial seria:
```
class Fac:
procedure Fac@ComputeFac
   if greater_or_equal(num, 1) goto .if_false;

   num_aux := 1;
   goto .if_next;

 .if_false:
   save_context;
   .new_Fac := call Fac@@new;
   load_context;
   f := .new_Fac;
   save_context;
   .sub := sub num, 1;
   param .sub;
   param f;
   .call := call Fac@ComputeFac;
   load_context;
   .mult := mult num, .call;
   num_aux := .mult;

 .if_next:
   return num_aux;
end
end
```

Em cima desta representação, foram implementadas algumas otimizações básicas. Elas funcionam tomando como base os [blocos básicos](http://en.wikipedia.org/wiki/Basic_block) de um procedimento, e fazendo uma análise em cima deles.

  * _Reescrita de instruções_: Algumas instruções são reescritas para diminuir a complexidade do código gerado, sempre que possível. Exemplo:
```
goto L;  // elimina o goto
L:
```

  * _Análise de vida_ : O compilador realiza uma [análise de vida das variáveis](http://en.wikipedia.org/wiki/Live_variable_analysis) simplificada. Ela trabalha montando o [grafo de fluxo de controle](http://en.wikipedia.org/wiki/Control_flow_graph) do procedimento atual. Estas informações serão utilizadas na geração de código.
    1. Primeiramente, monta o grafo considerando os nós como sendo os blocos básicos, e as arestas como o fluxo do programa (jumps entre blocos e blocos executados sequencialmente).
    1. Isto é feito para calcular, para cada bloco básico, utilizando uma busca no grafo, o conjunto de variáveis vivas na saída daquele bloco. Isto é, o conjunto das variáveis que necessitam ter o seu valor armazenado na memória ao fim daquele bloco.
    1. O pseudo-código correspondente seria: Para cada variável `V` que é escrita no bloco atual `B1`: caso exista um caminho de `B1` para `B2` no grafo de fluxo tal que `V` seja lida em `B2`, e antes disso não houve nenhuma escrita em `V` (isto é, o valor de `V` será o que foi determinado em `B1`), considera-se que `V` é uma variável viva na saída de `B1`.


### Geração de código ###

A partir da representação intermediária gerada na fase anterior, assim como as informações da tabela de símbolos que foram coletadas no início da análise semântica, é realizada a geração de código final.

Para a geração de código e a alocação de registradores, foi utilizado um algoritmo descrito no [Dragon Book](http://www.amazon.com/Compilers-Principles-Techniques-Alfred-Aho/dp/0201100886).

#### Mapeamento MiniJava -> código de máquina ####

O mapeamento utilizado foi baseado em uma adaptação do modelo utilizado pelo C++ em tempo de execução. Têm-se:

  * Os objetos são representados como blocos de dados contíguos, contendo os seus campos (representados por offsets a partir do endereço base do objeto), como uma struct do C++.

  * Métodos são procedimentos normais que esperam um parâmetro implícito, sendo este um ponteiro para o objeto no qual o método está sendo chamado (_this_).

  * Os arrays são representados como em C, porém sempre com um elemento à mais. Este elemento, considerado sempre na posição 0, guarda o tamanho do array. Ele é necessário pois o método .length do MiniJava requer a determinação do tamanho de um array alocado em tempo de execução. Exemplo:
```
 int[] x, y;
 x = new int[30];
 y = x;
 System.out.println(y.length);
```

  * Para implementar polimorfismo real (com despacho dinâmico), foi utilizado o esquema de [Virtual tables](http://en.wikipedia.org/wiki/Virtual_method_table). Cada objeto guarda um ponteiro para a tabela correspondente a sua classe, contendo os endereços de todos os métodos acessíveis por ele.

  * A convenção de chamada utilizada é uma adaptação da [ThisCall](http://en.wikipedia.org/wiki/X86_calling_conventions#thiscall). A diferença é que o ponteiro this é passado no registrador `%edx`, em vez de na pilha (facilita um pouco a implementação).


#### Algoritmo de tradução ####

O algoritmo de tradução utilizado trabalha em cada bloco básico separadamente. Assume que as definições de labels dos procedimentos, endereços de constantes e as tabelas virtuais já estão prontas.

  * Primeiramente, ele atribui a cada variável (seja ela local ao procedimento ou uma temporária necessária na avaliação de alguma expressão) um local na pilha. Isto é, faz uma alocação de pior caso. É mais ineficiente, em termos de memória, porém mais simples em termos de implementação.

  * Ao seguir na geração, são mantidos dois conjuntos de descritores. Os descritores de registrador guardam, para cada registrador, o conjunto das variáveis cujos valores estão presentes nele. E os descritores de variáveis, que guardam para cada variável, duas informações: o conjunto de registradores que contêm o seu valor correto, e se a sua posição na memória está consistente com o seu valor (o valor atual pode estar guardado somente em registradores).

  * Assim, para cada instrução de 3 endereços, são feitas operações básicas, tais como carregar valores em registradores, alocar registradores para receber valores de operações, etc. A cada operação básica, o conjunto de descritores é atualizado de acordo.

  * As operações básicas impôem certas restrições. Por exemplo: Caso o registrador R tenha que receber o valor da variável V, porém R é o único local com o valor atual de V', uma instrução mov será emitida, guardando o valor de V' na memória antes que seja apagado.

  * Isto permite certas otimizações também, tais como reutilizar registradores com valores conhecidos de variáveis, sem precisar recorrer a memória

  * Ao final do bloco básico, para todas as variáveis consideradas vivas na saída daquele bloco (isto foi determinado na fase anterior), seus valores são atualizados na memória de acordo.

Este algoritmo é um melhoramento com relação ao algoritmo mais simples possível, e gera código de qualidade razoável. É possível observar várias possíveis otimizações possíveis no assembly final, mas que não podem ser tão facilmente determinadas no código do compilador.


### C runtime ###

O código gerado pelo compilador segue, então, a sintaxe do NASM. Segue um trecho do assembly gerado para o [Factorial.nasm](http://paste.pocoo.org/show/152347/):
```
; constructors
segment .text

 Factorial@@new:
   push dword 4
   call _alloc
   add esp, 4
   mov [eax+0], dword Factorial@@vt
   ret

 Fac@@new:
   push dword 4
   call _alloc
   add esp, 4
   mov [eax+0], dword Fac@@vt
   ret

; code
segment .text

 Factorial@main:
   push ebp
   mov ebp, esp
   sub esp, 28

   push edx
   call Fac@@new
   pop edx
   push edx
   push dword 10
   mov edx, eax
   mov esi, [edx]
   call [esi+4]
   add esp, 4
   pop edx
   push edx
   push eax
   call _print_int
   add esp, 4
   pop edx
   push edx
   call Fac@@new
   pop edx
   push edx
   push dword 10
   mov edx, eax
   mov esi, [edx]
   call [esi+0]
   add esp, 4
   pop edx
   push edx
   push eax
   call _print_int
   add esp, 4
   pop edx

   mov esp, ebp
   pop ebp
   ret
```

Este código precisa fazer chamadas à funções do sistema operacional, tais como: escrever em arquivos (para imprimir na saída padrão), alocar espaço da heap, etc. Para manter o código portável, estas funções são chamadas a partir da biblioteca padrão C, que é implementada em diversos sistemas diferentes.

Esta decisão faz com que o código objeto gerado pelo NASM tenha que ser linkado junto à código C, cujo um trecho segue à seguir:
```
#include <stdlib.h>
#include <stdio.h>

void* alloc(int size) {
  return malloc(size);
}

int* new_array(int size) {
  int* array = (int*)alloc(4*(size+1));
  array[0] = size;
  return array;
}

void print_int(int n) {
  printf("%d\n", n);
}
```


## Bugs, testes, contribuições ##

O compilador vem sendo testado com os programas disponíveis na página do projeto do [MiniJava](http://www.cambridge.org/us/features/052182060X/index.html). Na revisão mais recente, ambos os backends passam em todos os testes utilizados. Mesmo assim, o código ainda pode conter bugs. Qualquer bug pode ser reportado para `giulianoxt@gmail.com`. Qualquer contribuição ou idéia nova para implementação no compilador também será bem vinda.