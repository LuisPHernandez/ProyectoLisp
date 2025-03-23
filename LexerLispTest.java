import static org.junit.Assert.*;
import java.util.List;
import org.junit.Test;

public class LexerLispTest {
    /**
     * 
     */
    @Test
    public void testEvaluarExpresionValida() {
        // Crea una instancia de LexerLisp
        LexerLisp lexer = new LexerLisp();

        // Expresion que se considera valida
        String expresionValida = "(+ a b)";
        List<Object> resultadoValido = lexer.evaluarExpresion(expresionValida);

        // Verificar que el primer elemento es "true"
        assertTrue("La expresion debe de ser valida.", (Boolean) resultadoValido.get(0));

        // Verificar los tokens
        List<String> tokensEsperados = List.of("(", "+", "a", "b", ")");
        assertEquals("Los tokens no coinciden", tokensEsperados, (List<String>) resultadoValido.get(1));
    }

    /**
     * 
     */
    @Test
    public void testEvaluarExpresionInvalida() {
        // Crea una instancia de LexerLisp
        LexerLisp lexer = new LexerLisp();

        // Expresion con paréntesis incorrectos
        String expresionInvalida = "(+ a b";
        List<Object> resultadoInvalido = lexer.evaluarExpresion(expresionInvalida);

        // Verificar que el primer elemento es "false"
        assertFalse("La expresión debe de ser invalida", (Boolean) resultadoInvalido.get(0));

        // Verificar los tokens
        List<String> tokensEsperadosInvalido = List.of("(", "+", "a", "b");
        assertEquals("Los tokens no coinciden", tokensEsperadosInvalido, (List<String>) resultadoInvalido.get(1));
    }
}
