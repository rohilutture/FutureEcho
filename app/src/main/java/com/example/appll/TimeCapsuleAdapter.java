package com.example.appll;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;



public class TimeCapsuleAdapter extends RecyclerView.Adapter<TimeCapsuleAdapter.TimeCapsuleViewHolder> {

    private Context context;
    private ArrayList<MessageData> messageList;

    public TimeCapsuleAdapter(Context context, ArrayList<MessageData> messageList) {
        this.context = context;
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public TimeCapsuleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new TimeCapsuleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeCapsuleViewHolder holder, int position) {
        MessageData messageData = messageList.get(position);

        // Set title and message
        holder.title.setText(messageData.getTitle());
        holder.message.setText(messageData.getMessage());

        // Set date and time
        holder.dateTime.setText(messageData.getDateTime());

        // Load image using Glide or Picasso
        Glide.with(context)
                .load(messageData.getImageUri())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class TimeCapsuleViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView message;
        TextView dateTime;
        ImageView imageView;

        public TimeCapsuleViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            message = itemView.findViewById(R.id.message);
            dateTime = itemView.findViewById(R.id.dateTime);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
