package com.proyecto.app.servicios;

import java.util.List;
import java.util.Optional;

import com.proyecto.app.entidades.Superheroe;

/**
 * 
 * Interface que define el CRUD a ser implementado en la entidad Superheroe
 * 
 */
public interface ISuperheroeServicio {

	List<Superheroe> listarTodosLosSuperheroes();

	List<Superheroe> listarSuperheroesContienenEnNombre(String nombre);

	Optional<Superheroe> buscarSuperheroePorId(Integer id);

	Optional<Superheroe> buscarSuperheroePorNombre(String nombre);

	Superheroe crearSuperheroe(Superheroe poder);

	Superheroe actualizarSuperheroe(Superheroe poder);

	void borrarSuperheroe(Superheroe poder);
}