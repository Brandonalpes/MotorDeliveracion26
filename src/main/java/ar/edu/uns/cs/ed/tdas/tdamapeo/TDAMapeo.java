package ar.edu.uns.cs.ed.tdas.tdamapeo;

import ar.edu.uns.cs.ed.tdas.excepciones.InvalidEntryException;
import java.util.Iterator;
import ar.edu.uns.cs.ed.tdas.excepciones.InvalidKeyException;
import ar.edu.uns.cs.ed.tdas.tdalista.ListaDoblementeEnlazada;
import ar.edu.uns.cs.ed.tdas.tdalista.PositionList;
import ar.edu.uns.cs.ed.tdas.Entrada;
import ar.edu.uns.cs.ed.tdas.Entry;
import ar.edu.uns.cs.ed.tdas.Position;

public class TDAMapeo<K, V> implements Map<K, V> {
    private int N;
    private int n;
    private PositionList<Entrada<K, V>>[] A;
    private static final float FC = 0.7f;

    public TDAMapeo() {
        N = 11;
        n = 0;
        A = new PositionList[N];
        for (int i = 0; i < A.length; i++) {
            A[i] = new ListaDoblementeEnlazada<Entrada<K, V>>();
        }
    }

    public int size() {
        return n;
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public V get(K key) {
        if (key == null)
            throw new InvalidKeyException("Clave nula");
        int i = hash(key);
        PositionList<Entrada<K, V>> l = A[i];
        V toRet = null;
        for (Entrada<K, V> e : l) {
            if (e.getKey().equals(key)) {
                toRet = e.getValue();
                break;
            }
        }
        return toRet;
    }

    public V put(K llave, V valor) {
        V toRet = null;
        int i = hash(llave);
        boolean encontrado = false;
        PositionList<Entrada<K, V>> l = A[i];
        for (Entrada<K, V> e : l) {
            if (e.getKey().equals(llave)) {
                toRet = e.getValue();
                e = new Entrada(llave, valor);
                encontrado = true;
                break;
            }
        }
        if (!encontrado) {
            l.addLast(new Entrada(llave, valor));
            n++;
            if (n > N * FC) {
                reHash();
            }
        }
        return toRet;
    }

    public V remove(K key) {
        int i = hash(key);
        V resultado = null;
        ListaDoblementeEnlazada<Entrada<K, V>> nueva = new ListaDoblementeEnlazada<>();
        for (Entrada<K, V> e : A[i]) {
            if (resultado == null && e.getKey().equals(key)) {
                resultado = e.getValue();
            } else {
                nueva.addLast(e);
            }
        }
        if (resultado != null) {
            A[i] = nueva;
            n--;
        }
        return resultado;
    }

    @Override
    public Iterable<K> keys() {
        PositionList<K> resultado = new ListaDoblementeEnlazada<>();
        for (PositionList<Entrada<K, V>> bucket : A) {
            for (Entrada<K, V> e : bucket) {
                resultado.addLast(e.getKey());
            }
        }
        return resultado;
    }

    @Override
    public Iterable<V> values() {
        PositionList<V> resultado = new ListaDoblementeEnlazada<>();
        for (PositionList<Entrada<K, V>> bucket : A) {
            for (Entrada<K, V> e : bucket) {
                resultado.addLast(e.getValue());
            }
        }
        return resultado;
    }

    @Override
    public Iterable<Entry<K, V>> entries() {
        PositionList<Entry<K, V>> resultado = new ListaDoblementeEnlazada<>();
        for (PositionList<Entrada<K, V>> bucket : A) {
            for (Entrada<K, V> e : bucket) {
                resultado.addLast(e);
            }
        }
        return resultado;
    }

    private void reHash() {
        PositionList<Entrada<K, V>>[] viejo = A;
        N = N * 2 + 1;
        A = new PositionList[N];
        for (int i = 0; i < A.length; i++) {
            A[i] = new ListaDoblementeEnlazada<>();
        }
        n = 0;
        for (PositionList<Entrada<K, V>> bucket : viejo) {
            for (Entrada<K, V> e : bucket) {
                put(e.getKey(), e.getValue());
            }
        }
    }

    private int hash(K key) {
        return Math.abs(key.hashCode() % N);
    }
}
