package com.example.sanyo.group24_inclass13;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sanyo on 4/24/2018.
 */

public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.ViewHolder>  {

    String username;
    ArrayList<Email> emails;


    public InboxAdapter(ArrayList<Email> emails, String username) {
        this.emails = emails;
        this.username = username;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inbox_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Email email = emails.get(position);
        holder.tVSender.setText(email.sender);
        holder.email = email;
        holder.tVMessage.setText(email.message);

        holder.tVSentAt.setText(email.date);
        holder.position = position;

        if(email.isRead.equals("true")){
            BitmapDrawable bitmapDrawable = (BitmapDrawable) holder.imageViewRead.getDrawable();
            Bitmap b = bitmapDrawable.getBitmap().copy(Bitmap.Config.ARGB_8888,true);;
            b.eraseColor(Color.GRAY);
            holder.imageViewRead.setImageBitmap(b);
        }
        else{
            BitmapDrawable bitmapDrawable = (BitmapDrawable) holder.imageViewRead.getDrawable();
            Bitmap b = bitmapDrawable.getBitmap().copy(Bitmap.Config.ARGB_8888,true);
            b.eraseColor(Color.BLUE);
            holder.imageViewRead.setImageBitmap(b);
        }
    }

    @Override
    public int getItemCount() {
        return emails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tVSender;
        TextView tVSentAt;
        TextView tVMessage;
        ImageView imageViewRead;
        Email email;
        int position;

        public ViewHolder(final View itemView) {
            super(itemView);

            tVSender = itemView.findViewById(R.id.textViewSender);
            tVMessage = itemView.findViewById(R.id.textViewMessage);
            tVSentAt = itemView.findViewById(R.id.textViewTime);
            imageViewRead = itemView.findViewById(R.id.imageViewRead);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(itemView.getContext(), ReadMessageActivity.class);

                    intent.putExtra("EMAIL", email);
                    intent.putExtra("USERNAME",username);
                    Log.d("demo", "Indide InboxAdapter"+username);

                    itemView.getContext().startActivity(intent);

                    ((Activity)itemView.getContext()).finish();
                }
            });


        }
    }


}
