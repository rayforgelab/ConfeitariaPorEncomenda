# Explicação do E4 da Confeitaria por Encomenda

Ray Gomes Pereira e Carlos Eduardo de Oliveira Barbosa

O E4 foi desenvolvido sobre o projeto E3 enviado. O sistema continua sendo executado no console e agora permite montar uma encomenda com vários itens, acompanhar seu orçamento e digitar novamente números e datas quando houver erro. As alterações estão sinalizadas no código por comentários com **E4**, incluindo blocos grandes nas partes centrais.

A regra de negócio utilizada é a descrita na proposta de modelagem anterior da dupla, na seção “Regra de negócio”: calcular o valor de cada item considerando quantidade, preço unitário e adicionais, e somar esses valores para obter o total da encomenda.

## 1 O que o enunciado pede e como foi atendido

| Exigência do E4 | Implementação |
| --- | --- |
| Regra de negócio coerente com a E1 | Orçamento de produtos e personalizações em `ItemEncomenda.calcularValorItem()` e `Encomenda.calcularValorTotal()` |
| Menu único reunindo CRUD e herança e polimorfismo | `Main` reúne clientes, os sete tipos de produto, encomendas e gerenciamento de itens |
| Tratamento de entrada com try/catch e InputMismatchException | `lerInt()` e `lerDouble()` capturam essa exceção e repetem a pergunta |
| Sistema funcionando como um todo | Verificação das classes e execução de cenários completos pelo console |

O E3 já tinha um menu principal e os métodos de cálculo. O trabalho do E4 aproveita essa base, torna o orçamento visível durante o uso, integra os métodos de gerenciamento de itens ao console e completa o tratamento das entradas.

## 2 A regra de negócio explicada do zero

Uma regra de negócio define como a atividade atendida pelo programa deve funcionar. Neste sistema, a atividade é montar e orçar encomendas de confeitaria com produtos, quantidades e personalizações.

Um **produto** é uma opção do cardápio, como um bolo de chocolate. Um **item da encomenda** é a inclusão daquele produto em determinado pedido, com sua quantidade, seu preço unitário e o valor dos adicionais escolhidos. Uma **encomenda** reúne o cliente e todos esses itens.

A fórmula do item é:

```text
Valor do item = (quantidade × preço unitário) + adicionais totais do item
```

A fórmula da encomenda é:

```text
Valor total da encomenda = soma do valor de todos os seus itens
```

Considere este pedido:

| Item | Quantidade | Preço unitário | Adicionais totais | Resultado |
| --- | ---: | ---: | ---: | ---: |
| Bolo | 2 | R$ 50,00 | R$ 15,00 de decoração | R$ 115,00 |
| Brigadeiro | 10 | R$ 2,00 | R$ 0,00 | R$ 20,00 |
| Total da encomenda | | | | R$ 135,00 |

Primeiro, o programa calcula `2 × 50 + 15`, obtendo R$ 115,00. Depois calcula `10 × 2 + 0`, obtendo R$ 20,00. Por fim, soma R$ 115,00 e R$ 20,00.

**O adicional informado é o valor total daquele item e é somado uma única vez.** Se uma decoração custar R$ 5,00 por unidade e forem pedidas três unidades, deve-se informar R$ 15,00 no campo de adicionais.

Os valores dos adicionais são informados no console. As descrições de sabor, camadas, frutas ou decoração identificam as características do produto; o sistema não possui uma tabela automática de preços para cada uma dessas características.

Para doces e salgados, a quantidade se refere à unidade de venda do produto cadastrado. Se o preço do produto representa uma porção com 100 doces, quantidade 2 significa duas porções. O atributo `unidadesPorPorcao` descreve o conteúdo da porção e não é multiplicado novamente no orçamento.

## 3 Por que guardar o preço unitário no item

No E3, o cálculo consultava diretamente `produto.getPrecoBase()` toda vez que precisava calcular o item. Isso criava um problema: ao mudar o preço do cardápio, o valor de um pedido anterior também mudava.

No E4, o item recebe o atributo `precoUnitario`, previsto na proposta de modelagem. Seu construtor copia o preço do produto no momento em que o item é montado:

```java
this.precoUnitario = produto.getPrecoBase();
```

O cálculo usa esse valor registrado:

```java
return (quantidade * precoUnitario) + valoresAdicionais;
```

