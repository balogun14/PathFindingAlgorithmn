import java.util.*;

public class Path {
    private List<Position> positions;
    private List<Sequence> completedSequences;
    private int totalScore;

    public Path() {
        this.positions = new ArrayList<>();
        this.completedSequences = new ArrayList<>();
        this.totalScore = 0;
    }

    public Path(Path other) {
        this.positions = new ArrayList<>(other.positions);
        this.completedSequences = new ArrayList<>(other.completedSequences);
        this.totalScore = other.totalScore;
    }

    public void addPosition(Position pos) {
        positions.add(pos);
    }

    public void addCompletedSequence(Sequence seq) {
        completedSequences.add(seq);
        totalScore += seq.getScore();
    }

    public List<Position> getPositions() {
        return positions;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public List<Sequence> getCompletedSequences() {
        return completedSequences;
    }
}
