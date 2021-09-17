package org.example.app.entity.author;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.example.app.entity.book.Book;

import javax.persistence.*;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "author")
@ApiModel(description = "entity representing an author")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INT NOT NULL AUTO_INCREMENT")
    @ApiModelProperty(value = "author id generated by db",position = 1)
    private Integer id;

    @Column(columnDefinition = "TEXT")
//  описание (биография, характеристика)
    private String description;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    @ApiModelProperty(value = "name of author",example = "Bob Bob", position = 2)
    private String name;

    @Column(columnDefinition = "VARCHAR(255)")
//  ссылка на изображение с фотографией автора
    private String photo;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
//  мнемонический идентификатор автора, который будет отображаться в ссылке на его страницу
    private String slug;

    @ManyToMany(mappedBy = "authors")
    @JsonIgnore
    private Set<Book> books;
}
