package four;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class ConnectFour extends JFrame {
    final int rows = 6;
    final int cols = 7;
    final int interval = 4;
    JButton[][] cells = new JButton[rows][cols];
    JButton[][] rotatedCells = new JButton[cols][rows];
    ArrayList<JButton>[] diagonalCells = new ArrayList[rows * 2];
    LinkedHashMap<Character, Integer> filledMap = new LinkedHashMap<>();
    boolean isX = true;
    boolean finished = false;

    public ConnectFour() {
        JPanel cellsPanel = new JPanel();
        JPanel resetPanel = new JPanel();
        cellsPanel.setLayout(new GridLayout(rows, cols));

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setVisible(true);
        setLayout(new BorderLayout());
        setTitle("Connect Four");
        add(cellsPanel, BorderLayout.CENTER);
        add(resetPanel, BorderLayout.SOUTH);

        addCells(cellsPanel);
        getRotateCells();
        getDiagonalCells();
        addResetButton(resetPanel);
    }

    private void addCells(JPanel cellsPanel) {
        int rowLabel = rows;

        for (int i = 0; i < rows; i++) {
            char colLabel = 'A';

            for (int j = 0; j < cols; j++) {
                JButton cell = getCell(colLabel, rowLabel);

                cells[i][j] = cell;
                cellsPanel.add(cell);

                colLabel++;
            }

            rowLabel--;
        }
    }

    private JButton getCell(char colLabel, int rowLabel) {
        JButton cell = new JButton();
        String name = "Button" + colLabel + rowLabel;

        cell.setFocusPainted(false);
        cell.setBackground(Color.white);
        cell.setText(" ");
        cell.setName(name);
        cell.addActionListener(e -> {
            if (!finished) {
                placeMove(colLabel);
                checkHorizontal();
                checkVertical();
                checkDiagonal();
            }
        });

        return cell;
    }

    private void addResetButton(JPanel resetPanel) {
        JButton resetButton = new JButton("Reset");
        resetButton.setName("ButtonReset");
        resetButton.addActionListener(e -> reset());
        resetPanel.add(resetButton);
    }

    private void reset() {
        for (var row : cells) {

            for (var cell : row) {
                cell.setText(" ");
                cell.setBackground(Color.white);
            }
        }

        filledMap.clear();
        finished = false;
        isX = true;
    }

    private void placeMove(char col) {
        int filledRow = filledMap.getOrDefault(col, 0);
        int freeRow = filledRow + 1;
        filledMap.put(col, freeRow);
        String freeCellName = "Button" + col + freeRow;

        for (var row : cells) {

            for (var cell : row) {
                if (cell.getName().equals(freeCellName)) {
                    cell.setText(isX ? "X" : "O");
                    isX = !isX;
                    break;
                }
            }
        }
    }

    private void checkHorizontal() {
        for (var row : cells) {
            checkConnected(row);
        }
    }

    private void checkVertical() {
        for (var row : rotatedCells) {
            checkConnected(row);
        }
    }

    private void checkDiagonal() {
        for (var row : diagonalCells) {
            checkConnected(row.toArray(new JButton[row.size()]));
        }
    }

    private void checkConnected(JButton[] arr) {
        int upper = interval - 1;
        int limit = arr.length - upper;

        for (int lower = 0; lower < limit; lower++) {
            var range = Arrays.stream(Arrays.copyOfRange(arr, lower, upper + 1)).toList();
            var texts = range.stream().map(JButton::getText).toList();
            upper++;

            int xCount = Collections.frequency(texts, "X");
            int oCount = Collections.frequency(texts, "O");

            if (xCount == 4 || oCount == 4) {
                range.forEach(cell -> cell.setBackground(Color.green));
                finished = true;
                break;
            }
        }
    }

    private void getRotateCells() {

        for (int i = 0; i < rotatedCells.length; i++) {

            for (int j = 0; j < rotatedCells[i].length; j++) {
                rotatedCells[i][j] = cells[j][i];
            }
        }
    }

    private void getDiagonalCells() {
        int limit = rows - (interval - 1);
        int count = rows;
        int index = 0;

        for (int i = 0; i < diagonalCells.length; i++) {
            diagonalCells[i] = new ArrayList<>();
        }

        for (int i = 0; i < limit; i++) {

            for (int j = 0; j < count; j++) {
                diagonalCells[index].add(cells[j + i][j]);
            }
            index++;

            for (int j = 0; j < count; j++) {
                diagonalCells[index].add(cells[j][j + i + 1]);
            }
            index++;

            for (int j = 0; j < count; j++) {
                diagonalCells[index].add(cells[j][count - 1 - j]);
            }
            index++;

            for (int j = 0; j < count; j++) {
                diagonalCells[index].add(cells[rows - count][rows - j]);
            }
            index++;
            count--;
        }
    }
}