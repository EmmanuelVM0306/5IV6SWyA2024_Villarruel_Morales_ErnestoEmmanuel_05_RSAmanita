/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;
import java.util.Arrays;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;

public class Main {
    private JFrame frame;
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JLabel label4;
    private JTextField inputField;
    private JTextField inputFieldP;
    private JButton encryptButton;
    private JButton decryptButton;
    private JButton btnPrimos;
    private JButton btnCopiar;
    private JTextArea outputArea1;
    private JTextArea outputArea2;
    private rsa rsa;
    private int tamprimo;

    public Main() {

        tamprimo = 1024;
        rsa = new rsa(tamprimo);
        rsa.generarPrimos();
        rsa.generarClaves();
        initializeUI();
    }
    

    private void initializeUI() {
        frame = new JFrame("Cifrado RSA");
        frame.setBounds(100, 100, 600, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        
        label1 = new JLabel();
        label1.setBounds(10, 30, 500, 30);
        label1.setText("Ingrese el texto que desee cifrar:");
        frame.add(label1);
        
        label2 = new JLabel();
        label2.setBounds(10, 136, 500, 30);
        label2.setText("Texto cifrado:");
        frame.add(label2);
        
        label3 = new JLabel();
        label3.setBounds(300, 136, 500, 30);
        label3.setText("Texto descifrado:");
        frame.add(label3);
        
        label4 = new JLabel();
        label4.setBounds(10, 10, 500, 30);
        label4.setText("Ingrese el tamaño de los primos:");
        frame.add(label4);
        
        inputFieldP = new JTextField();
        inputFieldP.setBounds(210, 10, 100, 25);
        frame.getContentPane().add(inputFieldP);
        inputFieldP.setColumns(10);
        
        btnPrimos = new JButton("Establecer tamaño");
        btnPrimos.setBounds(320, 10, 150, 23);
        frame.getContentPane().add(btnPrimos);
        
        btnCopiar = new JButton("Copiar cifrado al portapapeles");
        btnCopiar.setBounds(10, 400, 220, 23);
        frame.getContentPane().add(btnCopiar);
        
        inputField = new JTextField();
        inputField.setBounds(10, 61, 500, 30);
        frame.getContentPane().add(inputField);
        inputField.setColumns(10);

        encryptButton = new JButton("Cifrar");
        encryptButton.setBounds(10, 102, 89, 23);
        frame.getContentPane().add(encryptButton);

        decryptButton = new JButton("Descifrar");
        decryptButton.setBounds(109, 102, 89, 23);
        frame.getContentPane().add(decryptButton);

        outputArea1 = new JTextArea();
        outputArea1.setBounds(10, 176, 270, 210);
        outputArea1.setEditable(false);
        frame.getContentPane().add(outputArea1);
        
        outputArea2 = new JTextArea();
        outputArea2.setBounds(300, 176, 270, 210);
        outputArea2.setEditable(false);
        frame.getContentPane().add(outputArea2);
        
        encryptButton.setEnabled(false);
        decryptButton.setEnabled(false);
        btnCopiar.setEnabled(false);
        inputField.setEditable(false);

        btnPrimos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String tamaño = inputFieldP.getText();

                if (tamaño.matches("\\d+")) {
                    if(tamaño.length()>=3){
                        int tamañoNumero = Integer.parseInt(tamaño);
                        tamprimo = tamañoNumero;
                        JOptionPane.showMessageDialog(null, "El valor de tamprimo es: "+ tamprimo, "Info", JOptionPane.INFORMATION_MESSAGE);
                        encryptButton.setEnabled(true);
                        decryptButton.setEnabled(true);
                        btnPrimos.setEnabled(false);
                        inputField.setEditable(true);
                        inputFieldP.setEditable(false);
                        btnCopiar.setEnabled(true);
                    }else{
                        JOptionPane.showMessageDialog(null, "Ingrese un número de 3 dígitos o más", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {

                    JOptionPane.showMessageDialog(null, "Ingrese un número entero", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        
        encryptButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String mensaje = inputField.getText();
                if(!mensaje.trim().isEmpty()){
                    BigInteger[] cifrado = rsa.cifrar(mensaje);
                    outputArea1.setText(Arrays.toString(cifrado).replace("[", "").replace("]", ""));
                }else{
                    JOptionPane.showMessageDialog(null, "Ingrese el texto que desee cifrar", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
                
            }
        });

        decryptButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String ciphertext = inputField.getText();
                if (!ciphertext.trim().isEmpty()) {
                    String[] ciphertextArray = ciphertext.split(", ");
                    BigInteger[] cifrado = new BigInteger[ciphertextArray.length];
            
                    // Validar si cada elemento ingresado es un BigInteger
                    boolean isValidBigInteger = true;
                    for (int i = 0; i < ciphertextArray.length; i++) {
                        try {
                            cifrado[i] = new BigInteger(ciphertextArray[i].trim());
                        } catch (NumberFormatException ex) {
                            // Si hay un error al convertir, no es un BigInteger válido
                            isValidBigInteger = false;
                            break;
                        }
                    }
            
                    if (isValidBigInteger) {
                        // El texto ingresado es un BigInteger
                        String mensajeDescifrado = rsa.descifrar(cifrado);
                        outputArea2.setText(mensajeDescifrado);
                    } else {
                        JOptionPane.showMessageDialog(null, "Ingrese valores válidos de BigInteger", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Ingrese el texto que desee descifrar", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        
        btnCopiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String texto = outputArea1.getText();
                if (!texto.isEmpty()) {
                    StringSelection stringSelection = new StringSelection(texto);
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    clipboard.setContents(stringSelection, null);
                    JOptionPane.showMessageDialog(null, "Texto copiado al portapapeles", "Copiado", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "El área de texto está vacía", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            }
        });


        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    new Main();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
