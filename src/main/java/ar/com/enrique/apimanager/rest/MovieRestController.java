package ar.com.enrique.apimanager.rest;

import ar.com.enrique.apimanager.common.PageRequest;
import ar.com.enrique.apimanager.common.PageResponse;
import ar.com.enrique.apimanager.model.MovieData;
import ar.com.enrique.apimanager.model.MovieDataVo;
import ar.com.enrique.apimanager.service.MovieDataService;
import ar.com.enrique.apimanager.utils.AppConstants;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@RestController
@RequestMapping("/api")
@Produces(MediaType.APPLICATION_JSON)
public class MovieRestController {

    @Autowired
    private MovieDataService movieDataService;

    @GetMapping("/movies")
    @ApiOperation(value = "Devuelve la lista de Películas/Series/Episodios", notes = "Lista de Películas/Series/Episodios", response = MovieData.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Error interno del sistema => Ver code y message lanzados"),
            @ApiResponse(code = 401, message = "Usuario no autenticado"),
            @ApiResponse(code = 403, message = "Usuario no autorizado")})
    public ResponseEntity<?> findAll(@Context HttpServletRequest request,
                                     @ApiParam(value = "ApiKey") @RequestParam(value = "apikey") String apiKey,
                                     @ApiParam(value = "nombre de la pelicula") @RequestParam(value = "name") String name,
                                     @ApiParam(value = "Tipo de Titulo - movie, series, episode") @RequestParam(value = "tipoTitulo", required = false) String tipoTitulo,
                                     @ApiParam(value = "Año de estreno") @RequestParam(value = "year", required = false) String year,
                                     @ApiParam(value = "Nro de pagina a devolver") @RequestParam(value = "page-number", required = false, defaultValue = "0") String pageNumber,
                                     @ApiParam(value = "Cantidad de resultados a devolver") @RequestParam(value = "page-size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) String pageSize) {

        PageResponse<MovieDataVo> result = movieDataService.findAllMovies(new PageRequest(Integer.valueOf(pageNumber), Integer.valueOf(pageSize))
                                        ,apiKey,  name, tipoTitulo, year);
        if (result==null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(result, getHeadersOk(), HttpStatus.OK);
    }

    @GetMapping("/movies/{imdbId}")
    @ApiOperation(value = "Devuelve la lista de Películas/Series/Episodios", notes = "Lista de Películas/Series/Episodios", response = MovieData.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Error interno del sistema => Ver code y message lanzados"),
            @ApiResponse(code = 401, message = "Usuario no autenticado"),
            @ApiResponse(code = 403, message = "Usuario no autorizado")})
    public ResponseEntity<?> findMovie(@Context HttpServletRequest request,
                                     @ApiParam(value = "ApiKey") @RequestParam(value = "apikey") String apiKey,
                                     @ApiParam(value = "imdbId Id de Imdb") @PathVariable("imdbId") String imdbId) {

        if (apiKey != null && imdbId != null) {
            MovieData result = movieDataService.findMovie(apiKey, imdbId);
            if (result == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>(result, getHeadersOk(), HttpStatus.OK);
        }else
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/ratedMovie")
    @ApiOperation(value = "Devuelve la lista las mejores peliculas", notes = "Lista de las top movies", response = MovieData.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Error interno del sistema => Ver code y message lanzados"),
            @ApiResponse(code = 401, message = "Usuario no autenticado"),
            @ApiResponse(code = 403, message = "Usuario no autorizado")})
    public ResponseEntity<?> findMovieRate(@Context HttpServletRequest request,
                                       @ApiParam(value = "ApiKey") @RequestParam(value = "apikey") String apiKey,
                                           @ApiParam(value = "Year") @RequestParam(value = "year") String year) {
        //TODO NUEVO REQUERIMIENTO

        //        if(year == null){
//            year = String.valueOf(new Date().getYear());
//        }
////        PageResponse<MovieDataVo> result = movieDataService.
        return null;
    }


        private HttpHeaders getHeadersOk() {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("status", "ok");
        responseHeaders.set("message", "succesfully");
        return responseHeaders;
    }
}
