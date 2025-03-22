import java.util.ArrayList;
import java.util.List;

public class InterpreteLisp {
    /**
     * Entorno global para almacenar variables y funciones
     */
    private EntornoLisp entorno;
    
    /**
     * Analizador léxico para tokenizar la entrada
     */
    private LexerLisp lexer;
    
    /**
     * Analizador sintáctico para construir el árbol de sintaxis
     */
    private ParserLisp parser;

    /**
     * Constructor del intérprete
     */
    public InterpreteLisp() {
        this.entorno = new EntornoLisp();
        this.lexer = new LexerLisp();
        inicializarFuncionesBasicas();
    }
    
    /**
     * Inicializa las funciones básicas de Lisp
     */
    private void inicializarFuncionesBasicas() {
        entorno.definirVariable("T", true);
        entorno.definirVariable("NIL", null);
    }

    /**
     * @param expresion
     * @return
     * @throws Exception
     */
    public Object evaluar(String expresion) throws Exception {
        // Tokeniza la expresión con el lexer
        List<Object> resultadoLexer = lexer.evaluarExpresion(expresion);
        
        // Verifica si la expresión es válida
        boolean esValido = (boolean) resultadoLexer.get(0);
        if (!esValido) {
            throw new Exception("Error de sintaxis: paréntesis no balanceados");
        }
        
        // Obtiene los tokens
        List<String> tokens = (List<String>) resultadoLexer.get(1);
        
        // Si no hay tokens, retorna null
        if (tokens.isEmpty()) {
            return null;
        }
        
        // Crea un parser con los tokens
        parser = new ParserLisp(tokens);
        
        // Parsea la expresión para obtener su estructura
        Object expresionLisp = parser.parseExpresion();
        
        // Evalúa la estructura en el entorno global
        return evaluarLisp(expresionLisp, entorno);
    }
    
