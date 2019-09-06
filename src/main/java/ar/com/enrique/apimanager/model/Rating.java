package ar.com.enrique.apimanager.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "Rating")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rating {

    @Id
    @Column(name = "RATING_ID", nullable=false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long ratingID;

    @JsonProperty("Source")
    private String source;
    @JsonProperty("Value")
    private String value;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "moviedata_id", nullable = false)
    @JsonIgnore
    private MovieData moviedata;
}
