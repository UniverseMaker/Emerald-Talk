package com.messenger.emeraldtalk;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public FBRemoteConfig mFBRemoteConfig;
    public ActivityChatList activityChatList;
    public ActivityChatRoom activityChatRoom;

    private LocalBroadcastManager broadcaster;

    public int noticeUpdate = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chatUpdate();
                Snackbar.make(view, "메세지 새로고침", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.container, ActivityChatList.newInstance(this, "",""));
        //transaction.addToBackStack(null);
        transaction.commit();


        try{
            DBHelper dbHelper = new DBHelper(this, "EMERALDTALK", null , 1);
            //dbHelper.querySQL("DROP TABLE IF EXISTS FRIENDLIST");
            //dbHelper.querySQL("DROP TABLE IF EXISTS CHATLIST");
            //dbHelper.querySQL("DROP TABLE IF EXISTS CHATROOM");
        }catch (Exception e){

        }

        FBRemoteConfig.checkGooglePlayServices(this);
        mFBRemoteConfig = new FBRemoteConfig(this);
        mFBRemoteConfig.initialize();


        /*
        new Thread() {
            public void run() {
                String marketVersion = getPlaystoreVersion();

                Bundle bun = new Bundle();
                bun.putString("MARKET_VERSION", marketVersion);
                Message msg = handler.obtainMessage();
                msg.setData(bun);
                handler.sendMessage(msg);
            }
        }.start();
        */

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED ||
                        ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_DENIED ||
                        ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED ||
                        ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED ||
                        ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ||
                        ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
                    dlgAlert.setCancelable(false);
                    dlgAlert.setTitle("권한요청");
                    dlgAlert.setMessage("다음 화면에서 메신저 서비스를 제공하기 위해 인터넷과 연락처, 전화, 문자의 제어권한 그리고 데이터 저장을 위한 저장소 제어권한을 요청합니다");
                    dlgAlert.setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //dismiss the dialog

                                    requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE,Manifest.permission.READ_CONTACTS,
                                            Manifest.permission.CALL_PHONE,
                                            Manifest.permission.RECORD_AUDIO,Manifest.permission.READ_EXTERNAL_STORAGE,
                                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                                }
                            });
                    dlgAlert.setNegativeButton("거절",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //dismiss the dialog
                                    finish();
                                }
                            });

                    //dlgAlert.create().show();
                    Dialog dlg = dlgAlert.create();
                    dlg.setCanceledOnTouchOutside(false);
                    dlg.show();

                } else {
                    // 권한 있음
                    mFBRemoteConfig.onFirebaseConfigLoad_defaultWork();
                    mFBRemoteConfig.onFirebaseConfigLoad_createInstance();
                }
            }
        }catch (Exception e){

        }

        //Intent intent = new Intent(this, ActivityAgreement.class);
        //EditText editText = (EditText) findViewById(R.id.editText);
        //String message = editText.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE, message);
        //startActivity(intent);


        SharedPreferences pref = getSharedPreferences("EMERALDTALK", MODE_PRIVATE);
        String pp = pref.getString("privacy_policy", "");

        if(pp.isEmpty() == true) {
            Intent intent = new Intent(MainActivity.this, ActivityAgreement.class);
            startActivity(intent);
        }

        chatUpdate();
    }

    @Override
    public void onStart() {
        super.onStart();

        LocalBroadcastManager.getInstance(this).registerReceiver((mMessageReceiver),
                new IntentFilter("dbupdate")
        );
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                try {
                    if (grantResults.length > 0){
                        //PackageManager.PERMISSION_GRANTED = 1
                        int grantsum = 0;
                        for(int i = 0; i < grantResults.length; i++)
                            grantsum += grantResults[i];

                        if(grantsum == 0) {
                            // 권한 허가
                            // 해당 권한을 사용해서 작업을 진행할 수 있습니다
                            mFBRemoteConfig.onFirebaseConfigLoad_defaultWork();
                            mFBRemoteConfig.onFirebaseConfigLoad_createInstance();
                        }
                        else{
                            finish();
                        }
                    } else {
                        finish();
                    }
                }catch (Exception e){

                }
                return;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_friendlist) {
            // Handle the camera action
            transaction.replace(R.id.container, ActivityFriendList.newInstance(this, "", ""));
        } else if (id == R.id.nav_chatlist) {
            transaction.replace(R.id.container, ActivityChatList.newInstance(this, "", ""));
        } else if (id == R.id.nav_settings) {
            transaction.replace(R.id.container, ActivitySettings.newInstance(this, "", ""));
        }

        //transaction.addToBackStack(null);
        transaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //chatlistupdate
            chatUpdate();
        }
    };

    public void updateList(String data){
        try {
            JSONArray jarray = new JSONArray(data);

            for(int i=0; i < jarray.length(); i = i + 4){
                //jarray.getString(i);
                DBHelper dbHelper = new DBHelper(this, "EMERALDTALK", null, 1);
                String id = dbHelper.searchChatListID(jarray.getString(i + 1));
                if(id == null) {
                    dbHelper.insertChatList(jarray.getString(i+1), jarray.getString(i+1), jarray.getString(i + 2), "", jarray.getString(i + 3));
                    id = dbHelper.searchChatListID(jarray.getString(i+1));
                }

                dbHelper.updateChatList(id, jarray.getString(i+2), jarray.getString(i+3));
                dbHelper.insertChat(id, jarray.getString(i), jarray.getString(i+2), jarray.getString(i+3));

            }

            //refreshList();
            try {
                //Intent intent = new Intent("chatlistupdate");
                //intent.putExtra("phone", "");
                //broadcaster.sendBroadcast(intent);
                activityChatList.refreshList();
            }catch (Exception e2){

            }

            try {
                //Intent intent = new Intent("chatroomupdate");
                //intent.putExtra("phone", "");
                //broadcaster.sendBroadcast(intent);
                activityChatRoom.refreshList();
            }catch (Exception e2){

            }

        } catch (Exception e){

        }
    }

    // Server에 생성된 토큰을 등록하기 위해, 생성후 바로 보낼 때 활용하는 메소드
    public String updateRequest() {
        // TODO: Implement this method to send token to your app server.
        //Log.d(TAG, "new token: " + token);
        try {
            Log.d("emeraldtalk", "check update request");
            String token = FirebaseInstanceId.getInstance().getToken();

            // HttpURLConnection 을 사용하여 보내는 방법
            HttpURLConnection connection;

            URL url = new URL("https://" + mFBRemoteConfig.getConfigValue("server_host") + mFBRemoteConfig.getConfigValue("url_messenger_receive"));
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true); //서버에 데이터 보낼 때, Post의 경우 꼭 사용
            connection.setDoInput(true); //서버에서 데이터 가져올 때
            connection.setRequestMethod("POST"); // POST방식을

            StringBuffer buffer = new StringBuffer();
            buffer = buffer.append("receiver").append("=").append(token);
            OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
            wr.write(buffer.toString());
            wr.flush(); // 서버에 작성
            wr.close(); // 객체를 닫음

            // 서버에서 값을 받아오지 않더라도 작성해야함
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            connection.disconnect();

            return sb.toString();

        }catch (Exception e) {
            e.printStackTrace();

            return "";
        }

    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Bundle bun = msg.getData();
            String rData = bun.getString("data");
            updateList(rData);
        }
    };

    public void chatUpdate(){
        new Thread() {
            public void run() {
                String result = updateRequest();

                Bundle bun = new Bundle();
                bun.putString("data", result);
                Message msg = handler.obtainMessage();
                msg.setData(bun);
                handler.sendMessage(msg);
            }
        }.start();
    }
}
