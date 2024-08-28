import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Thread {

    // Verifica se é seguro colocar uma rainha na posição (row, col) no tabuleiro.
    public static boolean isSafe(int[] board, int row, int col) {
        for (int i = 0; i < col; i++) {
            if (board[i] == row || Math.abs(board[i] - row) == Math.abs(i - col)) {
                return false;
            }
        }
        return true;
    }

    // Resolve o problema das N Rainhas de forma recursiva a partir de uma coluna específica.
    public static void solveNQueensParallel(int[] board, int col, List<int[]> results) {
        if (col == board.length) {
            results.add(board.clone());
            return;
        }

        for (int i = 0; i < board.length; i++) {
            if (isSafe(board, i, col)) {
                board[col] = i;
                solveNQueensParallel(board, col + 1, results);
            }
        }
    }

    // Cria uma nova tarefa para resolver o problema das N Rainhas a partir de uma coluna inicial.
    public static List<int[]> solveNQueensTask(int[] board, int startCol) {
        List<int[]> results = new ArrayList<>();
        solveNQueensParallel(board, startCol, results);
        return results;
    }

    // Escreve os resultados das soluções e o tempo de execução em um arquivo.
    public static void writeResultsToFile(List<int[]> results, long executionTime, int n) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("solucoes_paralelo.txt"))) {
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

            writer.write("Tempo de execução (paralelo): " + (executionTime / 1000.0) + " segundos\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Resolve o problema das N Rainhas utilizando múltiplas threads para processamento paralelo.
    public static void solveNQueens(int n) {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<Future<List<int[]>>> futures = new ArrayList<>();
        List<int[]> results = new ArrayList<>();

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < n; i++) {
            final int[] board = new int[n];
            board[0] = i;
            futures.add(executor.submit(() -> solveNQueensTask(board, 1)));
        }

        for (Future<List<int[]>> future : futures) {
            try {
                results.addAll(future.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();

        long endTime = System.currentTimeMillis();
        writeResultsToFile(results, endTime - startTime, n);
    }

    public static void main(String[] args) {
        int n = 16;
        solveNQueens(n);
    }
}