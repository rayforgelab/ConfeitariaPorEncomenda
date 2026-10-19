

package model;

public class BoloACombinar extends ProdutoConfeitaria {

    private String descricao;

    public BoloACombinar(int id, String nome, double precoBase, String descricao) {
        super(id, nome, precoBase);
        this.descricao = descricao;
    }

    @Override 
    public String getCategoria() {
        return "Bolo a Combinar";
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | Descrição: %s", descricao);
    }
}
