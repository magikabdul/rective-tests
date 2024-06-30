package cloud.cholewa.reactivetests.controller;

import cloud.cholewa.reactivetests.domain.Anime;
import cloud.cholewa.reactivetests.service.AnimeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("animes")
@Slf4j
@RequiredArgsConstructor
public class AnimeController {

    private final AnimeService animeService;

    @GetMapping
    Flux<Anime> listAll() {
        return animeService.findAll();
    }

    @GetMapping("{id}")
    Mono<Anime> findById(@PathVariable Integer id) {
        return animeService.findById(id);
    }

    @PostMapping
    Mono<Anime> save(@Valid @RequestBody Anime anime) {
        return animeService.save(anime);
    }

    @PutMapping("{id}")
    Mono<Void> update(@Valid @RequestBody Anime anime, @PathVariable Integer id) {
        return animeService.update(id, anime);
    }

    @DeleteMapping("{id}")
    Mono<Void> delete(@PathVariable Integer id) {
        return animeService.delete(id);
    }
}
