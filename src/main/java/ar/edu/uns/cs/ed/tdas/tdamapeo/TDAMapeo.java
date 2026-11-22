import Excepciones.InvalidEntryException;
import java.util.Iterator;
import Excepciones.InvalidKeyException;
import Repositorio.Entrada;

public class TDAMapeo<K,V> implements Map<K,V> {
    private int N;
    private int n;
    private PositionList<Entrada<K,V>> [] A;
    private static final float FC = 0.7f;
    
    public TDAMapeo(){
        N=11;
        n=0;
        A= new PositionList[N];
        for (int i=0; i<A.length; i++){
            A[i]= new ListaDoblementeEnlazada<Entrada<K,V>>();
        }
    }

    public int size(){
        return n;
    }

    public boolean isEmpty(){
        return n==0;
    }

    public V get(K key){
        if (key==null)
            throw new InvalidKeyException("Clave nula");
        int i=hash(key);
        PositionList<Entrada<K,V>> l= A[i];
        V toRet=null;
        for (Entrada<K,V> e:l){
            if (e.getKey().equals(key)){
                toRet=e.getValue();
                break;
            }
        }
        return toRet;
    }

    public V put(K llave, V valor){
        V toRet=null;
        int i= hash(llave);
        boolean encontrado=false;
        PositionList<Entrada<K,V>> l = A[i];
        for (Entrada<K,V> e:l){
            if (e.getKey().equals(llave)){
                toRet=e.getValue();
                e= new Entrada(llave, valor);
                encontrado=true;
                break;
            }
        }
        if (!encontrado){
            l.addLast(new Entrada(llave, valor));
            n++;
            if (n>N*FC){
                reHash();
            }
        }
        return toRet;
    }


    public V remove(K key) {
        int i = hash(key);
        Iterator<Position<Entry<K, V>>> it = (Iterator)A[i].positions().iterator();
        V resultado = null;
        while (it.hasNext()) {
            Position<Entry<K, V>> p = it.next();
            if (p.element().getKey().equals(key)) {
                resultado = p.element().getValue();
                A[i].remove(p);
                n--;
                break;
            }
        }
        return resultado;
    }
    
    
    @Override
    public Iterable<K> keys() {
        
    }
    
    @Override
    public Iterable<V> values() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'values'");
    }
    
    @Override
    public Iterable<Entry<K, V>> entries() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'entries'");
    }

    private int hash(K key){
        return Math.abs(key.hashCode()/N); 
    }
    
    private void reHash(){
    
    }
}
