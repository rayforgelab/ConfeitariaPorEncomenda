

package model;

public class ItemEncomenda {

    private int id;
    private int quantidade;
    private double valoresAdicionais;
    private ProdutoConfeitaria produto;

    public ItemEncomenda(int id, int quantidade, double valoresAdicionais, ProdutoConfeitaria produto) {
        this.id = id;
        this.quantidade = quantidade;
        this.valoresAdicionais = valoresAdicionais;
        this.produto = produto;
    }

    public double calcularValorItem() {
        return (quantidade * produto.getPrecoBase()) + valoresAdicionais;
    }

    public int getId() {
        return id;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public ProdutoConfeitaria getProduto() {
        return produto;
    }

    @Override
    public String toString() {
        return String.format("%s - %d | Id: %d | Quantidade: %d | Preco: %f | Preco Adicionais: %f", produto.getNome(), produto.getId(), id, quantidade, calcularValorItem(), valoresAdicionais);
    }
}
