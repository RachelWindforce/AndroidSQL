package com.example.proyectoconsql;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v13.view.inputmethod.InputContentInfoCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContentInfo;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText ID_Trabajador, Nombretrabajador, Cargotrabajador;
    ListView ListaTrabajadores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ID_Trabajador = findViewById(R.id.ID);
        Nombretrabajador = findViewById(R.id.NombreTrabajador);
        Cargotrabajador = findViewById(R.id.CargoTrabajador);
        ListaTrabajadores = findViewById(R.id.ListaTrabajadores);
        CargaTrabajadores();


    }

    public void  CrearTrabajador(View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "Produccion", null, 1);
        SQLiteDatabase BaseDatos = admin.getWritableDatabase();
        String ID = ID_Trabajador.getText().toString();
        String Nombre = Nombretrabajador.getText().toString();
        String Cargo = Cargotrabajador.getText().toString();
        if (ID.isEmpty() || Nombre.isEmpty() || Cargo.isEmpty()){
            Toast.makeText(this, "No se permiten campos vacios", Toast.LENGTH_SHORT).show();

        }else{
            ContentValues DatosUsuarios = new ContentValues();
            DatosUsuarios.put("ID_Usuario", ID);
            DatosUsuarios.put("NombreTrabajador", Nombre);
            DatosUsuarios.put("CargoTrabajador", Cargo);
            BaseDatos.insert("Trabajadores", null, DatosUsuarios);
            BaseDatos.close();
            ID_Trabajador.setText("");
            Nombretrabajador.setText("");
            Cargotrabajador.setText("");
            Toast.makeText(this, "Trabajador Registrado", Toast.LENGTH_SHORT).show();
            CargaTrabajadores();

        }

    }

    public void CargaTrabajadores (){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "Produccion", null, 1);
        SQLiteDatabase BaseDatos = admin.getWritableDatabase();
        Cursor fila = BaseDatos.rawQuery("SELECT * FROM Trabajadores", null);
        ArrayList<String> ListaUsuarios = new ArrayList<>();
        if (fila.moveToFirst()) {
            do {
                String ID = fila.getString(0);
                String Nombre = fila.getString(1);
                String Cargo = fila.getString(2);
                String UserInfo = "ID: " + ID + ", Nombre: " + Nombre + ", Cargo: " + Cargo;
                ListaUsuarios.add(UserInfo);


            } while (fila.moveToNext());
        }
        BaseDatos.close();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,ListaUsuarios);
        ListaTrabajadores.setAdapter(adapter);


    }

    public void BuscarTrabajador(View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "Produccion", null, 1);
        SQLiteDatabase BaseDatos = admin.getWritableDatabase();
        String ID = ID_Trabajador.getText().toString();
        if (!ID .isEmpty()){
            Cursor fila = BaseDatos.rawQuery("SELECT NombreTrabajador, CargoTrabajador from Trabajadores WHERE ID_Usuario ="+ ID, null);
            if (fila.moveToFirst()){
                Nombretrabajador.setText(fila.getString(0));
                Cargotrabajador.setText(fila.getString(1));
                BaseDatos.close();

            }else{
                Toast.makeText(this, "El id"+ID+" proporcionado no se existe", Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(this, "No puede dejar campos vacios"+ID, Toast.LENGTH_SHORT).show();
        }
    }

    public void ActualizarTrabajador(View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "Produccion", null, 1);
        SQLiteDatabase BaseDatos = admin.getWritableDatabase();
        String ID = ID_Trabajador.getText().toString();
        String Nombre = Nombretrabajador.getText().toString();
        String Cargo = Cargotrabajador.getText().toString();
        if (!Nombre.isEmpty() && !Cargo.isEmpty()){
            ContentValues DatosUsuario = new ContentValues();
            DatosUsuario.put("NombreTrabajador", Nombre);
            DatosUsuario.put("CargoTrabajador", Cargo);

            //Devuelve datos binarios, por eso se ocupa int
            int cantidad = BaseDatos.update("Trabajadores", DatosUsuario, "ID_Usuario="+ ID, null);
            if (cantidad == 1){
                Toast.makeText(this, "El registro se actualizo correctamente", Toast.LENGTH_SHORT).show();
                //Limpiar datos
                ID_Trabajador.setText("");
                Nombretrabajador.setText("");
                Cargotrabajador.setText("");
                CargaTrabajadores();
            }else{
                Toast.makeText(this, "No se encontro el ID ingresado", Toast.LENGTH_SHORT).show();

            }
        }else{
            Toast.makeText(this, "No puede dejar campos vacios", Toast.LENGTH_SHORT).show();

        }


    }

    public void Eliminartrabajador (View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "Produccion", null, 1);
        SQLiteDatabase BaseDatos = admin.getWritableDatabase();
        String ID = ID_Trabajador.getText().toString();

        if (!ID.isEmpty()){
            int eliminar = BaseDatos.delete("Trabajadores","ID_Usuario =" + ID,null);
            if (eliminar == 1){
                Toast.makeText(this, "El trbajador se elimino correctamente", Toast.LENGTH_SHORT).show();
                ID_Trabajador.setText("");
                Nombretrabajador.setText("");
                Cargotrabajador.setText("");
                CargaTrabajadores();

            }else{
                Toast.makeText(this, "El id proporcionado no existe.", Toast.LENGTH_SHORT).show();

            }


        }else{
            Toast.makeText(this, "El capo id no puede estar vacio", Toast.LENGTH_SHORT).show();

        }
    }
}