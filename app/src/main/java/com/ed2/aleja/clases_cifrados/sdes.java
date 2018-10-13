package com.ed2.aleja.clases_cifrados;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

public class sdes {
    private int[] VectorDePermutacion10 = {4, 9, 3, 2, 6, 5, 7, 1, 8, 0};
    private int[] SeleccionarPermutar = {9, 7, 5, 3, 1, 4, 2, 6};
    private int[] VectorDePermutacion8 = {3, 5, 7, 0, 4, 2, 6, 1};
    private int[] VectorPermutar = {2, 0, 1, 3};
    private int[] VectorDePermutacion4 = {0, 1, 3, 2};
    private int[] VectorDePermutacion4P2 = {3, 2, 0, 1};
    private String[][] sBox1 = {{"01", "00", "11", "10"}, {"11", "10", "01", "00"}, {"00", "10", "01", "11"}, {"11", "01", "11", "01"}};
    private String[][] sBox2 = {{"00", "01", "10", "11"}, {"10", "00", "01", "11"}, {"11", "00", "01", "00"}, {"10", "01", "00", "11"}};
    private String TextoParaCifrar, Key, TextoCifrado;
    private Context Contexto;
    private boolean SobreescribirArchivo;
    private String NombreArchivo;

    public sdes(Context contexto, boolean sobreescribir, String nombre) {
        Contexto = contexto;
        SobreescribirArchivo = sobreescribir;
        NombreArchivo = nombre;
    }

    public void Cifrar(String textCifrar, String key) throws Exception {
        Key = key;
        TextoParaCifrar = textCifrar;
        /* Creación de las llaves */

        String llaveInicial = Integer.toBinaryString(Integer.parseInt(Key));
        if (llaveInicial.length() < 10) {
            for (int i = 0; i < 10 - llaveInicial.length(); i++) {
                llaveInicial = "0" + llaveInicial;
            }
        }
        boolean[] llave = convertirString(llaveInicial);
        llave = Permutacion(llave);
        boolean[] llave1 = {llave[0], llave[1], llave[2], llave[3], llave[4]};
        boolean[] llave2 = {llave[5], llave[6], llave[7], llave[8], llave[9]};

        llave1 = leftShift(llave1, 1);
        llave2 = leftShift(llave2, 1);
        llave = ConcatenarArreglos(llave1, llave2);
        boolean[] llaveFinal1 = SeleccionarYPermutar(llave);

        llave1 = leftShift(llave1, 2);
        llave2 = leftShift(llave2, 2);
        llave = ConcatenarArreglos(llave1, llave2);
        boolean[] llaveFinal2 = SeleccionarYPermutar(llave);


        /* Fin de creación de las llaves */
         /* Inicio del cifrado*/

        int longitud = TextoParaCifrar.length();
        boolean[] arregloByteActual = null;
        boolean[] arregloActualP1 = new boolean[4];
        boolean[] arregloActualP2 = new boolean[4];
        String actualBinario = "";
        String binarioCifrado = "";
        TextoCifrado = "";
        for (int i = 0; i < longitud; i++) {
            actualBinario = Integer.toBinaryString((int) TextoParaCifrar.charAt(i));
            switch (actualBinario.length()) {
                case 8:
                    break;
                default:
                    int cont = 8 - actualBinario.length();
                    for (int contadorCeros = 0; contadorCeros < cont; contadorCeros++) {
                        actualBinario = "0" + actualBinario;
                    }
                    break;
            }
            arregloByteActual = convertirString(actualBinario);

            arregloActualP1[0] = arregloByteActual[0]; arregloActualP1[1] = arregloByteActual[1]; arregloActualP1[2] = arregloByteActual[2]; arregloActualP1[3] = arregloByteActual[3];
            binarioCifrado = Fk(arregloActualP1, llaveFinal1);
            arregloActualP2[0] = arregloByteActual[4]; arregloActualP2[1] = arregloByteActual[5]; arregloActualP2[2] = arregloByteActual[6]; arregloActualP2[3] = arregloByteActual[7];
            binarioCifrado += Fk(arregloActualP2, llaveFinal2);
            TextoCifrado += String.valueOf((char) Integer.parseInt(binarioCifrado, 2));
        }
        escribirArchivoCifrado(NombreArchivo, TextoCifrado);
        /* Fin del cifrado */
    }

    private String Fk(boolean[] arreglo, boolean[] llave) {
        boolean[] arregloParaSbox1 = new boolean[4];
        boolean[] arregloParaSbox2 = new boolean[4];
        boolean[] arregloDespuesSbox1;
        boolean[] arregloDespuesSbox2;
        arreglo = ExpandirPermutar(arreglo);
        arreglo = XOR(arreglo, llave);
        arregloParaSbox1[0] = arreglo[0]; arregloParaSbox1[1] = arreglo[1]; arregloParaSbox1[2] = arreglo[2]; arregloParaSbox1[3] = arreglo[3];
        arregloParaSbox2[0] = arreglo[4]; arregloParaSbox2[1] = arreglo[5]; arregloParaSbox2[2] = arreglo[6]; arregloParaSbox2[3] = arreglo[7];
        arregloDespuesSbox1 = convertirString(buscarEnSbox(arregloParaSbox1, sBox1));
        arregloDespuesSbox2 = convertirString(buscarEnSbox(arregloParaSbox2, sBox2));
        return convertirBoolean(ConcatenarArreglos(arregloDespuesSbox1, arregloDespuesSbox2));
    }

