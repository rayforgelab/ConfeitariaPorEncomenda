

package model;

public class BoloTematico extends Bolo {

    private int camadas;
    private String tema;
    private String referencia;
    private String objetosTematicosDecorativos;

    public BoloTematico(int id, String nome, double precoBase, String tamanho, String sabor, String recheio, String cobertura, int camadas, String tema, String referencia, String objetosTematicosDecorativos) {
        super(id, nome, precoBase, tamanho, sabor, recheio, cobertura);
        this.camadas = camadas;
        this.tema = tema;
        this.referencia = referencia;
        this.objetosTematicosDecorativos = objetosTematicosDecorativos;
    }

    @Override
    public String getCategoria() {
        return "Bolo Temático";
    }

    public int getCamadas() {
        return camadas;
    }

    public String getTema() {
        return tema;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | Camadas: %d | Tema: %s | Referência: %s | Objetos decorativos: %s", camadas, tema, referencia, objetosTematicosDecorativos);
    }
}
