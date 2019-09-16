package com.dev.e_auctions.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dev.e_auctions.Model.MenuItem;
import com.dev.e_auctions.Model.Message;
import com.dev.e_auctions.Model.User;
import com.dev.e_auctions.R;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private Context context;
    private List<Message> messages;

    public MessageAdapter(Context context, List<Message> messages) {
        this.context = context;
        this.messages = messages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.message_item, viewGroup, false);
        return new MessageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        Message message = messages.get(position);
        viewHolder.subject.setText(message.getSubject());
        if (message.isRead()){
            viewHolder.read_unread.setImageResource(R.drawable.ic_mail_outline_green_24dp);
        }
        else {
            viewHolder.read_unread.setImageResource(R.drawable.ic_email_green_24dp);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView subject;
        public ImageView read_unread;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            subject = itemView.findViewById(R.id.message_item_subject);
            read_unread = itemView.findViewById(R.id.message_item_read_unread);
        }
    }

    public void updateDataset(/*List<Message> newDataset*/){
        System.out.println("prin to clear: " + messages.size());
        /*System.out.println("prin to clear: " + newDataset.size());
        messages.clear();
        System.out.println("Meta to clear: " + messages.size());
        System.out.println("Meta to clear: " + newDataset.size());
//        messages.addAll(newDataset);
        for (Message message : newDataset){
            System.out.println(message.getMessageId());
            messages.add(message);
        }
        System.out.println("meta to addAll: " + messages.size());
        System.out.println("meta to addAll: " + newDataset.size());*/
        this.notifyDataSetChanged();
        System.out.println("prin to clear: " + messages.size());
    }
}