Assim, um item criado com dois bolos de R$ 50,00 e R$ 15,00 de adicionais continua valendo R$ 115,00 mesmo que o preço do bolo no cardápio passe a R$ 80,00. Um novo item com dois bolos de R$ 80,00 e os mesmos adicionais valerá R$ 175,00.

**A atualização explícita de um item refaz o orçamento desse item com o preço atual do cardápio.** Essa informação aparece no próprio menu. A edição do produto no cardápio, sozinha, preserva os preços dos itens já registrados.

A proteção se refere ao preço. O item continua associado ao objeto do produto, portanto uma mudança no nome do produto também aparece na identificação desse produto nos pedidos.

## 4 Alterações em ItemEncomenda java

Foram acrescentados o atributo `precoUnitario`, sua captura no construtor e o método `getPrecoUnitario()` para consulta. O cálculo passou a usar o preço registrado no item.

O construtor também valida os dados. Ele recusa produto ausente, quantidade zero ou negativa e adicionais negativos, infinitos ou `NaN`. `NaN` é um valor especial de ponto flutuante que significa “não é um número”; ele não serve como valor de orçamento.

Quando um dado viola essas condições, a classe lança `IllegalArgumentException`. Essa exceção indica que o código recebeu um argumento incompatível com suas regras.

A exibição do item foi ampliada para mostrar quantidade, preço unitário registrado, adicionais totais e valor final com duas casas decimais. Isso permite conferir a composição do orçamento.

## 5 Alterações em Encomenda java

O método `calcularValorTotal()` percorre os itens usando `for` e soma o resultado de `item.calcularValorItem()`. A fórmula já existia no E3 e recebeu comentários explicativos. Agora seu resultado aparece ao criar e listar encomendas e durante o gerenciamento de itens.

O total é calculado quando consultado. Por isso, adicionar, substituir ou remover um item atualiza o valor mostrado na próxima exibição, sem precisar manter uma variável de total manualmente.

A classe passou a validar cliente e datas no construtor. Uma entrega anterior à data do pedido é recusada. O método `setDataEntrega()` aplica a mesma condição ao reagendar. A nova data só é gravada depois de passar pela validação.

O formato de datas passou a usar `ResolverStyle.STRICT`, que recusa dias impossíveis. Sem essa configuração, certos formatos podem ajustar uma data inválida para outra data. O padrão interno é `dd/MM/uuuu HH:mm`; o usuário continua digitando dia, mês, ano, hora e minuto. O `uuuu` permite trabalhar com o ano no modo estrito.

Também foram adicionadas verificações para impedir item nulo, atualização nula e ID de item repetido dentro da mesma encomenda. Esses erros são identificados antes de modificar a lista. Os IDs dos itens são locais à encomenda: encomendas diferentes podem ter um item de ID 1.

A listagem de encomendas agora inclui o valor total calculado.

## 6 Alterações em ProdutoConfeitaria java

O método `setPrecoBase()` passou a recusar preço negativo, infinito e `NaN`. O preço zero é aceito. O construtor usa esse mesmo método para validar o preço inicial, centralizando a condição em um único lugar.

Como os diferentes tipos de produto herdam de `ProdutoConfeitaria`, essa proteção também se aplica aos bolos, doces e salgados.

A validação na classe complementa a validação no console. O console orienta o usuário a corrigir a digitação; a classe protege os próprios dados mesmo quando é utilizada diretamente por outro código.

## 7 Alterações em Main java

### 7 1 Leitura segura de números

Os métodos antigos convertiam textos com `Integer.parseInt()` e `Double.parseDouble()` sem tratamento. Uma letra no campo numérico causava uma exceção que encerrava a execução.

Agora, `lerInt()` e `lerDouble()` repetem a leitura dentro de `while (true)`. O bloco `try` tenta interpretar o valor; o bloco `catch (InputMismatchException erro)` mostra uma orientação quando a entrada não corresponde ao tipo esperado. O `return` só acontece quando o valor é aceito.

O procedimento é:

1. `lerTexto()` consome uma linha completa do console.
2. Um `Scanner` temporário interpreta somente essa linha.
3. `nextInt()` ou `nextDouble()` tenta extrair o número.
4. Letras, números inteiros fora do limite de `int` ou conteúdo extra causam recusa.
5. O `catch` mostra uma mensagem e o laço pede o valor novamente.

