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
import br.edu.unifio.ecommerce.entidades.Pedido;

@DataJpaTest
@ActiveProfiles("test")
class PedidoRepositorioTest {

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    @Autowired
    private PedidoRepositorio pedidoRepositorio;

    private Cliente criarClientePersistido(String nome) {
        Cliente cliente = new Cliente();
        cliente.setNome(nome);
        cliente.setEmail(nome.toLowerCase().replace(" ", ".") + "@email.com");
        cliente.setTelefone("43900000000");
        return clienteRepositorio.save(cliente);
    }

    @Test
    void deveInserirPedido() {
        Cliente cliente = criarClientePersistido("João Pereira");

        Pedido pedido = new Pedido();
        pedido.setData(LocalDateTime.now());
        pedido.setStatus("ABERTO");
        pedido.setValorTotal(new BigDecimal("150.00"));
        pedido.setCliente(cliente);

        Pedido pedidoSalvo = pedidoRepositorio.save(pedido);

        assertThat(pedidoSalvo.getId()).isNotNull();
        assertThat(pedidoSalvo.getStatus()).isEqualTo("ABERTO");
        assertThat(pedidoSalvo.getCliente().getNome()).isEqualTo("João Pereira");
    }

    @Test
    void deveBuscarPedidoPorId() {
        Cliente cliente = criarClientePersistido("Ana Lima");

        Pedido pedido = new Pedido();
        pedido.setData(LocalDateTime.now());
        pedido.setStatus("ABERTO");
        pedido.setValorTotal(new BigDecimal("200.00"));
        pedido.setCliente(cliente);
        Pedido pedidoSalvo = pedidoRepositorio.save(pedido);

        Optional<Pedido> resultado = pedidoRepositorio.findById(pedidoSalvo.getId());

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getStatus()).isEqualTo("ABERTO");
        assertThat(resultado.get().getCliente().getNome()).isEqualTo("Ana Lima");
    }

    @Test
    void deveListarPedidos() {
        Cliente cliente = criarClientePersistido("Carlos Nunes");

        Pedido pedido1 = new Pedido();
        pedido1.setData(LocalDateTime.now());
        pedido1.setStatus("ABERTO");
        pedido1.setValorTotal(new BigDecimal("100.00"));
        pedido1.setCliente(cliente);
        pedidoRepositorio.save(pedido1);

        Pedido pedido2 = new Pedido();
        pedido2.setData(LocalDateTime.now());
        pedido2.setStatus("FINALIZADO");
        pedido2.setValorTotal(new BigDecimal("300.00"));
        pedido2.setCliente(cliente);
        pedidoRepositorio.save(pedido2);

        List<Pedido> pedidos = pedidoRepositorio.findAll();

        assertThat(pedidos.size()).isGreaterThanOrEqualTo(2);
        assertThat(pedidos).extracting(Pedido::getStatus).contains("ABERTO", "FINALIZADO");
    }

    @Test
    void deveAlterarPedido() {
        Cliente cliente = criarClientePersistido("Pedro Alves");

        Pedido pedido = new Pedido();
        pedido.setData(LocalDateTime.now());
        pedido.setStatus("ABERTO");
        pedido.setValorTotal(new BigDecimal("120.00"));
        pedido.setCliente(cliente);
        Pedido pedidoSalvo = pedidoRepositorio.save(pedido);
        Integer id = pedidoSalvo.getId();
        long totalAntes = pedidoRepositorio.count();

        pedidoSalvo.setStatus("FINALIZADO");
        pedidoRepositorio.save(pedidoSalvo);

        long totalDepois = pedidoRepositorio.count();
        Optional<Pedido> resultado = pedidoRepositorio.findById(id);

        assertThat(totalDepois).isEqualTo(totalAntes);
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getStatus()).isEqualTo("FINALIZADO");
    }

    @Test
    void deveExcluirPedido() {
        Cliente cliente = criarClientePersistido("Cliente Temporário");

        Pedido pedido = new Pedido();
        pedido.setData(LocalDateTime.now());
        pedido.setStatus("ABERTO");
        pedido.setValorTotal(new BigDecimal("50.00"));
        pedido.setCliente(cliente);
        Pedido pedidoSalvo = pedidoRepositorio.save(pedido);
        Integer id = pedidoSalvo.getId();

        assertThat(pedidoRepositorio.existsById(id)).isTrue();

        pedidoRepositorio.deleteById(id);

        assertThat(pedidoRepositorio.existsById(id)).isFalse();
    }
}