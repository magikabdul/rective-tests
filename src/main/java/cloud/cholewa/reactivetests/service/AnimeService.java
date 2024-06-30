package cloud.cholewa.reactivetests.service;

import cloud.cholewa.reactivetests.domain.Anime;
import cloud.cholewa.reactivetests.repository.AnimeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@org.springframework.stereotype.Service
@Slf4j
@RequiredArgsConstructor
public class AnimeService {

    private final AnimeRepository animeRepository;

    public Flux<Anime> findAll() {
        return animeRepository.findAll();
    }

    public Mono<Anime> findById(int id) {
        return animeRepository.findById(id)
            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    public Mono<Anime> save(Anime anime) {
        return animeRepository.save(anime);
    }

    public Mono<Void> update(Integer id, Anime anime) {
        return animeRepository.findById(id)
            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Anime not found")))
            .map(a -> {
                a.setName(anime.getName());
                return a;
            })
            .flatMap(animeRepository::save)
            .then();

    }

    public Mono<Void> delete(Integer id) {
        return animeRepository.findById(id)
            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Anime not found")))
            .flatMap(animeRepository::delete);
    }
}
