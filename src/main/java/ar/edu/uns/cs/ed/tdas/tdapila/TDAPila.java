package ar.edu.uns.cs.ed.tdas.tdapila;

import ar.edu.uns.cs.ed.tdas.excepciones.EmptyStackException;

public class TDAPila<E> implements Stack<E> {
    private int size;
    private E[] pila;

    public TDAPila() {
        pila = (E[]) new Object[10];
    }

    public TDAPila(int i) {
        pila = (E[]) new Object[i];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public E top() throws EmptyStackException {
        if (isEmpty()) {
            throw new EmptyStackException("Pila vacía");
        }
        return pila[size - 1];
    }

    @Override
    public void push(E element) {
        if (size == pila.length) {
            reOrdenar();
        }
        pila[size] = element;
        size++;
    }

    @Override
    public E pop() throws EmptyStackException {
        if (isEmpty()) {
            throw new EmptyStackException("Lista vacía");
        }

        E aux = pila[size - 1];
        pila[size - 1] = null;
        size--;
        return aux;
    }

    private void reOrdenar() {
        E[] nuevaPila = (E[]) new Object[pila.length * 2];
        for (int i = 0; i < pila.length; i++) {
            nuevaPila[i] = pila[i];
        }
        pila = nuevaPila;
    }

}
