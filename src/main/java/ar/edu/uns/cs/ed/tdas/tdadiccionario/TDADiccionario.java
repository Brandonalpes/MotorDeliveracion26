import Excepciones.InvalidKeyException;
import java.security.Key;

import Repositorio.Entry;

public class TDADiccionario<K,V> implements Dictionary<K,V> {
    private K key;
    private V value;
    private int tamaño;
    private PositionList<Entrada<K,V>>[] buckets;
    private static final float fc = 0.0f;

    

    @Override
    public int size() {
        return tamaño;
    }
    @Override
    public boolean isEmpty() {
        return tamaño==0;
    }
    @Override
    public Entry<K, V> find(K key) {

    }
    @Override
    public Iterable<Entry<K, V>> findAll(K key) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }
    @Override
    public Entry<K, V> insert(K key, V value) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'insert'");
    }
    @Override
    public Entry<K, V> remove(Entry<K, V> e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'remove'");
    }
    @Override
    public Iterable<Entry<K, V>> entries() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'entries'");
    }

    private int CodigoHash(K key){
        return Math.abs(k.hashCode())/buckets.length;
    }

    //------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------

    public boolean ClavesIguales(K k1, K k2){
        //-----------------------------------------------------------------------------
        // PARA ESTA IMPLEMENTACIÓN, SE ASUME QUE AMBAS CLAVES TIENEN SUS VALORES
        // ORDENADOS DENTRO DE SUS RESPECTIVOS BUCKETS
        //-----------------------------------------------------------------------------
        TDAPila aux1 = new TDAPila();
        TDAPila aux2 = new TDAPila();
        boolean toRet=true;
        for (Entrada entry1: buckets[CodigoHash(k1)]){
            if (entry1.getKey().equals(k1)){
                if (aux1.isEmpty()){
                    aux1.push(entry1.getValue());
                } else {
                    if (!aux1.top().equals(entry1.getValue())){
                        aux1.push(entry1.getValue());
                    }
                }
            }
        }
        for (Entrada entry2: buckets[CodigoHash(k2)]){
            if (entry2.getKey().equals(k2)){
                if (aux2.isEmpty()){
                    aux2.push(entry2.getValue());
                } else {
                    if (!aux2.top().equals(entry2.getValue())){
                        aux2.push(entry2.getValue());
                    }
                }
            }
        }
        if (aux1.size()!=aux2.size()){
                toRet=false;
            } 
        while (toRet && !aux1.isEmpty() && !aux2.isEmpty()){
            toRet=aux1.pop().equals(aux2.pop());
        }
        return toRet;
    }

    //------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------


    public boolean contieneMasQue(K key, V value, int n) throws InvalidKeyException{
        int i=0;
        if (key==null){
            throw new InvalidKeyException("llave nula");
        }
        for (Entrada entry : buckets[CodigoHash(key)]){
            if (entry.getKey().equals(key) && entry.getValue().equals(value)){
                i++;
            }
        }
        return i>n;
    }
}
