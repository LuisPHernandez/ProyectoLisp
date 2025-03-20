import java.util.List;
import java.util.ArrayList;

public class ParserLisp {
    /**
     *
     */
    private List<String> tokens;
    /**
     *
     */
    private int posicionActual;

    /**
     * @param tokens
     */
    public ParserLisp(List<String> tokens) {
        this.tokens = tokens;
        this.posicionActual = 0;
    }

    /**
     * @return
     */
    public Object parseExpresion() {
        // Si no hay tokens se devuelve null
        if (this.tokens.isEmpty()) {
            return null;
        }
        // Se obtiene el token en la posición actual y se pasa el indicador a la siguiente posición
        String token = tokens.get(posicionActual);
        posicionActual++;
        
        // Si es un paréntesis abierto, se analiza como una lista
        if (token.equals("(")) {
            return parseLista();
        } 
        // Si no lo es, se devuelve el valor del token actual
        else {
            return parseValor(token);
        }
    }

    /**
     * @return
     */
    public List<Object> parseLista() {
        List<Object> lista = new ArrayList<>();
        
        // Analiza recursivamente hasta encontrar un paréntesis cerrado o hasta llegar al final de los tokens
        while (posicionActual < tokens.size() && !tokens.get(posicionActual).equals(")")) {
            Object elemento = parseExpresion();
            lista.add(elemento);
        }
        
        // Avanza despues del paréntesis cerrado
        if (posicionActual < tokens.size() && tokens.get(posicionActual).equals(")")) {
            posicionActual++;
        }
        
        return lista;
    }

    /**
     * @param token
     * @return
     */
    public Object parseValor(String token) {
        // Intenta convertir el token a un entero para ver si es un número
        // De esta manera el token se podrá operar más facilmente por las siguientes fases del proyecto
        try {
            return Integer.parseInt(token);
        } catch (NumberFormatException e) {
            // Si no se puede convertir, lo devuelve como string
            return token;
        }
    }
}