package main.java.ar.edu.uns.cs.ed.tdas.tdalista;

import main.java.ar.edu.uns.cs.ed.tdas.excepciones.BoundaryViolationException;
import main.java.ar.edu.uns.cs.ed.tdas.excepciones.EmptyListException;
import main.java.ar.edu.uns.cs.ed.tdas.excepciones.InvalidPositionException;
import java.util.Iterator;

import main.java.ar.edu.uns.cs.ed.ElementIterator;
import main.java.ar.edu.uns.cs.ed.tdas.DNodo;
import main.java.ar.edu.uns.cs.ed.tdas.Position;

public class ListaDoblementeEnlazada<E> implements PositionList<E> {
    private int size;
    private DNodo<E> header;
    private DNodo<E> trailer;

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    public Position<E> first() {
        return header.getNext();
    }

    @Override
    public Position<E> last() {
        return trailer.getPrev();
    }

    public Position<E> next(Position<E> p) {
        DNodo<E> aux = checkPosition(p);
        if (!(p instanceof DNodo)) {
            throw new InvalidPositionException("El parámetro no es un DNodo");
        }
        return aux.getNext();
    }

    public Position<E> prev(Position<E> p) {
        DNodo<E> aux = checkPosition(p);
        if (!(p instanceof DNodo)) {
            throw new InvalidPositionException("El parámetro no es un DNodo");
        }
        return aux.getPrev();
    }

    @Override
    public void addFirst(E element) {
        header.setNext(new DNodo(element));
        size++;
    }

    @Override
    public void addLast(E element) {
        trailer.setPrev(new DNodo(element));
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
        DNodo<E> aux = (DNodo) p;
        if (!(p instanceof DNodo)) {
            throw new IllegalArgumentException("Posición inválida");
        }
        if (aux.element() == null || (aux.getPrev() == null && aux.getNext() == null)) {
            throw new IllegalArgumentException("Posición inválida");
        }
        return aux;
    }

}
