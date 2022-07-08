package com.proyecto.app.entidades;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SuperheroeDTO {

	private Integer id;
	private String nombre;
	private String historia;
	@Builder.Default
	private Boolean estaVivo = true;
	private Integer universoId;
	@Builder.Default
	private List<String> poderes = new ArrayList<>();

}
