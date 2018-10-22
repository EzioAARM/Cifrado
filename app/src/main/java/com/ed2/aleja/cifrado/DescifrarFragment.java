package com.ed2.aleja.cifrado;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DescifrarFragment extends Fragment {

    private static String UBICACION_ARCHIVO_DESCIFRAR = " ";
    private static final int READ_REQUEST_CODE_FILE = 1;
    View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.descifrar_fragment, container, false);
        Button examinarArchivoDescifrar = (Button) rootView.findViewById(R.id.examinar_descifrar_descifrar);
        examinarArchivoDescifrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent()
                        .addCategory(Intent.CATEGORY_OPENABLE)
                        .setType("*/*")
                        .setAction(Intent.ACTION_OPEN_DOCUMENT);
                startActivityForResult(intent, READ_REQUEST_CODE_FILE);
            }
        });
        return rootView;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (requestCode == READ_REQUEST_CODE_FILE && resultCode == Activity.RESULT_OK) {
            try {
                EditText ubicacionArchivoCifrar = (EditText) rootView.findViewById(R.id.ubicacion_archivo_descifrar);
                UBICACION_ARCHIVO_DESCIFRAR = obtenerNombreArchivo(resultData.getData());
                ubicacionArchivoCifrar.setText(UBICACION_ARCHIVO_DESCIFRAR);
                String extension = UBICACION_ARCHIVO_DESCIFRAR.substring(UBICACION_ARCHIVO_DESCIFRAR.lastIndexOf('.'));
                InputStream inputStream = rootView.getContext().getContentResolver().openInputStream(resultData.getData());
                BufferedReader lector = new BufferedReader(new InputStreamReader(inputStream));
                String textoArchivo = "";
                int caracter;
                while ((caracter = lector.read()) != -1) {
                    textoArchivo += (char) caracter;
                }
                FragmentManager fragmentManager;
                Bundle argumentos;
                switch (extension) {
                    case ".cif":
                        fragmentManager = getActivity().getSupportFragmentManager();
                        ZigZagDescifrarFragment zigZagDescifrarFragment = new ZigZagDescifrarFragment();
                        argumentos = new Bundle();
                        argumentos.putSerializable("textoDescifrar", textoArchivo);
                        argumentos.putSerializable("nombreArchivo", UBICACION_ARCHIVO_DESCIFRAR);
                        zigZagDescifrarFragment.setArguments(argumentos);
                        fragmentManager.beginTransaction().replace(R.id.contenedor_descifrados, zigZagDescifrarFragment).commit();
                        break;
                    case ".scif":
                        fragmentManager = getActivity().getSupportFragmentManager();
                        SdesDescifrarFragment sdesDescifrarFragment = new SdesDescifrarFragment();
                        argumentos = new Bundle();
                        argumentos.putSerializable("textoDescifrar", textoArchivo);
                        argumentos.putSerializable("nombreArchivo", UBICACION_ARCHIVO_DESCIFRAR);
                        sdesDescifrarFragment.setArguments(argumentos);
                        fragmentManager.beginTransaction().replace(R.id.contenedor_descifrados, sdesDescifrarFragment).commit();
                        break;
                    case ".rsacif":
                        fragmentManager = getActivity().getSupportFragmentManager();
                        RSADescifrarFragment rsaDescifrarFragment = new RSADescifrarFragment();
                        argumentos = new Bundle();
                        argumentos.putSerializable("textoDescifrar", textoArchivo);
                        argumentos.putSerializable("nombreArchivo", UBICACION_ARCHIVO_DESCIFRAR);
                        rsaDescifrarFragment.setArguments(argumentos);
                        fragmentManager.beginTransaction().replace(R.id.contenedor_descifrados, rsaDescifrarFragment).commit();
                        break;
                    default:
                        Toast.makeText(rootView.getContext(), "Debe seleccionar un archivo de extensión .cif", Toast.LENGTH_LONG).show();
                        break;
                }
            } catch (FileNotFoundException e) {
                Toast.makeText(rootView.getContext(), "No se encontró el archivo", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } catch (IOException e) {
                Toast.makeText(rootView.getContext(), "Hubo un error leyendo el archivo", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }else {
            Toast.makeText(rootView.getContext(), "No se seleccionón ningún archivo", Toast.LENGTH_SHORT).show();
        }
    }

    public String obtenerNombreArchivo(Uri uri) {
        String nombre = "";
        File archivo = new File(uri.toString());
        Cursor cursor = null;
        if (uri.toString().startsWith("content://")) {
            try {
                cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    nombre = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        } else if (uri.toString().startsWith("file://")) {
            nombre = archivo.getAbsolutePath().toString();
        }
        return nombre;
    }
}
