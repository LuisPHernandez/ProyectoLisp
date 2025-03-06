package ProyectoLisp;
import java.util.List;

public class Main {
    // Método main para hacer pruebas y ejemplos de uso
    public static void main(String[] args) {
        LexerLisp lexer = new LexerLisp();
        String expresion = "QUOTE(+ 2   (* \n  V 8 ))  ";
        List<Object> resultado = lexer.evaluarExpresion(expresion);

        System.out.println("Expresión válida: " + resultado.get(0));
        System.out.println("Tokens: " + resultado.get(1));
    }
}
