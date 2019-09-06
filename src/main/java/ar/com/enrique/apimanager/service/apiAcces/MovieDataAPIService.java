package ar.com.enrique.apimanager.service.apiAcces;

import ar.com.enrique.apimanager.common.PageRequest;
import ar.com.enrique.apimanager.model.MovieData;
import ar.com.enrique.apimanager.model.ResponseOmdbApi;
import ar.com.enrique.apimanager.utils.AppConstants;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MovieDataAPIService {

    @Autowired
    RestTemplate restTemplate;

    @Value(AppConstants.uri)
    String serviceURL;

    public List<MovieData> findAllMovies(PageRequest pageRequest, String apiKey, String movieName, String tipoTitulo, String year) {
        HttpEntity<String>  response = null;
        Integer page = 1;
        if (apiKey != null) {

            try {
                List<MovieData> movieList = new ArrayList<>();
                response = makeRequest(apiKey, tipoTitulo, movieName, year, null,null);

                ResponseOmdbApi responseOmdbApi =  getMoviesData(response);
                movieList = responseOmdbApi.getMovieDataList();
                if(responseOmdbApi.getTotalResults() != 0){
                    //hay mas respuestas seria bueno acumular todas y despues devolver
                    for (int contador = 10; contador < responseOmdbApi.getTotalResults(); contador += 10){
                        page++;
                        response = makeRequest(apiKey, tipoTitulo, movieName, year, null,String.valueOf(page));
                        ResponseOmdbApi responseOmdbApiParcial =  getMoviesData(response);
                        List<MovieData> newTemporalList = Stream.concat(movieList.stream(), responseOmdbApiParcial.getMovieDataList().stream())
                                .collect(Collectors.toList());
                        movieList = newTemporalList;
                    }
                }

                return  movieList;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public MovieData findMovie(String apiKey,String imdbID){
        HttpEntity<String>  response = null;
        if (apiKey != null) {
            try {
                response = makeRequest(apiKey, null, null, null, imdbID, null);
                MovieData movie= getMovie(response);
                if(movie!=null) {
                    return movie;
                }else return  null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    private HttpEntity<String>   makeRequest(String apiKey, String type, String title, String year, String imdbId, String page) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(serviceURL)
                .queryParam("apikey", apiKey)
                .queryParam("plot", "full")
                .queryParam("type", type)
                .queryParam("s", title)
                .queryParam("y",year)
                .queryParam("page", page)
                .queryParam("i", imdbId);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        HttpEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                String.class);
        return response;
    }

    private ResponseOmdbApi getMoviesData(HttpEntity<String> response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(response.getBody());
        ResponseOmdbApi responseOmdbApi = new ResponseOmdbApi();
        List<MovieData> movieList = new ArrayList<>();
        if (rootNode instanceof ArrayNode) {
            // Read the json as a list:
//            rootNode.get("Search") aca esta el array
            MovieData[] movieArray = mapper.readValue(rootNode.toString(), MovieData[].class);
            movieList = Arrays.asList(movieArray);

            responseOmdbApi.setMovieDataList(movieList);
            return responseOmdbApi;
        } else if (rootNode instanceof JsonNode) {
            //falta manejar el error
            String searchValue = rootNode.get("Search").toString(); //aca esta el array
            if (!searchValue.isEmpty()){
                MovieData[] movieArray = mapper.readValue(rootNode.get("Search").toString(), MovieData[].class);
                movieList = Arrays.asList(movieArray);
                responseOmdbApi.setMovieDataList(movieList);
                responseOmdbApi.setTotalResults(Integer.valueOf(rootNode.get("totalResults").toString().replace("\"", "")));
                return responseOmdbApi;
            }
            MovieData movie = mapper.readValue(rootNode.toString(), MovieData.class);
            movieList.add(movie);
            responseOmdbApi.setMovieDataList(movieList);
        }
        return responseOmdbApi;
    }

    private MovieData getMovie(HttpEntity<String> response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(response.getBody());
        if (rootNode instanceof JsonNode) {
            MovieData movieDatum = mapper.readValue(rootNode.toString(), MovieData.class);
            return movieDatum;
        }
        return null;
    }
}
