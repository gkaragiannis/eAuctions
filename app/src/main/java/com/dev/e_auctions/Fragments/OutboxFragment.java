package com.dev.e_auctions.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.e_auctions.APIRequests.GetMessagesRequest;
import com.dev.e_auctions.APIResponses.GetMessagesResponse;
import com.dev.e_auctions.Adapter.MessageAdapter;
import com.dev.e_auctions.Client.RestClient;
import com.dev.e_auctions.Common.Common;
import com.dev.e_auctions.Interface.RestApi;
import com.dev.e_auctions.Model.Message;
import com.dev.e_auctions.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OutboxFragment extends Fragment {
    private List<Message> messages = new ArrayList<>();
    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_outbox, container, false);

        recyclerView = view.findViewById(R.id.frg_outbox_rec_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        messageAdapter = new MessageAdapter(getContext(), messages);
        recyclerView.setAdapter(messageAdapter);
        new HttpGetOutboxMessagesTask().execute();
        return view;
    }

    private void getMessages() {

        GetMessagesRequest getMessagesRequest = new GetMessagesRequest(Common.token);
        Call<GetMessagesResponse> call = RestClient.getClient().create(RestApi.class).postGetInbox(getMessagesRequest);

        call.enqueue(new Callback<GetMessagesResponse>() {
            @Override
            public void onResponse(Call<GetMessagesResponse> call, Response<GetMessagesResponse> response) {
                if (!response.isSuccessful()){
                    return;
                }
                else if (!response.body().getStatusCode().equals("SUCCESS")){
                    return;
                }
                else {
                    messages.addAll(response.body().getMessages());
                    messageAdapter.updateDataset();
                }
            }

            @Override
            public void onFailure(Call<GetMessagesResponse> call, Throwable t) {
                System.out.println("Unavailable services");
                return;
            }
        });

        for (Message message : messages){
            System.out.println(message.getSubject());
        }
        System.out.println(messages.size());
    }

    private class HttpGetOutboxMessagesTask extends AsyncTask<Void, Void, List<Message>> {
        @Override
        protected List<Message> doInBackground(Void... voids) {
            GetMessagesRequest getMessagesRequest = new GetMessagesRequest(Common.token);
            Call<GetMessagesResponse> call = RestClient.getClient().create(RestApi.class).postGetOutbox(getMessagesRequest);

            try {
                Response<GetMessagesResponse> getMessagesResponse = call.execute();

                if (!getMessagesResponse.isSuccessful()){
                    return null;
                }
                else if (!getMessagesResponse.body().getStatusCode().equals("SUCCESS")){
                    return null;
                }
                else {
                    messages.addAll(getMessagesResponse.body().getMessages());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return messages;
        }

        @Override
        protected void onPostExecute(List<Message> messages) {
            messageAdapter.updateDataset();
        }
    }
}
