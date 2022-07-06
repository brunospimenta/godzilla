package br.com.pepper.godzilla.controllers;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.pepper.godzilla.dtos.FilmeDto;
import br.com.pepper.godzilla.models.Filme;
import br.com.pepper.godzilla.services.FilmeService;

@RestController
@CrossOrigin(origins = "*")
public class FilmeController {

    @Autowired
    FilmeService filmeService;

    @GetMapping("/godzilla/filmes")
    public ResponseEntity<Page<Filme>> getAllFilmes(
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<Filme> filmes = filmeService.findAll(pageable);
        return new ResponseEntity<Page<Filme>>(filmes, HttpStatus.OK);
    }

    @GetMapping("/godzilla")
    public List<Filme> FindWithFilter(@RequestParam("titulo") String titulo) {
        List<Filme> filme = filmeService.findByTituloContains(titulo);
        return filme;
    }

    @PostMapping("/godzilla/filmes")
    @Transactional
    public ResponseEntity<Object> addFilme(@RequestBody @Valid FilmeDto filmeDto) {
        if (filmeService.existsByTitulo(filmeDto.getTitulo())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Este filme ja esta listado");
        }

        Filme filme = new Filme();
        BeanUtils.copyProperties(filmeDto, filme);
        if (filme.getImageUrl() == null) {
            filme.setImageUrl(
                    "https://cdn.neemo.com.br/uploads/settings_webdelivery/logo/2496/not-found-image-15383864787lu.jpg");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(filmeService.save(filme));
    }

    @PutMapping("/godzilla/filmes/{id}")
    @Transactional
    public ResponseEntity<?> alugar(@PathVariable Long id) {
        Optional<Filme> optional = filmeService.findById(id);

        if (optional.get().getEstoque() == 0) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Filme sem estoque!");
        }

        Filme filme = filmeService.alugar(id);
        optional.get().setEstoque(optional.get().getEstoque() - 1);

        return ResponseEntity.ok(filmeService.save(filme));
    }

}