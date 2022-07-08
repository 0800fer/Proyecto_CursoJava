package com.proyecto.app.servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.app.entidades.Poder;
import com.proyecto.app.entidades.Superheroe;
import com.proyecto.app.entidades.SuperheroeDTO;
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

	@Autowired
	IPoderServicio poderServicio;

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
	public Superheroe crearSuperheroe(Superheroe superheroe, List<Integer> listaPoderes) {

		// Crea Superheroe
		var superHeroeNuevo = Superheroe.builder().id(superheroe.getId()).nombre(superheroe.getNombre())
				.historia(superheroe.getHistoria()).universoId(superheroe.getUniversoId()).build();
		var superheroeGuardado = superheroeRepositorio.save(superHeroeNuevo);

		// Agrega Poderes
		for (Integer id : listaPoderes) {
			var poder = poderServicio.buscarPoderPorId(id);
			if (poder.isPresent()) {
				superheroeGuardado.getPoderes().add(poder.get());
			}
		}
		return superheroeRepositorio.save(superheroeGuardado);
	}

	@Override
	public Superheroe actualizarSuperheroe(Superheroe superheroe) {
		return superheroeRepositorio.save(superheroe);
	}

	@Override
	public void borrarSuperheroe(Superheroe superheroe) {
		superheroeRepositorio.delete(superheroe);
	}

	@Override
	public void matarSuperheroe(Superheroe superheroe) {
		if (Boolean.FALSE.equals(superheroe.getEstaVivo())) {
			throw new IllegalArgumentException("El superheroe ya se encuentra muerto");
		}
		superheroe.setEstaVivo(false);
		superheroeRepositorio.save(superheroe);
	}

	@Override
	public void resucitarSuperheroe(Superheroe superheroe) {
		if (Boolean.TRUE.equals(superheroe.getEstaVivo())) {
			throw new IllegalArgumentException("El superheroe ya se encuentra vivo");
		}
		superheroe.setEstaVivo(true);
		superheroeRepositorio.save(superheroe);
	}

	@Override
	public SuperheroeDTO superHeroeMapperToDto(Superheroe superheroe) {

		List<String> listaPoderes = new ArrayList<>();

		for (Poder poder : superheroe.getPoderes()) {
			listaPoderes.add(poder.getNombre());
		}
		return SuperheroeDTO.builder().id(superheroe.getId()).nombre(superheroe.getNombre())
				.historia(superheroe.getHistoria()).universoId(superheroe.getUniversoId()).poderes(listaPoderes)
				.build();
	}

	@Override
	public Boolean validaPoderes(List<Integer> lista) {
		int cantidadPoderesValidos = 0;
		Boolean listaValida = true;
		for (Integer id : lista) {
			Optional<Poder> poderExiste = poderServicio.buscarPoderPorId(id);
			if (poderExiste.isPresent())
				cantidadPoderesValidos++;
			else
				listaValida = false;
		}
		return (listaValida) && (cantidadPoderesValidos > 0);
	}

	public void agregaPoderes(Superheroe superheroe, List<Integer> listaIdPoderes) {
		// Agrega Poderes
		for (Integer id : listaIdPoderes) {
			var poder = poderServicio.buscarPoderPorId(id);
			if (poder.isPresent()) {
				superheroe.getPoderes().add(poder.get());
			}
		}
		superheroeRepositorio.save(superheroe);
	}

	public void eliminarPoderes(Superheroe superheroe, List<Integer> listaIdPoderes) {
		// Elimina Poderes
		for (Integer id : listaIdPoderes) {
			var poder = poderServicio.buscarPoderPorId(id);
			if (poder.isPresent()) {
				superheroe.getPoderes().remove(poder.get());
			}
		}
		if (superheroe.getPoderes().isEmpty()) {
			throw new IllegalArgumentException("El superheroe no puede quedarse sin poderes");
		}
		superheroeRepositorio.save(superheroe);
	}
}
