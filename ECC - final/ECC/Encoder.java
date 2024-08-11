package ECC;

import static ECC.Helpers.toBinary;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.JTextArea;


public class Encoder {

    private HashMap<Integer, Point> charTable;
    private List<String> steps;

    public Encoder(HashMap<Integer, Point> charTable) {
        this.charTable = charTable;
        this.steps = new ArrayList<>();
    }

    public Matrix encode(String plainText, JTextArea textArea) {
        Matrix mMatrix = createMatrix(plainText, textArea);
        textArea.append("\n2) Convert the list of points to a binary Matrix\n");
        textArea.append(String.valueOf(mMatrix));
        textArea.append("\n3) Matrix Scrambling");
        int w = new BigInteger(ECC.PAD, ECC.getRandom()).intValue();
        int[] bits = Helpers.toBinary(ECC.getRandom().nextInt(1024), ECC.PAD * 2);
        textArea.append("\nnumber of transformations, w = " + w);
        textArea.append("\nRandom sequence of bits, Bits = ");
        Helpers.print(bits, textArea);
        textArea.append("\n");
        int bit, i = 0;
        do {
            bit = bits[i];
            if (bit == 0) {
                mMatrix.scramble(true, textArea);
            } else {
                mMatrix.scramble(false, textArea);
            }
            if (i == bits.length - 1) {
                i = 0;
            }else{
                i++;
            }
            //textArea.append("Iteration" + String.valueOf(i) +": \n");
            textArea.append(String.valueOf(mMatrix));
            w--;
        } while (w > 0);
        return mMatrix;
    }

    public List<String> getSteps() {
        return steps;
    }

    private Matrix createMatrix(String plainText, JTextArea textArea) {
        List<Point> pList = new ArrayList<>();
        //get the respective point in elliptic curve for each character in plainText
        for (Character c : plainText.toCharArray()) {
            Point p = charTable.get((int) c.charValue());
            pList.add(p);
        }
        textArea.append("\n1) Convert m to a list of Points");
        
        // Iterar sobre la lista pList y agregar cada punto al JTextArea
        StringBuilder stringBuilder = new StringBuilder();
        for (Point p : pList) {
            stringBuilder.append("(").append(p.getX()).append(", ").append(p.getY()).append(") ");
        }
        // Agregar un separador entre el texto existente y la nueva cadena
        stringBuilder.insert(0, "\n"); // Agrega un salto de línea al inicio
        // Concatenar el nuevo texto con el texto existente
        textArea.append(stringBuilder.toString() + "\n");

        List<Integer> bList = new ArrayList<>();
        for (Point p : pList) {
            //Get the binary representation for the coordinates (x,y) for each element in pList and create a unique string
            String str = toBinary(p.getX()) + "" + toBinary(p.getY());
            for (int i = 0; i < str.length(); i++) {
                bList.add(Character.getNumericValue(str.charAt(i)));
            }
        }
        return Helpers.listToMatrix(bList);
    }

}
