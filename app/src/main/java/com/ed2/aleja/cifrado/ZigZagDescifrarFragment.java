package com.ed2.aleja.cifrado;

import android.os.Bundle;
import android.os.Environment;
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

public class ZigZagDescifrarFragment extends Fragment {

    View rootView;
    private String TextoDescifrar = "";
    private static String UBICACION_GUARDAR = "";
    public static boolean SobreescribirArchivo = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.zigzag_descifrar_fragment, container, false);

        Bundle argumentos = getArguments();
        TextoDescifrar = (String) argumentos.getSerializable("textoDescifrar");

        File directorio = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            directorio = new File(Environment.getExternalStorageDirectory() + "/DescifradosEstructuras/");
        else
            directorio = new File(rootView.getContext().getFilesDir() + "/DescifradosEstructuras/");
        if (!directorio.exists())
            directorio.mkdir();
        TextView mostrarInformacionDirectorio = (TextView) rootView.findViewById(R.id.info_carpeta_destino_descifrar_descifrar);
        UBICACION_GUARDAR = directorio.getAbsolutePath();
        mostrarInformacionDirectorio.setText(getString(R.string.no_sobre) + " " + UBICACION_GUARDAR + "/");

        final CheckBox sobreescribirArchivo = (CheckBox) rootView.findViewById(R.id.sobreescribir_descifrar_zigzag);
        sobreescribirArchivo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                TextView mostrarInformacionDirectorio = (TextView) rootView.findViewById(R.id.info_carpeta_destino_descifrar_descifrar);
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
                EditText nivelesSeparacion = (EditText) rootView.findViewById(R.id.niveles_separacion_descifrar_zigzag);
                if (!nivelesSeparacion.getText().toString().equals("")) {
                    // código para descifrar
                    int niveles = Integer.parseInt(nivelesSeparacion.getText().toString());
                } else {
                    Toast.makeText(rootView.getContext(), "Debe ingresar el número de niveles para cifrar", Toast.LENGTH_LONG).show();
                }
            }
        });

        return rootView;
    }
}
