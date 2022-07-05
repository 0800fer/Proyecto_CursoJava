package com.proyecto.app.repositorios;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.proyecto.app.entidades.Superheroe;

/**
 * 
 * Test: comprueba que el repositorio de la entidad PODER está operativo
 *
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SuperheroeRepositorioTest {
	@Autowired
	ISuperheroeRepositorio repo;

	@Test
	@DisplayName("Test para confirmar operatividad del repositorio con constructor vacio")
	void testRepositorioConConstructorVacio() {
		Superheroe superheroe = new Superheroe();
		repo.save(superheroe);

		Assertions.assertNotNull(superheroe.getId());
	}

	@Test
	@DisplayName("Test para confirmar operatividad del repositorio con constructor lleno")
	void testRepositorioConConstructorLleno() {
		Superheroe superheroe = new Superheroe(1, "Batman", "Descripcion", true);
		repo.save(superheroe);

		Assertions.assertNotNull(repo.findAll());
	}

}