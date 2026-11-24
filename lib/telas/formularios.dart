import 'package:flutter/material.dart';

import '../models/bolo.dart';
import '../models/bolo_a_combinar.dart';
import '../models/bolo_casamento.dart';
import '../models/bolo_personalizado.dart';
import '../models/bolo_tematico.dart';
import '../models/cliente.dart';
import '../models/doce.dart';
import '../models/encomenda.dart';
import '../models/item_encomenda.dart';
import '../models/produto_confeitaria.dart';
import '../models/salgado.dart';

const categorias = [
  'Bolo',
  'Bolo a Combinar',
  'Bolo de Casamento',
  'Bolo Personalizado',
  'Bolo Temático',
  'Doce',
  'Salgado',
];

String formatarPreco(double valor) =>
    'R\$ ${valor.toStringAsFixed(2).replaceAll('.', ',')}';

String formatarData(DateTime data) {
  String dois(int numero) => numero.toString().padLeft(2, '0');
  return '${dois(data.day)}/${dois(data.month)}/${data.year.toString().padLeft(4, '0')} '
      '${dois(data.hour)}:${dois(data.minute)}';
}

DateTime? _lerData(String texto) {
  final partes = RegExp(
    r'^(\d{2})/(\d{2})/(\d{4}) (\d{2}):(\d{2})$',
  ).firstMatch(texto);
  if (partes == null) return null;
  final data = DateTime(
    int.parse(partes[3]!),
    int.parse(partes[2]!),
    int.parse(partes[1]!),
    int.parse(partes[4]!),
    int.parse(partes[5]!),
  );
  return formatarData(data) == texto ? data : null;
}

double? _lerValor(String texto) => double.tryParse(texto.replaceAll(',', '.'));

String? _validarValor(String texto) {
  final valor = _lerValor(texto);
  if (valor == null || !valor.isFinite || valor < 0) {
    return 'Digite um valor igual ou maior que zero.';
  }
  return null;
}

String? _validarQuantidade(String texto) {
  final numero = int.tryParse(texto);
  if (numero == null || numero <= 0) {
    return 'Digite um número inteiro maior que zero.';
  }
  return null;
}

Future<bool> confirmarExclusao(BuildContext context, String mensagem) async {
  final confirmou = await showDialog<bool>(
    context: context,
    builder: (context) => AlertDialog(
      title: const Text('Confirmar exclusão'),
      content: Text(mensagem),
      actions: [
        TextButton(
          onPressed: () => Navigator.pop(context, false),
          child: const Text('Cancelar'),
        ),
        FilledButton(
          onPressed: () => Navigator.pop(context, true),
          child: const Text('Excluir'),
        ),
      ],
    ),
  );
  return confirmou ?? false;
}

Widget _campo(
  String nome,
  Map<String, String> dados, {
  String? inicial,
  bool obrigatorio = false,
  String? dica,
  TextInputType? teclado,
  String? Function(String)? validar,
}) {
  return Padding(
    padding: const EdgeInsets.only(top: 12),
    child: TextFormField(
      key: ValueKey(nome),
      initialValue: inicial,
      keyboardType: teclado,
      decoration: InputDecoration(labelText: nome, hintText: dica),
      validator: (valor) {
        final texto = valor?.trim() ?? '';
        if (texto.isEmpty) {
          return obrigatorio ? 'Preencha este campo.' : null;
        }
        return validar?.call(texto);
      },
      onSaved: (valor) {
        final texto = valor?.trim() ?? '';
        if (texto.isNotEmpty) dados[nome] = texto;
      },
    ),
  );
}

Widget _dialogo<T>({
  required BuildContext context,
  required String titulo,
  required GlobalKey<FormState> formulario,
  required List<Widget> campos,
  required T Function() salvar,
}) {
  return AlertDialog(
    title: Text(titulo),
    scrollable: true,
    content: SizedBox(
      width: 420,
      child: Form(
        key: formulario,
        child: Column(
          mainAxisSize: MainAxisSize.min,
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: campos,
        ),
      ),
    ),
    actions: [
      TextButton(
        onPressed: () => Navigator.pop(context),
        child: const Text('Cancelar'),
      ),
      FilledButton(
        onPressed: () {
          final estado = formulario.currentState!;
          if (estado.validate()) {
            estado.save();
            Navigator.pop(context, salvar());
          }
        },
        child: const Text('Salvar'),
      ),
    ],
  );
}

