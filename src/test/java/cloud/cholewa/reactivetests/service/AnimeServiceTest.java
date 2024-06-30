package cloud.cholewa.reactivetests.service;

import cloud.cholewa.reactivetests.domain.Anime;
import cloud.cholewa.reactivetests.repository.AnimeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnimeServiceTest {

    @InjectMocks
    private AnimeService animeService;
    @Mock
    private AnimeRepository animeRepository;

    /*
        @BeforeAll
        static void setup() {
            BlockHound.install();
        }
        @Test
        void blockHoundWorks() {
            try {
                FutureTask<?> task = new FutureTask<Object>(() -> {
                    Thread.sleep(0);
                    return "";
                });
                Schedulers.parallel().schedule(task);

                task.get(10, TimeUnit.SECONDS);
                Assertions.fail();
            } catch (Exception exception) {
                Assertions.assertInstanceOf(BlockingOperationError.class, exception.getCause());
            }
        }
    */
    @Test
    void shouldReturnAllAnimes() {
        Flux<Anime> animeList = Flux.just(
            Anime.builder().name("one").build(),
            Anime.builder().name("two").build()
        );

        when(animeRepository.findAll()).thenReturn(animeList);

        animeService.findAll()
            .as(StepVerifier::create)
            .expectNextCount(2)
            .verifyComplete();

        verify(animeRepository, times(1)).findAll();
    }

    @Test
    void shouldAddAnime() {
        Anime anime = Anime.builder().name("aaa").build();

        when(animeRepository.save(any())).thenReturn(Mono.empty());

        animeService.save(anime)
            .as(StepVerifier::create)
            .verifyComplete();

        verify(animeRepository, times(1)).save(anime);
    }

    @Test
    void shouldFindAnimeById() {
        Anime anime = Anime.builder().name("aaa").build();

        when(animeRepository.findById(1)).thenReturn(Mono.just(anime));

        animeService.findById(1)
            .as(StepVerifier::create)
            .expectNext(anime)
            .verifyComplete();

        verify(animeRepository, times(1)).findById(1);
    }

    @Test
    void shouldThrowExceptionWhenAnimeNotFoundById() {
        when(animeRepository.findById(anyInt())).thenReturn(Mono.empty());

        animeService.findById(1)
            .as(StepVerifier::create)
            .expectError(ResponseStatusException.class)
            .verify();

        verify(animeRepository, times(1)).findById(anyInt());
    }

    @Test
    void shouldUpdateAnimeWhenExists() {
        Anime anime = Anime.builder().name("aaa").build();

        when(animeRepository.findById(anyInt())).thenReturn(Mono.just(anime));
        when(animeRepository.save(any())).thenReturn(Mono.just(anime));

        animeService.update(1, anime)
            .as(StepVerifier::create)
            .verifyComplete();

        verify(animeRepository, times(1)).findById(anyInt());
        verify(animeRepository, times(1)).save(any(Anime.class));
    }

    @Test
    void shouldNotUpdateAnimeWhenNotExists() {
        Anime anime = Anime.builder().name("aaa").build();

        when(animeRepository.findById(anyInt())).thenReturn(Mono.empty());

        animeService.update(1, anime)
            .as(StepVerifier::create)
            .expectError(ResponseStatusException.class)
            .verify();

        verify(animeRepository, times(1)).findById(anyInt());
        verify(animeRepository, never()).save(any());
    }

    @Test
    void shouldDeleteAnimeWhenExists() {
        Anime anime = Anime.builder().id(1).name("aaa").build();

        when(animeRepository.findById(anyInt())).thenReturn(Mono.just(anime));
        when(animeRepository.delete(any())).thenReturn(Mono.empty());

        animeService.delete(1)
            .as(StepVerifier::create)
            .verifyComplete();

        verify(animeRepository, times(1)).findById(anyInt());
        verify(animeRepository, times(1)).delete(any());
    }

    @Test
    void shouldNotDeleteAnimeWhenNotExistsAndThrowException() {
        when(animeRepository.findById(anyInt())).thenReturn(Mono.empty());

        animeService.delete(1)
            .as(StepVerifier::create)
            .expectError(ResponseStatusException.class)
            .verify();

        verify(animeRepository, times(1)).findById(anyInt());
        verify(animeRepository, never()).delete(any());
    }
}