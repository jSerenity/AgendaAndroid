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

public class CheckList_Compra extends AppCompatActivity {
    AppSQLiteOpenHepler conn;
    EditText nombreFecha;
    ListView listViewarticulos;
    ArrayList<articulos> listaarticulos;
    ArrayList<String> listainfoartic;
    ArrayAdapter adaptadorr;
    listas list_fecha;
   // ImageView SAVEB;
    ImageView UPDATEB;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unchek_list_compras);

        conn = new AppSQLiteOpenHepler(this, "db_agenda", null, 1);
        nombreFecha = (EditText) findViewById(R.id.editTextFecha);
        listViewarticulos = (ListView) findViewById(R.id.listvieunchek);
        //SAVEB = (ImageView) findViewById(R.id.save);
        UPDATEB = (ImageView) findViewById(R.id.imageView5);

        Bundle objetoEnviado = getIntent().getExtras();

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

        if (objetoEnviado != null) {
            list_fecha = (listas) objetoEnviado.getSerializable("Fecha");
            nombreFecha.setText(list_fecha.getFecha().toString());
            UPDATEB.setVisibility(View.VISIBLE);
            consultarUpdate();
        }
    }
    public void onClickCheck(View view){
        switch (view.getId()) {
            case R.id.imageView5:
                registroUpdate();
                break;
            case R.id.imageView4:
                redirect();
                break;
        }
    }

    private void redirect() {
        Intent intent=new Intent(CheckList_Compra.this,CheckList.class);

        startActivity(intent);
    }

    public void consultarUpdate() {
        SQLiteDatabase db=conn.getReadableDatabase();
        articulos arti=null;
        listaarticulos= new ArrayList<articulos>();
        String consulta = "SELECT a."+ tablas.CAMPO_ID+",a."+tablas.CAMPO_NOMBRE+",CASE "+ "b."+tablas.CAMPO_ESTADO_ARTI +" WHEN '0'THEN'0'ELSE'1' END  checkactivos"+ " FROM "+ tablas.TABLA_ARTICULOS+" a" +" INNER JOIN "+ tablas.TABLA_ARTI_LIST +" b" +" ON a."
                + tablas.CAMPO_ID+" = b."+tablas.ID_ARTICULOS+ " WHERE b." +tablas.ID_LISTAS+ " = " +"'"+list_fecha.getID()+"' "
                + "AND a."+tablas.CAMPO_ESTADO +"= 1";
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
    private  void registroUpdate(){
        //ArrayList<Integer> articulos =articulosSeleccionados();
        ArrayList<articulos> selectARTI= articulosSeleccionados();
        actualiarFechaARTICULO(selectARTI,list_fecha.getID());

    }

    private void actualiarFechaARTICULO(ArrayList<articulos> articulos, Integer id) {
        SQLiteDatabase db = conn.getWritableDatabase();


        for(int i=0;i<listaarticulos.size();i++){
            Integer estadoValue =listaarticulos.get(i).isActive()?1:0;
            String QUERY = "UPDATE "+ tablas.TABLA_ARTI_LIST + " SET "+
                    tablas.CAMPO_ESTADO_ARTI +" ="+estadoValue+
                    " WHERE "+tablas.ID_LISTAS +" ="+id.toString()+ " AND "+ tablas.ID_ARTICULOS +
                    " ="+listaarticulos.get(i).getID().toString();
            try {
                db.execSQL(QUERY);
            }catch (Exception e){
                String A =e.toString();
            }

            /*String[] parametro ={id.toString(),listaarticulos.get(i).getID().toString() };
            ContentValues values = new ContentValues();
            values.put(tablas.CAMPO_ESTADO_ARTI, listaarticulos.get(i).isActive()?1:0);
            db.update(tablas.TABLA_ARTI_LIST,values, tablas.ID_LISTAS+"=? AND "+ tablas.ID_ARTICULOS+ " =?",parametro);*/
        }

        Toast.makeText(getApplicationContext(),"ARTICULOS ACTUALIZADOS: ", Toast.LENGTH_SHORT).show();
        db.close();
    }

    public ArrayList<articulos> articulosSeleccionados()  {

        SparseBooleanArray sp = listViewarticulos.getCheckedItemPositions();

        ArrayList<articulos> sb= new ArrayList<articulos>();

        for(int i=0;i<sp.size();i++){
            //if(sp.valueAt(i)==true){
                articulos ARTIC= (articulos) listViewarticulos.getItemAtPosition(i);
                int s= ARTIC.getID();
                ARTIC.setActive(sp.valueAt(i));
                listaarticulos.get(i).setActive(sp.valueAt(i));
                sb.add(ARTIC);
            //}
        }
        return sb;
    }
}
