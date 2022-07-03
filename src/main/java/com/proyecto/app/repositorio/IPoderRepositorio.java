package com.proyecto.app.repositorio;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.app.entidad.Poder;

/**
 * 
 * Interface que implementa las funcionalidades CRUD para el repositorio de la
 * clase PODER
 * 
 */
@Repository
public interface IPoderRepositorio extends CrudRepository<Poder, Integer> {

}
