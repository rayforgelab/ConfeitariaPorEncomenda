package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    static Scanner sc = new Scanner(System.in);

    static ArrayList<Cliente> clientes = new ArrayList<>();
    static ArrayList<ProdutoConfeitaria> produtos = new ArrayList<>();
    static ArrayList<Encomenda> encomendas = new ArrayList<>();

    static int proxCliente = 1;
    static int proxProduto = 1;
    static int proxEncomenda = 1;

    public static void main(String[] args) {
        try {
            executarSistema();
        } catch (NoSuchElementException e) {
            System.out.println("\nEntrada encerrada. Finalizando o sistema.");
        } finally {
            sc.close();
        }
    }

    static void executarSistema() {
        clientes.add(new Cliente(proxCliente++, "Cliente padrão", "00000000000", "11999999999", "cliente@email.com"));
        produtos.add(new Bolo(proxProduto++, "Bolo padrão", 50.0, "Médio", "Chocolate", "Brigadeiro", "Ganache"));

        Encomenda encomenda = new Encomenda(proxEncomenda++, clientes.get(0), LocalDateTime.now().withSecond(0).withNano(0),
                LocalDateTime.now().withSecond(0).withNano(0).plusDays(3), "Recebida", "Encomenda padrão");
        encomenda.adicionarItem(new ItemEncomenda(1, 1, 0, produtos.get(0)));
        encomendas.add(encomenda);

        int op;

        do {
            System.out.println("\n===== CONFEITARIA =====");
            System.out.println("1 - Clientes");
            System.out.println("2 - Produtos");
            System.out.println("3 - Encomendas");
            System.out.println("0 - Sair");
            op = lerIntEntre("Opção: ", 0, 3);

            if (op == 1) {
                menuClientes();
            } else if (op == 2) {
                menuProdutos();
            } else if (op == 3) {
                menuEncomendas();
            } else if (op == 0) {
                System.out.println("Encerrando...");
            } else {
                System.out.println("Opção inválida.");
            }
        } while (op != 0);
    }

    static void menuClientes() {
        int op;

        do {
            System.out.println("\n--- Clientes ---");
            System.out.println("1 - Cadastrar");
            System.out.println("2 - Listar");
            System.out.println("3 - Atualizar");
            System.out.println("4 - Excluir");
            System.out.println("0 - Voltar");
            op = lerIntEntre("Opção: ", 0, 4);

            switch (op) {
                case 1:
                    cadastrarCliente();
                    break;
                case 2:
                    listarClientes();
                    break;
                case 3:
                    atualizarCliente();
                    break;
                case 4:
                    excluirCliente();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        } while (op != 0);
    }

    static void cadastrarCliente() {
        String nome = lerTextoObrigatorio("Nome: ");
        String cpf = lerTexto("CPF: ");
        String telefone = lerTexto("Telefone: ");
        String email = lerTexto("E-mail: ");

        clientes.add(new Cliente(proxCliente, nome, cpf, telefone, email));
        proxCliente++;
        System.out.println("Cliente cadastrado!");
    }

    static void listarClientes() {
        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado.");
            return;
        }
        for (Cliente c : clientes) {
            System.out.println(c);
        }
    }

    static void atualizarCliente() {
        Cliente c = buscarCliente(lerInt("ID do cliente: "));

        if (c == null) {
            System.out.println("Cliente não encontrado.");
            return;
        }

        c.setNome(lerTextoObrigatorio("Novo nome: "));
        c.setTelefone(lerTexto("Novo telefone: "));
        c.setEmail(lerTexto("Novo e-mail: "));
        System.out.println("Cliente atualizado!");
    }

    static void excluirCliente() {
        Cliente c = buscarCliente(lerInt("ID do cliente: "));

        if (c == null) {
            System.out.println("Cliente não encontrado.");
        } else if (clienteTemEncomenda(c)) {
            System.out.println("Não dá pra excluir, o cliente tem encomendas.");
        } else {
                clientes.remove(c);
            System.out.println("Cliente excluído!");
        }
    }

    static void menuProdutos() {
        int op;

        do {
            System.out.println("\n--- Produtos ---");
            System.out.println("1 - Cadastrar");
            System.out.println("2 - Listar");
            System.out.println("3 - Atualizar");
            System.out.println("4 - Excluir");
            System.out.println("0 - Voltar");
            op = lerIntEntre("Opção: ", 0, 4);

            switch (op) {
                case 1:
                    cadastrarProduto();
                    break;
                case 2:
                    listarProdutos();
                    break;
                case 3:
                    atualizarProduto();
                    break;
                case 4:
                    excluirProduto();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        } while (op != 0);
    }

    static void cadastrarProduto() {
        System.out.println("1 - Bolo comum");
        System.out.println("2 - Bolo a combinar");
        System.out.println("3 - Bolo de casamento");
        System.out.println("4 - Bolo personalizado");
        System.out.println("5 - Bolo temático");
        System.out.println("6 - Doce");
        System.out.println("7 - Salgado");
        int tipo = lerIntEntre("Tipo: ", 1, 7);

        String nome = lerTextoObrigatorio("Nome: ");
        double preco = lerValorNaoNegativo("Preço: ");

        String tamanho = "";
        String sabor = "";
        String recheio = "";
        String cobertura = "";

        if (tipo == 1 || tipo == 3 || tipo == 4 || tipo == 5) {
            tamanho = lerTexto("Tamanho: ");
            sabor = lerTexto("Sabor: ");
            recheio = lerTexto("Recheio: ");
            cobertura = lerTexto("Cobertura: ");
        }

        ProdutoConfeitaria novo = null;

        switch (tipo) {
            case 1:
                novo = new Bolo(proxProduto, nome, preco, tamanho, sabor, recheio, cobertura);
                break;
            case 2: {
                String descricao = lerTexto("Descrição: ");
                novo = new BoloACombinar(proxProduto, nome, preco, descricao);
                break;
            }
            case 3: {
                int camadas = lerIntPositivo("Camadas: ");
                boolean bonecos = lerSimNao("Bonecos decorativos no topo? (S/N): ");
                String decoracao = lerTexto("Decoração: ");
                String frutas = lerTexto("Frutas: ");
                String biscoito = lerTexto("Biscoito: ");
                novo = new BoloCasamento(proxProduto, nome, preco, tamanho, sabor, recheio, cobertura,
                        camadas, bonecos, decoracao, frutas, biscoito);
                break;
            }
            case 4: {
                int camadas = lerIntPositivo("Camadas: ");
                boolean caixa = lerSimNao("Caixa enfeitada? (S/N): ");
                String frutas = lerTexto("Frutas: ");
                String biscoito = lerTexto("Biscoito: ");
                novo = new BoloPersonalizado(proxProduto, nome, preco, tamanho, sabor, recheio, cobertura,
                        camadas, caixa, frutas, biscoito);
                break;
            }
            case 5: {
                int camadas = lerIntPositivo("Camadas: ");
                String tema = lerTexto("Tema: ");
                String referencia = lerTexto("Referência: ");
                String objetos = lerTexto("Objetos decorativos: ");
                novo = new BoloTematico(proxProduto, nome, preco, tamanho, sabor, recheio, cobertura,
                        camadas, tema, referencia, objetos);
                break;
            }
            case 6: {
                String tipoDoce = lerTexto("Tipo do doce: ");
                int unidades = lerIntPositivo("Unidades por porção: ");
                novo = new Doce(proxProduto, nome, preco, tipoDoce, unidades);
                break;
            }
            case 7: {
                String tipoSalgado = lerTexto("Tipo do salgado: ");
                int unidades = lerIntPositivo("Unidades por porção: ");
                novo = new Salgado(proxProduto, nome, preco, tipoSalgado, unidades);
                break;
            }
        }

        produtos.add(novo);
        proxProduto++;
        System.out.println("Produto cadastrado!");
    }

    static void listarProdutos() {
        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado.");
            return;
        }

        boolean filtrar = lerSimNao("Filtrar por tipo de produto? (S/N): ");

        if (!filtrar) {
            for (ProdutoConfeitaria p : produtos) {
                System.out.println(p);
            }
            return;
        }

        System.out.println("1 - Bolo comum");
        System.out.println("2 - Bolo a combinar");
        System.out.println("3 - Bolo de casamento");
        System.out.println("4 - Bolo personalizado");
        System.out.println("5 - Bolo temático");
        System.out.println("6 - Doce");
        System.out.println("7 - Salgado");
        int tipo = lerIntEntre("Tipo: ", 1, 7);

        String tema = "";
        if (tipo == 5) {
            tema = lerTexto("Tema (Enter para todos): ");
        }

        boolean achou = false;

        for (ProdutoConfeitaria p : produtos) {
            boolean mostrar = false;

            switch (tipo) {
                case 1:
                    mostrar = p.getCategoria().equals("Bolo");
                    break;
                case 2:
                    mostrar = p instanceof BoloACombinar;
                    break;
                case 3:
                    mostrar = p instanceof BoloCasamento;
                    break;
                case 4:
                    mostrar = p instanceof BoloPersonalizado;
                    break;
                case 5:
                    if (p instanceof BoloTematico) {
                        BoloTematico bt = (BoloTematico) p;
                        mostrar = tema.isEmpty() || bt.getTema().equalsIgnoreCase(tema);
                    }
                    break;
                case 6:
                    mostrar = p instanceof Doce;
                    break;
                case 7:
                    mostrar = p instanceof Salgado;
                    break;
                default:
                    System.out.println("Tipo inválido.");
                    return;
            }

            if (mostrar) {
                System.out.println(p);
                achou = true;
            }
        }

        if (!achou) {
            System.out.println("Nenhum produto encontrado para esse filtro.");
        }
    }

    static void atualizarProduto() {
        ProdutoConfeitaria p = buscarProduto(lerInt("ID do produto: "));

        if (p == null) {
            System.out.println("Produto não encontrado.");
            return;
        }

        p.setNome(lerTextoObrigatorio("Novo nome: "));
        p.setPrecoBase(lerValorNaoNegativo("Novo preço: "));
        System.out.println("Produto atualizado!");
    }

    static void excluirProduto() {
        ProdutoConfeitaria p = buscarProduto(lerInt("ID do produto: "));

        if (p == null) {
            System.out.println("Produto não encontrado.");
        } else if (produtoEmEncomenda(p)) {
            System.out.println("Não dá pra excluir, o produto está em uma encomenda.");
        } else {
            produtos.remove(p);
            System.out.println("Produto excluído!");
        }
    }

    static void menuEncomendas() {
        int op;

        do {
            System.out.println("\n--- Encomendas ---");
            System.out.println("1 - Criar");
            System.out.println("2 - Listar");
            System.out.println("3 - Atualizar");
            System.out.println("4 - Excluir");
            System.out.println("5 - Gerenciar itens e consultar orçamento");
            System.out.println("0 - Voltar");
            op = lerIntEntre("Opção: ", 0, 5);

            switch (op) {
                case 1:
                    criarEncomenda();
                    break;
                case 2:
                    listarEncomendas();
                    break;
                case 3:
                    atualizarEncomenda();
                    break;
                case 4:
                    excluirEncomenda();
                    break;
                case 5:
                    gerenciarItens();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        } while (op != 0);
    }

    static void criarEncomenda() {
        if (clientes.isEmpty() || produtos.isEmpty()) {
            System.out.println("Cadastre pelo menos um cliente e um produto antes.");
            return;
        }

        listarClientes();
        Cliente cliente = buscarCliente(lerInt("ID do cliente: "));

        listarProdutos();
        ProdutoConfeitaria produto = buscarProduto(lerInt("ID do produto: "));

        if (cliente == null || produto == null) {
            System.out.println("Cliente ou produto não encontrado.");
            return;
        }

        LocalDateTime dataPedido = LocalDateTime.now().withSecond(0).withNano(0);
        LocalDateTime dataEntrega = lerData("Entrega (dd/MM/yyyy HH:mm): ");
        String obs = lerTexto("Observação: ");
        int qtd = lerIntPositivo("Quantidade: ");
        double adicional = lerValorNaoNegativo("Valor adicional total do item: ");
        ItemEncomenda item = new ItemEncomenda(1, qtd, adicional, produto);

        while (true) {
            try {
                Encomenda e = new Encomenda(proxEncomenda, cliente, dataPedido, dataEntrega,
                        "Recebida", obs);
                e.adicionarItem(item);
                encomendas.add(e);
                proxEncomenda++;
                System.out.println("Encomenda criada! Total: R$ "
                        + String.format("%.2f", e.calcularValorTotal()));
                return;
            } catch (IllegalArgumentException erro) {
                System.out.println("Encomenda recusada: " + erro.getMessage());
                dataEntrega = lerData("Nova entrega (dd/MM/yyyy HH:mm): ");
            }
        }
    }

    static void listarEncomendas() {
        if (encomendas.isEmpty()) {
            System.out.println("Nenhuma encomenda cadastrada.");
            return;
        }

        for (Encomenda e : encomendas) {
            System.out.println(e);
        }
    }

    static void atualizarEncomenda() {
        Encomenda e = buscarEncomenda(lerInt("ID da encomenda: "));

        if (e == null) {
            System.out.println("Encomenda não encontrada.");
            return;
        }

        if (lerSimNao("Alterar também a data de entrega? (S/N): ")) {
            while (true) {
                LocalDateTime novaData = lerData("Nova entrega (dd/MM/yyyy HH:mm): ");
                try {
                    e.setDataEntrega(novaData);
                    break;
                } catch (IllegalArgumentException erro) {
                    System.out.println("Reagendamento recusado: " + erro.getMessage());
                }
            }
        }
        e.setStatus(lerTextoObrigatorio("Novo status: "));
        e.setObservacao(lerTexto("Nova observação: "));
        System.out.println("Encomenda atualizada!");
    }

    static void excluirEncomenda() {
        Encomenda e = buscarEncomenda(lerInt("ID da encomenda: "));

        if (e == null) {
            System.out.println("Encomenda não encontrada.");
        } else {
            encomendas.remove(e);
            System.out.println("Encomenda excluída!");
        }
    }

    static Cliente buscarCliente(int id) {
        for (Cliente c : clientes) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }

    static ProdutoConfeitaria buscarProduto(int id) {
        for (ProdutoConfeitaria p : produtos) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    static Encomenda buscarEncomenda(int id) {
        for (Encomenda e : encomendas) {
            if (e.getId() == id) {
                return e;
            }
        }
        return null;
    }

    static boolean clienteTemEncomenda(Cliente cliente) {
        for (Encomenda e : encomendas) {
            if (e.getCliente().getId() == cliente.getId()) {
                return true;
            }
        }
        return false;
    }

    static boolean produtoEmEncomenda(ProdutoConfeitaria produto) {
        for (Encomenda e : encomendas) {
            for (ItemEncomenda item : e.getItens()) {
                if (item.getProduto().getId() == produto.getId()) {
                    return true;
                }
            }
        }
        return false;
    }


    static void gerenciarItens() {
        Encomenda e = buscarEncomenda(lerInt("ID da encomenda: "));
        if (e == null) {
            System.out.println("Encomenda não encontrada.");
            return;
        }
        int op;
        do {
            System.out.println("\n--- Itens da encomenda " + e.getId() + " ---");
            for (ItemEncomenda item : e.getItens()) {
                System.out.println(item);
            }
            System.out.printf("Total da encomenda: R$ %.2f%n", e.calcularValorTotal());
            System.out.println("1 - Adicionar item");
            System.out.println("2 - Atualizar item usando o preço atual do cardápio");
            System.out.println("3 - Remover item");
            System.out.println("0 - Voltar");
            op = lerIntEntre("Opção: ", 0, 3);
            if (op == 1 || op == 2) {
                salvarItem(e, op == 2);
            } else if (op == 3) {
                if (e.getItens().size() == 1) {
                    System.out.println("A encomenda precisa manter pelo menos um item.");
                } else {
                    boolean removeu = e.removerItem(lerInt("ID do item: "));
                    System.out.println(removeu ? "Item removido!" : "Item não encontrado.");
                }
            }
        } while (op != 0);
    }

    static void salvarItem(Encomenda e, boolean atualizar) {
        int idItem = 1;
        for (ItemEncomenda item : e.getItens()) {
            if (item.getId() >= idItem) {
                idItem = item.getId() + 1;
            }
        }
        if (atualizar) {
            idItem = lerInt("ID do item para atualizar: ");
            boolean existe = false;
            for (ItemEncomenda item : e.getItens()) {
                if (item.getId() == idItem) {
                    existe = true;
                }
            }
            if (!existe) {
                System.out.println("Item não encontrado.");
                return;
            }
        }
        listarProdutos();
        ProdutoConfeitaria produto = buscarProduto(lerInt("ID do produto: "));
        if (produto == null) {
            System.out.println("Produto não encontrado.");
            return;
        }
        int quantidade = lerIntPositivo("Quantidade: ");
        double adicional = lerValorNaoNegativo("Valor adicional total do item: ");
        try {
            ItemEncomenda novo = new ItemEncomenda(idItem, quantidade, adicional, produto);
            if (atualizar) {
                e.atualizarItem(novo);
            } else {
                e.adicionarItem(novo);
            }
            System.out.println("Item salvo!");
        } catch (IllegalArgumentException erro) {
            System.out.println("Item recusado: " + erro.getMessage());
            System.out.println("Confira os valores e tente novamente.");
        }
    }

    static int lerInt(String msg) {
        while (true) {
            try (Scanner entrada = new Scanner(lerTexto(msg))) {
                if (!entrada.hasNext()) {
                    throw new InputMismatchException();
                }
                int valor = entrada.nextInt();
                if (entrada.hasNext()) {
                    throw new InputMismatchException();
                }
                return valor;
            } catch (InputMismatchException erro) {
                System.out.println("Entrada inválida. Digite um número inteiro, como 1 ou 2.");
            }
        }
    }

    static double lerDouble(String msg) {
        while (true) {
            String texto = lerTexto(msg).trim().replace(',', '.');
            try (Scanner entrada = new Scanner(texto).useLocale(Locale.US)) {
                if (!entrada.hasNext()) {
                    throw new InputMismatchException();
                }
                double valor = entrada.nextDouble();
                if (entrada.hasNext() || !Double.isFinite(valor)) {
                    throw new InputMismatchException();
                }
                return valor;
            } catch (InputMismatchException erro) {
                System.out.println("Entrada inválida. Digite um número, como 12,50 ou 12.50.");
            }
        }
    }

    static int lerIntEntre(String msg, int minimo, int maximo) {
        while (true) {
            int valor = lerInt(msg);
            if (valor >= minimo && valor <= maximo) {
                return valor;
            }
            System.out.println("Digite uma opção entre " + minimo + " e " + maximo + ".");
        }
    }

    static int lerIntPositivo(String msg) {
        while (true) {
            int valor = lerInt(msg);
            if (valor > 0) {
                return valor;
            }
            System.out.println("Digite um número inteiro maior que zero.");
        }
    }

    static double lerValorNaoNegativo(String msg) {
        while (true) {
            double valor = lerDouble(msg);
            if (valor >= 0) {
                return valor;
            }
            System.out.println("O valor não pode ser negativo. Digite zero ou um valor positivo.");
        }
    }

    static LocalDateTime lerData(String msg) {
        while (true) {
            try {
                return LocalDateTime.parse(lerTexto(msg).trim(), Encomenda.FORMATO);
            } catch (DateTimeParseException erro) {
                System.out.println("Data inválida. Use dd/MM/yyyy HH:mm, por exemplo 30/11/2026 14:30.");
            }
        }
    }

    static boolean lerSimNao(String msg) {
        while (true) {
            String resposta = lerTexto(msg).trim();
            if (resposta.equalsIgnoreCase("S")) {
                return true;
            }
            if (resposta.equalsIgnoreCase("N")) {
                return false;
            }
            System.out.println("Resposta inválida. Digite S para sim ou N para não.");
        }
    }

    static String lerTextoObrigatorio(String msg) {
        while (true) {
            String texto = lerTexto(msg).trim();
            if (!texto.isEmpty()) {
                return texto;
            }
            System.out.println("Este campo não pode ficar vazio.");
        }
    }

    static String lerTexto(String msg) {
        System.out.print(msg);
        return sc.nextLine();
    }
}
