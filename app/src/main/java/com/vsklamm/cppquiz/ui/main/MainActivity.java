package com.vsklamm.cppquiz.ui.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.pddstudio.highlightjs.HighlightJsView;
import com.pddstudio.highlightjs.models.Language;
import com.pddstudio.highlightjs.models.Theme;
import com.sackcentury.shinebuttonlib.ShineButton;
import com.vsklamm.cppquiz.App;
import com.vsklamm.cppquiz.R;
import com.vsklamm.cppquiz.data.model.Question;
import com.vsklamm.cppquiz.data.model.UserData;
import com.vsklamm.cppquiz.data.model.UsersAnswer;
import com.vsklamm.cppquiz.api.CppQuizLiteApi;
import com.vsklamm.cppquiz.data.database.AppDatabase;
import com.vsklamm.cppquiz.data.prefs.SharedPreferencesHelper;
import com.vsklamm.cppquiz.loader.DumpLoader;
import com.vsklamm.cppquiz.ui.about.AboutActivity;
import com.vsklamm.cppquiz.ui.dialogs.ConfirmHintDialog;
import com.vsklamm.cppquiz.ui.dialogs.ConfirmResetDialog;
import com.vsklamm.cppquiz.ui.dialogs.GoToDialog;
import com.vsklamm.cppquiz.ui.dialogs.ThemeChangerDialog;
import com.vsklamm.cppquiz.ui.explanation.ExplanationActivity;
import com.vsklamm.cppquiz.ui.favourites.FavouritesActivity;
import com.vsklamm.cppquiz.ui.main.quiz.QuizView;
import com.vsklamm.cppquiz.ui.main.training.TrainingView;
import com.vsklamm.cppquiz.utils.ActivityUtils;
import com.vsklamm.cppquiz.utils.DeepLinksUtils;
import com.vsklamm.cppquiz.utils.FlipperChild;
import com.vsklamm.cppquiz.utils.RequestType;
import com.vsklamm.cppquiz.utils.ResultBehaviourType;
import com.vsklamm.cppquiz.utils.TimeWork;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import ru.noties.markwon.Markwon;

