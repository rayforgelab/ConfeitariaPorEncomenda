import 'package:flutter/material.dart';

import 'models/bolo.dart';
import 'models/bolo_a_combinar.dart';
import 'models/bolo_casamento.dart';
import 'models/bolo_personalizado.dart';
import 'models/bolo_tematico.dart';
import 'models/cliente.dart';
import 'models/doce.dart';
import 'models/encomenda.dart';
import 'models/item_encomenda.dart';
import 'models/produto_confeitaria.dart';
import 'models/salgado.dart';
import 'telas/detalhes_encomenda.dart';
import 'telas/formularios.dart';

const nomeSistema = 'Confeitaria por Encomenda';

void main() {
  runApp(const ConfeitariaApp());
}

class ConfeitariaApp extends StatelessWidget {
  const ConfeitariaApp({super.key});

  @override
  Widget build(BuildContext context) {
    const fundo = Color(0xFFFFF8F4);
    final esquema = ColorScheme.fromSeed(
      seedColor: const Color(0xFF8B3E56),
      surface: fundo,
    );

    return MaterialApp(
      title: nomeSistema,
      debugShowCheckedModeBanner: false,
      home: const TelaInicial(),
      theme: ThemeData(
        colorScheme: esquema,
        scaffoldBackgroundColor: fundo,
        appBarTheme: AppBarTheme(
          backgroundColor: fundo,
          foregroundColor: esquema.onSurface,
        ),
        cardTheme: CardThemeData(
          color: Colors.white,
          elevation: 0,
          margin: const EdgeInsets.only(bottom: 10),
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(16),
            side: BorderSide(color: esquema.outlineVariant),
          ),
        ),
        inputDecorationTheme: const InputDecorationTheme(
          border: OutlineInputBorder(),
        ),
      ),
    );
  }
}

class TelaInicial extends StatefulWidget {
  const TelaInicial({super.key});

  @override
  State<TelaInicial> createState() => _TelaInicialState();
}

class _TelaInicialState extends State<TelaInicial> {
  final _produtos = <ProdutoConfeitaria>[
    Bolo(
      id: 1,
      nome: 'Bolo de chocolate',
      precoBase: 65,
      tamanho: 'Médio',
      sabor: 'Chocolate',
      recheio: 'Brigadeiro',
      cobertura: 'Ganache',
    ),
    BoloACombinar(
      id: 2,
      nome: 'Bolo sob medida',
      precoBase: 90,
      descricao: 'Massa, recheio e acabamento escolhidos com o cliente.',
    ),
    BoloCasamento(
      id: 3,
      nome: 'Bolo de baunilha com flores',
      precoBase: 280,
      tamanho: 'Grande',
      sabor: 'Baunilha',
      recheio: 'Creme de baunilha',
      cobertura: 'Buttercream',
      camadas: 3,
      bonecosDecorativos: false,
      decoracao: 'Flores de açúcar',
    ),
    BoloPersonalizado(
      id: 4,
      nome: 'Bolo de morango',
      precoBase: 120,
      tamanho: 'Médio',
      sabor: 'Baunilha',
      recheio: 'Creme com morango',
      camadas: 2,
      caixaEnfeitada: true,
      frutas: 'Morangos',
    ),
    BoloTematico(
      id: 5,
      nome: 'Bolo céu estrelado',
      precoBase: 160,
      tamanho: 'Médio',
      sabor: 'Chocolate',
      cobertura: 'Chantilly',
      camadas: 2,
      tema: 'Espaço',
      objetosTematicosDecorativos: 'Lua e estrelas de açúcar',
    ),
    Doce(
      id: 6,
      nome: 'Brigadeiros',
      precoBase: 28,
      tipo: 'Brigadeiro tradicional',
      unidadesPorPorcao: 12,
    ),
    Salgado(
      id: 7,
      nome: 'Mini coxinhas',
      precoBase: 32,
      tipo: 'Frango',
      unidadesPorPorcao: 12,
    ),
  ];

  final _clientes = <Cliente>[
    Cliente(id: 1, nome: 'Marina Lopes', email: 'marina@example.com'),
    Cliente(id: 2, nome: 'Rafael Dias'),
  ];
  final _encomendas = <Encomenda>[];

  int _proximoProduto = 8;
  int _proximoCliente = 3;
  int _proximaEncomenda = 2;
  String _categoria = 'Todas as categorias';
  String _tema = '';

