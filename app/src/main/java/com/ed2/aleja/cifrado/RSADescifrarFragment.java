package com.ed2.aleja.cifrado;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ed2.aleja.clases_cifrados.RSA;
import com.ed2.aleja.clases_cifrados.sdes;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RSADescifrarFragment extends Fragment {

    View rootView;
    private static String UBICACION_GUARDAR = " ";
    private static String UBICACION_ARCHIVO_CIFRAR = " ";
    private static final int READ_REQUEST_CODE_FILE = 1;
    private String TextoDescifrar = "";
    private boolean SobreescribirArchivo = false;
    private String NombreArchivo;
    private String clavePrivada;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.rsa_descifrar_fragment, container, false);

        Bundle argumentos = getArguments();
        TextoDescifrar = (String) argumentos.getSerializable("textoDescifrar");
        NombreArchivo = (String) argumentos.getSerializable("nombreArchivo");

        File directorio = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            directorio = new File(Environment.getExternalStorageDirectory() + "/CifradoEstructuras/");
        else
            directorio = new File(rootView.getContext().getFilesDir() + "/CifradoEstructuras/");
        if (!directorio.exists())
            directorio.mkdir();
            TextView mostrarInformacionDirectorio = (TextView) rootView.findViewById(R.id.info_carpeta_destino_descifrar_rsa);
        UBICACION_GUARDAR = directorio.getAbsolutePath();
        mostrarInformacionDirectorio.setText(getString(R.string.no_sobre) + " " + UBICACION_GUARDAR + "/");

        Button examinarArchivoCifrar = (Button) rootView.findViewById(R.id.examinar_private_descifrar_rsa);
        examinarArchivoCifrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent()
                        .addCategory(Intent.CATEGORY_OPENABLE)
                        .setType("*/*")
                        .setAction(Intent.ACTION_OPEN_DOCUMENT);
                startActivityForResult(intent, READ_REQUEST_CODE_FILE);
            }
        });

        final CheckBox sobreescribirArchivo = (CheckBox) rootView.findViewById(R.id.sobreescribir_descifrar_rsa);
        sobreescribirArchivo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                TextView mostrarInformacionDirectorio = (TextView) rootView.findViewById(R.id.info_carpeta_destino_descifrar_rsa);
                String textoMostrar = "";
                if (sobreescribirArchivo.isChecked()) {
                    textoMostrar = getString(R.string.si_sobre);
                    SobreescribirArchivo = true;
                } else {
                    textoMostrar = getString(R.string.no_sobre);
                    SobreescribirArchivo = false;
                }
                mostrarInformacionDirectorio.setText(textoMostrar + " " + UBICACION_GUARDAR  + "/");
            }
        });

        Button descifrarArchivo = (Button) rootView.findViewById(R.id.descifrar_descifrar);
        descifrarArchivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int d = Integer.parseInt(clavePrivada.split(" ")[0]);
                    int n = Integer.parseInt(clavePrivada.split(" ")[1]);
                    RSA descifrador = new RSA(rootView.getContext(), SobreescribirArchivo, NombreArchivo);
                    descifrador.descifrar(TextoDescifrar, n, d);
                    Toast.makeText(rootView.getContext(), "El archivo se descifró correctamente", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(rootView.getContext(), "Hubo un error descifrando el archivo", Toast.LENGTH_LONG).show();
                }
            }
        });

        return rootView;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (requestCode == READ_REQUEST_CODE_FILE && resultCode == Activity.RESULT_OK) {
            try {
                EditText ubicacionArchivoCifrar = (EditText) rootView.findViewById(R.id.ubicacion_private_archivo_descifrar_rsa);
                Uri uri = resultData.getData();
                UBICACION_ARCHIVO_CIFRAR = obtenerNombreArchivo(uri);

                ubicacionArchivoCifrar.setText(UBICACION_ARCHIVO_CIFRAR);
                InputStream inputStream = rootView.getContext().getContentResolver().openInputStream(uri);
                BufferedReader lector = new BufferedReader(new InputStreamReader(inputStream));
                String textoArchivo = "";
                int caracter;
                while ((caracter = lector.read()) != -1) {
                    textoArchivo += (char) caracter;
                }
                clavePrivada = textoArchivo;
            } catch (IOException ioex) {
                ioex.printStackTrace();
                Toast.makeText(rootView.getContext(), "Hubo un error leyendo el archivo", Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(rootView.getContext(), "El archivo se cargó correctamente", Toast.LENGTH_SHORT).show();
        } else {
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
