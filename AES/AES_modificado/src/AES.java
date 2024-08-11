/**
 * Main AES Class
 * Contains all common AES encrypt/decrypt data and methods
 */
public abstract class AES {
    // Holds the expanded Key
    protected int[][] expandedKey;
    // Holds the initialization vector.
    protected int[][] initializationVector = new int[4][4];

    /**
     * Expands given key to create individual round keys
     * @param key
     */
    protected int[][] keyExpansion(String key) {
        // Set number of keys we need - 10 keys x 4 bytes + initial
        int keySize = 11 * 4;
        // Init rcon pointer
        int rconIndex = 1;
        // Init key variable
        int[][] expandedKey = new int[4][keySize];
        // First parse key into 4x4 matrix
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                expandedKey[j][i] = Integer.parseInt(key.substring((8 * i) + (2 * j), (8 * i) + (2 * j + 2)), 16);
            }
        }

        //Print Original 128 bits Key in Hexadecimal
        System.out.println("--------------------------------------------------------------------");
        System.out.println("Original Key: ");
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                expandedKey[j][i] = Integer.parseInt(key.substring((8 * i) + (2 * j), (8 * i) + (2 * j + 2)), 16);
                System.out.printf("%02X ", expandedKey[j][i]); // Print each byte of the key in hexadecimal format
            }
        }
        System.out.println();        

        // Set start point - given we have already filled the first key
        int current = 4;
        // Set some temp variables
        // a = temp save the last word of current key
        int[] a = new int[4];
        int b;
        while (current < keySize) {
            //Determine if we have to apply the g() function or
            //just perform an XOR with the above keyword.
            if (current % 4 == 0) {
                // We need to go through the g function
                // First copy last word in "a"
                for (b = 0; b < 4; b++) {
                    a[b] = expandedKey[b][current - 1];
                }
                // The g() function is applied to the keyword stored in "a"
                a = gFunction(a, rconIndex++);
                // XOR with [i-4] word
                for (b = 0; b < 4; b++) {
                    expandedKey[b][current] = a[b] ^ expandedKey[b][current - 4];
                }
            } else {
                // Simply XOR with [i-4]
                for (b = 0; b < 4; b++) {
                    expandedKey[b][current] = expandedKey[b][current - 1] ^ expandedKey[b][current - 4];
                }
            }
            current++;
        }

        // Print each subkey
        System.out.println("--------------------------------------------------------------------");
        for (int i = 0; i < keySize; i += 4) {
            System.out.println("Subkey " + (i / 4 + 1) + ":");
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    System.out.printf("%02X ", expandedKey[j][i]); // Print each byte of the subkey in hexadecimal format
                }
            }
            System.out.println();
            System.out.println();
        }
        
        return expandedKey;
    }

    /**
     * Helper function used in key expansion
     *
     * For each 4th word in the expanded key
     * This function rotates the word
     * Subs with Sbox
     * And XORS each byte with an rcon constant
     */
    private int[] gFunction(int[] a, int index) {
        int[] tmp = new int[4];

        // Rotate similar to shift rows
        tmp[0] = a[1];
        tmp[1] = a[2];
        tmp[2] = a[3];
        tmp[3] = a[0];

        // Sub with sBox
        int val;
        for (int i = 0; i < 4; i++) {
            val = tmp[i];
            // Divide the original byte value by 16 (val / 16) to get the row index 
            // Calculate modulo 16 (val % 16) to get the column index.
            tmp[i] = Constants.sbox[val / 16][val % 16];
        }

        // Finally XOR with rcon
        tmp[0] ^= Constants.rcon[index];

        return tmp;
    }

    /**
     * Adds round key to state via XOR
     * This method adds the "current round key" to the "current state" 
     * of the AES encryption using an XOR operation, which merges 
     * the round key with the state for the current round of encryption.
     */
    protected int[][] addRoundKey(int[][] state, int round) {
        // First need to get the round key from key matrix
        int[][] roundKey = new int[4][4];
        // Fill the matrix with the corresponding values of the expandedKey
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                roundKey[i][j] = this.expandedKey[i][(4 * round) + j];
            }
        }

        // Now XOR roundKey with state
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                state[i][j] ^= roundKey[i][j];
            }
        }

        return state;
    }

    /**
     * Mix columns via galois multiplication
     *
     * Each byte is transformed via galois multiplication
     * To do this we will use multiplication lookup tables
     * The lookup index is based off the bytes position in state
     * And its comparison to a galois constant array
     */
    protected int[][] mixColumns(int[][] state) {
        // Init temp matrix of the same size as the current state
        int[][] tmp = new int[4][4];
        // Iterate twice to parse state
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                // Store the result of mix columns for a specific byte
                int val = 0;

                // The lookup result of each rows bytes is XORed, so iterate the row
                for (int k = 0; k < 4; k++) {
                    // Get galois value
                    int g = Constants.galois[i][k];
                    // Get state value
                    int s = state[k][j];
                    // Use galois multiplication lookups
                    if (g == 1) {
                        val = val ^ s;
                    } else if (g == 2) {
                        val = val ^ Constants.mc2[s / 16][s % 16];
                    } else if (g == 3) {
                        val = val ^ Constants.mc3[s / 16][s % 16];
                    } else {
                        val = val ^ 0;
                    }
                }
                // Insert result into temp state
                tmp[i][j] = val;
            }
        }

        return tmp;
    }

    /**
     * Inverse mix columns
     *
     * This is the inverse of mixColumns
     * See above for operations
     */
    protected int[][] invMixColumns(int[][] state) {
        int[][] tmp = new int[4][4];
        // Loop the 2D array
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                // Setup temp calc value for sumations
                int val = 0;
                // The lookup result of each rows bytes is XORed, so iterate the row
                for (int k = 0; k < 4; k++) {
                    // Get galois value
                    int g = Constants.invgalois[i][k];
                    // Get state value
                    int s = state[k][j];
                    // Use galois multiplication lookups
                    if (g == 1) {
                        val = val ^ s;
                    } else if (g == 9) {
                        val = val ^ Constants.mc9[s / 16][s % 16];
                    } else if (g == 11) {
                        val = val ^ Constants.mc11[s / 16][s % 16];
                    } else if (g == 13) {
                        val = val ^ Constants.mc13[s / 16][s % 16];
                    } else if (g == 14) {
                        val = val ^ Constants.mc14[s / 16][s % 16];
                    } else {
                        val = val ^ 0;
                    }
                }
                // Insert result into temp state
                tmp[i][j] = val;
            }
        }

        return tmp;
    }

    /**
     * Shifts rows in state
     *
     * Row 0 is left untouched
     * Row 1 shifts 1 left
     * Row 2 shifts 2 left
     * Row 3 shifts 3 left
     */
    protected int[][] shiftRows(int[][] state) {
        // Init temp matrix
        int[][] tmp = new int[4][4];

        // Row 0 is untouched
        tmp[0][0] = state[0][0];
        tmp[0][1] = state[0][1];
        tmp[0][2] = state[0][2];
        tmp[0][3] = state[0][3];

        // Shift row 1 left 1 position
        tmp[1][0] = state[1][1];
        tmp[1][1] = state[1][2];
        tmp[1][2] = state[1][3];
        tmp[1][3] = state[1][0];

        // Shift row 2 left 2 positions
        tmp[2][0] = state[2][2];
        tmp[2][1] = state[2][3];
        tmp[2][2] = state[2][0];
        tmp[2][3] = state[2][1];

        // Shift row 3 left 3 positions
        tmp[3][0] = state[3][3];
        tmp[3][1] = state[3][0];
        tmp[3][2] = state[3][1];
        tmp[3][3] = state[3][2];

        return tmp;
    }

    /**
     * Shifts rows in state
     *
     * Row 0 is left untouched
     * Row 1 shifts 1 right
     * Row 2 shifts 2 right
     * Row 3 shifts 3 right
     */
    protected int[][] invShiftRows(int[][] state) {
        // Init temp matrix
        int[][] tmp = new int[4][4];

        // Row 0 is untouched
        tmp[0][0] = state[0][0];
        tmp[0][1] = state[0][1];
        tmp[0][2] = state[0][2];
        tmp[0][3] = state[0][3];

        // Shift row 1 right 1 position
        tmp[1][0] = state[1][3];
        tmp[1][1] = state[1][0];
        tmp[1][2] = state[1][1];
        tmp[1][3] = state[1][2];

        // Shift row 2 right 2 positions
        tmp[2][0] = state[2][2];
        tmp[2][1] = state[2][3];
        tmp[2][2] = state[2][0];
        tmp[2][3] = state[2][1];

        // Shift row 3 right 3 positions
        tmp[3][0] = state[3][1];
        tmp[3][1] = state[3][2];
        tmp[3][2] = state[3][3];
        tmp[3][3] = state[3][0];

        return tmp;
    }

    /**
     * Substitute key bytes with bytes from the S-Box
     * @param state
     */
    protected int[][] subBytes(int[][] state) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int hexValue = state[i][j];
                state[i][j] = Constants.sbox[hexValue / 16][hexValue % 16];
            }
        }

        return state;
    }

    /**
     * Substitute key bytes with bytes from the inverse S-Box
     * @param state
     */
    protected int[][] invSubBytes(int[][] state) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int hexValue = state[i][j];
                state[i][j] = Constants.rsbox[hexValue / 16][hexValue % 16];
            }
        }

        return state;
    }

    /**
     * Helper function to perform deep copies on 2D array.
     * They need separate versions to avoid unwanted side effects
     */
    protected int[][] deepCopyState(int[][] state) {
        int[][] tmp = new int[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                tmp[i][j] = state[i][j];
            }
        }

        return tmp;
    }

    /**
     * Parse IV into a useful block array
     */
    public void parseIV(String iv) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                this.initializationVector[j][i] = Integer.parseInt(iv.substring((8 * i) + (2 * j), (8 * i) + (2 * j + 2)), 16);
            }
        }
    }

    /**
     * Converts integer array state to a string
     */
    protected String toString(int[][] state) {
        // String to store hexadecimal representation of the matrix
        String output = "";

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                // Gets the hexadecimal value of the current element of the matrix
                String k = Integer.toHexString(state[j][i]).toUpperCase();
                // Represents a single hexadecimal digit
                if (k.length() == 1) {
                    // A '0' is added to the beginning to ensure that the 
                    // hexadecimal representation always has two characters.
                    output += '0' + k;
                } else {
                    output += k;
                }
                // Append a space
                //output += ' ';
            }
        }

        return output;
    }


    /**
     * From Hex to String
     */

    public String HexToTextConverter(String output) {
        
        String hexadecimal = output; // Ejemplo de cadena hexadecimal

        // Eliminar los espacios en blanco de la cadena hexadecimal
        String hexadecimalWithoutSpaces = hexadecimal.replaceAll("\\s+", "");

        // Convertir la cadena hexadecimal a bytes y luego a texto
        String texto = hexToString(hexadecimalWithoutSpaces);

        //System.out.println("Texto convertido: " + texto);
        
        return texto;
    }

     // Método para convertir una cadena hexadecimal en una cadena de texto
     public static String hexToString(String hexadecimal) {
        StringBuilder resultado = new StringBuilder();
        for (int i = 0; i < hexadecimal.length(); i += 2) {
            String parHex = hexadecimal.substring(i, i + 2);
            int valorByte = Integer.parseInt(parHex, 16);
            resultado.append((char) valorByte);
        }
        return resultado.toString();
    }



    //Verificar formato de texto
    public String verifyAndTransformInput(String input) {        
        // Transforma el texto de entrada al formato hexadecimal
        String hexInput = "";
        if (isBinary(input)) {
            // Convertir el texto de entrada a su representación hexadecimal
            hexInput = binaryToHex(input);
        } else if (isHexadecimal(input)) {
            // El texto de entrada ya está en formato hexadecimal
            hexInput = input;            
        } else if (isAscii("input")) {
            // Convertir el texto de entrada ASCII a hexadecimal
            hexInput = stringToHex(input);   
        } else {
            // Verifica si el texto de entrada es ASCII alfanumérico
            throw new IllegalArgumentException("Input text does not match Binary, Ascii or Hexadecimal format.");
        }
        return hexInput;
    }

    // Método para convertir una cadena de texto a su representación hexadecimal
    private static String stringToHex(String input) {
        StringBuilder hexString = new StringBuilder();
        for (char character : input.toCharArray()) {
            hexString.append(String.format("%02X", (int) character));
        }
        return hexString.toString();
    }

    // Método para convertir una cadena de texto binario a su representación hexadecimal
    private static String binaryToHex(String input) {
        StringBuilder hexBuilder = new StringBuilder();
        String[] binaryArray = input.split(" "); // Dividimos la cadena en segmentos binarios separados por espacios
        for (String binary : binaryArray) {
            int decimal = Integer.parseInt(binary, 2); // Convertimos el segmento binario a decimal
            String hex = Integer.toString(decimal, 16); // Convertimos el decimal a hexadecimal
            hexBuilder.append(hex); // Agregamos el hexadecimal al resultado
        }
        return hexBuilder.toString();
    }

    // Método para verificar si una cadena es binaria
    private boolean isBinary(String input) {
        // Verifica si la cadena contiene solo caracteres binarios válidos (0 o 1)
        for (char c : input.toCharArray()) {
            if (c != '0' && c != '1' && c != ' ') {
                return false;
            }
        }
        return true;
    }

    // Método para verificar si una cadena es hexadecimal
    private boolean isHexadecimal(String input) {
        // Verifica si la cadena contiene solo caracteres hexadecimales válidos
        return input.matches("[0-9A-Fa-f]+");
    }

    // Método para verificar si una cadena es ASCII
    private boolean isAscii(String input) {
        // Verifica si la cadena contiene solo caracteres ASCII alfanuméricos válidos (A-Z, a-z, 0-9)
        for (char c : input.toCharArray()) {
            // Verifica si el carácter no es alfanumérico
            if (!((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || c == '.')) {
                return false;
            }
        }
        return true;
    }


    /**
     * Abstract class for encryption
     * To be implemented on an Encryption mode basis
     */
    public abstract String encrypt(String input);

    /**
     * Abstract class for decryption
     * To be implemented on a Decryption mode basis
     */
    public abstract String decrypt(String input);
}