  @override
  void initState() {
    super.initState();
    final agora = DateTime.now();
    final pedido = DateTime(
      agora.year,
      agora.month,
      agora.day,
      agora.hour,
      agora.minute,
    );
    final exemplo = Encomenda(
      id: 1,
      cliente: _clientes.first,
      dataPedido: pedido,
      dataEntrega: pedido.add(const Duration(days: 3)),
      status: 'Recebida',
      observacao: 'Entregar o bolo com os brigadeiros.',
    );
    exemplo.adicionarItem(
      ItemEncomenda(id: 1, quantidade: 1, produto: _produtos[0]),
    );
    exemplo.adicionarItem(
      ItemEncomenda(id: 2, quantidade: 2, produto: _produtos[5]),
    );
    _encomendas.add(exemplo);
  }

  void _avisar(String mensagem) {
    ScaffoldMessenger.of(
      context,
    ).showSnackBar(SnackBar(content: Text(mensagem)));
  }

  Future<void> _salvarProduto([ProdutoConfeitaria? produto]) async {
    final salvo = await editarProduto(
      context,
      id: _proximoProduto,
      produto: produto,
    );
    if (!mounted || salvo == null) return;
    setState(() {
      if (produto == null) {
        _produtos.add(salvo);
        _proximoProduto++;
        _categoria = 'Todas as categorias';
        _tema = '';
      }
    });
  }

  Future<void> _salvarCliente([Cliente? cliente]) async {
    final salvo = await editarCliente(
      context,
      id: _proximoCliente,
      cliente: cliente,
    );
    if (!mounted || salvo == null) return;
    setState(() {
      if (cliente == null) {
        _clientes.add(salvo);
        _proximoCliente++;
      }
    });
  }

  Future<void> _salvarEncomenda([Encomenda? encomenda]) async {
    if (_clientes.isEmpty || _produtos.isEmpty) {
      _avisar('Cadastre pelo menos um cliente e um produto antes.');
      return;
    }
    final salva = await editarEncomenda(
      context,
      id: _proximaEncomenda,
      clientes: _clientes,
      produtos: _produtos,
      encomenda: encomenda,
    );
    if (!mounted || salva == null) return;
    setState(() {
      if (encomenda == null) {
        _encomendas.add(salva);
        _proximaEncomenda++;
      }
    });
  }

  Future<void> _excluir(String mensagem, VoidCallback remover) async {
    if (await confirmarExclusao(context, mensagem) && mounted) {
      setState(remover);
    }
  }

  void _excluirProduto(ProdutoConfeitaria produto) {
    final emUso = _encomendas.any(
      (encomenda) =>
          encomenda.itens.any((item) => item.produto.id == produto.id),
    );
    if (emUso) {
      _avisar('Este produto está em uma encomenda.');
      return;
    }
    _excluir('Excluir ${produto.nome}?', () => _produtos.remove(produto));
  }

  void _excluirCliente(Cliente cliente) {
    if (_encomendas.any((encomenda) => encomenda.cliente.id == cliente.id)) {
      _avisar('Este cliente tem encomendas cadastradas.');
      return;
    }
    _excluir('Excluir ${cliente.nome}?', () => _clientes.remove(cliente));
  }

