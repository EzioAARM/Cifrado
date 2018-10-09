package com.ed2.aleja.cifrado;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DescifrarFragment extends Fragment {

    private static String UBICACION_GUARDAR = " ";
    private static String UBICACION_ARCHIVO_CIFRAR = " ";
    private static final int READ_REQUEST_CODE_FILE = 1;
    private static final int READ_REQUEST_CODE_DIRECTORY = 2;
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
                        .setType("*/cif")
                        .setAction(Intent.ACTION_OPEN_DOCUMENT);
                startActivityForResult(intent, READ_REQUEST_CODE_FILE);
            }
        });
        return rootView;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (requestCode == READ_REQUEST_CODE_FILE && resultCode == Activity.RESULT_OK) {
            EditText ubicacionArchivoCifrar = (EditText) rootView.findViewById(R.id.ubicacion_archivo_descifrar);
            // Obtener la ruta del archivo
            ubicacionArchivoCifrar.setText(UBICACION_ARCHIVO_CIFRAR);
        } else if (requestCode == READ_REQUEST_CODE_DIRECTORY && resultCode == Activity.RESULT_OK) {
            /*TextView mostrarInformacionDirectorio = (TextView) rootView.findViewById(R.id.info_carpeta_destino_cifrar_zigzag);
            CheckBox sobreescribirArchivo = (CheckBox) rootView.findViewById(R.id.sobreescribir_cifrar_zigzag);
            // Obtener la ruta del directorio
            String textoMostrar = "";
            if (sobreescribirArchivo.isChecked()) {
                textoMostrar = getString(R.string.si_sobre);
            } else {
                textoMostrar = getString(R.string.no_sobre);
            }
            mostrarInformacionDirectorio.setText(textoMostrar + " " + UBICACION_GUARDAR);*/
        } else if (requestCode == READ_REQUEST_CODE_FILE && resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(rootView.getContext(), "No se seleccionón ningún archivo", Toast.LENGTH_SHORT).show();
        } else if (requestCode == READ_REQUEST_CODE_DIRECTORY && resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(rootView.getContext(), "No se seleccionón ningún directorio", Toast.LENGTH_SHORT).show();
        }
    }
}
