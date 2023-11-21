/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src;

import java.math.BigInteger;
import java.util.*;

public class rsa {

    private int tamprimo;
    private BigInteger p, q, n;
    private BigInteger fi;
    private BigInteger e, d;

    public rsa(int tamprimo) {
        this.tamprimo = tamprimo;
    }

    public void generarPrimos() {
        p = new BigInteger(tamprimo, 10, new Random());
        do {
            q = new BigInteger(tamprimo, 10, new Random());
        } while (q.compareTo(p) == 0);
    }

    public void generarClaves() {
        n = p.multiply(q);

        fi = p.subtract(BigInteger.valueOf(1));
        fi = fi.multiply(q.subtract(BigInteger.valueOf(1)));

        do {
            e = new BigInteger(2 * tamprimo, new Random());
        } while ((e.compareTo(fi) != 1) || (e.gcd(fi).compareTo(BigInteger.valueOf(1)) != 0));

        d = e.modInverse(fi);
    }

    public BigInteger[] cifrar(String mensaje) {
        byte[] digitos = mensaje.getBytes();
        BigInteger[] bigdigitos = new BigInteger[digitos.length];

        for (int i = 0; i < bigdigitos.length; i++) {
            bigdigitos[i] = new BigInteger(String.valueOf(digitos[i]));
        }

        BigInteger[] cifrado = new BigInteger[bigdigitos.length];

        for (int i = 0; i < bigdigitos.length; i++) {
            cifrado[i] = bigdigitos[i].modPow(e, n);
        }
        return cifrado;
    }

    public String descifrar(BigInteger[] cifrado) {
        BigInteger[] descifrado = new BigInteger[cifrado.length];

        for (int i = 0; i < descifrado.length; i++) {
            descifrado[i] = cifrado[i].modPow(d, n);
        }

        char[] charArray = new char[descifrado.length];

        for (int i = 0; i < charArray.length; i++) {
            charArray[i] = (char) (descifrado[i].intValue());
        }
        return new String(charArray);
    }

    public BigInteger getN() {
        return n;
    }

    public BigInteger getE() {
        return e;
    }

    /*public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Tamaño de los números primos
        int tamprimo = 1024;

        // Crear una instancia de la clase rsa
        rsa rsa = new rsa(tamprimo);

        // Generar números primos p y q
        rsa.generarPrimos();

        // Generar claves pública y privada
        rsa.generarClaves();

        System.out.println("Ingrese el mensaje a cifrar:");
        String mensaje = scanner.nextLine();

        // Cifrar el mensaje
        BigInteger[] cifrado = rsa.cifrar(mensaje);
        System.out.println("Mensaje cifrado: " + Arrays.toString(cifrado));

        // Descifrar el mensaje
        String mensajeDescifrado = rsa.descifrar(cifrado);
        System.out.println("Mensaje descifrado: " + mensajeDescifrado);

        scanner.close();
    }*/
}
