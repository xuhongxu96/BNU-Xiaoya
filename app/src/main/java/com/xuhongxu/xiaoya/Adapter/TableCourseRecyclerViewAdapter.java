package com.xuhongxu.xiaoya.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import com.xuhongxu.Assist.TableCourse;
import com.xuhongxu.xiaoya.Fragment.TableCourseFragment;
import com.xuhongxu.xiaoya.R;

/**
 * {@link RecyclerView.Adapter} that can display a {@link TableCourse} and makes a call to the
 * specified {@link com.xuhongxu.xiaoya.Fragment.TableCourseFragment.OnListFragmentInteractionListener}.
 */
public class TableCourseRecyclerViewAdapter extends RecyclerView.Adapter<TableCourseRecyclerViewAdapter.ViewHolder> {

    private final List<TableCourse> mValues;
    private final TableCourseFragment.OnListFragmentInteractionListener mListener;

    public TableCourseRecyclerViewAdapter(List<TableCourse> items, TableCourseFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_table_course, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mContentView.setText(mValues.get(position).getName());
        holder.mDetailView.setText(mValues.get(position).getLocationTime());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;
        public final TextView mDetailView;
        public TableCourse mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view.findViewById(R.id.item);
            mContentView = (TextView) view.findViewById(R.id.name);
            mDetailView = (TextView) view.findViewById(R.id.details);
        }
    }
}
