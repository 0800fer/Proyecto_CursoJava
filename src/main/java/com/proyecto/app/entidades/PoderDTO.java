package com.proyecto.app.entidades;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PoderDTO {

	private Integer id;
	private String nombre;
	private String descripcion;
}
