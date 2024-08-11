import javax.swing.*;
import javax.swing.border.*;
import java.lang.System;


import java.awt.*;
import java.awt.event.*;
import java.io.PrintStream;
//import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

public class AESGUISwing extends JFrame {
    // Declarar los componentes de la interfaz gráfica
    //private JTextField functionTypeField, operationModeField, transmissionSizeField;
    private JTextArea inputTextArea, keyTextArea, ivTextArea, outputTextArea, outputTextArea1, originalTextArea, stepTextArea, stepTextArea1;

    private JPanel topPanel;
    private JPanel rightPanel;
    private JPanel encryptedPanel;
    private JPanel outputPanel;
    private JPanel outputPanel1;
    private JComboBox<String> modeComboBox;

    private JScrollPane scrollOut;
    private JScrollPane scrollOut1;
    private JScrollPane scrollStep;
    private JScrollPane scrollStep1;
    
    


    //private JTabbedPane tabbedPane;

    public AESGUISwing() {

        // Layout
        setLayout(new BorderLayout());

        // Panel superior
        topPanel = new JPanel();
        topPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        topPanel.setBackground(Color.BLACK);


        JLabel titleLabel = new JLabel("Advanced Encryption Standard Algorithm");
        titleLabel.setFont(new Font("Cambria", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        topPanel.add(titleLabel);

        add(topPanel, BorderLayout.NORTH);

        // Panel para entrada de texto
        JPanel inputPanel = new JPanel(new BorderLayout());
        //inputPanel.setBorder(BorderFactory.createTitledBorder("Input Data"));
            
        // Crear un borde con un título personalizado y una fuente más grande
        TitledBorder titledInput = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "", TitledBorder.CENTER, TitledBorder.TOP);
        titledInput = new TitledBorder(titledInput, "Input Data", TitledBorder.CENTER, TitledBorder.TOP); // Crear un nuevo TitledBorder con el título
        titledInput.setTitleFont(new Font("Cambria", Font.BOLD, 16)); // Establecer la fuente del título

        // Asignar el borde personalizado al panel
        inputPanel.setBorder(titledInput);

        inputTextArea = new JTextArea(1, 10);
        inputTextArea.setLineWrap(true);
        JScrollPane scroll = new JScrollPane(inputTextArea);

        inputTextArea.setBorder(BorderFactory.createTitledBorder("Plain text"));
        inputPanel.add(scroll, BorderLayout.CENTER);

        //Panel para contener los modos y tipo de texto
        JPanel selectionPanel = new JPanel();
        selectionPanel.setLayout(new BoxLayout(selectionPanel, BoxLayout.Y_AXIS)); // BoxLayout en orientación vertical
        selectionPanel.setBorder(BorderFactory.createTitledBorder("Mode and type of text Panel"));


        // Panel para el menú de selección
        JPanel infomodePanel = new JPanel();
        infomodePanel.setBorder(BorderFactory.createTitledBorder("Cipher mode"));
        infomodePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        String[] modes = {"Select an option","ECB", "CBC"};
        modeComboBox = new JComboBox<>(modes);

        //Menu para seleccion de tipe de texto
        JPanel selectTextPanel = new JPanel();
        selectTextPanel.setAlignmentX(Component.CENTER_ALIGNMENT); // Centrar horizontalmente
        selectTextPanel.setBorder(BorderFactory.createTitledBorder("Text types"));
        //String[] typeText = {"Ascii", "Binary", "Hexadecimal"};
        //modeComboBox1 = new JComboBox<>(typeText);


        // Crear el JTextArea con el texto deseado
        JTextArea textArea = new JTextArea();
        textArea.setText("The text format must be one of these options:\n" +
                          "==> Ascii: Lorem ipsum \n" +
                          "==> Binary: 01001100 01101111 01110010.... \n" + 
                          "==> Hexadecimal: 4c6f72656d20697073756d");
        textArea.setEditable(false); // Desactivar la edición del texto
        textArea.setWrapStyleWord(false); // Envolver palabras
        textArea.setLineWrap(false); // Envolver líneas

        // Crear el JScrollPane y agregar el JTextArea
        JScrollPane scrollPane = new JScrollPane(textArea);
        selectTextPanel.add(scrollPane);

        // Establecer las dimensiones preferidas
        Dimension comboBoxSize = new Dimension(150, 30); // Ancho de 150 pixels
        modeComboBox.setPreferredSize(comboBoxSize);

        // Ruta del archivo de icono
        String iconPath = "C:\\Users\\User\\Desktop\\Cifradores\\AES\\AES_modificado\\icons\\information.png";

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

        JLabel infoLabel = new JLabel(scaledIcon);
        Dimension labelSize = new Dimension(150, 100);
        infoLabel.setPreferredSize(labelSize);

        // Agregar el icono de información/ayuda en la esquina superior derecha
        infomodePanel.add(infoLabel);

        // Agregar el JComboBox al panel
        infomodePanel.add(modeComboBox);
        
        //Agregar el panel de info y seleccion de modo al panel
        selectionPanel.add(infomodePanel);

        // Agregar glue para empujar modeComboBox1 hacia abajo
        selectionPanel.add(Box.createVerticalGlue());

        // Agregar la seleccion de texto al seleccion
        selectionPanel.add(selectTextPanel);

        // Agregar el JComboBox al panel de entrada
        inputPanel.add(selectionPanel, BorderLayout.WEST);

        // Panel para la llave y el vector de inicialización
        JPanel keyAndIVPanel = new JPanel(new GridLayout(2, 1));
        
        // Crear un borde con un título personalizado y una fuente más grande
        TitledBorder titledBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Keys for the encryption", TitledBorder.CENTER, TitledBorder.TOP);
        titledBorder = new TitledBorder(titledBorder, "Keys for encryption and decryption", TitledBorder.CENTER, TitledBorder.TOP); // Crear un nuevo TitledBorder con el título
        titledBorder.setTitleFont(new Font("Cambria", Font.BOLD, 16)); // Establecer la fuente del título

        // Asignar el borde personalizado al panel
        keyAndIVPanel.setBorder(titledBorder);

        // Panel para la llave
        JPanel keyPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcKey = new GridBagConstraints();
        gbcKey.gridx = 0;
        gbcKey.gridy = 0;
        gbcKey.insets = new Insets(5, 10, 5, 10); // Espacio alrededor de los componentes

        JLabel infoKey = new JLabel(scaledIcon);

        keyTextArea = new JTextArea(3, 25);
        keyTextArea.setLineWrap(true);
        JScrollPane scrollkey = new JScrollPane(keyTextArea);


        keyPanel.add(new JLabel("Cipher key: "), gbcKey);
        gbcKey.gridx = 1;
        keyPanel.add(scrollkey, gbcKey);
        gbcKey.gridx = 2;
        keyPanel.add(infoKey, gbcKey);
        

        keyAndIVPanel.add(keyPanel);
        

        // Panel para el vector de inicialización
        JPanel ivPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcIV = new GridBagConstraints();
        gbcIV.gridx = 0;
        gbcIV.gridy = 0;
        gbcIV.insets = new Insets(5, 5, 5, 5); // Espacio alrededor de los componentes
    
        JLabel infoIV = new JLabel(scaledIcon);

        ivTextArea = new JTextArea(3, 25);
        ivTextArea.setLineWrap(true);
        JScrollPane ivscroll = new JScrollPane(ivTextArea);

        ivPanel.add(new JLabel("Initialization vector: "), gbcIV);
        gbcIV.gridx = 1;
        ivPanel.add(ivscroll, gbcIV);
        gbcIV.gridx = 2;
        ivPanel.add(infoIV, gbcIV);

        keyAndIVPanel.add(ivPanel);

        //Panel para ver el texto cifrado
        encryptedPanel = new JPanel(new BorderLayout());
        //encryptedPanel.setBorder(BorderFactory.createTitledBorder("Encrypted Text"));

        // Crear un borde con un título personalizado y una fuente más grande
        TitledBorder titledOutput = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "", TitledBorder.CENTER, TitledBorder.TOP);
        titledOutput = new TitledBorder(titledOutput, "Output", TitledBorder.CENTER, TitledBorder.TOP); // Crear un nuevo TitledBorder con el título
        titledOutput.setTitleFont(new Font("Cambria", Font.BOLD, 16)); // Establecer la fuente del título

        // Asignar el borde personalizado al panel
        encryptedPanel.setBorder(titledOutput);

        originalTextArea = new JTextArea(3, 20);
        originalTextArea.setLineWrap(false);
        originalTextArea.setEditable(false);
        JScrollPane scrollOriginal = new JScrollPane(originalTextArea);
        scrollOriginal.setBorder(BorderFactory.createTitledBorder("Original Text"));

        encryptedPanel.add(scrollOriginal, BorderLayout.NORTH);

        outputTextArea = new JTextArea(1, 20);
        outputTextArea.setLineWrap(true);
        outputTextArea.setEditable(false);
        scrollOut = new JScrollPane(outputTextArea);
        scrollOut.setBorder(BorderFactory.createTitledBorder("Encrypted Text (Hexadecimal)"));

        encryptedPanel.add(scrollOut, BorderLayout.CENTER);

        outputPanel1 = new JPanel(new GridLayout());

        outputTextArea1 = new JTextArea(3, 20);
        outputTextArea1.setLineWrap(true);
        outputTextArea1.setEditable(false);
        scrollOut1 = new JScrollPane(outputTextArea1);
        scrollOut1.setBorder(BorderFactory.createTitledBorder("Decrypted Text"));

        outputPanel1.add(scrollOut1, BorderLayout.CENTER);

        encryptedPanel.add(outputPanel1, BorderLayout.SOUTH);


        JLabel infoOut = new JLabel(scaledIcon);
        //encryptedPanel.add(infoOut, BorderLayout.SOUTH);


        // Panel para mostrar el Paso a Paso del cifrado AES
        outputPanel = new JPanel(new BorderLayout());

        //stepTextArea = new JTextArea(3, 20);
        //stepTextArea.setLineWrap(true);
        //stepTextArea.setEditable(false);
        //scrollStep = new JScrollPane(stepTextArea);
        //scrollStep.setBorder(BorderFactory.createTitledBorder("Step by Step Area"));

        //outputPanel.add(scrollStep, BorderLayout.CENTER);



        // Crear un borde con un título personalizado y una fuente más grande
        TitledBorder titledStep = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "", TitledBorder.CENTER, TitledBorder.TOP);
        titledStep = new TitledBorder(titledStep, "Step by Step", TitledBorder.CENTER, TitledBorder.TOP); // Crear un nuevo TitledBorder con el título
        titledStep.setTitleFont(new Font("Cambria", Font.BOLD, 16)); // Establecer la fuente del título

