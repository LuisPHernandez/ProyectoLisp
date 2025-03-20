import java.util.HashMap;
import java.util.Map;

public class EntornoLisp {
    /**
     * Mapa que almacena las variables declaradas (nombre -> valor)
     */
    private Map<String, Object> variables;
    
    /**
     * Mapa que almacena las funciones declaradas (nombre -> objeto FuncionLisp)
     */
    private Map<String, FuncionLisp> funciones;
    
    /**
     * Referencia al entorno padre (para manejo de ámbitos/scopes)
     */
    private EntornoLisp entornoPadre;

    /**
     * Constructor para crear un nuevo entorno sin padre
     */
    public EntornoLisp() {
        this.variables = new HashMap<>();
        this.funciones = new HashMap<>();
        this.entornoPadre = null;
    }
    
    /**
     * Constructor para crear un entorno con un entorno padre específico
     * @param entornoPadre Entorno padre
     */
    public EntornoLisp(EntornoLisp entornoPadre) {
        this.variables = new HashMap<>();
        this.funciones = new HashMap<>();
        this.entornoPadre = entornoPadre;
    }

    public void definirVariable(String nombre, Object valor) {
        variables.put(nombre, valor);
    }

    public Object obtenerVariable(String nombre) {
        // Buscar en el entorno actual
        if (variables.containsKey(nombre)) {
            return variables.get(nombre);
        }
        
        // Si no se encuentra y hay un entorno padre, buscar en él
        if (entornoPadre != null) {
            return entornoPadre.obtenerVariable(nombre);
        }
        
        // Si no se encuentra en ningún lado, retornar null
        return null;
    }

    public void definirFuncion(String nombre, FuncionLisp funcion) {
        funciones.put(nombre, funcion);
    }

    public FuncionLisp obtenerFuncion(String nombre) {
        // Buscar en el entorno actual
        if (funciones.containsKey(nombre)) {
            return funciones.get(nombre);
        }
        
        // Si no se encuentra y hay un entorno padre, buscar en él
        if (entornoPadre != null) {
            return entornoPadre.obtenerFuncion(nombre);
        }
        
        // Si no se encuentra en ningún lado, retornar null
        return null;
    }
    
    /**
     * Crea un nuevo entorno hijo del actual (para manejar funciones)
     * 
     * @return Nuevo entorno con referencia al actual como padre
     */
    public EntornoLisp crearEntornoHijo() {
        return new EntornoLisp(this);
    }
}