  void _verProduto(ProdutoConfeitaria produto) {
    showDialog<void>(
      context: context,
      builder: (context) => AlertDialog(
        scrollable: true,
        title: Text(produto.nome),
        content: Column(
          mainAxisSize: MainAxisSize.min,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(produto.categoria),
            Text(formatarPreco(produto.precoBase)),
            for (final campo in produto.detalhes().entries)
              if (campo.value?.isNotEmpty ?? false)
                Text('${campo.key}: ${campo.value}'),
          ],
        ),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(context),
            child: const Text('Fechar'),
          ),
        ],
      ),
    );
  }

  Future<void> _verEncomenda(Encomenda encomenda) async {
    await Navigator.push<void>(
      context,
      MaterialPageRoute(
        builder: (context) =>
            DetalhesEncomenda(encomenda: encomenda, produtos: _produtos),
      ),
    );
    if (mounted) setState(() {});
  }

  Widget _acoes(VoidCallback editar, VoidCallback excluir) {
    return PopupMenuButton<String>(
      tooltip: 'Ações',
      onSelected: (acao) {
        if (acao == 'editar') {
          editar();
        } else {
          excluir();
        }
      },
      itemBuilder: (context) => const [
        PopupMenuItem(value: 'editar', child: Text('Editar')),
        PopupMenuItem(value: 'excluir', child: Text('Excluir')),
      ],
    );
  }

  Widget _lista<T>({
    required String titulo,
    required String botao,
    required VoidCallback adicionar,
    required List<T> dados,
    required String vazio,
    required ListTile Function(T) exibir,
    List<Widget> filtros = const [],
  }) {
    return Center(
      child: ConstrainedBox(
        constraints: const BoxConstraints(maxWidth: 840),
        child: Padding(
          padding: const EdgeInsets.fromLTRB(16, 24, 16, 0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              Wrap(
                alignment: WrapAlignment.spaceBetween,
                crossAxisAlignment: WrapCrossAlignment.center,
                spacing: 16,
                runSpacing: 12,
                children: [
                  Text(
                    titulo,
                    style: Theme.of(context).textTheme.headlineSmall,
                  ),
                  FilledButton.icon(
                    onPressed: adicionar,
                    icon: const Icon(Icons.add),
                    label: Text(botao),
                  ),
                ],
              ),
              const SizedBox(height: 16),
              ...filtros,
              Expanded(
                child: dados.isEmpty
                    ? Center(child: Text(vazio))
                    : ListView.builder(
                        itemCount: dados.length,
                        itemBuilder: (context, indice) => Card(
                          clipBehavior: Clip.antiAlias,
                          child: exibir(dados[indice]),
                        ),
                      ),
              ),
            ],
          ),
        ),
      ),
    );
  }

  Widget _catalogo() {
    final exibidos = _produtos.where((produto) {
      if (_categoria == 'Todas as categorias') return true;
      if (produto.categoria != _categoria) return false;
      if (produto is BoloTematico) return produto.correspondeAoTema(_tema);
      return true;
    }).toList();

    return _lista(
      titulo: 'Cardápio',
      botao: 'Novo produto',
      adicionar: _salvarProduto,
      dados: exibidos,
      vazio: 'Nenhum produto encontrado.',
      filtros: [
        Padding(
          padding: const EdgeInsets.only(bottom: 12),
          child: DropdownButton<String>(
            value: _categoria,
            isExpanded: true,
            items: [
              for (final tipo in ['Todas as categorias', ...categorias])
                DropdownMenuItem(value: tipo, child: Text(tipo)),
            ],
            onChanged: (valor) => setState(() {
              _categoria = valor!;
              _tema = '';
            }),
          ),
        ),
        if (_categoria == 'Bolo Temático')
          Padding(
            padding: const EdgeInsets.only(bottom: 12),
            child: TextFormField(
              initialValue: _tema,
              decoration: const InputDecoration(labelText: 'Filtrar por tema'),
              onChanged: (valor) => setState(() => _tema = valor.trim()),
            ),
          ),
      ],
      exibir: (produto) => ListTile(
        leading: CircleAvatar(
          child: Icon(
            produto is Bolo || produto is BoloACombinar
                ? Icons.cake_outlined
                : Icons.restaurant_outlined,
          ),
        ),
        title: Text(produto.nome),
        subtitle: Text(
          '${produto.categoria}  •  ${formatarPreco(produto.precoBase)}',
        ),
        onTap: () => _verProduto(produto),
        trailing: _acoes(
          () => _salvarProduto(produto),
          () => _excluirProduto(produto),
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return DefaultTabController(
      length: 3,
      child: Scaffold(
        appBar: AppBar(
          title: const Text(nomeSistema),
          bottom: const TabBar(
            tabs: [
              Tab(text: 'Produtos'),
              Tab(text: 'Encomendas'),
              Tab(text: 'Clientes'),
            ],
          ),
        ),
        body: TabBarView(
          children: [
            _catalogo(),
            _lista(
              titulo: 'Pedidos',
              botao: 'Nova encomenda',
              adicionar: _salvarEncomenda,
              dados: _encomendas,
              vazio: 'Nenhuma encomenda cadastrada.',
              exibir: (encomenda) => ListTile(
                leading: const CircleAvatar(
                  child: Icon(Icons.receipt_long_outlined),
                ),
                title: Text('#${encomenda.id} · ${encomenda.cliente.nome}'),
                subtitle: Text(
                  '${encomenda.status}  •  ${formatarData(encomenda.dataEntrega)}\n'
                  '${formatarPreco(encomenda.calcularValorTotal())}',
                ),
                isThreeLine: true,
                onTap: () => _verEncomenda(encomenda),
                trailing: _acoes(
                  () => _salvarEncomenda(encomenda),
                  () => _excluir(
                    'Excluir a encomenda #${encomenda.id}?',
                    () => _encomendas.remove(encomenda),
                  ),
                ),
              ),
            ),
            _lista(
              titulo: 'Clientes',
              botao: 'Novo cliente',
              adicionar: _salvarCliente,
              dados: _clientes,
              vazio: 'Nenhum cliente cadastrado.',
              exibir: (cliente) => ListTile(
                leading: const CircleAvatar(child: Icon(Icons.person_outline)),
                title: Text(cliente.nome),
                subtitle: Text(
                  'CPF: ${cliente.cpf ?? '—'}  •  Tel.: ${cliente.telefone ?? '—'}\n'
                  'E-mail: ${cliente.email ?? '—'}',
                ),
                isThreeLine: true,
                onTap: () => _salvarCliente(cliente),
                trailing: _acoes(
                  () => _salvarCliente(cliente),
                  () => _excluirCliente(cliente),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
