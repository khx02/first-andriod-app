package com.example.assignment_2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment_2.provider.Event;

import java.util.ArrayList;

public class EventRecyclerAdapter extends RecyclerView.Adapter<EventRecyclerAdapter.EventRecyclerViewHolder>{

    ArrayList<Event> data = new ArrayList<>();

    public void setData(ArrayList<Event> data){
        this.data = data;
    }
    @Override
    public int getItemCount() {
        if (this.data != null) { // if data is not null
            return this.data.size(); // then return the size of ArrayList
        }
        // else return zero if data is null
        return 0;
    }

    @NonNull
    @Override
    public EventRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card_layout, parent, false);
        return new EventRecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EventRecyclerViewHolder holder, int position) {
        holder.tvEventId.setText(data.get(position).getEventId());
        holder.tvEventName.setText(data.get(position).getEventName());
        int ticketCount = data.get(position).getTicketCount();
        holder.tvEventTickets.setText(String.valueOf(ticketCount));
        holder.tvEventCatId.setText(data.get(position).getEventCategoryId());
        if (data.get(position).isActive()) {
            holder.tvIsActive.setText("Active");
        } else {
            holder.tvIsActive.setText("Inactive");
        }
        holder.itemView.setOnClickListener(v -> {
                String eventName = data.get(position).getEventName();

                Context context  = holder.itemView.getContext();
                Intent intent = new Intent(context, EventGoogleResultActivity.class);
                intent.putExtra("eventName", eventName);
                context.startActivity(intent);
            }
        );

    }
    public class EventRecyclerViewHolder extends RecyclerView.ViewHolder {

        public TextView tvEventId;
        public TextView tvEventName;
        public TextView tvEventTickets ;
        public TextView tvEventCatId;
        public TextView tvIsActive;

        public EventRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEventId = itemView.findViewById(R.id.tvEventId);
            tvEventName = itemView.findViewById(R.id.tvEventName);
            tvEventTickets = itemView.findViewById(R.id.tvEventTickets);
            tvEventCatId = itemView.findViewById(R.id.tvCatId);
            tvIsActive = itemView.findViewById(R.id.tvIsActive);
        }
    }
}