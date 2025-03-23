import static org.junit.Assert.assertEquals;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class FuncionLispTest {
    /**
     * 
     */
    @Test
    public void testConstructorAndGetters() {
        // Prepara los datos
        List<String> parametros = Arrays.asList("a", "b", "c");
        List<Object> cuerpo = Arrays.asList(
                Arrays.asList("+", "a", "b"),  // First expression: (+ a b)
                Arrays.asList("*", "c", 2)     // Second expression: (* c 2)
        );

        // Crea una instancia
        FuncionLisp funcion = new FuncionLisp(parametros, cuerpo);

        // Test getParametros
        assertEquals("los parametros deben ser iguales", parametros, funcion.getParametros());

        // Test getCuerpo
        assertEquals("el cuerpo debe de ser igual", cuerpo, funcion.getCuerpo());
    }
}
