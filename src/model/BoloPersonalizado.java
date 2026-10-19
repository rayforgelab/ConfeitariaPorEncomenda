

package model;

public class BoloPersonalizado extends Bolo {

    private int camadas;
    private boolean caixaEnfeitada;
    private String frutas;
    private String biscoito;

    public BoloPersonalizado(int id, String nome, double precoBase, String tamanho, String sabor, String recheio, String cobertura, int camadas, boolean caixaEnfeitada, String frutas, String biscoito) {
        super(id, nome, precoBase, tamanho, sabor, recheio, cobertura);
        this.camadas = camadas;
        this.caixaEnfeitada = caixaEnfeitada;
        this.frutas = frutas;
        this.biscoito = biscoito;
    }

    @Override
    public String getCategoria() {
        return "Bolo Personalizado";
    }

    public int getCamadas() {
        return camadas;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | Camadas: %d | Caixa enfeitada: %s | Frutas: %s | Biscoito: %s", camadas, caixaEnfeitada ? "Sim" : "Não", frutas, biscoito);
    }
}
