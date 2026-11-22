public class TDACola<E> implements Queue<E> {
    private int size;
    private E[] cola;

    public TDACola(){
        cola = (E[]) new Object[10];
    }

    public TDACola(int i){
        cola = (E[]) new Object[i];
    }
    

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size==0;
    }

    @Override
    public E front() {
        return cola[0];
    }

    @Override
    public void enqueue(E element) {
        if (size==cola.length){
            reOrdenar(cola);
            cola[size]=element;
        } else {
            cola[size]=element;
        }
        size++;
    }

    @Override
    public E dequeue() {
        E aux = cola[0];
        for (int i=1; i<cola.length && cola[i]!=null; i++){
            cola[i]=cola[i-1];
        }
        return aux;
    }
    
    private void reOrdenar(E[] cola){
        E[] nuevacola = (E[]) new Object[cola.length*2];
        for (int i=0; i<cola.length; i++){
            nuevacola[i] = cola[i];
        }
        cola=nuevacola;
    }
}
