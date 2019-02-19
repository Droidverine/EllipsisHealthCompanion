package com.droidverine.ellipsis.ellipsishealthcompanion.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.droidverine.ellipsis.ellipsishealthcompanion.Models.Messages;
import com.droidverine.ellipsis.ellipsishealthcompanion.R;

import java.util.Calendar;
import java.util.List;


/**
 * Created by DELL on 25-11-2017.
 */

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.msgholder>
{
    Context context;
    List<Messages> msglist;
    public MessagesAdapter()
    {

    }
    public MessagesAdapter(Context context, List<Messages> msglist)
    {
        this.msglist=msglist;
        this.context=context;

    }
    @Override
    public MessagesAdapter.msgholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.messagelayout,null);
        return new msgholder(view);
    }

    @Override
    public void onBindViewHolder(MessagesAdapter.msgholder holder, int position) {
        holder.msgtxt.setText(msglist.get(position).getMsgtext());
        holder.msgtitle.setText(msglist.get(position).getMsghead());
        Calendar now = Calendar.getInstance();


        holder.msgtime.setText(DateFormat.format("HH:mm",msglist.get(position).getMsgtime()));

    }

    @Override
    public int getItemCount() {
        return msglist.size();
    }
    class msgholder extends RecyclerView.ViewHolder
    {   TextView msgtitle,msgtxt,msgtime;
        public msgholder(View itemView) {
            super(itemView);
            msgtitle=(TextView)itemView.findViewById(R.id.msgtitle);
            msgtxt=(TextView)itemView.findViewById(R.id.msgdata);
            msgtime=(TextView)itemView.findViewById(R.id.msgtime);

        }
    }
}