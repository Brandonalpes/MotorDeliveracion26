package ar.edu.uns.cs.ed;

import java.util.Iterator;
import ar.edu.uns.cs.ed.tdas.Position;
import ar.edu.uns.cs.ed.tdas.excepciones.EmptyStackException;
import ar.edu.uns.cs.ed.tdas.excepciones.InvalidPositionException;
import ar.edu.uns.cs.ed.tdas.tdaarbol.Arbol;
import ar.edu.uns.cs.ed.tdas.tdaarbol.Tree;
import ar.edu.uns.cs.ed.tdas.tdadiccionario.DiccionarioHashAbierto;
import ar.edu.uns.cs.ed.tdas.tdalista.ListaDoblementeEnlazada;
import ar.edu.uns.cs.ed.tdas.tdalista.PositionList;
import ar.edu.uns.cs.ed.tdas.tdamapeo.Map;
import ar.edu.uns.cs.ed.tdas.tdamapeo.MapeoConHash;
import ar.edu.uns.cs.ed.tdas.tdapila.PilaConLista;
import ar.edu.uns.cs.ed.tdas.tdapila.Stack;

/**
 * Incluir los import necesarios para las clases utilizadas en el código.
 */

/**
 * Clase que representa el motor de deliveración.
 * 
 */
public class MotorDeliberacion {

    /**
     * Incluir los atributos necesarios para el desarrollo.
     */
    private Tree<Opinion> arbol;
    private Map<String, Position<Opinion>> nodos;
    private Map<String, PositionList<Opinion>> opinionesDe;
    private Map<String, Position<Opinion>> lugarEnUsuario;
    private Stack<Operaciones> historial;

    /**
     * Constructor de la clase MotorDeliveracion.
     * Inicializa el árbol de debate, y las estructuras correspondientes.  
     * 
     */
    public MotorDeliberacion() {
        arbol = new Arbol<Opinion>();
        nodos = new MapeoConHash<String, Position<Opinion>>();
        opinionesDe = new MapeoConHash<String, PositionList<Opinion>>();
        lugarEnUsuario = new MapeoConHash<String, Position<Opinion>>();
        historial = new PilaConLista<Operaciones>();
    }