Future<Cliente?> editarCliente(
  BuildContext context, {
  required int id,
  Cliente? cliente,
}) {
  final formulario = GlobalKey<FormState>();
  final dados = <String, String>{};
  return showDialog<Cliente>(
    context: context,
    builder: (context) => _dialogo(
      context: context,
      titulo: cliente == null ? 'Novo cliente' : 'Editar cliente',
      formulario: formulario,
      campos: [
        _campo('Nome', dados, inicial: cliente?.nome, obrigatorio: true),
        if (cliente == null) _campo('CPF', dados),
        _campo('Telefone', dados, inicial: cliente?.telefone),
        _campo('E-mail', dados, inicial: cliente?.email),
      ],
      salvar: () {
        final cadastro =
            cliente ?? Cliente(id: id, nome: dados['Nome']!, cpf: dados['CPF']);
        cadastro.atualizarCadastro(
          nome: dados['Nome']!,
          telefone: dados['Telefone'],
          email: dados['E-mail'],
        );
        return cadastro;
      },
    ),
  );
}

Future<ProdutoConfeitaria?> editarProduto(
  BuildContext context, {
  required int id,
  ProdutoConfeitaria? produto,
}) {
  final formulario = GlobalKey<FormState>();
  final dados = <String, String>{};
  var categoria = categorias.first;
  var bonecos = false;
  var caixa = false;
  return showDialog<ProdutoConfeitaria>(
    context: context,
    builder: (context) => StatefulBuilder(
      builder: (context, atualizar) {
        final casamento = categoria == 'Bolo de Casamento';
        final personalizado = categoria == 'Bolo Personalizado';
        final tematico = categoria == 'Bolo Temático';
        final boloComMassa =
            categoria == 'Bolo' || casamento || personalizado || tematico;
        return _dialogo(
          context: context,
          titulo: produto == null ? 'Novo produto' : 'Editar produto',
          formulario: formulario,
          campos: [
            if (produto == null)
              DropdownButtonFormField<String>(
                initialValue: categoria,
                isExpanded: true,
                decoration: const InputDecoration(labelText: 'Categoria'),
                items: [
                  for (final tipo in categorias)
                    DropdownMenuItem(value: tipo, child: Text(tipo)),
                ],
                onChanged: (valor) => atualizar(() => categoria = valor!),
              ),
            _campo('Nome', dados, inicial: produto?.nome, obrigatorio: true),
            _campo(
              'Preço base (R\$)',
              dados,
              inicial: produto?.precoBase.toString().replaceAll('.', ','),
              obrigatorio: true,
              teclado: const TextInputType.numberWithOptions(decimal: true),
              validar: _validarValor,
            ),
            if (produto == null) ...[
              if (boloComMassa) ...[
                _campo('Tamanho', dados),
                _campo('Massa', dados),
                _campo('Recheio', dados),
                _campo('Cobertura', dados),
              ],
              if (casamento || personalizado || tematico)
                _campo(
                  'Camadas',
                  dados,
                  inicial: '1',
                  obrigatorio: true,
                  teclado: TextInputType.number,
                  validar: _validarQuantidade,
                ),
              if (casamento) ...[
                CheckboxListTile(
                  contentPadding: EdgeInsets.zero,
                  title: const Text('Bonecos no topo'),
                  value: bonecos,
                  onChanged: (valor) => atualizar(() => bonecos = valor!),
                ),
                _campo('Decoração', dados),
              ],
              if (personalizado)
                CheckboxListTile(
                  contentPadding: EdgeInsets.zero,
                  title: const Text('Caixa enfeitada'),
                  value: caixa,
                  onChanged: (valor) => atualizar(() => caixa = valor!),
                ),
              if (casamento || personalizado) ...[
                _campo('Frutas', dados),
                _campo('Biscoito', dados),
              ],
              if (tematico) ...[
                _campo('Tema', dados),
                _campo('Referência', dados),
                _campo('Objetos decorativos', dados),
              ],
              if (categoria == 'Bolo a Combinar') _campo('Descrição', dados),
              if (categoria == 'Doce' || categoria == 'Salgado') ...[
                _campo('Tipo', dados),
                _campo(
                  'Unidades por porção',
                  dados,
                  inicial: '1',
                  obrigatorio: true,
                  teclado: TextInputType.number,
                  validar: _validarQuantidade,
                ),
              ],
            ],
          ],
          salvar: () {
            final nome = dados['Nome']!;
            final preco = _lerValor(dados['Preço base (R\$)']!)!;
            if (produto != null) {
              produto.nome = nome;
              produto.precoBase = preco;
              return produto;
            }
            final tamanho = dados['Tamanho'] ?? '';
            final sabor = dados['Massa'] ?? '';
            final recheio = dados['Recheio'];
            final cobertura = dados['Cobertura'];
            final camadas = int.tryParse(dados['Camadas'] ?? '') ?? 1;
            final unidades =
                int.tryParse(dados['Unidades por porção'] ?? '') ?? 1;
            switch (categoria) {
              case 'Bolo a Combinar':
                return BoloACombinar(
                  id: id,
                  nome: nome,
                  precoBase: preco,
                  descricao: dados['Descrição'],
                );
              case 'Bolo de Casamento':
                return BoloCasamento(
                  id: id,
                  nome: nome,
                  precoBase: preco,
                  tamanho: tamanho,
                  sabor: sabor,
                  recheio: recheio,
                  cobertura: cobertura,
                  camadas: camadas,
                  bonecosDecorativos: bonecos,
                  decoracao: dados['Decoração'],
                  frutas: dados['Frutas'],
                  biscoito: dados['Biscoito'],
                );
              case 'Bolo Personalizado':
                return BoloPersonalizado(
                  id: id,
                  nome: nome,
                  precoBase: preco,
                  tamanho: tamanho,
                  sabor: sabor,
                  recheio: recheio,
                  cobertura: cobertura,
                  camadas: camadas,
                  caixaEnfeitada: caixa,
                  frutas: dados['Frutas'],
                  biscoito: dados['Biscoito'],
                );
              case 'Bolo Temático':
                return BoloTematico(
                  id: id,
                  nome: nome,
                  precoBase: preco,
                  tamanho: tamanho,
                  sabor: sabor,
                  recheio: recheio,
                  cobertura: cobertura,
                  camadas: camadas,
                  tema: dados['Tema'] ?? '',
                  referencia: dados['Referência'],
                  objetosTematicosDecorativos: dados['Objetos decorativos'],
                );
              case 'Doce':
                return Doce(
                  id: id,
                  nome: nome,
                  precoBase: preco,
                  tipo: dados['Tipo'],
                  unidadesPorPorcao: unidades,
                );
              case 'Salgado':
                return Salgado(
                  id: id,
                  nome: nome,
                  precoBase: preco,
                  tipo: dados['Tipo'],
                  unidadesPorPorcao: unidades,
                );
              default:
                return Bolo(
                  id: id,
                  nome: nome,
                  precoBase: preco,
                  tamanho: tamanho,
                  sabor: sabor,
                  recheio: recheio,
                  cobertura: cobertura,
                );
            }
          },
        );
      },
    ),
  );
}

