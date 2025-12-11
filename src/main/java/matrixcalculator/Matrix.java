package matrixcalculator;

public class Matrix {
    private double[][] data;
    private int rows;
    private int cols;

    public Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.data = new double[rows][cols];
    }

    public Matrix(double[][] data) {
        this.rows = data.length;
        this.cols = data[0].length;
        this.data = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            System.arraycopy(data[i], 0, this.data[i], 0, cols);
        }
    }

    public int getRows() { return rows; }
    public int getCols() { return cols; }
    public double get(int i, int j) { return data[i][j]; }
    public void set(int i, int j, double value) { data[i][j] = value; }
    public double[][] getData() { return data; }

    public Matrix add(Matrix other) throws MatrixException {
        if (this.rows != other.rows || this.cols != other.cols) {
            throw new MatrixException("Матрицы должны быть одного размера для сложения");
        }

        Matrix result = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result.set(i, j, this.get(i, j) + other.get(i, j));
            }
        }
        return result;
    }

    public Matrix subtract(Matrix other) throws MatrixException {
        if (this.rows != other.rows || this.cols != other.cols) {
            throw new MatrixException("Матрицы должны быть одного размера для вычитания");
        }

        Matrix result = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result.set(i, j, this.get(i, j) - other.get(i, j));
            }
        }
        return result;
    }

    public Matrix multiply(Matrix other) throws MatrixException {
        if (this.cols != other.rows) {
            throw new MatrixException("Количество столбцов первой матрицы должно равняться количеству строк второй матрицы");
        }

        Matrix result = new Matrix(this.rows, other.cols);
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < other.cols; j++) {
                double sum = 0;
                for (int k = 0; k < this.cols; k++) {
                    sum += this.get(i, k) * other.get(k, j);
                }
                result.set(i, j, sum);
            }
        }
        return result;
    }

    public double determinant() throws MatrixException {
        if (rows != cols) {
            throw new MatrixException("Матрица должна быть квадратной для вычисления определителя");
        }
        return calculateDeterminant(this.data);
    }

    private double calculateDeterminant(double[][] matrix) {
        int n = matrix.length;
        if (n == 1) return matrix[0][0];
        if (n == 2) return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];

        double det = 0;
        for (int i = 0; i < n; i++) {
            double[][] minor = getMinor(matrix, 0, i);
            det += Math.pow(-1, i) * matrix[0][i] * calculateDeterminant(minor);
        }
        return det;
    }

    private double[][] getMinor(double[][] matrix, int row, int col) {
        int n = matrix.length;
        double[][] minor = new double[n-1][n-1];
        int r = 0;
        for (int i = 0; i < n; i++) {
            if (i == row) continue;
            int c = 0;
            for (int j = 0; j < n; j++) {
                if (j == col) continue;
                minor[r][c] = matrix[i][j];
                c++;
            }
            r++;
        }
        return minor;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                sb.append(String.format("%8.2f", data[i][j]));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public String toFormattedString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            sb.append("[ ");
            for (int j = 0; j < cols; j++) {
                sb.append(String.format("%6.2f ", data[i][j]));
            }
            sb.append("]\n");
        }
        return sb.toString();
    }
}