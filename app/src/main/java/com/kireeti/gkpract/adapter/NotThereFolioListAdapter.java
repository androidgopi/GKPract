package com.kireeti.gkpract.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kireeti.gkpract.NotThereActivity;
import com.kireeti.gkpract.R;
import com.kireeti.gkpract.cameraView.CamActivity_QualityFinding;
import com.kireeti.gkpract.models.NotThere;

import java.util.List;

/**
 * Created by kstl on 23-05-2018.
 */

public class NotThereFolioListAdapter extends RecyclerView.Adapter<NotThereFolioListAdapter.ViewHolder> {

    Context mContext;
    List<NotThere> escalationList;
    NotThereActivity act;
    CamActivity_QualityFinding cam_act_Qty_Find;

    public NotThereFolioListAdapter(Context mContext, List<NotThere> escalationList) {
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

        final NotThere escalationName = escalationList.get(position);
        holder.folio_name.setText(escalationName.getNTFolioStr());

        holder.folio_name.setTag(escalationName);
        holder.folio_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act.callsetFoliolist((NotThere) view.getTag());

            }
        });

    }

    public void setCallBack(NotThereActivity act) {
        this.act = act;
    }

    @Override
    public int getItemCount() {
        return escalationList.size();
        //return (null != filterList ? filterList.size() : 0);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView folio_name,folio_comment;
        LinearLayout folio_layout;

        public ViewHolder(View view) {
            super(view);
            folio_name = (TextView) view.findViewById(R.id.escalation_name);
            folio_layout=(LinearLayout) view.findViewById(R.id.folio_layout);
        }
    }
}