List<Widget> _camposItem(
  List<ProdutoConfeitaria> produtos,
  Map<String, String> dados, {
  required ProdutoConfeitaria produto,
  required void Function(ProdutoConfeitaria) selecionar,
  ItemEncomenda? item,
}) {
  return [
    DropdownButtonFormField<ProdutoConfeitaria>(
      initialValue: produto,
      isExpanded: true,
      decoration: const InputDecoration(labelText: 'Produto'),
      items: [
        for (final opcao in produtos)
          DropdownMenuItem(
            value: opcao,
            child: Text(
              '${opcao.nome} • ${formatarPreco(opcao.precoBase)}',
              overflow: TextOverflow.ellipsis,
            ),
          ),
      ],
      onChanged: (valor) => selecionar(valor!),
    ),
    _campo(
      'Quantidade',
      dados,
      inicial: '${item?.quantidade ?? 1}',
      obrigatorio: true,
      teclado: TextInputType.number,
      validar: _validarQuantidade,
    ),
    _campo(
      'Adicional total (R\$)',
      dados,
      inicial: item?.valoresAdicionais?.toString(),
      teclado: const TextInputType.numberWithOptions(decimal: true),
      validar: _validarValor,
    ),
  ];
}

Future<ItemEncomenda?> editarItem(
  BuildContext context, {
  required int id,
  required List<ProdutoConfeitaria> produtos,
  ItemEncomenda? item,
}) {
  final formulario = GlobalKey<FormState>();
  final dados = <String, String>{};
  var produto = item?.produto ?? produtos.first;
  return showDialog<ItemEncomenda>(
    context: context,
    builder: (context) => _dialogo(
      context: context,
      titulo: item == null ? 'Adicionar item' : 'Editar item',
      formulario: formulario,
      campos: [
        if (item != null)
          const Padding(
            padding: EdgeInsets.only(bottom: 16),
            child: Text('Ao salvar, o item usa o preço atual do cardápio.'),
          ),
        ..._camposItem(
          produtos,
          dados,
          produto: produto,
          selecionar: (valor) => produto = valor,
          item: item,
        ),
      ],
      salvar: () => ItemEncomenda(
        id: id,
        quantidade: int.parse(dados['Quantidade']!),
        produto: produto,
        valoresAdicionais: _lerValor(dados['Adicional total (R\$)'] ?? ''),
      ),
    ),
  );
}

