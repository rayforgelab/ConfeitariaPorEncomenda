package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class TestesE4 {
    private static int verificacoes = 0;

    private static void verificar(boolean condicao, String descricao) {
        verificacoes++;
        if (!condicao) {
            throw new AssertionError(descricao);
        }
    }

    private static void verificarValor(double esperado, double obtido, String descricao) {
        verificar(Math.abs(esperado - obtido) < 0.000001, descricao);
    }

    private static void deveRecusar(Runnable operacao, String descricao) {
        boolean recusou = false;
        try {
            operacao.run();
        } catch (IllegalArgumentException erro) {
            recusou = true;
        }
        verificar(recusou, descricao);
    }

    public static void main(String[] args) {
        Cliente cliente = new Cliente(1, "Cliente teste", "000", "999", "teste@email.com");
        LocalDateTime pedido = LocalDateTime.of(2026, 11, 2, 10, 0);
        Encomenda encomenda = new Encomenda(1, cliente, pedido, pedido.plusDays(1), "Recebida", "");
        ProdutoConfeitaria bolo = new Bolo(1, "Bolo", 50, "M", "Chocolate", "Brigadeiro", "Ganache");
        ProdutoConfeitaria doce = new Doce(2, "Brigadeiro", 2, "Tradicional", 1);

        ItemEncomenda itemBolo = new ItemEncomenda(1, 2, 15, bolo);
        ItemEncomenda itemDoce = new ItemEncomenda(2, 10, 0, doce);
        verificarValor(115, itemBolo.calcularValorItem(), "Adicional deve ser somado uma única vez");
        verificarValor(20, itemDoce.calcularValorItem(), "Item sem adicional");
        encomenda.adicionarItem(itemBolo);
        encomenda.adicionarItem(itemDoce);
        verificarValor(135, encomenda.calcularValorTotal(), "Soma dos itens");

        bolo.setPrecoBase(80);
        verificarValor(50, itemBolo.getPrecoUnitario(), "Preço unitário registrado");
        verificarValor(115, itemBolo.calcularValorItem(), "Item anterior deve manter seu valor");
        verificarValor(135, encomenda.calcularValorTotal(), "Pedido anterior deve manter seu orçamento");
        ItemEncomenda atualizado = new ItemEncomenda(1, 2, 15, bolo);
        verificarValor(175, atualizado.calcularValorItem(), "Novo orçamento usa preço atual");
        verificar(encomenda.atualizarItem(atualizado), "Atualizar item existente");
        verificarValor(195, encomenda.calcularValorTotal(), "Total após atualizar item");
        verificar(encomenda.removerItem(2), "Remover item existente");
        verificarValor(175, encomenda.calcularValorTotal(), "Total após remover item");
        verificar(!encomenda.removerItem(999), "Remover item inexistente retorna false");
        verificar(!encomenda.atualizarItem(new ItemEncomenda(999, 1, 0, doce)),
                "Atualizar item inexistente retorna false");
        deveRecusar(() -> encomenda.adicionarItem(atualizado), "Recusar ID de item repetido");
        deveRecusar(() -> encomenda.adicionarItem(null), "Recusar item nulo");
        deveRecusar(() -> encomenda.atualizarItem(null), "Recusar atualização nula");

        deveRecusar(() -> new ItemEncomenda(3, 0, 0, bolo), "Quantidade zero");
        deveRecusar(() -> new ItemEncomenda(3, -1, 0, bolo), "Quantidade negativa");
        deveRecusar(() -> new ItemEncomenda(3, 1, -1, bolo), "Adicional negativo");
        deveRecusar(() -> new ItemEncomenda(3, 1, Double.NaN, bolo), "Adicional NaN");
        deveRecusar(() -> new ItemEncomenda(3, 1, Double.POSITIVE_INFINITY, bolo), "Adicional infinito");
        deveRecusar(() -> new ItemEncomenda(3, 1, 0, null), "Produto ausente");
        deveRecusar(() -> bolo.setPrecoBase(-1), "Preço negativo");
        deveRecusar(() -> bolo.setPrecoBase(Double.NaN), "Preço NaN");
        deveRecusar(() -> bolo.setPrecoBase(Double.POSITIVE_INFINITY), "Preço infinito");
        verificarValor(80, bolo.getPrecoBase(), "Tentativas recusadas preservam o preço anterior");
        ProdutoConfeitaria gratuito = new Salgado(3, "Amostra", 0, "Coxinha", 1);
        verificarValor(0, new ItemEncomenda(3, 1, 0, gratuito).calcularValorItem(), "Preço zero permitido");
        verificarValor(0.3, new ItemEncomenda(4, 3, 0,
                new Doce(4, "Doce", 0.1, "Teste", 1)).calcularValorItem(), "Valor decimal");

        deveRecusar(() -> new Encomenda(2, cliente, pedido, pedido.minusMinutes(1), "", ""),
                "Entrega anterior ao pedido");
        deveRecusar(() -> new Encomenda(2, null, pedido, pedido.plusDays(1), "", ""), "Cliente ausente");
        deveRecusar(() -> encomenda.setDataEntrega(pedido.minusDays(1)), "Reagendamento inválido");
        verificar(encomenda.getDataEntrega().equals(pedido.plusDays(1)), "Data preservada após recusa");
        encomenda.setDataEntrega(pedido.plusDays(2));
        verificar(encomenda.getDataEntrega().equals(pedido.plusDays(2)), "Reagendamento válido");
        boolean dataInvalida = false;
        try {
            LocalDateTime.parse("31/02/2026 10:00", Encomenda.FORMATO);
        } catch (DateTimeParseException erro) {
            dataInvalida = true;
        }
        verificar(dataInvalida, "Data impossível deve ser recusada");
        verificar(LocalDateTime.parse("29/02/2028 10:00", Encomenda.FORMATO).getDayOfMonth() == 29,
                "Data válida em ano bissexto");

        boolean listaProtegida = false;
        try {
            encomenda.getItens().clear();
        } catch (UnsupportedOperationException erro) {
            listaProtegida = true;
        }
        verificar(listaProtegida, "Lista externa não deve ser modificável");

        ProdutoConfeitaria[] tipos = {
            bolo, doce, gratuito,
            new BoloACombinar(5, "A combinar", 70, "Descrição"),
            new BoloPersonalizado(6, "Personalizado", 80, "M", "A", "B", "C", 2, true, "Não", "Não"),
            new BoloCasamento(7, "Casamento", 100, "G", "A", "B", "C", 3, true, "Flores", "Não", "Não"),
            new BoloTematico(8, "Temático", 90, "M", "A", "B", "C", 2, "Festa", "Foto", "Topo")
        };
        String[] categorias = {"Bolo", "Doce", "Salgado", "Bolo a Combinar", "Bolo Personalizado",
                "Bolo de Casamento", "Bolo Temático"};
        for (int i = 0; i < tipos.length; i++) {
            verificar(tipos[i].getCategoria().equals(categorias[i]), "Categoria polimórfica " + categorias[i]);
            verificar(tipos[i].toString().contains(categorias[i]), "Exibição polimórfica " + categorias[i]);
        }
        System.out.println("E4: " + verificacoes + " verificações passaram.");
    }
}
