package br.com.pepper.godzilla.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.pepper.godzilla.models.Filme;
import br.com.pepper.godzilla.repositories.FilmeRepository;

@Service
public class FilmeService {
    
    @Autowired
    FilmeRepository filmeRepository;

    public Page<Filme> findAll(Pageable pageable){
        return filmeRepository.findAll(pageable);
    }

    public Optional<Filme> findById(Long id){
        return filmeRepository.findById(id);
    }

    public boolean existsByTitulo(String titulo) {
        return filmeRepository.existsByTitulo(titulo);
    }


    public Filme save(Filme filme){
        return filmeRepository.save(filme);
    }

    public Filme alugar(Long id) {

        Filme filme = filmeRepository.getReferenceById(id);
        return filme;
    }

    public List<Filme> findByTituloContains(String titulo) {
        return filmeRepository.findByTituloContains(titulo);
    }


}