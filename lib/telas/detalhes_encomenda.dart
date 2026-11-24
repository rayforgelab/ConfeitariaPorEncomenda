import 'package:flutter/material.dart';

import '../models/encomenda.dart';
import '../models/item_encomenda.dart';
import '../models/produto_confeitaria.dart';
import 'formularios.dart';

class DetalhesEncomenda extends StatefulWidget {
  const DetalhesEncomenda({
    super.key,
    required this.encomenda,
    required this.produtos,
  });

  final Encomenda encomenda;
  final List<ProdutoConfeitaria> produtos;

  @override
  State<DetalhesEncomenda> createState() => _DetalhesEncomendaState();
}

class _DetalhesEncomendaState extends State<DetalhesEncomenda> {
  Future<void> _salvarItem([ItemEncomenda? item]) async {
    final encomenda = widget.encomenda;
    var proximoId = 1;
    for (final existente in encomenda.itens) {
      if (existente.id >= proximoId) proximoId = existente.id + 1;
    }

    final salvo = await editarItem(
      context,
      id: item?.id ?? proximoId,
      produtos: widget.produtos,
      item: item,
    );
    if (!mounted || salvo == null) return;
    setState(() {
      if (item == null) {
        encomenda.adicionarItem(salvo);
      } else {
        encomenda.atualizarItem(salvo);
      }
    });
  }

  Future<void> _excluirItem(ItemEncomenda item) async {
    final encomenda = widget.encomenda;
    if (encomenda.itens.length == 1) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          content: Text('A encomenda precisa manter pelo menos um item.'),
        ),
      );
      return;
    }
    if (await confirmarExclusao(
          context,
          'Remover ${item.produto.nome} da encomenda?',
        ) &&
        mounted) {
      setState(() => encomenda.removerItem(item.id));
    }
  }

  @override
  Widget build(BuildContext context) {
    final encomenda = widget.encomenda;
    final itens = encomenda.itens;
    final tema = Theme.of(context);

    return Scaffold(
      appBar: AppBar(title: Text('Encomenda #${encomenda.id}')),
      body: Center(
        child: ConstrainedBox(
          constraints: const BoxConstraints(maxWidth: 840),
          child: ListView.builder(
            padding: const EdgeInsets.all(16),
            itemCount: itens.length + 1,
            itemBuilder: (context, indice) {
              if (indice == 0) {
                return Padding(
                  padding: const EdgeInsets.only(bottom: 16),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        encomenda.cliente.nome,
                        style: tema.textTheme.headlineSmall,
                      ),
                      Text('Status: ${encomenda.status}'),
                      Text('Pedido: ${formatarData(encomenda.dataPedido)}'),
                      Text('Entrega: ${formatarData(encomenda.dataEntrega)}'),
                      if (encomenda.observacao != null)
                        Text('Observação: ${encomenda.observacao}'),
                      const SizedBox(height: 16),
                      Wrap(
                        spacing: 16,
                        runSpacing: 12,
                        crossAxisAlignment: WrapCrossAlignment.center,
                        children: [
                          Text(
                            'Total: ${formatarPreco(encomenda.calcularValorTotal())}',
                            style: tema.textTheme.titleLarge,
                          ),
                          FilledButton.icon(
                            onPressed: _salvarItem,
                            icon: const Icon(Icons.add),
                            label: const Text('Adicionar item'),
                          ),
                        ],
                      ),
                    ],
                  ),
                );
              }
              final item = itens[indice - 1];
              final unidades = item.calcularUnidades();
              return Card(
                child: ListTile(
                  title: Text(item.produto.nome),
                  subtitle: Text(
                    'Quantidade: ${item.quantidade}'
                    '${unidades == null ? '' : ' porções ($unidades unidades)'}\n'
                    'Unitário: ${formatarPreco(item.precoUnitario)}  •  '
                    'Adicional: ${formatarPreco(item.valoresAdicionais ?? 0)}\n'
                    'Subtotal: ${formatarPreco(item.calcularValorItem())}',
                  ),
                  onTap: () => _salvarItem(item),
                  trailing: IconButton(
                    tooltip: 'Remover item',
                    onPressed: () => _excluirItem(item),
                    icon: const Icon(Icons.delete_outline),
                  ),
                ),
              );
            },
          ),
        ),
      ),
    );
  }
}
