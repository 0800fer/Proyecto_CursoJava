package com.proyecto.app.servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.proyecto.app.entidades.Superheroe;
import com.proyecto.app.repositorios.ISuperheroeRepositorio;

/**
 * 
 * Clase que implementa la interface con el CRUD al repositorio Superheroe
 * 
 */
@Service
public class SuperheroeServicioImpl implements ISuperheroeServicio {

	@Autowired
	ISuperheroeRepositorio superheroeRepositorio;

	@Override
	public List<Superheroe> listarTodosLosSuperheroes() {
		return superheroeRepositorio.findAll();
	}

	@Override
	public List<Superheroe> listarSuperheroesContienenEnNombre(String nombre) {
		return superheroeRepositorio.findByNombreContainingIgnoreCase(nombre);
	}

	@Override
	public Optional<Superheroe> buscarSuperheroePorId(Integer id) {
		return superheroeRepositorio.findById(id);
	}

	@Override
	public Optional<Superheroe> buscarSuperheroePorNombre(String nombre) {
		return superheroeRepositorio.findByNombre(nombre);
	}

	@Override
	public Superheroe crearSuperheroe(Superheroe superheroe) {
		Optional<Superheroe> superHeroeExistente = superheroeRepositorio.findByNombre(superheroe.getNombre());
		if (superHeroeExistente.isPresent()) {
			throw new DuplicateKeyException("Ya existe Universo con nombre: " + superHeroeExistente.get().getNombre());
		}
		return superheroeRepositorio.save(superheroe);
	}

	@Override
	public Superheroe actualizarSuperheroe(Superheroe superheroe) {
		return superheroeRepositorio.save(superheroe);
	}

	@Override
	public void borrarSuperheroe(Superheroe superheroe) {
		superheroeRepositorio.delete(superheroe);
	}

}
