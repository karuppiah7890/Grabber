package com.kruppiah.grabber;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class LinkAdapter extends RecyclerView.Adapter<LinkAdapter.LinkViewHolder>{

    ArrayList<Link> AllLinks;

    LinkAdapter(ArrayList<Link> AllLinks){
        this.AllLinks = AllLinks;
    }

    @Override
    public LinkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.link, parent, false);
        LinkViewHolder lvh = new LinkViewHolder(v);
        return lvh;
    }

    @Override
    public void onBindViewHolder(LinkViewHolder holder, int position) {
        holder.name.setText(AllLinks.get(position).getName());

        long size = AllLinks.get(position).getSize();

        if(size==-100)
        holder.size.setText("");

        else if(size==-1)
            holder.size.setText("Size Unknown");

        else
            holder.size.setText("" + size);

        holder.downloadStatus.setText(AllLinks.get(position).getStatus());
    }

    @Override
    public int getItemCount() {
        return AllLinks.size();
    }

    public static class LinkViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView name;
        TextView size;
        TextView downloadStatus;

        LinkViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            name = (TextView)itemView.findViewById(R.id.tvLinkName);
            size = (TextView)itemView.findViewById(R.id.tvLinkSize);
            downloadStatus = (TextView) itemView.findViewById(R.id.tvLinkDownloadStatus);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}