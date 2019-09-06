package ar.com.enrique.apimanager.service.dbAcces;

import ar.com.enrique.apimanager.common.PageRequest;
import ar.com.enrique.apimanager.common.SpecificationBuilder;
import ar.com.enrique.apimanager.model.MovieData;
import ar.com.enrique.apimanager.repository.MovieDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MovieDataDBService {

    @Autowired
    MovieDataRepository movieDataRepository;

    public void saveMovie(MovieData movieData){
        movieDataRepository.save(movieData);
    }

    public List<MovieData> findAllMovies(PageRequest pageRequest, String apiKey, String movieName, String tipoTitulo, String year ){

        SpecificationBuilder<MovieData> spBuilder = new SpecificationBuilder<MovieData>()
                .attrSimilarIfNotNull("title", movieName)
                .attrEqualsIfNotNull("type", tipoTitulo)
                .attrEqualsIfNotNull("year", year);
        return movieDataRepository.findAll(spBuilder.getSpecification());
    }

    public void saveMovieList(List<MovieData> movieList){
        for (MovieData movieData : movieList){
            saveMovie(movieData);
        }
    }

    public MovieData findMovie(String imdbId){
        return movieDataRepository.findByImdbID(imdbId);
    }

//    public void updateMovie(MovieData movieData){
//        movieDataRepository.
//    }


}
