package com.ed2.aleja.cifrado;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CifradosFragment extends Fragment {
    View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.cifrados_fragment, container, false);

        try {
            BufferedReader lector = new BufferedReader(new InputStreamReader(rootView.getContext().openFileInput("cifrados.txt")));
            ArrayList<String> listadoCifrados = new ArrayList<>();
            String linea = lector.readLine();
            while (linea != null) {
                listadoCifrados.add(linea);
                linea = lector.readLine();
            }
            TextView hayCifrados = (TextView) rootView.findViewById(R.id.sin_cifrados_cifrados);
            ListView cifrados = (ListView) rootView.findViewById(R.id.listado_archivos_cifrados);
            if (listadoCifrados.size() > 0) {
                hayCifrados.setVisibility(View.INVISIBLE);
                cifrados.setVisibility(View.VISIBLE);
                ArrayList<ListViewItem> listadoItems = new ArrayList<>();
                int i = 0;
                String[] separados = null;
                while (i < listadoCifrados.size()) {
                    separados = listadoCifrados.get(i).split("\\|");
                    listadoItems.add(new ListViewItem(separados[0], separados[1], separados[2]));
                    i++;
                }
                lector.close();
                AdaptadorListViewItem adaptador = new AdaptadorListViewItem(listadoItems, rootView.getContext());
                cifrados.setAdapter(adaptador);
            } else {
                cifrados.setVisibility(View.INVISIBLE);
                hayCifrados.setVisibility(View.VISIBLE);
            }
        } catch (IOException e) {
            Toast.makeText(rootView.getContext(), "Hubo un error leyendo el archivo", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        return rootView;
    }
}
