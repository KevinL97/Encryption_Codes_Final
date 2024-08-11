import java.nio.charset.StandardCharsets;

public class byte_calculator {
    public static void main(String[] args) {
        String inputText = "Started earnest brother believe an exposed so. Me he believing daughters if forfeited at furniture.";
        
        // Obtener el tamaño en bytes
        byte[] byteArray = inputText.getBytes(StandardCharsets.UTF_8);
        int inputSizeInBytes = byteArray.length;

        // Imprimir el tamaño en bytes
        System.out.println("Tamaño en bytes: " + inputSizeInBytes);

        // Calcular el tamaño en bits
        int inputSizeInBits = inputSizeInBytes * 8;

        // Imprimir el tamaño en bits
        System.out.println("Tamaño en bits: " + inputSizeInBits);
    }
}
