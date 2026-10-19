

package model;

public class Bolo extends ProdutoConfeitaria {

    private String tamanho;
    private String sabor;
    private String recheio;
    private String cobertura;

    public Bolo(int id, String nome, double precoBase, String tamanho, String sabor, String recheio, String cobertura) {
        super(id, nome, precoBase);
        this.tamanho = tamanho;
        this.sabor = sabor;
        this.recheio = recheio;
        this.cobertura = cobertura;
    }

    @Override
    public String getCategoria() {
        return "Bolo";
    }

    public String getTamanho() {
        return tamanho;
    }

    public String getSabor() {
        return sabor;
    }

    public String getRecheio() {
        return recheio;
    }

    public String getCobertura() {
        return cobertura;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(
                "%n      Tamanho: %s | Massa: %s | Recheio: %s | Cobertura: %s",
                tamanho, sabor, recheio, cobertura);
    }
}
