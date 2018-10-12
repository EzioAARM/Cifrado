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

    public sdes(Context contexto, boolean sobreescribir, String nombre) {
        Contexto = contexto;
        SobreescribirArchivo = sobreescribir;
        NombreArchivo = nombre;
    }

    public void Cifrar(String textCifrar, String key) {
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

        /* Fin del cifrado */
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

    private boolean[] ConcatenarArreglos(boolean[] arreglo1, boolean[] arreglo2) {
        boolean[] arregloNuevo = new boolean[arreglo1.length + arreglo2.length];
        int i = 0;
        for (i = 0; i < arreglo1.length; i++) {
            arregloNuevo[i] = arreglo1[i];
        }
        for (i = i; i < arreglo2.length; i++) {
            arregloNuevo[i] = arreglo2[i - arreglo1.length - 1];
        }
        return arregloNuevo;
    }
}
