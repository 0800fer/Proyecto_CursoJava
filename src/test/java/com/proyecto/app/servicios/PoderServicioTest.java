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

import com.proyecto.app.entidades.Poder;
import com.proyecto.app.repositorios.IPoderRepositorio;

@ExtendWith(MockitoExtension.class) // -> JUNIT 5
class PoderServicioTest {

	@InjectMocks
	private PoderServicioImpl poderServicio;

	@Mock
	private IPoderRepositorio poderRepositorio;

	private Poder poder;

	@BeforeEach
	public void setup() {
		poder = Poder.builder().id(1).nombre("Volar").descripcion("Descripcion de Volar").build();
	}

	@DisplayName("Test para obtener lista de poderes (Con valores)")
	@Test
	void listarTodosLosPoderesPositivoTest() {
		// DEFINICIÓN DE VARIABLES DE ENTRADA
		Poder poder2 = Poder.builder().id(null).nombre("Electricidad").descripcion("Descripcion de Electricidad")
				.build();

		// COMPORTAMIENTO ESPERADO DEL CUERPO DEL MÉTODO
		given(poderRepositorio.findAll()).willReturn(List.of(poder, poder2));

		// LLAMADA A MÉTODO A TESTEAR
		List<Poder> poderes = poderServicio.listarTodosLosPoderes();

		// COMPROBACIONES DEL RESULTADO ESPERADO
		assertThat(poderes).isNotNull().hasSize(2);
	}

	@DisplayName("Test para obtener lista de poderes (Vacia)")
	@Test
	void listarTodosLosPoderesNegativoTest() {

		// COMPORTAMIENTO ESPERADO DEL CUERPO DEL MÉTODO
		given(poderRepositorio.findAll()).willReturn(Collections.emptyList());

		// LLAMADA A MÉTODO A TESTEAR
		List<Poder> poderes = poderServicio.listarTodosLosPoderes();

		// COMPROBACIONES DEL RESULTADO ESPERADO
		assertThat(poderes).isEmpty();
	}

	@DisplayName("Test para buscar un poder (Positivo)")
	@Test
	void buscarPoderPorIdPositivoTest() throws Exception {

		// DEFINICIÓN DE VARIABLES DE ENTRADA Y RESULTADOS
		Integer poderIdParam = Integer.valueOf("1");

		// COMPORTAMIENTO ESPERADO DEL CUERPO DEL MÉTODO
		given(poderRepositorio.findById(poderIdParam)).willReturn(Optional.of(poder));

		// LLAMADA A MÉTODO A TESTEAR
		Poder poderLeido = poderServicio.buscarPoderPorId(poder.getId()).get();

		// COMPROBACIONES DEL RESULTADO ESPERADO
		assertThat(poderLeido).isNotNull();
	}

	@DisplayName("Test para buscar un poder (Negativo)")
	@Test
	void buscarPoderPorIdNegativoTest() throws Exception {

		// DEFINICIÓN DE VARIABLES DE ENTRADA Y RESULTADOS
		Integer poderIdParam = Integer.valueOf("1");

		// COMPORTAMIENTO ESPERADO DEL CUERPO DEL MÉTODO
		given(poderRepositorio.findById(poderIdParam)).willReturn(null);

		// LLAMADA A MÉTODO A TESTEAR
		Optional<Poder> poderActual = poderServicio.buscarPoderPorId(poderIdParam);

		// COMPROBACIONES DEL RESULTADO ESPERADO
		assertThat(poderActual).isNull();
	}

	@DisplayName("Test para crear un Poder")
	@Test
	void crearPoderTest() throws Exception {
		// DEFINICIÓN DE VARIABLES DE ENTRADA Y RESULTADOS
		Integer poderIdParam = Integer.valueOf("1");

		// COMPORTAMIENTO ESPERADO DEL CUERPO DEL MÉTODO
		given(poderRepositorio.findByNombre(poder.getNombre())).willReturn(Optional.empty());

		given(poderRepositorio.save(poder)).willReturn(poder);

		// LLAMADA A MÉTODO A TESTEAR
		Poder nuevoPoder = poderServicio.crearPoder(poder);

		// COMPROBACIONES DEL RESULTADO ESPERADO
		assertThat(nuevoPoder).isNotNull();
		assertThat(nuevoPoder.getId()).isEqualTo(poderIdParam);
	}

	@DisplayName("Test para actualizar un Poder")
	@Test
	void actualizarPoderTest() {
		// COMPORTAMIENTO ESPERADO DEL CUERPO DEL MÉTODO
		given(poderRepositorio.save(poder)).willReturn(poder);
		poder.setNombre("Nuevo");
		poder.setDescripcion("Nueva");
		// LLAMADA A MÉTODO A TESTEAR
		Poder poderActualizado = poderServicio.actualizarPoder(poder);

		// COMPROBACIONES DEL RESULTADO ESPERADO
		assertThat(poderActualizado.getNombre()).isEqualTo("Nuevo");
		assertThat(poderActualizado.getDescripcion()).isEqualTo("Nueva");
	}

	@DisplayName("Test para eliminar un Poder")
	@Test
	void eliminarPoderTest() {
		// COMPORTAMIENTO ESPERADO DEL CUERPO DEL MÉTODO
		willDoNothing().given(poderRepositorio).delete(poder);

		// LLAMADA A MÉTODO A TESTEAR
		poderServicio.borrarPoder(poder);

		// COMPROBACIONES DEL RESULTADO ESPERADO
		verify(poderRepositorio, times(1)).delete(poder);
	}

}