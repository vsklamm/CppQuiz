package com.vsklamm.cppquiz.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.vsklamm.cppquiz.model.Question;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface QuestionDao { // TODO: rename methods

    @Query("SELECT * FROM question")
    List<Question> getAll();

    @Query("SELECT id FROM question")
    Single<List<Integer>> getAllIds();

    @SuppressWarnings("unused")
    @Query("SELECT * FROM question WHERE id IN (:userIds)")
    List<Question> loadAllByIDs(int[] userIds);

    @Query("SELECT * FROM question WHERE id = :number")
    Single<Question> findById(int number);

    @Query("SELECT Count(*) FROM question")
    int getSize();

    @Query("DELETE FROM question")
    void clearTable();

    @SuppressWarnings("unused")
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertReplace(Question question);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Question> question);

    @SuppressWarnings("unused")
    @Update
    void update(Question question);

    @SuppressWarnings("unused")
    @Delete
    void delete(Question question);

}
