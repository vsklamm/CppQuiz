package com.vsklamm.cppquiz.data;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

public final class QuestionIds implements Serializable {
    @NotNull
    public Set<Integer> all = new LinkedHashSet<>();
    @NotNull
    private Set<Integer> difficultyOne = new LinkedHashSet<>();
    @NotNull
    private Set<Integer> difficultyTwo = new LinkedHashSet<>();
    @NotNull
    private Set<Integer> difficultyThree = new LinkedHashSet<>();

    @NotNull
    public final Set<Integer> difficultySet(Integer difficulty) {
        switch (difficulty) {
            case 1:
                return this.difficultyOne;
            case 2:
                return this.difficultyTwo;
            case 3:
                return this.difficultyThree;
            default:
                return this.all;
        }
    }

    public final void addId(Question question) {
        all.add(question.getId());
        switch (question.getDifficulty()) {
            case 1:
                difficultyOne.add(question.getId());
                break;
            case 2:
                difficultyTwo.add(question.getId());
                break;
            default:
                difficultyThree.add(question.getId());
        }
    }
}
