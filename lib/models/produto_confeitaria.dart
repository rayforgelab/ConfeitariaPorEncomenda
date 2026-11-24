abstract class ProdutoConfeitaria {
  ProdutoConfeitaria({
    required this.id,
    required this.nome,
    required double precoBase,
    this.descricao,
  }) : _precoBase = validarPreco(precoBase);

  final int id;
  String nome;
  double _precoBase;
  final String? descricao;

  double get precoBase => _precoBase;

  set precoBase(double valor) {
    _precoBase = validarPreco(valor);
  }

  String get categoria;

  static double validarPreco(double valor) {
    if (!valor.isFinite || valor < 0) {
      throw ArgumentError('O preço deve ser finito e não negativo.');
    }
    return valor;
  }

  Map<String, String?> detalhes() => {'Descrição': descricao};
}
