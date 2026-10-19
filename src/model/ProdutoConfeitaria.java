


package model;

public abstract class ProdutoConfeitaria {

    private int id;
    private String nome;
    private double precoBase;
    public abstract String getCategoria();

    public ProdutoConfeitaria(int id, String nome, double precoBase) {
        this.id = id;
        this.nome = nome;
        this.precoBase = precoBase;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public double getPrecoBase() {
        return precoBase;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setPrecoBase(double precoBase) {
        this.precoBase = precoBase;
    }

    @Override
    public String toString() {
        return String.format("%s - %s | Id: %d | Preço base: R$ %f", nome, getCategoria(), id, precoBase);
    }
}
