package ar.com.enrique.apimanager.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MovieDataVo {

    @JsonProperty("Title")
    private String title;
    @JsonProperty("Year")
    private String year;
    @JsonProperty("Poster")
    private String poster;
    @JsonProperty("imdbID")
    private String imdbID;
    @JsonProperty("Type")
    private String type;

}
