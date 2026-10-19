

package model;

public class Salgado extends ProdutoConfeitaria {

    private String tipo;
    private int unidadesPorPorcao;

    public Salgado(int id, String nome, double precoBase, String tipo, int unidadesPorPorcao) {
        super(id, nome, precoBase);
        this.tipo = tipo;
        this.unidadesPorPorcao = unidadesPorPorcao;
    }

    @Override 
    public String getCategoria() {
        return "Salgado";
    }

    public String getTipo() {
        return tipo;
    }

    public int getUnidadesPorPorcao() {
        return unidadesPorPorcao;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | Tipo: %s | Unidades/porcao: %d", tipo, unidadesPorPorcao);
    }
}
