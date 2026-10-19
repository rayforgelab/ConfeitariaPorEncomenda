

package model;

public class BoloCasamento extends Bolo {

    private int camadas;
    private boolean bonecosDecorativos;
    private String decoracao;
    private String frutas;
    private String biscoito;

    public BoloCasamento(int id, String nome, double precoBase, String tamanho, String sabor, String recheio, String cobertura, int camadas, boolean bonecosDecorativos, String decoracao, String frutas, String biscoito) {
        super(id, nome, precoBase, tamanho, sabor, recheio, cobertura);
        this.camadas = camadas;
        this.bonecosDecorativos = bonecosDecorativos;
        this.decoracao = decoracao;
        this.frutas = frutas;
        this.biscoito = biscoito;
    }

    @Override
    public String getCategoria() {
        return "Bolo de Casamento";
    }

    public int getCamadas() {
        return camadas;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | Camadas: %d | Bonecos no topo: %s | Decoração: %s | Frutas: %s | Biscoito: %s", camadas, bonecosDecorativos ? "Sim" : "Não", decoracao, frutas, biscoito);
    }
}
