package com.vsklamm.cppquiz.ui.explanation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.pddstudio.highlightjs.HighlightJsView;
import com.pddstudio.highlightjs.models.Language;
import com.pddstudio.highlightjs.models.Theme;
import com.vsklamm.cppquiz.R;
import com.vsklamm.cppquiz.data.Question;
import com.vsklamm.cppquiz.utils.ActivityUtils;
import com.vsklamm.cppquiz.utils.ResultBehaviourType;

import ru.noties.markwon.Markwon;

import static com.vsklamm.cppquiz.ui.main.MainActivity.APP_PREFERENCES;
import static com.vsklamm.cppquiz.ui.main.MainActivity.APP_PREF_LINE_NUMBERS;
import static com.vsklamm.cppquiz.ui.main.MainActivity.APP_PREF_ZOOM;
import static com.vsklamm.cppquiz.ui.main.MainActivity.IS_GIVE_UP;
import static com.vsklamm.cppquiz.ui.main.MainActivity.QUESTION;
import static com.vsklamm.cppquiz.ui.main.MainActivity.THEME;
import static com.vsklamm.cppquiz.utils.ActivityUtils.APP_THEME_IS_DARK;

public class ExplanationActivity extends AppCompatActivity {

    HighlightJsView codeView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        SharedPreferences appPreferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        ActivityUtils.setUpTheme(this, appPreferences);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explanation);

        ScrollView scrollView = findViewById(R.id.scroll_explanation);
        boolean isDark = appPreferences.getBoolean(APP_THEME_IS_DARK, false);
        int background = isDark ? R.drawable.style_dark_triangular_halves : R.drawable.style_triangular_halves;
        scrollView.setBackground(getDrawable(background));

        Intent intent = getIntent();
        final boolean isGiveUp = intent.getBooleanExtra(IS_GIVE_UP, false);
        final Question question = (Question) intent.getSerializableExtra(QUESTION);

        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            String title;
            if (isGiveUp) {
                title = String.format(getResources().getString(R.string.toolbar_give_up), question.getId());
            } else {
                title = String.format(getResources().getString(R.string.toolbar_correct), question.getId());
            }
            ab.setTitle(title);
        }

        final String theme = appPreferences.getString(THEME, "GITHUB");
        codeView = findViewById(R.id.highlight_view_card_view_2);
        codeView.setTheme(Theme.valueOf(theme));
        codeView.setHighlightLanguage(Language.C_PLUS_PLUS);
        codeView.setSource(question.getCode());

        onZoomSupportToggled(appPreferences.getBoolean(APP_PREF_ZOOM, false));
        onShowLineNumbersToggled(appPreferences.getBoolean(APP_PREF_LINE_NUMBERS, false));

        TextView tvAnswer = findViewById(R.id.tv_answer);
        TextView tvExplanation = findViewById(R.id.tv_explanation);

        String[] results = getResources().getStringArray(R.array.result_behaviour_type);
        String resultText = results[question.getResult().ordinal()];
        String answerText = (question.getResult() == ResultBehaviourType.OK) ? (" `" + question.getAnswer() + "`") : "";

        /*int color;
        if (isGiveUp) {
            color = ContextCompat.getColor(this, R.color.git_background);
        } else {
            color = ContextCompat.getColor(this, R.color.explanation_correct);
        }
        tvAnswer.setBackgroundColor(color);
        tvExplanation.setBackgroundColor(color);*/

        Markwon.setMarkdown(tvAnswer, resultText + answerText);
        Markwon.setMarkdown(tvExplanation, question.getExplanation());

        Button btnNextQuestion = findViewById(R.id.btn_next_question);
        btnNextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_FIRST_USER);
                finish();
            }
        });
    }

    private void onShowLineNumbersToggled(final boolean enableLineNumbers) {
        codeView.setShowLineNumbers(enableLineNumbers);
        codeView.refresh();
    }

    private void onZoomSupportToggled(final boolean enableZooming) {
        codeView.setZoomSupportEnabled(enableZooming);
        codeView.refresh();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
