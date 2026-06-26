package com.raizesnordeste.api.application.service;

import com.raizesnordeste.api.api.dto.request.ProdutoRequest;
import com.raizesnordeste.api.api.dto.response.ProdutoResponse;
import com.raizesnordeste.api.application.exception.RecursoDuplicadoException;
import com.raizesnordeste.api.application.exception.RecursoNaoEncontradoException;
import com.raizesnordeste.api.domain.Produto;
import com.raizesnordeste.api.infrastructure.repository.ProdutoRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@RequiredArgsConstructor
@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public List<ProdutoResponse> listarProdutos(){
        log.info("Iniciando listagem dos produtos cadastrados");

        List<ProdutoResponse> produtosResponse = produtoRepository.findAll()
                .stream()
                .map(this::toProdutoResponse)
                .toList();

        log.info("Listagem finalizada com sucesso");

        return produtosResponse;
    }

    public ProdutoResponse buscarProdutoPorId(Long id){
        log.info("Iniciando busca do produto com id {}", id);

        Produto produtoEncontrado = produtoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Produto não encontrado com ID: {}", id);
                    return new RecursoNaoEncontradoException("Produto não encontrado");
                });

        log.info("Produto encontrado com ID: {}", id);

        return toProdutoResponse(produtoEncontrado);
    }

    public ProdutoResponse cadastrarProduto(ProdutoRequest produtoRequest){
        log.info("Iniciando cadastro de novo produto");

        if(produtoRepository.existsByNome(produtoRequest.nome())){
            log.warn("Produto com nome: {} já existe", produtoRequest.nome());
            throw new RecursoDuplicadoException("Produto já cadastrado com este nome");
        }

        Produto novoProduto = new Produto();

        novoProduto.setNome(produtoRequest.nome());
        novoProduto.setDescricao(produtoRequest.descricao());

        Produto produtoSalvo = produtoRepository.save(novoProduto);

        log.info("Novo produto cadastrado com sucesso");

        return toProdutoResponse(produtoSalvo);
    }

    public ProdutoResponse atualizarProduto(Long id, ProdutoRequest produtoRequest){
        log.info("Iniciando atualização do produto com id {}", id);

        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Produto para atualização não encontrado com ID: {}", id);
                    return new RecursoNaoEncontradoException("Produto não encontrado");
                });

        if (produtoRepository.existsByNomeAndIdNot(produtoRequest.nome(), id)) {
            log.warn("Já existe outro produto com nome: {}", produtoRequest.nome());
            throw new RecursoDuplicadoException(
                    "Já existe outro produto cadastrado com nome: " + produtoRequest.nome());
        }


        produto.setNome(produtoRequest.nome());
        produto.setDescricao(produtoRequest.descricao());

        produtoRepository.save(produto);

        log.info("Produto atualizado com sucesso");

        return toProdutoResponse(produto);
    }

    private ProdutoResponse toProdutoResponse(Produto produto){
        return new ProdutoResponse(
                produto.getId(),
                produto.getNome(),
                produto.getDescricao()
        );
    }
}
