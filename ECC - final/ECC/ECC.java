package ECC;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Random;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.swing.JTextArea;

public class ECC {

    public static int PAD = 5;
    public static final Random r = new Random();

    private HashMap<Point, Integer> pointTable;
    private HashMap<Integer, Point> charTable;


    public ECC(EllipticCurve c) {
        initCodeTable(c);
    }

    

    public static Random getRandom() {
        return r;
    }

    public Object[] encrypt(String msg, PublicKey key, JTextArea textArea) {
        EllipticCurve c = key.getCurve();
        Point g = c.getBasePoint();
        Point publicKey = key.getKey();
        BigInteger p = c.getP();
        int numBits = p.bitLength();
        BigInteger k;
        do {
            k = new BigInteger(numBits, getRandom());
        } while (k.mod(p).compareTo(BigInteger.ZERO) == 0);
        Point sharedSecret = c.multiply(publicKey, k);

        Point keyHint = c.multiply(g, k); // key to send

        textArea.append("\n----------------- Encryption process -----------------");
        textArea.append("\n" + String.valueOf(c) + "\n");
        textArea.append("\nGenerating Keys:\n");
        textArea.append("\nBob's public key: B = Bob's privateKey * basePoint = " + publicKey);
        textArea.append("\nAlice's private key: k = " + k);
        textArea.append("\nAlice's public key: A = k * basePoint = " + keyHint);
        textArea.append("\nThe encryption key: sharedSecret = k * B = " + sharedSecret + "\n");
        
        
        textArea.append("\n======Starting AES cipher for 128 bits======\n");
        
        // Crear una instancia de CBC
        AES aes = new CBC();
        
        // Generar la clave AES a partir de la coordenada x del sharedSecret
        textArea.append("\n1) Applying hashing with SHA-1\n");
        textArea.append("\nX-coordinate of shared key before hashing: ");
        textArea.append("\n --> Default: " + String.valueOf(sharedSecret.getX()));
        //textArea.append("\n --> Binary: " + sharedSecret.getX().toByteArray() + "\n");

        byte[] sharedSecretBytes = sharedSecret.getX().toByteArray();
        byte[] aesKey = hashSHA1(sharedSecretBytes, 128 / 8); // 128 bits = 16 bytes
        textArea.append("\nX-coordinate of Shared Key after hashing: ");
        //textArea.append("\n --> : " + String.valueOf(aesKey));

        // Imprimir la clave AES en formato hexadecimal
        StringBuilder hexKeyBuilder = new StringBuilder();
        for (byte b : aesKey) {
            hexKeyBuilder.append(String.format("%02X ", b));
        }
        String keyHex = hexKeyBuilder.toString();
        textArea.append("\n --> Hexadecimal: " + keyHex + "\n");

        // Generar un vector de inicialización aleatorio de 16 caracteres
        byte[] ivBytes = new byte[16];
        ECC.getRandom().nextBytes(ivBytes);

        StringBuilder IVBuilder = new StringBuilder();
        for (byte b : ivBytes) {
            IVBuilder.append(String.format("%02X ", b));
        }
        String ivHex = IVBuilder.toString();

        textArea.append("\n2) Create a random initialization vector");
        textArea.append("\n --> Hexadecimal: " + ivHex + "\n");
        
        // Cifrar el mensaje usando AES-CBC

        textArea.append("\n3) Setting the encryption keys:");
        
        textArea.append("\n a. Key expansion for encryption Key");
        aes.expandedKey = aes.keyExpansion(keyHex, textArea);
        textArea.append("\n b. Parse the Initialization vector into a 4x4 matrix\n");
        aes.parseIV(ivHex, textArea);

        // Llamar al método verifyAndTransformInput con el mensaje y guardar el resultado
        String transformedInput = aes.verifyAndTransformInput(msg);

        textArea.append("\n4) Convert the original plain text into Hexadecimal format\n");
        textArea.append("\nOriginal format: " + msg);
        textArea.append("\nHexadecimal format: " + transformedInput + "\n");

        // Crear un ByteArrayOutputStream para capturar la salida de la función encrypt
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Llamar al método encrypt de la clase CBC con el mensaje transformado y el textArea
        String encryptedInput = aes.encrypt(transformedInput, textArea, outputStream);

        // Agregar el contenido del outputStream al textArea
        textArea.append("\n5) Encrypting with key encryption and initialization vector:\n" + outputStream.toString());

        // Detenemos el proceso aquí
        return new Object[]{encryptedInput, ivHex, keyHint};

    }

