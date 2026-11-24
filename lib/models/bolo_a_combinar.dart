import 'produto_confeitaria.dart';

class BoloACombinar extends ProdutoConfeitaria {
  BoloACombinar({
    required super.id,
    required super.nome,
    required super.precoBase,
    super.descricao,
  });

  @override
  String get categoria => 'Bolo a Combinar';
}
