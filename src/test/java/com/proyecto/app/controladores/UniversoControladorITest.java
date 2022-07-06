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
import com.proyecto.app.entidades.Universo;
import com.proyecto.app.repositorios.IUniversoRepositorio;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class UniversoControladorITest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private IUniversoRepositorio universoRepositorio;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	void setup() {
		universoRepositorio.deleteAll();
	}

	final String baseUrl = "http://localhost:8080/api/universos";

	@DisplayName("Test para el endpoint GET /api/universos")
	@Test
	void dadaUnaListaDeUniversos_cuandoListarTodosLosUniversos_devuelveListaUniversos() throws Exception {

		// COMPORTAMIENTO ESPERADO DEL CUERPO DEL MÉTODO
		List<Universo> listaUniversos = new ArrayList<>();
		listaUniversos.add(Universo.builder().nombre("Marvel").descripcion("Descripcion").build());
		listaUniversos.add(Universo.builder().nombre("Disney").descripcion("Descripcion").build());
		universoRepositorio.saveAll(listaUniversos);
		// LLAMADA A MÉTODO A TESTEAR
		ResultActions response = mockMvc.perform(get(baseUrl));

		// COMPROBACIONES DEL RESULTADO ESPERADO
		response.andExpect(status().isOk()).andDo(print()).andExpect(jsonPath("$.size()", is(listaUniversos.size())));

	}

	@DisplayName("Test para el endpoint POST /api/universos")
	@Test
	void dadoUnObjetoUniverso_cuandoCrearUniverso_devuelveUniversoCreado() throws Exception {

		// COMPORTAMIENTO ESPERADO DEL CUERPO DEL MÉTODO
		Universo universo = Universo.builder().nombre("Marvel").descripcion("Descripcion").build();

		// LLAMADA A MÉTODO A TESTEAR
		ResultActions response = mockMvc.perform(post(baseUrl).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(universo)));

		// COMPROBACIONES DEL RESULTADO ESPERADO
		response.andDo(print()).andExpect(status().isCreated())
				.andExpect(jsonPath("$.nombre", is(universo.getNombre())))
				.andExpect(jsonPath("$.descripcion", is(universo.getDescripcion())));

	}

	@DisplayName("Test para el endpoint GET /api/universos/{id} Escenario positivo")
	@Test
	void dadoUniversoId_cuandoBuscarPorId_devuelveUniverso() throws Exception {
		// COMPORTAMIENTO ESPERADO DEL CUERPO DEL MÉTODO
		Universo universo = Universo.builder().nombre("Marvel").descripcion("Descripcion").build();
		universoRepositorio.save(universo);

		// LLAMADA A MÉTODO A TESTEAR
		ResultActions response = mockMvc.perform(get(baseUrl + "/{id}", universo.getId()));

		// COMPROBACIONES DEL RESULTADO ESPERADO
		response.andExpect(status().isOk()).andDo(print()).andExpect(jsonPath("$.nombre", is(universo.getNombre())))
				.andExpect(jsonPath("$.descripcion", is(universo.getDescripcion())));

	}

	@DisplayName("Test para el endpoint GET /api/universos/{id} Escenario negativo")
	@Test
	void dadoUniversoId_cuandoBuscarPorId_devuelveVacio() throws Exception {
		// COMPORTAMIENTO ESPERADO DEL CUERPO DEL MÉTODO
		Integer universoId = 1;
		Universo universo = Universo.builder().nombre("Marvel").descripcion("Descripcion").build();
		universoRepositorio.save(universo);

		// LLAMADA A MÉTODO A TESTEAR
		ResultActions response = mockMvc.perform(get(baseUrl + "/{id}", universoId));

		// COMPROBACIONES DEL RESULTADO ESPERADO
		response.andExpect(status().isNotFound()).andDo(print());

	}

	@DisplayName("Test para el endpoint PUT /api/universos/{id} Escenario positivo")
	@Test
	void dadoUniversoActualizado_cuandoActualizarUniverso_devuelveUniversoActualizado() throws Exception {
		// COMPORTAMIENTO ESPERADO DEL CUERPO DEL MÉTODO
		Universo universoGuardado = Universo.builder().nombre("Marvel").descripcion("Descripcion").build();
		universoRepositorio.save(universoGuardado);

		Universo universoActualizado = Universo.builder().nombre("Nuevo").descripcion("Nueva").build();

		// LLAMADA A MÉTODO A TESTEAR
		ResultActions response = mockMvc.perform(put(baseUrl + "/{id}", universoGuardado.getId())
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(universoActualizado)));

		// COMPROBACIONES DEL RESULTADO ESPERADO
		response.andExpect(status().isOk()).andDo(print())
				.andExpect(jsonPath("$.nombre", is(universoActualizado.getNombre())))
				.andExpect(jsonPath("$.descripcion", is(universoActualizado.getDescripcion())));
	}

	@DisplayName("Test para el endpoint PUT /api/universos/{id} Escenario negativo")
	@Test
	void dadoUniversoActualizado_cuandoActualizarUniverso_devuelveExcepcion() throws Exception {
		// COMPORTAMIENTO ESPERADO DEL CUERPO DEL MÉTODO
		Integer universoId = 1;
		Universo universoGuardado = Universo.builder().nombre("Marvel").descripcion("Descripcion").build();
		universoRepositorio.save(universoGuardado);

		Universo universoActualizado = Universo.builder().nombre("Nuevo").descripcion("Nueva").build();

		// LLAMADA A MÉTODO A TESTEAR
		ResultActions response = mockMvc.perform(put(baseUrl + "/{id}", universoId)
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(universoActualizado)));

		// COMPROBACIONES DEL RESULTADO ESPERADO
		response.andExpect(status().isNotFound()).andDo(print());
	}

	@DisplayName("Test para el endpoint DELETE /api/universos/{id} Escenario positivo")
	@Test
	void dadoUniversoId_cuandoEliminarUniverso_devuelve204() throws Exception {
		// COMPORTAMIENTO ESPERADO DEL CUERPO DEL MÉTODO
		Universo universo = Universo.builder().nombre("Marvel").descripcion("Descripcion").build();
		universoRepositorio.save(universo);

		// LLAMADA A MÉTODO A TESTEAR
		ResultActions response = mockMvc.perform(delete(baseUrl + "/{id}", universo.getId()));

		// COMPROBACIONES DEL RESULTADO ESPERADO
		response.andExpect(status().is2xxSuccessful()).andDo(print());
	}

	@DisplayName("Test para el endpoint DELETE /api/universos/{id} Escenario negativo")
	@Test
	void dadoUniversoIdInvalido_cuandoEliminarUniverso_devuelve404() throws Exception {
		// COMPORTAMIENTO ESPERADO DEL CUERPO DEL MÉTODO
		Integer universoId = 1;
		Universo universo = Universo.builder().nombre("Marvel").descripcion("Descripcion").build();
		universoRepositorio.save(universo);

		// LLAMADA A MÉTODO A TESTEAR
		ResultActions response = mockMvc.perform(delete(baseUrl + "/{id}", universoId));

		// COMPROBACIONES DEL RESULTADO ESPERADO
		response.andExpect(status().isNotFound()).andDo(print());
	}
}