    /**
     * @param expresionLisp
     * @param entorno
     * @return
     * @throws Exception
     */
    private Object evaluarLisp(Object expresionLisp, EntornoLisp entorno) throws Exception {
        // Si es null, retorna null
        if (expresionLisp == null) {
            return null;
        }
        
        // Si es un número, lo retorna directamente
        if (expresionLisp instanceof Integer) {
            return expresionLisp;
        }
        
        // Si es un String, podría ser una variable o un literal de string
        if (expresionLisp instanceof String) {
            String token = (String) expresionLisp;
            
            // Si comienza y termina con comillas es un literal de string
            if (token.startsWith("\"") && token.endsWith("\"")) {
                return token.substring(1, token.length() - 1);
            }
            
            // De lo contrario, es un símbolo (nombre de variable)
            Object valor = entorno.obtenerVariable(token);
            if (valor == null) {
                throw new Exception("Variable no definida: " + token);
            }
            return valor;
        }
        
        // Si es una lista, evalúa la expresión Lisp
        if (expresionLisp instanceof List) {
            List<Object> lista = (List<Object>) expresionLisp;
            
            // Lista vacía
            if (lista.isEmpty()) {
                return lista;
            }
            
            // Obtiene el primer elemento que debería ser un operador o función
            Object primerElemento = lista.get(0);
            
            // Si es una palabra reservada
            if (primerElemento instanceof String) {
                String operador = (String) primerElemento;
                
                // QUOTE: devuelve la expresión sin evaluar
                if (operador.equalsIgnoreCase("QUOTE")) {
                    if (lista.size() != 2) {
                        throw new Exception("QUOTE requiere exactamente un argumento");
                    }
                    return lista.get(1);
                }
                
                // SETQ: asigna un valor a una variable
                else if (operador.equalsIgnoreCase("SETQ")) {
                    if (lista.size() < 3 || lista.size() % 2 != 1) {
                        throw new Exception("SETQ requiere un número par de argumentos");
                    }
                    
                    Object resultado = null;
                    // Permite múltiples asignaciones en un solo SETQ
                    for (int i = 1; i < lista.size(); i += 2) {
                        if (!(lista.get(i) instanceof String)) {
                            throw new Exception("El primer argumento de SETQ debe ser un símbolo");
                        }
                        String variable = (String) lista.get(i);
                        Object valor = evaluarLisp(lista.get(i + 1), entorno);
                        entorno.definirVariable(variable, valor);
                        resultado = valor;
                    }
                    return resultado;
                }
                
                // DEFUN: define una función
                else if (operador.equalsIgnoreCase("DEFUN")) {
                    if (lista.size() < 4) {
                        throw new Exception("DEFUN requiere al menos 3 argumentos");
                    }
                    
                    if (!(lista.get(1) instanceof String)) {
                        throw new Exception("El nombre de la función debe ser un string");
                    }
                    
                    String nombreFuncion = (String) lista.get(1);
                    Object parametrosObj = lista.get(2);
                    
                    if (!(parametrosObj instanceof List)) {
                        throw new Exception("La lista de parámetros debe ser una lista");
                    }
                    
                    List<Object> parametrosLista = (List<Object>) parametrosObj;
                    List<String> parametros = new ArrayList<>();
                    
                    for (Object param : parametrosLista) {
                        parametros.add(param.toString());
                    }
                    
                    // El cuerpo de la función son todas las expresiones después de la lista de parámetros
                    List<Object> cuerpo = new ArrayList<>();
                    for (int i = 3; i < lista.size(); i++) {
                        cuerpo.add(lista.get(i));
                    }
                    
                    FuncionLisp funcion = new FuncionLisp(parametros, cuerpo);
                    entorno.definirFuncion(nombreFuncion, funcion);
                    
                    return nombreFuncion;
                }
                
                // ATOM: verifica si el argumento es un átomo (no una lista)
                else if (operador.equalsIgnoreCase("ATOM")) {
                    if (lista.size() != 2) {
                        throw new Exception("ATOM requiere exactamente un argumento");
                    }
                    
                    Object arg = evaluarLisp(lista.get(1), entorno);
                    return !(arg instanceof List) || ((List<?>) arg).isEmpty();
                }
                
                // LIST: crea una lista con los argumentos evaluados
                else if (operador.equalsIgnoreCase("LIST")) {
                    List<Object> resultado = new ArrayList<>();
                    
                    for (int i = 1; i < lista.size(); i++) {
                        resultado.add(evaluarLisp(lista.get(i), entorno));
                    }
                    
                    return resultado;
                }
                
                // EQUAL: compara igualdad de dos valores
                else if (operador.equalsIgnoreCase("EQUAL")) {
                    if (lista.size() != 3) {
                        throw new Exception("EQUAL requiere exactamente dos argumentos");
                    }
                    
                    Object arg1 = evaluarLisp(lista.get(1), entorno);
                    Object arg2 = evaluarLisp(lista.get(2), entorno);
                    
                    return sonIguales(arg1, arg2);
                }
                
                // <: compara si el primer argumento es menor que el segundo
                else if (operador.equals("<")) {
                    if (lista.size() != 3) {
                        throw new Exception("< requiere exactamente dos argumentos");
                    }
                    
                    Object arg1 = evaluarLisp(lista.get(1), entorno);
                    Object arg2 = evaluarLisp(lista.get(2), entorno);
                    
                    if (!(arg1 instanceof Integer) || !(arg2 instanceof Integer)) {
                        throw new Exception("Los argumentos de < deben ser números");
                    }
                    
                    return (Integer) arg1 < (Integer) arg2;
                }
                
                // >: compara si el primer argumento es mayor que el segundo
                else if (operador.equals(">")) {
                    if (lista.size() != 3) {
                        throw new Exception("> requiere exactamente dos argumentos");
                    }
                    
                    Object arg1 = evaluarLisp(lista.get(1), entorno);
                    Object arg2 = evaluarLisp(lista.get(2), entorno);
                    
                    if (!(arg1 instanceof Integer) || !(arg2 instanceof Integer)) {
                        throw new Exception("Los argumentos de > deben ser números");
                    }
                    
                    return (Integer) arg1 > (Integer) arg2;
                }
                
                // COND: evalúa condiciones hasta encontrar una verdadera
                /**
                 * Este bloque de código evalúa una condición en LISP.
                 * Este bloque fue realizado con ayuda de Claude AI.
                **/
                else if (operador.equalsIgnoreCase("COND")) {
                    if (lista.size() < 2) {
                        throw new Exception("COND requiere al menos dos argumentos");
                    }
                    
                    for (int i = 1; i < lista.size(); i++) {
                        Object clausula = lista.get(i);
                        
                        if (!(clausula instanceof List)) {
                            throw new Exception("La condición de COND debe ser una lista");
                        }
                        
                        List<Object> condExpresion = (List<Object>) clausula;
                        
                        if (condExpresion.isEmpty()) {
                            continue;
                        }
                        
                        Object condicion = evaluarLisp(condExpresion.get(0), entorno);
                        
                        // En Lisp, cualquier valor que no sea null o false se considera verdadero
                        if (condicion != null && !(condicion instanceof Boolean && !(Boolean) condicion)) {
                            // Si solo hay una expresión en la cláusula, devolvemos el resultado de la condición
                            if (condExpresion.size() == 1) {
                                return condicion;
                            }
                            
                            // De lo contrario, evaluamos y devolvemos el resultado de la última expresión
                            Object resultado = null;
                            for (int j = 1; j < condExpresion.size(); j++) {
                                resultado = evaluarLisp(condExpresion.get(j), entorno);
                            }
                            return resultado;
                        }
                    }
                    
                    // Si ninguna condición es verdadera, devolvemos null
                    return null;
                }
                
                // Operadores aritméticos o funciones definidas
                else {
                    // Intentamos ver si es una función definida
                    FuncionLisp funcion = entorno.obtenerFuncion(operador);
                    
                    if (funcion != null) {
                        // Es una función definida por el usuario
                        return aplicarFuncion(funcion, lista.subList(1, lista.size()), entorno);
                    } else {
                        // Es un operador aritmético básico (+, -, *, /, =)
                        return evaluarOperador(operador, lista.subList(1, lista.size()), entorno);
                    }
                }
            } 
            // Si cabeza es una lista u otro objeto, la evaluamos y seguimos
            else {
                Object funcionEvaluada = evaluarLisp(primerElemento, entorno);
                
                if (funcionEvaluada instanceof FuncionLisp) {
                    FuncionLisp funcion = (FuncionLisp) funcionEvaluada;
                    return aplicarFuncion(funcion, lista.subList(1, lista.size()), entorno);
                } else {
                    throw new Exception("No se puede aplicar: " + funcionEvaluada);
                }
            }
        }
        
        throw new Exception("Sintaxis no soportada: " + expresionLisp.getClass().getName());
    }
    
