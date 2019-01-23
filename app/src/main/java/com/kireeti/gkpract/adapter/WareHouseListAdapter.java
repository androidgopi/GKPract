package com.kireeti.gkpract.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kireeti.gkpract.Damaged_PhotoUploadActivity;
import com.kireeti.gkpract.GatePassActivity;
import com.kireeti.gkpract.HomeActivity;
import com.kireeti.gkpract.NotThereActivity;
import com.kireeti.gkpract.Quality_FindingActivity;
import com.kireeti.gkpract.R;
import com.kireeti.gkpract.Receiving_Photo_UploadActivity;
import com.kireeti.gkpract.Shipping_Photo_UploadActivity;
import com.kireeti.gkpract.Warehouse_PhotoUploadActivity;
import com.kireeti.gkpract.models.Warehouse;

import java.util.ArrayList;
import java.util.List;


public class WareHouseListAdapter extends BaseAdapter implements Filterable {

    Context mContext;
    List<Warehouse> details;
    LayoutInflater inflator;
    private Filter filter = new CustomFilter();
    private List<Warehouse> suggestions;
    private Warehouse pholioObj;
    private HomeActivity callbacks;
    private Receiving_Photo_UploadActivity reciving_activity_calback;
    private Damaged_PhotoUploadActivity damaged_activity_callback;
    private Warehouse_PhotoUploadActivity warehouse_activity_callback;
    private Shipping_Photo_UploadActivity shipping_activity_callback;
    private Quality_FindingActivity qualityfinding_activity_callback;
    private NotThereActivity notthere_activity_callback;
    private GatePassActivity gatepass_activity_callback;

    private String Warehouse_code;

    public WareHouseListAdapter(Context mContext, List<Warehouse> details) {

        this.mContext = mContext;
        this.details = details;

        this.suggestions = new ArrayList<Warehouse>();
        this.suggestions.addAll(this.details);
    }


    @Override
    public int getCount() {
        return suggestions.size();
    }

    @Override
    public String getItem(int position) {
        return suggestions.get(position).getValue();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Viewholder holder = null;


        inflator = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        pholioObj = suggestions.get(position);
        if (convertView == null) {
            holder = new Viewholder();
            convertView = inflator.inflate(R.layout.trailer_view, null);

            holder.trailer_txt = (TextView) convertView.findViewById(R.id.trailer_txt);
            convertView.setTag(holder);
        }

        holder = (Viewholder) convertView.getTag();

        holder.trailer_txt.setText(pholioObj.getName());
        holder.trailer_txt.setTag(pholioObj);
        holder.trailer_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mContext instanceof HomeActivity) {
                    callbacks.wareHouseAuto((Warehouse) v.getTag());
                }
                else if(mContext instanceof Receiving_Photo_UploadActivity){
                    reciving_activity_calback.wareHouseAuto((Warehouse) v.getTag());
                }
                else if(mContext instanceof Damaged_PhotoUploadActivity){
                    damaged_activity_callback.wareHouseAuto((Warehouse) v.getTag());
                }
                else if(mContext instanceof Warehouse_PhotoUploadActivity){
                    warehouse_activity_callback.wareHouseAuto((Warehouse) v.getTag());
                }
                else if(mContext instanceof Shipping_Photo_UploadActivity){
                    shipping_activity_callback.wareHouseAuto((Warehouse) v.getTag());
                }
                else if(mContext instanceof Quality_FindingActivity){
                    qualityfinding_activity_callback.wareHouseAuto((Warehouse) v.getTag());
                }
                else if(mContext instanceof NotThereActivity){
                    notthere_activity_callback.wareHouseAuto((Warehouse) v.getTag());
                }
                else if(mContext instanceof GatePassActivity){
                  gatepass_activity_callback.wareHouseAuto((Warehouse) v.getTag());
                }


            }
        });

        return convertView;
    }


    @Override
    public Filter getFilter() {
        return filter;
    }

    public void setCallBack(Receiving_Photo_UploadActivity receiving_photo_uploadActivity) {

        this.reciving_activity_calback = receiving_photo_uploadActivity;
    }

    public void setCallBack(Damaged_PhotoUploadActivity callBack) {
        this.damaged_activity_callback = callBack;
    }

    public void setCallBack(Warehouse_PhotoUploadActivity callBack) {
        this.warehouse_activity_callback = callBack;
    }

    public void setCallBack(Shipping_Photo_UploadActivity callBack) {
        this.shipping_activity_callback = callBack;
    }

    public void setCallBack(Quality_FindingActivity callBack) {
        this.qualityfinding_activity_callback = callBack;
    }

    public void setCallBack(NotThereActivity callBack) {
        this.notthere_activity_callback = callBack;
    }

    public void setCallBack(GatePassActivity callBack) {
        this.gatepass_activity_callback = callBack;
    }

    class Viewholder {
        TextView trailer_txt;
        LinearLayout cardView;
    }

    private class CustomFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            suggestions.clear();

            if (details != null && constraint != null) {
                for (int i = 0; i < details.size(); i++) {
                    if (details.get(i).getName().toLowerCase().contains(constraint)) {
                        suggestions.add(details.get(i));
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = suggestions;
            results.count = suggestions.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }

    public void setCallBack(HomeActivity activity) {
        this.callbacks = activity;
    }
}
