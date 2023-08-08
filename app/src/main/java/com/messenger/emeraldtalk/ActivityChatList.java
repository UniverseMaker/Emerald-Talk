package com.messenger.emeraldtalk;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.messenger.emeraldtalk.utility.UniqueID;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

public class ActivityChatList extends Fragment {
    static MainActivity ma;
    View v;

    RecyclerAdapterChatList adapter;
    ArrayList<RecyclerItemChatList> mItems = new ArrayList<>();
    RecyclerView recyclerView;

    public ActivityChatList() {
        // Required empty public constructor
    }

    public static ActivityChatList newInstance(MainActivity _ma, String param1, String param2) {
        ma = _ma;
        ActivityChatList fragment = new ActivityChatList();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam1 = getArguments().getString("param1");
            String mParam2 = getArguments().getString("param2");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("대화");

        //inflate메소드는 XML데이터를 가져와서 실제 View객체로 만드는 작업을 합니다.
        v = inflater.inflate(R.layout.content_chatlist, container, false);
        ma.activityChatList = this;
        setRecyclerView();
        refreshList();
        adapter.setItemClick(recyclerChatList_Clicked);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        LocalBroadcastManager.getInstance(ma).registerReceiver((mMessageReceiver),
                new IntentFilter("chatlistupdate")
        );
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(ma).unregisterReceiver(mMessageReceiver);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
        super.onSaveInstanceState(outState);
    }

    private void setRecyclerView(){
        //ActivityMainBinding mainBinding;

        recyclerView = (RecyclerView) v.findViewById(R.id.chatListRecyclerView);

        // 각 Item 들이 RecyclerView 의 전체 크기를 변경하지 않는 다면
        // setHasFixedSize() 함수를 사용해서 성능을 개선할 수 있습니다.
        // 변경될 가능성이 있다면 false 로 , 없다면 true를 설정해주세요.
        recyclerView.setHasFixedSize(true);

        // RecyclerView에 Adapter를 설정해줍니다.
        adapter = new RecyclerAdapterChatList(mItems);
        recyclerView.setAdapter(adapter);

        // 다양한 LayoutManager 가 있습니다. 원하시는 방법을 선택해주세요.
        // 지그재그형의 그리드 형식
        //mainBinding.recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        // 그리드 형식
        //mainBinding.recyclerView.setLayoutManager(new GridLayoutManager(this,4));
        // 가로 또는 세로 스크롤 목록 형식
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //adapter.setItemClick(listMemberRecycler_Clicked);

    }

    public void refreshList(){
        mItems.clear();

        DBHelper dbHelper = new DBHelper(ma, "EMERALDTALK", null, 1);
        Map<Integer, ArrayList<String>> data = dbHelper.getChatList();

        Set keyset = data.keySet();
        for (Iterator iterator = keyset.iterator(); iterator.hasNext(); ) {
            int key = (int) iterator.next();
            ArrayList<String> value = (ArrayList<String>) data.get(key);

            mItems.add(new RecyclerItemChatList(getResources().getDrawable(R.mipmap.defaultimage), String.valueOf(key), value.get(1), value.get(2), value.get(4).replace(" ", "\r\n")));
        }

        //mItems.add(new RecyclerItemChatList(getResources().getDrawable(R.mipmap.defaultimage), "1", "1", "공지사항", "반갑습니다", "2018-11-01"));
        adapter.notifyDataSetChanged();

        TextView tvEmpty = (TextView)v.findViewById(R.id.chatListEmpty);
        if(mItems.size() > 0){
            tvEmpty.setVisibility(View.GONE);
        }
        else{
            tvEmpty.setVisibility(View.VISIBLE);
        }
    }



    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //chatlistupdate
            refreshList();
        }
    };

    RecyclerAdapterChatList.ItemClick recyclerChatList_Clicked = new RecyclerAdapterChatList.ItemClick() {
        @Override
        public void onClick(View view, final int position) {
            //클릭시 실행될 함수 작성

            final String id = String.valueOf(mItems.get(position).getId());

            FragmentTransaction transaction = ma.getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, ActivityChatRoom.newInstance(ma, id,""));
            transaction.addToBackStack(null);
            transaction.commit();

        }
    };

}
