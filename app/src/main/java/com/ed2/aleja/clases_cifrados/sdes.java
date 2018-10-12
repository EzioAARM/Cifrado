package com.ed2.aleja.clases_cifrados;

import android.content.Context;

public class sdes {
    private int[] VectorDePermutacion10 = {4, 9, 3, 2, 6, 5, 7, 1, 8, 0};
    private int[] SeleccionarPermutar = {9, 7, 5, 3, 1, 4, 2, 6};
    private int[] VectorDePermutacion8 = {3, 5, 7, 0, 4, 2, 6, 1};
    private int[] VectorPermutar = {2, 4, 1, 2};
    private int[] VectorDePermutacion4 = {4, 1, 3, 2};
    private int[] VectorDePermutacion4P2 = {3, 2, 4, 1};
    private String[][] sBox1 = {{"01", "00", "11", "10"}, {"11", "10", "01", "00"}, {"00", "10", "01", "11"}, {"11", "01", "11", "01"}};
    private String[][] sBox2 = {{"00", "01", "10", "11"}, {"10", "00", "01", "11"}, {"11", "00", "01", "00"}, {"10", "01", "00", "11"}};
    private String TextoParaCifrar, Key, Key1, Key2, TextoCifrado;
    private Context Contexto;
    private boolean SobreescribirArchivo;
    private String NombreArchivo;

    public sdes(String textCifrar, String key, Context contexto, boolean sobreescribir, String nombre) {
        Contexto = contexto;
        SobreescribirArchivo = sobreescribir;
        Key = key;
        TextoParaCifrar = textCifrar;
        NombreArchivo = nombre;
    }

    public void Cifrar() {

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
        boolean[] byteNuevo = new boolean[8];
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
        int fila = Integer.parseInt(convertirBoolean(n1));
        boolean[] n2 = {arreglo[1], arreglo[2]};
        int columna = Integer.parseInt(convertirBoolean(n2));
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
}
