package br.edu.unifio.ecommerce.repositorios;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import br.edu.unifio.ecommerce.entidades.Cliente;

@DataJpaTest
@ActiveProfiles("test")
class ClienteRepositorioTest {

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    @Test
    void deveInserirCliente() {
        Cliente cliente = new Cliente();
        cliente.setNome("Maria Souza");
        cliente.setEmail("maria.souza@email.com");
        cliente.setTelefone("43999990000");

        Cliente clienteSalvo = clienteRepositorio.save(cliente);

        assertThat(clienteSalvo.getId()).isNotNull();
        assertThat(clienteSalvo.getNome()).isEqualTo("Maria Souza");
        assertThat(clienteSalvo.getEmail()).isEqualTo("maria.souza@email.com");
    }

    @Test
    void deveBuscarClientePorId() {
        Cliente cliente = new Cliente();
        cliente.setNome("João Pereira");
        cliente.setEmail("joao.pereira@email.com");
        cliente.setTelefone("43988887777");
        Cliente clienteSalvo = clienteRepositorio.save(cliente);

        Optional<Cliente> resultado = clienteRepositorio.findById(clienteSalvo.getId());

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNome()).isEqualTo("João Pereira");
        assertThat(resultado.get().getEmail()).isEqualTo("joao.pereira@email.com");
    }

    @Test
    void deveListarClientes() {
        Cliente cliente1 = new Cliente();
        cliente1.setNome("Ana Lima");
        cliente1.setEmail("ana.lima@email.com");
        cliente1.setTelefone("43977776666");
        clienteRepositorio.save(cliente1);

        Cliente cliente2 = new Cliente();
        cliente2.setNome("Carlos Nunes");
        cliente2.setEmail("carlos.nunes@email.com");
        cliente2.setTelefone("43966665555");
        clienteRepositorio.save(cliente2);

        List<Cliente> clientes = clienteRepositorio.findAll();

        assertThat(clientes.size()).isGreaterThanOrEqualTo(2);
        assertThat(clientes).extracting(Cliente::getNome).contains("Ana Lima", "Carlos Nunes");
    }

    @Test
    void deveAlterarCliente() {
        Cliente cliente = new Cliente();
        cliente.setNome("Pedro Alves");
        cliente.setEmail("pedro.alves@email.com");
        cliente.setTelefone("43955554444");
        Cliente clienteSalvo = clienteRepositorio.save(cliente);
        Integer id = clienteSalvo.getId();
        long totalAntes = clienteRepositorio.count();

        clienteSalvo.setTelefone("43911112222");
        clienteRepositorio.save(clienteSalvo);

        long totalDepois = clienteRepositorio.count();
        Optional<Cliente> resultado = clienteRepositorio.findById(id);

        assertThat(totalDepois).isEqualTo(totalAntes);
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getTelefone()).isEqualTo("43911112222");
    }

    @Test
    void deveExcluirCliente() {
        Cliente cliente = new Cliente();
        cliente.setNome("Cliente Temporário");
        cliente.setEmail("temporario@email.com");
        cliente.setTelefone("43900000000");
        Cliente clienteSalvo = clienteRepositorio.save(cliente);
        Integer id = clienteSalvo.getId();

        assertThat(clienteRepositorio.existsById(id)).isTrue();

        clienteRepositorio.deleteById(id);

        assertThat(clienteRepositorio.existsById(id)).isFalse();
    }
}