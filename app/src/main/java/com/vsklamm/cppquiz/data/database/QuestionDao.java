package com.vsklamm.cppquiz.data.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.vsklamm.cppquiz.data.Question;

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
