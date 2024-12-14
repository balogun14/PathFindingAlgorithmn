import exceptions.InvalidArgumentError;
import exceptions.PathNotFoundError;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        try {
            // Create a sample Matrix
            String[][] matrixData = {
                    {"3A", "3A", "10", "9B", "72"},
                    {"9B", "72", "3A", "10", "72"},
                    {"10", "3A", "3A", "3A","10"},
                    {"3A", "10", "3A", "9B", "72"},
                    {"10", "10", "3A", "72", "72"},
            };
            Matrix matrix = new Matrix(matrixData);

            // Create sample Sequences
            List<Sequence> sequences = new ArrayList<>();

            // Sequence 1: Looking for "A", "F", "K"
            Sequence seq1 = new Sequence(Arrays.asList("3A", "10", "9B"), 10);

            // Sequence 2: Looking for "D", "N", "L"
           // Sequence seq2 = new Sequence(Arrays.asList("D", "N", "L"), 15);

            sequences.add(seq1);

            // Maximum path length
            int maxPathLength = 3;

            // Create PathFinder and run
            PathFinder pathFinder = new PathFinder();
            Path bestPath = pathFinder.run(matrix, sequences, maxPathLength);

            // Print out the results
            System.out.println("Best Path Found:");
            System.out.println("Total Score: " + bestPath.getTotalScore());

            System.out.println("Path Positions:");
            for (Position pos : bestPath.getPositions()) {
                System.out.println("Row: " + pos.getRow() + ", Column: " + pos.getColumn() +
                        " - Value: " + matrix.getCell(pos.getRow(), pos.getColumn()));
            }

            System.out.println("Completed Sequences:");
            for (Sequence seq : bestPath.getCompletedSequences()) {
                System.out.println(seq.getCodes());
            }

        } catch (InvalidArgumentError | PathNotFoundError e) {
            e.printStackTrace();
        }
    }
}