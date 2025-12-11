package matrixcalculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class Main {
    private static Matrix matrixA;
    private static Matrix matrixB;
    private static JTextArea displayArea;
    private static JLabel statusLabel;
    private static JTextField filePathAField;
    private static JTextField filePathBField;

    public static void main(String[] args) {
        // Запуск GUI в потоке обработки событий Swing
        SwingUtilities.invokeLater(Main::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        // Главное окно
        JFrame frame = new JFrame("Калькулятор матриц - Курсовая работа");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        // Панель заголовка
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("МАТРИЧНЫЙ КАЛЬКУЛЯТОР");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 100, 0));
        titlePanel.add(titleLabel);

        // Панель ввода файлов
        JPanel filePanel = new JPanel(new GridLayout(4, 1, 5, 5));
        filePanel.setBorder(BorderFactory.createTitledBorder("Загрузка матриц"));

        // Матрица A
        JPanel panelA = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelA.add(new JLabel("Матрица A:"));
        filePathAField = new JTextField(30);
        panelA.add(filePathAField);
        JButton browseAButton = new JButton("Обзор...");
        panelA.add(browseAButton);

        // Матрица B
        JPanel panelB = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelB.add(new JLabel("Матрица B:"));
        filePathBField = new JTextField(30);
        panelB.add(filePathBField);
        JButton browseBButton = new JButton("Обзор...");
        panelB.add(browseBButton);

        // Кнопки загрузки
        JPanel loadButtonsPanel = new JPanel();
        JButton loadAButton = new JButton("Загрузить A");
        JButton loadBButton = new JButton("Загрузить B");
        JButton loadBothButton = new JButton("Загрузить обе");
        loadButtonsPanel.add(loadAButton);
        loadButtonsPanel.add(loadBButton);
        loadButtonsPanel.add(loadBothButton);

        filePanel.add(panelA);
        filePanel.add(panelB);
        filePanel.add(loadButtonsPanel);

        // Панель отображения
        JPanel displayPanel = new JPanel(new BorderLayout());
        displayPanel.setBorder(BorderFactory.createTitledBorder("Матрицы и результаты"));

        displayArea = new JTextArea(15, 50);
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        displayArea.setText("Здесь будут отображаться матрицы и результаты операций.\n\n");

        JScrollPane scrollPane = new JScrollPane(displayArea);
        displayPanel.add(scrollPane, BorderLayout.CENTER);

        // Панель операций
        JPanel operationPanel = new JPanel();
        operationPanel.setBorder(BorderFactory.createTitledBorder("Операции"));

        JButton addButton = createOperationButton("Сложение", "A + B", Color.GREEN);
        JButton subtractButton = createOperationButton("Вычитание", "A - B", Color.ORANGE);
        JButton multiplyButton = createOperationButton("Умножение", "A × B", Color.CYAN);
        JButton determinantButton = createOperationButton("Определитель", "det(A), det(B)", Color.MAGENTA);

        operationPanel.add(addButton);
        operationPanel.add(subtractButton);
        operationPanel.add(multiplyButton);
        operationPanel.add(determinantButton);

        // Панель управления
        JPanel controlPanel = new JPanel();
        JButton clearButton = new JButton("Очистить все");
        clearButton.setBackground(Color.RED);
        clearButton.setForeground(Color.WHITE);
        JButton saveButton = new JButton("Сохранить результат");
        saveButton.setBackground(Color.BLUE);
        saveButton.setForeground(Color.WHITE);

        controlPanel.add(clearButton);
        controlPanel.add(saveButton);

        // Панель статуса
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusLabel = new JLabel("Готов к работе");
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        statusLabel.setForeground(Color.BLUE);
        statusPanel.add(statusLabel, BorderLayout.WEST);

        // Добавление всех панелей
        frame.add(titlePanel, BorderLayout.NORTH);
        frame.add(filePanel, BorderLayout.CENTER);
        frame.add(displayPanel, BorderLayout.EAST);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(operationPanel, BorderLayout.NORTH);
        southPanel.add(controlPanel, BorderLayout.CENTER);
        southPanel.add(statusPanel, BorderLayout.SOUTH);

        frame.add(southPanel, BorderLayout.SOUTH);

        // Обработчики событий
        browseAButton.addActionListener(e -> chooseFile(filePathAField));
        browseBButton.addActionListener(e -> chooseFile(filePathBField));

        loadAButton.addActionListener(e -> loadMatrix('A', filePathAField.getText()));
        loadBButton.addActionListener(e -> loadMatrix('B', filePathBField.getText()));
        loadBothButton.addActionListener(e -> {
            loadMatrix('A', filePathAField.getText());
            loadMatrix('B', filePathBField.getText());
        });

        addButton.addActionListener(e -> performOperation("ADD"));
        subtractButton.addActionListener(e -> performOperation("SUBTRACT"));
        multiplyButton.addActionListener(e -> performOperation("MULTIPLY"));
        determinantButton.addActionListener(e -> calculateDeterminants());

        clearButton.addActionListener(e -> clearAll());
        saveButton.addActionListener(e -> saveResult());

        // Настройка окна
        frame.pack();
        frame.setLocationRelativeTo(null); // Центрирование
        frame.setMinimumSize(new Dimension(900, 700));
        frame.setVisible(true);
    }

    private static JButton createOperationButton(String text, String tooltip, Color color) {
        JButton button = new JButton(text);
        button.setToolTipText(tooltip);
        button.setBackground(color);
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setPreferredSize(new Dimension(120, 40));
        return button;
    }

    private static void chooseFile(JTextField field) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Выберите файл с матрицей");
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            field.setText(fileChooser.getSelectedFile().getAbsolutePath());
        }
    }

    private static void loadMatrix(char matrixName, String filePath) {
        if (filePath.isEmpty()) {
            showError("Укажите путь к файлу для матрицы " + matrixName);
            return;
        }

        try {
            Matrix matrix = MatrixFileIO.readFromFile(filePath);

            if (matrixName == 'A') {
                matrixA = matrix;
                displayArea.append("=== МАТРИЦА A ЗАГРУЖЕНА ===\n");
            } else {
                matrixB = matrix;
                displayArea.append("=== МАТРИЦА B ЗАГРУЖЕНА ===\n");
            }

            displayArea.append("Размер: " + matrix.getRows() + "×" + matrix.getCols() + "\n");
            displayArea.append(matrix.toFormattedString() + "\n");

            setStatus("Матрица " + matrixName + " загружена успешно", Color.GREEN);

        } catch (MatrixException e) {
            showError("Ошибка загрузки матрицы " + matrixName + ": " + e.getMessage());
        }
    }

    private static void performOperation(String operation) {
        if (matrixA == null || matrixB == null) {
            showError("Загрузите обе матрицы перед выполнением операции");
            return;
        }

        try {
            Matrix result = MatrixCalculator.performOperation(matrixA, matrixB, operation);

            String operationName = "";
            switch (operation) {
                case "ADD": operationName = "СЛОЖЕНИЯ"; break;
                case "SUBTRACT": operationName = "ВЫЧИТАНИЯ"; break;
                case "MULTIPLY": operationName = "УМНОЖЕНИЯ"; break;
            }

            displayArea.append("=== РЕЗУЛЬТАТ " + operationName + " ===\n");
            displayArea.append("Размер: " + result.getRows() + "×" + result.getCols() + "\n");
            displayArea.append(result.toFormattedString() + "\n");

            setStatus("Операция выполнена успешно", Color.GREEN);

        } catch (MatrixException e) {
            showError("Ошибка операции: " + e.getMessage());
        }
    }

    private static void calculateDeterminants() {
        try {
            if (matrixA != null && matrixA.getRows() == matrixA.getCols()) {
                double detA = MatrixCalculator.calculateDeterminant(matrixA);
                displayArea.append("Определитель матрицы A: " + String.format("%.4f", detA) + "\n");
            } else if (matrixA != null) {
                displayArea.append("Матрица A не квадратная, определитель не вычисляется\n");
            }

            if (matrixB != null && matrixB.getRows() == matrixB.getCols()) {
                double detB = MatrixCalculator.calculateDeterminant(matrixB);
                displayArea.append("Определитель матрицы B: " + String.format("%.4f", detB) + "\n");
            } else if (matrixB != null) {
                displayArea.append("Матрица B не квадратная, определитель не вычисляется\n");
            }

            displayArea.append("\n");
            setStatus("Определители вычислены", Color.BLUE);

        } catch (MatrixException e) {
            showError("Ошибка вычисления определителя: " + e.getMessage());
        }
    }

    private static void clearAll() {
        matrixA = null;
        matrixB = null;
        filePathAField.setText("");
        filePathBField.setText("");
        displayArea.setText("Все данные очищены.\n\n");
        setStatus("Готов к работе", Color.BLUE);
    }

    private static void saveResult() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Сохранить результат");

        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            if (!filePath.endsWith(".txt")) {
                filePath += ".txt";
            }

            try {
                String content = displayArea.getText();
                if (content.trim().isEmpty()) {
                    showError("Нет данных для сохранения");
                    return;
                }

                try (PrintWriter writer = new PrintWriter(filePath)) {
                    writer.println("=== РЕЗУЛЬТАТЫ РАБОТЫ МАТРИЧНОГО КАЛЬКУЛЯТОРА ===");
                    writer.println(content);
                }

                setStatus("Результат сохранен в: " + filePath, Color.GREEN);

            } catch (Exception e) {
                showError("Ошибка сохранения: " + e.getMessage());
            }
        }
    }

    private static void setStatus(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);
    }

    private static void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Ошибка", JOptionPane.ERROR_MESSAGE);
        setStatus("Ошибка: " + message, Color.RED);
    }
}