    /**
     * Registra una operación en la estructura que almacena las operaciones.
     * Las siguientes operaciones se pueden deshacer: "CREAR_DEBATE", "POSTEAR". El resto de las operaciones no se pueden deshacer.
     * 
     * La operación eliminar posteo no se puede deshacer, y si se intenta deshacer una operación cuando no hay operaciones registradas, se lanza una excepción.
     * 
     * 
     * @param operacion : La operación a registrar. 1: CREAR_DEBATE, 2: POSTEAR
     * @param detalle : Detalle de la operación, como el id del argumento o el id del usuario. El texto de la operación se almacena en mayúsculas para mantener consistencia. Por ejemplo: "CREAR_DEBATE:" + idRaiz.trim()
     * @param idArgumento : Identificador del argumento o de root en caso de ser el tema raíz.
     * @throws IllegalArgumentException si la operación es nula o vacía.
     */
    public void registrarOperacion(int operacion, String detalle, String idArgumento) {
        if ((operacion != 1 && operacion != 2) || detalle == null || detalle.trim().isEmpty()
                || idArgumento == null || idArgumento.trim().isEmpty()) {
            throw new IllegalArgumentException("operacion invalida");
        }
        Operaciones op = new Operaciones(operacion, detalle.trim().toUpperCase(), idArgumento.trim());
        historial.push(op);
    }

/**
 * Deshace la última operación registrada en el motor de deliveración.
 * La operación eliminar posteo no se puede deshacer, y si se intenta deshacer una operación cuando no hay operaciones registradas, se lanza una excepción.
 * @return
 * @throws EmptyStackException
 */
    public String deshacerUltimaOperacion() throws EmptyStackException {
        if (historial.isEmpty()) {
            throw new EmptyStackException("No hay operaciones para deshacer");
        }

        // desapilo antes porque clearDebate reinicia la pila
        Operaciones opActual = historial.pop();

        if (opActual.operacion == 1) {
            clearDebate();
            return "Se deshizo CREAR_DEBATE: " + opActual.id;
        }

        Position<Opinion> pos = nodos.get(opActual.id);
        if (pos == null) {
            throw new IllegalStateException("No se pudo deshacer la operación POSTEAR: " + opActual.id);
        }

        borrarSubarbol(pos);
        return "Se deshizo POSTEAR: " + opActual.id;
    }
/**
 * Crea un nuevo debate con un argumento raíz.
 * @param idRaiz   : Identificador único del tema raíz.
 * @param texto:    Texto del tema raíz.
 * @param idUsuario: Identificador del usuario.
 * @param nombreUsuario: Nombre del usuario.
 * 
 * El método valida que los parámetros no sean nulos o vacíos, y que el identificador de la raíz sea numérico.
 * Si el árbol ya tiene un debate, lanza una excepción.
 * El método registra la operación en la pila de operaciones para permitir deshacerla posteriormente.
 * @throws IllegalArgumentException si algún parámetro es inválido.
 * @throws IllegalStateException si ya existe un debate en curso.
 * Crea un usuario y un tema raíz, y los agrega al árbol y a las estructuras correspondientes.
 */
    public void crearDebate(String idRaiz, String texto, String idUsuario, String nombreUsuario) {
        if (idRaiz == null || idRaiz.trim().isEmpty()
                || texto == null || texto.trim().isEmpty()
                || idUsuario == null || idUsuario.trim().isEmpty()
                || nombreUsuario == null || nombreUsuario.trim().isEmpty()) {
            throw new IllegalArgumentException("parametros invalidos");
        }
        if (!arbol.isEmpty()) {
            throw new IllegalStateException("ya hay un debate creado");
        }

        String id = idRaiz.trim();
        String idUsr = idUsuario.trim();
        Usuario u = new Usuario(idUsr, nombreUsuario.trim());
        Opinion op = new Opinion(id, texto, "ROOT", u);

        arbol.createRoot(op);
        Position<Opinion> posRaiz = arbol.root();
        nodos.put(id, posRaiz);

        PositionList<Opinion> lista = opinionesDe.get(idUsr);
        if (lista == null) {
            lista = new ListaDoblementeEnlazada<Opinion>();
            opinionesDe.put(idUsr, lista);
        }
        lista.addLast(op);
        // guardo la posicion en la lista del usuario para despues poder borrar en O(1)
        lugarEnUsuario.put(id, lista.last());

        registrarOperacion(1, "CREAR_DEBATE:" + id, id);
    }

    public void Postear(Usuario usr, String idNuevo, String idPadre, String postura, String texto) {
        if (arbol.isEmpty()) {
            throw new IllegalStateException("no hay debate creado");
        }
        if (usr == null || usr.getId() == null || usr.getId().trim().isEmpty()
                || idNuevo == null || idNuevo.trim().isEmpty()
                || idPadre == null || idPadre.trim().isEmpty()
                || postura == null || postura.trim().isEmpty()
                || texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("parametros invalidos");
        }

        String post = postura.trim().toUpperCase().replace(" ", "");
        if (!post.equals("AFAVOR") && !post.equals("ENCONTRA")) {
            throw new IllegalArgumentException("postura invalida");
        }

        String id = idNuevo.trim();
        Position<Opinion> posPadre = nodos.get(idPadre.trim());
        if (posPadre == null) {
            throw new IllegalArgumentException("el padre no existe");
        }
        if (nodos.get(id) != null) {
            throw new IllegalArgumentException("ya existe un posteo con ese id");
        }

        Opinion nuevaOp = new Opinion(id, texto, post, usr);
        Position<Opinion> posNueva = arbol.addLastChild(posPadre, nuevaOp);
        nodos.put(id, posNueva);

        String idUsr = usr.getId().trim();
        PositionList<Opinion> lista = opinionesDe.get(idUsr);
        if (lista == null) {
            lista = new ListaDoblementeEnlazada<Opinion>();
            opinionesDe.put(idUsr, lista);
        }
        lista.addLast(nuevaOp);
        lugarEnUsuario.put(id, lista.last());

        registrarOperacion(2, "POSTEAR:" + id, id);
    }
    

