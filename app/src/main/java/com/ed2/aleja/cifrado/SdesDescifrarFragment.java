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

import com.ed2.aleja.clases_cifrados.sdes;

import java.io.File;

public class SdesDescifrarFragment extends Fragment {

    View rootView;
    private static String UBICACION_GUARDAR = " ";
    private static String UBICACION_ARCHIVO_CIFRAR = " ";
    private static final int READ_REQUEST_CODE_FILE = 1;
    private String TextoDescifrar = "";
    private boolean SobreescribirArchivo = false;
    private String NombreArchivo;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.sdes_descifrar_fragment, container, false);

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
        TextView mostrarInformacionDirectorio = (TextView) rootView.findViewById(R.id.info_carpeta_destino_descifrar_sdes);
        UBICACION_GUARDAR = directorio.getAbsolutePath();
        mostrarInformacionDirectorio.setText(getString(R.string.no_sobre) + " " + UBICACION_GUARDAR + "/");

        final CheckBox sobreescribirArchivo = (CheckBox) rootView.findViewById(R.id.sobreescribir_descifrar_sdes);
        sobreescribirArchivo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                TextView mostrarInformacionDirectorio = (TextView) rootView.findViewById(R.id.info_carpeta_destino_descifrar_sdes);
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
                    EditText contraseniaSdes = (EditText) rootView.findViewById(R.id.contrasenia_sdes_descifrar);
                    if (!contraseniaSdes.getText().toString().equals("")) {
                        // código para descifrar
                        int password = Integer.parseInt(contraseniaSdes.getText().toString());
                        //NombreArchivo = NombreArchivo.substring(0, NombreArchivo.lastIndexOf('.'));
                        sdes Descifrador = new sdes(rootView.getContext(), SobreescribirArchivo, NombreArchivo);
                        Descifrador.Descifrar(TextoDescifrar, String.valueOf(password));
                        Toast.makeText(rootView.getContext(), "El archivo se descifró correctamente", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(rootView.getContext(), "Debe ingresar el número de niveles para cifrar", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(rootView.getContext(), "Hubo un error descifrando el archivo", Toast.LENGTH_LONG).show();
                }
            }
        });

        return rootView;
    }

}
