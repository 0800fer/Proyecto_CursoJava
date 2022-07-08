package com.proyecto.app.entidades;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

class UniversoModelMapperTest {

	private ModelMapper modelMapper = new ModelMapper();

	@Test
	@DisplayName("Test mapper DTO a entidad")
	void dtoToEntidadTest() {
		UniversoDTO dto = new UniversoDTO(123, "Marvel", "Descripcion");

		Universo universo = modelMapper.map(dto, Universo.class);

		assertThat(universo.getId()).isEqualTo(dto.getId());
		assertThat(universo.getNombre()).isEqualTo(dto.getNombre());
		assertThat(universo.getDescripcion()).isEqualTo(dto.getDescripcion());
	}

	@Test
	@DisplayName("Test mapper entidad a DTO")
	void entidadToDtoTest() {
		Universo universo = new Universo(123, "Marvel", "Descripcion");

		UniversoDTO dto = modelMapper.map(universo, UniversoDTO.class);

		assertThat(dto.getId()).isEqualTo(universo.getId());
		assertThat(dto.getNombre()).isEqualTo(universo.getNombre());
		assertThat(dto.getDescripcion()).isEqualTo(universo.getDescripcion());
	}
}
