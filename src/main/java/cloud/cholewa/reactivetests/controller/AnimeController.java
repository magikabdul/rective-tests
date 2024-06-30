package cloud.cholewa.reactivetests.controller;

import cloud.cholewa.reactivetests.domain.Anime;
import cloud.cholewa.reactivetests.service.AnimeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
