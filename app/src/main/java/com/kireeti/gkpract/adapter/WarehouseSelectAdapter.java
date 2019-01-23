package com.kireeti.gkpract.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kireeti.gkpract.Quality_FindingActivity;
import com.kireeti.gkpract.R;
import com.kireeti.gkpract.cameraView.CamActivity_QualityFinding;
import com.kireeti.gkpract.models.WarehouseResult;

import java.util.List;

/**
 * Created by kstl on 23-05-2018.
 */

public class WarehouseSelectAdapter extends RecyclerView.Adapter<WarehouseSelectAdapter.ViewHolder> {

    Context mContext;
    List<WarehouseResult> escalationList;
    Quality_FindingActivity act;
    CamActivity_QualityFinding cam_act_Qty_Find;

    public WarehouseSelectAdapter(Context mContext, List<WarehouseResult> escalationList) {
        this.mContext = mContext;
        this.escalationList = escalationList;
    }

    @SuppressLint("NewApi")
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        viewGroup.setClipToPadding(false);
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.warehouse_selection_lyt, viewGroup, false);
        return new ViewHolder(view);
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final WarehouseResult escalationName = escalationList.get(position);
        holder.escalation_name.setText(escalationName.getWarehouseName());


        holder.escalation_name.setTag(escalationName);
        holder.escalation_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act.callsetData((WarehouseResult) view.getTag());

            }
        });


    }

    public void setCallBack(Quality_FindingActivity act) {
        this.act = act;
    }

    @Override
    public int getItemCount() {
        return escalationList.size();
        //return (null != filterList ? filterList.size() : 0);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView escalation_name;

        public ViewHolder(View view) {
            super(view);
            escalation_name = (TextView) view.findViewById(R.id.escalation_name);
        }
    }
}

