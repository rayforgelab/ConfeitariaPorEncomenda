# Confeitaria por Encomenda E4

Projeto Java de console desenvolvido sobre o E3 de Ray Gomes Pereira e Carlos Eduardo de Oliveira Barbosa.

## Executar

Requer JDK Java 8 ou superior. No Eclipse, importe esta pasta como projeto existente e execute `src/model/Main.java`.

No Linux, dentro desta pasta:

```bash
mkdir -p bin
javac -encoding UTF-8 -d bin src/model/*.java
java -cp bin model.Main
```

## O que foi implementado

- Orçamento conforme a proposta E1: `(quantidade × preço unitário) + adicionais totais` por item; o total do pedido soma os itens.
- Preço unitário registrado no item para preservar orçamentos ao alterar o cardápio.
- Console integrado com gerenciamento de vários itens em Encomendas, opção 5.
- Leitura segura com `try/catch` e `InputMismatchException`, além de validação de datas e limites dos campos.
- Comentários destacados por `E4` nas partes alteradas.

O adicional é o valor total do item. A atualização explícita de um item utiliza o preço atual do cardápio. Os dados permanecem em memória enquanto o programa está aberto.

A explicação didática completa, com fórmulas, exemplos, alterações por arquivo e roteiro de defesa, está em **EXPLICACAO_E4.md**.

## Verificações opcionais das classes

Depois de compilar o código principal:

```bash
javac -encoding UTF-8 -cp bin -d bin test/model/TestesE4.java
java -cp bin model.TestesE4
```

Resultado esperado: `E4: 50 verificações passaram.`

Os testes usam somente Java e ficam separados do código da aplicação.
