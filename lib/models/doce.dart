import 'produto_confeitaria.dart';

class Doce extends ProdutoConfeitaria {
  Doce({
    required super.id,
    required super.nome,
    required super.precoBase,
    required int unidadesPorPorcao,
    this.tipo,
  }) : _unidadesPorPorcao = unidadesPorPorcao {
    if (unidadesPorPorcao < 1) {
      throw ArgumentError('Informe pelo menos uma unidade por porção.');
    }
  }

  final int _unidadesPorPorcao;
  final String? tipo;

  int get unidadesPorPorcao => _unidadesPorPorcao;

  @override
  String get categoria => 'Doce';

  int calcularUnidades(int porcoes) => porcoes * unidadesPorPorcao;

  @override
  Map<String, String?> detalhes() => {
    'Tipo': tipo,
    'Unidades por porção': '$unidadesPorPorcao',
  };
}
