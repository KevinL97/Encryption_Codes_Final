public class Interface {
    private AES aes;

    public String encrypt(int functionType, String modeComboBox, int transmissionSize, String inputText, String key, String initilizationVector) {
        // Validar los parámetros de entrada
        if (functionType != 0 && functionType != 1) {
            throw new IllegalArgumentException("Invalid function type. Must be 0 for encryption or 1 for decryption.");
        }

        if (transmissionSize < 0) {
            throw new IllegalArgumentException("Invalid transmission size. Must be a positive integer.");
        }

        if (inputText == null || inputText.isEmpty()) {
            throw new IllegalArgumentException("Input text cannot be null or empty.");
        }

        if (key == null) {
            throw new IllegalArgumentException("Invalid key. Must be a at least 16-characters long.");
        }

        if (modeComboBox == "CBC") {
            if (initilizationVector == null) {
                throw new IllegalArgumentException("Invalid initialization vector. Must be a 32-character hexadecimal string.");
            }
        }

        // Crear la instancia de AES según el modo de operación
        switch (modeComboBox) {
            case "ECB":
                // ECB
                this.aes = new ECB();
                break;
            case "CBC":
                // CBC
                this.aes = new CBC();

                //Verify format of IV
                String verifiedIV = aes.verifyAndTransformInput(initilizationVector);
                // Parse IV
                this.aes.parseIV(verifiedIV);                
                break;
            default:
                break;
        }

        //Verificar el formato de la llave
        String verifiedKey = aes.verifyAndTransformInput(key);
        // Expandir la clave
        aes.expandedKey = aes.keyExpansion(verifiedKey);

        System.out.println("input: " + inputText);
        System.out.println();
        
        String transformedInput = aes.verifyAndTransformInput(inputText);

        // Realizar el cifrado
        return aes.encrypt(transformedInput);
    }

    public String decrypt(int functionType, String modeComboBox, int transmissionSize, String inputText, String key, String initilizationVector) {
        // Validar los parámetros de entrada
        if (functionType != 0 && functionType != 1) {
            throw new IllegalArgumentException("Invalid function type. Must be 0 for encryption or 1 for decryption.");
        }
    
        if (transmissionSize < 0) {
            throw new IllegalArgumentException("Invalid transmission size. Must be a positive integer.");
        }
    
        if (inputText == null || inputText.isEmpty()) {
            throw new IllegalArgumentException("Input text cannot be null or empty.");
        }
    
        if (key == null) {
            throw new IllegalArgumentException("Invalid key. Must be at least 16-characters long.");
        }
    
        if (modeComboBox == "CBC") {
            if (initilizationVector == null) {
                throw new IllegalArgumentException("Invalid initialization vector. Must be a 32-character hexadecimal string.");
            }
        }

        // Crear la instancia de AES según el modo de operación
        switch (modeComboBox) {
            case "ECB":
                // ECB
                this.aes = new ECB();
                break;
            case "CBC":
                // CBC
                this.aes = new CBC();
                //Verify format of IV
                String verifiedIV = aes.verifyAndTransformInput(initilizationVector);
                // Parse IV
                this.aes.parseIV(verifiedIV);
                break;
            default:
                break;
        }

        String verifiedKey = aes.verifyAndTransformInput(key);
        // Expandir la clave
        aes.expandedKey = aes.keyExpansion(verifiedKey);

        // Realizar el descifrado
        return aes.decrypt(inputText);
    }
}