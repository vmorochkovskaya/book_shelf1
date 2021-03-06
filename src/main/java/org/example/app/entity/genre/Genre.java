package org.example.app.entity.genre;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.example.app.entity.book.Book;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "genre")
@ApiModel(description = "entity representing a genre")
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("id generated by db automatically")
    private int id;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    @ApiModelProperty("genre name")
    private String name;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
//  мнемонический код жанра, используемый в ссылках на страницу данного жанра
    private String slug;

    @ManyToOne
    @JoinColumn(name = "parent_id",
            columnDefinition = "INT DEFAULT NULL")
    @JsonIgnore
    private Genre parentGenre;

    @OneToMany(mappedBy = "parentGenre")
    @JsonIgnore
    private List<Genre> subGenreList;

    @ManyToMany(mappedBy = "genres")
    @JsonIgnore
    private Set<Book> books;
}
