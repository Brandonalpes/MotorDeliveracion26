package main.java.ar.edu.uns.cs.ed.tdas.tdaarbolbinario;

import main.java.ar.edu.uns.cs.ed.tdas.Position;

public class BTNodo<E> implements Position<E> {

    private BTNodo<E> padre;
    private E elemento;
    private BTNodo<E> HijoIzq;
    private BTNodo<E> HijoDer;

    public BTNodo(E e) {
        elemento = e;
        padre = null;
        HijoIzq = null;
        HijoDer = null;
    }

    public Position<E> getPadre() {
        return padre;
    }

    public void setPadre(Position<E> p) {
        padre = (BTNodo<E>) p;
    }

    public BTNodo<E> getHijoIzq() {
        return HijoIzq;
    }

    public void setHijoIzq(Position<E> p) {
        HijoIzq = (BTNodo<E>) p;
    }

    public BTNodo<E> getHijoDer() {
        return HijoDer;
    }

    public void setHijoDer(Position<E> p) {
        HijoDer = (BTNodo<E>) p;
    }

    @Override
    public E element() {
        return elemento;
    }

}
