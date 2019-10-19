package com.vsklamm.cppquiz.ui.explanation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.pddstudio.highlightjs.HighlightJsView;
import com.pddstudio.highlightjs.models.Language;
import com.pddstudio.highlightjs.models.Theme;
import com.vsklamm.cppquiz.R;
import com.vsklamm.cppquiz.data.model.Question;
import com.vsklamm.cppquiz.utils.ActivityUtils;
import com.vsklamm.cppquiz.utils.ResultBehaviourType;
import com.vsklamm.cppquiz.utils.StandardLinksUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.noties.markwon.Markwon;

import static com.vsklamm.cppquiz.ui.main.MainActivity.APP_PREFERENCES;
import static com.vsklamm.cppquiz.ui.main.MainActivity.APP_PREF_LINE_NUMBERS;
import static com.vsklamm.cppquiz.ui.main.MainActivity.APP_PREF_ZOOM;
import static com.vsklamm.cppquiz.ui.main.MainActivity.IS_GIVE_UP;
import static com.vsklamm.cppquiz.ui.main.MainActivity.QUESTION;
import static com.vsklamm.cppquiz.ui.main.MainActivity.THEME;

public class ExplanationActivity extends AppCompatActivity {

    @BindView(R.id.tv_answer)
    TextView tvAnswer;
    @BindView(R.id.tv_explanation)
    TextView tvExplanation;
    @BindView(R.id.btn_next_question)
    Button btnNextQuestion;
    @BindView(R.id.highlight_view_card_view_2)
    HighlightJsView codeView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        SharedPreferences appPreferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        ActivityUtils.setUpTheme(this, appPreferences);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explanation);
        ButterKnife.bind(this);

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
        codeView.setTheme(Theme.valueOf(theme));
        codeView.setHighlightLanguage(Language.C_PLUS_PLUS);
        codeView.setSource(question.getCode());

        onZoomSupportToggled(appPreferences.getBoolean(APP_PREF_ZOOM, false));
        onShowLineNumbersToggled(appPreferences.getBoolean(APP_PREF_LINE_NUMBERS, false));

        final String[] results = getResources().getStringArray(R.array.result_behaviour_type);
        final String resultText = results[question.getResult().ordinal()];
        final String answerText = (question.getResult() == ResultBehaviourType.OK) ? (" `" + question.getAnswer() + "`") : "";

        Markwon.setMarkdown(tvAnswer, resultText + answerText);
        Markwon.setMarkdown(tvExplanation, StandardLinksUtils.addLinks(question.getExplanation()));

        btnNextQuestion.setOnClickListener(v -> {
            setResult(RESULT_FIRST_USER);
            finish();
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
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
