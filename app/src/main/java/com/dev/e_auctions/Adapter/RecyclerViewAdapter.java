package com.dev.e_auctions.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dev.e_auctions.AuctionActivity;
import com.dev.e_auctions.Interface.MenuItemClickListener;
import com.dev.e_auctions.Model.MenuItem;
import com.dev.e_auctions.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MenuItemViewHolder> {

    private ArrayList<MenuItem> mMenuItemList;
    private Context context;

    public RecyclerViewAdapter(ArrayList<MenuItem> mMenuItemList, Context context) {
        this.mMenuItemList = mMenuItemList;
        this.context = context;
    }

    public class MenuItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txtMenuItemName;
        public ImageView imgMenuItemView;

        private MenuItemClickListener menuItemClickListener;

        public MenuItemViewHolder(View itemView){
            super(itemView);

            txtMenuItemName = (TextView)itemView.findViewById(R.id.menu_item_name);
            imgMenuItemView = (ImageView)itemView.findViewById(R.id.menu_item_image);

            itemView.setOnClickListener(this);
        }

        public void setMenuItemClickListener(MenuItemClickListener menuItemClickListener) {
            this.menuItemClickListener = menuItemClickListener;
        }

        @Override
        public void onClick(View v) {
            menuItemClickListener.onClick(v, getAdapterPosition(), false);
        }
    }

    /*public RecyclerViewAdapter(ArrayList<MenuItem> menuItemList){
        mMenuItemList = menuItemList;
    }*/

    @NonNull
    @Override
    public MenuItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item_home, parent, false);
        MenuItemViewHolder menuItemViewHolder = new MenuItemViewHolder(v);
        return menuItemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MenuItemViewHolder menuItemViewHolder, int position) {
        final MenuItem currentMenuListItem = mMenuItemList.get(position);

        Picasso.get().load(currentMenuListItem.getImage()).into(menuItemViewHolder.imgMenuItemView);
        menuItemViewHolder.txtMenuItemName.setText(currentMenuListItem.getName());
        menuItemViewHolder.setMenuItemClickListener(new MenuItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                //Get CategoryId and send to new Activity
                Intent newActivity = new Intent(context, AuctionActivity.class);
                //Because CategoryId is key, so we just get the key of this item
                newActivity.putExtra("AuctionId", Integer.toString(currentMenuListItem.getId()));
                context.startActivity(newActivity);
                return;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMenuItemList.size();
    }

    public void updateDataset(ArrayList<MenuItem> newDataset){
        mMenuItemList.clear();
        mMenuItemList.addAll(newDataset);
        this.notifyDataSetChanged();
    }
}
