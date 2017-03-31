package in.android2.com.autocompletetextview_demo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IM021 on 3/31/2017.
 */

class MutualFund_CustomAdapter extends BaseAdapter implements Filterable {
    private Context mContext;
    private List<MutualFund_CustomClass> originalList;
    private List<MutualFund_CustomClass> suggestions = new ArrayList<>();
    private Filter filter = new CustomFilter();

    public MutualFund_CustomAdapter(Context context, List<MutualFund_CustomClass> originalList) {
        this.mContext = context;
        this.originalList = originalList;
    }

    @Override
    public int getCount() {
        return suggestions.size();
    }

    @Override
    public MutualFund_CustomClass getItem(int i) {
        return suggestions.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.mutual_fund_list_row, viewGroup, false);
            holder = new ViewHolder();
            holder.autoText = ((TextView) view.findViewById(R.id.textview));
            holder.Id = (TextView) view.findViewById(R.id.textviewId);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.autoText.setText(suggestions.get(i).getFundName());
        holder.Id.setText(suggestions.get(i).getFundId());
        return view;

    }

    private static class ViewHolder {
        TextView autoText;
        TextView Id;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private class CustomFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            suggestions.clear();
            // Check if the Original List and Constraint aren't null.
            if (originalList != null && constraint != null) {
                for (int i = 0; i < originalList.size(); i++) {
                    // Compare item in original list if it contains constraints.
                    if (originalList.get(i).getFundName().toLowerCase().contains(constraint)) {
                        // If TRUE add item in Suggestions.
                        suggestions.add(originalList.get(i));
                    }
                }
            }
            // Create new Filter Results and return this to publishResults;
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
}
