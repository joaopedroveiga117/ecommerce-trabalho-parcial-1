package br.edu.unifio.ecommerce.repositorios;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import br.edu.unifio.ecommerce.entidades.Categoria;
import br.edu.unifio.ecommerce.entidades.Produto;

@DataJpaTest
@ActiveProfiles("test")
class ProdutoRepositorioTest {

    @Autowired
    private CategoriaRepositorio categoriaRepositorio;

    @Autowired
    private ProdutoRepositorio produtoRepositorio;

    private Categoria criarCategoriaPersistida(String nome) {
        Categoria categoria = new Categoria();
        categoria.setNome(nome);
        categoria.setDescricao("Categoria " + nome);
        return categoriaRepositorio.save(categoria);
    }

    @Test
    void deveInserirProduto() {
        Categoria categoria = criarCategoriaPersistida("Informática");

        Produto produto = new Produto();
        produto.setNome("Mouse sem fio");
        produto.setDescricao("Mouse óptico sem fio");
        produto.setEstoque((short) 50);
        produto.setPreco(new BigDecimal("79.90"));
        produto.setCategoria(categoria);

        Produto produtoSalvo = produtoRepositorio.save(produto);

        assertThat(produtoSalvo.getId()).isNotNull();
        assertThat(produtoSalvo.getNome()).isEqualTo("Mouse sem fio");
        assertThat(produtoSalvo.getCategoria().getNome()).isEqualTo("Informática");
    }

    @Test
    void deveBuscarProdutoPorId() {
        Categoria categoria = criarCategoriaPersistida("Periféricos");

        Produto produto = new Produto();
        produto.setNome("Teclado mecânico");
        produto.setDescricao("Teclado mecânico RGB");
        produto.setEstoque((short) 20);
        produto.setPreco(new BigDecimal("299.90"));
        produto.setCategoria(categoria);
        Produto produtoSalvo = produtoRepositorio.save(produto);

        Optional<Produto> resultado = produtoRepositorio.findById(produtoSalvo.getId());

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNome()).isEqualTo("Teclado mecânico");
        assertThat(resultado.get().getCategoria().getNome()).isEqualTo("Periféricos");
    }

    @Test
    void deveListarProdutos() {
        Categoria categoria = criarCategoriaPersistida("Áudio");

        Produto produto1 = new Produto();
        produto1.setNome("Fone de ouvido");
        produto1.setDescricao("Fone bluetooth");
        produto1.setEstoque((short) 30);
        produto1.setPreco(new BigDecimal("149.90"));
        produto1.setCategoria(categoria);
        produtoRepositorio.save(produto1);

        Produto produto2 = new Produto();
        produto2.setNome("Caixa de som");
        produto2.setDescricao("Caixa de som portátil");
        produto2.setEstoque((short) 15);
        produto2.setPreco(new BigDecimal("199.90"));
        produto2.setCategoria(categoria);
        produtoRepositorio.save(produto2);

        List<Produto> produtos = produtoRepositorio.findAll();

        assertThat(produtos.size()).isGreaterThanOrEqualTo(2);
        assertThat(produtos).extracting(Produto::getNome).contains("Fone de ouvido", "Caixa de som");
    }

    @Test
    void deveAlterarProduto() {
        Categoria categoria = criarCategoriaPersistida("Games");

        Produto produto = new Produto();
        produto.setNome("Controle de videogame");
        produto.setDescricao("Controle sem fio");
        produto.setEstoque((short) 40);
        produto.setPreco(new BigDecimal("249.90"));
        produto.setCategoria(categoria);
        Produto produtoSalvo = produtoRepositorio.save(produto);
        Integer id = produtoSalvo.getId();
        long totalAntes = produtoRepositorio.count();

        produtoSalvo.setPreco(new BigDecimal("199.90"));
        produtoSalvo.setEstoque((short) 35);
        produtoRepositorio.save(produtoSalvo);

        long totalDepois = produtoRepositorio.count();
        Optional<Produto> resultado = produtoRepositorio.findById(id);

        assertThat(totalDepois).isEqualTo(totalAntes);
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getPreco()).isEqualByComparingTo(new BigDecimal("199.90"));
        assertThat(resultado.get().getEstoque()).isEqualTo((short) 35);
    }

    @Test
    void deveExcluirProduto() {
        Categoria categoria = criarCategoriaPersistida("Temporária");

        Produto produto = new Produto();
        produto.setNome("Produto temporário");
        produto.setDescricao("Produto para exclusão");
        produto.setEstoque((short) 10);
        produto.setPreco(new BigDecimal("10.00"));
        produto.setCategoria(categoria);
        Produto produtoSalvo = produtoRepositorio.save(produto);
        Integer id = produtoSalvo.getId();

        assertThat(produtoRepositorio.existsById(id)).isTrue();

        produtoRepositorio.deleteById(id);

        assertThat(produtoRepositorio.existsById(id)).isFalse();
    }
}