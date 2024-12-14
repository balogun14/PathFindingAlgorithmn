import exceptions.InvalidArgumentError;
import exceptions.PathNotFoundError;

import java.util.ArrayList;
import java.util.List;

public class PathFinder {

    public Path run(Matrix matrix, List<Sequence> sequences, int maxPathLength)
            throws InvalidArgumentError, PathNotFoundError {
        // Validate input parameters
        validateInput(matrix, sequences, maxPathLength);

        // Try all possible top row starting positions
        Path bestPath = null;
        for (int startCol = 0; startCol < matrix.getColumns(); startCol++) {
            Path currentPath = findBestPath(matrix, sequences, maxPathLength, startCol);

            // Update the best path if current path is better
            if (currentPath != null &&
                    (bestPath == null ||
                            currentPath.getTotalScore() > bestPath.getTotalScore() ||
                            (currentPath.getTotalScore() == bestPath.getTotalScore() &&
                                    currentPath.getPositions().size() < bestPath.getPositions().size()))) {
                bestPath = currentPath;
            }
        }

        if (bestPath == null) {
            throw new PathNotFoundError("No valid path found");
        }

        return bestPath;
    }

    private void validateInput(Matrix matrix, List<Sequence> sequences, int maxPathLength)
            throws InvalidArgumentError {
        if (matrix.getRows() == 0 || matrix.getColumns() == 0) {
            throw new InvalidArgumentError("Matrix cannot be empty");
        }

        if (sequences.isEmpty()) {
            throw new InvalidArgumentError("At least one sequence is required");
        }

        for (Sequence seq : sequences) {
            if (seq.getCodes().isEmpty()) {
                throw new InvalidArgumentError("Sequences cannot be empty");
            }

            if (seq.getScore() <= 0) {
                throw new InvalidArgumentError("Sequence score must be greater than zero");
            }
        }

        if (maxPathLength <= 0) {
            throw new InvalidArgumentError("Maximum path length must be greater than zero");
        }
    }

    private Path findBestPath(Matrix matrix, List<Sequence> sequences, int maxPathLength, int startColumn) {
        Path initialPath = new Path();
        initialPath.addPosition(new Position(0, startColumn));

        return backtrackPath(matrix, sequences, initialPath, maxPathLength);
    }

    private Path backtrackPath(Matrix matrix, List<Sequence> sequences, Path currentPath, int maxPathLength) {
        if (currentPath.getPositions().size() > maxPathLength) {
            return null;
        }

        // Check if all sequences can be completed
        List<Sequence> remainingSequences = findRemainingSequences(sequences, currentPath);
        if (remainingSequences.isEmpty()) {
            return currentPath;
        }

        Path bestPath = null;
        Position lastPos = currentPath.getPositions().get(currentPath.getPositions().size() - 1);

        // Try moving vertically or horizontally
        List<Position> possibleMoves = getPossibleMoves(matrix, currentPath);

        for (Position move : possibleMoves) {
            if (!currentPath.getPositions().contains(move)) {
                Path newPath = new Path(currentPath);
                newPath.addPosition(move);

                // Try to complete sequences with current path
                updateCompletedSequences(matrix,newPath, sequences);

                Path resultPath = backtrackPath(matrix, sequences, newPath, maxPathLength);

                // Update the best path based on scoring rules
                if (resultPath != null &&
                        (bestPath == null ||
                                resultPath.getTotalScore() > bestPath.getTotalScore() ||
                                (resultPath.getTotalScore() == bestPath.getTotalScore() &&
                                        resultPath.getPositions().size() < bestPath.getPositions().size()))) {
                    bestPath = resultPath;
                }
            }
        }

        return bestPath;
    }

    private List<Sequence> findRemainingSequences(List<Sequence> sequences, Path currentPath) {
        List<Sequence> remaining = new ArrayList<>(sequences);
        remaining.removeAll(currentPath.getCompletedSequences());
        return remaining;
    }

    private void updateCompletedSequences(Matrix matrix, Path path, List<Sequence> sequences) {
        for (Sequence seq : sequences) {
            if (!path.getCompletedSequences().contains(seq)) {
                if (isSequenceCompleted(matrix, path, seq)) {
                    path.addCompletedSequence(seq);
                }
            }
        }
    }

    private boolean isSequenceCompleted(Matrix matrix, Path path, Sequence sequence) {
        List<Position> positions = path.getPositions();
        if (positions.size() < sequence.getCodes().size()) {
            return false;
        }

        for (int i = 0; i <= positions.size() - sequence.getCodes().size(); i++) {
            boolean match = true;
            for (int j = 0; j < sequence.getCodes().size(); j++) {
                Position pos = positions.get(i + j);
                String matrixCode = matrix.getCell(pos.getRow(), pos.getColumn());

                if (!matrixCode.equals(sequence.getCodes().get(j))) {
                    match = false;
                    break;
                }
            }

            if (match) {
                return true;
            }
        }

        return false;
    }

    private List<Position> getPossibleMoves(Matrix matrix, Path currentPath) {
        List<Position> moves = new ArrayList<>();
        Position lastPos = currentPath.getPositions().get(currentPath.getPositions().size() - 1);

        // Move vertically
        for (int row = 0; row < matrix.getRows(); row++) {
            if (row != lastPos.getRow()) {
                moves.add(new Position(row, lastPos.getColumn()));
            }
        }

        // Move horizontally
        for (int col = 0; col < matrix.getColumns(); col++) {
            if (col != lastPos.getColumn()) {
                moves.add(new Position(lastPos.getRow(), col));
            }
        }

        return moves;
    }
}
