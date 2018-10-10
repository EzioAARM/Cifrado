package com.ed2.aleja.cifrado;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdaptadorListViewItem extends BaseAdapter {

    private ArrayList<ListViewItem> ItemsList;
    private Context Contexto;

    public AdaptadorListViewItem(ArrayList<ListViewItem> itemsList, Context contexto) {
        ItemsList = itemsList;
        Contexto = contexto;
    }

    @Override
    public int getCount() {
        return ItemsList.size();
    }

    @Override
    public Object getItem(int position) {
        return ItemsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(Contexto).inflate(R.layout.item_cifrado, null);
        TextView nombreCifrado = (TextView) convertView.findViewById(R.id.nombre_cifrado_cifrados);
        TextView ubicacionCifrado = (TextView) convertView.findViewById(R.id.ubicacion_archivo_cifrados);
        TextView metodoCifrado = (TextView) convertView.findViewById(R.id.tipo_cifrado_cifrados);
        LinearLayout fondoCifrados = (LinearLayout) convertView.findViewById(R.id.fondo_cifrados);

        ListViewItem item = (ListViewItem) getItem(position);

        if (item.getMetodo().equals("ZigZag")) {
            fondoCifrados.setBackground(convertView.getResources().getDrawable(R.drawable.border_radius_item_zigzag));
        } else if (item.getMetodo().equals("S-DES")) {
            fondoCifrados.setBackground(convertView.getResources().getDrawable(R.drawable.border_radius_item_sdes));
        } else if (item.getMetodo().equals("RSA")) {
            fondoCifrados.setBackground(convertView.getResources().getDrawable(R.drawable.border_radius_item_rsa));
        }

        nombreCifrado.setText(item.getNombre());
        ubicacionCifrado.setText(item.getUbicacion());
        metodoCifrado.setText(item.getMetodo());

        return convertView;
    }
}