    public EncryptionParameters getEncryptionParameters(PublicKey key, String ivHex, Point keyHint) {
        EllipticCurve c = key.getCurve();
        return new EncryptionParameters(key.getKey(), c.getBasePoint(), c.getA(), c.getB(), c.getP(), ivHex, keyHint);
    }

    public String decrypt(String encryptedInput, PrivateKey key, EncryptionParameters encryptionParameters, JTextArea textArea) {
        //Recover the encryption parameters
        EllipticCurve c = new EllipticCurve(encryptionParameters.getA(), encryptionParameters.getB(), encryptionParameters.getP(), encryptionParameters.getBasePoint());
        //Get the previous private key created for Bob
        BigInteger privateKey = key.getKey();
        // Obtener la clave pública de Alice (keyHint) de los parámetros de encriptación
        Point alicePublicKey = encryptionParameters.getKeyHint();
        //Compute sharedSecret with privateKey from Bob and public key from Alice
        Point sharedSecret = c.multiply(alicePublicKey, privateKey);

        textArea.append("\n----------------- Decryption process -----------------");
        textArea.append("\n\n1) Bob receives the encrypted input and parameters:");
        textArea.append("\nEncrypted text: " + encryptedInput);
        textArea.append("\nBob's Public Key: " + encryptionParameters.getPublicKey());
        textArea.append("\nBase Point from Elliptic curve: " + encryptionParameters.getBasePoint());
        textArea.append("\n\nCurve Parameters (a, b, p): \n");
        textArea.append("a: " + encryptionParameters.getA() + "\n");
        textArea.append("b: " + encryptionParameters.getB() + "\n");
        textArea.append("p: " + encryptionParameters.getP() + "\n");
        textArea.append("\nInitialization Vector (IV): " + encryptionParameters.getIvHex());

        textArea.append("\n\n2) Compute shared secret using Bob's private key and Alice's public key:");
        textArea.append("\nShared Secret: " + sharedSecret);

        textArea.append("\n\n3) Perform AES decryption:");
        
        byte[] sharedSecretBytes = sharedSecret.getX().toByteArray();
        byte[] aesKey = hashSHA1(sharedSecretBytes, 128 / 8); // 128 bits = 16 bytes
        textArea.append("\nAES Key (Hashed Shared Secret):");

        StringBuilder hexKeyBuilder = new StringBuilder();
        for (byte b : aesKey) {
            hexKeyBuilder.append(String.format("%02X ", b));
        }
        String keyHex = hexKeyBuilder.toString();
        textArea.append("\n --> Hexadecimal: " + keyHex);

        AES aes = new CBC();

        textArea.append("\n\na. Key Expansion for Decryption Key:");
        aes.expandedKey = aes.keyExpansion(keyHex, textArea);

        textArea.append("\nb. Set Initialization Vector (IV):");
        aes.initializationVector = encryptionParameters.getInitializationVector();
        textArea.append("\n" + encryptionParameters.getIvHex());

        // Crear un ByteArrayOutputStream para capturar la salida de la función encrypt
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        textArea.append("\n\nc. Decrypting with key encryption and initialization vector:\n");
        String decryptedText = aes.decrypt(encryptedInput, textArea, outputStream);

        // Agregar el contenido del outputStream al textArea
        textArea.append(outputStream.toString());

        //textArea.append("Decrypted Text: " + decryptedText + "\n");

        return decryptedText;

    }

    /**
     * Generate a random key-pair, given the elliptic curve being used.
     * Generate public and private key for Bob
     */
    public static KeyPair generateKeyPair(EllipticCurve c) {
        // Randomly select the private key, such that it is relatively prime to p
        BigInteger p = c.getP();
        BigInteger privateKey;
        do {
            privateKey = new BigInteger(p.bitLength(), getRandom());
        } while (privateKey.compareTo(BigInteger.ONE) <= 0 || privateKey.compareTo(p.subtract(BigInteger.ONE)) >= 0 || !privateKey.gcd(p.subtract(BigInteger.ONE)).equals(BigInteger.ONE));

        // Calculate the public key, k * g.
        Point g = c.getBasePoint();
        Point publicKey = c.multiply(g, privateKey); //additon performing "privateKey" times the point g

        return new KeyPair(
                new PublicKey(c, publicKey),
                new PrivateKey(c, privateKey)
        );
    }

