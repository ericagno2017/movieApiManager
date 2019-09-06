package ar.com.enrique.apimanager.model;

import lombok.Data;

import java.util.List;

@Data
public class ResponseOmdbApi {

    private Integer totalResults;
    private Integer page;
    private List<MovieData> movieDataList;

}
