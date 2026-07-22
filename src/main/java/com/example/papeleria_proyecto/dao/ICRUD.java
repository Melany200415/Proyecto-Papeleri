package com.example.papeleria_proyecto.dao;

import javafx.collections.ObservableList;

public interface ICRUD<T> {

    boolean insertar(T objeto);

    boolean actualizar(T objeto);

    boolean eliminar(int id);

    ObservableList<T> listarTodos();
}