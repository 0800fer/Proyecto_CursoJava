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
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyecto.app.entidades.Poder;
import com.proyecto.app.repositorios.IPoderRepositorio;
import com.proyecto.app.repositorios.ISuperheroeRepositorio;
import com.proyecto.app.repositorios.IUniversoRepositorio;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class PoderControladorITest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private IPoderRepositorio poderRepositorio;

	@Autowired
	private IUniversoRepositorio universoRepositorio;

	@Autowired
	private ISuperheroeRepositorio superheroeRepositorio;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	void setup() {
		superheroeRepositorio.deleteAll();
		universoRepositorio.deleteAll();
		poderRepositorio.deleteAll();
	}

	final String baseUrl = "http://localhost:8080/api/poderes";

	@DisplayName("Test para el endpoint GET /api/poderes")
	@Test
	void dadaUnaListaDePoderes_cuandoListarTodosLosPoderes_devuelveListaPoderes() throws Exception {

		// COMPORTAMIENTO ESPERADO DEL CUERPO DEL MÉTODO
		List<Poder> listaPoderes = new ArrayList<>();
		listaPoderes.add(Poder.builder().nombre("Veloz").descripcion("Descripcion").build());
		listaPoderes.add(Poder.builder().nombre("Volar").descripcion("Descripcion").build());
		poderRepositorio.saveAll(listaPoderes);
		// LLAMADA A MÉTODO A TESTEAR
		ResultActions response = mockMvc.perform(get(baseUrl));

		// COMPROBACIONES DEL RESULTADO ESPERADO
		response.andExpect(status().isOk()).andDo(print()).andExpect(jsonPath("$.size()", is(listaPoderes.size())));

	}

	@DisplayName("Test para el endpoint POST /api/poderes")
	@Test
	void dadoUnObjetoPoder_cuandoCrearPoder_devuelvePoderCreado() throws Exception {

		// COMPORTAMIENTO ESPERADO DEL CUERPO DEL MÉTODO
		Poder poder = Poder.builder().nombre("Veloz").descripcion("Descripcion").build();

		// LLAMADA A MÉTODO A TESTEAR
		ResultActions response = mockMvc.perform(
				post(baseUrl).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(poder)));

		// COMPROBACIONES DEL RESULTADO ESPERADO
		response.andDo(print()).andExpect(status().isCreated()).andExpect(jsonPath("$.nombre", is(poder.getNombre())))
				.andExpect(jsonPath("$.descripcion", is(poder.getDescripcion())));

	}

	@DisplayName("Test para el endpoint GET /api/poderes/{id} Escenario positivo")
	@Test
	void dadoPoderId_cuandoBuscarPorId_devuelvePoder() throws Exception {
		// COMPORTAMIENTO ESPERADO DEL CUERPO DEL MÉTODO
		Poder poder = Poder.builder().nombre("Veloz").descripcion("Descripcion").build();
		poderRepositorio.save(poder);

		// LLAMADA A MÉTODO A TESTEAR
		ResultActions response = mockMvc.perform(get(baseUrl + "/{id}", poder.getId()));

		// COMPROBACIONES DEL RESULTADO ESPERADO
		response.andExpect(status().isOk()).andDo(print()).andExpect(jsonPath("$.nombre", is(poder.getNombre())))
				.andExpect(jsonPath("$.descripcion", is(poder.getDescripcion())));

	}

	@DisplayName("Test para el endpoint GET /api/poderes/{id} Escenario negativo")
	@Test
	void dadoPoderId_cuandoBuscarPorId_devuelveVacio() throws Exception {
		// COMPORTAMIENTO ESPERADO DEL CUERPO DEL MÉTODO
		Integer poderId = 1;
		Poder poder = Poder.builder().nombre("Veloz").descripcion("Descripcion").build();
		poderRepositorio.save(poder);

		// LLAMADA A MÉTODO A TESTEAR
		ResultActions response = mockMvc.perform(get(baseUrl + "/{id}", poderId));

		// COMPROBACIONES DEL RESULTADO ESPERADO
		response.andExpect(status().isNotFound()).andDo(print());

	}

	@DisplayName("Test para el endpoint PUT /api/poderes/{id} Escenario positivo")
	@Test
	void dadoPoderActualizado_cuandoActualizarPoder_devuelvePoderActualizado() throws Exception {
		// COMPORTAMIENTO ESPERADO DEL CUERPO DEL MÉTODO
		Poder poderGuardado = Poder.builder().nombre("Veloz").descripcion("Descripcion").build();
		poderRepositorio.save(poderGuardado);

		Poder poderActualizado = Poder.builder().nombre("Nuevo").descripcion("Nueva").build();

		// LLAMADA A MÉTODO A TESTEAR
		ResultActions response = mockMvc.perform(put(baseUrl + "/{id}", poderGuardado.getId())
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(poderActualizado)));

		// COMPROBACIONES DEL RESULTADO ESPERADO
		response.andExpect(status().isOk()).andDo(print())
				.andExpect(jsonPath("$.nombre", is(poderActualizado.getNombre())))
				.andExpect(jsonPath("$.descripcion", is(poderActualizado.getDescripcion())));
	}

	@DisplayName("Test para el endpoint PUT /api/poderes/{id} Escenario negativo")
	@Test
	void dadoPoderActualizado_cuandoActualizarPoder_devuelveExcepcion() throws Exception {
		// COMPORTAMIENTO ESPERADO DEL CUERPO DEL MÉTODO
		Integer poderId = 1;
		Poder poderGuardado = Poder.builder().nombre("Veloz").descripcion("Descripcion").build();
		poderRepositorio.save(poderGuardado);

		Poder poderActualizado = Poder.builder().nombre("Nuevo").descripcion("Nueva").build();

		// LLAMADA A MÉTODO A TESTEAR
		ResultActions response = mockMvc.perform(put(baseUrl + "/{id}", poderId).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(poderActualizado)));

		// COMPROBACIONES DEL RESULTADO ESPERADO
		response.andExpect(status().isNotFound()).andDo(print());
	}

	@DisplayName("Test para el endpoint DELETE /api/poderes/{id} Escenario positivo")
	@Test
	void dadoPoderId_cuandoEliminarPoder_devuelve204() throws Exception {
		// COMPORTAMIENTO ESPERADO DEL CUERPO DEL MÉTODO
		Poder poder = Poder.builder().nombre("Veloz").descripcion("Descripcion").build();
		poderRepositorio.save(poder);

		// LLAMADA A MÉTODO A TESTEAR
		ResultActions response = mockMvc.perform(delete(baseUrl + "/{id}", poder.getId()));

		// COMPROBACIONES DEL RESULTADO ESPERADO
		response.andExpect(status().is2xxSuccessful()).andDo(print());
	}

	@DisplayName("Test para el endpoint DELETE /api/poderes/{id} Escenario negativo")
	@Test
	void dadoPoderIdInvalido_cuandoEliminarPoder_devuelve404() throws Exception {
		// COMPORTAMIENTO ESPERADO DEL CUERPO DEL MÉTODO
		Integer poderId = 1;
		Poder poder = Poder.builder().nombre("Veloz").descripcion("Descripcion").build();
		poderRepositorio.save(poder);

		// LLAMADA A MÉTODO A TESTEAR
		ResultActions response = mockMvc.perform(delete(baseUrl + "/{id}", poderId));

		// COMPROBACIONES DEL RESULTADO ESPERADO
		response.andExpect(status().isNotFound()).andDo(print());
	}
}