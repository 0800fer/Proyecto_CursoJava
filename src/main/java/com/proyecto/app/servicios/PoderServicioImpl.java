package com.proyecto.app.servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.app.entidades.Poder;
import com.proyecto.app.repositorios.IPoderRepositorio;

/**
 * 
 * Clase que implementa la interface con el CRUD al repositorio PODER
 * 
 */
@Service
public class PoderServicioImpl implements IPoderServicio {

	@Autowired
	IPoderRepositorio poderRepositorio;

	@Override
	public List<Poder> listarTodosLosPoderes() {
		return (List<Poder>) poderRepositorio.findAll();
	}

	@Override
	public Optional<Poder> buscarPoderPorId(Integer id) {
		return poderRepositorio.findById(id);
	}

	@Override
	public Optional<Poder> buscarPoderNombre(String nombre) {
		return poderRepositorio.findByNombre(nombre);
	}

	@Override
	public Poder crearPoder(Poder poder) {
		Optional<Poder> poderExistente = this.buscarPoderNombre(poder.getNombre());
		if (poderExistente.isPresent()) {
			return poderExistente.get();
//			throw new Exception("Ya existe Poder:" + poderExistente.get().getNombre());
		}
		return poderRepositorio.save(poder);
	}

	@Override
	public Poder actualizarPoder(Poder poder) {
		return poderRepositorio.save(poder);
	}

	@Override
	public void borrarPoder(Poder poder) {
		poderRepositorio.delete(poder);

	}

}
