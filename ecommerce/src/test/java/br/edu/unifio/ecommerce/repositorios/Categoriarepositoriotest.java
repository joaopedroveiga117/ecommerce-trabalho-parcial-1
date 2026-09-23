package br.edu.unifio.ecommerce.repositorios;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import br.edu.unifio.ecommerce.entidades.Categoria;

@DataJpaTest
@ActiveProfiles("test")
class CategoriaRepositorioTest {

    @Autowired
    private CategoriaRepositorio categoriaRepositorio;

    @Test
    void deveInserirCategoria() {
        Categoria categoria = new Categoria();
        categoria.setNome("Livros");
        categoria.setDescricao("Livros em geral");

        Categoria categoriaSalva = categoriaRepositorio.save(categoria);

        assertThat(categoriaSalva.getId()).isNotNull();
        assertThat(categoriaSalva.getNome()).isEqualTo("Livros");
        assertThat(categoriaSalva.getDescricao()).isEqualTo("Livros em geral");
    }

    @Test
    void deveBuscarCategoriaPorId() {
        Categoria categoria = new Categoria();
        categoria.setNome("Eletrônicos");
        categoria.setDescricao("Produtos eletrônicos em geral");
        Categoria categoriaSalva = categoriaRepositorio.save(categoria);

        Optional<Categoria> resultado = categoriaRepositorio.findById(categoriaSalva.getId());

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNome()).isEqualTo("Eletrônicos");
        assertThat(resultado.get().getDescricao()).isEqualTo("Produtos eletrônicos em geral");
    }

    @Test
    void deveListarCategorias() {
        Categoria categoria1 = new Categoria();
        categoria1.setNome("Informática");
        categoria1.setDescricao("Produtos de informática");
        categoriaRepositorio.save(categoria1);

        Categoria categoria2 = new Categoria();
        categoria2.setNome("Móveis");
        categoria2.setDescricao("Móveis para casa");
        categoriaRepositorio.save(categoria2);

        List<Categoria> categorias = categoriaRepositorio.findAll();

        assertThat(categorias.size()).isGreaterThanOrEqualTo(2);
        assertThat(categorias).extracting(Categoria::getNome).contains("Informática", "Móveis");
    }

    @Test
    void deveAlterarCategoria() {
        Categoria categoria = new Categoria();
        categoria.setNome("Brinquedos");
        categoria.setDescricao("Brinquedos infantis");
        Categoria categoriaSalva = categoriaRepositorio.save(categoria);
        Short id = categoriaSalva.getId();
        long totalAntes = categoriaRepositorio.count();

        categoriaSalva.setDescricao("Brinquedos e jogos infantis");
        categoriaRepositorio.save(categoriaSalva);

        long totalDepois = categoriaRepositorio.count();
        Optional<Categoria> resultado = categoriaRepositorio.findById(id);

        assertThat(totalDepois).isEqualTo(totalAntes);
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getDescricao()).isEqualTo("Brinquedos e jogos infantis");
    }

    @Test
    void deveExcluirCategoria() {
        Categoria categoria = new Categoria();
        categoria.setNome("Temporária");
        categoria.setDescricao("Categoria para exclusão");
        Categoria categoriaSalva = categoriaRepositorio.save(categoria);
        Short id = categoriaSalva.getId();

        assertThat(categoriaRepositorio.existsById(id)).isTrue();

        categoriaRepositorio.deleteById(id);

        assertThat(categoriaRepositorio.existsById(id)).isFalse();
    }
}