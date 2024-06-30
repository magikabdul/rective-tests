package cloud.cholewa.reactivetests.service;

import cloud.cholewa.reactivetests.domain.Anime;
import cloud.cholewa.reactivetests.repository.AnimeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
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

    @ParameterizedTest(name = "{0}")
    @MethodSource("serviceParams")
    void serviceTests(
        final String name
    ) {

        animeService.findById(1)
            .as(StepVerifier::create)
            .expectNextCount(1)
            .verifyComplete();
    }

    private static Stream<Arguments> serviceParams() {
        return Stream.of(
            Arguments.of(
                "anime with id exists"
            ),
            Arguments.of(
                "anime with id does not exists"
            )
        );
    }
}