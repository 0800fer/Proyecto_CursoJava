package com.proyecto.app.servicios;

import java.util.List;
import java.util.Optional;

import com.proyecto.app.entidades.Universo;

/**
 * 
 * Interface que define el CRUD a ser implementado en la entidad Universo
 * 
 */
public interface IUniversoServicio {

	List<Universo> listarTodosLosUniversos();

	Optional<Universo> buscarUniversoPorId(Integer id);

	Optional<Universo> buscarUniversoNombre(String nombre);

	Universo crearUniverso(Universo universo);

	Universo actualizarUniverso(Universo universo);

	void borrarUniverso(Universo universo);

}
