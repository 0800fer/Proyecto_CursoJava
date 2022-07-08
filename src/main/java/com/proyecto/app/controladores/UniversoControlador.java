package com.proyecto.app.controladores;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.app.entidades.Universo;
import com.proyecto.app.entidades.UniversoDTO;
import com.proyecto.app.servicios.UniversoServicioImpl;

/**
 * 
 * Desarrollo de endpoint /api/universos
 *
 */
@RestController
@RequestMapping("api/universos")
public class UniversoControlador {

	private static final Logger logger = LoggerFactory.getLogger(UniversoControlador.class);

	@Autowired
	private UniversoServicioImpl servicio;

	@Autowired
	private ModelMapper modelMapper;

	/**
	 * 
	 * Endpoint GET /api/universos
	 * 
	 * @return Lista de universos
	 * 
	 */
	@GetMapping()
	public List<UniversoDTO> listarUniversoes() {
		logger.info("Obteniendo todos los universoes");
		return servicio.listarTodosLosUniversos().stream().map(universo -> modelMapper.map(universo, UniversoDTO.class))
				.collect(Collectors.toList());
	}

	/**
	 * 
	 * Endpoint POST /api/universoes Crea un Universo
	 * 
	 * @return Universo creado
	 * 
	 */
	@PostMapping
	public ResponseEntity<UniversoDTO> crearUniverso(@RequestBody UniversoDTO universoDTO) {
		logger.info("Creando universo con data {}", universoDTO);
		Optional<Universo> universoYaExiste = servicio.buscarUniversoNombre(universoDTO.getNombre());
		if (universoYaExiste.isPresent()) {
			throw new DuplicateKeyException("Ya existe universo con nombre: " + universoDTO.getNombre());
		}
		// DTO a entidad
		Universo universo = servicio.crearUniverso(modelMapper.map(universoDTO, Universo.class));
		// entidad a DTO
		UniversoDTO universoResponse = modelMapper.map(universo, UniversoDTO.class);
		return new ResponseEntity<>(universoResponse, HttpStatus.CREATED);
	}

	/**
	 * 
	 * Endpoint GET /api/universoes/{id} Busca un Universo
	 * 
	 * @return Universo encontrado
	 * @exception not Found
	 * 
	 */
	@GetMapping("/{id}")
	public ResponseEntity<UniversoDTO> buscarUniversoPorId(@PathVariable(name = "id") Integer idUniverso) {
		logger.info("Obteniendo universo con id {}", idUniverso);

		// Validacion
		Optional<Universo> universo = servicio.buscarUniversoPorId(idUniverso);
		if (universo.isEmpty()) {
			throw new NoSuchElementException("No existe universo con id: " + idUniverso);
		}
		// entidad a DTO
		UniversoDTO universoDTO = modelMapper.map(universo.get(), UniversoDTO.class);
		return ResponseEntity.ok().body(universoDTO);
	}

	/**
	 * 
	 * Endpoint PUT /api/universoes/{id} Actualiza un Universo
	 * 
	 * @return Universo actualizado
	 * @exception not Found
	 * 
	 */
	@PutMapping("{id}")
	public ResponseEntity<Universo> actualizarUniverso(@PathVariable(value = "id") Integer idUniverso,
			@RequestBody UniversoDTO universoDTO) {

		logger.info("Actualizando universo con id {} y data {}", idUniverso, universoDTO);

		return servicio.buscarUniversoPorId(idUniverso).map(universoGuardado -> {

			universoGuardado.setNombre(universoDTO.getNombre());
			universoGuardado.setDescripcion(universoDTO.getDescripcion());

			Universo universoActualizado = servicio.actualizarUniverso(universoGuardado);
			return new ResponseEntity<>(universoActualizado, HttpStatus.OK);

		}).orElseGet(() -> ResponseEntity.notFound().build());
	}

	/**
	 * 
	 * Endpoint DELETE /api/universoes/{id} Elimina un Universo
	 * 
	 * @return 204
	 * @exception not Found
	 * 
	 */
	@DeleteMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void eliminarPelicula(@PathVariable(value = "id") Integer idUniverso) {

		logger.info("Eliminando universo con id {}", idUniverso);

		// Validacion
		Optional<Universo> universo = servicio.buscarUniversoPorId(idUniverso);
		if (universo.isEmpty()) {
			throw new NoSuchElementException("No existe universo con id: " + idUniverso);
		}
		servicio.borrarUniverso(universo.get());
	}
}
