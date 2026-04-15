package com.biblioteca.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "libros")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Min(0)
    @Column(name = "cantidad_ejemplares", nullable = false)
    private Integer cantidadEjemplares;

    @NotBlank
    @Column(name = "nombre_libro", nullable = false)
    private String nombreLibro;

    @NotBlank
    @Column(nullable = false)
    private String autor;

    @NotBlank
    @Column(nullable = false)
    private String genero;

    @NotNull
    @Column(name = "anio_publicacion", nullable = false)
    private Integer anioPublicacion;

    public Libro() {
    }

    public Libro(Long id, Integer cantidadEjemplares, String nombreLibro, String autor, String genero, Integer anioPublicacion) {
        this.id = id;
        this.cantidadEjemplares = cantidadEjemplares;
        this.nombreLibro = nombreLibro;
        this.autor = autor;
        this.genero = genero;
        this.anioPublicacion = anioPublicacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCantidadEjemplares() {
        return cantidadEjemplares;
    }

    public void setCantidadEjemplares(Integer cantidadEjemplares) {
        this.cantidadEjemplares = cantidadEjemplares;
    }

    public String getNombreLibro() {
        return nombreLibro;
    }

    public void setNombreLibro(String nombreLibro) {
        this.nombreLibro = nombreLibro;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public Integer getAnioPublicacion() {
        return anioPublicacion;
    }

    public void setAnioPublicacion(Integer anioPublicacion) {
        this.anioPublicacion = anioPublicacion;
    }
}
