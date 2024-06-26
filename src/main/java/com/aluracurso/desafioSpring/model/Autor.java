package com.aluracurso.desafioSpring.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Autor(@JsonAlias("birth_year") String fechaNacimiento,
                    @JsonAlias("name") String nombre) {
}
