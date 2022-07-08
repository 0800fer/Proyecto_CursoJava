package com.proyecto.app.repositorios;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.app.entidades.Universo;

/**
 * 
 * Interface que implementa las funcionalidades CRUD para el repositorio de la
 * clase Universo
 * 
 */
@Repository
public interface IUniversoRepositorio extends CrudRepository<Universo, Integer> {

	Optional<Universo> findByNombre(String nombre);
}
