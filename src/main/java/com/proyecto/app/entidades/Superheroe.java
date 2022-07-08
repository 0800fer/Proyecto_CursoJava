package com.proyecto.app.entidades;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.annotations.ColumnDefault;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * Definición de la entidad que representa un PODER
 * 
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "superheroes")
public class Superheroe {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "nombre", unique = true)
	@Size(max = 50, message = "Longitud máxima 50 caracteres")
	private String nombre;

	@Column(name = "historia")
	@Size(max = 512, message = "Longitud máxima 512 caracteres")
	private String historia;

	@Builder.Default
	@Column(name = "esta_vivo")
	@ColumnDefault("true")
	private Boolean estaVivo = true;

	@Column(name = "universo_id", nullable = false)
	private Integer universoId;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "universo_id", insertable = false, updatable = false)
	@JsonProperty(access = Access.WRITE_ONLY)
	private Universo universo;

	@ManyToMany(cascade = { CascadeType.ALL })
	@JoinTable(name = "heroes_poderes", joinColumns = @JoinColumn(name = "heroe_id"), inverseJoinColumns = @JoinColumn(name = "poder_id"))
	@JsonIgnore
	@Builder.Default
	private Set<Poder> poderes = new HashSet<>();

}