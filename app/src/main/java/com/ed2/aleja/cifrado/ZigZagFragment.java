package com.ed2.aleja.cifrado;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class ZigZagFragment extends Fragment {

    private static final int READ_REQUEST_CODE = 42;
    View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.zigzag_fragment, container, false);

        Button cifrar = (Button) rootView.findViewById(R.id.examinar_cifrar_zigzag);
        cifrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent()
                        .addCategory(Intent.CATEGORY_OPENABLE)
                        .setType("*/*")
                        .setAction(Intent.ACTION_OPEN_DOCUMENT);
                startActivityForResult(intent, READ_REQUEST_CODE);
            }
        });

        return rootView;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Toast.makeText(rootView.getContext(), "Si se seleecionó algo", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(rootView.getContext(), "No se seleccionón ningún archivo", Toast.LENGTH_SHORT).show();
        }
    }
}