    // Elimina un posteo y todo el subárbol.
    public void eliminarPosteo(String idUsuario, String idPosteo) {
        if(idUsuario == null || idUsuario.trim().isEmpty() || idPosteo == null || idPosteo.trim().isEmpty()) {
            throw new IllegalArgumentException("datos invalidos");
        }

        Position<Opinion> p = nodos.get(idPosteo.trim());
        if (p == null) {
            throw new IllegalArgumentException("no existe ese posteo");
        }

        if (!p.element().getUsuario().getId().equals(idUsuario.trim())) {
            throw new IllegalStateException("solo el autor puede eliminar");
        }

        if (arbol.isRoot(p)) {
            clearDebate();
            return;
        }

        borrarSubarbol(p);
    }


    private void borrarSubarbol(Position<Opinion> raizSub) {
        PositionList<String> listaIds = new ListaDoblementeEnlazada<String>();
        PositionList<Position<Opinion>> listaPos = new ListaDoblementeEnlazada<Position<Opinion>>();
        obtenerIdsYPosicionesSubarbol(raizSub, listaIds, listaPos);

        Iterator<String> itIds = listaIds.iterator();
        Iterator<Position<Opinion>> itPos = listaPos.iterator();

        while (itIds.hasNext()) {
            String idAct = itIds.next();
            Position<Opinion> posAct = itPos.next();
            String idUsr = posAct.element().getUsuario().getId().trim();

            PositionList<Opinion> listaUsr = opinionesDe.get(idUsr);
            Position<Opinion> posEnLista = lugarEnUsuario.remove(idAct);
            if(listaUsr != null && posEnLista != null) {
                listaUsr.remove(posEnLista);
            }

            nodos.remove(idAct);
            arbol.removeExternalNode(posAct);
        }
    }


    //obtiene las posiciones en postorden.
    private void obtenerIdsYPosicionesSubarbol(Position<Opinion> nodo, PositionList<String> ids, PositionList<Position<Opinion>> posiciones) {
        for (Position<Opinion> h : arbol.children(nodo)) {
            obtenerIdsYPosicionesSubarbol(h, ids, posiciones);
        }
        ids.addLast(nodo.element().getIdArgumento());
        posiciones.addLast(nodo);
    }

    //limpia todo el debate, dejando la estructura vacía.   
    public void clearDebate() {
        arbol = new Arbol<Opinion>();
        nodos = new MapeoConHash<String, Position<Opinion>>();
        opinionesDe = new MapeoConHash<String, PositionList<Opinion>>();
        lugarEnUsuario = new MapeoConHash<String, Position<Opinion>>();
        historial = new PilaConLista<Operaciones>();
    }

    //consultar por usuario, devuelve una lista de argumentos que pertenecen a ese usuario.
    public PositionList<Opinion> consultarArgumentosPorUsuario(String idUsuario) {
        if (idUsuario == null || idUsuario.trim().isEmpty()) {
            throw new IllegalArgumentException("id de usuario invalido");
        }
        PositionList<Opinion> lista = opinionesDe.get(idUsuario.trim());
        if (lista == null) {
            // si el usuario no tiene argumentos devuelve lista vacia
            return new ListaDoblementeEnlazada<Opinion>();
        }
        return lista;
    }



    public void mostrarOperaciones() {
        if (historial.isEmpty()) {
            System.out.println("No hay operaciones");
            return;
        }

        Stack<Operaciones> aux = new PilaConLista<Operaciones>();
        while (!historial.isEmpty()) {
            aux.push(historial.pop());
        }

        int i = 1;
        while (!aux.isEmpty()) {
            Operaciones op = aux.pop();
            System.out.println(i + ") " + op.detalle + " - " + op.id);
            historial.push(op);
            i++;
        }
    }

