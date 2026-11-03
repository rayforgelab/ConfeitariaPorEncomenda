package model;

public class ItemEncomenda {

    private int id;
    private int quantidade;
    private double valoresAdicionais;
    private ProdutoConfeitaria produto;

    /* ======================================================================
     * E4 - PREÇO UNITÁRIO DO ORÇAMENTO
     * Guardamos o preço vigente quando este item é montado. Alterar o preço
     * do cardápio depois não deve alterar os orçamentos já registrados.
     * Este atributo corresponde ao precoUnitario descrito na proposta E1.
     * ====================================================================== */
    private double precoUnitario;

    public ItemEncomenda(int id, int quantidade, double valoresAdicionais, ProdutoConfeitaria produto) {
        /* E4 - A interface repete a pergunta se o valor for inválido. A classe
         * também se protege: quantidade positiva, produto e adicional finito
         * não negativo. Zero é permitido para um item sem personalizações pagas. */
        if (produto == null || quantidade <= 0 || !Double.isFinite(valoresAdicionais)
                || valoresAdicionais < 0) {
            throw new IllegalArgumentException("Informe produto, quantidade positiva e adicional não negativo.");
        }
        this.id = id;
        this.quantidade = quantidade;
        this.valoresAdicionais = valoresAdicionais;
        this.produto = produto;
        // E4 - Copia o preço neste momento, em vez de consultá-lo a cada cálculo.
        this.precoUnitario = produto.getPrecoBase();
    }

    /* ======================================================================
     * E4 - CÁLCULO DO ITEM CONFORME A E1
     * Valor do item = (quantidade x preço unitário) + adicionais totais.
     * Ex.: 2 bolos de R$ 50 + R$ 15 de decoração = R$ 115.
     * O adicional é o TOTAL deste item, somado uma única vez.
     * Para cobrar R$ 5 por unidade em 3 unidades, informe R$ 15 de adicional.
     * ====================================================================== */
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

    // E4 - Permite consultar o preço registrado, que pode diferir do cardápio atual.
    public double getPrecoUnitario() {
        return precoUnitario;
    }

    @Override
    public String toString() {
        // E4 - Mostra a composição do orçamento, deixando claro o que foi cobrado.
        return String.format("%s - %d | Id do item: %d | Quantidade: %d | Unitário: R$ %.2f | Adicionais totais: R$ %.2f | Total do item: R$ %.2f",
                produto.getNome(), produto.getId(), id, quantidade,
                precoUnitario, valoresAdicionais, calcularValorItem());
    }
}
