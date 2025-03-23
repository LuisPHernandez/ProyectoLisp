// Clase main para hacer pruebas y ejemplos de uso
// Ejemplo FIBONACCI:(DEFUN FIBONACCI (N) (COND ((= N 0) 1) ((= N 1) 1) (T (+ (FIBONACCI (- N 1)) (FIBONACCI (- N 2))))))
// Ejemplo MALAN: (DEFUN MALAN (M N) (COND ((= N 0) 1) (T (* M (MALAN M (- N 1))))))
import java.util.Scanner;

public class Main {
    /**
     * @param args
     */
    public static void main(String[] args) {
        InterpreteLisp interprete = new InterpreteLisp();
        Scanner scanner = new Scanner(System.in);
        boolean ejecutando = true;
        
        System.out.println("Intérprete Lisp");
        System.out.println("Escriba 'salir' para terminar el programa");
        
        while (ejecutando) {
            System.out.print("\nlisp> ");
            String entrada = scanner.nextLine().trim();
            
            if (entrada.equalsIgnoreCase("salir")) {
                ejecutando = false;
                System.out.println("Programa finalizado");
            } else if (!entrada.isEmpty()) {
                try {
                    Object resultado = interprete.evaluar(entrada);
                    System.out.println(resultado);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }
        
        scanner.close();
    }
}