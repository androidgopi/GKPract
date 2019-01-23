package com.kireeti.gkpract.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kireeti.gkpract.PackingSlip;
import com.kireeti.gkpract.Quality_FindingActivity;
import com.kireeti.gkpract.R;
import com.kireeti.gkpract.cameraView.CamActivity_QualityFinding;

import java.util.List;

/**
 * Created by kstl on 23-05-2018.
 */

public class PackingSlipListAdapter extends RecyclerView.Adapter<PackingSlipListAdapter.ViewHolder> {

    Context mContext;
    List<PackingSlip> list_packingslip;
    Quality_FindingActivity act;
    CamActivity_QualityFinding cam_act_Qty_Find;

    public PackingSlipListAdapter(Context mContext, List<PackingSlip> list_packingslip) {
        this.mContext = mContext;
        this.list_packingslip = list_packingslip;
    }

    @SuppressLint("NewApi")
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        viewGroup.setClipToPadding(false);
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.packing_slip_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final PackingSlip escalationName = list_packingslip.get(position);
        holder.packing_slip_txt.setText(escalationName.getPackingslipText());

        if(escalationName.isAvailable()){
            holder.imageview_checked.setImageResource(R.drawable.ic_verified);
        }else {
            holder.imageview_checked.setImageResource(R.drawable.ic_cancel_btn);
        }
    }

    public void setCallBack(Quality_FindingActivity act) {
        this.act = act;
    }

    @Override
    public int getItemCount() {
        return list_packingslip.size();
        //return (null != filterList ? filterList.size() : 0);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView packing_slip_txt;
        ImageView imageview_checked;

        public ViewHolder(View view) {
            super(view);
            packing_slip_txt = (TextView) view.findViewById(R.id.packing_slip_txt);
            imageview_checked = (ImageView) view.findViewById(R.id.imageview_checked);

        }
    }
}

