public class Matrix {
    private String[][] matrix;

    public Matrix(String[][] matrix) {
        this.matrix = matrix;
    }

    public int getRows() {
        return matrix.length;
    }

    public int getColumns() {
        return matrix[0].length;
    }

    public String getCell(int row, int col) {
        return matrix[row][col];
    }
}
