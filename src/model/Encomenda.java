package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
// E4 - STRICT recusa datas impossíveis, como 31/02, sem ajustá-las silenciosamente.
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Encomenda {

    // E4 - uuuu permite validar o ano estritamente. A digitação continua dia/mês/ano.
    public static final DateTimeFormatter FORMATO = DateTimeFormatter
            .ofPattern("dd/MM/uuuu HH:mm").withResolverStyle(ResolverStyle.STRICT);

    private int id;
    private Cliente cliente;
    private LocalDateTime dataPedido;
    private LocalDateTime dataEntrega;
    private String status;
    private String observacao;
    private List<ItemEncomenda> itens;

    public Encomenda(int id, Cliente cliente, LocalDateTime dataPedido, LocalDateTime dataEntrega, String status, String observacao) {
        /* E4 - Valida antes de criar: a encomenda precisa de cliente e datas
         * coerentes. Não acrescentamos prazos de produção que a E1 não definiu. */
        if (cliente == null || dataPedido == null || dataEntrega == null) {
            throw new IllegalArgumentException("Informe o cliente e as datas da encomenda.");
        }
        if (dataEntrega.isBefore(dataPedido)) {
            throw new IllegalArgumentException("A entrega não pode ser anterior ao pedido.");
        }
        this.id = id;
        this.cliente = cliente;
        this.dataPedido = dataPedido;
        this.dataEntrega = dataEntrega;
        this.status = status;
        this.observacao = observacao;
        this.itens = new ArrayList<>();
    }

    public void adicionarItem(ItemEncomenda item) {
        // E4 - Não aceita item nulo nem ID repetido dentro da mesma encomenda.
        if (item == null) {
            throw new IllegalArgumentException("Informe um item válido.");
        }
        for (ItemEncomenda existente : itens) {
            if (existente.getId() == item.getId()) {
                throw new IllegalArgumentException("Já existe um item com esse ID na encomenda.");
            }
        }
        itens.add(item);
    }

    public boolean atualizarItem(ItemEncomenda itemAtualizado) {
        // E4 - Evita tentar acessar um item inexistente e lançar NullPointerException.
        if (itemAtualizado == null) {
            throw new IllegalArgumentException("Informe um item válido.");
        }
        for (int i = 0; i < itens.size(); i++) {
            if (itens.get(i).getId() == itemAtualizado.getId()) {
                itens.set(i, itemAtualizado);
                return true;
            }
        }
        return false;
    }

    public boolean removerItem(int idItem) {
        for (int i = 0; i < itens.size(); i++) {
            if (itens.get(i).getId() == idItem) {
                itens.remove(i);
                return true;
            }
        }
        return false;
    }

    /* ======================================================================
     * E4 - REGRA DE NEGÓCIO DA E1: ORÇAMENTO DA CONFEITARIA
     * Cada item inclui quantidade, preço unitário e custo das personalizações.
     * O total da encomenda é a soma desses valores, sem taxas inventadas.
     * A fórmula já existia no E3; agora aparece no console e é usada no fluxo
     * completo de montagem do pedido, inclusive com vários produtos.
     * Não armazenamos um total fixo: calculamos novamente quando consultado,
     * refletindo inclusões, alterações e remoções de itens imediatamente.
     * ====================================================================== */
    public double calcularValorTotal() {
        double total = 0;
        for (ItemEncomenda item : itens) {
            total += item.calcularValorItem();
        }
        return total;
    }

    public int getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public LocalDateTime getDataPedido() {
        return dataPedido;
    }

    public LocalDateTime getDataEntrega() {
        return dataEntrega;
    }

    public List<ItemEncomenda> getItens() {
        return Collections.unmodifiableList(itens);
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDataEntrega(LocalDateTime dataEntrega) {
        // E4 - Atualizar a data precisa respeitar a mesma validação do cadastro.
        if (dataEntrega == null || dataEntrega.isBefore(dataPedido)) {
            throw new IllegalArgumentException("A entrega não pode ser anterior ao pedido.");
        }
        this.dataEntrega = dataEntrega;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    @Override
    public String toString() {
        StringBuilder produtos = new StringBuilder();

        for (ItemEncomenda item : itens) {
            if (produtos.length() > 0) {
                produtos.append(", ");
            }
            produtos.append(item.getProduto().getNome());
        }

        // E4 - Exibe o resultado da regra de negócio ao listar as encomendas.
        return String.format("%s - %d | Id: %d | %s - %s | Status: %s | Obs.: %s | Produtos: %s | Total: R$ %.2f",
                cliente.getNome(), cliente.getId(), id, FORMATO.format(dataPedido),
                FORMATO.format(dataEntrega), status, observacao, produtos, calcularValorTotal());
    }
}
