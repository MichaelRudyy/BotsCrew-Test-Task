package com.botscrew.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

/**
 * Author Entity Class
 * Contains data about author(s) and books that was written by them
 *
 * @author Michael Rudyy
 * @version 1.5
 */

@Entity
@Table(name = "authors", schema = "schema_1")
public class Author {
    private int id;
    private String name;
    private List<Book> books;

    public Author(String name) {
        this.name = name;
    }

    public Author() {
    }

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
    @Column(name = "Name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "authors")
    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Author that = (Author) o;

        if (id != that.id) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return " Id: " + id + "  Name: " + name;
    }
}
