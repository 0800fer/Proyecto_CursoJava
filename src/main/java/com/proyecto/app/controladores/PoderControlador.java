package com.proyecto.app.controladores;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.app.entidades.Poder;
import com.proyecto.app.entidades.PoderDTO;
import com.proyecto.app.servicios.PoderServicioImpl;

/**
 * 
 * Desarrollo de endpoint /api/poderes
 *
 */
@RestController
@RequestMapping("api/poderes")
public class PoderControlador {

	private static final Logger logger = LogManager.getLogger(PoderControlador.class);

	@Autowired
	private PoderServicioImpl servicio;

	@Autowired
	private ModelMapper modelMapper;

	/**
	 * 
	 * Endpoint GET /api/poderes
	 * 
	 * @return Lista de poderes
	 * 
	 */
	@GetMapping()
	public List<PoderDTO> listarPoderes() {
		logger.debug("Obteniendo todos los poderes");
		return servicio.listarTodosLosPoderes().stream().map(poder -> modelMapper.map(poder, PoderDTO.class))
				.collect(Collectors.toList());
	}

	/**
	 * 
	 * Endpoint POST /api/poderes Crea un Poder
	 * 
	 * @return Poder creado
	 * 
	 */
	@PostMapping
	public ResponseEntity<PoderDTO> crearPoder(@RequestBody PoderDTO poderDTO) {
		logger.debug("Creando poder con data {}", poderDTO);
		Optional<Poder> poderYaExiste = servicio.buscarPoderNombre(poderDTO.getNombre());
		if (poderYaExiste.isPresent()) {
			throw new DuplicateKeyException("Ya existe poder con nombre: " + poderDTO.getNombre());
		}
		// DTO a entidad
		Poder poder = servicio.crearPoder(modelMapper.map(poderDTO, Poder.class));
		// entidad a DTO
		PoderDTO poderResponse = modelMapper.map(poder, PoderDTO.class);
		return new ResponseEntity<>(poderResponse, HttpStatus.CREATED);
	}

	/**
	 * 
	 * Endpoint GET /api/poderes/{id} Busca un Poder
	 * 
	 * @return Poder encontrado
	 * @exception not Found
	 * 
	 */
	@GetMapping("/{id}")
	public ResponseEntity<PoderDTO> buscarPoderPorId(@PathVariable(name = "id") Integer idPoder) {
		logger.debug("Obteniendo poder con id {}", idPoder);

		// Validacion
		Optional<Poder> poder = servicio.buscarPoderPorId(idPoder);
		if (poder.isEmpty()) {
			throw new NoSuchElementException("No existe poder con id: " + idPoder);
		}
		// entidad a DTO
		PoderDTO poderDTO = modelMapper.map(poder.get(), PoderDTO.class);
		return ResponseEntity.ok().body(poderDTO);
	}

	/**
	 * 
	 * Endpoint PUT /api/poderes/{id} Actualiza un Poder
	 * 
	 * @return Poder actualizado
	 * @exception not Found
	 * 
	 */
	@PutMapping("{id}")
	public ResponseEntity<Poder> actualizarPoder(@PathVariable(value = "id") Integer idPoder,
			@RequestBody PoderDTO poderDTO) {

		logger.debug("Actualizando poder con id {} y data {}", idPoder, poderDTO);

		return servicio.buscarPoderPorId(idPoder).map(poderGuardado -> {

			poderGuardado.setNombre(poderDTO.getNombre());
			poderGuardado.setDescripcion(poderDTO.getDescripcion());

			Poder poderActualizado = servicio.actualizarPoder(poderGuardado);
			return new ResponseEntity<>(poderActualizado, HttpStatus.OK);

		}).orElseGet(() -> ResponseEntity.notFound().build());
	}

	/**
	 * 
	 * Endpoint DELETE /api/poderes/{id} Elimina un Poder
	 * 
	 * @return 204
	 * @exception not Found
	 * 
	 */
	@DeleteMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void eliminarPelicula(@PathVariable(value = "id") Integer idPoder) {

		logger.debug("Eliminando poder con id {}", idPoder);

		// Validacion
		Optional<Poder> poder = servicio.buscarPoderPorId(idPoder);
		if (poder.isEmpty()) {
			throw new NoSuchElementException("No existe poder con id: " + idPoder);
		}
		servicio.borrarPoder(poder.get());
	}
}
