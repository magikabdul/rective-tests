package cloud.cholewa.reactivetests.repository;

import cloud.cholewa.reactivetests.domain.Anime;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface AnimeRepository extends ReactiveCrudRepository<Anime, Integer> {

}
