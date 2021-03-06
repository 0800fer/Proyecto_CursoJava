package com.proyecto.app.controladores;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.app.entidades.Poder;
import com.proyecto.app.entidades.Superheroe;
import com.proyecto.app.entidades.SuperheroeDTO;
import com.proyecto.app.entidades.Universo;
import com.proyecto.app.servicios.SuperheroeServicioImpl;
import com.proyecto.app.servicios.UniversoServicioImpl;

/**
 * 
 * Desarrollo de endpoint /api/superheroes
 *
 */
@RestController
@RequestMapping("api/superheroes")
public class SuperheroeControlador {

	private static final Logger logger = LoggerFactory.getLogger(SuperheroeControlador.class);

	private static final String MENSJENOID = "No existe superheroe con id: ";

	@Autowired
	private SuperheroeServicioImpl servicio;

	@Autowired
	private UniversoServicioImpl universoServicio;

	@Autowired
	private ModelMapper modelMapper;

	/**
	 * 
	 * Endpoint GET /api/superheroes
	 * 
	 * @return Lista de superheroes
	 * 
	 */
	@GetMapping()
	public List<SuperheroeDTO> listarSuperheroes() {
		logger.info("Obteniendo todos los superheroees");
		return servicio.listarTodosLosSuperheroes().stream()
				.map(superheroe -> servicio.superHeroeMapperToDto(superheroe)).collect(Collectors.toList());
	}

	/**
	 * 
	 * Endpoint GET /api/superheroes?nameLike="abc"
	 * 
	 * @return Lista de superheroes
	 * 
	 */
	@GetMapping("/buscar")
	public List<SuperheroeDTO> listarSuperheroesContienenEnNombre(@RequestParam String nombre) {
		logger.info("Obteniendo superheroees que contienen ...");
		return servicio.listarSuperheroesContienenEnNombre(nombre).stream()
				.map(superheroe -> servicio.superHeroeMapperToDto(superheroe)).collect(Collectors.toList());
	}

	/**
	 * 
	 * Endpoint POST /api/superheroees Crea un Superheroe
	 * 
	 * @return Superheroe creado
	 * 
	 */
	@PostMapping()
	public ResponseEntity<SuperheroeDTO> crearSuperheroe(@RequestBody SuperheroeDTO superheroeDTO) {
		logger.info("Creando superheroe con data {}", superheroeDTO);
		Optional<Superheroe> superheroeYaExiste = servicio.buscarSuperheroePorNombre(superheroeDTO.getNombre());
		if (superheroeYaExiste.isPresent()) {
			throw new DuplicateKeyException("Ya existe superheroe con nombre: " + superheroeDTO.getNombre());
		}

		// Valida Universo
		Optional<Universo> universoExiste = universoServicio.buscarUniversoPorId(superheroeDTO.getUniversoId());
		if (universoExiste.isEmpty()) {
			throw new IllegalArgumentException("No existe Universo con id: " + superheroeDTO.getUniversoId());
		}

		// Valida Poderes
		var listaPoderes = superheroeDTO.getPoderes();
		var listaIdPoderes = listaPoderes.stream().map(Integer::parseInt).collect(Collectors.toList());
		if (Boolean.FALSE.equals(servicio.validaPoderes(listaIdPoderes))) {
			throw new IllegalArgumentException("Poderes no v??lidos");
		}

		// DTO a entidad
		Superheroe superheroe = modelMapper.map(superheroeDTO, Superheroe.class);

		// entidad a DTO
		SuperheroeDTO superheroeResponse = servicio
				.superHeroeMapperToDto(servicio.crearSuperheroe(superheroe, listaIdPoderes));

		return new ResponseEntity<>(superheroeResponse, HttpStatus.CREATED);
	}

	/**
	 * 
	 * Endpoint GET /api/superheroees/{id} Busca un Superheroe
	 * 
	 * @return Superheroe encontrado
	 * @exception not Found
	 * 
	 */
	@GetMapping("/{id}")
	public ResponseEntity<SuperheroeDTO> buscarSuperheroePorId(@PathVariable(name = "id") Integer idSuperheroe) {
		logger.info("Obteniendo superheroe con id {}", idSuperheroe);

		// Validacion
		Optional<Superheroe> superheroe = servicio.buscarSuperheroePorId(idSuperheroe);
		if (superheroe.isEmpty()) {
			throw new NoSuchElementException(MENSJENOID + idSuperheroe);
		}
		// entidad a DTO
		SuperheroeDTO superheroeDTO = servicio.superHeroeMapperToDto(superheroe.get());

		return ResponseEntity.ok().body(superheroeDTO);
	}

	/**
	 * 
	 * Endpoint PUT /api/superheroees/{id} Actualiza un Superheroe
	 * 
	 * @return Superheroe actualizado
	 * @exception not Found
	 * 
	 */
	@PutMapping("{id}")
	public ResponseEntity<SuperheroeDTO> actualizarSuperheroe(@PathVariable(value = "id") Integer idSuperheroe,
			@RequestBody SuperheroeDTO superheroeDTO) {

		logger.info("Actualizando superheroe con id {} y data {}", idSuperheroe, superheroeDTO);

		var superheroe = modelMapper.map(superheroeDTO, Superheroe.class);

		return servicio.buscarSuperheroePorId(idSuperheroe).map(superheroeGuardado -> {

			superheroeGuardado.setNombre(superheroe.getNombre());
			superheroeGuardado.setHistoria(superheroe.getHistoria());
			superheroeGuardado.setUniversoId(superheroe.getUniversoId());

			Superheroe superheroeActualizado = servicio.actualizarSuperheroe(superheroeGuardado);

			return new ResponseEntity<>(servicio.superHeroeMapperToDto(superheroeActualizado), HttpStatus.OK);

		}).orElseGet(() -> ResponseEntity.notFound().build());
	}

