import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class ParserLispTest {
    /**
     * 
     */
    @Test
    public void testParser() {
        // Prepara los datos
        List<String> tokens = Arrays.asList("(", "+", "a", "b", ")");

        // Crea una instancia
        ParserLisp parser = new ParserLisp(tokens);

        // Prueba el parser
        Object expresionLisp = parser.parseExpresion();
        // Verifica que la expresion sea correcta
        assertEquals("La expresion no es correcta", Arrays.asList("+", "a", "b"), expresionLisp);
    }
    
}
