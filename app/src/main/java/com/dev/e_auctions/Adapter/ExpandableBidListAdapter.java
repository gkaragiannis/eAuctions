package com.dev.e_auctions.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.dev.e_auctions.Model.Bid;
import com.dev.e_auctions.R;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 *
 */
public class ExpandableBidListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> listDataHeader;
    private HashMap<String, List<Bid>> listHashMap;

    /**
     *
     * @param context
     * @param listDataHeader
     * @param listHashMap
     */
    public ExpandableBidListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<Bid>> listHashMap) {
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
    public Bid getChild(int groupPosition, int childPosition) {
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
            convertView = inflater.inflate(R.layout.auction_bids_elv_group, parent, false);
        }
        TextView auctionBidsELVheader_text = (TextView) convertView.findViewById(R.id.auctionBidsELVHeader);
        auctionBidsELVheader_text.setText(headerTitle);

        if (isExpanded){
            auctionBidsELVheader_text.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_arrow_drop_up_black_24dp,0);
        }
        else{
            auctionBidsELVheader_text.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_arrow_drop_down_black_24dp,0);
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
        DecimalFormat df = new DecimalFormat("#.00");
        final String childDate = (String)getChild(groupPosition, childPosition).getBidTime();
        final String childValue = (String)df.format((double) getChild(groupPosition, childPosition).getBidPrice()).toString();
        final String childRating = (String)getChild(groupPosition, childPosition).getBidder().getBidderRating().toString() + "/5.0";
        if (convertView==null){
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.auction_bids_elv_item, parent, false);
        }
        TextView auctionBidsELVitemDate = (TextView) convertView.findViewById(R.id.auctionBidsELVitemDate);
        try {
            Date startingDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(childDate);
            auctionBidsELVitemDate.setText(startingDate.toString());
        } catch (ParseException e) {
            e.printStackTrace();
            auctionBidsELVitemDate.setText(childDate);
        }
        TextView auctionBidsELVitemValue = (TextView) convertView.findViewById(R.id.auctionBidsELVitemValue);
        auctionBidsELVitemValue.setText(childValue);
        TextView auctionBidsELVitemRating = (TextView) convertView.findViewById(R.id.auctionBidsELVitemRating);
        auctionBidsELVitemRating.setText(childRating);
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
