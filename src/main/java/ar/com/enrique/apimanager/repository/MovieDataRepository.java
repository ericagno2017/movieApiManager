package ar.com.enrique.apimanager.repository;

import ar.com.enrique.apimanager.model.MovieData;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieDataRepository extends PagingAndSortingRepository<MovieData, String>,
        JpaSpecificationExecutor<MovieData> {

   MovieData findByImdbID(String imdbID);
   MovieData findByTitle(String title);
}
