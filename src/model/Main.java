package model;

import java.time.LocalDateTime;
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
        clientes.add(new Cliente(proxCliente++, "Cliente padrão", "00000000000", "11999999999", "cliente@email.com"));
        produtos.add(new Bolo(proxProduto++, "Bolo padrão", 50.0, "Médio", "Chocolate", "Brigadeiro", "Ganache"));

        Encomenda encomenda = new Encomenda(proxEncomenda++, clientes.get(0), LocalDateTime.now(),
                LocalDateTime.now().plusDays(3), "Recebida", "Encomenda padrão");
        encomenda.adicionarItem(new ItemEncomenda(1, 1, 0, produtos.get(0)));
        encomendas.add(encomenda);

        int op;

        do {
            System.out.println("\n===== CONFEITARIA =====");
            System.out.println("1 - Clientes");
            System.out.println("2 - Produtos");
            System.out.println("3 - Encomendas");
            System.out.println("0 - Sair");
            op = lerInt("Opção: ");

            if (op == 1) {
                menuClientes();
            } else if (op == 2) {
                menuBolos();
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
            op = lerInt("Opção: ");

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
        String nome = lerTexto("Nome: ");
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

        c.setNome(lerTexto("Novo nome: "));
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

    static void menuBolos() {
        int op;

        do {
            System.out.println("\n--- Produtos ---");
            System.out.println("1 - Cadastrar");
            System.out.println("2 - Listar");
            System.out.println("3 - Atualizar");
            System.out.println("4 - Excluir");
            System.out.println("0 - Voltar");
            op = lerInt("Opção: ");

            switch (op) {
                case 1:
                    cadastrarBolo();
                    break;
                case 2:
                    listarBolos();
                    break;
                case 3:
                    atualizarBolo();
                    break;
                case 4:
                    excluirBolo();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        } while (op != 0);
    }

    static void cadastrarBolo() {
        System.out.println("1 - Bolo comum");
        System.out.println("2 - Bolo a combinar");
        System.out.println("3 - Bolo de casamento");
        System.out.println("4 - Bolo personalizado");
        System.out.println("5 - Bolo temático");
        System.out.println("6 - Doce");
        System.out.println("7 - Salgado");
        int tipo = lerInt("Tipo: ");

        if (tipo < 1 || tipo > 7) {
            System.out.println("Tipo inválido.");
            return;
        }

        String nome = lerTexto("Nome: ");
        double preco = lerDouble("Preço: ");

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
                int camadas = lerInt("Camadas: ");
                boolean bonecos = lerSimNao("Bonecos decorativos no topo? (S/N): ");
                String decoracao = lerTexto("Decoração: ");
                String frutas = lerTexto("Frutas: ");
                String biscoito = lerTexto("Biscoito: ");
                novo = new BoloCasamento(proxProduto, nome, preco, tamanho, sabor, recheio, cobertura,
                        camadas, bonecos, decoracao, frutas, biscoito);
                break;
            }
            case 4: {
                int camadas = lerInt("Camadas: ");
                boolean caixa = lerSimNao("Caixa enfeitada? (S/N): ");
                String frutas = lerTexto("Frutas: ");
                String biscoito = lerTexto("Biscoito: ");
                novo = new BoloPersonalizado(proxProduto, nome, preco, tamanho, sabor, recheio, cobertura,
                        camadas, caixa, frutas, biscoito);
                break;
            }
            case 5: {
                int camadas = lerInt("Camadas: ");
                String tema = lerTexto("Tema: ");
                String referencia = lerTexto("Referência: ");
                String objetos = lerTexto("Objetos decorativos: ");
                novo = new BoloTematico(proxProduto, nome, preco, tamanho, sabor, recheio, cobertura,
                        camadas, tema, referencia, objetos);
                break;
            }
            case 6: {
                String tipoDoce = lerTexto("Tipo do doce: ");
                int unidades = lerInt("Unidades por porção: ");
                novo = new Doce(proxProduto, nome, preco, tipoDoce, unidades);
                break;
            }
            case 7: {
                String tipoSalgado = lerTexto("Tipo do salgado: ");
                int unidades = lerInt("Unidades por porção: ");
                novo = new Salgado(proxProduto, nome, preco, tipoSalgado, unidades);
                break;
            }
        }

        produtos.add(novo);
        proxProduto++;
        System.out.println("Produto cadastrado!");
    }

    static void listarBolos() {
        if (produtos.isEmpty()) {
            System.out.println("Nenhum bolo cadastrado.");
            return;
        }

        String resposta = lerTexto("Filtrar por tipo de bolo? (S/N): ").toUpperCase();

        if (!resposta.equals("S")) {
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
        int tipo = lerInt("Tipo: ");

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
            System.out.println("Nenhum bolo encontrado para esse filtro.");
        }
    }

    static void atualizarBolo() {
        ProdutoConfeitaria p = buscarProduto(lerInt("ID do bolo: "));

        if (p == null) {
            System.out.println("Bolo não encontrado.");
            return;
        }

        p.setNome(lerTexto("Novo nome: "));
        p.setPrecoBase(lerDouble("Novo preço: "));
        System.out.println("Bolo atualizado!");
    }

    static void excluirBolo() {
        ProdutoConfeitaria p = buscarProduto(lerInt("ID do bolo: "));

        if (p == null) {
            System.out.println("Bolo não encontrado.");
        } else if (produtoEmEncomenda(p)) {
            System.out.println("Não dá pra excluir, o bolo está em uma encomenda.");
        } else {
            produtos.remove(p);
            System.out.println("Bolo excluído!");
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
            System.out.println("0 - Voltar");
            op = lerInt("Opção: ");

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
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        } while (op != 0);
    }

    static void criarEncomenda() {
        if (clientes.isEmpty() || produtos.isEmpty()) {
            System.out.println("Cadastre pelo menos um cliente e um bolo antes.");
            return;
        }

        listarClientes();
        Cliente cliente = buscarCliente(lerInt("ID do cliente: "));

        listarBolos();
        ProdutoConfeitaria produto = buscarProduto(lerInt("ID do bolo: "));

        if (cliente == null || produto == null) {
            System.out.println("Cliente ou bolo não encontrado.");
            return;
        }

        LocalDateTime dataEntrega = LocalDateTime.parse(
                lerTexto("Entrega (dd/MM/yyyy HH:mm): "), Encomenda.FORMATO);
        String obs = lerTexto("Observação: ");
        int qtd = lerInt("Quantidade: ");
        double adicional = lerDouble("Valor adicional: ");

        Encomenda e = new Encomenda(proxEncomenda, cliente, LocalDateTime.now(), dataEntrega,
                "Recebida", obs);
        proxEncomenda++;

        e.adicionarItem(new ItemEncomenda(1, qtd, adicional, produto));
        encomendas.add(e);
        System.out.println("Encomenda criada!");
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

        e.setStatus(lerTexto("Novo status: "));
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

    static boolean lerSimNao(String msg) {
        return lerTexto(msg).equalsIgnoreCase("S");
    }

    static String lerTexto(String msg) {
        System.out.print(msg);
        return sc.nextLine();
    }

    static int lerInt(String msg) {
        return Integer.parseInt(lerTexto(msg));
    }

    static double lerDouble(String msg) {
        return Double.parseDouble(lerTexto(msg).replace(",", "."));
    }
}