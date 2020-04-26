package com.vsklamm.cppquiz.ui.main;

import android.content.Context;

import androidx.annotation.NonNull;

import com.vsklamm.cppquiz.App;
import com.vsklamm.cppquiz.data.Question;
import com.vsklamm.cppquiz.data.QuestionIds;
import com.vsklamm.cppquiz.data.UserData;
import com.vsklamm.cppquiz.data.database.AppDatabase;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static java.lang.Math.max;

public class GameLogic {

    public static final String CPP_STANDARD = "CPP_STANDARD";
    private static volatile GameLogic gameLogicInstance;
    private WeakReference<GameLogic.GameLogicCallbacks> listener;
    private QuestionIds questionsIds;
    private Question currentQuestion;

    private GameLogic() {
        if (gameLogicInstance != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }

    public static GameLogic getInstance() {
        if (gameLogicInstance == null) {
            synchronized (GameLogic.class) {
                if (gameLogicInstance == null) {
                    gameLogicInstance = new GameLogic();
                }
            }
        }
        return gameLogicInstance;
    }

    void initNewData(@NonNull Context context, @NonNull final String cppStandard, @NonNull QuestionIds questionsIds) {
        this.questionsIds = questionsIds;
        try {
            listener = new WeakReference<>((GameLogic.GameLogicCallbacks) context); // TODO: zabotat'
        } catch (ClassCastException e) {
            // ignore
        }
        // Class contract says that UserData is initialized now
        // Only one init per launch
        listener.get().onCppStandardInit(cppStandard);
        listener.get().onDifficultyLevelInit(UserData.getInstance().getDifficulty());
        // May be updated many times per launch
        LinkedHashSet<Integer> correctlyAnswered = UserData.getInstance().getCorrectlyAnsweredQuestions();
        HashMap<Integer, Integer> attempts = UserData.getInstance().getAttempts();
        List<Integer> erased = new ArrayList<>();
        for (Integer id : attempts.keySet()) {
            if (!questionsIds.all.contains(id)) {
                erased.add(id);
            }
        }
        for (Integer id : erased) {
            correctlyAnswered.remove(id);
            attempts.remove(id);
        }
        updateGameState();
    }

    public void ourQuestion() {
        if (currentQuestion.getId() == -1) {
            randomQuestion();
        } else {
            questionById(currentQuestion.getId());
        }
    }

    void randomQuestion() {
        final Integer difficulty = UserData.getInstance().getDifficulty();
        final int randomId = getUnansweredOrWithDifficultyQuestion(difficulty);
        if (randomId == -1) {
            listener.get().noMoreQuestions();
        } else {
            questionById(randomId);
        }
    }

    void questionById(final int questionId) {
        AppDatabase db = App.getInstance().getDatabase();
        db.questionDao().findById(questionId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<Question>() {
                    @Override
                    public void onSuccess(Question question) {
                        currentQuestion = question;
                        updateGameState();
                        listener.get().onQuestionLoaded(
                                currentQuestion,
                                max(3 - UserData.getInstance().attemptsGivenFor(currentQuestion.getId()), 0)
                        );
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.get().onQuestionNotFound(questionId);
                        if (currentQuestion == null) {
                            randomQuestion();
                        }
                    }
                });
    }

    void questionHint() {
        final String hint = currentQuestion.getHint();
        listener.get().onHintReceived(hint);
    }

    void checkAnswer() {
        final int currentId = currentQuestion.getId();
        UserData.getInstance().registerAttempt(currentId);

        final boolean correct = currentQuestion.compareWithAnswer(UserData.getInstance().getGivenAnswer());
        if (correct) {
            UserData.getInstance().registerCorrectAnswer(currentId);
            UserData.getInstance().saveAttempts();
            UserData.getInstance().saveCorrectlyAnswered();
            updateGameState();
            listener.get().onCorrectAnswered(currentQuestion, max(3 - UserData.getInstance().attemptsGivenFor(currentId), 0));
        } else {
            UserData.getInstance().saveAttempts();
            UserData.getInstance().saveCorrectlyAnswered();
            listener.get().onIncorrectAnswered(max(3 - UserData.getInstance().attemptsGivenFor(currentId), 0));
        }
    }

    void giveUp() {
        final int attemptsGivenFor = UserData.getInstance().attemptsGivenFor(currentQuestion.getId());
        if (attemptsGivenFor >= 3) {
            listener.get().onGiveUp(currentQuestion);
        } else {
            listener.get().tooEarlyToGiveUp(3 - attemptsGivenFor);
        }
    }

    private int getUnansweredOrWithDifficultyQuestion(Integer difficulty) { // TODO: too long & create exception
        try {
            Set<Integer> availableQuestions = new LinkedHashSet<>(questionsIds.difficultySet(difficulty));
            Set<Integer> correctlyAnswered = UserData.getInstance().getCorrectlyAnsweredQuestions();
            availableQuestions.removeAll(correctlyAnswered);
            if (availableQuestions.isEmpty()) {
                if (difficulty != 0) {
                    availableQuestions = new LinkedHashSet<>(questionsIds.difficultySet(difficulty));
                } else {
                    return -1;
                }
            }
            List<Integer> availableAsList = new ArrayList<>(availableQuestions);
            return availableAsList.get(new Random().nextInt(availableAsList.size()));
        } catch (NullPointerException e) { // TODO: handle this
            return 1;
        }
    }

    Question getCurrentQuestion() {
        return currentQuestion;
    }

    private void updateGameState() { // TODO: kludge ?
        if (currentQuestion != null && questionsIds != null) { // TODO: questionIds can't be null
            listener.get().onGameStateChanged(
                    currentQuestion.getId(),
                    UserData.getInstance().getCorrectlyAnsweredQuestions().size(),
                    questionsIds.all.size()
            );
        }
    }

    @SuppressWarnings("unused")
    protected GameLogic readResolve() {
        return getInstance();
    }

    public interface GameLogicCallbacks {

        void onCppStandardInit(@NonNull final String cppStandard);

        void onDifficultyLevelInit(@NonNull final Integer difficulty);

        void onGameStateChanged(final int questionId, final int correct, final int all);

        void onQuestionLoaded(@NonNull final Question question, int attemptsRequired);

        void onQuestionNotFound(final int questionId);

        void onHintReceived(@NonNull final String hint);

        void onCorrectAnswered(@NonNull final Question question, final int attemptsRequired);

        void onIncorrectAnswered(final int attemptsRequired);

        void onGiveUp(@NonNull final Question question);

        void tooEarlyToGiveUp(final int attemptsRequired);

        void noMoreQuestions(); // onFinishQuiz instead
    }
}
