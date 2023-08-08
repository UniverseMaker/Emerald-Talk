package com.messenger.emeraldtalk;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class DBHelper extends SQLiteOpenHelper {
    private Context context;

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // String 보다 StringBuffer가 Query 만들기 편하다.
        //StringBuffer sb = new StringBuffer();
        //sb.append(" CREATE TABLE RECORD ( ID INTEGER PRIMARY KEY AUTOINCREMENT,  NAME TEXT,  TARGET TEXT,  WINNER TEXT,  DATE TEXT ) ");

        // SQLite Database로 쿼리 실행
        db.execSQL("CREATE TABLE FRIENDLIST ( ID INTEGER PRIMARY KEY AUTOINCREMENT,  NAME TEXT,  PHONE TEXT,  PROFILE TEXT )");
        db.execSQL("CREATE TABLE CHATLIST ( ID INTEGER PRIMARY KEY AUTOINCREMENT,  MEMBER TEXT,  TITLE TEXT,  CONTENTS TEXT, VARIABLE TEXT, TIME TEXT )");
        db.execSQL("CREATE TABLE CHAT ( ID INTEGER PRIMARY KEY AUTOINCREMENT,  ROOMID TEXT, OWNER TEXT,  DATA TEXT, TIME TEXT )");
        //Toast.makeText(context, "Table 생성완료", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Toast.makeText(context, "버전이 올라갔습니다.", Toast.LENGTH_SHORT).show();
    }

    public void testDB() {
        SQLiteDatabase db = getReadableDatabase();
    }

    public void querySQL(String sql)
    {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL(sql);
    }

    public Map<Integer, ArrayList<String>> getFriendList()
    {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT ID, NAME, PHONE, PROFILE FROM FRIENDLIST ORDER BY ID DESC", null);
        Map<Integer, ArrayList<String>> data = new TreeMap<Integer, ArrayList<String>>(Collections.reverseOrder());

        // moveToNext 다음에 데이터가 있으면 true 없으면 false
        while( cursor.moveToNext() ) {
            data.put(cursor.getInt(0), new ArrayList<String>());
            data.get(cursor.getInt(0)).add(cursor.getString(1));
            data.get(cursor.getInt(0)).add(cursor.getString(2));
            data.get(cursor.getInt(0)).add(cursor.getString(3));
        }

        return data;
    }

    public void insertFriendList(String name, String phone, String profile)
    {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("INSERT INTO FRIENDLIST (  NAME, PHONE, PROFILE )  VALUES (  '" + name  + "', '" + phone + "', '" + profile + "' )");
    }

    public void updateFriendList(String id, String name, String phone, String profile)
    {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("UPDATE FRIENDLIST SET NAME='" + name + "', PHONE='" + phone + "', PROFILE='" + profile + "' WHERE ID=" + id);
    }

    public void removeFriendList(String id)
    {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("DELETE FROM FRIENDLIST WHERE ID=" + id);
    }

    public Map<Integer, ArrayList<String>> getChatList()
    {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT ID, MEMBER, TITLE, CONTENTS, VARIABLE, TIME FROM CHATLIST ORDER BY ID ASC", null);
        Map<Integer, ArrayList<String>> data = new TreeMap<Integer, ArrayList<String>>(); //Collections.reverseOrder()

        // moveToNext 다음에 데이터가 있으면 true 없으면 false
        while( cursor.moveToNext() ) {
            data.put(cursor.getInt(0), new ArrayList<String>());
            data.get(cursor.getInt(0)).add(cursor.getString(1));
            data.get(cursor.getInt(0)).add(cursor.getString(2));
            data.get(cursor.getInt(0)).add(cursor.getString(3));
            data.get(cursor.getInt(0)).add(cursor.getString(4));
            data.get(cursor.getInt(0)).add(cursor.getString(5));
        }

        return data;
    }

    public String searchChatListID(String member)
    {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT ID, MEMBER, TITLE, CONTENTS, VARIABLE, TIME FROM CHATLIST WHERE MEMBER='" + member + "' ORDER BY ID DESC", null);
        Map<Integer, ArrayList<String>> data = new TreeMap<Integer, ArrayList<String>>(Collections.reverseOrder());

        // moveToNext 다음에 데이터가 있으면 true 없으면 false
        while( cursor.moveToNext() ) {
            return String.valueOf(cursor.getInt(0));
        }

        return null;
    }

    public void insertChatList(String member, String title, String contents, String variable, String time)
    {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("INSERT INTO CHATLIST ( MEMBER, TITLE, CONTENTS, VARIABLE, TIME )  VALUES ( '" + member + "', '" + title  + "', '" + contents + "', '"+ variable +"', '"+ time +"' )");
    }

    public void updateChatList(String id, String member, String title, String contents, String variable, String time)
    {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("UPDATE CHATLIST SET MEMBER='" + member + "', TITLE='" + title + "', CONTENTS='" + contents + "', VARIABLE='" + variable + "', TIME='" + time + "' WHERE ID=" + id);
    }

    public void updateChatList(String id, String contents, String time)
    {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("UPDATE CHATLIST SET CONTENTS='" + contents + "', TIME='" + time + "'  WHERE ID=" + id);
    }

    public void removeChatList(String id)
    {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("DELETE FROM CHATLIST WHERE ID=" + id);
    }

    public Map<Integer, ArrayList<String>> getChat(String roomid)
    {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT ID, ROOMID, OWNER, DATA, TIME FROM CHAT WHERE ROOMID='" + roomid + "' ORDER BY ID ASC", null);
        Map<Integer, ArrayList<String>> data = new TreeMap<Integer, ArrayList<String>>(); //Collections.reverseOrder()

        // moveToNext 다음에 데이터가 있으면 true 없으면 false
        while( cursor.moveToNext() ) {
            data.put(cursor.getInt(0), new ArrayList<String>());
            data.get(cursor.getInt(0)).add(cursor.getString(1));
            data.get(cursor.getInt(0)).add(cursor.getString(2));
            data.get(cursor.getInt(0)).add(cursor.getString(3));
            data.get(cursor.getInt(0)).add(cursor.getString(4));
        }

        return data;
    }

    public void insertChat(String roomid, String owner, String data, String time)
    {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("INSERT INTO CHAT ( ROOMID, OWNER, DATA, TIME )  VALUES (  '" + roomid  + "', '" + owner  + "', '" + data + "', '"+ time +"' )");
    }

    public void removeChat(String id)
    {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("DELETE FROM CHAT WHERE ID=" + id);
    }

}
