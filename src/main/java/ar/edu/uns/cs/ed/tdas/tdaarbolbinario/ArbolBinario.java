package main.java.ar.edu.uns.cs.ed.tdas.tdaarbolbinario;

import java.util.Iterator;

import main.java.ar.edu.uns.cs.ed.tdas.Position;
import main.java.ar.edu.uns.cs.ed.tdas.excepciones.BoundaryViolationException;
import main.java.ar.edu.uns.cs.ed.tdas.excepciones.InvalidOperationException;
import main.java.ar.edu.uns.cs.ed.tdas.excepciones.InvalidPositionException;

public class ArbolBinario<E> implements BinaryTree<E> {
    BTNodo<E> raiz;
    int tamaño;

    @Override
    public Position<E> left(Position<E> v) {
        BTNodo<E> nodo = checkPosition(v);
        if (nodo.getHijoIzq() == null) {
            throw new BoundaryViolationException("El nodo no tiene hijo izquierdo");
        }
        return nodo.getHijoIzq();
    }

    @Override
    public Position<E> right(Position<E> v) {
        BTNodo<E> nodo = checkPosition(v);
        if (nodo.getHijoDer() == null) {
            throw new BoundaryViolationException("El nodo no tiene hijo derecho");
        }
        return nodo.getHijoDer();
    }

    @Override
    public boolean hasLeft(Position<E> v) {
        BTNodo<E> nodo = checkPosition(v);
        return nodo.getHijoIzq() != null;
    }

    @Override
    public boolean hasRight(Position<E> v) {
        BTNodo<E> nodo = checkPosition(v);
        return nodo.getHijoDer() != null;
    }

    @Override
    public Position<E> addLeft(Position<E> v, E r) {
        BTNodo<E> nodo = checkPosition(v);
        if (nodo.getHijoIzq() != null) {
            throw new InvalidOperationException("El nodo ya tiene hijo izquierdo");
        }
        BTNodo<E> nuevo = new BTNodo<>(r);
        nuevo.setPadre(nodo);
        nodo.setHijoIzq(nuevo);
        tamaño++;
        return nuevo;
    }

    @Override
    public Position<E> addRight(Position<E> v, E r) {
        BTNodo<E> nodo = checkPosition(v);
        if (nodo.getHijoDer() != null) {
            throw new InvalidOperationException("El nodo ya tiene hijo derecho");
        }
        BTNodo<E> nuevo = new BTNodo<>(r);
        nuevo.setPadre(nodo);
        nodo.setHijoDer(nuevo);
        tamaño++;
        return nuevo;
    }

    @Override
    public void attach(Position<E> v, BinaryTree<E> T1, BinaryTree<E> T2) {
        BTNodo<E> hoja = checkPosition(v);
        if (hoja.getHijoIzq() != null || hoja.getHijoDer() != null) {
            throw new InvalidPositionException("La posición dada no es una hoja");
        }
        if (T1 != null && !T1.isEmpty()) {
            BTNodo<E> raizT1 = (BTNodo<E>) ((ArbolBinario<E>) T1).raiz;
            raizT1.setPadre(hoja);
            hoja.setHijoIzq(raizT1);
            tamaño += T1.size();
            ((ArbolBinario<E>) T1).raiz = null;
            ((ArbolBinario<E>) T1).tamaño = 0;
        }
        if (T2 != null && !T2.isEmpty()) {
            BTNodo<E> raizT2 = (BTNodo<E>) ((ArbolBinario<E>) T2).raiz;
            raizT2.setPadre(hoja);
            hoja.setHijoDer(raizT2);
            tamaño += T2.size();
            ((ArbolBinario<E>) T2).raiz = null;
            ((ArbolBinario<E>) T2).tamaño = 0;
        }
    }

    @Override
    public int size() {
        return tamaño;
    }

    @Override
    public boolean isEmpty() {
        return tamaño == 0;
    }

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException("Método inválido para BinaryTree");
    }

    @Override
    public Iterable<Position<E>> positions() {
        throw new UnsupportedOperationException("Método inválido para BinaryTree");
    }

    @Override
    public E replace(Position<E> v, E e) {
        throw new UnsupportedOperationException("Método inválido para BinaryTree");
    }

    @Override
    public Position<E> root() {
        throw new UnsupportedOperationException("Método inválido para BinaryTree");
    }

    @Override
    public Position<E> parent(Position<E> v) {
        throw new UnsupportedOperationException("Método inválido para BinaryTree");
    }

    @Override
    public Iterable<Position<E>> children(Position<E> v) {
        throw new UnsupportedOperationException("Método inválido para BinaryTree");
    }

    @Override
    public boolean isInternal(Position<E> v) {
        throw new UnsupportedOperationException("Método inválido para BinaryTree");
    }

    @Override
    public boolean isExternal(Position<E> v) {
        throw new UnsupportedOperationException("Método inválido para BinaryTree");
    }

    @Override
    public boolean isRoot(Position<E> v) {
        throw new UnsupportedOperationException("Método inválido para BinaryTree");
    }

    @Override
    public void createRoot(E e) {
        throw new UnsupportedOperationException("Método inválido para BinaryTree");
    }

    @Override
    public Position<E> addFirstChild(Position<E> p, E e) {
        throw new UnsupportedOperationException("Método inválido para BinaryTree");
    }

    @Override
    public Position<E> addLastChild(Position<E> p, E e) {
        throw new UnsupportedOperationException("Método inválido para BinaryTree");
    }

    @Override
    public Position<E> addBefore(Position<E> p, Position<E> rb, E e) {
        throw new UnsupportedOperationException("Método inválido para BinaryTree");
    }

    @Override
    public Position<E> addAfter(Position<E> p, Position<E> lb, E e) {
        throw new UnsupportedOperationException("Método inválido para BinaryTree");
    }

    @Override
    public void removeExternalNode(Position<E> p) {
        throw new UnsupportedOperationException("Método inválido para BinaryTree");
    }

    @Override
    public void removeInternalNode(Position<E> p) {
        throw new UnsupportedOperationException("Método inválido para BinaryTree");
    }

    @Override
    public void removeNode(Position<E> p) {
        throw new UnsupportedOperationException("Método inválido para BinaryTree");
    }

    private BTNodo<E> checkPosition(Position<E> p) {
        BTNodo<E> toRet;
        try {
            toRet = (BTNodo<E>) p;
            if (toRet.getPadre() == toRet) {
                throw new InvalidPositionException("Posición inválida");
            }
        } catch (ClassCastException e) {
            throw new ClassCastException("La posición no pertenece a un árbol binario");
        }
        return toRet;
    }
}
