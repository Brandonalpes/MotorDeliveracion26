package ar.edu.uns.cs.ed.tdas.tdalista;

import ar.edu.uns.cs.ed.tdas.excepciones.BoundaryViolationException;
import ar.edu.uns.cs.ed.tdas.excepciones.EmptyListException;
import ar.edu.uns.cs.ed.tdas.excepciones.InvalidPositionException;
import java.util.Iterator;

import ar.edu.uns.cs.ed.ElementIterator;
import ar.edu.uns.cs.ed.tdas.DNodo;
import ar.edu.uns.cs.ed.tdas.Position;

public class ListaDoblementeEnlazada<E> implements PositionList<E> {
    private int size;
    private DNodo<E> header;
    private DNodo<E> trailer;

    public ListaDoblementeEnlazada() {
        header = new DNodo<E>(null);
        trailer = new DNodo<E>(null);
        header.setNext(trailer);
        trailer.setPrev(header);
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    public Position<E> first() {
        if (isEmpty()) {
            throw new EmptyListException("Lista vacía");
        }
        return header.getNext();
    }

    @Override
    public Position<E> last() {
        if (isEmpty()) {
            throw new EmptyListException("Lista vacía");
        }
        return trailer.getPrev();
    }

    public Position<E> next(Position<E> p) {
        DNodo<E> aux = checkPosition(p);
        if (aux.getNext() == trailer) {
            throw new BoundaryViolationException("No hay siguiente posición");
        }
        return aux.getNext();
    }

    public Position<E> prev(Position<E> p) {
        DNodo<E> aux = checkPosition(p);
        if (aux.getPrev() == header) {
            throw new BoundaryViolationException("No hay posición previa");
        }
        return aux.getPrev();
    }

    @Override
    public void addFirst(E element) {
        DNodo<E> nuevo = new DNodo<E>(element);
        DNodo<E> primero = header.getNext();
        nuevo.setNext(primero);
        nuevo.setPrev(header);
        header.setNext(nuevo);
        primero.setPrev(nuevo);
        size++;
    }

    @Override
    public void addLast(E element) {
        DNodo<E> nuevo = new DNodo<E>(element);
        DNodo<E> ultimo = trailer.getPrev();
        nuevo.setNext(trailer);
        nuevo.setPrev(ultimo);
        ultimo.setNext(nuevo);
        trailer.setPrev(nuevo);
        size++;
    }

    public void addAfter(Position<E> p, E element) {
        DNodo<E> pos = checkPosition(p);
        DNodo<E> agregado = new DNodo<E>(element);
        DNodo<E> siguiente = pos.getNext();
        pos.setNext(agregado);
        siguiente.setPrev(agregado);
        agregado.setPrev(pos);
        agregado.setNext(siguiente);
        size++;
    }

    public void addBefore(Position<E> p, E element) {
        DNodo<E> pos = checkPosition(p);
        DNodo<E> agregado = new DNodo<E>(element);
        DNodo<E> anterior = pos.getPrev();
        pos.setPrev(agregado);
        anterior.setNext(agregado);
        agregado.setNext(pos);
        agregado.setPrev(anterior);
        size++;
    }

    public E remove(Position<E> p) {
        DNodo<E> toRet = checkPosition(p);
        DNodo<E> anterior = toRet.getPrev();
        DNodo<E> siguiente = toRet.getNext();
        anterior.setNext(siguiente);
        siguiente.setPrev(anterior);
        toRet.setNext(null);
        toRet.setPrev(null);
        size--;
        return toRet.element();
    }

    public E set(Position<E> p, E element) {
        DNodo<E> pos = checkPosition(p);
        DNodo<E> toSet = new DNodo<E>(element);
        DNodo<E> ant = pos.getPrev();
        DNodo<E> sig = pos.getNext();
        ant.setNext(toSet);
        sig.setPrev(toSet);
        toSet.setPrev(ant);
        toSet.setNext(sig);
        pos.setPrev(null);
        pos.setNext(null);
        return pos.element();
    }

    @Override
    public Iterator<E> iterator() {
        return new ElementIterator<E>(this);
    }

    @Override
    public Iterable<Position<E>> positions() {
        PositionList<Position<E>> pl = new ListaDoblementeEnlazada<Position<E>>();
        if (size != 0) {
            DNodo<E> n = header.getNext();
            while (n != trailer) {
                pl.addLast(n);
                n = n.getNext();
            }
        }
        return pl;
    }

    public DNodo<E> checkPosition(Position<E> p) {
        if (p == null || !(p instanceof DNodo)) {
            throw new InvalidPositionException("Posición inválida");
        }
        DNodo<E> aux = (DNodo<E>) p;
        if (aux.getPrev() == null || aux.getNext() == null) {
            throw new InvalidPositionException("Posición inválida");
        }
        return aux;
    }

}
