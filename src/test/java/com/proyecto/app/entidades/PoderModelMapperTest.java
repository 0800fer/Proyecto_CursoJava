package com.proyecto.app.entidades;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

class PoderModelMapperTest {

	private ModelMapper modelMapper = new ModelMapper();

	@Test
	@DisplayName("Test mapper DTO a entidad")
	void dtoToEntidadTest() {
		PoderDTO dto = new PoderDTO(123, "Veloz", "Descripcion");

		Poder poder = modelMapper.map(dto, Poder.class);

		assertThat(poder.getId()).isEqualTo(dto.getId());
		assertThat(poder.getNombre()).isEqualTo(dto.getNombre());
		assertThat(poder.getDescripcion()).isEqualTo(dto.getDescripcion());
	}

	@Test
	@DisplayName("Test mapper entidad a DTO")
	void entidadToDtoTest() {
		Poder poder = new Poder(123, "Volar", "Descripcion", null);

		PoderDTO dto = modelMapper.map(poder, PoderDTO.class);

		assertThat(dto.getId()).isEqualTo(poder.getId());
		assertThat(dto.getNombre()).isEqualTo(poder.getNombre());
		assertThat(dto.getDescripcion()).isEqualTo(poder.getDescripcion());
	}
}
