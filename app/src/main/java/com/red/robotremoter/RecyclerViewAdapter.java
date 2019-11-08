package com.red.robotremoter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.red.robotremoter.bluetooth.BTLE_Device;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private Context context;
    private HashMap<String, BTLE_Device> devices;

    {

    }

    public RecyclerViewAdapter(Context context, HashMap<String, BTLE_Device> devices) {
        this.context = context;
        this.devices = devices;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listview, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        List<BTLE_Device> list = new ArrayList<>(devices.values());
        holder.nameView.setText(list.get(position).getName());
        holder.macView.setText(list.get(position).getAddress());
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    @Override
    public int getItemCount() {
        return devices.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView macView, nameView;
        private RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            macView = itemView.findViewById(R.id.mac);
            nameView = itemView.findViewById(R.id.name);
            parentLayout = itemView.findViewById(R.id.parentLayout);

        }
    }
}
