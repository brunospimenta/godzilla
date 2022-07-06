package br.com.pepper.godzilla.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.pepper.godzilla.models.Filme;

public interface FilmeRepository extends JpaRepository<Filme, Long>{

    List<Filme> findByTituloContains(String titulo);

    boolean existsByTitulo(String titulo);
}
