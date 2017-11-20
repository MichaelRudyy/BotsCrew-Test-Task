package com.botscrew.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

/**
 * Book Entity Class
 * Contains data about book(s) and author that write them
 *
 * @author Michael Rudyy
 * @version 1.2
 */

@Entity
@Table(name = "books", schema = "schema_1")
public class Book {
    private int id;
    private String title;
    private Integer year;
    private String genre;
    private List<Author> authors;

    @Id
    @Column(name = "id")
    @GenericGenerator(name = "generator", strategy = "increment")
    @GeneratedValue(generator = "generator")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "year")
    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    @Basic
    @Column(name = "genre")
    public String getGenre() {
        return genre;
    }


    public void setGenre(String genre) {
        this.genre = genre;
    }

    @ManyToMany
    @JoinTable(name = "book_author",
            joinColumns = {@JoinColumn(name = "idBook")},
            inverseJoinColumns = {@JoinColumn(name = "idAuthor")})
    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book that = (Book) o;

        if (id != that.id) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (year != null ? !year.equals(that.year) : that.year != null) return false;
        if (genre != null ? !genre.equals(that.genre) : that.genre != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (year != null ? year.hashCode() : 0);
        result = 31 * result + (genre != null ? genre.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        String string = " Id: " + id +
                "  Title: " + title +
                "  Authors: ";
        for (Author author : getAuthors()) {
            string += author.getName() + " ; ";
        }
        string += "  Year: " + year +
                "  Genre: " + genre;

        return string;
    }
}