    private byte[] hashSHA1(byte[] input, int numBytes) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] hash = md.digest(input);
            byte[] truncatedHash = new byte[numBytes];
            System.arraycopy(hash, 0, truncatedHash, 0, numBytes);
            return truncatedHash;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public final void initCodeTable(EllipticCurve curve) {
        charTable = new HashMap<>();
        pointTable = new HashMap<>();

        // Determinar el rango de caracteres según el tamaño de la curva
        int numCharacters = determineNumCharacters(curve.getP());

        //Lowercase characters
        Point p = curve.getBasePoint();
        for (int i = 1; i < numCharacters; i++) {
            do {
                p = curve.multiply(curve.getBasePoint(), i);
            } while (p.isInfinity());
            charTable.put(i + 96, p); // from 97 to 122 in ascii
        }

        //Uppercase characters
        for (int i = 1; i < 27; i++) {
            do {
                p = curve.multiply(curve.getBasePoint(), i);
            } while (p.isInfinity());
            charTable.put(i + 64, p); // from 65 to 90 in ascii
        }

        //special characters
        charTable.put(32, Point.getInfinity()); //space
        int[] codeAscii = new int[]{10, 13, 39, 40, 41, 44, 46, 58, 59};
        for (int i : codeAscii) {
            p = curve.add(p, curve.getBasePoint());
            charTable.put(i, p);
        }

        //populate the points symbol table
        for (Integer key : charTable.keySet()) {
            pointTable.put(charTable.get(key), key);
        }
    }

    private int determineNumCharacters(BigInteger p) {
        // Implementa la lógica para determinar el número de caracteres
        // según el tamaño de la curva elíptica (p)
        // Por ejemplo, puedes devolver un valor fijo para cada curva NIST
        if (p.equals(new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951"))) {
            // Curva NIST P-256
            return 256; // O el número de caracteres que desees
        } else if (p.equals(new BigInteger("6277101735386680763835789423207666416102355444459739541047"))) {
            // Curva M-221
            return 221; // O el número de caracteres que desees
        } else if (p.equals(new BigInteger("2").pow(251).subtract(new BigInteger("9")))) {
            // Curva Curve1174
            return 251; // O el número de caracteres que desees
        }
        // Agrega más casos para otras curvas elípticas

        // Valor predeterminado para la curva actual
        return 27; // O el número de caracteres que desees
    }

    public void displayCodeTable(JTextArea textArea) {
        textArea.append("------ Code Table -------");
        charTable.forEach((cle, val) -> {
            textArea.append((char) cle.intValue() + " -> " + val + "\n");
        });
    }



    public static void main(String[] args) {
        /*
        EllipticCurve c = new EllipticCurve(4, 20, 29, new Point(1, 5));
        JTextArea keysGenerationTextArea = new JTextArea();

        ECC ecc = new ECC(c);
        ecc.displayCodeTable(keysGenerationTextArea);
        String msg = "HOLA COMO ESTAS: BIEN";
        //String msg = "como 'estas' soy un texto de (prueba).";
        // generate pair of keys
        KeyPair keys = generateKeyPair(c);
        // encrypt the msg
        int[] cipherText = ecc.encrypt(msg, keys.getPublicKey(), keysGenerationTextArea);
        keysGenerationTextArea.append("\n5) Alice send this to Bob:\n");
        Helpers.print(cipherText, keysGenerationTextArea);
        keysGenerationTextArea.append("\n");

        // decrypt the result
        //String plainText = ecc.decrypt(cipherText, keys.getPrivateKey(), keysGenerationTextArea);
        //System.out.println("\n5) Translate each point to a carracter");

        //System.out.println("Cipher : ");
        //Helpers.print(cipherText);
        //System.out.println("Decrypted text : " + plainText);
        */
    }
}
