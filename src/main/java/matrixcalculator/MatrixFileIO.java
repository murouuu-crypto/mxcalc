package matrixcalculator;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MatrixFileIO {

    public static Matrix readFromFile(String filePath) throws MatrixException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<double[]> rowsList = new ArrayList<>();
            String line;
            int cols = -1;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split("\\s+");
                if (cols == -1) {
                    cols = parts.length;
                } else if (parts.length != cols) {
                    throw new MatrixException("Несовместимое количество столбцов в строке");
                }

                double[] row = new double[cols];
                for (int i = 0; i < cols; i++) {
                    try {
                        row[i] = Double.parseDouble(parts[i]);
                    } catch (NumberFormatException e) {
                        throw new MatrixException("Неверный формат числа: " + parts[i]);
                    }
                }
                rowsList.add(row);
            }

            if (rowsList.isEmpty()) {
                throw new MatrixException("Файл пуст");
            }

            double[][] data = new double[rowsList.size()][cols];
            for (int i = 0; i < rowsList.size(); i++) {
                data[i] = rowsList.get(i);
            }

            return new Matrix(data);

        } catch (IOException e) {
            throw new MatrixException("Ошибка чтения файла: " + e.getMessage());
        }
    }

    public static void writeToFile(Matrix matrix, String filePath) throws MatrixException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            for (int i = 0; i < matrix.getRows(); i++) {
                for (int j = 0; j < matrix.getCols(); j++) {
                    writer.printf("%.2f", matrix.get(i, j));
                    if (j < matrix.getCols() - 1) writer.print(" ");
                }
                writer.println();
            }
        } catch (IOException e) {
            throw new MatrixException("Ошибка записи в файл: " + e.getMessage());
        }
    }
}