        // Asignar el borde personalizado al panel
        outputPanel.setBorder(titledStep);
        



        // Crear un panel para la parte izquierda
        JPanel leftPanel = new JPanel(new GridLayout(2, 1));
        GridBagConstraints gbcLeft = new GridBagConstraints();
        gbcLeft.gridx = 0;
        gbcLeft.gridy = 0;
        gbcLeft.weightx = 1.0; // Expandir horizontalmente
        gbcLeft.weighty = 1.0; // Expandir verticalmente
        gbcLeft.fill = GridBagConstraints.BOTH;
        leftPanel.add(inputPanel, gbcLeft);
        gbcLeft.gridy = 1;
        leftPanel.add(keyAndIVPanel, gbcLeft);

        // Crear un panel para la parte derecha
        rightPanel = new JPanel(new GridLayout(2,1));
        //rightPanel.add(new JTextArea(5, 20), BorderLayout.CENTER);
        GridBagConstraints gbcRight = new GridBagConstraints();
        gbcRight.gridx = 0;
        gbcRight.gridy = 0;
        gbcRight.weightx = 1.0; // Expandir horizontalmente
        gbcRight.weighty = 1.0; // Expandir verticalmente
        gbcRight.fill = GridBagConstraints.BOTH;
        rightPanel.add(encryptedPanel, gbcRight);
        gbcRight.gridy = 1;
        rightPanel.add(outputPanel, gbcRight);
        

