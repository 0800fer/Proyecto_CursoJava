package com.proyecto.app.servicios;

import java.util.List;
import java.util.Optional;

import com.proyecto.app.entidades.Poder;

/**
 * 
 * Interface que define el CRUD a ser implementado en la entidad PODER
 * 
 */
public interface IPoderServicio {

	List<Poder> listarTodosLosPoderes();

	Optional<Poder> buscarPoderPorId(Integer id);

	Optional<Poder> buscarPoderNombre(String nombre);

	Poder crearPoder(Poder poder);

	Poder actualizarPoder(Poder poder);

	void borrarPoder(Integer id);
}
