package com.example.assignment_2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment_2.provider.EventCategory;

import java.util.ArrayList;

public class EventCategoryRecyclerAdapter extends RecyclerView.Adapter<EventCategoryRecyclerAdapter.EventCategoryRecyclerViewHolder>{

    ArrayList<EventCategory> data = new ArrayList<>();
    private final int HEADER_CARD_TYPE = 0;
    private final int VALUE_CARD_TYPE = 1;

    public void setData(ArrayList<EventCategory> data){
        this.data = data;
    }
    @Override
    public int getItemCount() {
        return data.size() + 1;
    }

    @NonNull
    @Override
    public EventCategoryRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if (viewType == VALUE_CARD_TYPE) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_category_card_layout, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_category_header_card_layout, parent, false);
        }
        return new EventCategoryRecyclerViewHolder(v);
    }
    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return HEADER_CARD_TYPE;
        else return VALUE_CARD_TYPE;
    }

    @Override
    public void onBindViewHolder(@NonNull EventCategoryRecyclerViewHolder holder, int position) {
        if (position != 0) {
            holder.tvCatId.setText(data.get(position-1).getCategoryId());
            holder.tvEventCatName.setText(data.get(position-1).getCategoryName());
            int sEventCount = data.get(position-1).getEventCount();
            holder.tvEventCount.setText(String.valueOf(sEventCount));
            if (data.get(position-1).isActive()) {
                holder.tvIsActive.setText("Active");
            } else {
                holder.tvIsActive.setText("Inactive");
            }
        }
        holder.itemView.setOnClickListener(v -> {
            String selectedCountry = data.get(position-1).getLocation();

            Context context  = holder.itemView.getContext();
            Intent intent = new Intent(context, GoogleMapActivity.class);
            intent.putExtra("countrySelected", selectedCountry);
            context.startActivity(intent);
            }
        );
    }
    public class EventCategoryRecyclerViewHolder extends RecyclerView.ViewHolder {

        public TextView tvCatId;
        public TextView tvEventCatName;
        public TextView tvEventCount;
        public TextView tvIsActive;

        public EventCategoryRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCatId = itemView.findViewById(R.id.tv_cat_id);
            tvEventCatName = itemView.findViewById(R.id.tv_event_name);
            tvEventCount = itemView.findViewById(R.id.tv_event_count);
            tvIsActive = itemView.findViewById(R.id.tv_isActive);
        }
    }
}