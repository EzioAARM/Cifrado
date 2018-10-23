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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import static android.content.Context.MODE_APPEND;

public class RSAFragment extends Fragment {

    private static String UBICACION_GUARDAR = " ";
    private static String UBICACION_ARCHIVO_CIFRAR = " ";
    private static final int READ_REQUEST_CODE_FILE = 1;
    private String TextoCifrar = "";
    private boolean SobreescribirArchivo = false;
    View rootView;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.rsa_cifrar_fragment, container, false);

        File directorio = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            directorio = new File(Environment.getExternalStorageDirectory() + "/CifradoEstructuras/");
        else
            directorio = new File(rootView.getContext().getFilesDir() + "/CifradoEstructuras/");
        if (!directorio.exists())
            directorio.mkdir();
        TextView mostrarInformacionDirectorio = (TextView) rootView.findViewById(R.id.info_carpeta_destino_cifrar_rsa);
        UBICACION_GUARDAR = directorio.getAbsolutePath();
        mostrarInformacionDirectorio.setText(getString(R.string.no_sobre) + " " + UBICACION_GUARDAR + "/");

        Button examinarArchivoCifrar = (Button) rootView.findViewById(R.id.examinar_cifrar_rsa);
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

        final CheckBox sobreescribirArchivo = (CheckBox) rootView.findViewById(R.id.sobreescribir_cifrar_rsa);
        sobreescribirArchivo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                TextView mostrarInformacionDirectorio = (TextView) rootView.findViewById(R.id.info_carpeta_destino_cifrar_rsa);
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

        Button cifrarArchivo = (Button) rootView.findViewById(R.id.cifrar_cifrar);
        cifrarArchivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String extension = UBICACION_ARCHIVO_CIFRAR.substring(UBICACION_ARCHIVO_CIFRAR.lastIndexOf('.') + 1);
                    String nombreArchivo = UBICACION_ARCHIVO_CIFRAR.substring(0, UBICACION_ARCHIVO_CIFRAR.lastIndexOf('.'));

                    RSA rsa = new RSA(rootView.getContext(), SobreescribirArchivo, nombreArchivo);
                    rsa.Cifrar(TextoCifrar, extension);

                    FileOutputStream output = rootView.getContext().openFileOutput("cifrados.txt", MODE_APPEND);
                    OutputStreamWriter escritor = new OutputStreamWriter(output);
                    escritor.append(rsa.nombreArchivoListado + "|" + UBICACION_GUARDAR + "/" + rsa.nombreArchivoListado + "|" + "RSA\n");
                    escritor.close();
                    Toast.makeText(rootView.getContext(), "El archivo se cifró correctamente", Toast.LENGTH_LONG).show();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Toast.makeText(rootView.getContext(), "Hubo un error cifrando el archivo", Toast.LENGTH_LONG).show();
                }
            }
        });

        return rootView;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (requestCode == READ_REQUEST_CODE_FILE && resultCode == Activity.RESULT_OK) {
            try {
                EditText ubicacionArchivoCifrar = (EditText) rootView.findViewById(R.id.ubicacion_archivo_cifrar_rsa);
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
                TextoCifrar = textoArchivo;
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
