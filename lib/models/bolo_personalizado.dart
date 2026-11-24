import 'bolo.dart';

class BoloPersonalizado extends Bolo {
  BoloPersonalizado({
    required super.id,
    required super.nome,
    required super.precoBase,
    required super.tamanho,
    required super.sabor,
    super.recheio,
    super.cobertura,
    required int camadas,
    required this.caixaEnfeitada,
    this.frutas,
    this.biscoito,
  }) : _camadas = Bolo.validarCamadas(camadas);

  final int _camadas;
  final bool caixaEnfeitada;
  final String? frutas;
  final String? biscoito;

  int get camadas => _camadas;

  @override
  String get categoria => 'Bolo Personalizado';

  @override
  Map<String, String?> detalhes() => {
    ...super.detalhes(),
    'Camadas': '$camadas',
    'Caixa enfeitada': caixaEnfeitada ? 'Sim' : 'Não',
    'Frutas': frutas,
    'Biscoito': biscoito,
  };
}
