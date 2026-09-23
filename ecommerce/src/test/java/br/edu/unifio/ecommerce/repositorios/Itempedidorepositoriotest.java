package br.edu.unifio.ecommerce.repositorios;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import br.edu.unifio.ecommerce.entidades.Categoria;
import br.edu.unifio.ecommerce.entidades.Cliente;
import br.edu.unifio.ecommerce.entidades.ItemPedido;
import br.edu.unifio.ecommerce.entidades.Pedido;
import br.edu.unifio.ecommerce.entidades.Produto;

@DataJpaTest
@ActiveProfiles("test")
class ItemPedidoRepositorioTest {

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    @Autowired
    private PedidoRepositorio pedidoRepositorio;

    @Autowired
    private CategoriaRepositorio categoriaRepositorio;

    @Autowired
    private ProdutoRepositorio produtoRepositorio;

    @Autowired
    private ItemPedidoRepositorio itemPedidoRepositorio;

    private Pedido criarPedidoPersistido(String nomeCliente) {
        Cliente cliente = new Cliente();
        cliente.setNome(nomeCliente);
        cliente.setEmail(nomeCliente.toLowerCase().replace(" ", ".") + "@email.com");
        cliente.setTelefone("43900000000");
        clienteRepositorio.save(cliente);

        Pedido pedido = new Pedido();
        pedido.setData(LocalDateTime.now());
        pedido.setStatus("ABERTO");
        pedido.setValorTotal(new BigDecimal("0.00"));
        pedido.setCliente(cliente);
        return pedidoRepositorio.save(pedido);
    }

    private Produto criarProdutoPersistido(String nomeProduto) {
        Categoria categoria = new Categoria();
        categoria.setNome("Categoria " + nomeProduto);
        categoria.setDescricao("Categoria para " + nomeProduto);
        categoriaRepositorio.save(categoria);

        Produto produto = new Produto();
        produto.setNome(nomeProduto);
        produto.setDescricao("Descrição de " + nomeProduto);
        produto.setEstoque((short) 100);
        produto.setPreco(new BigDecimal("50.00"));
        produto.setCategoria(categoria);
        return produtoRepositorio.save(produto);
    }

    @Test
    void deveInserirItemPedido() {
        Pedido pedido = criarPedidoPersistido("Ana Lima");
        Produto produto = criarProdutoPersistido("Teclado mecânico");

        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setQuantidade(2);
        itemPedido.setValorUnitario(new BigDecimal("299.90"));
        itemPedido.setPedido(pedido);
        itemPedido.setProduto(produto);

        ItemPedido itemPedidoSalvo = itemPedidoRepositorio.save(itemPedido);

        assertThat(itemPedidoSalvo.getId()).isNotNull();
        assertThat(itemPedidoSalvo.getQuantidade()).isEqualTo(2);
        assertThat(itemPedidoSalvo.getProduto().getNome()).isEqualTo("Teclado mecânico");
    }

    @Test
    void deveBuscarItemPedidoPorId() {
        Pedido pedido = criarPedidoPersistido("Carlos Nunes");
        Produto produto = criarProdutoPersistido("Mouse sem fio");

        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setQuantidade(1);
        itemPedido.setValorUnitario(new BigDecimal("79.90"));
        itemPedido.setPedido(pedido);
        itemPedido.setProduto(produto);
        ItemPedido itemPedidoSalvo = itemPedidoRepositorio.save(itemPedido);

        Optional<ItemPedido> resultado = itemPedidoRepositorio.findById(itemPedidoSalvo.getId());

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getQuantidade()).isEqualTo(1);
        assertThat(resultado.get().getPedido().getStatus()).isEqualTo("ABERTO");
    }

    @Test
    void deveListarItensPedido() {
        Pedido pedido = criarPedidoPersistido("Pedro Alves");
        Produto produto1 = criarProdutoPersistido("Monitor");
        Produto produto2 = criarProdutoPersistido("Webcam");

        ItemPedido item1 = new ItemPedido();
        item1.setQuantidade(1);
        item1.setValorUnitario(new BigDecimal("899.90"));
        item1.setPedido(pedido);
        item1.setProduto(produto1);
        itemPedidoRepositorio.save(item1);

        ItemPedido item2 = new ItemPedido();
        item2.setQuantidade(3);
        item2.setValorUnitario(new BigDecimal("149.90"));
        item2.setPedido(pedido);
        item2.setProduto(produto2);
        itemPedidoRepositorio.save(item2);

        List<ItemPedido> itens = itemPedidoRepositorio.findAll();

        assertThat(itens.size()).isGreaterThanOrEqualTo(2);
        assertThat(itens).extracting(ItemPedido::getQuantidade).contains(1, 3);
    }

    @Test
    void deveAlterarItemPedido() {
        Pedido pedido = criarPedidoPersistido("Mariana Costa");
        Produto produto = criarProdutoPersistido("Impressora");

        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setQuantidade(1);
        itemPedido.setValorUnitario(new BigDecimal("450.00"));
        itemPedido.setPedido(pedido);
        itemPedido.setProduto(produto);
        ItemPedido itemPedidoSalvo = itemPedidoRepositorio.save(itemPedido);
        Integer id = itemPedidoSalvo.getId();
        long totalAntes = itemPedidoRepositorio.count();

        itemPedidoSalvo.setQuantidade(4);
        itemPedidoRepositorio.save(itemPedidoSalvo);

        long totalDepois = itemPedidoRepositorio.count();
        Optional<ItemPedido> resultado = itemPedidoRepositorio.findById(id);

        assertThat(totalDepois).isEqualTo(totalAntes);
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getQuantidade()).isEqualTo(4);
    }

    @Test
    void deveExcluirItemPedido() {
        Pedido pedido = criarPedidoPersistido("Cliente Temporário");
        Produto produto = criarProdutoPersistido("Produto Temporário");

        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setQuantidade(1);
        itemPedido.setValorUnitario(new BigDecimal("10.00"));
        itemPedido.setPedido(pedido);
        itemPedido.setProduto(produto);
        ItemPedido itemPedidoSalvo = itemPedidoRepositorio.save(itemPedido);
        Integer id = itemPedidoSalvo.getId();

        assertThat(itemPedidoRepositorio.existsById(id)).isTrue();

        itemPedidoRepositorio.deleteById(id);

        assertThat(itemPedidoRepositorio.existsById(id)).isFalse();
    }
}