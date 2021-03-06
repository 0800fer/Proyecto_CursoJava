package com.proyecto.app.repositorios;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.app.entidades.Superheroe;

/**
 * 
 * Interface que implementa las funcionalidades CRUD para el repositorio de la
 * clase Superheroe
 * 
 */
@Repository
public interface ISuperheroeRepositorio extends JpaRepository<Superheroe, Integer> {
	List<Superheroe> findByNombreContainingIgnoreCase(String name);

	Optional<Superheroe> findByNombre(String nombre);
}
