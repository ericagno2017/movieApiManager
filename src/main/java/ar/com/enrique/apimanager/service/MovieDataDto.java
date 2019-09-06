package ar.com.enrique.apimanager.service;

import ar.com.enrique.apimanager.model.MovieData;
import ar.com.enrique.apimanager.model.MovieDataVo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MovieDataDto {

    public List<MovieDataVo> convertMovieDataToVO(List<MovieData> movieDataList){
        List<MovieDataVo> movieDataVoList = new ArrayList<>();
        for (MovieData movieData : movieDataList){
            MovieDataVo movieDataVo = new MovieDataVo();
            movieDataVo.setImdbID(movieData.getImdbID());
            movieDataVo.setPoster(movieData.getPoster());
            movieDataVo.setTitle(movieData.getTitle());
            movieDataVo.setType(movieData.getType());
            movieDataVo.setYear(movieData.getYear());
            movieDataVoList.add(movieDataVo);
        }
        return movieDataVoList;
    }
}
