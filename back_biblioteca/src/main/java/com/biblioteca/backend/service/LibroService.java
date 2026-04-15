package com.biblioteca.backend.service;

import com.biblioteca.backend.model.Libro;
import com.biblioteca.backend.repository.LibroRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibroService {

    private final LibroRepository libroRepository;

    public LibroService(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    public List<Libro> obtenerTodos() {
        return libroRepository.findAll();
    }

    public Libro guardar(Libro libro) {
        return libroRepository.save(libro);
    }

    public void eliminarPorId(Long id) {
        libroRepository.deleteById(id);
    }
}
