package com.nikitabolshakov.notes.notes;

import androidx.annotation.Nullable;

import com.nikitabolshakov.notes.callback.Callback;

import java.util.Date;
import java.util.List;

public interface NotesRepository {

    void getNotes(Callback<List<Note>> callback);

    void clear();

    void add(String name, String description, Callback<Note> callback);

    void remove(Note note, Callback<Object> callback);

    void update(Note note, @Nullable String name, @Nullable String description, @Nullable Date date, Callback<Note> callback);

}
