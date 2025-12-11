package matrixcalculator;

public class MatrixCalculator {

    public static Matrix performOperation(Matrix matrixA, Matrix matrixB, String operation)
            throws MatrixException {
        switch (operation) {
            case "ADD": return matrixA.add(matrixB);
            case "SUBTRACT": return matrixA.subtract(matrixB);
            case "MULTIPLY": return matrixA.multiply(matrixB);
            default: throw new MatrixException("Неизвестная операция: " + operation);
        }
    }

    public static double calculateDeterminant(Matrix matrix) throws MatrixException {
        return matrix.determinant();
    }
}