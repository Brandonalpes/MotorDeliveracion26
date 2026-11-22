package main.java.ar.edu.uns.cs.ed.tdas.tdaarbol;

import main.java.ar.edu.uns.cs.ed.tdas.excepciones.InvalidPositionException;
import main.java.ar.edu.uns.cs.ed.tdas.tdalista.PositionList;
import main.java.ar.edu.uns.cs.ed.tdas.Position;
import main.java.ar.edu.uns.cs.ed.tdas.excepciones.EmptyListException;

public class TNodo<E> implements Position<E> {
    private E elemento;
    TNodo<E> padre;
    PositionList<TNodo<E>> hijos;

    public TNodo(E elem) {
        elemento = elem;
        padre = null;
        hijos = null;
    }

    public E element() {
        return elemento;
    }

    public Position<E> getPadre() {
        return padre;
    }

    public PositionList<TNodo<E>> getHijos() {
        return hijos;
    }

    public void setPadre(Position<E> p) {
        try {
            padre = (TNodo) p;
        } catch (ClassCastException e) {
            throw new ClassCastException("Parámetro inválido");
        }
    }

    public void setHijos(PositionList<TNodo<E>> l) {
        hijos = l;
    }
}
