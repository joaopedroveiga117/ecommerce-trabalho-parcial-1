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

import br.edu.unifio.ecommerce.entidades.Cliente;
import br.edu.unifio.ecommerce.entidades.Pagamento;
import br.edu.unifio.ecommerce.entidades.Pedido;

@DataJpaTest
@ActiveProfiles("test")
class PagamentoRepositorioTest {

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    @Autowired
    private PedidoRepositorio pedidoRepositorio;

    @Autowired
    private PagamentoRepositorio pagamentoRepositorio;

    private Pedido criarPedidoPersistido(String nomeCliente, BigDecimal valorTotal) {
        Cliente cliente = new Cliente();
        cliente.setNome(nomeCliente);
        cliente.setEmail(nomeCliente.toLowerCase().replace(" ", ".") + "@email.com");
        cliente.setTelefone("43900000000");
        clienteRepositorio.save(cliente);

        Pedido pedido = new Pedido();
        pedido.setData(LocalDateTime.now());
        pedido.setStatus("FINALIZADO");
        pedido.setValorTotal(valorTotal);
        pedido.setCliente(cliente);
        return pedidoRepositorio.save(pedido);
    }

    @Test
    void deveInserirPagamento() {
        Pedido pedido = criarPedidoPersistido("Carlos Nunes", new BigDecimal("450.00"));

        Pagamento pagamento = new Pagamento();
        pagamento.setValor(new BigDecimal("450.00"));
        pagamento.setData(LocalDateTime.now());
        pagamento.setStatus("APROVADO");
        pagamento.setTipo("CARTAO_CREDITO");
        pagamento.setPedido(pedido);

        Pagamento pagamentoSalvo = pagamentoRepositorio.save(pagamento);

        assertThat(pagamentoSalvo.getId()).isNotNull();
        assertThat(pagamentoSalvo.getStatus()).isEqualTo("APROVADO");
        assertThat(pagamentoSalvo.getPedido().getStatus()).isEqualTo("FINALIZADO");
    }

    @Test
    void deveBuscarPagamentoPorId() {
        Pedido pedido = criarPedidoPersistido("Mariana Costa", new BigDecimal("300.00"));

        Pagamento pagamento = new Pagamento();
        pagamento.setValor(new BigDecimal("300.00"));
        pagamento.setData(LocalDateTime.now());
        pagamento.setStatus("APROVADO");
        pagamento.setTipo("PIX");
        pagamento.setPedido(pedido);
        Pagamento pagamentoSalvo = pagamentoRepositorio.save(pagamento);

        Optional<Pagamento> resultado = pagamentoRepositorio.findById(pagamentoSalvo.getId());

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getTipo()).isEqualTo("PIX");
        assertThat(resultado.get().getPedido().getStatus()).isEqualTo("FINALIZADO");
    }

    @Test
    void deveListarPagamentos() {
        Pedido pedido1 = criarPedidoPersistido("Pedro Alves", new BigDecimal("120.00"));
        Pedido pedido2 = criarPedidoPersistido("Ana Lima", new BigDecimal("220.00"));

        Pagamento pagamento1 = new Pagamento();
        pagamento1.setValor(new BigDecimal("120.00"));
        pagamento1.setData(LocalDateTime.now());
        pagamento1.setStatus("APROVADO");
        pagamento1.setTipo("BOLETO");
        pagamento1.setPedido(pedido1);
        pagamentoRepositorio.save(pagamento1);

        Pagamento pagamento2 = new Pagamento();
        pagamento2.setValor(new BigDecimal("220.00"));
        pagamento2.setData(LocalDateTime.now());
        pagamento2.setStatus("PENDENTE");
        pagamento2.setTipo("PIX");
        pagamento2.setPedido(pedido2);
        pagamentoRepositorio.save(pagamento2);

        List<Pagamento> pagamentos = pagamentoRepositorio.findAll();

        assertThat(pagamentos.size()).isGreaterThanOrEqualTo(2);
        assertThat(pagamentos).extracting(Pagamento::getTipo).contains("BOLETO", "PIX");
    }

    @Test
    void deveAlterarPagamento() {
        Pedido pedido = criarPedidoPersistido("João Pereira", new BigDecimal("500.00"));

        Pagamento pagamento = new Pagamento();
        pagamento.setValor(new BigDecimal("500.00"));
        pagamento.setData(LocalDateTime.now());
        pagamento.setStatus("PENDENTE");
        pagamento.setTipo("BOLETO");
        pagamento.setPedido(pedido);
        Pagamento pagamentoSalvo = pagamentoRepositorio.save(pagamento);
        Integer id = pagamentoSalvo.getId();
        long totalAntes = pagamentoRepositorio.count();

        pagamentoSalvo.setStatus("APROVADO");
        pagamentoRepositorio.save(pagamentoSalvo);

        long totalDepois = pagamentoRepositorio.count();
        Optional<Pagamento> resultado = pagamentoRepositorio.findById(id);

        assertThat(totalDepois).isEqualTo(totalAntes);
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getStatus()).isEqualTo("APROVADO");
    }

    @Test
    void deveExcluirPagamento() {
        Pedido pedido = criarPedidoPersistido("Cliente Temporário", new BigDecimal("10.00"));

        Pagamento pagamento = new Pagamento();
        pagamento.setValor(new BigDecimal("10.00"));
        pagamento.setData(LocalDateTime.now());
        pagamento.setStatus("APROVADO");
        pagamento.setTipo("PIX");
        pagamento.setPedido(pedido);
        Pagamento pagamentoSalvo = pagamentoRepositorio.save(pagamento);
        Integer id = pagamentoSalvo.getId();

        assertThat(pagamentoRepositorio.existsById(id)).isTrue();

        pagamentoRepositorio.deleteById(id);

        assertThat(pagamentoRepositorio.existsById(id)).isFalse();
    }
}