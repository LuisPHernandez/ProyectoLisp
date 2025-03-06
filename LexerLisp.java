import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.*; 

public class LexerLisp {
    public List<Object> evaluarExpresion(String expresion) {
        // Se crea una lista para guardar los tokens de la expresion
        List<String> tokens = new ArrayList<>();

        // Se crean contadores para ver si todos los parentesis tienen pareja
        int parentesisAbiertos = 0, parentesisCerrados = 0;

        /* Se usa la clase Pattern de Java.util.regex para denotar una expresion regular con los patrones que se desean
        buscar en la expresion Lisp pasada por el usuario. Por ejemplo, //( busca un paréntesis en la expresion Lisp.
        Además, se usa Pattern.COMMENTS para que Pattern.compile ignore los espacios en blanco en la expresion regular,
        de esta manera podemos usar espacios para mejorar el orden y legibilidad de la regex sin afectar su funcionamiento.
        Este código fue asistido por la IA Claude para ayuda en la generación de expresiones regulares y manejo de tokens.
         */ 
        Pattern simbolosValidos = Pattern.compile(" \\( | \\) | [\\w+\\-*/]+ | \"[^\"]*\" ", Pattern.COMMENTS);

        // Crea un objeto Matcher que busca los patrones definidos (símbolos válidos) en la expresión Lisp
        Matcher buscadorDeTokens = simbolosValidos.matcher(expresion);

        // Recorre todos los tokens encontrados en la expresión, agregándolos a la lista y contando los parentesis
        while (buscadorDeTokens.find()) {
            // Agrega el token actual a la lista
            String token = buscadorDeTokens.group();
            tokens.add(token);
            if (token.equals("(")) parentesisAbiertos++;
            else if (token.equals(")")) parentesisCerrados++;
        }

        // Se determina si hay la misma cantidad de paréntesis abiertos que cerrados
        boolean esValido = parentesisAbiertos == parentesisCerrados;
        
        return Arrays.asList(esValido, tokens);
    }
}
