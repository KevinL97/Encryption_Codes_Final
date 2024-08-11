package ECC;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.regex.*;

import javax.swing.JTextArea;

public class CBC extends AES {

    @Override
    public String encrypt(String input, JTextArea textArea, ByteArrayOutputStream outputStream) {
        // Rellenar con "0" si es necesario para que la longitud sea múltiplo de 32
        int paddingLength = 32 - (input.length() % 32);
        if (paddingLength != 32) {
            input = String.format("%-" + (input.length() + paddingLength) + "s", input).replace(' ', '0');
        }

        // Input may be greater than 16 bytes, so split
        Matcher m = Pattern.compile(".{1,32}").matcher(input);

        // Run for each 16 bytes
        int[][] state = new int[4][4];
        String output = "";
        int c = 1;
        //String mytext = "";

        // Redireccionar System.out al ByteArrayOutputStream
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);

        //My plain text as String
        /*System.out.println("My plain text as String:");
        mytext = this.HexToTextConverter(input);
        System.out.println(mytext + "\n");
        **/
        while (m.find()) {
            String chunk = input.substring(m.start(), m.end());

            // Parse string into 4 x 4 state
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    state[k][j] = Integer.parseInt(chunk.substring((8 * j) + (2 * k), (8 * j) + (2 * k + 2)), 16);
                }
            }

            System.out.println("My plain text as Hexadecimal:");
            printState(state);
            System.out.println();


            String phaseNum = String.valueOf(c);
            System.out.println("|===============> Phase: " + phaseNum + "<================|");
            c++;

            // Imprimir el estado original
            System.out.println("Original State ( Part " + phaseNum + " of plain text):");
            printState(state);
            System.out.println();

            // Imprimir el estado inicial
            System.out.println("-> Initial State (After XOR and round key addition):");

            // XOR IV
            state = xorIV(state, this.initializationVector);
            System.out.print("- After XOR with Initial Vector: ");
            printState(state);

            // Add round key - round 0
            state = this.addRoundKey(state, 0);
            System.out.print("- After round key addition: ");
            printState(state);
            System.out.println();

            int j;
            // Iterate for 10 rounds
            for (j = 1; j < 10; j++) {
                // Imprimir el estado después de cada iteración
                System.out.println("-> Round " + j + ":");
                
                state = this.subBytes(state);
                System.out.print("- After byte substitution: ");
                printState(state);

                state = this.shiftRows(state);
                System.out.print("- After shift rows: ");
                printState(state);

                state = this.mixColumns(state);
                System.out.print("- After mix columns: ");
                printState(state);
                
                state = this.addRoundKey(state, j);
                System.out.print("- After round key addition: ");
                printState(state);
                System.out.println();
                
                //printState(state);
                //System.out.println();

            }

            // Print final round
            System.out.println("-> Round "+ j + ":");

            // Final round
            state = this.subBytes(state);
            System.out.print("- After byte substitution: ");
            printState(state);
            
            state = this.shiftRows(state);
            System.out.print("- After shift rows: ");
            printState(state);

            state = this.addRoundKey(state, 10);
            System.out.print("- After round key addition: ");
            printState(state);
            System.out.println();
            
            //printState(state);
            //System.out.println();

            // Pass state into initializationVector for next round
            this.initializationVector = this.deepCopyState(state);

            // Add state to output string
            output += this.toString(state);
        }

        return output;
    }

    private void printState(int[][] state) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.print(String.format("%02X ", state[j][i]));
            }
            //System.out.println();
        }
        System.out.println();
    }



    @Override
    public String decrypt(String input,  JTextArea textArea, ByteArrayOutputStream outputStream) {
        //System.out.println("My encrypted text in Hexadecimal:");
        //System.out.println(input + "\n");
        // Need a tmp array to keep track of the input state
        int[][] initialInput;

        // Input may be greater than 16 bytes, so split
        Matcher m = Pattern.compile(".{1,32}").matcher(input);

        // Run for each 16 bytes
        int[][] state = new int[4][4];
        String output = "";
        String mytext = "";
        int j;
        int c = 1;

        // Redireccionar System.out al ByteArrayOutputStream
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);

        while (m.find()) {
            String chunk = input.substring(m.start(), m.end());

            // Parse string into 4 x 4 state
            for (j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    state[k][j] = Integer.parseInt(chunk.substring((8 * j) + (2 * k), (8 * j) + (2 * k + 2)), 16);
                }
            }

            // Copy parsed state to initialInput temp
            initialInput = this.deepCopyState(state);


            String phaseNum = String.valueOf(c);
            System.out.println("|===============> Phase: " + phaseNum + "<================|");
            c++;

            // Imprimir el estado original
            System.out.println("Original State ( Part " + phaseNum + " of cipher text):");
            printState(state);
            System.out.println();

            // Add round key - round 0
            state = this.addRoundKey(state, 10);
            System.out.print("- After round key addition: ");
            printState(state);
            System.out.println();

            // Iterate for 10 rounds
            for (j = 9; j > 0; j--) {
                // Imprimir el estado después de cada iteración
                System.out.println("-> Round " + (j+1) + ":");

                state = this.invSubBytes(state);
                System.out.print("- After inverse byte substitution: ");
                printState(state);

                state = this.invShiftRows(state);
                System.out.print("- After inverse shift rows: ");
                printState(state);

                state = this.addRoundKey(state, j);
                System.out.print("- After round key addition: ");
                printState(state);

                state = this.invMixColumns(state);
                System.out.print("- After inverse mix columns: ");
                printState(state);
                System.out.println();
            }

            // Final round
            System.out.println("-> Round "+ (j+1) + ":");

            state = this.invSubBytes(state);
            System.out.print("- After inverse byte substitution: ");
            printState(state);

            state = this.invShiftRows(state);
            System.out.print("- After inverse shift rows: ");
            printState(state);

            state = this.addRoundKey(state, 0);
            System.out.print("- After round key addition: ");
            printState(state);

            // Finally we need to XOR the result with the IV or previous input block
            state = xorIV(state, this.initializationVector);
            System.out.print("- After XOR with initialization vector: ");
            printState(state);
            System.out.println();

            // Copy initial state into initializationVector for next round
            this.initializationVector = this.deepCopyState(initialInput);

            output += this.toString(state);
        }

        System.out.println("-> Plain text as Hexadecimal:");
        System.out.println(output + "\n");

        mytext = this.HexToTextConverter(output);

        return mytext;
    }

    /**
     * Helper function to XOR state with IV
     */
    private int[][] xorIV(int[][] state, int[][] iv) {
        int[][] tmp = new int[4][4];

        for (int j = 0; j < 4; j++) {
            for (int k = 0; k < 4; k++) {
                tmp[j][k] = state[j][k] ^ iv[j][k];
            }
        }

        return tmp;
    }
}
