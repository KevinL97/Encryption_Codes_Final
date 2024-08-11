package rsa;

import java.math.BigInteger;
import java.security.SecureRandom;
import javax.swing.JTextArea;

public class RSA {
    //private static final int BITS = 1024;

	//Random prime number generator
    public static BigInteger generateProbablePrime(int numBits, int iterations, JTextArea textArea) {
        textArea.append("====================Primality test====================");
        while (true) {
            BigInteger randomNum = generateRandomNumber(numBits);
            if (fermatPrimalityTest(randomNum, iterations, textArea)) {
                return randomNum;
            }
        }
    }

	//Primality Test - Fermat's Theorem 
    public static boolean fermatPrimalityTest(BigInteger n, int iterations, JTextArea textArea) {
        textArea.append("\n===> Fermat's Theorem <===");
        textArea.append("\nRandom number: " + String.valueOf(n));
        textArea.append("\nChecking if " + n + " is prime using Fermat's Primality Test...\n");

        if (n.compareTo(BigInteger.ONE) <= 0) {
            textArea.append("1 and negative numbers are not prime.\n");
            return false;
        }

        if (n.compareTo(new BigInteger("3")) <= 0) {
            textArea.append("2 and 3 are prime.\n");
            return true;
        }

        SecureRandom random = new SecureRandom();
        for (int i = 0; i < iterations; i++) {
			// Generate a random number 'a' such that 1 < a < n(random number generated)
            BigInteger a = new BigInteger(n.bitLength() - 2, random).add(BigInteger.TWO);
            textArea.append("Trying a = " + a + "\n");
			// If a^(n-1) mod n != 1, then n is definitely composite
            if (!a.modPow(n.subtract(BigInteger.ONE), n).equals(BigInteger.ONE)) {
                textArea.append("a^(n-1) mod n != 1, so " + n + " is composite.\n");
                return false;
            }
        }

        textArea.append(n + " is probably prime.\n");
        return true;
    }

	//Coprime tester: gcd(e, Φ(n)) = 1
    public static BigInteger generateCoprime(BigInteger fiN, int iterations, JTextArea textArea) {
        textArea.append("\nGenerating a coprime number e...\n");
        BigInteger ePrime = generateProbablePrime(fiN.bitLength() - 1, iterations, textArea);
        textArea.append("Candidate e: " + ePrime + "\n");

        while (!ePrime.gcd(fiN).equals(BigInteger.ONE)) {
            textArea.append("gcd(e, phi(n)) = " + ePrime.gcd(fiN) + " != 1, trying again...\n");
            ePrime = generateProbablePrime(fiN.bitLength() - 1, iterations, textArea);
            textArea.append("Candidate e: " + ePrime + "\n");
        }

        textArea.append("Found coprime e: " + ePrime + "\n");
        return ePrime;
    }

	//Calculate d with Extended Euclidean Algorithm (EEA)
	public static BigInteger calculatePrivateKey(BigInteger ePrime, BigInteger fiN, JTextArea textArea) {
        BigInteger[] extendedEuclideanResult = extendedEuclidean(ePrime, fiN, textArea);
        BigInteger d = extendedEuclideanResult[1];

        // Ensure d is positive
        if (d.compareTo(BigInteger.ZERO) < 0) {
            d = d.add(fiN);
        }

        return d;
    }

    public static BigInteger[] extendedEuclidean(BigInteger a, BigInteger b, JTextArea textArea) {
        textArea.append("\nCalculating the private key d using the Extended Euclidean Algorithm...\n");
        textArea.append("a = " + a + ", b = " + b + "\n");

        // Base case
        if (b.equals(BigInteger.ZERO)) {
            textArea.append("b = 0, so the gcd is a = " + a + "\n");
            return new BigInteger[] { a, BigInteger.ONE, BigInteger.ZERO };
        }

        // Recursive call
        BigInteger[] extendedEuclideanValues = extendedEuclidean(b, a.mod(b), textArea);
        BigInteger gcd = extendedEuclideanValues[0];
        BigInteger x = extendedEuclideanValues[2];
        BigInteger y = extendedEuclideanValues[1].subtract((a.divide(b)).multiply(extendedEuclideanValues[2]));

        textArea.append("gcd(" + a + ", " + b + ") = " + gcd + "\n");
        textArea.append("x = " + x + ", y = " + y + "\n");
        return new BigInteger[] { gcd, x, y };
    }

    public static BigInteger generateRandomNumber(int numBits) {
        if (numBits <= 0) {
            throw new IllegalArgumentException("Number of bits must be positive");
        }

        SecureRandom random = new SecureRandom();
        BigInteger randomNumber = new BigInteger(numBits, random);

        while (randomNumber.bitLength() != numBits) {
            randomNumber = new BigInteger(numBits, random);
        }

        return randomNumber;
    }

    public static BigInteger encrypt(BigInteger e, BigInteger n, BigInteger message) {
        BigInteger result = BigInteger.ONE;
        BigInteger base = message.mod(n);

        while (e.compareTo(BigInteger.ZERO) > 0) {
            //Check if the last bit of 'e' is 1,if so multiply 'result' by 'base' and take modulus 'n' 
			if (e.testBit(0)) {
                //This ensures that the result does not become too large and stays within the range suitable for RSA.
				result = (result.multiply(base)).mod(n);
            }
            base = (base.multiply(base)).mod(n);
            e = e.shiftRight(1); //Shift bits to right to analize in the next iteration
        }

        return result;
    }

    public static BigInteger decrypt(BigInteger d, BigInteger n, BigInteger cipher) {
        BigInteger result = BigInteger.ONE;
        BigInteger base = cipher.mod(n);

        String binaryString = d.toString(2); //Convert d to its binary representation

        for (int i = 0; i < binaryString.length(); i++) {
            result = (result.multiply(result)).mod(n);//Square the 'result' and take its modulus 'n' 

            if (binaryString.charAt(i) == '1') {//If bits is 1, multiply 'result' by 'base' and take modulus 'n'
                result = (result.multiply(base)).mod(n);
            }
        }

        return result;
    }
}