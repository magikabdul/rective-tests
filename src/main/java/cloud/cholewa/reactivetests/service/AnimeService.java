package cloud.cholewa.reactivetests.service;

import cloud.cholewa.reactivetests.domain.Anime;
import cloud.cholewa.reactivetests.repository.AnimeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@org.springframework.stereotype.Service
@Slf4j
@RequiredArgsConstructor
public class AnimeService {

    private final AnimeRepository animeRepository;

    public Flux<Anime> findAll() {
        return animeRepository.findAll();
    }
}
