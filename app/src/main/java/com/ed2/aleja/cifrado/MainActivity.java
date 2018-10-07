package com.ed2.aleja.cifrado;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int respuesta = 0;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, respuesta);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, respuesta);
        }
        toolbar = findViewById(R.id.toolbar_navigation);
        toolbar.setTitle(R.string.cifrar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, new CifrarFragment()).commit();
            navigationView.setCheckedItem(R.id.cifrar);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.cifrar:
                getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, new CifrarFragment()).commit();
                toolbar.setTitle(R.string.cifrar);
                break;
            case R.id.cifrados:
                getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, new CifradosFragment()).commit();
                toolbar.setTitle(R.string.cifrados);
                break;
            case R.id.descifrar:
                getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, new DescifrarFragment()).commit();
                toolbar.setTitle(R.string.descifrar);
                break;
        }
        navigationView.setCheckedItem(item.getItemId());
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed(){
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            // Cierra el activity Drawer
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            // Otra accion cuando se presione regresar
            super.onBackPressed();
        }
    }
}