import static com.vsklamm.cppquiz.ui.main.GameLogic.CPP_STANDARD;
import static com.vsklamm.cppquiz.utils.ActivityUtils.APP_THEME_IS_DARK;
import static com.vsklamm.cppquiz.utils.TimeWork.LAST_UPDATE;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        HighlightJsView.OnThemeChangedListener,
        ThemeChangerDialog.ThemeChangeListener,
        LoaderManager.LoaderCallbacks<LoadResult<String, LinkedHashSet<Integer>>>,
        ConfirmHintDialog.DialogListener,
        GoToDialog.DialogListener,
        ConfirmResetDialog.DialogListener,
        // QuizModeContract.View,
        // TrainingModeContract.View,
        GameLogic.GameLogicCallbacks,
        View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    public static final String APP_PREFERENCES = "APP_PREFERENCES", APP_PREF_ZOOM = "APP_PREF_ZOOM";
    public static final String APP_PREF_LINE_NUMBERS = "APP_PREF_LINE_NUMBERS", THEME = "THEME";
    public static final String REQUEST_TYPE = "REQUEST_TYPE", IS_GIVE_UP = "IS_GIVE_UP", QUESTION = "QUESTION";
    public static final String GAME_MODE = "GAME_MODE", QUIZ_MODE = "QUIZ_MODE", TRAINING_MODE = "TRAINING_MODE";
    private static final String HAS_VISITED = "HAS_VISITED";
    private static final String USER_ANSWER = "USER_ANSWER";
    public static final int EXPLANATION_ACTIVITY = 0, FAVOURITES_ACTIVITY = 1;

    private SharedPreferences appPreferences;

    private ConfirmHintDialog confirmHintDialog;
    private GoToDialog goToDialog;
    private ConfirmResetDialog resetDialog;
    private ThemeChangerDialog themeChangerDialog;

    private ViewFlipper viewFlipper;
    private Toolbar toolbar;

    private HighlightJsView codeView;
    private ExpandableLayout expandableHint;
    public TextView progressTextViewLoading;
    private ShineButton shineButton;

    private GamePresenter gamePresenter;
    private TrainingView trainingView;
    private QuizView quizView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        appPreferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        ActivityUtils.setUpThemeNoActionBar(this, appPreferences);
        super.onCreate(savedInstanceState);

        // View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        // gamePresenter = new GameModeFactory.getPresenter(this, rootView, appPreferences.getString(GAME_MODE, TRAINING_MODE));

        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar); // TODO: disable OverflowMenu on non-MainContent views
        setSupportActionBar(toolbar);

        viewFlipper = findViewById(R.id.view_flipper_main);
        progressTextViewLoading = findViewById(R.id.tv_progress);

        boolean hasVisited = appPreferences.getBoolean(HAS_VISITED, false);
        if (!hasVisited) {
            viewFlipper.setDisplayedChild(FlipperChild.LOADING_VIEW.ordinal());
            initDumpLoader(RequestType.LOAD_DUMP);
        } else {
            if (TimeWork.isNextDay(appPreferences)) {
                initDumpLoader(RequestType.UPDATE);
            }

            AppDatabase db = App.getInstance().getDatabase();
            db.questionDao().getAllIds()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableSingleObserver<List<Integer>>() {
                        @Override
                        public void onSuccess(List<Integer> questionIds) {
                            GameLogic gameLogic = GameLogic.getInstance();
                            String cppStandard = appPreferences.getString(CPP_STANDARD, "C++17"); // for many years
                            gameLogic.initNewData(
                                    MainActivity.this,
                                    cppStandard,
                                    new LinkedHashSet<>(questionIds));
                             showFirstQuestion();
                        }

                        @Override
                        public void onError(Throwable e) {
                            // ignore
                        }
                    });
        }

        /* HIGHLIGHT JS VIEW */
        String theme = appPreferences.getString(THEME, "GITHUB");
        codeView = findViewById(R.id.highlight_view_card_view);
        codeView.setOnThemeChangedListener(this);
        codeView.setTheme(Theme.valueOf(theme));
        codeView.setHighlightLanguage(Language.C_PLUS_PLUS);

        /* EXPANSION PANELS */
        expandableHint = findViewById(R.id.expandable_hint);
        findViewById(R.id.expand_header_hint).setOnClickListener(this);
        expandableHint.setOnExpansionUpdateListener(new ExpandableLayout.OnExpansionUpdateListener() {
            @Override
            public void onExpansionUpdate(float expansionFraction, int state) {
                TextView expandHeaderHint = findViewById(R.id.expand_header_hint);
                if (expandableHint.isExpanded()) {
                    NestedScrollView nestedScrollView = findViewById(R.id.nested_scroll_view);
                    nestedScrollView.smoothScrollTo(0, nestedScrollView.getBottom());
                    expandHeaderHint.setText(R.string.hide_hint);
                } else {
                    expandHeaderHint.setText(R.string.show_hint);
                }
            }
        });

        /* APP SETTINGS */
        onZoomSupportToggled(appPreferences.getBoolean(APP_PREF_ZOOM, false));
        onShowLineNumbersToggled(appPreferences.getBoolean(APP_PREF_LINE_NUMBERS, false));

        /* DRAWER LAYOUT */
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if (viewFlipper.getDisplayedChild() != FlipperChild.MAIN_CONTENT.ordinal()) {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final MenuItem item = navigationView.getMenu().findItem(R.id.dark_side);
        final Switch mySwitch = item.getActionView().findViewById(R.id.switch_app_theme);
        mySwitch.setChecked(appPreferences.getBoolean(APP_THEME_IS_DARK, false));
        mySwitch.setOnCheckedChangeListener(this);

        /* BUTTON RANDOM */
        Button btnRandom = findViewById(R.id.btn_random);
        btnRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gamePresenter.nextQuestion();
            }
        });

        /* BUTTON HINT */
        Button btnHint = findViewById(R.id.btn_hint);
        btnHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (findViewById(R.id.expansion_panel_outer).getVisibility() == View.GONE) {
                    openConfirmDialog();
                } else expandableHint.expand();
            }
        });

        /* BUTTON GIVE UP */
        final Button btnGiveUp = findViewById(R.id.btn_give_up);
        btnGiveUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gamePresenter.giveUp();
            }
        });

        /* BUTTON / EDIT_TEXT/ SPINNER ANSWER */
        final FloatingActionButton btnAnswer = findViewById(R.id.btn_answer);
        final EditText etAnswer = findViewById(R.id.et_card_view_answer);
        final Spinner spResult = findViewById(R.id.sp_main_result);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.spinner_item,
                getResources().getStringArray(R.array.result_behaviour_type)
        );
        spResult.setAdapter(adapter);
        spResult.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {
                etAnswer.setEnabled(selectedItemPosition == 0); // OK behaviour type
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btnAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //GameLogic gameLogic = GameLogic.getInstance();
                UserData.getInstance().givenAnswer = new UsersAnswer(
                        gamePresenter.getCurrentQuestion().getId(),
                        ResultBehaviourType.getType(spResult.getSelectedItemPosition()),
                        etAnswer.getText().toString()
                );
                gamePresenter.checkAnswer();
                //gameLogic.checkAnswer();
            }
        });

        Button buttonRetry = findViewById(R.id.btn_retry);
        buttonRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFlipper.setDisplayedChild(FlipperChild.LOADING_VIEW.ordinal());
                Bundle args = new Bundle();
                args.putInt(REQUEST_TYPE, RequestType.LOAD_DUMP.ordinal());
                getSupportLoaderManager().restartLoader(0, args, MainActivity.this);
            }
        });

        Button buttonResetProgress = findViewById(R.id.btn_reset);
        buttonResetProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserData.getInstance().clearCorrectAnswers();
                viewFlipper.setDisplayedChild(FlipperChild.MAIN_CONTENT.ordinal());
            }
        });

        /* BUTTON FAVOURITE */
        shineButton = findViewById(R.id.shine_button);
        shineButton.init(this);
        shineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //GameLogic gameLogic = GameLogic.getInstance();
                if (shineButton.isChecked()) {
                    UserData.getInstance().addToFavouriteQuestions(/*gameLogic*/gamePresenter.getCurrentQuestion().getId());
                } else {
                    UserData.getInstance().deleteFromFavouriteQuestions(/*gameLogic*/gamePresenter.getCurrentQuestion().getId());
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        themeChangerDialog = new ThemeChangerDialog(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (confirmHintDialog != null)
            confirmHintDialog.dismiss();
        if (resetDialog != null)
            resetDialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case EXPLANATION_ACTIVITY:
                if (resultCode == RESULT_FIRST_USER) { // TODO: change Code or return value
                    gamePresenter.nextQuestion();
                    break;
                }
            case FAVOURITES_ACTIVITY:
                if (resultCode == RESULT_FIRST_USER && data != null) {
                    GameLogic.getInstance().questionById(data.getIntExtra(QUESTION, 1));
                    break;
                }
        }
    }

    @SuppressLint("RestrictedApi") // TODO: kludge ?
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItemImpl itemNumbers = (MenuItemImpl) menu.findItem(R.id.menu_check_line_numbers);
        MenuItemImpl cbZoom = (MenuItemImpl) menu.findItem(R.id.menu_check_zoom);
        boolean checkedNumbers = appPreferences.getBoolean(APP_PREF_LINE_NUMBERS, false);
        boolean checkedZoom = appPreferences.getBoolean(APP_PREF_ZOOM, false);
        itemNumbers.setChecked(checkedNumbers);
        cbZoom.setChecked(checkedZoom);
        return true;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.expand_header_hint) {
            if (expandableHint.isExpanded()) {
                expandableHint.collapse();
            } else {
                expandableHint.expand();
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.favourites: {
                Intent intent = new Intent(MainActivity.this, FavouritesActivity.class);
                startActivityForResult(intent, FAVOURITES_ACTIVITY);
                break;
            }
            case R.id.go_to:
                openGoToDialog();
                break;
            case R.id.dark_side:
                Switch switchOffline = findViewById(R.id.switch_app_theme);
                switchOffline.toggle();
                return true;
            case R.id.update_base:
                Bundle args = new Bundle();
                args.putInt(REQUEST_TYPE, RequestType.UPDATE.ordinal());
                getSupportLoaderManager().initLoader(0, args, MainActivity.this);
                break;
            case R.id.res_progress:
                openResetDialog();
                break;
            case R.id.start_new_quiz:

                break;
            case R.id.training_mode:

                break;
            case R.id.nav_about:
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.share_question: {
                shareQuestion();
                break;
            }
            case R.id.share_quiz: {
                shareQuiz();
                break;
            }
            case R.id.debug_no_questions:
                noMoreQuestions();
                // viewFlipper.setDisplayedChild(FlipperChild.NO_QUESTIONS_VIEW.ordinal());
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_switch_theme:
                themeChangerDialog.show(this, codeView.getTheme());
                break;
            case R.id.menu_check_line_numbers:
                item.setChecked(!item.isChecked());
                onShowLineNumbersToggled(item.isChecked());
                break;
            case R.id.menu_check_zoom:
                item.setChecked(!item.isChecked());
                onZoomSupportToggled(item.isChecked());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        final EditText etAnswer = findViewById(R.id.et_card_view_answer);
        final Spinner spResult = findViewById(R.id.sp_main_result);
        SharedPreferences.Editor editor = appPreferences.edit();

        editor.putBoolean(APP_THEME_IS_DARK, isChecked);
        editor.commit(); // TODO: commit or apply
        Intent intent = getIntent();
        intent.putExtra(USER_ANSWER, new UsersAnswer(
                gamePresenter.getCurrentQuestion().getId(),
                ResultBehaviourType.getType(spResult.getSelectedItemPosition()),
                etAnswer.getText().toString()
        ));
        finish();
        startActivity(intent);
    }


    private void onShowLineNumbersToggled(final boolean enableLineNumbers) {
        codeView.setShowLineNumbers(enableLineNumbers);
        codeView.refresh();
        SharedPreferencesHelper.save(appPreferences, APP_PREF_LINE_NUMBERS, enableLineNumbers);
    }

    private void onZoomSupportToggled(final boolean enableZooming) {
        codeView.setZoomSupportEnabled(enableZooming);
        codeView.refresh();
        SharedPreferencesHelper.save(appPreferences, APP_PREF_ZOOM, enableZooming);
    }

    @Override
    public void onThemeChanged(@NonNull Theme theme) {
        SharedPreferencesHelper.save(appPreferences, THEME, theme.name());
    }

    @Override
    public void onChangeTheme(@NonNull Theme theme) {
        codeView.setTheme(theme);
        codeView.refresh();
    }

    private void initDumpLoader(RequestType requestType) {
        Bundle args = new Bundle();
        args.putInt(REQUEST_TYPE, requestType.ordinal());
        getSupportLoaderManager().initLoader(0, args, this);
    }

    private void shareQuestion() {
        final int questionId = gamePresenter.getCurrentQuestion().getId();
        final int stringResource;
        if (UserData.getInstance().isCorrectlyAnswered(questionId)) {
            stringResource = R.string.share_q_right;
        } else {
            stringResource = R.string.share_q_unans;
        }
        String questionURL = CppQuizLiteApi.getQuestionURL(questionId);
        String contentText = String.format(
                getResources().getString(R.string.share_question_text),
                String.format(getResources().getString(stringResource), questionId),
                questionURL,
                getResources().getString(R.string.google_play_link)
        );
        shareSocial(
                contentText,
                getResources().getString(R.string.send_question_to)
        );
    }

    private void shareQuiz() {
        /*final int questionId = GameLogic.getInstance().getCurrentQuestion().id;
                String questionURL = CppQuizLiteApi.getQuestionURL(questionId);
                String contentText = String.format(
                        getResources().getString(R.string.share_quiz_text), questionURL, "cppquiz.org"
                );
                shareSocial(
                        contentText,
                        getResources().getString(R.string.send_question_to)
                );*/
    }

    private void shareSocial(String content, String title) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, content);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, title));
    }

    @NonNull
    @Override
    public Loader<LoadResult<String, LinkedHashSet<Integer>>> onCreateLoader(final int id, final Bundle args) {
        return new DumpLoader(this, args.getInt(REQUEST_TYPE));
    }

    @Override
    public void onLoadFinished(@NonNull Loader<LoadResult<String, LinkedHashSet<Integer>>> loader, LoadResult<String, LinkedHashSet<Integer>> data) {
        if (data.connectSuccessType == ConnectSuccessType.OK && data.questionsIds != null && data.cppStandard != null) { // TODO: remove extra checks
            SharedPreferences.Editor editor = appPreferences.edit();

            if (data.updated) {
                GameLogic gameLogic = GameLogic.getInstance();
                gameLogic.initNewData(this, data.cppStandard, data.questionsIds);
                Question currentQuestion = gameLogic.getCurrentQuestion();
                if (currentQuestion == null || !data.questionsIds.contains(currentQuestion.getId())) {
                    showFirstQuestion();
                }
            }

            switch (data.requestType) {
                case LOAD_DUMP:
                    editor.putBoolean(HAS_VISITED, true);
                    viewFlipper.setDisplayedChild(FlipperChild.MAIN_CONTENT.ordinal());
                    DrawerLayout drawer = findViewById(R.id.drawer_layout);
                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                    break;
                case UPDATE:
                    String message;
                    if (data.updated) {
                        message = getResources().getString(R.string.update_new_questions);
                    } else {
                        message = getResources().getString(R.string.update_no_change);
                    }
                    Snackbar.make(findViewById(R.id.ll_main_task), message, Snackbar.LENGTH_LONG).show(); // TODO: make button orange
                    break;
            }
            editor.putString(CPP_STANDARD, data.cppStandard);
            editor.putLong(LAST_UPDATE, new Date().getTime());
            editor.apply();
        } else {
            switch (data.requestType) {
                case LOAD_DUMP:
                    viewFlipper.setDisplayedChild(FlipperChild.NO_INTERNET_VIEW.ordinal());
                    break;
                case UPDATE:
                    int message;
                    if (data.connectSuccessType == ConnectSuccessType.ERROR) {
                        message = R.string.error_text;
                    } else {
                        message = R.string.unavailable_update_dialog_text;
                    }
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.nested_scroll_view), message, 4000);
                    snackbar.setAction(R.string.retry, new RetryLoadingListener());
                    snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.shackbar_action_retry));
                    snackbar.show();
                    break;
            }
        }
        getSupportLoaderManager().destroyLoader(0);
    }

    private void showFirstQuestion() {
        final Intent intent = getIntent();
        final String action = intent.getAction();
        final String data = intent.getDataString();
        if (Intent.ACTION_VIEW.equals(action) && data != null) {
            DeepLinksUtils deepLinksUtils = new DeepLinksUtils(data);
            if (deepLinksUtils.isQuestionLink()) {
                GameLogic.getInstance().questionById(deepLinksUtils.getQuestionId());
            } else if (deepLinksUtils.isQuizLink()) {
                // TODO: handle quiz links
            } else {
                gamePresenter.nextQuestion();
            }
        } else {
            UsersAnswer usersAnswer = (UsersAnswer) intent.getSerializableExtra(USER_ANSWER);
            if (usersAnswer != null) {
                GameLogic.getInstance().questionById(usersAnswer.questionId);
            } else {
                gamePresenter.nextQuestion();
            }
        }
    }

    private void openConfirmDialog() {
        confirmHintDialog = new ConfirmHintDialog();
        confirmHintDialog.show(getSupportFragmentManager(), "confirm_hint");
    }

    private void openGoToDialog() {
        goToDialog = new GoToDialog();
        goToDialog.show(getSupportFragmentManager(), "go_to");
    }

    private void openResetDialog() {
        resetDialog = new ConfirmResetDialog();
        resetDialog.show(getSupportFragmentManager(), "reset_half_universe");
    }

    @Override
    public void onConfirmedHintLoad() {
        gamePresenter.getHint();
    }

    @Override
    public void applyGoTo(final int questionId) {
        GameLogic.getInstance().questionById(questionId);
        if (viewFlipper.getDisplayedChild() != FlipperChild.MAIN_CONTENT.ordinal()) {
            viewFlipper.setDisplayedChild(FlipperChild.MAIN_CONTENT.ordinal());
        }
    }

    @Override
    public void onConfirmedReset() {
        UserData.getInstance().clearCorrectAnswers();
        if (viewFlipper.getDisplayedChild() != FlipperChild.MAIN_CONTENT.ordinal()) {
            viewFlipper.setDisplayedChild(FlipperChild.MAIN_CONTENT.ordinal());
        }
    }

    private void setExpandablePanel(final int visibility) {
        LinearLayout outer = findViewById(R.id.expansion_panel_outer);
        outer.setVisibility(visibility);
        if (visibility == View.GONE) {
            expandableHint.collapse();
        } else {
            expandableHint.expand();
        }
    }

    private void startExplanationActivity(final boolean isGaveUp, final Question question) {
        Intent intent = new Intent(MainActivity.this, ExplanationActivity.class);
        intent.putExtra(IS_GIVE_UP, isGaveUp);
        intent.putExtra(QUESTION, question);
        startActivityForResult(intent, EXPLANATION_ACTIVITY);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<LoadResult<String, LinkedHashSet<Integer>>> loader) {
    }

    @Override
    public void onCppStandardChanged(@NonNull final String cppStandard) {
        TextView tvTask = findViewById(R.id.tv_task);
        String task = String.format(getResources().getString(R.string.task_whole), cppStandard);
        tvTask.setText(task);
    }

    @Override
    public void onGameStateChanged(final int questionId, final int correct, final int all) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(String.format(getResources().getString(R.string.toolbar_title_training),
                questionId,
                correct,
                all));
    }

    @Override
    public void onQuestionLoaded(@NonNull final Question question, final int attemptsRequired) {
        setExpandablePanel(View.GONE);

        EditText etAnswer = findViewById(R.id.et_card_view_answer);
        etAnswer.getText().clear();

        ShineButton shineButton = findViewById(R.id.shine_button);
        boolean checked = UserData.getInstance().isFavouriteQuestion(question.getId());
        shineButton.setChecked(checked, false);

        Button btnGiveUp = findViewById(R.id.btn_give_up);
        if (attemptsRequired == 0) {
            btnGiveUp.setText(getResources().getString(R.string.give_up_default));
        } else {
            btnGiveUp.setText(String.format(getResources().getString(R.string.give_up_attempts), attemptsRequired));
        }

        Button btnRandom = findViewById(R.id.btn_random);
        btnRandom.setText(getResources().getString(R.string.another_question));

        FloatingActionButton btnAnswer = findViewById(R.id.btn_answer);
        btnAnswer.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.buttonAnswerDefault));

        ImageView imageViewLevel = findViewById(R.id.iv_linear_difficulty);
        TypedArray levels = getResources().obtainTypedArray(R.array.difficulty_levels);
        imageViewLevel.setImageResource(levels.getResourceId(question.getDifficulty() - 1, -1));
        levels.recycle();

        codeView.setSource(question.getCode());
    }

    @Override
    public void onQuestionNotFound(final int questionId) {
        showToast(getString(R.string.question_not_found));
    }

    @Override
    public void onHintReceived(@NonNull final String hint) {
        TextView tvHint = findViewById(R.id.tv_hint);
        Markwon.setMarkdown(tvHint, hint);

        setExpandablePanel(View.VISIBLE);
    }

    public void showAttempts(final int attemptsRequired) {
        Button btnGiveUp = findViewById(R.id.btn_give_up);
        if (attemptsRequired == 0) {
            btnGiveUp.setText(getResources().getString(R.string.give_up_default));
        } else {
            btnGiveUp.setText(String.format(getResources().getString(R.string.give_up_attempts), attemptsRequired));
        }
    }

    @Override
    public void onCorrectAnswered(@NonNull final Question question, final int attemptsRequired) {
        showAttempts(attemptsRequired);
        FloatingActionButton btnAnswer = findViewById(R.id.btn_answer);
        btnAnswer.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.buttonAnswerRight));

        YoYo.with(Techniques.Bounce)
                .duration(600)
                .playOn(btnAnswer);

        Button btnRandom = findViewById(R.id.btn_random);
        btnRandom.setText(getResources().getString(R.string.next));

        startExplanationActivity(false, question);
    }

    @Override
    public void onIncorrectAnswered(final int attemptsRequired) {
        showAttempts(attemptsRequired);

        FloatingActionButton btnAnswer = findViewById(R.id.btn_answer);
        btnAnswer.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.buttonAnswerError));

        showToast(getString(R.string.please_try_again));

        YoYo.with(Techniques.Shake)
                .duration(200)
                .playOn(btnAnswer);
    }

    @Override
    public void onGiveUp(@NonNull final Question question) {
        startExplanationActivity(true, question);
    }

    @Override
    public void tooEarlyToGiveUp(final int attemptsRequired) {
        showToast(getString(R.string.give_up_help));
    }

    @Override
    public void noMoreQuestions() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.toolbar_no_questions));
        viewFlipper.setDisplayedChild(FlipperChild.NO_QUESTIONS_VIEW.ordinal());
    }

    @Override
    public void onErrorHappens() {
        // TODO: handle errors
    }

    private void showToast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    public class RetryLoadingListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            initDumpLoader(RequestType.UPDATE);
        }
    }

}