import 'doce.dart';
import 'produto_confeitaria.dart';
import 'salgado.dart';

class ItemEncomenda {
  ItemEncomenda({
    required this.id,
    required this.quantidade,
    required this.produto,
    this.valoresAdicionais,
  }) : _precoUnitario = produto.precoBase {
    if (quantidade <= 0) {
      throw ArgumentError('Informe uma quantidade maior que zero.');
    }
    ProdutoConfeitaria.validarPreco(valoresAdicionais ?? 0);
  }

  final int id;
  final int quantidade;
  final ProdutoConfeitaria produto;
  final double? valoresAdicionais;
  final double _precoUnitario;

  double get precoUnitario => _precoUnitario;

  double calcularValorItem() {
    return quantidade * precoUnitario + (valoresAdicionais ?? 0);
  }

  int? calcularUnidades() {
    final produto = this.produto;
    if (produto is Doce) return produto.calcularUnidades(quantidade);
    if (produto is Salgado) return produto.calcularUnidades(quantidade);
    return null;
  }
}
