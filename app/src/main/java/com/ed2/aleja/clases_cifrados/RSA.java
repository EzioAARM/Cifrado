package com.ed2.aleja.clases_cifrados;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.util.Random;

public class RSA {

    private int n;
    private int e;
    private int d;
    private int limiteSuperior = 500;
    private Context Contexto;
    private boolean SobreescribirArchivo;
    public String NombreArchivo;
    private String extensionA;

    public RSA(Context contexto, boolean sobreescribir, String nombre) {
        Contexto = contexto;
        NombreArchivo = nombre;
        SobreescribirArchivo = sobreescribir;
    }

    private void generarLlaves() {
        Random r = new Random();
        int p = r.nextInt(limiteSuperior);
        while (!esPrimo(p) && p > 40) {
            p = r.nextInt(limiteSuperior);
            if (p < 0)
                p = p * 1;
            if (p == 0)
                p = r.nextInt();
        }
        int q = r.nextInt(limiteSuperior);
        while (!esPrimo(q) && q > 40) {
            q = r.nextInt(limiteSuperior);
            if (q < 0)
                q = q * 1;
            if (q == 0)
                q = r.nextInt();
            if (q == p)
                q = r.nextInt(limiteSuperior);
        }
        int fi = (p - 1) * (q - 1);
        e = fi / 2;
        for (int i = fi / 2; i < fi; i++) {
            if (!esPrimo(e))
                e++;
            else
                break;
        }
        d = obtenerInverso(fi, e);
        n = p * q;
    }

    public void Cifrar(String texto, String extension) throws Exception {
        generarLlaves();
        int N = 0;
        String textoCifrado = "";
        BigInteger numCif = null;
        BigInteger nMod = new BigInteger(String.valueOf(n));
        BigInteger potencia = new BigInteger("0");
        for (int i = 0; i < texto.length(); i++) {
            N = (int) texto.charAt(i);
            potencia = new BigInteger(String.valueOf(N));
            potencia = potencia.pow(e);
            numCif = new BigInteger(String.valueOf(potencia));
            numCif = numCif.mod(nMod);
            textoCifrado += String.valueOf(numCif.toString()) + " ";
        }
        String nPublic = NombreArchivo + "_public";
        String nPrivate = NombreArchivo + "_private";
        escribirArchivoCifrado(NombreArchivo, NombreArchivo +"|"+ extension +"|"+ textoCifrado);
        escribirArchivoClave(nPublic, String.valueOf(e) + " " + String.valueOf(n));
        escribirArchivoClave(nPrivate, String.valueOf(d) + " " + String.valueOf(n));
    }

    private boolean escribirArchivoClave(String nombreArchivo, String contenido) throws Exception {
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
            archivoEscribir = new File(directorio.getAbsolutePath() + "/" + nombreArchivo + ".key");
            creado = archivoEscribir.createNewFile();
            while (!creado) {
                archivoEscribir = new File(directorio.getAbsolutePath() + "/" + nombreArchivo + "(" + String.valueOf(numeroArchivo) + ")" + ".key");
                creado = archivoEscribir.createNewFile();
            }
        } else {
            archivoEscribir = new File(directorio.getAbsolutePath() + "/" + nombreArchivo + ".key");
            archivoEscribir.delete();
            if (!archivoEscribir.createNewFile())
                throw new Exception("No se pudo crear el archivo " + directorio.getAbsolutePath());
        }
        NombreArchivo = archivoEscribir.getName();
        FileOutputStream fileOutputStream = new FileOutputStream(archivoEscribir);
        fileOutputStream.write(contenido.getBytes());
        fileOutputStream.close();
        return true;
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
            archivoEscribir = new File(directorio.getAbsolutePath() + "/" + nombreArchivo + ".rsacif");
            creado = archivoEscribir.createNewFile();
            while (!creado) {
                archivoEscribir = new File(directorio.getAbsolutePath() + "/" + nombreArchivo + "(" + String.valueOf(numeroArchivo) + ")" + ".rsacif");
                creado = archivoEscribir.createNewFile();
            }
        } else {
            archivoEscribir = new File(directorio.getAbsolutePath() + "/" + nombreArchivo + ".rsacif");
            archivoEscribir.delete();
            if (!archivoEscribir.createNewFile())
                throw new Exception("No se pudo crear el archivo " + directorio.getAbsolutePath());
        }
        NombreArchivo = archivoEscribir.getName();
        FileOutputStream fileOutputStream = new FileOutputStream(archivoEscribir);
        fileOutputStream.write(contenido.getBytes());
        fileOutputStream.close();
        return true;
    }

    public int obtenerInverso(int a,int m)
    {
        int c1 = 1;
        int c2 = -(m/a);
        int t1 = 0;
        int t2 = 1;
        int r = m % a;
        int x=a,y=r,c;
        while(r!=0)
        {
            c = x/y;
            r = x%y;
            c1*=-c;
            c2*=-c;
            c1+=t1;
            c2+=t2;
            t1=-(c1-t1)/c;
            t2=-(c2-t2)/c;
            x=y;
            y=r;
        }
        return t2;
    }

    private boolean esPrimo(int numero){
        boolean primo = true;
        int i = 2;
        while (primo && i != numero) {
            if (numero % i == 0) {
                primo = false;
            }
            i++;
        }
        return primo;
    }

    public void escribirArchivoDescifrado(String nombreArchivo, String contenido) throws Exception {
        File directorio = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            directorio = new File(Environment.getExternalStorageDirectory() + "/DescifradoEstructuras/");
        else
            directorio = new File(Contexto.getFilesDir() + "/DescifradoEstructuras/");
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
            archivoEscribir = new File(directorio.getAbsolutePath() + "/" + nombreArchivo + "." + extensionA);
            creado = archivoEscribir.createNewFile();
            while (!creado) {
                archivoEscribir = new File(directorio.getAbsolutePath() + "/" + nombreArchivo + "(" + String.valueOf(numeroArchivo) + ")" + "." + extensionA);
                creado = archivoEscribir.createNewFile();
                numeroArchivo++;
            }
        } else {
            archivoEscribir = new File(directorio.getAbsolutePath() + "/" + nombreArchivo + "." + extensionA);
            archivoEscribir.delete();
            if (!archivoEscribir.createNewFile())
                throw new Exception("No se pudo crear el archivo " + directorio.getAbsolutePath());
        }
        boolean eliminoExtension = false;
        int i = 0;
        while (!eliminoExtension) {
            if (contenido.charAt(i) == '|') {
                contenido = contenido.substring(i + 1);
                eliminoExtension = true;
            }
            i++;
        }
        FileOutputStream fileOutputStream = new FileOutputStream(archivoEscribir);
        fileOutputStream.write(contenido.getBytes());
        fileOutputStream.close();
    }

    public void descifrar(String textoCifrado, int n, int d){
        String[] aChars = textoCifrado.split("\\s");
        String aux = aChars[0];
        String[] header = aux.split("|");
        aChars[0] = header[2];
        extensionA = header[1];
        NombreArchivo = header[0];
        BigInteger N = new BigInteger(String.valueOf(n));
        BigInteger D = new BigInteger(String.valueOf(d));
        BigInteger res;
        String cadena = "";
        for (int i = 0; i<aChars.length; i++) {
            res = new BigInteger(String.valueOf(Integer.parseInt(aChars[i])));
            res = res.modPow(D,N);
            cadena = cadena+res.toString();
        }
        try {
            escribirArchivoDescifrado(NombreArchivo, cadena);
        }
        catch (Exception e){

        }

    }

}
