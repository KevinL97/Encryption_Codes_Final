package rsa;

import javax.swing.*;
import java.awt.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.awt.event.*;
//import java.awt.event.FocusEvent;
//import java.awt.event.FocusListener;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;


public class RSAGUIApplication extends JFrame {

    private JTextArea inputTextArea;
    private JTextField nTextField, eTextField, dTextField;
    private JButton encryptButton, decryptButton, generateKeysButton, loadFileButton;
    private JComboBox<String> keyLengthComboBox;

    private JTextField pTextField, qTextField, fiNTextField;
    //private JTextField timeEncTextField, timeDecTextField;

    public RSAGUIApplication() {
        setTitle("RSA Encryption");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        JPanel contentPane = new JPanel(new GridBagLayout());
        contentPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel inputLabel = new JLabel("Input:");
        contentPane.add(inputLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        inputTextArea = new JTextArea();
        inputTextArea.setLineWrap(true);
        inputTextArea.setWrapStyleWord(true);
        inputTextArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane inputScrollPane = new JScrollPane(inputTextArea);
        contentPane.add(inputScrollPane, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        JLabel nLabel = new JLabel("n:");
        contentPane.add(nLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        nTextField = new JTextField();
        nTextField.setFont(new Font("Arial", Font.PLAIN, 14));
        nTextField.setEditable(false); // Hacer no editable
        contentPane.add(nTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel eLabel = new JLabel("e:");
        contentPane.add(eLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        eTextField = new JTextField();
        eTextField.setFont(new Font("Arial", Font.PLAIN, 14));
        eTextField.setEditable(false); // Hacer no editable
        contentPane.add(eTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel dLabel = new JLabel("d:");
        contentPane.add(dLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        dTextField = new JTextField();
        dTextField.setFont(new Font("Arial", Font.PLAIN, 14));
        dTextField.setEditable(false); // Hacer no editable
        contentPane.add(dTextField, gbc);

        // Creamos un panel para contener el JLabel y el icono de información
        JPanel keyLengthPanel = new JPanel(new BorderLayout());

        // Creamos el JLabel con el texto "Key Length (bits):"
        JLabel keyLengthLabel = new JLabel("Key Length (bits):");
        keyLengthPanel.add(keyLengthLabel, BorderLayout.WEST);

        // Ruta del archivo de icono
        String iconPath = "C:\\Users\\User\\Desktop\\Cifradores\\RSA\\rsa\\icons\\information.png";

        // Crear un icono de información/ayuda
        ImageIcon icon = new ImageIcon(iconPath);

        // Obtener la imagen del icono
        Image originalImage = icon.getImage();

        // Redimensionar la imagen
        int newWidth = 20; // Nuevo ancho
        int newHeight = 20; // Nuevo alto
        Image scaledImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

        // Crear un ImageIcon con la imagen redimensionada
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        // Creamos un JLabel para el icono de información y lo configuramos
        JLabel infoIconLabel = new JLabel(scaledIcon);
        Dimension labelSize = new Dimension(150, 100);
        infoIconLabel.setPreferredSize(labelSize);
        keyLengthPanel.add(infoIconLabel, BorderLayout.EAST);

        gbc.gridx = 0;
        gbc.gridy = 5;
        contentPane.add(keyLengthPanel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        /*keyLengthTextField = new JTextField("");
        keyLengthTextField.setFont(new Font("Arial", Font.PLAIN, 14));
        contentPane.add(keyLengthTextField, gbc);*/
        keyLengthComboBox = new JComboBox<>();
        keyLengthComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        keyLengthComboBox.addItem("1024");
        keyLengthComboBox.addItem("2048");
        keyLengthComboBox.addItem("3072");
        contentPane.add(keyLengthComboBox, gbc);

        gbc.gridx = 2;
        gbc.gridy = 2;
        encryptButton = new JButton("Encrypt");
        encryptButton.addActionListener(event -> encryptText());
        contentPane.add(encryptButton, gbc);

        gbc.gridx = 2;
        gbc.gridy = 3;
        decryptButton = new JButton("Decrypt");
        decryptButton.addActionListener(event -> decryptText());
        contentPane.add(decryptButton, gbc);

        gbc.gridx = 2;
        gbc.gridy = 4;
        generateKeysButton = new JButton("Generate Keys");
        generateKeysButton.addActionListener(event -> generateKeys());
        contentPane.add(generateKeysButton, gbc);

        gbc.gridx = 2;
        gbc.gridy = 5;
        loadFileButton = new JButton("Load File");
        loadFileButton.addActionListener(event -> loadFile());
        contentPane.add(loadFileButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        JLabel pLabel = new JLabel("p:");
        contentPane.add(pLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        pTextField = new JTextField();
        pTextField.setFont(new Font("Arial", Font.PLAIN, 14));
        pTextField.setEditable(false); // Hacer no editable
        pTextField.setPreferredSize(new Dimension(300, 30)); // Ajustar el ancho
        contentPane.add(pTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        JLabel qLabel = new JLabel("q:");
        contentPane.add(qLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 7;
        qTextField = new JTextField();
        qTextField.setFont(new Font("Arial", Font.PLAIN, 14));
        qTextField.setEditable(false); // Hacer no editable
        qTextField.setPreferredSize(new Dimension(300, 30)); // Ajustar el ancho
        contentPane.add(qTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        JLabel fiNLabel = new JLabel("\u03D5(n):");
        contentPane.add(fiNLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 8;
        fiNTextField = new JTextField();
        fiNTextField.setFont(new Font("Arial", Font.PLAIN, 14));
        fiNTextField.setEditable(false); // Hacer no editable
        fiNTextField.setPreferredSize(new Dimension(300, 30)); // Ajustar el ancho
        contentPane.add(fiNTextField, gbc);

        /*
        gbc.gridx = 0;
        gbc.gridy = 9;
        JLabel timeEncLabel = new JLabel("Execution time for Encryption (Milliseconds):");
        contentPane.add(timeEncLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 9;
        timeEncTextField = new JTextField();
        timeEncTextField.setFont(new Font("Arial", Font.PLAIN, 14));
        timeEncTextField.setEditable(false); // Hacer no editable
        timeEncTextField.setPreferredSize(new Dimension(300, 30));
        contentPane.add(timeEncTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 10;
        JLabel timeDecLabel = new JLabel("Execution time for Decryption (Milliseconds):");
        contentPane.add(timeDecLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 10;
        timeDecTextField = new JTextField();
        timeDecTextField.setFont(new Font("Arial", Font.PLAIN, 14));
        timeDecTextField.setEditable(false); // Hacer no editable
        timeDecTextField.setPreferredSize(new Dimension(300, 30)); // Ajustar el ancho
        contentPane.add(timeDecTextField, gbc);
        */

        setContentPane(contentPane);

        infoIconLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String message = "Select the bit length you want for the parameters";
                JOptionPane optionPane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
                JDialog dialog = optionPane.createDialog("Información");
                dialog.setLocationRelativeTo(RSAGUIApplication.this); // Mostrar el mensaje en el centro de la interfaz gráfica
                dialog.setVisible(true);
            }
        });


    }

    private double encryptionTime;
    private double decryptionTime;
    //private double keyGenerationTime;
    private double totalEncrTime;

    private void generateKeys() {
        String selectedKeyLength = (String) keyLengthComboBox.getSelectedItem();
        int keyLength = Integer.parseInt(selectedKeyLength);
        int iterations = 5;

        JFrame keysGenerationFrame = new JFrame("Key Generation Steps");
        keysGenerationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        keysGenerationFrame.setSize(800, 600);
        keysGenerationFrame.setLocationRelativeTo(this);

        JTextArea keysGenerationTextArea = new JTextArea();
        keysGenerationTextArea.setEditable(false);
        keysGenerationTextArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        keysGenerationTextArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JScrollPane keysGenerationScrollPane = new JScrollPane(keysGenerationTextArea);

        keysGenerationFrame.add(keysGenerationScrollPane, BorderLayout.CENTER);
        keysGenerationFrame.setVisible(true);

        //long startKeyGenTime = System.nanoTime();

        BigInteger pPrime = RSA.generateProbablePrime(keyLength / 2, iterations, keysGenerationTextArea);
        BigInteger qPrime = RSA.generateProbablePrime(keyLength / 2, iterations, keysGenerationTextArea);
        BigInteger nPrime = pPrime.multiply(qPrime);
        BigInteger fiN = pPrime.subtract(BigInteger.ONE).multiply(qPrime.subtract(BigInteger.ONE));
        BigInteger ePrime = RSA.generateCoprime(fiN, iterations, keysGenerationTextArea);
        //BigInteger dPrime0 = ePrime.modInverse(fiN);
        //System.out.println("d with library: " + String.valueOf(dPrime0));
        BigInteger dPrime = RSA.calculatePrivateKey(ePrime, fiN, keysGenerationTextArea);

        //long endKeyGenTime = System.nanoTime();
        //keyGenerationTime = endKeyGenTime - startKeyGenTime;
        
        nTextField.setText(nPrime.toString());
        eTextField.setText(ePrime.toString());
        dTextField.setText(dPrime.toString());

        pTextField.setText(pPrime.toString());
        qTextField.setText(qPrime.toString());
        fiNTextField.setText(fiN.toString());
    }

    private void encryptText() {

        String input = inputTextArea.getText();
        int selectedKeyLength = Integer.parseInt((String) keyLengthComboBox.getSelectedItem());
        int maxTextLength = selectedKeyLength / 8;

        if (input.length() > maxTextLength) {
            JOptionPane.showMessageDialog(this, "The input text length exceeds the selected key length. Please choose a larger key length or reduce the text size.", "Text Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Obtener el uso de memoria antes de la encriptación
        long beforeEncryptionMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        // Iniciar medición de CPU
        ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
        long startCpuTime = threadBean.getCurrentThreadCpuTime();
        long startTime = System.nanoTime();

        long startEncTime = System.nanoTime();
        BigInteger e = new BigInteger(eTextField.getText());
        BigInteger n = new BigInteger(nTextField.getText());
        BigInteger ciphertext = RSA.encrypt(e, n, new BigInteger(1, input.getBytes()));
        inputTextArea.setText(ciphertext.toString(16));

        //Calculate time execution in miliseconds
        long endEncTime = System.nanoTime();

        //Millisecond
        encryptionTime = endEncTime - startEncTime;
        //totalEncrTime = encryptionTime + keyGenerationTime;
        totalEncrTime = encryptionTime;

        // Finalizar medición de CPU
        long endTime = System.nanoTime();
        long endCpuTime = threadBean.getCurrentThreadCpuTime();
        long encryptionCPUTime = endTime - startTime;
        long cpuTime = endCpuTime - startCpuTime;
        
        // Obtener el uso de memoria después de la encriptación
        long afterEncryptionMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        // Calcular el uso de memoria durante la encriptación en bytes (B)
        long encryptionMemoryUsage = afterEncryptionMemory - beforeEncryptionMemory;

        // Calculate throughput
        int inputSize = input.getBytes().length;
        //For bytes
        double throughputBps = (double) inputSize / (totalEncrTime / 1_000_000_000.0);
        //For bits
        double throughputbps = throughputBps * 8;

        // Calcular el porcentaje de uso de CPU
        double cpuUsagePercent = (double) cpuTime / encryptionCPUTime * 100;

        // Save throughput in file
        saveResults("encrypt",inputSize, throughputBps, throughputbps);

        // Guardar uso de memoria en archivo
        saveMemoryResults("encrypt", inputSize, encryptionMemoryUsage);

        // Guardar los resultados del uso de CPU
        saveCPU("encrypt", inputSize, encryptionTime / 1_000_000, throughputBps, throughputbps, cpuUsagePercent);

    
    }

    

    private void decryptText() {
        // Obtener el uso de memoria antes de la desencriptación
        long beforeDecryptionMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        // Iniciar medición de CPU
        ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
        long startCpuTime = threadBean.getCurrentThreadCpuTime();
        long startTime = System.nanoTime();


        long startDecTime = System.nanoTime();
        String input = inputTextArea.getText();
        BigInteger d = new BigInteger(dTextField.getText());
        BigInteger n = new BigInteger(nTextField.getText());
        BigInteger plaintext = RSA.decrypt(d, n, new BigInteger(input, 16));
        inputTextArea.setText(new String(plaintext.toByteArray()));

        //Calculate execution time
        long endTime = System.nanoTime();

        //Miliseconds
        decryptionTime = endTime - startDecTime;

        saveTimesToFile();

        // Finalizar medición de CPU
        long endCPUTime = System.nanoTime();
        long endCpuTime = threadBean.getCurrentThreadCpuTime();
        long decryptionCPUTime = endCPUTime - startTime;
        long cpuTime = endCpuTime - startCpuTime;

        // Obtener el uso de memoria después de la desencriptación
        long afterDecryptionMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        // Calcular el uso de memoria durante la desencriptación en bytes (B)
        long decryptionMemoryUsage = afterDecryptionMemory - beforeDecryptionMemory;

        // Calculate throughput
        int inputSize = input.getBytes().length;
        //For bytes
        double throughputBps = (double) inputSize / (decryptionTime / 1_000_000_000.0);
        //For bits
        double throughputbps = throughputBps * 8;

        // Calcular el porcentaje de uso de CPU
        double cpuUsagePercent = (double) cpuTime / decryptionCPUTime * 100;

        // Save throughput in file
        saveResults("decrypt", inputSize, throughputBps, throughputbps);

        // Guardar uso de memoria en archivo
        saveMemoryResults("decrypt", inputSize, decryptionMemoryUsage);

        // Guardar los resultados del uso de CPU
        saveCPU("decrypt", inputSize, decryptionTime / 1_000_000, throughputBps, throughputbps, cpuUsagePercent);


    }

    private void loadFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                byte[] fileBytes = Files.readAllBytes(Paths.get(fileChooser.getSelectedFile().getAbsolutePath()));
                inputTextArea.setText(new String(fileBytes));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    //Save time
    private void saveTimesToFile() {
        try {
            FileWriter writer = new FileWriter("time_3072.txt", true);

            // Convertir los tiempos a milisegundos
            double encryptionTimeMs = totalEncrTime / 1000000;
            double decryptionTimeMs = decryptionTime / 1000000;

            writer.write(encryptionTimeMs + "," + decryptionTimeMs + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Save Throughput
    private void saveResults(String operation, int inputSize, double throughputBps, double throughputbps) {
        try {
            FileWriter writer = new FileWriter("throughput_3072.txt", true);
            writer.write(operation + "," + inputSize + "," + throughputBps + "," + throughputbps + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Save Memory Usage
    private void saveMemoryResults(String operation, int inputSize,  long memoryUsage) {
        try (FileWriter writer = new FileWriter("memory_3072.txt", true)) {
            writer.write(operation + "," + inputSize + "," + memoryUsage + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Save CPU Usage
    private void saveCPU(String operation, int inputSize, double executionTime, double throughputBps, double throughputbps, double cpuUsage) {
        try {
            FileWriter writer = new FileWriter("CPU_3072.txt", true);
            writer.write(operation + "," + inputSize + "," + executionTime + "," + throughputBps + "," + throughputbps + "," + cpuUsage + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RSAGUIApplication app = new RSAGUIApplication();
            app.setVisible(true);
        });
    }
}