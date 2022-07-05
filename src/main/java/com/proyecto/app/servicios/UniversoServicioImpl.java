package com.proyecto.app.servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.proyecto.app.entidades.Universo;
import com.proyecto.app.repositorios.IUniversoRepositorio;

/**
 * 
 * Clase que implementa la interface con el CRUD al repositorio Universo
 * 
 */
@Service
public class UniversoServicioImpl implements IUniversoServicio {

	@Autowired
	IUniversoRepositorio universoRepositorio;

	@Override
	public List<Universo> listarTodosLosUniversos() {
		return (List<Universo>) universoRepositorio.findAll();
	}

	@Override
	public Optional<Universo> buscarUniversoPorId(Integer id) {
		return universoRepositorio.findById(id);
	}

	@Override
	public Optional<Universo> buscarUniversoNombre(String nombre) {
		return universoRepositorio.findByNombre(nombre);
	}

	@Override
	public Universo crearUniverso(Universo universo) {
		Optional<Universo> universoExistente = universoRepositorio.findByNombre(universo.getNombre());
		if (universoExistente.isPresent()) {
			throw new DuplicateKeyException("Ya existe Universo con nombre: " + universoExistente.get().getNombre());
		}
		return universoRepositorio.save(universo);
	}

	@Override
	public Universo actualizarUniverso(Universo universo) {
		return universoRepositorio.save(universo);
	}

	@Override
	public void borrarUniverso(Universo universo) {
		universoRepositorio.delete(universo);
	}
}