Ler a linha inteira evita ficar tentando interpretar o mesmo caractere inválido e evita que uma quebra de linha pendente pule um campo de texto.

O `try (Scanner entrada = ...)` é um **try com recursos**: ao finalizar a tentativa, Java fecha automaticamente esse Scanner temporário. O Scanner principal, responsável por ler o teclado, é fechado somente ao encerrar o sistema.

O leitor decimal aceita `12,50` e `12.50`, substituindo a vírgula pelo ponto e interpretando o número com `Locale.US`. Os valores devem ser digitados sem separador de milhar. Valores como `NaN`, infinito e números que resultem em infinito são recusados nesse leitor.

### 7 2 Validar o significado do número

Conseguir ler um número não significa que ele seja permitido naquele campo. Por exemplo, `-2` é um inteiro, mas não serve como quantidade de bolos.

| Método | Finalidade |
| --- | --- |
| `lerIntEntre()` | Aceitar apenas opções dentro do intervalo daquele menu |
| `lerIntPositivo()` | Exigir quantidade, número de camadas e unidades por porção maiores que zero |
| `lerValorNaoNegativo()` | Aceitar preços e adicionais a partir de zero |
| `lerData()` | Exigir formato e data de calendário válidos |
| `lerSimNao()` | Aceitar apenas S ou N, em maiúsculas ou minúsculas |
| `lerTextoObrigatorio()` | Impedir nome ou status vazio |

Um ID numérico que não corresponde a um cadastro produz uma mensagem de registro não encontrado e retorna ao menu, seguindo o fluxo do CRUD. Já um erro de tipo, como uma letra no ID, repete a pergunta numérica.

### 7 3 Criação e atualização de encomendas

Ao criar uma encomenda, o programa seleciona cliente e produto, lê entrega, observação, quantidade e adicionais, monta o item e registra o pedido. O total é mostrado ao finalizar.

Se a data de entrega vier antes do pedido, a operação mostra uma mensagem e solicita uma nova data. O ID da próxima encomenda só avança depois que o cadastro é aceito.

O horário do pedido usa precisão de minuto, compatível com o formato digitado no console. Isso evita uma diferença invisível de segundos na comparação das datas.

A atualização de encomendas ganhou uma pergunta opcional para reagendar a entrega. Quando o usuário escolhe S, o sistema repete a solicitação da nova data até que ela seja válida. Em seguida, atualiza status e observação.

### 7 4 Gerenciamento de vários itens

No menu Encomendas, a opção **5 — Gerenciar itens e consultar orçamento** permite selecionar um pedido, consultar seus itens e seu total, adicionar um produto, atualizar um item ou remover um item.

`gerenciarItens()` controla esse submenu. `salvarItem()` reúne o fluxo de inclusão e atualização: escolhe produto, lê quantidade e adicionais, cria o item e chama o método correspondente de `Encomenda`.

Uma inclusão recebe um ID maior que todos os IDs atualmente usados naquela encomenda. Uma atualização conserva o ID escolhido e substitui o item correspondente. A inclusão de um ID já existente é recusada pela classe `Encomenda`.

A interface impede remover o último item. Para apagar o pedido inteiro, existe a opção Excluir no menu de encomendas. Essa proteção do último item está no fluxo do console; o método de remoção da classe continua retornando se encontrou e removeu o ID solicitado.

### 7 5 Filtros e encerramento

O filtro de produtos passou a contemplar doces e salgados, além dos tipos de bolo. A escolha de filtro usa o leitor S/N validado. O filtro de bolo temático também permite selecionar um tema ou deixar a busca sem tema específico.

O método `main()` chama `executarSistema()` e trata o fim da entrada com `NoSuchElementException`. Assim, ao fechar o fluxo de entrada, como com Ctrl+D no Linux, o programa mostra uma mensagem de encerramento e fecha seu Scanner.

## 8 Como herança e polimorfismo aparecem no sistema

O cardápio usa uma lista de `ProdutoConfeitaria` que pode conter objetos de diferentes subclasses. Essa organização permite percorrer e trabalhar com produtos diferentes no mesmo menu.

`Bolo`, `Doce`, `Salgado` e `BoloACombinar` herdam de `ProdutoConfeitaria`. `BoloPersonalizado`, `BoloCasamento` e `BoloTematico` especializam `Bolo`.

