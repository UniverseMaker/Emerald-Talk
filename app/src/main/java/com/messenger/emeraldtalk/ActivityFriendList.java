package com.messenger.emeraldtalk;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class ActivityFriendList extends Fragment {
    static MainActivity ma;
    View v;

    RecyclerAdapterFriendList adapter;
    ArrayList<RecyclerItemFriendList> mItems = new ArrayList<>();
    RecyclerView recyclerView;

    public ActivityFriendList() {
        // Required empty public constructor
    }

    public static ActivityFriendList newInstance(MainActivity _ma, String param1, String param2) {
        ma = _ma;
        ActivityFriendList fragment = new ActivityFriendList();
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
        getActivity().setTitle("연락처");

        //inflate메소드는 XML데이터를 가져와서 실제 View객체로 만드는 작업을 합니다.
        v = inflater.inflate(R.layout.content_friendlist, container, false);
        setRecyclerView();

        mItems.add(new RecyclerItemFriendList(getResources().getDrawable(R.mipmap.defaultimage), "김지석", "", ""));
        adapter.notifyDataSetChanged();


        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        //ma.mFBRemoteConfig.onFirebaseConfigLoad_defaultWork();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
        super.onSaveInstanceState(outState);
    }

    private void setRecyclerView(){
        //ActivityMainBinding mainBinding;

        recyclerView = (RecyclerView) v.findViewById(R.id.friendListRecyclerView);

        // 각 Item 들이 RecyclerView 의 전체 크기를 변경하지 않는 다면
        // setHasFixedSize() 함수를 사용해서 성능을 개선할 수 있습니다.
        // 변경될 가능성이 있다면 false 로 , 없다면 true를 설정해주세요.
        recyclerView.setHasFixedSize(true);

        // RecyclerView에 Adapter를 설정해줍니다.
        adapter = new RecyclerAdapterFriendList(mItems);
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



}
