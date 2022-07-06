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

import com.proyecto.app.entidades.Superheroe;
import com.proyecto.app.repositorios.ISuperheroeRepositorio;

@ExtendWith(MockitoExtension.class) // -> JUNIT 5
class SuperheroeServicioTest {

	@InjectMocks
	private SuperheroeServicioImpl superheroeServicio;

	@Mock
	private ISuperheroeRepositorio superheroeRepositorio;

	private Superheroe superheroe;

	@BeforeEach
	public void setup() {
		superheroe = Superheroe.builder().id(1).nombre("Batman").historia("Descripcion de Batman").build();
	}

	@DisplayName("Test para obtener lista de todos los superheroes (Con valores)")
	@Test
	void listarTodosLosSuperheroeesPositivoTest() {
		// DEFINICIÓN DE VARIABLES DE ENTRADA
		Superheroe superheroe2 = Superheroe.builder().id(null).nombre("Superman").historia("Descripcion de Disney")
				.build();

		// COMPORTAMIENTO ESPERADO DEL CUERPO DEL MÉTODO
		given(superheroeRepositorio.findAll()).willReturn(List.of(superheroe, superheroe2));

		// LLAMADA A MÉTODO A TESTEAR
		List<Superheroe> superheroes = superheroeServicio.listarTodosLosSuperheroes();

		// COMPROBACIONES DEL RESULTADO ESPERADO
		assertThat(superheroes).isNotNull().hasSize(2);
	}

	@DisplayName("Test para obtener lista de todos los superheroes (Vacia)")
	@Test
	void listarTodosLosSuperheroeesNegativoTest() {

		// COMPORTAMIENTO ESPERADO DEL CUERPO DEL MÉTODO
		given(superheroeRepositorio.findAll()).willReturn(Collections.emptyList());

		// LLAMADA A MÉTODO A TESTEAR
		List<Superheroe> superheroes = superheroeServicio.listarTodosLosSuperheroes();

		// COMPROBACIONES DEL RESULTADO ESPERADO
		assertThat(superheroes).isEmpty();
	}

	@DisplayName("Test para buscar un superheroe (Positivo)")
	@Test
	void buscarSuperheroePorIdPositivoTest() throws Exception {

		// DEFINICIÓN DE VARIABLES DE ENTRADA Y RESULTADOS
		Integer superheroeIdParam = Integer.valueOf("1");

		// COMPORTAMIENTO ESPERADO DEL CUERPO DEL MÉTODO
		given(superheroeRepositorio.findById(superheroeIdParam)).willReturn(Optional.of(superheroe));

		// LLAMADA A MÉTODO A TESTEAR
		Superheroe superheroeLeido = superheroeServicio.buscarSuperheroePorId(superheroe.getId()).get();

		// COMPROBACIONES DEL RESULTADO ESPERADO
		assertThat(superheroeLeido).isNotNull();
	}

	@DisplayName("Test para buscar un superheroe (Negativo)")
	@Test
	void buscarSuperheroePorIdNegativoTest() throws Exception {

		// DEFINICIÓN DE VARIABLES DE ENTRADA Y RESULTADOS
		Integer superheroeIdParam = Integer.valueOf("1");

		// COMPORTAMIENTO ESPERADO DEL CUERPO DEL MÉTODO
		given(superheroeRepositorio.findById(superheroeIdParam)).willReturn(null);

		// LLAMADA A MÉTODO A TESTEAR
		Optional<Superheroe> superheroeActual = superheroeServicio.buscarSuperheroePorId(superheroeIdParam);

		// COMPROBACIONES DEL RESULTADO ESPERADO
		assertThat(superheroeActual).isNull();
	}

	@DisplayName("Test para crear un Superheroe")
	@Test
	void crearSuperheroeTest() throws Exception {
		// DEFINICIÓN DE VARIABLES DE ENTRADA Y RESULTADOS
		Integer superheroeIdParam = Integer.valueOf("1");

		// COMPORTAMIENTO ESPERADO DEL CUERPO DEL MÉTODO
		given(superheroeRepositorio.findByNombre(superheroe.getNombre())).willReturn(Optional.empty());

		given(superheroeRepositorio.save(superheroe)).willReturn(superheroe);

		// LLAMADA A MÉTODO A TESTEAR
		Superheroe nuevoSuperheroe = superheroeServicio.crearSuperheroe(superheroe);

		// COMPROBACIONES DEL RESULTADO ESPERADO
		assertThat(nuevoSuperheroe).isNotNull();
		assertThat(nuevoSuperheroe.getId()).isEqualTo(superheroeIdParam);
	}

	@DisplayName("Test para actualizar un Superheroe")
	@Test
	void actualizarSuperheroeTest() {
		// DEFINICIÓN DE VARIABLES DE ENTRADA Y RESULTADOS

		// COMPORTAMIENTO ESPERADO DEL CUERPO DEL MÉTODO
		given(superheroeRepositorio.save(superheroe)).willReturn(superheroe);
		superheroe.setNombre("Nuevo");
		superheroe.setHistoria("Nueva");
		// LLAMADA A MÉTODO A TESTEAR
		Superheroe superheroeActualizado = superheroeServicio.actualizarSuperheroe(superheroe);

		// COMPROBACIONES DEL RESULTADO ESPERADO
		assertThat(superheroeActualizado.getNombre()).isEqualTo("Nuevo");
		assertThat(superheroeActualizado.getHistoria()).isEqualTo("Nueva");
	}

	@DisplayName("Test para eliminar un Superheroe")
	@Test
	void eliminarSuperheroeTest() {

		// COMPORTAMIENTO ESPERADO DEL CUERPO DEL MÉTODO
		willDoNothing().given(superheroeRepositorio).delete(superheroe);

		// LLAMADA A MÉTODO A TESTEAR
		superheroeServicio.borrarSuperheroe(superheroe);

		// COMPROBACIONES DEL RESULTADO ESPERADO
		verify(superheroeRepositorio, times(1)).delete(superheroe);
	}

}