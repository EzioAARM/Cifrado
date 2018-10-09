package com.ed2.aleja.cifrado;

import android.content.Context;
import android.os.Environment;

import java.io.Console;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;


public class zigZag {


    int tOla;
    int cresta;
    int cola;
    int nOlas;
    int tBloque;
    int nBloques;
    int salto;
    int lvls;
    String TextArchivo;
    Context Context;
    public String NombreArchivoNuevo = "";
    public String NombreOriginalArchivo = "";
    public String outPut = "";
    public String ubicacionArchivo = "";
    public ArrayList<Character> charList;

    public zigZag(String text, Context contextApp, int levels){
        Context = contextApp;
        TextArchivo = text;
        lvls = levels;
        tOla = (levels-1)*2;

    }

    public void cifrar (){
        String cifrado = "";
        int salto;
        char[] noCifrado = TextArchivo.toCharArray();
        nOlas = noCifrado.length%tOla;
        for (int j = 0; j < noCifrado.length; j++){
            charList.add(noCifrado[j]);
        }
        int extras = lvls-(noCifrado.length%lvls);
        int indice = tOla;
        for (int i = 0; i < extras; i++){
            charList.add('|');
        }
        for (int z = 0; z < lvls; z++){
            salto = tOla-(2*z);
            if(z == 0 || z == lvls -1){
                for (int x = 0; x < nOlas; x++){
                    cifrado = cifrado + noCifrado[z + (x*salto)];
                }
            }
            else {
                for (int s = 0; s < nOlas*2; s++){
                    cifrado = cifrado + noCifrado[z+(s*salto)];
                }
            }
        }
        try {
            escribirArchivoCifrado(NombreOriginalArchivo + ".cif", cifrado);
        }
        catch (Exception e) {

        }
    }

    public void desCifrar(String cifrado, int levels){
        char [] aCifrado = cifrado.toCharArray();
        lvls = levels;
        tOla = (levels-1)*2;
        nOlas = nOlas = aCifrado.length%tOla;
        tBloque = nOlas * 2;
        cresta = cola = nOlas;
        for (int i = 0; i < aCifrado.length; i++){

        }
    }

    private boolean escribirArchivoCifrado(String nombreArchivo, String contenido) throws Exception {
        File directorio = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            directorio = new File(Environment.getExternalStorageDirectory() + "/CifradoEstructuras/");
        else
            directorio = new File(Context.getFilesDir() + "/CifradoEstructuras/");
        boolean dirCre = true;
        if (!directorio.exists())
            dirCre = directorio.mkdirs();
        if (!dirCre) {
            throw new Exception("No se pudo crear la ruta " + directorio.getAbsolutePath());
        }
        File archivoEscribir = new File(directorio.getAbsolutePath() + "/" + nombreArchivo + ".huff");
        if (!archivoEscribir.createNewFile())
            throw new Exception("No se pudo crear el archivo " + directorio.getAbsolutePath());
        FileOutputStream fileOutputStream = new FileOutputStream(archivoEscribir);
        fileOutputStream.write(contenido.getBytes());
        fileOutputStream.close();
        ubicacionArchivo = archivoEscribir.getAbsolutePath();
        return true;
    }


    public void escribirArchivoDescifrado(String nombreArchivo, String contenido) throws Exception {
        File directorio = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            directorio = new File(Environment.getExternalStorageDirectory() + "/DescifradoEstructuras/");
        else
            directorio = new File(Context.getFilesDir() + "/DescifradoEstructuras/");
        boolean dirCre = true;
        if (!directorio.exists())
            dirCre = directorio.mkdirs();
        if (!dirCre) {
            throw new Exception("No se pudo crear la ruta " + directorio.getAbsolutePath());
        }
        File archivoEscribir = new File(directorio.getAbsolutePath() + "/" + nombreArchivo);
        if (archivoEscribir.exists()) {
            throw new Exception("El archivo " + nombreArchivo + " ya existe");
        } else {
            if (!archivoEscribir.createNewFile())
                throw new Exception("No se pudo crear el archivo " + archivoEscribir.getAbsolutePath());
            FileOutputStream fileOutputStream = new FileOutputStream(archivoEscribir);
            fileOutputStream.write(contenido.getBytes());
            fileOutputStream.close();
        }
    }

    public void setNombreOriginalArchivo(String nombre) {
        NombreOriginalArchivo = nombre;
    }

    public String getNombreOriginalArchivo() {
        return NombreOriginalArchivo;
    }

    public void setNombreArchivoNuevo (String nombre){
        NombreArchivoNuevo = nombre;
    }

    public String getNombreArchivoNuevo () {
        return NombreArchivoNuevo;
    }

}
