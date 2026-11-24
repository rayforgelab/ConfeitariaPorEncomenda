import 'produto_confeitaria.dart';

class Bolo extends ProdutoConfeitaria {
  Bolo({
    required super.id,
    required super.nome,
    required super.precoBase,
    required this.tamanho,
    required this.sabor,
    this.recheio,
    String? cobertura,
  }) : _cobertura = cobertura;

  final String tamanho;
  final String sabor;
  final String? recheio;
  final String? _cobertura;

  String? get cobertura => _cobertura;

  @override
  String get categoria => 'Bolo';

  static int validarCamadas(int camadas) {
    if (camadas < 1) {
      throw ArgumentError('Informe pelo menos uma camada.');
    }
    return camadas;
  }

  @override
  Map<String, String?> detalhes() => {
    'Tamanho': tamanho,
    'Massa': sabor,
    'Recheio': recheio,
    'Cobertura': cobertura,
  };
}