    /**
     * Calcula la polarización de todos los nodos del debate.
     * La polarización se define como la cantidad de argumentos a favor y en contra que existen en el subárbol que tiene como raíz cada nodo.
     * @throws IllegalStateException si no hay un debate activo.
     * @return Un diccionario cuya clave es el ID del nodo y el valor es un diccionario con la polarización: 
     * "AFAVOR" y "ENCONTRA" y sus respectivos conteos.
     */
    public DiccionarioHashAbierto<String,DiccionarioHashAbierto<String,Integer>> calcularPolarizacionDebate(){
        // TODO: falta armarlo en una pasada postorden para que quede en O(n)
        return null;
    }

     /**
    * Calcula la polarización de un nodo específico en el árbol de debate.
    * La polarización se define como la cantidad de argumentos a favor y en contra que existen en el subárbol que tiene como raíz el nodo especificado por idNodo.
    * @throws IllegalStateException si no hay un debate activo. 
    * @param idNodo : Identificador del nodo para el cual se desea calcular la polarización.
    * @return Un DiccionarioHashAbierto<String,Integer> que contiene la polarización del nodo, con las claves "AFAVOR" y "ENCONTRA" y sus respectivos conteos.
    */
    public DiccionarioHashAbierto<String,Integer> calcularPolarizacionNodo(String idNodo){
        if (idNodo == null) {
            throw new IllegalArgumentException("id invalido");
        }

        Position<Opinion> p = nodos.get(idNodo.trim());
        if (p == null) {
            throw new IllegalArgumentException("no existe el nodo");
        }

        int[] cont = contarPol(p);
        // System.out.println(idNodo + " -> " + cont[0] + " / " + cont[1]);

        DiccionarioHashAbierto<String, Integer> res = new DiccionarioHashAbierto<String, Integer>();
        res.insert("AFAVOR", cont[0]);
        res.insert("ENCONTRA", cont[1]);
        return res;
    }

    private int[] contarPol(Position<Opinion> v) {
        int[] r = new int[2];
        String post = v.element().getPostura();
        if (post.equals("AFAVOR")) {
            r[0]++;
        } else if (post.equals("ENCONTRA")) {
            r[1]++;
        }

        for (Position<Opinion> h : arbol.children(v)) {
            int[] rh = contarPol(h);
            r[0] += rh[0];
            r[1] += rh[1];
        }
        return r;
    }
 /*** Mostrar árbol por consola ***/
 
    /**
     * Imprime por consola, en modo texto, el contenido del árbol de debate,
     * con indentación jerárquica que muestra la estructura padre-hijo.
     * Cada nodo se muestra con su id, postura, usuario y texto del argumento.
     */
    public void imprimirDebate() {
        if (arbol == null || arbol.isEmpty()) {
            System.out.println("(El árbol de debate está vacío.)");
            return;
        }

        try {
            Position<Opinion> raiz = arbol.root();
            System.out.println(formatearNodo(raiz.element()));
            imprimirHijos(raiz, "");
        } catch (Exception e) {
            System.out.println("No se pudo imprimir el árbol: " + e.getMessage());
        }
    }

    /**
     * Imprime recursivamente los hijos de 'nodo', usando 'prefijo' para
     * mantener la indentación acumulada de los niveles superiores y
     * conectores tipo "├──" / "└──" para el nivel actual.
     */
    private void imprimirHijos(Position<Opinion> nodo, String prefijo) throws InvalidPositionException {
        PositionList<Position<Opinion>> hijos = new ListaDoblementeEnlazada<Position<Opinion>>();
        for (Position<Opinion> hijo : arbol.children(nodo)) {
            hijos.addLast(hijo);
        }


        for (Position<Opinion> hijo : arbol.children(nodo)) {
            boolean esUltimo = (hijo == hijos.last().element());
            
            String conector = esUltimo ? "└── " : "├── ";
            System.out.println(prefijo + conector + formatearNodo(hijo.element()));

            String prefijoHijo = prefijo + (esUltimo ? "    " : "│   ");
            imprimirHijos(hijo, prefijoHijo);
            
        }
    }

/** Arma la línea de texto que representa a un Argumento en la impresión. */
    private String formatearNodo(Opinion a) {
        if (a == null) {
            return "(argumento nulo)";
        }
        return "[" + a.getIdArgumento() + "] (" + a.getPostura() + ") "
                + "usuario=" + a.getUsuario().getId() + " -> " + a.getTexto();
    }
}
