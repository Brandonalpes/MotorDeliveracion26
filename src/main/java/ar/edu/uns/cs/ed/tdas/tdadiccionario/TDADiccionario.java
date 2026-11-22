package ar.edu.uns.cs.ed.tdas.tdadiccionario;

import ar.edu.uns.cs.ed.tdas.Entrada;
import ar.edu.uns.cs.ed.tdas.Entry;
import ar.edu.uns.cs.ed.tdas.excepciones.InvalidKeyException;

import ar.edu.uns.cs.ed.tdas.tdalista.ListaDoblementeEnlazada;
import ar.edu.uns.cs.ed.tdas.tdalista.PositionList;

public class TDADiccionario<K, V> implements Dictionary<K, V> {
    private K key;
    private V value;
    private int tamaño;
    private PositionList<Entrada<K, V>>[] buckets;
    private static final float fc = 0.0f;

    @Override
    public int size() {
        return tamaño;
    }

    @Override
    public boolean isEmpty() {
        return tamaño == 0;
    }

    @Override
    public Entry<K, V> find(K key) {
        if (key == null) {
            throw new InvalidKeyException("Clave inválida");
        }
        int idx = CodigoHash(key);
        if (buckets[idx] == null) {
            return null;
        }
        for (Entrada<K, V> entrada : buckets[idx]) {
            if (entrada.getKey().equals(key)) {
                return entrada;
            }
        }
        return null;
    }

    @Override
    public Iterable<Entry<K, V>> findAll(K key) {
        if (key == null) {
            throw new InvalidKeyException("Clave inválida");
        }
        PositionList<Entry<K, V>> resultado = new ListaDoblementeEnlazada<>();
        int idx = CodigoHash(key);
        if (buckets[idx] != null) {
            for (Entrada<K, V> entrada : buckets[idx]) {
                if (entrada.getKey().equals(key)) {
                    resultado.addLast(entrada);
                }
            }
        }
        return resultado;
    }

    @Override
    public Entry<K, V> insert(K key, V value) {
        if (key == null) {
            throw new InvalidKeyException("Clave inválida");
        }
        int idx = CodigoHash(key);
        if (buckets[idx] == null) {
            buckets[idx] = new ListaDoblementeEnlazada<>();
        }
        Entrada<K, V> nueva = new Entrada<>(key, value);
        buckets[idx].addLast(nueva);
        tamaño++;
        return nueva;
    }

    @Override
    public Entry<K, V> remove(Entry<K, V> e) {
        if (e == null) {
            throw new InvalidKeyException("Entrada inválida");
        }
        int idx = CodigoHash(e.getKey());
        if (buckets[idx] != null) {
            for (Entrada<K, V> entrada : buckets[idx]) {
                if (entrada.equals(e)) {
                    PositionList<Entry<K, V>> aux = new ListaDoblementeEnlazada<>();
                    for (Entrada<K, V> otro : buckets[idx]) {
                        if (!otro.equals(e)) {
                            aux.addLast(otro);
                        }
                    }
                    buckets[idx] = new ListaDoblementeEnlazada<>();
                    for (Entry<K, V> otro : aux) {
                        buckets[idx].addLast((Entrada<K, V>) otro);
                    }
                    tamaño--;
                    return entrada;
                }
            }
        }
        return null;
    }

    @Override
    public Iterable<Entry<K, V>> entries() {
        PositionList<Entry<K, V>> resultado = new ListaDoblementeEnlazada<>();
        for (PositionList<Entrada<K, V>> bucket : buckets) {
            if (bucket != null) {
                for (Entrada<K, V> entrada : bucket) {
                    resultado.addLast(entrada);
                }
            }
        }
        return resultado;
    }

    private int CodigoHash(K key) {
        return Math.abs(key.hashCode()) % buckets.length;
    }
}
