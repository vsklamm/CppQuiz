package com.vsklamm.cppquiz.ui.favourites;

import android.app.Activity;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pddstudio.highlightjs.HighlightJsView;
import com.pddstudio.highlightjs.models.Language;
import com.pddstudio.highlightjs.models.Theme;
import com.vsklamm.cppquiz.App;
import com.vsklamm.cppquiz.R;
import com.vsklamm.cppquiz.data.Question;
import com.vsklamm.cppquiz.data.database.AppDatabase;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private ArrayList<Integer> mDataset;

    private final ClickListener listener;

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_view_favourites, parent, false), listener);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public CardView mCardView;
        public TextView mTextView;
        public HighlightJsView mCodeView;
        private WeakReference<ClickListener> listenerRef;

        public MyViewHolder(View itemView, ClickListener clickListener) {
            super(itemView);
            this.mCardView = itemView.findViewById(R.id.cv_recycler);
            this.mTextView = itemView.findViewById(R.id.question_name);
            this.mCodeView = itemView.findViewById(R.id.highlight_code_card_view);

            this.listenerRef = new WeakReference<>(clickListener);

            this.mCardView.setOnClickListener(this);
            this.mTextView.setOnClickListener(this);
            this.mCodeView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listenerRef.get().onPositionClicked( getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            return false;
        }
    }

    public MyAdapter(@NonNull ArrayList<Integer> myDataset, ClickListener listener) {
        this.mDataset = myDataset;
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        Resources resources = holder.itemView.getContext().getResources();

        Integer questionId = mDataset.get(position);
        holder.mTextView.setText(String.format(resources.getString(R.string.question_item_text),
                questionId));

        AppDatabase db = App.getInstance().getDatabase();

        db.questionDao().findById(questionId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<Question>() {
                    @Override
                    public void onSuccess(Question question) {
                        holder.mCodeView.setTheme(Theme.GITHUB);
                        holder.mCodeView.setHighlightLanguage(Language.C_PLUS_PLUS);
                        holder.mCodeView.setSource(question.getCode());
                    }

                    @Override
                    public void onError(Throwable e) {
                        // ignored
                    }
                });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public interface ClickListener {
        void onPositionClicked(int position);
    }
}