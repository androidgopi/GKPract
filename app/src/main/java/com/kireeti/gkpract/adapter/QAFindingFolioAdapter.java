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

import com.kireeti.gkpract.Quality_FindingActivity;
import com.kireeti.gkpract.R;
import com.kireeti.gkpract.models.QAFindingFolio;

import java.util.ArrayList;
import java.util.List;


public class QAFindingFolioAdapter extends BaseAdapter implements Filterable {

    Context mContext;
    List<QAFindingFolio> details;
    LayoutInflater inflator;
    private Filter filter = new CustomFilter();
    private List<QAFindingFolio> suggestions;
    private QAFindingFolio pholioObj;
    private Quality_FindingActivity callbacks;
    private String Warehouse_code;

    public QAFindingFolioAdapter(Context mContext, List<QAFindingFolio> details) {

        this.mContext = mContext;
        this.details = details;

        this.suggestions = new ArrayList<QAFindingFolio>();
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

        holder.trailer_txt.setText(pholioObj.getQFFolio());
        holder.trailer_txt.setTag(pholioObj);
        holder.trailer_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbacks.qaFindingAuto((QAFindingFolio) v.getTag());
            }
        });

        return convertView;
    }


    @Override
    public Filter getFilter() {
        return filter;
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
                    if (details.get(i).getQFFolio().toLowerCase().contains(constraint)) {
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

    public void setCallBack(Quality_FindingActivity activity) {
        this.callbacks = activity;
    }
}
