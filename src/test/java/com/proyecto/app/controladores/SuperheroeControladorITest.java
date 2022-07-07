package com.proyecto.app.controladores;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyecto.app.entidades.Poder;
import com.proyecto.app.entidades.Superheroe;
import com.proyecto.app.entidades.SuperheroeDTO;
import com.proyecto.app.entidades.Universo;
import com.proyecto.app.repositorios.IPoderRepositorio;
import com.proyecto.app.repositorios.ISuperheroeRepositorio;
import com.proyecto.app.repositorios.IUniversoRepositorio;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class SuperheroeControladorITest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ISuperheroeRepositorio superheroeRepositorio;

	@Autowired
	private IUniversoRepositorio universoRepositorio;

	@Autowired
	private IPoderRepositorio poderRepositorio;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private ObjectMapper objectMapper;

	final String baseUrl = "http://localhost:8080/api/superheroes";

	@BeforeEach
	void setup() {
		superheroeRepositorio.deleteAll();
		universoRepositorio.deleteAll();
		poderRepositorio.deleteAll();
	}

	@DisplayName("Test para el endpoint GET /api/superheroes")
	@Test
	void dadaUnaListaDeSuperheroes_cuandoListarTodosLosSuperheroes_devuelveListaSuperheroes() throws Exception {

		// COMPORTAMIENTO ESPERADO DEL CUERPO DEL MÉTODO
		List<Superheroe> listaSuperheroes = new ArrayList<>();
		Universo universoSalvado = universoRepositorio
				.save(Universo.builder().nombre("Wagner1").descripcion("Descripcion").build());
		listaSuperheroes.add(
				Superheroe.builder().nombre("Wagner").historia("Historia").universoId(universoSalvado.getId()).build());
		listaSuperheroes.add(
				Superheroe.builder().nombre("Disney").historia("Historia").universoId(universoSalvado.getId()).build());
		superheroeRepositorio.saveAll(listaSuperheroes);
		// LLAMADA A MÉTODO A TESTEAR
		ResultActions response = mockMvc.perform(get(baseUrl));

		// COMPROBACIONES DEL RESULTADO ESPERADO
		response.andExpect(status().isOk()).andDo(print()).andExpect(jsonPath("$.size()", is(listaSuperheroes.size())));

	}

	@DisplayName("Test para el endpoint GET /api/superheroes/buscar?nombre= (Positivo")
	@Test
	void dadaUnaListaDeSuperheroes_cuandoListarSuperheroesConFiltro_devuelveListaSuperheroes() throws Exception {

		// COMPORTAMIENTO ESPERADO DEL CUERPO DEL MÉTODO
		List<Superheroe> listaSuperheroes = new ArrayList<>();
		Universo universoSalvado = universoRepositorio
				.save(Universo.builder().nombre("Wagner1").descripcion("Descripcion").build());
		listaSuperheroes.add(
				Superheroe.builder().nombre("Batman").historia("Historia").universoId(universoSalvado.getId()).build());
		listaSuperheroes.add(Superheroe.builder().nombre("Superman").historia("Historia")
				.universoId(universoSalvado.getId()).build());
		listaSuperheroes.add(Superheroe.builder().nombre("Supergirl").historia("Historia")
				.universoId(universoSalvado.getId()).build());
		superheroeRepositorio.saveAll(listaSuperheroes);

		// LLAMADA A MÉTODO A TESTEAR
		ResultActions response = mockMvc.perform(get(baseUrl + "/buscar?nombre=super"));

		// COMPROBACIONES DEL RESULTADO ESPERADO
		response.andExpect(status().isOk()).andDo(print()).andExpect(jsonPath("$.size()", is(2)));

	}

	@DisplayName("Test para el endpoint GET /api/superheroes/buscar?nombre= (Negativo)")
	@Test
	void dadaUnaListaDeSuperheroes_cuandoListarSuperheroesConFiltro_devuelveListaVacia() throws Exception {

		// COMPORTAMIENTO ESPERADO DEL CUERPO DEL MÉTODO
		List<Superheroe> listaSuperheroes = new ArrayList<>();
		Universo universoSalvado = universoRepositorio
				.save(Universo.builder().nombre("Wagner1").descripcion("Descripcion").build());
		listaSuperheroes.add(
				Superheroe.builder().nombre("Batman").historia("Historia").universoId(universoSalvado.getId()).build());
		listaSuperheroes.add(Superheroe.builder().nombre("Superman").historia("Historia")
				.universoId(universoSalvado.getId()).build());
		listaSuperheroes.add(Superheroe.builder().nombre("Supergirl").historia("Historia")
				.universoId(universoSalvado.getId()).build());
		superheroeRepositorio.saveAll(listaSuperheroes);
		// LLAMADA A MÉTODO A TESTEAR
		ResultActions response = mockMvc.perform(get(baseUrl + "/buscar?nombre=mo"));

		// COMPROBACIONES DEL RESULTADO ESPERADO
		response.andExpect(status().isOk()).andDo(print()).andExpect(jsonPath("$.size()", is(0)));

	}

	@DisplayName("Test para el endpoint POST /api/superheroes")
	@Test
	void dadoUnObjetoSuperheroe_cuandoCrearSuperheroe_devuelveSuperheroeCreado() throws Exception {

		Poder poderSalvado = poderRepositorio.save(Poder.builder().nombre("Veloz").descripcion("Descripcion").build());
		List<String> listaPoderesIds = new ArrayList<>();
		listaPoderesIds.add(poderSalvado.getId().toString());

		// COMPORTAMIENTO ESPERADO DEL CUERPO DEL MÉTODO
		Universo universoSalvado = universoRepositorio
				.save(Universo.builder().nombre("Wagner1").descripcion("Descripcion").build());
		SuperheroeDTO superheroe = SuperheroeDTO.builder().nombre("Wagner").poderes(listaPoderesIds)
				.historia("Historia").universoId(universoSalvado.getId()).build();

		// LLAMADA A MÉTODO A TESTEAR
		ResultActions response = mockMvc.perform(post(baseUrl).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(superheroe)));

		// COMPROBACIONES DEL RESULTADO ESPERADO
		response.andDo(print()).andExpect(status().isCreated())
				.andExpect(jsonPath("$.nombre", is(superheroe.getNombre())))
				.andExpect(jsonPath("$.historia", is(superheroe.getHistoria())));

	}

	@DisplayName("Test para el endpoint POST /api/superheroes Poderes invalidos")
	@Test
	void dadoUnObjetoSuperheroeConPoderesInvalidos_cuandoCrearSuperheroe_devuelveExcepcion() throws Exception {

		Integer poderSalvado = 0;
		List<String> listaPoderesIds = new ArrayList<>();
		listaPoderesIds.add(poderSalvado.toString());

		// COMPORTAMIENTO ESPERADO DEL CUERPO DEL MÉTODO
		Universo universoSalvado = universoRepositorio
				.save(Universo.builder().nombre("Wagner1").descripcion("Descripcion").build());
		SuperheroeDTO superheroe = SuperheroeDTO.builder().nombre("Wagner").poderes(listaPoderesIds)
				.historia("Historia").universoId(universoSalvado.getId()).build();

		// LLAMADA A MÉTODO A TESTEAR
		ResultActions response = mockMvc.perform(post(baseUrl).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(superheroe)));

		// COMPROBACIONES DEL RESULTADO ESPERADO
		response.andDo(print()).andExpect(status().isNotAcceptable());
	}

	@DisplayName("Test para el endpoint POST /api/superheroes Universo Invalido")
	@Test
	void dadoUnObjetoSuperheroeConUniversoInvalido_cuandoCrearSuperheroe_devuelveExcepcion() throws Exception {

		Poder poderSalvado = poderRepositorio.save(Poder.builder().nombre("Veloz").descripcion("Descripcion").build());
		List<String> listaPoderesIds = new ArrayList<>();
		listaPoderesIds.add(poderSalvado.getId().toString());

		// COMPORTAMIENTO ESPERADO DEL CUERPO DEL MÉTODO

		int universoIdInvalido = 0;
		SuperheroeDTO superheroe = SuperheroeDTO.builder().nombre("Wagner").poderes(listaPoderesIds)
				.historia("Historia").universoId(universoIdInvalido).build();

		// LLAMADA A MÉTODO A TESTEAR
		ResultActions response = mockMvc.perform(post(baseUrl).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(superheroe)));

		// COMPROBACIONES DEL RESULTADO ESPERADO
		response.andDo(print()).andExpect(status().isNotAcceptable());
	}

	@DisplayName("Test para el endpoint POST /api/superheroes Superheroe ya existe")
	@Test
	void dadoUnObjetoSuperheroeConNombreDuplicado_cuandoCrearSuperheroe_devuelveExcepcion() throws Exception {

		Universo universoSalvado = universoRepositorio
				.save(Universo.builder().nombre("Wagner1").descripcion("Descripcion").build());

		superheroeRepositorio.save(
				Superheroe.builder().nombre("Wagner").historia("Historia").universoId(universoSalvado.getId()).build());

		Poder poderSalvado = poderRepositorio.save(Poder.builder().nombre("Veloz").descripcion("Descripcion").build());
		List<String> listaPoderesIds = new ArrayList<>();
		listaPoderesIds.add(poderSalvado.getId().toString());

		// COMPORTAMIENTO ESPERADO DEL CUERPO DEL MÉTODO

		SuperheroeDTO superheroe = SuperheroeDTO.builder().nombre("Wagner").poderes(listaPoderesIds)
				.historia("Historia").universoId(universoSalvado.getId()).build();

		// LLAMADA A MÉTODO A TESTEAR
		ResultActions response = mockMvc.perform(post(baseUrl).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(superheroe)));

		// COMPROBACIONES DEL RESULTADO ESPERADO
		response.andDo(print()).andExpect(status().isUnprocessableEntity());
	}

	@DisplayName("Test para el endpoint GET /api/superheroes/{id} Escenario positivo")
	@Test
	void dadoSuperheroeId_cuandoBuscarPorId_devuelveSuperheroe() throws Exception {
		// COMPORTAMIENTO ESPERADO DEL CUERPO DEL MÉTODO
		Poder poderSalvado = poderRepositorio.save(Poder.builder().nombre("Veloz").descripcion("Descripcion").build());
		Set<Poder> poderSet = new HashSet<>();
		List<String> listaPoderesIds = new ArrayList<>();
		listaPoderesIds.add(poderSalvado.getId().toString());
		poderSet.add(poderSalvado);
		Universo universoSalvado = universoRepositorio
				.save(Universo.builder().nombre("Wagner1").descripcion("Descripcion").build());
		SuperheroeDTO superheroeDTO = SuperheroeDTO.builder().nombre("Wagner").historia("Historia")
				.universoId(universoSalvado.getId()).poderes(listaPoderesIds).build();
		Superheroe superheroeSalvado = superheroeRepositorio.save(modelMapper.map(superheroeDTO, Superheroe.class));

		// LLAMADA A MÉTODO A TESTEAR
		ResultActions response = mockMvc.perform(get(baseUrl + "/{id}", superheroeSalvado.getId()));

		// COMPROBACIONES DEL RESULTADO ESPERADO
		response.andExpect(status().isOk()).andDo(print())
				.andExpect(jsonPath("$.nombre", is(superheroeSalvado.getNombre())))
				.andExpect(jsonPath("$.historia", is(superheroeSalvado.getHistoria())));

	}

	@DisplayName("Test para el endpoint GET /api/superheroes/{id} Escenario negativo")
	@Test
	void dadoSuperheroeId_cuandoBuscarPorId_devuelveVacio() throws Exception {
		// COMPORTAMIENTO ESPERADO DEL CUERPO DEL MÉTODO
		Integer superheroeId = 1;
		Universo universoSalvado = universoRepositorio
				.save(Universo.builder().nombre("Wagner1").descripcion("Descripcion").build());
		Superheroe superheroe = Superheroe.builder().nombre("Wagner").historia("Historia")
				.universoId(universoSalvado.getId()).build();
		superheroeRepositorio.save(superheroe);

		// LLAMADA A MÉTODO A TESTEAR
		ResultActions response = mockMvc.perform(get(baseUrl + "/{id}", superheroeId));

		// COMPROBACIONES DEL RESULTADO ESPERADO
		response.andExpect(status().isNotFound()).andDo(print());

	}

	@DisplayName("Test para el endpoint PUT /api/superheroes/{id} Escenario positivo")
	@Test
	void dadoSuperheroeActualizado_cuandoActualizarSuperheroe_devuelveSuperheroeActualizado() throws Exception {
		// COMPORTAMIENTO ESPERADO DEL CUERPO DEL MÉTODO
		Universo universoSalvado = universoRepositorio
				.save(Universo.builder().nombre("Wagner1").descripcion("Descripcion").build());
		Superheroe superheroeGuardado = Superheroe.builder().nombre("Wagner").historia("Historia")
				.universoId(universoSalvado.getId()).build();
		superheroeRepositorio.save(superheroeGuardado);

		Superheroe superheroeActualizado = Superheroe.builder().nombre("Nuevo").historia("Nueva")
				.universoId(universoSalvado.getId()).build();

		// LLAMADA A MÉTODO A TESTEAR
		ResultActions response = mockMvc
				.perform(put(baseUrl + "/{id}", superheroeGuardado.getId()).contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(superheroeActualizado)));

		// COMPROBACIONES DEL RESULTADO ESPERADO
		response.andExpect(status().isOk()).andDo(print())
				.andExpect(jsonPath("$.nombre", is(superheroeActualizado.getNombre())))
				.andExpect(jsonPath("$.historia", is(superheroeActualizado.getHistoria())));
	}

	@DisplayName("Test para el endpoint PUT /api/superheroes/{id} Escenario negativo")
	@Test
	void dadoSuperheroeActualizado_cuandoActualizarSuperheroe_devuelveExcepcion() throws Exception {
		// COMPORTAMIENTO ESPERADO DEL CUERPO DEL MÉTODO
		Integer superheroeId = 1;
		Universo universoSalvado = universoRepositorio
				.save(Universo.builder().nombre("Wagner1").descripcion("Descripcion").build());
		Superheroe superheroeGuardado = Superheroe.builder().nombre("Wagner").historia("Historia")
				.universoId(universoSalvado.getId()).build();
		superheroeRepositorio.save(superheroeGuardado);

		Superheroe superheroeActualizado = Superheroe.builder().nombre("Nuevo").historia("Nueva")
				.universoId(universoSalvado.getId()).build();

		// LLAMADA A MÉTODO A TESTEAR
		ResultActions response = mockMvc
				.perform(put(baseUrl + "/{id}", superheroeId).contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(superheroeActualizado)));

		// COMPROBACIONES DEL RESULTADO ESPERADO
		response.andExpect(status().isNotFound()).andDo(print());
	}

	@DisplayName("Test para el endpoint DELETE /api/superheroes/{id} Escenario positivo")
	@Test
	void dadoSuperheroeId_cuandoEliminarSuperheroe_devuelve204() throws Exception {
		// COMPORTAMIENTO ESPERADO DEL CUERPO DEL MÉTODO
		Universo universoSalvado = universoRepositorio
				.save(Universo.builder().nombre("Wagner1").descripcion("Descripcion").build());
		Superheroe superheroe = Superheroe.builder().nombre("Wagner").historia("Historia")
				.universoId(universoSalvado.getId()).build();
		superheroeRepositorio.save(superheroe);

		// LLAMADA A MÉTODO A TESTEAR
		ResultActions response = mockMvc.perform(delete(baseUrl + "/{id}", superheroe.getId()));

		// COMPROBACIONES DEL RESULTADO ESPERADO
		response.andExpect(status().is2xxSuccessful()).andDo(print());
	}

	@DisplayName("Test para el endpoint DELETE /api/superheroes/{id} Escenario negativo")
	@Test
	void dadoSuperheroeIdInvalido_cuandoEliminarSuperheroe_devuelve404() throws Exception {
		// COMPORTAMIENTO ESPERADO DEL CUERPO DEL MÉTODO
		Integer superheroeId = 1;
		Universo universoSalvado = universoRepositorio
				.save(Universo.builder().nombre("Wagner1").descripcion("Descripcion").build());
		Superheroe superheroe = Superheroe.builder().nombre("Wagner").historia("Historia")
				.universoId(universoSalvado.getId()).build();
		superheroeRepositorio.save(superheroe);

		// LLAMADA A MÉTODO A TESTEAR
		ResultActions response = mockMvc.perform(delete(baseUrl + "/{id}", superheroeId));

		// COMPROBACIONES DEL RESULTADO ESPERADO
		response.andExpect(status().isNotFound()).andDo(print());
	}

	@DisplayName("Test para el endpoint POST /api/superheroes/{id}/matar Escenario positivo")
	@Test
	void dadoSuperheroeId_cuandoMatarSuperheroe_devuelve200() throws Exception {
		// COMPORTAMIENTO ESPERADO DEL CUERPO DEL MÉTODO
		Universo universoSalvado = universoRepositorio
				.save(Universo.builder().nombre("Wagner1").descripcion("Descripcion").build());
		Superheroe superheroe = Superheroe.builder().nombre("Wagner").historia("Historia")
				.universoId(universoSalvado.getId()).build();
		superheroeRepositorio.save(superheroe);
		Integer superheroeId = superheroe.getId();

		// LLAMADA A MÉTODO A TESTEAR
		ResultActions response = mockMvc.perform(post(baseUrl + "/{id}/matar", superheroeId));

		// COMPROBACIONES DEL RESULTADO ESPERADO
		response.andExpect(status().isOk()).andDo(print());
	}

	@DisplayName("Test para el endpoint POST /api/superheroes/{id}/matar Escenario negativo")
	@Test
	void dadoSuperheroeIdInvalido_cuandoMatarSuperheroe_devuelve404() throws Exception {
		// COMPORTAMIENTO ESPERADO DEL CUERPO DEL MÉTODO
		Universo universoSalvado = universoRepositorio
				.save(Universo.builder().nombre("Wagner1").descripcion("Descripcion").build());
		Superheroe superheroe = Superheroe.builder().nombre("Wagner").historia("Historia")
				.universoId(universoSalvado.getId()).build();
		superheroeRepositorio.save(superheroe);
		Integer superheroeId = superheroe.getId();

		// LLAMADA A MÉTODO A TESTEAR
		ResultActions response = mockMvc.perform(post(baseUrl + "/{id}/matar", superheroeId + 1));

		// COMPROBACIONES DEL RESULTADO ESPERADO
		response.andExpect(status().isNotFound()).andDo(print());
	}

	@DisplayName("Test para el endpoint POST /api/superheroes/{id}/resucitar Escenario positivo")
	@Test
	void dadoSuperheroeId_cuandoResucitarSuperheroe_devuelve200() throws Exception {
		// COMPORTAMIENTO ESPERADO DEL CUERPO DEL MÉTODO
		Universo universoSalvado = universoRepositorio
				.save(Universo.builder().nombre("Wagner1").descripcion("Descripcion").build());
		Superheroe superheroe = Superheroe.builder().nombre("Wagner").estaVivo(false).historia("Historia")
				.universoId(universoSalvado.getId()).build();
		superheroeRepositorio.save(superheroe);
		Integer superheroeId = superheroe.getId();

		// LLAMADA A MÉTODO A TESTEAR
		ResultActions response = mockMvc.perform(post(baseUrl + "/{id}/resucitar", superheroeId));

		// COMPROBACIONES DEL RESULTADO ESPERADO
		response.andExpect(status().isOk()).andDo(print());
	}

	@DisplayName("Test para el endpoint POST /api/superheroes/{id}/matar Escenario negativo")
	@Test
	void dadoSuperheroeIdInvalido_cuandoResucitarSuperheroe_devuelve404() throws Exception {
		// COMPORTAMIENTO ESPERADO DEL CUERPO DEL MÉTODO
		Universo universoSalvado = universoRepositorio
				.save(Universo.builder().nombre("Wagner1").descripcion("Descripcion").build());
		Superheroe superheroe = Superheroe.builder().nombre("Wagner").historia("Historia")
				.universoId(universoSalvado.getId()).build();
		superheroeRepositorio.save(superheroe);
		Integer superheroeId = superheroe.getId();

		// LLAMADA A MÉTODO A TESTEAR
		ResultActions response = mockMvc.perform(post(baseUrl + "/{id}/resucitar", superheroeId + 1));

		// COMPROBACIONES DEL RESULTADO ESPERADO
		response.andExpect(status().isNotFound()).andDo(print());
	}

	@DisplayName("Test para el endpoint POST /api/superheroes/{id}/matar Escenario ya muerto")
	@Test
	void dadoSuperheroeId_cuandoMatarSuperheroeYaMuerto_devuelve406() throws Exception {
		// COMPORTAMIENTO ESPERADO DEL CUERPO DEL MÉTODO
		Universo universoSalvado = universoRepositorio
				.save(Universo.builder().nombre("Wagner1").descripcion("Descripcion").build());
		Superheroe superheroe = Superheroe.builder().nombre("Wagner").estaVivo(false).historia("Historia")
				.universoId(universoSalvado.getId()).build();
		superheroeRepositorio.save(superheroe);
		Integer superheroeId = superheroe.getId();

		// LLAMADA A MÉTODO A TESTEAR
		ResultActions response = mockMvc.perform(post(baseUrl + "/{id}/matar", superheroeId));

		// COMPROBACIONES DEL RESULTADO ESPERADO
		response.andExpect(status().isNotAcceptable()).andDo(print());
	}

	@DisplayName("Test para el endpoint POST /api/superheroes/{id}/resucitar Escenario ya vivo")
	@Test
	void dadoSuperheroeId_cuandoResucitarSuperheroeYaVivo_devuelve406() throws Exception {
		// COMPORTAMIENTO ESPERADO DEL CUERPO DEL MÉTODO
		Universo universoSalvado = universoRepositorio
				.save(Universo.builder().nombre("Wagner1").descripcion("Descripcion").build());
		Superheroe superheroe = Superheroe.builder().nombre("Wagner").historia("Historia")
				.universoId(universoSalvado.getId()).build();
		superheroeRepositorio.save(superheroe);
		Integer superheroeId = superheroe.getId();

		// LLAMADA A MÉTODO A TESTEAR
		ResultActions response = mockMvc.perform(post(baseUrl + "/{id}/resucitar", superheroeId));

		// COMPROBACIONES DEL RESULTADO ESPERADO
		response.andExpect(status().isNotAcceptable()).andDo(print());
	}

	@DisplayName("Test para el endpoint GET /api/superheroes/{id}/poderes Escenario positivo")
	@Test
	void dadoSuperheroeId_cuandoListarPoderes_devuelveLista() throws Exception {
		// COMPORTAMIENTO ESPERADO DEL CUERPO DEL MÉTODO
		Poder poderSalvado = poderRepositorio.save(Poder.builder().nombre("Veloz").descripcion("Descripcion").build());
		Set<Poder> poderSet = new HashSet<>();
		List<String> listaPoderesIds = new ArrayList<>();
		listaPoderesIds.add(poderSalvado.getId().toString());
		poderSet.add(poderSalvado);
		Universo universoSalvado = universoRepositorio
				.save(Universo.builder().nombre("Wagner1").descripcion("Descripcion").build());
		SuperheroeDTO superheroeDTO = SuperheroeDTO.builder().nombre("Wagner").historia("Historia")
				.universoId(universoSalvado.getId()).poderes(listaPoderesIds).build();
		Superheroe superheroeSalvado = superheroeRepositorio.save(modelMapper.map(superheroeDTO, Superheroe.class));

		Integer superheroeId = superheroeSalvado.getId();

		// LLAMADA A MÉTODO A TESTEAR
		ResultActions response = mockMvc.perform(get(baseUrl + "/{id}/poderes", superheroeId));

		// COMPROBACIONES DEL RESULTADO ESPERADO
		response.andExpect(status().isOk()).andDo(print()).andExpect(jsonPath("$.size()", is(listaPoderesIds.size())));
	}

	@DisplayName("Test para el endpoint GET /api/superheroes/{id}/poderes Escenario Negativo")
	@Test
	void dadoSuperheroeIdInvalido_cuandoListarPoderes_arrojaExcepcion() throws Exception {
		// COMPORTAMIENTO ESPERADO DEL CUERPO DEL MÉTODO
//		Poder poderSalvado = poderRepositorio.save(Poder.builder().nombre("Veloz").descripcion("Descripcion").build());
//		Set<Poder> poderSet = new HashSet<>();
//		List<String> listaPoderesIds = new ArrayList<>();
//		listaPoderesIds.add(poderSalvado.getId().toString());
//		poderSet.add(poderSalvado);
//		Universo universoSalvado = universoRepositorio
//				.save(Universo.builder().nombre("Wagner1").descripcion("Descripcion").build());
//		SuperheroeDTO superheroeDTO = SuperheroeDTO.builder().nombre("Wagner").historia("Historia")
//				.universoId(universoSalvado.getId()).poderes(listaPoderesIds).build();
//		Superheroe superheroeSalvado = superheroeRepositorio.save(modelMapper.map(superheroeDTO, Superheroe.class));
//
//		Integer superheroeId = superheroeSalvado.getId();

		// LLAMADA A MÉTODO A TESTEAR
		ResultActions response = mockMvc.perform(get(baseUrl + "/{id}/poderes", 0));

		// COMPROBACIONES DEL RESULTADO ESPERADO
		response.andExpect(status().isNotFound()).andDo(print());
	}
}