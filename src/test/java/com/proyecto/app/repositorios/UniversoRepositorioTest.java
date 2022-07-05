package com.proyecto.app.repositorios;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.proyecto.app.entidades.Universo;

/**
 * 
 * Test: comprueba que el repositorio de la entidad Universo está operativo
 *
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UniversoRepositorioTest {
	@Autowired
	IUniversoRepositorio repo;

	@Test
	@DisplayName("Test para confirmar operatividad del repositorio con constructor vacio")
	void testRepositorioConConstructorVacio() {
		Universo universo = new Universo();
		repo.save(universo);

		Assertions.assertNotNull(universo.getId());
	}

	@Test
	@DisplayName("Test para confirmar operatividad del repositorio con constructor lleno")
	void testRepositorioConConstructorLleno() {
		Universo universo = new Universo(1, "Marvel", "Descripcion");
		repo.save(universo);

		Assertions.assertNotNull(repo.findAll());
	}

}