package com.example.sqllite;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    daoContacto dao;
    Adaptador adapter;
    ArrayList<Contacto>lista;
    Contacto c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dao = new daoContacto(MainActivity.this);
        lista = dao.verTodo();
        adapter = new Adaptador(this, lista, dao);
        ListView list = findViewById(R.id.listaV);
        Button insertar = findViewById(R.id.btnInsertar);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
             //dialogo para ver vista previa de registro
                int posicion = i;
                Contacto contacto = dao.verUno(posicion);
            }
        });
        insertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Dialogo para agregar
                Dialog dialog = new Dialog(MainActivity.this);
                dialog.setTitle("Nuevo Registro");
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.dialogo);
                dialog.show();
                final EditText nombre = dialog.findViewById(R.id.et_Nombre);
                final EditText apellido = dialog.findViewById(R.id.et_Apellido);
                final EditText email = dialog.findViewById(R.id.et_Email);
                final EditText telefono = dialog.findViewById(R.id.et_Telefono);
                final EditText ciudad = dialog.findViewById(R.id.et_Ciudad);
                Button guardar = dialog.findViewById(R.id.btAgregar);

                guardar.setText("Agregar");

                guardar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String nombreStr = nombre.getText().toString();
                        String apellidoStr = apellido.getText().toString();
                        String emailStr = email.getText().toString();
                        String telefonoStr = telefono.getText().toString();
                        String ciudadStr = ciudad.getText().toString();

                        // Validar que al menos un campo tenga información
                        if (!nombreStr.isEmpty() || !apellidoStr.isEmpty() || !emailStr.isEmpty() || !telefonoStr.isEmpty() || !ciudadStr.isEmpty()) {
                            c = new Contacto(nombreStr, apellidoStr, emailStr, telefonoStr, ciudadStr);
                            dao.insertar(c);
                            lista = dao.verTodo();
                            adapter.notifyDataSetChanged();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(getApplicationContext(), "Por favor, complete al menos un campo.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                Button cancelar = dialog.findViewById(R.id.btnCancelar);
                cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss(); // Cierra el diálogo
                    }
                });
            }
        });
    }
}