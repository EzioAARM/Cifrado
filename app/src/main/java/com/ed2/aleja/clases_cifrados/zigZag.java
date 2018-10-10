package com.ed2.aleja.clases_cifrados;

import android.content.Context;
import android.os.Environment;

import java.io.Console;
import java.io.File;
import java.io.FileOutputStream;
import java.security.cert.Extension;
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
    String extensionA = "";
    Context Context;
    public String NombreArchivoNuevo = "";
    public String NombreOriginalArchivo = "";
    public String outPut = "";
    public String ubicacionArchivo = "";
    public ArrayList<Character> charList;
    private boolean SobreescribirArchivo;

    public zigZag(String text, Context contextApp, int levels, boolean sobreescribir, String nombreArchivo){
        setNombreArchivoNuevo(nombreArchivo);
        Context = contextApp;
        TextArchivo = text;
        lvls = levels;
        tOla = (levels-1)*2;
        SobreescribirArchivo = sobreescribir;
    }

    public void cifrar (){
        String cifrado = "";
        int salto;
        char[] noCifrado = TextArchivo.toCharArray();
        charList = new ArrayList<>();
        for (int j = 0; j < noCifrado.length; j++){
            charList.add(noCifrado[j]);
        }
        int extras = lvls-(noCifrado.length%lvls);
        int indice = tOla;
        for (int i = 0; i < extras; i++){
            charList.add('|');
        }
        nOlas = charList.size()/tOla;
        for (int z = 0; z < lvls; z++){
            salto = tOla-(2*z);
            if(z == 0 || z == lvls -1){
                for (int x = 0; x < nOlas; x++){
                    salto = tOla;
                    cifrado = cifrado + charList.get(z + (x*salto));
                }
            }
            else {
                int var = charList.size()%tOla;
                for (int s = 0; s < nOlas; s++){
                    cifrado = cifrado +charList.get((s*tOla)+z) +charList.get(z+(s*tOla)+salto);
                }
            }
        }
        int var = charList.size()%tOla;
        try {
            escribirArchivoCifrado(NombreArchivoNuevo, cifrado);
        }
        catch (Exception e) {

        }
    }

    public void desCifrar(String cifrado, int levels){
        char [] aCifrado = cifrado.toCharArray();
        String outPut = "";
        lvls = levels;
        tOla = (levels-1)*2;
        nOlas = aCifrado.length/tOla;
        tBloque = nOlas * 2;
        cresta = cola = nOlas;
        nBloques = (aCifrado.length - cresta - cola)/tBloque;
        char [] aCresta = new char[cresta];
        char [] aCola = new char[cola];
        int indice = 0;
        ArrayList<Character> aBloque = new ArrayList<>();
        for (int i = 0; i < aCifrado.length; i++){
            if (i < cresta){
                aCresta[i] = aCifrado[i];
            }
            else if(i >= cresta && i < (aCifrado.length-cola)){
                aBloque.add(aCifrado[i]);
            }
            else {
                aCola[indice] = aCifrado[i];
                indice++;
            }
        }
        for (int j = 0; j < cresta; j++) {
            outPut = outPut + aCresta[j];
            for (int z = 0; z < nBloques; z++){
                outPut = outPut + aBloque.get((z*tBloque)+(j*2));
            }
            outPut = outPut + aCola[j];
            for (int z = nBloques -1; z >= 0; z--){
                outPut = outPut + aBloque.get((z*tBloque)+(j*2)+1);
            }
        }
        aCresta = outPut.toCharArray();
        int indice2 = 0;
        while (aCresta[indice2] != '|'){
            extensionA = extensionA + aCresta[indice2];
            indice2++;
        }
        try {
            escribirArchivoDescifrado(NombreArchivoNuevo, outPut);
        }
        catch (Exception e){

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
        File archivoEscribir = null;
        if (!SobreescribirArchivo) {
            boolean creado = false;
            int numeroArchivo = 1;
            archivoEscribir = new File(directorio.getAbsolutePath() + "/" + nombreArchivo + ".cif");
            creado = archivoEscribir.createNewFile();
            while (!creado) {
                archivoEscribir = new File(directorio.getAbsolutePath() + "/" + nombreArchivo + "(" + String.valueOf(numeroArchivo) + ")" + ".cif");
                creado = archivoEscribir.createNewFile();
            }
        } else {
            archivoEscribir = new File(directorio.getAbsolutePath() + "/" + nombreArchivo + ".cif");
            archivoEscribir.delete();
            if (!archivoEscribir.createNewFile())
                throw new Exception("No se pudo crear el archivo " + directorio.getAbsolutePath());
        }
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
