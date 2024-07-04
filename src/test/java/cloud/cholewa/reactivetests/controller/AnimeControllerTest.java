package cloud.cholewa.reactivetests.controller;

import cloud.cholewa.reactivetests.domain.Anime;
import cloud.cholewa.reactivetests.repository.AnimeRepository;
import cloud.cholewa.reactivetests.service.AnimeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@WebFluxTest
@Import(AnimeService.class)
class AnimeControllerTest {

    Anime anime = Anime.builder().id(1).name("aaa").build();

    @MockBean
    private AnimeRepository animeRepository;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void should_returnSuccessful_whenListAll() {
        when(animeRepository.findAll()).thenReturn(Flux.just(anime));

        webTestClient
            .get()
            .uri("/animes")
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.[0].id").isEqualTo(anime.getId())
            .jsonPath("$.[0].name").isEqualTo(anime.getName());

        webTestClient.get()
            .uri("/animes")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(Anime.class)
            .hasSize(1)
            .contains(anime);
    }

    @Test
    void should_return_anime_by_id() {
        when(animeRepository.findById(anyInt())).thenReturn(Mono.just(anime));

        webTestClient
            .get()
            .uri("/animes/{id}", 1)
            .exchange()
            .expectStatus().isOk()
            .expectBody(Anime.class)
            .isEqualTo(anime);
    }

    @Test
    void should_return_not_found_error_when_not_found() {
        when(animeRepository.findById(anyInt())).thenReturn(Mono.empty());

        webTestClient
            .get()
            .uri("/animes/{id}", 1)
            .exchange()
            .expectStatus().isNotFound();
    }

    @Test
    void should_delete_anime_by_id_when_anime_exists() {
        when(animeRepository.findById(anyInt())).thenReturn(Mono.just(anime));
        when(animeRepository.delete(any())).thenReturn(Mono.empty());

        webTestClient
            .delete()
            .uri("/animes/{id}", 1)
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    void should_return_error_when_anime_not_exists() {
        when(animeRepository.findById(anyInt())).thenReturn(Mono.empty());

        webTestClient
            .delete()
            .uri("/animes/{id}", 1)
            .exchange()
            .expectStatus().isBadRequest();
    }

    @Test
    void should_add_anime_and_return_ok() {
        when(animeRepository.save(any())).thenReturn(Mono.just(anime));

        webTestClient
            .post()
            .uri("/animes")
            .body(Mono.just(anime), Anime.class)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.name").isEqualTo("aaa");
    }

    @Test
    void should_update_anime_when_anime_exists_and_return_ok() {
        when(animeRepository.findById(anyInt())).thenReturn(Mono.just(anime));
        when(animeRepository.save(any())).thenReturn(Mono.just(anime));

        webTestClient
            .put()
            .uri("/animes/{id}", 1)
            .body(Mono.just(anime), Anime.class)
            .exchange()
            .expectStatus().isOk();

    }

    @Test
    void should_not_update_anime_when_anime_not_exists_and_return_bad_request() {
        when(animeRepository.findById(anyInt())).thenReturn(Mono.empty());

        webTestClient
            .put()
            .uri("/animes/{id}", 1)
            .body(Mono.just(anime), Anime.class)
            .exchange()
            .expectStatus().isBadRequest();
    }
}