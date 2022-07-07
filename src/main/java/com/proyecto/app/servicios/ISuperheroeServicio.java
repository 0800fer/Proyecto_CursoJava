package com.proyecto.app.servicios;

import java.util.List;
import java.util.Optional;

import com.proyecto.app.entidades.Superheroe;
import com.proyecto.app.entidades.SuperheroeDTO;

/**
 * 
 * Interface que define los servicios a ser implementados en la entidad
 * Superheroe
 * 
 */
public interface ISuperheroeServicio {

	List<Superheroe> listarTodosLosSuperheroes();

	List<Superheroe> listarSuperheroesContienenEnNombre(String nombre);

	Optional<Superheroe> buscarSuperheroePorId(Integer id);

	Optional<Superheroe> buscarSuperheroePorNombre(String nombre);

	Superheroe crearSuperheroe(Superheroe superheroe);

	Superheroe actualizarSuperheroe(Superheroe superheroe);

	void borrarSuperheroe(Superheroe superheroe);

	void matarSuperheroe(Superheroe superheroe);

	void resucitarSuperheroe(Superheroe superheroe);

	SuperheroeDTO superHeroeMapperToDto(Superheroe superheroe);

	Boolean validaPoderes(List<String> lista);
}