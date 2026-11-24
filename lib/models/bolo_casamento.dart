import 'bolo.dart';

class BoloCasamento extends Bolo {
  BoloCasamento({
    required super.id,
    required super.nome,
    required super.precoBase,
    required super.tamanho,
    required super.sabor,
    super.recheio,
    super.cobertura,
    required int camadas,
    required this.bonecosDecorativos,
    this.decoracao,
    this.frutas,
    this.biscoito,
  }) : _camadas = Bolo.validarCamadas(camadas);

  final int _camadas;
  final bool bonecosDecorativos;
  final String? decoracao;
  final String? frutas;
  final String? biscoito;

  int get camadas => _camadas;

  @override
  String get categoria => 'Bolo de Casamento';

  @override
  Map<String, String?> detalhes() => {
    ...super.detalhes(),
    'Camadas': '$camadas',
    'Bonecos no topo': bonecosDecorativos ? 'Sim' : 'Não',
    'Decoração': decoracao,
    'Frutas': frutas,
    'Biscoito': biscoito,
  };
}