    /**
     * @param obj1
     * @param obj2
     * @return
     */
    private boolean sonIguales(Object obj1, Object obj2) {
        // Si ambos son null o referencias al mismo objeto
        if (obj1 == obj2) {
            return true;
        }
        
        // Si uno es null pero el otro no
        if (obj1 == null || obj2 == null) {
            return false;
        }
        
        // Si son de diferente clase
        if (obj1.getClass() != obj2.getClass()) {
            return false;
        }
        
        // Si son números
        if (obj1 instanceof Integer) {
            return ((Integer) obj1).equals(obj2);
        }
        
        // Si son strings
        if (obj1 instanceof String) {
            return obj1.equals(obj2);
        }
        
        // Si son listas
        if (obj1 instanceof List) {
            List<Object> lista1 = (List<Object>) obj1;
            List<Object> lista2 = (List<Object>) obj2;
            
            if (lista1.size() != lista2.size()) {
                return false;
            }
            
            for (int i = 0; i < lista1.size(); i++) {
                if (!sonIguales(lista1.get(i), lista2.get(i))) {
                    return false;
                }
            }
            
            return true;
        }
        
        // Para otros tipos, usamos equals
        return obj1.equals(obj2);
    }
    
    /**
     * @param funcion
     * @param argumentos
     * @param entorno
     * @return
     * @throws Exception
     */
    private Object aplicarFuncion(FuncionLisp funcion, List<Object> argumentos, EntornoLisp entorno) throws Exception {
        List<String> parametros = funcion.getParametros();
        List<Object> cuerpo = funcion.getCuerpo();
        
        // Verifica que la cantidad de argumentos coincida con la cantidad de parámetros
        if (parametros.size() != argumentos.size()) {
            throw new Exception("Número incorrecto de argumentos: esperaba " + 
                                parametros.size() + ", recibió " + argumentos.size());
        }
        
        // Crea un nuevo entorno para la ejecución de la función
        EntornoLisp nuevoEntorno = entorno.crearEntornoHijo();
        
        // Evalúa los argumentos y los vincula a los parámetros en el nuevo entorno
        for (int i = 0; i < parametros.size(); i++) {
            Object argEvaluado = evaluarLisp(argumentos.get(i), entorno);
            nuevoEntorno.definirVariable(parametros.get(i), argEvaluado);
        }
        
        // Evalúa cada expresión del cuerpo, retornando la última
        Object resultado = null;
        for (Object expr : cuerpo) {
            resultado = evaluarLisp(expr, nuevoEntorno);
        }
        
        return resultado;
    }
    
