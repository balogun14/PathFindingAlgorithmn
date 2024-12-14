import java.util.List;

public class Sequence{
    private List<String> codes;
    private int score;

    public Sequence(List<String> codes, int score) {
        this.codes = codes;
        this.score = score;
    }

    public List<String> getCodes() {
        return codes;
    }

    public int getScore() {
        return score;
    }
}


