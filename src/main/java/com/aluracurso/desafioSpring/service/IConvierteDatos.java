package com.aluracurso.desafioSpring.service;

public interface IConvierteDatos {
    <T> T convertirDatos(String json, Class<T> clase);
}
