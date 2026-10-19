

package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Encomenda {

    public static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private int id;
    private Cliente cliente;
    private LocalDateTime dataPedido;
    private LocalDateTime dataEntrega;
    private String status;
    private String observacao;
    private List<ItemEncomenda> itens;

    public Encomenda(int id, Cliente cliente, LocalDateTime dataPedido, LocalDateTime dataEntrega, String status, String observacao) {
        this.id = id;
        this.cliente = cliente;
        this.dataPedido = dataPedido;
        this.dataEntrega = dataEntrega;
        this.status = status;
        this.observacao = observacao;
        this.itens = new ArrayList<>();
    }

    public void adicionarItem(ItemEncomenda item) {
        itens.add(item);
    }

    public boolean atualizarItem(ItemEncomenda itemAtualizado) {
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

        return String.format("%s - %d | Id: %d | %s - %s | Status: %s | Obs.: %s | Produtos: %s", cliente.getNome(), cliente.getId(), id, FORMATO.format(dataPedido), FORMATO.format(dataEntrega), status, observacao, produtos);
    }
}