        // Crear un panel para contener los dos paneles
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcMain = new GridBagConstraints();
        gbcMain.gridx = 0;
        gbcMain.gridy = 0;
        gbcMain.weightx = 1.0; // Expandir horizontalmente
        gbcMain.weighty = 1.0; // Expandir verticalmente
        gbcMain.fill = GridBagConstraints.BOTH;
        mainPanel.add(leftPanel, gbcMain);
        gbcMain.gridx = 1;
        mainPanel.add(rightPanel, gbcMain);

        // Crear un panel para contener todos los componentes
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(topPanel, BorderLayout.NORTH);
        contentPanel.add(mainPanel, BorderLayout.CENTER);


        // Agregar el panel principal al centro del JFrame
        //getContentPane().setLayout(new BorderLayout());
        getContentPane().add(contentPanel);

        setVisible(true);

        

        // Panel inferior para botones
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBackground(Color.WHITE);

        JButton encryptButton = new JButton("Encrypt");
        encryptButton.setFont(new Font("Cambria", Font.BOLD, 16));
        encryptButton.setBackground(Color.BLACK);
        encryptButton.setForeground(Color.WHITE);
        encryptButton.setBorder(new EmptyBorder(10, 20, 10, 20));

        JButton decryptButton = new JButton("Decrypt");
        decryptButton.setFont(new Font("Cambria", Font.BOLD, 16));
        decryptButton.setBackground(Color.BLACK);
        decryptButton.setForeground(Color.WHITE);
        decryptButton.setBorder(new EmptyBorder(10, 20, 10, 20));


