package com.dev.e_auctions.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.dev.e_auctions.R;

import java.util.HashMap;
import java.util.List;

/**
 *
 */
public class ExpandableCategoryListAdapter  extends BaseExpandableListAdapter {
    private Context context;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listHashMap;

    /**
     *
     * @param context
     * @param listDataHeader
     * @param listHashMap
     */
    public ExpandableCategoryListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<String>> listHashMap) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listHashMap = listHashMap;
    }

    /**
     *
     * @return
     */
    @Override
    public int getGroupCount() {
        return listDataHeader.size();
    }

    /**
     *
     * @param groupPosition
     * @return
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        return listHashMap.get(listDataHeader.get(groupPosition)).size();
    }

    /**
     *
     * @param groupPosition
     * @return
     */
    @Override
    public Object getGroup(int groupPosition) {
        return listDataHeader.get(groupPosition);
    }

    /**
     *
     * @param groupPosition
     * @param childPosition
     * @return
     */
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listHashMap.get(listDataHeader.get(groupPosition)).get(childPosition);
    }

    /**
     *
     * @param groupPosition
     * @return
     */
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    /**
     *
     * @param groupPosition
     * @param childPosition
     * @return
     */
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean hasStableIds() {
        return false;
    }

    /**
     *
     * @param groupPosition
     * @param isExpanded
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String)getGroup(groupPosition);
        if (convertView==null){
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.auction_categories_elv_group, parent, false);
        }
        TextView auctionCategoriesELVheader_text = (TextView) convertView.findViewById(R.id.auctionCategoriesELVHeader);
        auctionCategoriesELVheader_text.setText(headerTitle);

        if (isExpanded){
//            auctionCategoriesELVheader_text.setTypeface(null, Typeface.BOLD);
            auctionCategoriesELVheader_text.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_arrow_drop_up_black_24dp,0);
        }
        else{
//            auctionCategoriesELVheader_text.setTypeface(null, Typeface.BOLD);
            auctionCategoriesELVheader_text.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_arrow_drop_down_black_24dp,0);
        }
        return convertView;
    }

    /**
     *
     * @param groupPosition
     * @param childPosition
     * @param isLastChild
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText = (String)getChild(groupPosition, childPosition);
        if (convertView==null){
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.auction_categories_elv_item, parent, false);
        }
        TextView auctionCategoriesELVitem_text = (TextView) convertView.findViewById(R.id.auctionCategoriesELVitem);
        auctionCategoriesELVitem_text.setText(childText);
        return convertView;
    }

    /**
     *
     * @param groupPosition
     * @param childPosition
     * @return
     */
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
