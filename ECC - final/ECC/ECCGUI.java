package ECC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
//import java.util.List;
import java.math.BigInteger;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

public class ECCGUI extends JFrame implements ActionListener {

    private JTextArea inputTextArea, outputTextArea, encryptedTextArea, infoTextArea;
    private JButton encryptButton, clearButton;
    private JComboBox<String> modeComboBox;

    private EllipticCurve curve;
    private ECC ecc;

    public ECCGUI() {
        super("Elliptic Curve Cryptography with AES");

        // Inicializa la GUI
        initComponents();

        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 10, 30, 10)); // Ajuste del margen general para todos los componentes


        // Panel de entrada de texto
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputTextArea = new JTextArea(10, 20);
        inputTextArea.setLineWrap(true);
        JScrollPane inputScrollPane = new JScrollPane(inputTextArea);
        inputPanel.add(new JLabel("Input Text:"), BorderLayout.NORTH);
        inputPanel.add(inputScrollPane, BorderLayout.CENTER);

        // Panel de salida de texto
        JPanel outputPanel = new JPanel(new BorderLayout());
        outputTextArea = new JTextArea(10, 20);
        outputTextArea.setLineWrap(true);
        outputTextArea.setEditable(false);
        JScrollPane outputScrollPane = new JScrollPane(outputTextArea);
        outputPanel.add(new JLabel("Decrypted Text:"), BorderLayout.NORTH);
        outputPanel.add(outputScrollPane, BorderLayout.CENTER);

        // Panel de botones de cifrado y descifrado
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 10, 30));
        encryptButton = new JButton("Encrypt");
        clearButton = new JButton("Clear");
        encryptButton.addActionListener(this);
        clearButton.addActionListener(this);
        buttonPanel.add(encryptButton);
        buttonPanel.add(clearButton);

        // Panel de texto cifrado
        encryptedTextArea = new JTextArea(10, 20);
        encryptedTextArea.setLineWrap(true);
        encryptedTextArea.setEditable(false);
        JScrollPane encryptedScrollPane = new JScrollPane(encryptedTextArea);
        JPanel encryptedPanel = new JPanel(new BorderLayout());
        encryptedPanel.add(new JLabel("Encrypted Text:"), BorderLayout.NORTH);
        encryptedPanel.add(encryptedScrollPane, BorderLayout.CENTER);

        // Panel de información del proceso de cifrado
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 10, 30));
        infoTextArea = new JTextArea();
        infoTextArea.setEditable(false);
        JScrollPane infoScrollPane = new JScrollPane(infoTextArea);
        infoPanel.add(new JLabel("Encryption Process:"), BorderLayout.NORTH);
        infoPanel.add(infoScrollPane, BorderLayout.CENTER);

        // Nuevo JComboBox para seleccionar el modo
        Dimension comboBoxSize = new Dimension(200, 30); // Ancho de 150 pixels
        modeComboBox = new JComboBox<>(new String[]{"E-1174", "P-256", "M-221"});
        modeComboBox.setPreferredSize(comboBoxSize);
        JPanel comboBoxPanel = new JPanel(new GridBagLayout());
        comboBoxPanel.setBorder(BorderFactory.createTitledBorder("Elliptic curve selection: ..."));
        GridBagConstraints gbcComboBox = new GridBagConstraints();
        gbcComboBox.gridx = 0;
        gbcComboBox.gridy = 0;
        gbcComboBox.insets = new Insets(5, 10, 5, 10);
        comboBoxPanel.add(modeComboBox, gbcComboBox);

        // Agregar paneles al marco
        mainPanel.add(inputPanel, BorderLayout.WEST);
        mainPanel.add(encryptedPanel, BorderLayout.CENTER);
        mainPanel.add(outputPanel, BorderLayout.EAST);

        JPanel middlePanel = new JPanel(new BorderLayout());
        middlePanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        middlePanel.add(mainPanel, BorderLayout.NORTH);
        middlePanel.add(comboBoxPanel, BorderLayout.CENTER);
        
        
        add(middlePanel, BorderLayout.NORTH);
        add(infoPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private double encryptionTime;
    private double decryptionTime;

    @Override
    public void actionPerformed(ActionEvent e) {
        String inputText = inputTextArea.getText();
        Object[] encryptionResult  = null;
        KeyPair keys = null;

        if (e.getSource() == encryptButton) {
            // Define la curva elíptica
            
            //curve = new EllipticCurve(4, 20, 29, new Point(1, 5));
            
            String selectedMode = (String) modeComboBox.getSelectedItem();
            switch (selectedMode) {
                case "E-1174":
                    // Curva elíptica Curve1174
                    curve = createCurve1174();
                    break;
                case "P-256":
                    // Curva elíptica NIST P-256
                    curve = createNISTP256Curve();
                    break;
                case "M-221":
                    // Curva elíptica NIST M-221
                    curve = createM221Curve();
                    break;
                // Agrega más casos para otras curvas elípticas
            }

            ecc = new ECC(curve);

            infoTextArea.setText("");
            outputTextArea.setText("");
            encryptedTextArea.setText("");

            Helpers.clearSubKeysFile();
            keys = ECC.generateKeyPair(curve); // Genera un solo par de claves
            //ecc.displayCodeTable(infoTextArea);

            // Medir memoria antes del cifrado
            Runtime runtime = Runtime.getRuntime();
            runtime.gc(); // Invocar GC para obtener una medición más precisa
            
            // Obtener el uso de memoria antes de la encriptación
            long beforeEncryptionMemory = runtime.totalMemory() - runtime.freeMemory();

            // Iniciar medición de CPU
            ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
            long startCpuTime = threadBean.getCurrentThreadCpuTime();

            long startEncTime = System.nanoTime();
            encryptionResult = ecc.encrypt(inputText, keys.getPublicKey(), infoTextArea); // Cifrar el nuevo texto
            long endEncTime = System.nanoTime();

            String cipherText = (String) encryptionResult[0];
            String ivHex = (String) encryptionResult[1];
            Point alicePublicKey = (Point) encryptionResult[2];
            infoTextArea.append("\n6) Alice send this to Bob:\n");
            infoTextArea.append(cipherText);
            infoTextArea.append("\n");
            encryptedTextArea.setText(cipherText);
            encryptionTime = endEncTime - startEncTime;

            // Finalizar medición de CPU
            long endCpuTime = threadBean.getCurrentThreadCpuTime();
            long cpuTime = endCpuTime - startCpuTime;

            runtime.gc(); // Invocar GC nuevamente después de la operación para limpieza
            // Obtener el uso de memoria después de la encriptación
            long afterEncryptionMemory = runtime.totalMemory() - runtime.freeMemory();

            // Calcular el uso de memoria durante la encriptación en bytes (B)
            long encryptionMemoryUsage = afterEncryptionMemory - beforeEncryptionMemory;
            if (encryptionMemoryUsage < 0) encryptionMemoryUsage = 0; // Ajustar valores negativos a cero

            // Calculate throughput
            int inputSize = inputText.getBytes().length;
            //For bytes
            double throughputBps = (double) inputSize / (encryptionTime / 1_000_000_000.0);
            //For bits
            double throughputbps = throughputBps * 8;

            // Calcular el porcentaje de uso de CPU
            double cpuUsagePercent = (double) cpuTime / encryptionTime * 100;


            // Save throughput in file
            saveResults("encrypt", inputSize, throughputBps, throughputbps);

            // Guardar uso de memoria en archivo
            saveMemoryResults("encrypt", inputSize, encryptionMemoryUsage);

            // Guardar los resultados del uso de CPU
            saveCPU("encrypt", inputSize, throughputBps, throughputbps, cpuUsagePercent);


            EncryptionParameters encryptionParameters = ecc.getEncryptionParameters(keys.getPublicKey(), ivHex, alicePublicKey);
            
            // Obtener el uso de memoria antes de la desencriptación
            long beforeDecryptionMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

            // Iniciar medición de CPU
            ThreadMXBean threadCPUBean = ManagementFactory.getThreadMXBean();
            long startCpuDTime = threadCPUBean.getCurrentThreadCpuTime();

            long startDecTime = System.nanoTime();
            String decryptedInput = ecc.decrypt(cipherText, keys.getPrivateKey(), encryptionParameters, infoTextArea);
            long endDecTime = System.nanoTime();
            outputTextArea.setText(decryptedInput);
            decryptionTime = endDecTime - startDecTime;


            // Finalizar medición de CPU
            long endCpuTime1 = threadCPUBean.getCurrentThreadCpuTime();
            long cpuTime1 = endCpuTime1 - startCpuDTime;


            //outputText = ecc.decrypt(cipherText, keys.getPrivateKey(), infoTextArea);
            //infoTextArea.append("\n5) Translate each point to a character:\n");
            //infoTextArea.append("Decrypted text : " + decryptedInput);

            outputTextArea.setText(decryptedInput);

            saveTimesToFile();

            // Obtener el uso de memoria después de la desencriptación
            long afterDecryptionMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

            // Calcular el uso de memoria durante la desencriptación en bytes (B)
            long decryptionMemoryUsage = afterDecryptionMemory - beforeDecryptionMemory;

            // Calculate throughput
            int inpuDectSize = cipherText.getBytes().length / 2;
            //For bytes
            double throughputBps_1 = (double) inpuDectSize / (decryptionTime / 1_000_000_000.0);
            //For bits
            double throughputbps_1 = throughputBps * 8;

            // Calcular el porcentaje de uso de CPU
            double cpuUsagePercent1 = (double) cpuTime1 / decryptionTime * 100;


            // Save throughput in file
            saveResults("decrypt", inpuDectSize, throughputBps_1, throughputbps_1);

            // Guardar uso de memoria en archivo
            saveMemoryResults("decrypt", inpuDectSize, decryptionMemoryUsage);

            // Guardar los resultados del uso de CPU
            saveCPU("decrypt", inputSize, throughputbps_1, throughputbps, cpuUsagePercent1);

            

        } else if (e.getSource() == clearButton) {
            // Limpiar todos los campos de texto
            Helpers.clearSubKeysFile();
            inputTextArea.setText("");
            outputTextArea.setText("");
            encryptedTextArea.setText("");
            infoTextArea.setText("");
            encryptionResult = null; // Reinicializar cipherText a null
            keys = null; // Reinicializar keys a null
        }  
    }

    private EllipticCurve createCurve1174() {
        BigInteger p = new BigInteger("2").pow(251).subtract(new BigInteger("9"));
        BigInteger a = new BigInteger("1174");
        BigInteger b = new BigInteger("1");
        BigInteger x = new BigInteger("1582619097725911541954547006453739763381091388846394833492296309729998839514"); // Coordenada x del punto base
        BigInteger y = new BigInteger("1408064552111686812003849472883342418268552463368335372985825484318164105384"); // Coordenada y del punto base
        Point g = new Point(x, y);
        return new EllipticCurve(a, b, p, g);
    }

    private EllipticCurve createNISTP256Curve() {
        BigInteger p = new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951");
        BigInteger a = new BigInteger("-3");
        BigInteger b = new BigInteger("41058363725152142129326129780047268409114441015993725554835256314039467401291");
        Point g = new Point(
                new BigInteger("48439561293906451759052585252797914202762949526041747995844080717082404635286"),
                new BigInteger("36134794756577183366534992453949150051429845216487380239116097112389808073059")
        );
        return new EllipticCurve(a, b, p, g);
    }

    private EllipticCurve createM221Curve() {
        BigInteger p = new BigInteger("6277101735386680763835789423207666416102355444459739541047");
        BigInteger a = new BigInteger("6277101735386680763835789423184887902756787778601141086066");
        BigInteger b = new BigInteger("2702627879641361961941029801828705734793045847880133174900");
        Point g = new Point(
                new BigInteger("2722199071451431051069537841689051308491261328636682477198"),
                new BigInteger("4418243372714423442765109689069973422089114465591206284845")
        );
        return new EllipticCurve(a, b, p, g);
    }

    private void saveTimesToFile() {
        try {
            FileWriter writer = new FileWriter("time_M221_3.txt", true);

            // Convertir los tiempos a milisegundos
            double encryptionTimeMs = encryptionTime / 1000000;
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
            FileWriter writer = new FileWriter("throughput_M221_3.txt", true);
            writer.write(operation + "," + inputSize + "," + throughputBps + "," + throughputbps + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Save Memory Usage
    private void saveMemoryResults(String operation, int inputSize,  long memoryUsage) {
        try (FileWriter writer = new FileWriter("memory_M221_3.txt", true)) {
            writer.write(operation + "," + inputSize + "," + memoryUsage + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Save CPU Usage
    private void saveCPU(String operation, int inputSize, double throughputBps, double throughputbps, double cpuUsage) {
        try {
            FileWriter writer = new FileWriter("CPU_M221_3.txt", true);
            writer.write(operation + "," + inputSize + "," + throughputBps + "," + throughputbps + "," + cpuUsage + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ECCGUI::new);
    }
}
