package com.proyecto.app.repositorios;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.proyecto.app.entidades.Poder;
import com.proyecto.app.repositorios.IPoderRepositorio;

/**
 * 
 * Test: comprueba que el repositorio de la entidad PODER está operativo
 *
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PoderRepositorioTest {
	@Autowired
	IPoderRepositorio repo;

	@Test
	void testRepositorioConConstructorVacio() {
		Poder poder = new Poder();
		repo.save(poder);

		Assertions.assertNotNull(poder.getId());
	}

	@Test
	void testRepositorioConConstructorLleno() {
		Poder poder = new Poder(1, "Electricidad", "Descripcion");
		repo.save(poder);

		Assertions.assertNotNull(repo.findAll());
	}

}
