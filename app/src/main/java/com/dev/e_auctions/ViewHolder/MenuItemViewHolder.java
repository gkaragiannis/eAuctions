/*
package com.dev.e_auctions.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dev.e_auctions.Interface.MenuItemClickListener;
import com.dev.e_auctions.R;

*/
/**
 *
 *//*

public class MenuItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtMenuItemName;
    public ImageView imgMenuItemView;

    private MenuItemClickListener menuItemClickListener;

    */
/**
     *
     * @param itemView
     *//*

    public MenuItemViewHolder(View itemView){
        super(itemView);

        txtMenuItemName = (TextView)itemView.findViewById(R.id.menu_item_name);
        imgMenuItemView = (ImageView)itemView.findViewById(R.id.menu_item_image);

        itemView.setOnClickListener(this);
    }

    */
/**
     *
     * @param menuItemClickListener
     *//*

    public void setMenuItemClickListener(MenuItemClickListener menuItemClickListener) {
        this.menuItemClickListener = menuItemClickListener;
    }

    */
/**
     *
     * @param v
     *//*

    @Override
    public void onClick(View v) {
        menuItemClickListener.onClick(v, getAdapterPosition(), false);
    }
}
*/
