package com.ed2.aleja.cifrado;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
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

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class ZigZagFragment extends Fragment {

    private static String UBICACION_GUARDAR = " ";
    private static String UBICACION_ARCHIVO_CIFRAR = " ";
    private static final int READ_REQUEST_CODE_FILE = 1;
    private static final int READ_REQUEST_CODE_DIRECTORY = 2;
    View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.zigzag_fragment, container, false);

        Button examinarArchivoCifrar = (Button) rootView.findViewById(R.id.examinar_cifrar_zigzag);
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

        Button folderGuardar = (Button) rootView.findViewById(R.id.examinar_carpeta_cifrar_zigzag);
        folderGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
                        .addCategory(Intent.CATEGORY_DEFAULT);
                startActivityForResult(Intent.createChooser(intent, "Seleccione ubicación del archivo cifrado"), READ_REQUEST_CODE_DIRECTORY);
            }
        });

        final CheckBox sobreescribirArchivo = (CheckBox) rootView.findViewById(R.id.sobreescribir_cifrar_zigzag);
        sobreescribirArchivo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                TextView mostrarInformacionDirectorio = (TextView) rootView.findViewById(R.id.info_carpeta_destino_cifrar_zigzag);
                String textoMostrar = "";
                if (sobreescribirArchivo.isChecked()) {
                    textoMostrar = getString(R.string.si_sobre);
                } else {
                    textoMostrar = getString(R.string.no_sobre);
                }
                mostrarInformacionDirectorio.setText(textoMostrar + " " + UBICACION_GUARDAR);
            }
        });

        return rootView;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (requestCode == READ_REQUEST_CODE_FILE && resultCode == Activity.RESULT_OK) {
            EditText ubicacionArchivoCifrar = (EditText) rootView.findViewById(R.id.ubicacion_archivo_cifrar_zigzag);
            // Obtener la ruta del archivo
            ubicacionArchivoCifrar.setText(UBICACION_ARCHIVO_CIFRAR);
        } else if (requestCode == READ_REQUEST_CODE_DIRECTORY && resultCode == Activity.RESULT_OK) {
            TextView mostrarInformacionDirectorio = (TextView) rootView.findViewById(R.id.info_carpeta_destino_cifrar_zigzag);
            CheckBox sobreescribirArchivo = (CheckBox) rootView.findViewById(R.id.sobreescribir_cifrar_zigzag);
            // Obtener la ruta del directorio
            String textoMostrar = "";
            if (sobreescribirArchivo.isChecked()) {
                textoMostrar = getString(R.string.si_sobre);
            } else {
                textoMostrar = getString(R.string.no_sobre);
            }
            mostrarInformacionDirectorio.setText(textoMostrar + " " + UBICACION_GUARDAR);
        } else if (requestCode == READ_REQUEST_CODE_FILE && resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(rootView.getContext(), "No se seleccionón ningún archivo", Toast.LENGTH_SHORT).show();
        } else if (requestCode == READ_REQUEST_CODE_DIRECTORY && resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(rootView.getContext(), "No se seleccionón ningún directorio", Toast.LENGTH_SHORT).show();
        }
    }
}
