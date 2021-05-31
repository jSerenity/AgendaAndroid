package com.example.parcial1;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.parcial1.clases.articulos;
import com.example.parcial1.clases.listas;
import com.example.parcial1.tablas.tablas;

import java.util.ArrayList;

public class RegistroCompra2 extends AppCompatActivity {

    AppSQLiteOpenHepler conn;
    EditText nombreFecha;
    ListView listViewarticulos;
    ArrayList<articulos> listaarticulos;
    ArrayList<String> listainfoartic;
    ArrayAdapter adaptadorr;
    listas list_fecha;
    ImageView SAVEB;
    ImageView UPDATEB;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_compras2);

        conn= new AppSQLiteOpenHepler(this,"db_agenda",null,1);
        nombreFecha =(EditText) findViewById(R.id.text_fecha);
        listViewarticulos= (ListView) findViewById(R.id.listadearti);
        SAVEB = (ImageView) findViewById(R.id.save);
        UPDATEB =(ImageView) findViewById(R.id.update);

        Bundle objetoEnviado=getIntent().getExtras();

        this.listViewarticulos.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        this.listViewarticulos.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // Log.i(TAG, "onItemClick: " +position);
                CheckedTextView v = (CheckedTextView) view;
                boolean currentCheck = v.isChecked();
                articulos arti = (articulos) listViewarticulos.getItemAtPosition(position);
                arti.setActive(!currentCheck);
            }
        });

        if (objetoEnviado!=null){
            list_fecha=(listas) objetoEnviado.getSerializable("Fecha");
            nombreFecha.setText(list_fecha.getFecha().toString());
            SAVEB.setVisibility(View.GONE);
            UPDATEB.setVisibility(View.VISIBLE);
            consultarUpdate();
         }
           else {
            SAVEB.setVisibility(View.VISIBLE);
            UPDATEB.setVisibility(View.GONE);
                   consultarlistadearticulos();
                }

        
        // adaptadorr= new ArrayAdapter(this, android.R.layout.simple_list_item_1,listainfoartic);
        //listViewarticulos.setAdapter(adaptadorr);
    }


    private  void registro(){
        SQLiteDatabase db = conn.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(tablas.CAMPO_FECHA, nombreFecha.getText().toString());

        long id = db.insert(tablas.TABLA_LISTAS, tablas.CAMPO_ID_LISTA,values);
        Toast.makeText(getApplicationContext(),"id: "+id, Toast.LENGTH_SHORT).show();

        ArrayList<Integer> at =articulosSeleccionados();
        registrarFechaARTICULO(at,id);
        db.close();

    }

    public void onClickRF(View view){
        switch (view.getId()) {
            case R.id.save:
                registro();
                redirect();
                break;
            case R.id.update:
                registroUpdate();
                break;
            case R.id.previus_2:
                redirect();
                break;
        }
    }

    public void consultarlistadearticulos() {
        SQLiteDatabase db=conn.getReadableDatabase();
        articulos arti=null;
        listaarticulos= new ArrayList<articulos>();
        Cursor cursor=db.rawQuery("SELECT * FROM "+ tablas.TABLA_ARTICULOS,null);

        while (cursor.moveToNext()){
            arti = new articulos();
            arti.setID(cursor.getInt(0));
            arti.setNombre(cursor.getString(1));

            listaarticulos.add(arti);

        }
        db.close();
        obtenerlista();

    }
    public void consultarUpdate() {
        SQLiteDatabase db=conn.getReadableDatabase();
        articulos arti=null;
        listaarticulos= new ArrayList<articulos>();
        String consulta = "SELECT a."+tablas.CAMPO_ID+",a."+tablas.CAMPO_NOMBRE+",CASE "+ "IFNULL(b."+tablas.ID_LISTAS +",'0')WHEN '0'THEN'0'ELSE'1' END  checkactivos"+ " FROM "+ tablas.TABLA_ARTICULOS+" a" +" LEFT JOIN "+ tablas.TABLA_ARTI_LIST +" b" +" ON a."
                + tablas.CAMPO_ID+" = b."+tablas.ID_ARTICULOS+ " AND b." +tablas.ID_LISTAS+ " = " +"'"+list_fecha.getID()+"'"
                + " WHERE "+tablas.CAMPO_ESTADO +"= 1";
        try {
            Cursor cursor=db.rawQuery(consulta,null);
            while (cursor.moveToNext()){
                arti = new articulos();
                arti.setID(cursor.getInt(0));
                arti.setNombre(cursor.getString(1));
                arti.setActive(cursor.getString(2).equals("0")?false:true);
                listaarticulos.add(arti);
            }
        }catch (Exception e){
            String A =e.toString();
        }



        db.close();
        obtenerlista();

    }
    private void obtenerlista() {
        ArrayAdapter<articulos> arrayAdapter
                = new ArrayAdapter(this, android.R.layout.simple_list_item_checked ,listaarticulos);

        this.listViewarticulos.setAdapter(arrayAdapter);

        for(int i=0;i< listaarticulos.size(); i++ )  {
            this.listViewarticulos.setItemChecked(i,listaarticulos.get(i).isActive());
        }

    }

    public ArrayList<Integer> articulosSeleccionados()  {

        SparseBooleanArray sp = listViewarticulos.getCheckedItemPositions();

        ArrayList<Integer> sb= new ArrayList<Integer>();

        for(int i=0;i<sp.size();i++){
            if(sp.valueAt(i)==true){
                articulos ARTIC= (articulos) listViewarticulos.getItemAtPosition(i);
                int s= ARTIC.getID();
                  sb.add(s);
            }
        }
        return sb;
    }
    public void registrarFechaARTICULO(ArrayList<Integer> L_A, long IDfecha){
        SQLiteDatabase db = conn.getWritableDatabase();
        ContentValues values = new ContentValues();
        String insert="INSERT INTO " + tablas.TABLA_ARTI_LIST + "( "+tablas.ID_ARTICULOS+ ","+tablas.ID_LISTAS+") VALUES";
        String valuesSTR="";
        for(int i=0;i<L_A.size();i++){
            //values.put(tablas.ID_ARTICULOS,L_A.get(i).toString() );
            //values.put(tablas.ID_LISTAS,IDfecha );
            //long id = db.insert(tablas.TABLA_ARTI_LIST, tablas.ID_REGISTRO_ARTI_LIST,values);
            String coma=i+1!=L_A.size()?",":";";
            valuesSTR+=" ("+L_A.get(i).toString()+","+IDfecha+")"+ coma;
            }
        insert += valuesSTR;
        db.execSQL(insert);
        Log.i("queries: ", insert);
        Toast.makeText(getApplicationContext(),"ARTICULOS ACTUALIZADOS: ", Toast.LENGTH_SHORT).show();
        db.close();
    }
    private  void registroUpdate(){
        SQLiteDatabase db = conn.getWritableDatabase();
        String[] parametro ={list_fecha.getID().toString()};
        ContentValues values = new ContentValues();
        values.put(tablas.CAMPO_FECHA, nombreFecha.getText().toString());

        db.update(tablas.TABLA_LISTAS,values, tablas.CAMPO_ID_LISTA+"=?",parametro);
        Toast.makeText(getApplicationContext(),"NOMBRE ACTUALIZADO: ", Toast.LENGTH_SHORT).show();

        ArrayList<Integer> at =articulosSeleccionados();
        EliminarArticulos();
        ArrayList<Integer> articulos =articulosSeleccionados();
        registrarFechaARTICULO(articulos,list_fecha.getID());
        db.close();

    }
   private  void EliminarArticulos(){
       SQLiteDatabase db = conn.getWritableDatabase();
       String[] parametro ={list_fecha.getID().toString()};

       db.delete(tablas.TABLA_ARTI_LIST,tablas.ID_LISTAS+"=?",parametro);
       db.close();
   }
    private void redirect() {
        Intent intent=new Intent(RegistroCompra2.this,RegistroCompras.class);

        startActivity(intent);
    }
}
