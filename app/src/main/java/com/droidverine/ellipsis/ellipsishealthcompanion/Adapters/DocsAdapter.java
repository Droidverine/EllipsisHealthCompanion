package com.droidverine.ellipsis.ellipsishealthcompanion.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.droidverine.ellipsis.ellipsishealthcompanion.Models.Messages;
import com.droidverine.ellipsis.ellipsishealthcompanion.Models.Uploaddocs;
import com.droidverine.ellipsis.ellipsishealthcompanion.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DocsAdapter extends RecyclerView.Adapter<DocsAdapter.docsholder> {
    Context context;
    List<Uploaddocs> uploaddocsList;

    public DocsAdapter() {
    }

    public DocsAdapter(Context context, List<Uploaddocs> uploaddocsList) {
        this.context = context;
        this.uploaddocsList = uploaddocsList;
    }

    @NonNull
    @Override
    public docsholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.docs_item_layout,null);
        return new DocsAdapter.docsholder(view);    }

    @Override
    public void onBindViewHolder(@NonNull docsholder holder, int position) {
        holder.docstitle.setText(uploaddocsList.get(position).getName());
        Glide.with(context)
                .load(uploaddocsList.get(position).getImgurl()).centerInside().into(holder.docsimg);
      //  Picasso.with(context).load(uploaddocsList.get(position).getImgurl()).into(holder.docsimg);
      //  holder.docsimg.se(msglist.get(position).getMsghead());
    }

    @Override
    public int getItemCount() {
        return uploaddocsList.size();
    }

    class docsholder extends RecyclerView.ViewHolder
    {   TextView docstitle;
    ImageView docsimg;
        public docsholder(View itemView) {
            super(itemView);
            docstitle=(TextView)itemView.findViewById(R.id.docstitle);
            docsimg=(ImageView)itemView.findViewById(R.id.docsimg);

        }
    }
}
