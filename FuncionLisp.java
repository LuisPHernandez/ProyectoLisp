import java.util.List;

public class FuncionLisp {
    /**
     * Lista de parámetros de la función
     */
    private List<String> parametros;
    
    /**
     * Cuerpo de la función
     */
    private List<Object> cuerpo;

    /**
     * @param parametros
     * @param cuerpo
     */
    public FuncionLisp(List<String> parametros, List<Object> cuerpo) {
        this.parametros = parametros;
        this.cuerpo = cuerpo;
    }

    /**
     * Obtiene la lista de parámetros de la función
     * 
     * @return Lista de parámetros
     */
    public List<String> getParametros() {
        return parametros;
    }

    /**
     * Obtiene el cuerpo de la función
     * 
     * @return Cuerpo de la función como una lista de expresiones
     */
    public List<Object> getCuerpo() {
        return cuerpo;
    }
}
