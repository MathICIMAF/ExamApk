package com.alkilerprueba.amg.examapk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import io.github.kexanie.library.MathView;

/**
 * Created by AMG on 26/01/2020.
 */
public class MathAdapter extends RecyclerView.Adapter<MathAdapter.MathViewHolder> {

    private List<Exercise> items;
    private Context context;

    public MathAdapter(Context context,List<Exercise> items) {
        this.context = context;
        this.items = items;
    }


    @Override
    public MathAdapter.MathViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new MathViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MathAdapter.MathViewHolder holder, int position) {
        //initializeView(holder.value);
        holder.value.setText(position+" "+items.get(position).getSolution());
    }

    private void initializeView(MathView entry){
        entry.getSettings().setLoadWithOverviewMode(true);
        entry.getSettings().setUseWideViewPort(true);
        entry.getSettings().setMinimumFontSize(50);
        entry.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        entry.setScrollbarFadingEnabled(false);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class MathViewHolder extends RecyclerView.ViewHolder{

        public MathView value;
        public MathViewHolder(View itemView) {
            super(itemView);
            value = (MathView) itemView.findViewById(R.id.item);
        }
    }
}