    /**
     * @param operador
     * @param argumentos
     * @param entorno
     * @return
     * @throws Exception
     */
    private Object evaluarOperador(String operador, List<Object> argumentos, EntornoLisp entorno) throws Exception {
        // Si no hay argumentos da un error
        if (argumentos.isEmpty()) {
            if (operador.equals("+") || operador.equals("-")) throw new Exception("Se requiere al menos un argumento para sumar o restar");
            if (operador.equals("*")) throw new Exception("Se requiere al menos un argumento para multiplicar");
            if (operador.equals("/")) throw new Exception("Se requiere al menos un argumento para dividir");
            if (operador.equals("=")) throw new Exception("Se requiere al menos dos argumentos para comparar");
        }
        
        // Evalúa todos los argumentos
        List<Object> args = new ArrayList<>();
        for (Object arg : argumentos) {
            args.add(evaluarLisp(arg, entorno));
        }
        
        // Para operaciones  básicas, asumimos que todos los argumentos son números
        if (operador.equals("+")) {
            int resultado = 0;
            for (Object arg : args) {
                if (!(arg instanceof Integer)) {
                    throw new Exception("El operador + espera números enteros");
                }
                resultado += (Integer) arg;
            }
            return resultado;
        } 
        else if (operador.equals("-")) {
            if (args.isEmpty()) return 0;
            
            if (!(args.get(0) instanceof Integer)) {
                throw new Exception("El operador - espera números enteros");
            }
            
            int resultado = (Integer) args.get(0);
            
            // Si solo hay un argumento, es negativo
            if (args.size() == 1) {
                return -resultado;
            }
            
            // De lo contrario, resta
            for (int i = 1; i < args.size(); i++) {
                if (!(args.get(i) instanceof Integer)) {
                    throw new Exception("El operador - espera números enteros");
                }
                resultado -= (Integer) args.get(i);
            }
            return resultado;
        } 
        else if (operador.equals("*")) {
            int resultado = 1;
            for (Object arg : args) {
                if (!(arg instanceof Integer)) {
                    throw new Exception("El operador * espera números enteros");
                }
                resultado *= (Integer) arg;
            }
            return resultado;
        } 
        else if (operador.equals("/")) {
            if (!(args.get(0) instanceof Integer)) {
                throw new Exception("El operador / espera números enteros");
            }
            
            int resultado = (Integer) args.get(0);
            
            // División con un solo argumento (1/x)
            if (args.size() == 1) {
                if (resultado == 0) {
                    throw new Exception("División por cero");
                }
                return 1 / resultado;
            }
            
            // División normal
            for (int i = 1; i < args.size(); i++) {
                if (!(args.get(i) instanceof Integer)) {
                    throw new Exception("El operador / espera números enteros");
                }
                int divisor = (Integer) args.get(i);
                if (divisor == 0) {
                    throw new Exception("División por cero");
                }
                resultado /= divisor;
            }
            return resultado;
        }
        else if (operador.equals("=")) {
            // Se requieren al menos dos argumentos para la comparación
            if (args.size() < 2) {
                throw new Exception("El operador = requiere al menos dos argumentos para comparar");
            }
            
            // Tomamos el primer valor como referencia
            Object primerValor = args.get(0);
            
            // Comparamos todos los valores con el primero
            for (int i = 1; i < args.size(); i++) {
                // Si algún valor no es igual al primero, retornamos falso
                if (!sonIguales(primerValor, args.get(i))) {
                    return false;
                }
            }
            
            // Si todos los valores son iguales, retornamos verdadero
            return true;
        }
        
        // Si el operador no es reconocido
        throw new Exception("Operador no soportado: " + operador);
    }
}