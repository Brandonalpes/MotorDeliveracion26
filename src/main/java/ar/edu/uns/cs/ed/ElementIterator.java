package ar.edu.uns.cs.ed;

import ar.edu.uns.cs.ed.tdas.excepciones.InvalidPositionException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import ar.edu.uns.cs.ed.tdas.Position;
import ar.edu.uns.cs.ed.tdas.tdalista.PositionList;

public class ElementIterator<E> implements Iterator<E> {
    protected PositionList<E> list;
    protected Position<E> cursor;

    public ElementIterator(PositionList<E> l) {
        list = l;
        if (list.isEmpty()) {
            cursor = null;
        } else {
            cursor = list.first();
        }
    }

    @Override
    public boolean hasNext() {
        return cursor != null;
    }

    @Override
    public E next() throws NoSuchElementException {
        if (cursor == null) {
            throw new NoSuchElementException("No existe sigueinte elemento");
        }
        E toRet = cursor.element();
        try {
            cursor = (cursor == list.last()) ? null : list.next(cursor);
        } catch (InvalidPositionException e) {
            e.printStackTrace();
        }
        return toRet;
    }
}
