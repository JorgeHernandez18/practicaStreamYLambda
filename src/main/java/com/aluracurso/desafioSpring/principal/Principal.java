package com.aluracurso.desafioSpring.principal;

import com.aluracurso.desafioSpring.model.Datos;
import com.aluracurso.desafioSpring.model.Libro;
import com.aluracurso.desafioSpring.service.ConsultaAPI;
import com.aluracurso.desafioSpring.service.ConvierteDatos;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner sc = new Scanner(System.in);
    private ConsultaAPI consultaAPI = new ConsultaAPI();
    private ConvierteDatos convertir = new ConvierteDatos();

    private final String URL_BASE = "https://gutendex.com/books/";

    public void mostrarMenu() {
        var json = consultaAPI.consultarApi(URL_BASE);
        var datos = convertir.convertirDatos(json, Datos.class);

        topTenLibros(datos);

        System.out.println("Digite el nombre del libro que desea buscar: ");
        var busqueda = sc.nextLine();
        var libro = busquedaPorTitulo(busqueda);

        if (libro.isPresent()) {
            System.out.println("Libro buscado: " + libro);
        } else {
            System.out.println("Libro no encontrado");
        }

        var est = obtenerEstadisticas(datos);


        System.out.println("Total de libros en la estadistica: " + est.getCount());
        System.out.println("Media de las descargas: " + est.getAverage());
        System.out.println("Libro más descargado: " + est.getMax());
        System.out.println("Libro menos descargado: " + est.getMin());
    }

    private IntSummaryStatistics obtenerEstadisticas(Datos datos) {
        return datos.libros().stream()
                .filter(l -> l.descargas() > 0)
                .collect(Collectors.summarizingInt(Libro::descargas));
    }

    private void topTenLibros(Datos datos) {
        System.out.println("Top 10 libros más descargados: ");
        datos.libros().stream()
                .sorted(Comparator.comparing(Libro::descargas).reversed())
                .limit(10)
                .map(l -> l.titulo().toUpperCase())
                .forEach(System.out::println);
        System.out.println("********************************************");
    }

    private Optional<Libro> busquedaPorTitulo(String busqueda) {
        var response = consultaAPI.consultarApi(URL_BASE + "?search=" + busqueda);
        var libro = convertir.convertirDatos(response, Datos.class);

        return libro.libros().stream()
                .filter(l -> l.titulo().toUpperCase().contains(busqueda.toUpperCase()))
                .findFirst();
    }
}
