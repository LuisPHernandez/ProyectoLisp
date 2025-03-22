// Clase main para hacer pruebas y ejemplos de uso
public class Main {
    /**
     * @param args
     */
    public static void main(String[] args) {
        InterpreteLisp interprete = new InterpreteLisp();
        
        // Ejemplos de uso
        try {
            System.out.println(interprete.evaluar("(DEFUN FIBONACCI (N)" +
                                "(COND ((= N 0) 1)" +
                                "((= N 1) 1)" +
                                "(T (+ (FIBONACCI (- N 1))" + 
                                "(FIBONACCI (- N 2))))))"));
            System.out.println(interprete.evaluar("(FIBONACCI 10)"));
            System.out.println(interprete.evaluar("(DEFUN MALAN (M N)" +
                                "(COND ( (= N 0) 1)" +
                                "(T (* M (MALAN M (- N 1)))" +
                                ")" +
                                "))"));
            System.out.println(interprete.evaluar("(MALAN 2 5)"));
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}