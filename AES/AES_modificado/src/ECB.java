import java.util.regex.*;

public class ECB extends AES {

    @Override
    public String encrypt(String input) {
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
        //String mytext = "";
        int c = 1;
        int j;

        //My plain text as String
        //System.out.println("My plain text as String:");
        //mytext = this.HexToTextConverter(input);
        //System.out.println(mytext + "\n");


        while (m.find()) {
            String chunk = input.substring(m.start(), m.end());

            // Parse string into 4 x 4 state
            for (j = 0; j < 4; j++) {
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
            System.out.println("-> Initial State (After round key addition):");

            // Add round key - round 0
            state = this.addRoundKey(state, 0);
            System.out.print("- After round key addition: ");
            printState(state);
            System.out.println();

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
    public String decrypt(String input) {
        // Input may be greater than 16 bytes, so split
        Matcher m = Pattern.compile(".{1,32}").matcher(input);

        // Run for each 16 bytes
        int[][] state = new int[4][4];
        String output = "";
        String mytext = "";
        int j;
        int c = 1;

        while (m.find()) {
            String chunk = input.substring(m.start(), m.end());

            // Parse string into 4 x 4 state
            for (j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    state[k][j] = Integer.parseInt(chunk.substring((8 * j) + (2 * k), (8 * j) + (2 * k + 2)), 16);
                }
            }

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
            System.out.println();

            output += this.toString(state);
        }

        System.out.println("My plain text in Hexadecimal:");
        System.out.println(output + "\n");

        mytext = this.HexToTextConverter(output);

        return mytext;
    }
}
