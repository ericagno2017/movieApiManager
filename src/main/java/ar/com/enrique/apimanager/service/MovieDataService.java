package ar.com.enrique.apimanager.service;

import ar.com.enrique.apimanager.common.PageRequest;
import ar.com.enrique.apimanager.common.PageResponse;
import ar.com.enrique.apimanager.model.MovieData;
import ar.com.enrique.apimanager.model.MovieDataVo;
import ar.com.enrique.apimanager.service.apiAcces.MovieDataAPIService;
import ar.com.enrique.apimanager.service.dbAcces.MovieDataDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieDataService {

    @Autowired
    private MovieDataDBService movieDataDBService;

    @Autowired
    private MovieDataAPIService movieDataAPIService;

    @Autowired
    MovieDataDto movieDataDto;

    public PageResponse<MovieDataVo> findAllMovies(PageRequest pageRequest, String apiKey, String movieName, String tipoTitulo, String year) {
        //primero buscar la pelicular en la DB
        List<MovieData> movieList = movieDataDBService.findAllMovies(pageRequest, apiKey, movieName, tipoTitulo, year);
        //Si no se la encuenta, buscar en el servicio por fuera, previo almacenar los datos en DB para futuras busquedas.
        if (movieList.isEmpty()) {
            movieList = movieDataAPIService.findAllMovies(pageRequest, apiKey, movieName, tipoTitulo, year);
            if (!movieList.isEmpty()) {
                movieDataDBService.saveMovieList(movieList);
            }
        }
        List<MovieDataVo> movieDataVos = movieDataDto.convertMovieDataToVO(movieList);
        return PageResponse.fromList(movieDataVos, pageRequest.getPageNumber(), pageRequest.getPageSize(), Long.valueOf(movieDataVos.size()));
    }

    public MovieData findMovie(String apiKey, String imdbId) {
        MovieData movieData = movieDataDBService.findMovie(imdbId);
        Boolean searchOnAPI = true;
        if (movieData != null) {
            if (!(movieData.getRated() == null && movieData.getGenre() == null && movieData.getRuntime() == null && movieData.getActors() == null)) {
                searchOnAPI = false;
            }
        }
        if (searchOnAPI) {
            movieData = movieDataAPIService.findMovie(apiKey, imdbId);
            if (movieData != null) {
                //antes de guardar verificar que no existia.
                MovieData oldMovieData = movieDataDBService.findMovie(movieData.getImdbID());
                if (oldMovieData != null) { //ya existia
                    movieData.setMovieId(oldMovieData.getMovieId());
                }
                    movieDataDBService.saveMovie(movieData);
            }
        }
        return movieData;
    }

}
