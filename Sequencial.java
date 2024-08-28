import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Sequencial {

    // Verifica se é seguro colocar uma rainha na posição (row, col) do tabuleiro.
    public static boolean isSafe(int[] board, int row, int col) {
        for (int i = 0; i < col; i++) {
            if (board[i] == row || Math.abs(board[i] - row) == Math.abs(i - col)) {
                return false;
            }
        }
        return true;
    }

    // Resolve o problema das N Rainhas de forma sequencial e armazena todas as soluções possíveis.
    public static void solveNQueensSequential(int[] board, int col, List<int[]> results) {
        if (col == board.length) {
            results.add(board.clone());
            return;
        }

        for (int i = 0; i < board.length; i++) {
            if (isSafe(board, i, col)) {
                board[col] = i;
                solveNQueensSequential(board, col + 1, results);
            }
        }
    }

    // Escreve as soluções encontradas e o tempo de execução em um arquivo.
    public static void writeResultsToFile(List<int[]> results, long executionTime, int n) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("solucoes_sequencial.txt"))) {
            writer.write(results.size() + " soluções encontradas para " + n + " rainhas:\n\n");

            // for (int[] solution : results) {
            //     for (int row : solution) {
            //         writer.write("[");
            //         for (int i = 0; i < n; i++) {
            //             writer.write((i == row ? "1" : "0") + ", ");
            //         }
            //         writer.write("]\n");
            //     }
            //     writer.write("\n");
            // }

            writer.write("Tempo de execução (sequencial): " + (executionTime / 1000.0) + " segundos\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Inicia o processo de solução das N Rainhas e mede o tempo de execução.
    public static void solveNQueens(int n) {
        int[] board = new int[n];
        List<int[]> results = new ArrayList<>();

        long startTime = System.currentTimeMillis();
        solveNQueensSequential(board, 0, results);
        long endTime = System.currentTimeMillis();

        writeResultsToFile(results, endTime - startTime, n);
    }

    public static void main(String[] args) {
        int n = 16;
        solveNQueens(n);
    }
}