Future<Encomenda?> editarEncomenda(
  BuildContext context, {
  required int id,
  required List<Cliente> clientes,
  required List<ProdutoConfeitaria> produtos,
  Encomenda? encomenda,
}) {
  final formulario = GlobalKey<FormState>();
  final dados = <String, String>{};
  final agora = DateTime.now();
  final dataPedido =
      encomenda?.dataPedido ??
      DateTime(agora.year, agora.month, agora.day, agora.hour, agora.minute);
  var cliente = encomenda?.cliente ?? clientes.first;
  var produto = produtos.first;

  return showDialog<Encomenda>(
    context: context,
    builder: (context) => _dialogo(
      context: context,
      titulo: encomenda == null ? 'Nova encomenda' : 'Editar encomenda',
      formulario: formulario,
      campos: [
        if (encomenda == null)
          DropdownButtonFormField<Cliente>(
            initialValue: cliente,
            isExpanded: true,
            decoration: const InputDecoration(labelText: 'Cliente'),
            items: [
              for (final opcao in clientes)
                DropdownMenuItem(value: opcao, child: Text(opcao.nome)),
            ],
            onChanged: (valor) => cliente = valor!,
          ),
        _campo(
          'Entrega',
          dados,
          inicial: formatarData(
            encomenda?.dataEntrega ?? dataPedido.add(const Duration(days: 1)),
          ),
          obrigatorio: true,
          dica: 'dd/MM/aaaa HH:mm',
          validar: (texto) {
            final data = _lerData(texto);
            if (data == null) return 'Use uma data válida em dd/MM/aaaa HH:mm.';
            if (data.isBefore(dataPedido)) {
              return 'A entrega não pode ser anterior ao pedido.';
            }
            return null;
          },
        ),
        if (encomenda != null)
          _campo('Status', dados, inicial: encomenda.status, obrigatorio: true),
        _campo('Observação', dados, inicial: encomenda?.observacao),
        if (encomenda == null) ...[
          const Padding(
            padding: EdgeInsets.only(top: 20, bottom: 8),
            child: Text(
              'Primeiro item',
              style: TextStyle(fontWeight: FontWeight.w600),
            ),
          ),
          ..._camposItem(
            produtos,
            dados,
            produto: produto,
            selecionar: (valor) => produto = valor,
          ),
        ],
      ],
      salvar: () {
        final entrega = _lerData(dados['Entrega']!)!;
        if (encomenda != null) {
          encomenda.reagendar(entrega);
          encomenda.status = dados['Status']!;
          encomenda.observacao = dados['Observação'];
          return encomenda;
        }
        final novo = Encomenda(
          id: id,
          cliente: cliente,
          dataPedido: dataPedido,
          dataEntrega: entrega,
          status: 'Recebida',
          observacao: dados['Observação'],
        );
        novo.adicionarItem(
          ItemEncomenda(
            id: 1,
            quantidade: int.parse(dados['Quantidade']!),
            produto: produto,
            valoresAdicionais: _lerValor(dados['Adicional total (R\$)'] ?? ''),
          ),
        );
        return novo;
      },
    ),
  );
}
