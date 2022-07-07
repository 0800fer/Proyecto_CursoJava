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
	public Superheroe crearSuperheroe(Superheroe superheroe) {
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
	public Boolean validaPoderes(List<String> lista) {
		int cantidadPoderesValidos = 0;
		Boolean listaValida = true;
		for (String id : lista) {
			Optional<Poder> poderExiste = poderServicio.buscarPoderPorId(Integer.valueOf(id));
			if (poderExiste.isPresent())
				cantidadPoderesValidos++;
			else
				listaValida = false;
		}

		return (listaValida) && (cantidadPoderesValidos > 0);
	}

}
