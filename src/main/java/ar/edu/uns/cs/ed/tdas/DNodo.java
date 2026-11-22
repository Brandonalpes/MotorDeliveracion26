package ar.edu.uns.cs.ed.tdas;

public class DNodo<E> implements Position<E> {
    private E element;
    private DNodo<E> next;
    private DNodo<E> prev;

    public DNodo(E e) {
        element = e;
        next = null;
        prev = null;
    }

    @Override
    public E element() {
        return element;
    }

    public DNodo getNext() {
        return next;
    }

    public DNodo getPrev() {
        return prev;
    }

    public void setNext(DNodo<E> d) {
        next = d;
    }

    public void setPrev(DNodo<E> d) {
        prev = d;
    }
}
