package com.example.parcial1.tablas;

public class tablas {

    public static final  String TABLA_ARTICULOS="articulos";
    public static final  String CAMPO_ID="id";
    public static final  String CAMPO_NOMBRE="nombre";
    public static final  String CAMPO_ESTADO="estado";

    public static final String CREAR_TABLA_ARTICULOS="CREATE TABLE " + ""+TABLA_ARTICULOS+" " +
            "("+CAMPO_ID+" " +"INTEGER primary key autoincrement, "+CAMPO_NOMBRE+" TEXT, "+CAMPO_ESTADO+ " INTEGER)";


    public static final String TABLA_LISTAS="listas";
    public static final String CAMPO_ID_LISTA="id";
    public static final String CAMPO_FECHA="fecha";

    public static final String CREAR_TABLA_LISTAS="CREATE TABLE " + ""+TABLA_LISTAS+" "+
            "("+CAMPO_ID_LISTA+" " +"INTEGER primary key autoincrement, "+CAMPO_FECHA+" TEXT)";

    public static final String TABLA_ARTI_LIST="artilist";
    public static final String ID_REGISTRO_ARTI_LIST="id";
    public static final String ID_ARTICULOS="idARTICULOS";
    public static final String ID_LISTAS="idLISTAS";
    public static final  String CAMPO_ESTADO_ARTI="estado";

    public static final String CREAR_TABLA_ARTI_LIST="CREATE TABLE " + ""+TABLA_ARTI_LIST+" "+
            "("+ID_REGISTRO_ARTI_LIST+" "+"INTEGER primary key autoincrement, "+ID_ARTICULOS+" " +"INTEGER, "+ID_LISTAS+" " +"INTEGER, "+CAMPO_ESTADO_ARTI+" INTEGER)";

}

