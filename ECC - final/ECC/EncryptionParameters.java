package ECC;

import java.math.BigInteger;

public class EncryptionParameters {
    private Point publicKey;
    private Point basePoint;
    private BigInteger a;
    private BigInteger b;
    private BigInteger p;
    private String ivHex;
    private int[][] initializationVector;
    private Point keyHint;

    public EncryptionParameters(Point publicKey, Point basePoint, BigInteger a, BigInteger b, BigInteger p, String ivHex, Point keyHint) {
        this.publicKey = publicKey;
        this.basePoint = basePoint;
        this.a = a;
        this.b = b;
        this.p = p;
        this.ivHex = ivHex;
        this.initializationVector = parseIV(ivHex);
        this.keyHint = keyHint;
    }

    private int[][] parseIV(String ivHex) {
        int[][] initializationVector = new int[4][4];
        ivHex = ivHex.replaceAll("\\s+", "");
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                initializationVector[j][i] = Integer.parseInt(ivHex.substring((8 * i) + (2 * j), (8 * i) + (2 * j + 2)), 16);
            }
        }
        return initializationVector;
    }

    // Getters for the parameters
    public Point getPublicKey() {
        return publicKey;
    }

    public Point getBasePoint() {
        return basePoint;
    }

    public BigInteger getA() {
        return a;
    }

    public BigInteger getB() {
        return b;
    }

    public BigInteger getP() {
        return p;
    }

    public String getIvHex() {
        return ivHex;
    }

    public int[][] getInitializationVector() {
        return initializationVector;
    }

    public Point getKeyHint() {
        return keyHint;
    }

    public void setKeyHint(Point keyHint) {
        this.keyHint = keyHint;
    }
}