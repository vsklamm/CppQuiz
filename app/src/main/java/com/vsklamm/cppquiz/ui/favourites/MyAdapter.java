package com.vsklamm.cppquiz.ui.favourites;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vsklamm.cppquiz.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.SortedSet;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private ArrayList<Integer> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        //public CardView mCardView;
        public TextView mTextView;
        public MyViewHolder(/*CardView v,*/ TextView v2) {
            super(v2);
          //  mCardView = v;
            mTextView = v2;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList<Integer> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        /*CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_favourites, parent, false);*/
        TextView v2 = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.favourite_question_text, parent, false);
        MyViewHolder vh = new MyViewHolder(/*v, */v2);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(mDataset.get(position).toString());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}