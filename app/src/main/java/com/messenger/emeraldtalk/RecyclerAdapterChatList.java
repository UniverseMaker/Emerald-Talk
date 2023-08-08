package com.messenger.emeraldtalk;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerAdapterChatList extends RecyclerView.Adapter<RecyclerAdapterChatList.ItemViewHolder> {
    ArrayList<RecyclerItemChatList> mitems;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    /*
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public ViewHolder(TextView v) {
            super(v);
            mTextView = v;
        }
    }
    */

    //아이템 클릭시 실행 함수
    private ItemClick itemClick;
    public interface ItemClick {
        public void onClick(View view, int position);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerAdapterChatList(ArrayList<RecyclerItemChatList> items) {
        mitems = items;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        //TextView v = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.my_text_view, parent, false);

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleritem_chatlist, parent, false);

        //ViewHolder vh = new ViewHolder(v);
        return new ItemViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        final int Position = position;
        //중략 ...................
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClick != null){
                    itemClick.onClick(v, Position);
                }
            }
        });

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //holder.mTextView.setText(mDataset[position]);
        holder.mImage.setImageDrawable(mitems.get(position).getImage());
        holder.mTitle.setText(mitems.get(position).getTitle());
        holder.mContents.setText(mitems.get(position).getContents());
        holder.mTime.setText(mitems.get(position).getTime());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mitems.size();
    }

    //아이템 클릭시 실행 함수 등록 함수
    public void setItemClick(ItemClick itemClick) {
        this.itemClick = itemClick;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{
        View view;
        String roomid;
        private ImageView mImage;
        private TextView mTitle;
        private TextView mContents;
        private TextView mTime;
        public ItemViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            mImage = (ImageView) itemView.findViewById(R.id.riChatListImage);
            mTitle = (TextView) itemView.findViewById(R.id.riChatListTitle);
            mContents = (TextView) itemView.findViewById(R.id.riChatListContents);
            mTime = (TextView) itemView.findViewById(R.id.riChatListTime);
        }
    }
}
