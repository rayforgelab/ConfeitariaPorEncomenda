class Cliente {
  Cliente({
    required int id,
    required this.nome,
    this.cpf,
    this.telefone,
    this.email,
  }) : _id = id;

  final int _id;
  String nome;
  final String? cpf;
  String? telefone;
  String? email;

  int get id => _id;

  void atualizarCadastro({
    required String nome,
    String? telefone,
    String? email,
  }) {
    if (nome.trim().isEmpty) {
      throw ArgumentError('Informe o nome do cliente.');
    }
    this.nome = nome.trim();
    this.telefone = telefone;
    this.email = email;
  }
}
