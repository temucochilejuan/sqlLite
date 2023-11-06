package com.example.sqllite;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Adaptador extends BaseAdapter {

    ArrayList<Contacto> lista;
    daoContacto dao;
    Contacto c;
    Activity a; // una referencia a la actividad que utiliza este adaptador
    int id = 0; // una variable que almacena temporalmente el ID del contacto seleccionado

    public Adaptador(Activity a, ArrayList<Contacto> lista, daoContacto dao) {
        this.lista = lista;
        this.dao = dao;
        this.a = a;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int i) {
        c = lista.get(i);
        return null;
    }

    @Override
    public long getItemId(int i) {
        c = lista.get(i);
        return c.getId();
    }

    @Override
    public View getView(int posicion, View view, ViewGroup viewGroup) {
        View v = view;
        if (v == null) {
            LayoutInflater li = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.item, null);
        }
        c = lista.get(posicion);
        TextView nombre = v.findViewById(R.id.txtNombre);
        TextView apellido = v.findViewById(R.id.txtApellido);
        TextView email = v.findViewById(R.id.txtEmail);
        TextView telefono = v.findViewById(R.id.txtTelefono);
        TextView ciudad = v.findViewById(R.id.txtCuidad);
        Button editar = v.findViewById(R.id.btnEditar);
        Button eliminar = v.findViewById(R.id.btnEliminar);
        Button detalles = v.findViewById(R.id.btnDetalles);

        nombre.setText(c.getNombre());
        apellido.setText(c.getApellido());
        email.setText(c.getEmail());
        telefono.setText(c.getTelefono());
        ciudad.setText(c.getCiudad());

        editar.setTag(posicion);
        eliminar.setTag(posicion);
        detalles.setTag(posicion);

        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = Integer.parseInt(view.getTag().toString());
                final Dialog dialog = new Dialog(a);
                dialog.setTitle("Editar registro");
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.dialogo);
                dialog.show();
                final EditText nombre = dialog.findViewById(R.id.et_Nombre);
                final EditText apellido = dialog.findViewById(R.id.et_Apellido);
                final EditText email = dialog.findViewById(R.id.et_Email);
                final EditText telefono = dialog.findViewById(R.id.et_Telefono);
                final EditText ciudad = dialog.findViewById(R.id.et_Ciudad);
                Button guardar = dialog.findViewById(R.id.btAgregar);
                Button cancelar = dialog.findViewById(R.id.btnCancelar);
                c = lista.get(pos);
                setId(c.getId());
                nombre.setText(c.getNombre());
                apellido.setText(c.getApellido());
                email.setText(c.getEmail());
                telefono.setText(c.getTelefono());
                ciudad.setText(c.getCiudad());
                guardar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            c = new Contacto(getId(), nombre.getText().toString(),
                                    apellido.getText().toString(),
                                    email.getText().toString(),
                                    telefono.getText().toString(),
                                    ciudad.getText().toString());
                            dao.editar(c);
                            lista = dao.verTodo();
                            notifyDataSetChanged();
                            dialog.dismiss();
                        } catch (Exception e) {
                            Toast.makeText(a, "Error al guardar", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });

        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = Integer.parseInt(view.getTag().toString());
                c = lista.get(pos);
                setId(c.getId());
                AlertDialog.Builder del = new AlertDialog.Builder(a);
                del.setMessage("¿Estás seguro?");
                del.setCancelable(false);
                del.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dao.eliminar(getId());
                        lista = dao.verTodo();
                        notifyDataSetChanged();
                    }
                });
                del.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                del.show();
            }
        });

        detalles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = Integer.parseInt(view.getTag().toString());
                mostrarDetalles(pos);
            }
        });
        return v;
    }

    private void mostrarDetalles(int posicion) {
        c = lista.get(posicion);

        // Crear un diálogo personalizado
        final Dialog dialog = new Dialog(a);
        dialog.setTitle("Detalles del Contacto");
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialogo);

        final EditText nombre = dialog.findViewById(R.id.et_Nombre);
        final EditText apellido = dialog.findViewById(R.id.et_Apellido);
        final EditText email = dialog.findViewById(R.id.et_Email);
        final EditText telefono = dialog.findViewById(R.id.et_Telefono);
        final EditText ciudad = dialog.findViewById(R.id.et_Ciudad);
        Button aceptar = dialog.findViewById(R.id.btAgregar);
        Button cancelar = dialog.findViewById(R.id.btnCancelar);

        nombre.setText(c.getNombre());
        apellido.setText(c.getApellido());
        email.setText(c.getEmail());
        telefono.setText(c.getTelefono());
        ciudad.setText(c.getCiudad());

        // Configurar las vistas como no editables
        nombre.setEnabled(false);
        apellido.setEnabled(false);
        email.setEnabled(false);
        telefono.setEnabled(false);
        ciudad.setEnabled(false);

        aceptar.setVisibility(View.GONE);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
