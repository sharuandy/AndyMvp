package com.andy.andymvp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andy.andymvp.data.RowsData;
import com.andy.andymvp.utils.AppLog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RvListAdapter extends RecyclerView.Adapter<RvListAdapter.MyViewHolder> {
    private Context _thisContext;
    private List<RowsData> rowList = new ArrayList<>();
    private final String TAG = "RvListAdapter";

    public RvListAdapter(Context context, List<RowsData> list) {
        this._thisContext = context;
        this.rowList = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        try {
            RowsData rowsData = rowList.get(position);
            holder.tvTitle.setText(rowsData.getTitle());
            holder.tvDescription.setText(rowsData.getDescription());
            Picasso.get().load(rowsData.getImageHref()).resize((int) _thisContext.getResources().getDimension(R.dimen.image_width), (int) _thisContext.getResources().getDimension(R.dimen.image_height)).error(R.drawable.ic_launcher_background).noFade().placeholder(R.drawable.ic_launcher_background).into(holder.ivRef);
        } catch (Exception e) {
            AppLog.e(TAG, e);
        }
    }

    @Override
    public int getItemCount() {
        return rowList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_description)
        TextView tvDescription;
        @BindView(R.id.iv_reference)
        ImageView ivRef;

        public MyViewHolder(View itemView) {
            super(itemView);
            //Inintializes ButterKnife and binds the view
            ButterKnife.bind(this, itemView);
        }
    }
}