package ECC;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.JTextArea;

public class Decoder {

    public static int PAD = 5;
    private HashMap<Point, Integer> pointTable;
    private List<String> steps;

    public Decoder(HashMap<Point, Integer> pointTable) {
        this.pointTable = pointTable;
        this.steps = new ArrayList<>();
    }

    public String decode(Matrix A, JTextArea textArea) {
        List<String> subKeys = Helpers.getSubKeys();
        for (String subKey : subKeys) {
            String[] array = subKey.split("\\/");
            String transformation = array[0];
            int op = Integer.parseInt(array[1]);
            int a = Integer.parseInt(array[2]);
            int b = Integer.parseInt(array[3]);
            int c = Integer.parseInt(array[4]);
            int d = Integer.parseInt(array[5]);
            if (transformation.equals("R")) {
                A.reverseRowTrasformation(a, c, d, op);
                A.reverseRowTrasformation(b, c, d, op);
            } else {
                A.reverseColumnTrasformation(a, c, d, op);
                A.reverseColumnTrasformation(b, c, d, op);
            }
            textArea.append("\nsub-key : " + subKey + "\n");
            textArea.append(String.valueOf(A));
        }
        return getPlainText(A, textArea);
    }

    public List<String> getSteps() {
        return steps;
    }

    private String getPlainText(Matrix A, JTextArea textArea) {
        String plaintText = "";
        textArea.append("\n4) Convert the matrix M to a list of points");
        
        // Suponiendo que A.toPoints() devuelve una lista de puntos
        List<Point> pointsList = A.toPoints();

        // Construir una cadena con las coordenadas x e y de cada punto en una sola fila
        StringBuilder pointsString = new StringBuilder();
        for (Point point : pointsList) {
            pointsString.append("(").append(point.getX()).append(", ").append(point.getY()).append(") ");
        }

        // Agregar la cadena de puntos al JTextArea
        textArea.append("\n" + pointsString.toString() + "\n");
        
        for (Point p : A.toPoints()) {
            if (pointTable.get(p) != null) {
                int asciCode = pointTable.get(p);
                plaintText += Character.toString((char) asciCode);
            } else {
                plaintText += "$";
            }
        }
        return plaintText;
    }
}
