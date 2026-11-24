import 'bolo.dart';

class BoloTematico extends Bolo {
  BoloTematico({
    required super.id,
    required super.nome,
    required super.precoBase,
    required super.tamanho,
    required super.sabor,
    super.recheio,
    super.cobertura,
    required int camadas,
    required this.tema,
    this.referencia,
    this.objetosTematicosDecorativos,
  }) : _camadas = Bolo.validarCamadas(camadas);

  final int _camadas;
  final String tema;
  final String? referencia;
  final String? objetosTematicosDecorativos;

  int get camadas => _camadas;

  @override
  String get categoria => 'Bolo Temático';

  bool correspondeAoTema(String filtro) {
    return filtro.isEmpty || tema.toLowerCase() == filtro.toLowerCase();
  }

  @override
  Map<String, String?> detalhes() => {
    ...super.detalhes(),
    'Camadas': '$camadas',
    'Tema': tema,
    'Referência': referencia,
    'Objetos decorativos': objetosTematicosDecorativos,
  };
}
