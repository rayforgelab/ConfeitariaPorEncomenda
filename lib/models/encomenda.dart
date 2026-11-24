import 'cliente.dart';
import 'item_encomenda.dart';

class Encomenda {
  Encomenda({
    required this.id,
    required this.cliente,
    required this.dataPedido,
    required DateTime dataEntrega,
    required this.status,
    this.observacao,
  }) : _dataEntrega = dataEntrega {
    reagendar(dataEntrega);
  }

  final int id;
  Cliente cliente;
  final DateTime dataPedido;
  DateTime _dataEntrega;
  String status;
  String? observacao;
  final List<ItemEncomenda> _itens = [];

  DateTime get dataEntrega => _dataEntrega;
  List<ItemEncomenda> get itens => List.unmodifiable(_itens);

  void reagendar(DateTime dataEntrega) {
    if (dataEntrega.isBefore(dataPedido)) {
      throw ArgumentError('A entrega não pode ser anterior ao pedido.');
    }
    _dataEntrega = dataEntrega;
  }

  void adicionarItem(ItemEncomenda item) {
    if (_itens.any((existente) => existente.id == item.id)) {
      throw ArgumentError('Já existe um item com esse ID na encomenda.');
    }
    _itens.add(item);
  }

  bool atualizarItem(ItemEncomenda item) {
    final indice = _itens.indexWhere((existente) => existente.id == item.id);
    if (indice == -1) return false;
    _itens[indice] = item;
    return true;
  }

  bool removerItem(int id) {
    final indice = _itens.indexWhere((item) => item.id == id);
    if (indice == -1) return false;
    if (_itens.length == 1) {
      throw StateError('A encomenda precisa manter pelo menos um item.');
    }
    _itens.removeAt(indice);
    return true;
  }

  double calcularValorTotal() {
    var total = 0.0;
    for (final item in _itens) {
      total += item.calcularValorItem();
    }
    return total;
  }
}
