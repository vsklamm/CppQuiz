package com.vsklamm.cppquiz.ui.favourites;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

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

    private ArrayList<Integer> favouritesIds;

    private final ClickListener listener;

    private final String codeTheme;

    public MyAdapter(@NonNull ArrayList<Integer> favouritesIds, final String codeTheme, ClickListener listener) {
        this.codeTheme = codeTheme;
        this.favouritesIds = favouritesIds;
        this.listener = listener;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public CardView cardView;
        public TextView textView;
        public ImageButton imageButton;
        public HighlightJsView codeView;
        private WeakReference<ClickListener> listenerRef;

        @SuppressLint("ClickableViewAccessibility")
        public MyViewHolder(View itemView, ClickListener clickListener) {
            super(itemView);
            this.cardView = itemView.findViewById(R.id.cv_recycler);
            this.textView = itemView.findViewById(R.id.question_name);
            this.codeView = itemView.findViewById(R.id.highlight_code_card_view);
            this.imageButton = itemView.findViewById(R.id.btn_run_question);

            this.listenerRef = new WeakReference<>(clickListener);

            itemView.setOnClickListener(this);
            this.cardView.setOnClickListener(this);
            this.textView.setOnClickListener(this);
            this.imageButton.setOnClickListener(this);
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

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_favourites, parent, false), listener);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final Resources resources = holder.itemView.getContext().getResources();

        Integer questionId = favouritesIds.get(position);
        holder.textView.setText(String.format(resources.getString(R.string.question_item_text), questionId));
        holder.codeView.setTheme(Theme.valueOf(codeTheme));
        holder.codeView.setHighlightLanguage(Language.C_PLUS_PLUS);

        AppDatabase db = App.getInstance().getDatabase();
        db.questionDao().findById(questionId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<Question>() {
                    @Override
                    public void onSuccess(Question question) {
                        holder.codeView.setSource(question.getCode());
                    }

                    @Override
                    public void onError(Throwable ignored) {
                        holder.codeView.setSource(resources.getString(R.string.retracted_question));
                    }
                });
    }

    @Override
    public int getItemCount() {
        return favouritesIds.size();
    }

    public interface ClickListener {
        void onPositionClicked(int position);
    }
}