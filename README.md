# Confeitaria por Encomenda

**Autores** — Ray Gomes Pereira e Carlos Eduardo de Oliveira Barbosa.

**Tema** — Confeitaria por encomenda

## Como rodar

Com o Flutter instalado, abra o terminal na pasta do projeto. O app foi verificado com o Flutter 3.47.6.

```bash
flutter pub get
```

Escolha o comando para o seu sistema. Windows e Linux abrem o app em uma janela própria.

| Plataforma     | Comando                    |
| -------------- | -------------------------- |
| Windows        | `flutter run -d windows` |
| Linux          | `flutter run -d linux`   |
| Web com Chrome | `flutter run -d chrome`  |

- No Windows, instale o Visual Studio com a carga de trabalho **Desenvolvimento para desktop com C++**. [Guia do Flutter para Windows](https://docs.flutter.dev/platform-integration/windows/setup)
- No Linux, instale as ferramentas de compilação e as bibliotecas de desenvolvimento do GTK 3 para a sua distribuição. [Guia do Flutter para Linux](https://docs.flutter.dev/platform-integration/linux/setup)

Use `flutter doctor -v` para verificar o ambiente. Para gerar uma versão de distribuição, rode `flutter build windows` no Windows ou `flutter build linux` no Linux.

Para usar a versão web em outro navegador, inicie o servidor do Flutter.

```bash
flutter run -d web-server
```

Abra no Brave, Vivaldi ou outro navegador o endereço exibido no terminal.

A aba **Produtos** abre com sete exemplos. Em **Encomendas** você pode criar um pedido, gerenciar os itens e conferir o orçamento. Os dados ficam em memória durante a execução.

## Estrutura e regras

- As 11 classes do sistema do 3º bimestre estão em `lib/models`, uma por arquivo. Os modelos usam parâmetros nomeados com `required`, campos opcionais com `?` e campos privados acessados por getters.
- `lib/main.dart` contém a lista inicial de produtos e a interface que usa `ListView.builder`, `Card` e `ListTile`. Cada produto mostra nome, categoria e preço base.
- O valor de um item é a quantidade multiplicada pelo preço unitário registrado, somada ao adicional total do item. O orçamento soma os valores dos itens. Uma mudança no cardápio afeta novos itens. Editar e salvar um item existente atualiza seu preço.
- A encomenda deve ter pelo menos um item e a entrega deve ocorrer na data do pedido ou depois dela.

## Uso de IA

A IA foi utilizada para entender conceitos de Dart e auxiliar na organização do repositório, por exemplo, na criação deste README.
