package com.vsklamm.cppquiz.ui.main;

import android.content.Context;

import androidx.annotation.NonNull;

import com.vsklamm.cppquiz.App;
import com.vsklamm.cppquiz.data.model.Question;
import com.vsklamm.cppquiz.data.model.UserData;
import com.vsklamm.cppquiz.data.local.AppDatabase;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static java.lang.Math.max;

public class GameLogic {

    private static volatile GameLogic gameLogicInstance;
    private WeakReference<GameLogic.GameLogicCallbacks> listener;

    private LinkedHashSet<Integer> questionsIds;
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

    void initNewData(@NonNull Context context, @NonNull final String cppStandard, @NonNull LinkedHashSet<Integer> questionsIds) {
        this.questionsIds = questionsIds;
        try {
            listener = new WeakReference<>((GameLogic.GameLogicCallbacks) context); // TODO: zabotat'
        } catch (ClassCastException e) {
            // ignore
        }
        setCppStandard(cppStandard);
        // Class contract says that UserData is initialized now
        LinkedHashSet<Integer> correctlyAnswered = UserData.getInstance().getCorrectlyAnsweredQuestions();
        HashMap<Integer, Integer> attempts = UserData.getInstance().getAttempts();
        List<Integer> erased = new ArrayList<>();
        for (Integer id : attempts.keySet()) {
            if (!questionsIds.contains(id)) {
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
        }
        else {
            questionById(currentQuestion.getId());
        }
    }

    void randomQuestion() {
        final int randomId = getUnansweredQuestion();
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

        final boolean correct = currentQuestion.compareWithAnswer(UserData.getInstance().givenAnswer);
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

    private int getUnansweredQuestion() { // TODO: too long & create exception
        try {
            LinkedHashSet<Integer> availableQuestions = new LinkedHashSet<>(questionsIds);
            LinkedHashSet<Integer> correctlyAnswered = UserData.getInstance().getCorrectlyAnsweredQuestions();
            availableQuestions.removeAll(correctlyAnswered);

            if (availableQuestions.size() == 0) {
                return -1;
            } else {
                List<Integer> availableAsList = new ArrayList<>(availableQuestions);
                return availableAsList.get(new Random().nextInt(availableAsList.size()));
            }
        } catch (NullPointerException e) { // TODO: handle this
            return 1;
        }
    }

    Question getCurrentQuestion() {
        return currentQuestion;
    }

    private void setCppStandard(final String cppStandard) {
        listener.get().onCppStandardChanged(cppStandard);
    }

    private void updateGameState() { // TODO: kludge ?
        if (currentQuestion != null && questionsIds != null) {
            listener.get().onGameStateChanged(
                    currentQuestion.getId(),
                    UserData.getInstance().getCorrectlyAnsweredQuestions().size(),
                    questionsIds.size()
            );
        }
    }

    @SuppressWarnings("unused")
    protected GameLogic readResolve() {
        return getInstance();
    }

    public interface GameLogicCallbacks {

        void onCppStandardChanged(@NonNull final String cppStandard); // same

        void onGameStateChanged(final int questionId, final int correct, final int all); // diff

        void onQuestionLoaded(@NonNull final Question question, int attemptsRequired); // diff or with extra method (progress)

        void onQuestionNotFound(final int questionId);

        void onHintReceived(@NonNull final String hint); // diff or with extra method (score)

        void onCorrectAnswered(@NonNull final Question question, final int attemptsRequired); // diff or with extra method (progress)

        void onIncorrectAnswered(final int attemptsRequired); // diff or with extra method (progress)

        void onGiveUp(@NonNull final Question question); // none

        void tooEarlyToGiveUp(final int attemptsRequired); // none

        void noMoreQuestions(); // onFinishQuiz instead
    }
}