    private boolean[] XOR(boolean[] arreglo1, boolean[] arreglo2) {
        boolean[] arregloNuevo = new boolean[arreglo1.length];
        for (int i = 0; i < arregloNuevo.length; i++) {
            arregloNuevo[i] = arreglo1[i] ^ arreglo2[i];
        }
        return arregloNuevo;
    }

    private boolean[] PermutacionInicial(boolean[] byteViejo){
        boolean[] byteNuevo = new boolean[byteViejo.length];
        for (int i = 0; i < byteNuevo.length; i++) {
            byteNuevo[i] = byteViejo[VectorDePermutacion8[i]];
        }
        return byteNuevo;
    }

    private boolean[] PermutacionInversa(boolean[] byteViejo) {
        boolean[] byteNuevo = new boolean[byteViejo.length];
        for (int i = 0; i < byteNuevo.length; i++) {
            byteNuevo[i] = byteViejo[VectorDePermutacion8[i]];
        }
        return byteNuevo;
    }

    private boolean[] ExpandirPermutar(boolean[] arreglo) {
        boolean[] arregloNuevo = new boolean[8];
        for (int i = 0; i < arreglo.length; i++) {
            arregloNuevo[i] = arreglo[VectorDePermutacion4[i]];
        }
        for (int i = 4; i < arregloNuevo.length; i++) {
            arregloNuevo[i] = arreglo[VectorDePermutacion4P2[i - 4]];
        }
        return arregloNuevo;
    }

    private boolean[] Permutar(boolean[] arreglo) {
        boolean[] arregloNuevo = new boolean[4];
        for (int i = 0; i < arreglo.length; i++) {
            arregloNuevo[i] = arreglo[VectorPermutar[i]];
        }
        return arregloNuevo;
    }

    private boolean[] SeleccionarYPermutar(boolean[] arreglo){
        boolean[] arregloNuevo = new boolean[8];
        for (int i = 0; i < arregloNuevo.length; i++) {
            arregloNuevo[i] = arreglo[SeleccionarPermutar[i]];
        }
        return arregloNuevo;
    }

    private boolean[] Permutacion(boolean[] arreglo) {
        boolean[] byteNuevo = new boolean[arreglo.length];
        for (int i = 0; i < byteNuevo.length; i++) {
            byteNuevo[i] = arreglo[VectorDePermutacion10[i]];
        }
        return byteNuevo;
    }

    private boolean[] leftShift(boolean[] arreglo, int cantidad) {
        boolean temp;
        for (int i = 0; i < cantidad; i++) {
            temp = arreglo[i];
            for (int j = 1; j < arreglo.length; j++) {
                arreglo[j - 1] = arreglo[j];
                if (j == arreglo.length - 1)
                    arreglo[j] = temp;
            }
        }
        return arreglo;
    }

    private String buscarEnSbox(boolean[] arreglo, String[][] sbox) {
        boolean[] n1 = {arreglo[0], arreglo[3]};
        int fila = Integer.parseInt(convertirBoolean(n1), 2);
        boolean[] n2 = {arreglo[1], arreglo[2]};
        int columna = Integer.parseInt(convertirBoolean(n2), 2);
        return sbox[fila][columna];
    }

    private boolean[] convertirString(String binario) {
        boolean[] arreglo = new boolean[binario.length()];
        for (int i = 0; i < binario.length(); i++) {
            arreglo[i] = String.valueOf(binario.charAt(i)).equals("1") ? true : false;
        }
        return arreglo;
    }

    private String convertirBoolean(boolean[] arreglo) {
        String binario = "";
        for (int i = 0; i < arreglo.length; i++) {
            binario += arreglo[i] ? "1" : "0";
        }
        return binario;
    }

    private boolean[] ConcatenarArreglos(boolean[] arreglo1, boolean[] arreglo2) {
        boolean[] arregloNuevo = new boolean[arreglo1.length + arreglo2.length];
        int i = 0;
        for (i = 0; i < arreglo1.length; i++) {
            arregloNuevo[i] = arreglo1[i];
        }
        for (int j = i; j < arregloNuevo.length; j++) {
            arregloNuevo[j] = arreglo2[j - arreglo1.length];
        }
        return arregloNuevo;
    }

    private boolean escribirArchivoCifrado(String nombreArchivo, String contenido) throws Exception {
        File directorio = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            directorio = new File(Environment.getExternalStorageDirectory() + "/CifradoEstructuras/");
        else
            directorio = new File(Contexto.getFilesDir() + "/CifradoEstructuras/");
        boolean dirCre = true;
        if (!directorio.exists())
            dirCre = directorio.mkdirs();
        if (!dirCre) {
            throw new Exception("No se pudo crear la ruta " + directorio.getAbsolutePath());
        }
        File archivoEscribir = null;
        if (!SobreescribirArchivo) {
            boolean creado = false;
            int numeroArchivo = 1;
            archivoEscribir = new File(directorio.getAbsolutePath() + "/" + nombreArchivo + ".scif");
            creado = archivoEscribir.createNewFile();
            while (!creado) {
                archivoEscribir = new File(directorio.getAbsolutePath() + "/" + nombreArchivo + "(" + String.valueOf(numeroArchivo) + ")" + ".scif");
                creado = archivoEscribir.createNewFile();
            }
        } else {
            archivoEscribir = new File(directorio.getAbsolutePath() + "/" + nombreArchivo + ".scif");
            archivoEscribir.delete();
            if (!archivoEscribir.createNewFile())
                throw new Exception("No se pudo crear el archivo " + directorio.getAbsolutePath());
        }
        FileOutputStream fileOutputStream = new FileOutputStream(archivoEscribir);
        fileOutputStream.write(contenido.getBytes());
        fileOutputStream.close();
        return true;
    }
}
