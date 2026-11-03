package model;

public class ItemEncomenda {

    private int id;
    private int quantidade;
    private double valoresAdicionais;
    private ProdutoConfeitaria produto;

    private double precoUnitario;

    public ItemEncomenda(int id, int quantidade, double valoresAdicionais, ProdutoConfeitaria produto) {
        if (produto == null || quantidade <= 0 || !Double.isFinite(valoresAdicionais)
                || valoresAdicionais < 0) {
            throw new IllegalArgumentException("Informe produto, quantidade positiva e adicional não negativo.");
        }
        this.id = id;
        this.quantidade = quantidade;
        this.valoresAdicionais = valoresAdicionais;
        this.produto = produto;
        this.precoUnitario = produto.getPrecoBase();
    }

    public double calcularValorItem() {
        return (quantidade * precoUnitario) + valoresAdicionais;
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

    public double getPrecoUnitario() {
        return precoUnitario;
    }

    @Override
    public String toString() {
        return String.format("%s - %d | Id do item: %d | Quantidade: %d | Unitário: R$ %.2f | Adicionais totais: R$ %.2f | Total do item: R$ %.2f",
                produto.getNome(), produto.getId(), id, quantidade,
                precoUnitario, valoresAdicionais, calcularValorItem());
    }
}
