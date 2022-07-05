package com.proyecto.app.entidades;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuperheroeDTO {

	private Integer id;
	private String nombre;
	private String historia;
	private Boolean estaVivo = true;
	private String universo;

	public SuperheroeDTO(Superheroe superheroe) {
		this.id = superheroe.getId();
		this.nombre = superheroe.getNombre();
		this.historia = superheroe.getHistoria();
		this.estaVivo = superheroe.getEstaVivo();
//		this.universo = superheroe.getUniverso().getNombre();
	}

}