	/**
	 * 
	 * Endpoint DELETE /api/superheroees/{id} Elimina un Superheroe
	 * 
	 * @return 204
	 * @exception not Found
	 * 
	 */
	@DeleteMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void eliminarSuperheroe(@PathVariable(value = "id") Integer idSuperheroe) {

		logger.info("Eliminando superheroe con id {}", idSuperheroe);

		// Validacion
		Optional<Superheroe> superheroe = servicio.buscarSuperheroePorId(idSuperheroe);
		if (superheroe.isEmpty()) {
			throw new NoSuchElementException(MENSJENOID + idSuperheroe);
		}
		servicio.borrarSuperheroe(superheroe.get());
	}

	/**
	 * 
	 * Endpoint POST /api/superheroees/{id}/matar "Mata" un Superheroe
	 * 
	 * @return 200
	 * @exception not Found
	 * 
	 */
	@PostMapping(value = "/{id}/matar")
	@ResponseStatus(code = HttpStatus.OK)
	public ResponseEntity<String> matarSuperheroe(@PathVariable(value = "id") Integer idSuperheroe) {

		logger.info("Matando al superheroe con id {}", idSuperheroe);

		// Validacion
		Optional<Superheroe> superheroe = servicio.buscarSuperheroePorId(idSuperheroe);
		if (superheroe.isEmpty()) {
			throw new NoSuchElementException(MENSJENOID + idSuperheroe);
		}
		servicio.matarSuperheroe(superheroe.get());
		return ResponseEntity.ok().body("Superheroe muerto satisfactoriamente!");
	}

	/**
	 * 
	 * Endpoint POST /api/superheroees/{id}/revivir "Resucita" un Superheroe
	 * 
	 * @return 200
	 * @exception not Found
	 * 
	 */
	@PostMapping(value = "/{id}/resucitar")
	@ResponseStatus(code = HttpStatus.OK)
	public ResponseEntity<String> resucitarSuperheroe(@PathVariable(value = "id") Integer idSuperheroe) {

		logger.info("Resucitando al superheroe con id {}", idSuperheroe);

		// Validacion
		Optional<Superheroe> superheroe = servicio.buscarSuperheroePorId(idSuperheroe);
		if (superheroe.isEmpty()) {
			throw new NoSuchElementException(MENSJENOID + idSuperheroe);
		}
		servicio.resucitarSuperheroe(superheroe.get());
		return ResponseEntity.ok().body("Superheroe resucitado satisfactoriamente!");
	}

	/**
	 * 
	 * Endpoint GET /api/superheroees/{id}/listar-poderes Lista los poderes de un
	 * Superheroe
	 * 
	 * @return 200
	 * @exception not Found
	 * 
	 */
	@GetMapping(value = "/{id}/listar-poderes")
	@ResponseStatus(code = HttpStatus.OK)
	public Set<Poder> listarPoderesDeSuperheroe(@PathVariable(value = "id") Integer idSuperheroe) {

		logger.info("Listando poderes de Superheroe con id {}", idSuperheroe);

		// Validacion
		Optional<Superheroe> superheroe = servicio.buscarSuperheroePorId(idSuperheroe);
		if (superheroe.isEmpty()) {
			throw new NoSuchElementException(MENSJENOID + idSuperheroe);
		}

		return superheroe.get().getPoderes();
	}

	/**
	 * 
	 * Endpoint GET /api/superheroees/{id}/agregar-poderes Agrega poderes a un
	 * Superheroe
	 * 
	 * @return 200
	 * @exception not Found
	 * 
	 */
	@PostMapping(value = "/{id}/agregar-poderes")
	@ResponseStatus(code = HttpStatus.OK)
	public ResponseEntity<String> agregarPoderesASuperheroe(@PathVariable(value = "id") Integer idSuperheroe,
			@RequestBody List<Integer> listaIdPoderes) {

		logger.info("Agregando poderes a Superheroe con id {}", idSuperheroe);

		// Validacion
		Optional<Superheroe> superheroe = servicio.buscarSuperheroePorId(idSuperheroe);
		if (superheroe.isEmpty()) {
			throw new NoSuchElementException(MENSJENOID + idSuperheroe);
		}

		if (Boolean.FALSE.equals(servicio.validaPoderes(listaIdPoderes))) {
			throw new IllegalArgumentException("Poderes no v??lidos");
		}

		servicio.agregaPoderes(superheroe.get(), listaIdPoderes);

		return ResponseEntity.ok().body("Poderes agregados satisfactoriamente!");
	}

	/**
	 * 
	 * Endpoint GET /api/superheroees/{id}/poderes Elimina poderes a un Superheroe
	 * 
	 * @return 200
	 * @exception not Found
	 * 
	 */
	@PostMapping(value = "/{id}/eliminar-poderes")
	@ResponseStatus(code = HttpStatus.OK)
	public ResponseEntity<String> eliminarPoderesASuperheroe(@PathVariable(value = "id") Integer idSuperheroe,
			@RequestBody List<Integer> listaIdPoderes) {

		logger.info("Eliminando poderes a Superheroe con id {}", idSuperheroe);

		// Validacion
		Optional<Superheroe> superheroe = servicio.buscarSuperheroePorId(idSuperheroe);
		if (superheroe.isEmpty()) {
			throw new NoSuchElementException(MENSJENOID + idSuperheroe);
		}

		if (Boolean.FALSE.equals(servicio.validaPoderes(listaIdPoderes))) {
			throw new IllegalArgumentException("Lista de Poderes no v??lidos");
		}

		servicio.eliminarPoderes(superheroe.get(), listaIdPoderes);

		return ResponseEntity.ok().body("Poderes eliminados satisfactoriamente!");
	}

}