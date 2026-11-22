package main.java.ar.edu.uns.cs.ed.tdas.tdaarbol;

import java.util.Iterator;

import main.java.ar.edu.uns.cs.ed.tdas.Position;
import main.java.ar.edu.uns.cs.ed.*;
import main.java.ar.edu.uns.cs.ed.tdas.tdalista.*;
import main.java.ar.edu.uns.cs.ed.tdas.excepciones.BoundaryViolationException;
import main.java.ar.edu.uns.cs.ed.tdas.excepciones.EmptyTreeException;
import main.java.ar.edu.uns.cs.ed.tdas.excepciones.InvalidOperationException;
import main.java.ar.edu.uns.cs.ed.tdas.excepciones.InvalidPositionException;
import main.java.ar.edu.uns.cs.ed.tdas.excepciones.NullPointerException;

public class TDAÁrbol<E> implements Tree<E> {
    private int tamaño;
    private TNodo<E> raíz;

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
        PositionList<E> elementos = new ListaDoblementeEnlazada<E>();
        for (Position<E> pos : positions()) {
            elementos.addLast(pos.element());
        }
        return elementos.iterator();
    }

    @Override
    public Iterable<Position<E>> positions() {
        PositionList<Position<E>> lista = new ListaDoblementeEnlazada<Position<E>>();
        if (!isEmpty()) {
            preorden(raíz, lista);
        }
        return lista;
    }

    private void preorden(TNodo<E> nodo, PositionList<Position<E>> lista) {
        lista.addLast(nodo);
        if (nodo.getHijos() != null) {
            for (TNodo<E> hijo : nodo.getHijos()) {
                preorden(hijo, lista);
            }
        }
    }

    @Override
    public E replace(Position<E> v, E e) {
        TNodo<E> nodo = checkPosition(v);
        E elementoAnterior = nodo.element();
        nodo = new TNodo<E>(e);
        return elementoAnterior;
    }

    @Override
    public Position<E> root() {
        if (raíz == null) {
            throw new EmptyTreeException("Árbol vacío");
        }
        return raíz;
    }

    @Override
    public Position<E> parent(Position<E> v) {
        TNodo<E> aux;
        try {
            aux = (TNodo) v;
            if (v == null) {
                throw new InvalidPositionException("El nodo provisto es nulo");
            }
            if (aux == raíz) {
                throw new BoundaryViolationException("La posición dada no tiene padre (es raíz)");
            }
        } catch (ClassCastException e) {
            throw new InvalidPositionException("La posición no corresponde a un nodo de un árbol");
        }
        return aux.getPadre();
    }

    @Override
    public Iterable<Position<E>> children(Position<E> v) {
        TNodo<E> aux = checkPosition(v);
        PositionList<Position<E>> resultado = new ListaDoblementeEnlazada<Position<E>>();
        if (aux.getHijos() != null) {
            for (TNodo<E> hijo : aux.getHijos()) {
                resultado.addLast(hijo);
            }
        }
        return resultado;
    }

    @Override
    public boolean isInternal(Position<E> v) {
        TNodo<E> aux;
        boolean LoEs = false;
        try {
            if (v == null) {
                throw new InvalidPositionException("Parámetro nulo");
            }
            aux = (TNodo) v;
            if (aux.getHijos() != null) {
                LoEs = true;
            }
        } catch (ClassCastException e) {
            throw new ClassCastException("El parámetro provisto no pertenece al nodo de un árbol");
        }
        return LoEs;
    }

    public boolean isExternal(Position<E> v) {
        TNodo<E> aux;
        boolean LoEs = false;
        try {
            if (v == null) {
                throw new InvalidPositionException("Parámetro nulo");
            }
            aux = (TNodo) v;
            if (aux.getHijos() == null) {
                LoEs = true;
            }
        } catch (ClassCastException e) {
            throw new ClassCastException("El parámetro provisto no pertenece al nodo de un árbol");
        }
        return LoEs;
    }

    public boolean isRoot(Position<E> v) {
        TNodo<E> aux;
        boolean LoEs = false;
        try {
            if (v == null) {
                throw new InvalidPositionException("Posición nula");
            }
            aux = (TNodo) v;
            if (aux == raíz) {
                LoEs = true;
            }
        } catch (ClassCastException e) {
            throw new ClassCastException("El parámetro provisto no pertenece al nodo de un árbol");
        }
        return LoEs;
    }

    @Override
    public void createRoot(E e) {
        if (raíz != null) {
            throw new InvalidOperationException("El árbol ya tiene raíz");
        }
        raíz = new TNodo(e);
        tamaño++;
    }

    public Position<E> addFirstChild(Position<E> p, E e) {
        TNodo aux;
        TNodo<E> nuevo;
        try {
            if (p == null || tamaño == 0) {
                throw new InvalidPositionException("Posición inválida");
            }
            if (e == null) {
                throw new InvalidOperationException("No se pueden crear nodos con nulos cómo elemento");
            }
            aux = (TNodo) p;
            nuevo = new TNodo(e);
            if (aux.getHijos() == null) {
                aux.setHijos(new ListaDoblementeEnlazada<TNodo<E>>());
                aux.getHijos().addFirst(nuevo);
            } else {
                aux.getHijos().addFirst(nuevo);
            }
        } catch (ClassCastException error) {
            throw new ClassCastException("El parametro provisto no pertenece al nodo de un árbol");
        }
        if (nuevo == null) {
            throw new NullPointerException("Posición no creada");
        }
        tamaño++;
        return nuevo;
    }

    public Position<E> addLastChild(Position<E> p, E e) {
        TNodo aux;
        TNodo<E> nuevo;
        try {
            if (p == null || tamaño == 0) {
                throw new InvalidPositionException("Posición inválida");
            }
            if (e == null) {
                throw new InvalidOperationException("No se pueden crear nodos con nulos cómo elemento");
            }
            aux = (TNodo) p;
            nuevo = new TNodo(e);
            if (aux.getHijos() == null) {
                aux.setHijos(new ListaDoblementeEnlazada<TNodo<E>>());
                aux.getHijos().addLast(nuevo);
            } else {
                aux.getHijos().addLast(nuevo);
            }
        } catch (ClassCastException error) {
            throw new ClassCastException("El parametro provisto no pertenece al nodo de un árbol");
        }
        if (nuevo == null) {
            throw new NullPointerException("Posición no creada");
        }
        tamaño++;
        return nuevo;
    }

    public void removeNode(Position<E> p) {
        if (isExternal(p)) {
            this.removeExternalNode(p);
        } else {
            this.removeInternalNode(p);
        }
    }

    private TNodo<E> checkPosition(Position<E> p) throws ClassCastException {
        TNodo<E> toRet;
        try {
            toRet = (TNodo) p;
            if (p == null) {
                throw new InvalidPositionException("Posición provista inválida");
            }
            if (p.element() == null) {
                throw new InvalidPositionException("Posición con elemento nulo");
            }

            if (toRet.getPadre() == toRet) {
                throw new InvalidPositionException("Nodo removido");
            }
        } catch (ClassCastException e) {
            throw new ClassCastException("Posición incompatible");
        }
        return toRet;
    }

    @Override
    public Position<E> addBefore(Position<E> p, Position<E> rb, E e) {
        TNodo<E> padre = checkPosition(p);
        TNodo<E> hermano = checkPosition(rb);
        if (padre.getHijos() == null) {
            throw new InvalidPositionException("El padre no tiene hijos");
        }
        TNodo<E> nuevo = new TNodo<>(e);
        nuevo.setPadre(padre);
        PositionList<TNodo<E>> hijosNuevos = new ListaDoblementeEnlazada<>();
        boolean encontrado = false;
        for (TNodo<E> hijo : padre.getHijos()) {
            if (hijo == hermano) {
                hijosNuevos.addLast(nuevo);
                encontrado = true;
            }
            hijosNuevos.addLast(hijo);
        }
        if (!encontrado) {
            throw new InvalidPositionException("El hermano no es hijo del padre dado");
        }
        padre.setHijos(hijosNuevos);
        tamaño++;
        return nuevo;
    }

    @Override
    public Position<E> addAfter(Position<E> p, Position<E> lb, E e) {
        TNodo<E> padre = checkPosition(p);
        TNodo<E> hermano = checkPosition(lb);
        if (padre.getHijos() == null) {
            throw new InvalidPositionException("El padre no tiene hijos");
        }
        TNodo<E> nuevo = new TNodo<>(e);
        nuevo.setPadre(padre);
        PositionList<TNodo<E>> hijosNuevos = new ListaDoblementeEnlazada<>();
        boolean encontrado = false;
        for (TNodo<E> hijo : padre.getHijos()) {
            hijosNuevos.addLast(hijo);
            if (hijo == hermano) {
                hijosNuevos.addLast(nuevo);
                encontrado = true;
            }
        }
        if (!encontrado) {
            throw new InvalidPositionException("El hermano no es hijo del padre dado");
        }
        padre.setHijos(hijosNuevos);
        tamaño++;
        return nuevo;
    }

    @Override
    public void removeExternalNode(Position<E> p) {
        if (raíz == null) {
            throw new EmptyTreeException("Árbol vacío");
        }
        TNodo<E> aux = checkPosition(p);
        if (aux.getHijos() != null) {
            throw new InvalidPositionException("El nodo no es hoja");
        }
        if (aux == raíz) {
            raíz = null;
        } else {
            TNodo<E> padre = (TNodo<E>) aux.getPadre();
            PositionList<TNodo<E>> hijosNuevos = new ListaDoblementeEnlazada<>();
            boolean encontrado = false;
            for (TNodo<E> hijo : padre.getHijos()) {
                if (hijo == aux) {
                    encontrado = true;
                } else {
                    hijosNuevos.addLast(hijo);
                }
            }
            if (!encontrado) {
                throw new InvalidPositionException("El nodo no se encontró en los hijos del padre");
            }
            padre.setHijos(hijosNuevos.isEmpty() ? null : hijosNuevos);
            aux.setPadre(null);
        }
        tamaño--;
    }

    @Override
    public void removeInternalNode(Position<E> p) {
        if (raíz == null) {
            throw new EmptyTreeException("Árbol vacío");
        }
        TNodo<E> aux = checkPosition(p);
        if (aux.getHijos() == null || aux.getHijos().isEmpty()) {
            throw new InvalidPositionException("El nodo es hoja, use removeExternalNode");
        }
        if (aux == raíz) {
            if (aux.getHijos().size() != 1) {
                throw new InvalidPositionException("La raíz tiene más de un hijo");
            }
            raíz = aux.getHijos().iterator().next();
            raíz.setPadre(null);
        } else {
            TNodo<E> padre = (TNodo<E>) aux.getPadre();
            PositionList<TNodo<E>> hijosNuevos = new ListaDoblementeEnlazada<>();
            boolean encontrado = false;
            for (TNodo<E> hijo : padre.getHijos()) {
                if (hijo == aux) {
                    encontrado = true;
                    for (TNodo<E> nieto : aux.getHijos()) {
                        nieto.setPadre(padre);
                        hijosNuevos.addLast(nieto);
                    }
                } else {
                    hijosNuevos.addLast(hijo);
                }
            }
            if (!encontrado) {
                throw new InvalidPositionException("El nodo no se encontró entre los hijos del padre");
            }
            padre.setHijos(hijosNuevos);
        }
        tamaño--;
    }

}
