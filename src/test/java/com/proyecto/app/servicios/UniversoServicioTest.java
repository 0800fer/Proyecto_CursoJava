package com.proyecto.app.servicios;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.proyecto.app.entidades.Universo;
import com.proyecto.app.repositorios.IUniversoRepositorio;

@ExtendWith(MockitoExtension.class) // -> JUNIT 5
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class UniversoServicioTest {

	@InjectMocks
	private UniversoServicioImpl universoServicio;

	@Mock
	private IUniversoRepositorio universoRepositorio;

	private Universo universo;

	@BeforeEach
	public void setup() {
		universo = Universo.builder().id(1).nombre("Marvel").descripcion("Descripcion de Marvel").build();
	}

	@DisplayName("Test para obtener lista de universos (Con valores)")
	@Test
	void listarTodosLosUniversoesPositivoTest() {
		// DEFINICIÓN DE VARIABLES DE ENTRADA
		Universo universo2 = Universo.builder().id(null).nombre("Disney").descripcion("Descripcion de Disney").build();

		// COMPORTAMIENTO ESPERADO DEL CUERPO DEL MÉTODO
		given(universoRepositorio.findAll()).willReturn(List.of(universo, universo2));

		// LLAMADA A MÉTODO A TESTEAR
		List<Universo> universoes = universoServicio.listarTodosLosUniversos();

		// COMPROBACIONES DEL RESULTADO ESPERADO
		assertThat(universoes).isNotNull().hasSize(2);
	}

	@DisplayName("Test para obtener lista de universos (Vacia)")
	@Test
	void listarTodosLosUniversoesNegativoTest() {

		// COMPORTAMIENTO ESPERADO DEL CUERPO DEL MÉTODO
		given(universoRepositorio.findAll()).willReturn(Collections.emptyList());

		// LLAMADA A MÉTODO A TESTEAR
		List<Universo> universoes = universoServicio.listarTodosLosUniversos();

		// COMPROBACIONES DEL RESULTADO ESPERADO
		assertThat(universoes).isEmpty();
	}

	@DisplayName("Test para buscar un universo (Positivo)")
	@Test
	void buscarUniversoPorIdPositivoTest() throws Exception {

		// DEFINICIÓN DE VARIABLES DE ENTRADA Y RESULTADOS
		Integer universoIdParam = Integer.valueOf("1");

		// COMPORTAMIENTO ESPERADO DEL CUERPO DEL MÉTODO
		given(universoRepositorio.findById(universoIdParam)).willReturn(Optional.of(universo));

		// LLAMADA A MÉTODO A TESTEAR
		Universo universoLeido = universoServicio.buscarUniversoPorId(universo.getId()).get();

		// COMPROBACIONES DEL RESULTADO ESPERADO
		assertThat(universoLeido).isNotNull();
	}

	@DisplayName("Test para buscar un universo (Negativo)")
	@Test
	void buscarUniversoPorIdNegativoTest() throws Exception {

		// DEFINICIÓN DE VARIABLES DE ENTRADA Y RESULTADOS
		Integer universoIdParam = Integer.valueOf("1");

		// COMPORTAMIENTO ESPERADO DEL CUERPO DEL MÉTODO
		given(universoRepositorio.findById(universoIdParam)).willReturn(null);

		// LLAMADA A MÉTODO A TESTEAR
		Optional<Universo> universoActual = universoServicio.buscarUniversoPorId(universoIdParam);

		// COMPROBACIONES DEL RESULTADO ESPERADO
		assertThat(universoActual).isNull();
	}

	@DisplayName("Test para crear un Universo")
	@Test
	void crearUniversoTest() throws Exception {
		// DEFINICIÓN DE VARIABLES DE ENTRADA Y RESULTADOS
		Integer universoIdParam = Integer.valueOf("1");

		// COMPORTAMIENTO ESPERADO DEL CUERPO DEL MÉTODO
		given(universoRepositorio.findByNombre(universo.getNombre())).willReturn(Optional.empty());

		given(universoRepositorio.save(universo)).willReturn(universo);

		// LLAMADA A MÉTODO A TESTEAR
		Universo nuevoUniverso = universoServicio.crearUniverso(universo);

		// COMPROBACIONES DEL RESULTADO ESPERADO
		assertThat(nuevoUniverso).isNotNull();
		assertThat(nuevoUniverso.getId()).isEqualTo(universoIdParam);
	}

	@DisplayName("Test para actualizar un Universo")
	@Test
	void actualizarUniversoTest() {
		// DEFINICIÓN DE VARIABLES DE ENTRADA Y RESULTADOS

		// COMPORTAMIENTO ESPERADO DEL CUERPO DEL MÉTODO
		given(universoRepositorio.save(universo)).willReturn(universo);
		universo.setNombre("Nuevo");
		universo.setDescripcion("Nueva");
		// LLAMADA A MÉTODO A TESTEAR
		Universo universoActualizado = universoServicio.actualizarUniverso(universo);

		// COMPROBACIONES DEL RESULTADO ESPERADO
		assertThat(universoActualizado.getNombre()).isEqualTo("Nuevo");
		assertThat(universoActualizado.getDescripcion()).isEqualTo("Nueva");
	}

	@DisplayName("Test para eliminar un Universo")
	@Test
	void eliminarUniversoTest() {

		// COMPORTAMIENTO ESPERADO DEL CUERPO DEL MÉTODO
		willDoNothing().given(universoRepositorio).delete(universo);

		// LLAMADA A MÉTODO A TESTEAR
		universoServicio.borrarUniverso(universo);

		// COMPROBACIONES DEL RESULTADO ESPERADO
		verify(universoRepositorio, times(1)).delete(universo);
	}

}