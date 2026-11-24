import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';

import 'package:confeitaria_por_encomenda/main.dart';
import 'package:confeitaria_por_encomenda/models/cliente.dart';
import 'package:confeitaria_por_encomenda/models/doce.dart';
import 'package:confeitaria_por_encomenda/models/encomenda.dart';
import 'package:confeitaria_por_encomenda/models/item_encomenda.dart';

void main() {
  test('O orçamento preserva o preço registrado no item', () {
    final doce = Doce(
      id: 1,
      nome: 'Brigadeiros',
      precoBase: 20,
      unidadesPorPorcao: 12,
    );
    final cliente = Cliente(id: 1, nome: 'Cliente teste');
    final pedido = Encomenda(
      id: 1,
      cliente: cliente,
      dataPedido: DateTime(2026, 11, 23),
      dataEntrega: DateTime(2026, 11, 25),
      status: 'Recebida',
    );
    final item = ItemEncomenda(
      id: 1,
      quantidade: 3,
      produto: doce,
      valoresAdicionais: 5,
    );
    pedido.adicionarItem(item);

    expect(item.calcularUnidades(), 36);
    expect(pedido.calcularValorTotal(), 65);
    expect(() => pedido.adicionarItem(item), throwsArgumentError);
    expect(() => pedido.removerItem(item.id), throwsStateError);

    doce.precoBase = 22;
    expect(pedido.calcularValorTotal(), 65);
    pedido.atualizarItem(
      ItemEncomenda(id: 1, quantidade: 3, produto: doce, valoresAdicionais: 5),
    );
    expect(pedido.calcularValorTotal(), 71);
  });

  testWidgets('O cardápio abre com sete produtos e filtra por tema', (
    tester,
  ) async {
    await tester.pumpWidget(const ConfeitariaApp());

    expect(find.text(nomeSistema), findsOneWidget);
    expect(find.byType(Card), findsWidgets);
    final lista = tester.widget<ListView>(find.byType(ListView));
    expect(lista.childrenDelegate.estimatedChildCount, 7);
    expect(find.text('Bolo de chocolate'), findsOneWidget);
    expect(find.text('Bolo  •  R\$ 65,00'), findsOneWidget);

    await tester.tap(find.byType(DropdownButton<String>));
    await tester.pumpAndSettle();
    await tester.tap(find.text('Bolo Temático').last);
    await tester.pumpAndSettle();
    await tester.enterText(find.byType(TextField), 'ESPAÇO');
    await tester.pumpAndSettle();

    expect(find.text('Bolo céu estrelado'), findsOneWidget);
    expect(find.text('Bolo de chocolate'), findsNothing);
    await tester.enterText(find.byType(TextField), 'Selva');
    await tester.pumpAndSettle();
    expect(find.text('Nenhum produto encontrado.'), findsOneWidget);
  });

  testWidgets('Cria uma encomenda com quantidade e adicional', (tester) async {
    await tester.pumpWidget(const ConfeitariaApp());

    await tester.tap(find.text('Encomendas'));
    await tester.pumpAndSettle();
    await tester.tap(find.text('Nova encomenda'));
    await tester.pumpAndSettle();

    final quantidade = find.byKey(const ValueKey('Quantidade'));
    await tester.ensureVisible(quantidade);
    await tester.enterText(quantidade, '0');
    await tester.tap(find.text('Salvar'));
    await tester.pumpAndSettle();
    expect(
      find.text('Digite um número inteiro maior que zero.'),
      findsOneWidget,
    );

    await tester.enterText(quantidade, '2');
    final adicional = find.byKey(const ValueKey('Adicional total (R\$)'));
    await tester.ensureVisible(adicional);
    await tester.enterText(adicional, '5,50');
    await tester.tap(find.text('Salvar'));
    await tester.pumpAndSettle();

    await tester.tap(find.text('#2 · Marina Lopes'));
    await tester.pumpAndSettle();
    expect(find.text('Total: R\$ 135,50'), findsOneWidget);
    expect(find.text('Bolo de chocolate'), findsOneWidget);

    await tester.tap(find.byTooltip('Remover item'));
    await tester.pumpAndSettle();
    expect(
      find.text('A encomenda precisa manter pelo menos um item.'),
      findsOneWidget,
    );
  });
}