        buttonsPanel.add(encryptButton);
        buttonsPanel.add(decryptButton);

        add(buttonsPanel, BorderLayout.SOUTH);


        // Configuración de ventana
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Graphic Interface AES - 128 bits");
        
        // Controladores de eventos  
        encryptButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            originalTextArea.setText("");
            outputTextArea.setText("");
            outputTextArea1.setText("");
            handleEncryptionRequest();  
        }
        });

        decryptButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                outputTextArea1.setText("");
                handleDecryptionRequest();  
            }
            });

        modeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedMode = (String) modeComboBox.getSelectedItem();
                if (selectedMode.equals("ECB")) {
                    // Modo ECB seleccionado, hacer el JTextArea no editable
                    ivTextArea.setText("");
                    ivTextArea.setEditable(false);
                    ivTextArea.setBackground(Color.decode("#404040")); // Gris oscuro
                } else if (selectedMode.equals("CBC")) {
                    // Modo CBC seleccionado, hacer el JTextArea editable
                    ivTextArea.setEditable(true);
                    ivTextArea.setBackground(null);

                }
            }
        });

        infoLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String message = "* Electronic Codebook mode (ECB)\n" +
                                "* Cipher Block Chaining mode (CBC)";
                JOptionPane optionPane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
                JDialog dialog = optionPane.createDialog("Information about cipher mode");
                dialog.setLocationRelativeTo(AESGUISwing.this); // Mostrar el mensaje en el centro de la interfaz gráfica
                dialog.setVisible(true);
            }
        });

        infoKey.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String message = "The cipher key must be: \n" +
                                "=> 128 bits \n" + 
                                "=> 32 Hexadecimal characters \n" + 
                                "=> 16 Ascii characters";
                JOptionPane optionPane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
                JDialog dialog = optionPane.createDialog("Information about key");
                dialog.setLocationRelativeTo(AESGUISwing.this); // Mostrar el mensaje en el centro de la interfaz gráfica
                dialog.setVisible(true);
            }
        });

        infoIV.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String message = "The initialization vector must be: \n" +
                                "=> 128 bits\n" + 
                                "=> 32 Hexadecimal characters \n" + 
                                "=> 16 Ascii characters";
                JOptionPane optionPane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
                JDialog dialog = optionPane.createDialog("Information");
                dialog.setLocationRelativeTo(AESGUISwing.this); // Mostrar el mensaje en el centro de la interfaz gráfica
                dialog.setVisible(true);
            }
        });

        infoOut.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String message = "The 'Original Text' will be displayed as plain text and the 'Encrypted Text' will be displayed in Hexadecimal format.";
                JOptionPane optionPane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
                JDialog dialog = optionPane.createDialog("Information");
                dialog.setLocationRelativeTo(AESGUISwing.this); // Mostrar el mensaje en el centro de la interfaz gráfica
                dialog.setVisible(true);
            }
        });

    }

    private long encryptionTime;
    private long decryptionTime;
    

    private void handleEncryptionRequest() {
        int functionType = Integer.parseInt("0");
        String operationMode = (String) modeComboBox.getSelectedItem();
        System.out.println(operationMode);
        int transmissionSize = Integer.parseInt("0");
        String inputText = inputTextArea.getText();
        String key = keyTextArea.getText();
        String iv = ivTextArea.getText();
        String mytext = "";

        // Crear un área de texto para mostrar las iteraciones de cifrado
        stepTextArea = new JTextArea();

        // Redirigir la salida estándar al área de texto
        PrintStream printStream = new PrintStream(new CustomOutputStream(stepTextArea));
        System.setOut(printStream);
    
        // Crear una instancia de la clase Interface y realizar el cifrado
        Interface aesInterface = new Interface();


        // Medir memoria antes del cifrado
        Runtime runtime = Runtime.getRuntime();
        runtime.gc(); // Invocar GC para obtener una medición más precisa
        
        // Obtener el uso de memoria antes de la encriptación
        long beforeEncryptionMemory = runtime.totalMemory() - runtime.freeMemory();

        // Iniciar medición de CPU
        ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
        long startCpuTime = threadBean.getCurrentThreadCpuTime();
        long startTime = System.nanoTime();

        long startEncryptTime = System.nanoTime();
        String encryptedText = aesInterface.encrypt(functionType, operationMode, transmissionSize, inputText, key, iv);

        long endEncryptTime = System.nanoTime();
        encryptionTime = endEncryptTime - startEncryptTime;

        // Finalizar medición de CPU
        long endTime = System.nanoTime();
        long endCpuTime = threadBean.getCurrentThreadCpuTime();
        long encryptionCPUTime = endTime - startTime;
        long cpuTime = endCpuTime - startCpuTime;


        runtime.gc(); // Invocar GC nuevamente después de la operación para limpieza
        // Obtener el uso de memoria después de la encriptación
        long afterEncryptionMemory = runtime.totalMemory() - runtime.freeMemory();

        // Calcular el uso de memoria durante la encriptación en bytes (B)
        long encryptionMemoryUsage = afterEncryptionMemory - beforeEncryptionMemory;
        if (encryptionMemoryUsage < 0) encryptionMemoryUsage = 0; // Ajustar valores negativos a cero


        // Calcular el throughput en bytes por segundo (Bps)
        int inputSize = inputText.length();     //cada carácter en una cadena generalmente representa un byte (asumiendo codificación ASCII)
        double throughputBps = (double) inputSize / (encryptionTime / 1_000_000_000.0);

        // Calcular el throughput en bits por segundo (bps)
        double throughputbps = throughputBps * 8;

        // Calcular el porcentaje de uso de CPU
        double cpuUsagePercent = (double) cpuTime / encryptionCPUTime * 100;

        // Guardar los resultados en un archivo de texto
        saveResults("encrypt", inputSize, encryptionTime / 1_000_000, throughputBps, throughputbps);

        // Guardar uso de memoria en archivo
        saveMemoryResults("encrypt", inputSize, encryptionMemoryUsage);

        // Guardar los resultados del uso de CPU
        saveCPU("encrypt", inputSize, encryptionTime / 1_000_000, throughputBps, throughputbps, cpuUsagePercent);

        mytext = inputTextArea.getText();
    
        originalTextArea.append(mytext);
        outputTextArea.append(encryptedText + "\n");

        //JTextArea encryptedTextArea = new JTextArea(encryptedText);
        //encryptedTextArea.setEditable(false);
        //JScrollPane scrollPane = new JScrollPane(encryptedTextArea);
        //outputPanel.removeAll(); // Limpiar el panel antes de agregar el nuevo componente
        //encryptedPanel.add(scrollPane, BorderLayout.CENTER);
        //encryptedPanel.revalidate(); // Actualizar la disposición del panel


        
        // Mostrar el resultado en un cuadro de diálogo
        //JTextArea textArea = new JTextArea(encryptedText);
        //textArea.setEditable(false); // Para que el texto no sea editable
        //JScrollPane scrollPane1 = new JScrollPane(textArea); // Envolver el JTextArea en un JScrollPane

        //JOptionPane.showMessageDialog(this, scrollPane1, "Encrypted Text", JOptionPane.INFORMATION_MESSAGE);

        
        // Mostrar las iteraciones de cifrado en un cuadro de diálogo
        //JDialog dialog = new JDialog(this, "Iterations", true);

        // Crear JScrollPane y agregar el JTextArea
        scrollStep = new JScrollPane(stepTextArea);

        // Establecer la política de la barra de desplazamiento vertical
        scrollStep.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Agregar el JScrollPane al JDialog en lugar del JTextArea directamente
        //dialog.add(scrollPane2);

        //dialog.pack();
        //dialog.setVisible(true);
        outputPanel.removeAll();
        outputPanel.add(scrollStep);
        outputPanel.revalidate(); // Actualizar la disposición del panel
        outputPanel.repaint();
    }

    class CustomOutputStream extends OutputStream {
        private JTextArea textArea;

        public CustomOutputStream(JTextArea textArea) {
            this.textArea = textArea;
        }

        @Override
        public void write(int b) {
            // Redirigir los bytes a la línea de texto
            textArea.append(String.valueOf((char) b));
            // Hacer que el final de la línea sea visible
            textArea.setCaretPosition(textArea.getDocument().getLength());
        }
    }

    public static void main(String[] args) {  
        AESGUISwing gui = new AESGUISwing();
        gui.setVisible(true);
    }
    
    private void handleDecryptionRequest() {
        int functionType = Integer.parseInt("1");
        String operationMode = (String) modeComboBox.getSelectedItem();
        int transmissionSize = Integer.parseInt("0");
        String inputText = inputTextArea.getText();
        String key = keyTextArea.getText();
        String iv = ivTextArea.getText();

        // Crear un área de texto para mostrar las iteraciones de cifrado
        stepTextArea1 = new JTextArea();

        // Redirigir la salida estándar al área de texto
        PrintStream printStream = new PrintStream(new CustomOutputStream(stepTextArea1));
        System.setOut(printStream);
    
        // Crear una instancia de la clase Interface y realizar el descifrado
        Interface aesInterface = new Interface();


        // Medir memoria antes del descifrado
        Runtime runtime = Runtime.getRuntime();
        runtime.gc(); // Invocar GC para obtener una medición más precisa

        // Obtener el uso de memoria antes de la desencriptación
        long beforeDecryptionMemory = runtime.totalMemory() - runtime.freeMemory();


        // Iniciar medición de CPU
        ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
        long startCpuTime = threadBean.getCurrentThreadCpuTime();
        long startTime = System.nanoTime();

        long startDecryptTime = System.nanoTime();
        String decryptedText = aesInterface.decrypt(functionType, operationMode, transmissionSize, inputText, key, iv);
        long endDecryptTime = System.nanoTime();
        decryptionTime = endDecryptTime - startDecryptTime;

        runtime.gc(); // Invocar GC nuevamente después de la operación para limpieza
        // Obtener el uso de memoria después de la desencriptación
        long afterDecryptionMemory = runtime.totalMemory() - runtime.freeMemory();


        // Finalizar medición de CPU
        long endTime = System.nanoTime();
        long endCpuTime = threadBean.getCurrentThreadCpuTime();
        long decryptionCPUTime = endTime - startTime;
        long cpuTime = endCpuTime - startCpuTime;

        // Calcular el uso de memoria durante la desencriptación en bytes (B)
        long decryptionMemoryUsage = afterDecryptionMemory - beforeDecryptionMemory;
        if (decryptionMemoryUsage < 0) decryptionMemoryUsage = 0; // Ajustar valores negativos a cero

        // Calcular el throughput en bytes por segundo (Bps)
        int inputSize = inputText.length() / 2;  //cada 2 caracteres en una cadena generalmente representa un byte (asumiendo codificación ASCII)
        double throughputBps = (double) inputSize / (decryptionTime / 1_000_000_000.0);

        // Calcular el throughput en bits por segundo (bps)
        double throughputbps = throughputBps * 8;

        // Calcular el porcentaje de uso de CPU
        double cpuUsagePercent = (double) cpuTime / decryptionCPUTime * 100;

        // Guardar los resultados en un archivo de texto
        saveResults("decrypt", inputSize, decryptionTime / 1_000_000, throughputBps, throughputbps);
        
        // Guardar uso de memoria en archivo
        saveMemoryResults("decrypt", inputSize, decryptionMemoryUsage);

        // Guardar los resultados del uso de CPU
        saveCPU("decrypt", inputSize, decryptionTime / 1_000_000, throughputBps, throughputbps, cpuUsagePercent);


        //Mostrar texto desencriptado en el cuadro de texto
        outputTextArea1.append(decryptedText + "\n");
        saveTimesToFile();

        // Crear JScrollPane y agregar el JTextArea
        scrollStep1 = new JScrollPane(stepTextArea1);

        // Establecer la política de la barra de desplazamiento vertical
        scrollStep1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Agregar el JScrollPane al JDialog en lugar del JTextArea directamente
        //dialog.add(scrollPane2);

        //dialog.pack();
        //dialog.setVisible(true);
        outputPanel.removeAll();
        outputPanel.add(scrollStep1);
        outputPanel.revalidate(); // Actualizar la disposición del panel
        outputPanel.repaint();


    }
    
    //Save time execution un file
    private void saveTimesToFile() {
        try {
            FileWriter writer = new FileWriter("tiempos_CBC_3.txt", true);

            // Convertir los tiempos a milisegundos
            long encryptionTimeMs = encryptionTime / 1000000;
            long decryptionTimeMs = decryptionTime / 1000000;

            writer.write(encryptionTimeMs + "," + decryptionTimeMs + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Save throughput in file
    private void saveResults(String operation, int inputSize, double executionTime, double throughputBps, double throughputbps) {
        try {
            FileWriter writer = new FileWriter("throughput_CBC_3.txt", true);
            writer.write(operation + "," + inputSize + "," + executionTime + "," + throughputBps + "," + throughputbps + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Sabe Memory file
    private void saveMemoryResults(String operation, int inputSize,  long memoryUsage) {
        try (FileWriter writer = new FileWriter("memory_CBC_3.txt", true)) {
            writer.write(operation + "," + inputSize + "," + memoryUsage + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveCPU(String operation, int inputSize, double executionTime, double throughputBps, double throughputbps, double cpuUsage) {
        try {
            FileWriter writer = new FileWriter("CPU_CBC_3.txt", true);
            writer.write(operation + "," + inputSize + "," + executionTime + "," + throughputBps + "," + throughputbps + "," + cpuUsage + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}