Ao executar `p.getCategoria()` ou imprimir um produto, Java utiliza a implementação correspondente ao objeto concreto. Isso é polimorfismo. Por exemplo, uma variável declarada como `ProdutoConfeitaria` pode apontar para um `BoloCasamento`, cuja categoria é “Bolo de Casamento”.

O `instanceof` é usado nos filtros para verificar a qual classe um produto pertence. No filtro temático, depois de confirmar que o objeto é `BoloTematico`, o código pode consultar `getTema()`.

Os arquivos das subclasses e de `Cliente` são os mesmos do E3 enviado. As alterações funcionais ficaram concentradas em `Main`, `Encomenda`, `ItemEncomenda` e `ProdutoConfeitaria`.

## 9 Como executar

É necessário um **JDK Java 8 ou superior**. Um JDK inclui o compilador `javac`; instalar somente o ambiente de execução pode não disponibilizar esse comando.

No Eclipse, importe a pasta `ConfeitariaPorEncomenda` como projeto existente e execute `src/model/Main.java` como aplicação Java. A pasta conserva a configuração original do projeto e a codificação UTF-8.

No terminal Linux, entre na pasta `ConfeitariaPorEncomenda` e execute:

```bash
mkdir -p bin
javac -encoding UTF-8 -d bin src/model/*.java
java -cp bin model.Main
```

Para executar as verificações opcionais das classes:

```bash
javac -encoding UTF-8 -cp bin -d bin test/model/TestesE4.java
java -cp bin model.TestesE4
```

O arquivo de testes usa somente recursos do próprio Java e fica separado do código principal. Ele não precisa ser executado para usar o sistema.

## 10 Roteiro para demonstrar na defesa

1. Abra o sistema e digite uma letra no menu. Explique que `InputMismatchException` é capturada e a pergunta é repetida.
2. Atualize o produto padrão para ter preço de R$ 50,00. O produto padrão e o cliente padrão têm ID 1.
3. Crie uma encomenda para esse cliente, escolhendo não filtrar a lista e selecionando o produto 1. Informe uma entrega futura, quantidade 2 e adicional 15. O total será R$ 115,00.
4. Entre na opção 5 de Encomendas e selecione o pedido criado. Adicione mais um item do produto 1, com quantidade 1 e adicional zero. O total será R$ 165,00.
5. Atualize o preço do produto no cardápio para R$ 80,00. Consulte novamente o pedido: seus itens anteriores continuam totalizando R$ 165,00.
6. Adicione um novo item do mesmo produto, quantidade 1 e adicional zero. Esse novo item usa R$ 80,00, e o pedido passa a R$ 245,00.
7. Remova esse novo item. O total volta para R$ 165,00. Mostre que o valor é recalculado a partir dos itens existentes.
8. Tente digitar uma data impossível, como 31/02/2099, e depois uma entrega anterior ao pedido. Explique a diferença entre validar o calendário e validar a relação entre as datas.

Para explicar oralmente, apresente a sequência: o produto fornece o preço atual; o item registra esse preço e calcula quantidade × preço unitário + adicionais; a encomenda soma os itens; o menu exibe os resultados e pede novamente dados inválidos.

## 11 Verificações realizadas e limites do projeto

O código foi compilado em Java 17 com compatibilidade de linguagem e bytecode para Java 8, sem avisos do compilador. Foram aprovadas **50 verificações das classes**, incluindo cálculo dos adicionais, total de vários itens, atualização e remoção, preservação do preço, preços e quantidades inválidos, datas e polimorfismo das sete categorias.

Também passaram **57 verificações em seis cenários do console**, cobrindo entrada numérica inválida, cadastro, listagem, atualização e exclusão, os sete tipos de produto, filtros, datas inválidas, pedidos com vários itens, preservação do orçamento e encerramento ao terminar a entrada. Os testes do console foram executados externamente durante a validação; o arquivo opcional incluído no projeto contém as 50 verificações das classes.

Os dados ficam nas listas em memória e são reiniciados ao fechar o programa. Há cliente, produto e encomenda de demonstração ao iniciar, como na base do E3. O preço é preservado durante a execução pelo atributo do item; o projeto não grava um histórico em banco de dados.

Os cálculos mantêm o tipo `double` usado no projeto e exibem duas casas decimais. Para valores usuais da atividade, os testes conferem os resultados com tolerância para a representação binária dos decimais. Uma aplicação financeira de produção exigiria definir uma política monetária e de arredondamento mais completa.
