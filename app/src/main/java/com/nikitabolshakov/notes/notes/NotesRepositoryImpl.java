package com.nikitabolshakov.notes.notes;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;

import com.nikitabolshakov.notes.callback.Callback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NotesRepositoryImpl implements NotesRepository {

    public static final NotesRepository INSTANCE = new NotesRepositoryImpl();

    private ArrayList<Note> notes = new ArrayList<>();

    private ExecutorService executor = Executors.newCachedThreadPool();

    private Handler handler = new Handler(Looper.getMainLooper());

    public NotesRepositoryImpl() {
        notes.add(new Note("id1", "Котлин", "Статически типизированный, объектно-ориентированный язык программирования, работающий поверх Java Virtual Machine и разрабатываемый компанией JetBrains. Также компилируется в JavaScript и в исполняемый код ряда платформ через инфраструктуру LLVM. Язык назван в честь острова Котлин в Финском заливе, на котором расположен город Кронштадт.", new Date()));
        notes.add(new Note("id2", "Купить розового слона", "Просто купи!", new Date()));
        notes.add(new Note("id3", "Координаты кота", "555666", new Date()));
    }

    @Override
    public void getNotes(Callback<List<Note>> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(2000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSuccess(notes);
                    }
                });
            }
        });
    }

    @Override
    public void clear() {
        notes.clear();
    }

    @Override
    public void add(String name, String description, Callback<Note> callback) {

        Note note = new Note(UUID.randomUUID().toString(), name, description, new Date());
        notes.add(note);

        callback.onSuccess(note);
    }

    @Override
    public void remove(Note note, Callback<Object> callback) {
        notes.remove(note);
        callback.onSuccess(note);
    }

    @Override
    public void update(Note note, @Nullable String name, @Nullable String description, @Nullable Date date, Callback<Note> callback) {

        for (int i = 0; i < notes.size(); i++) {

            Note item = notes.get(i);

            if (item.getId().equals(note.getId())) {

                String nameToSet = item.getName();
                String descriptionToSet = item.getDescription();
                Date dateToSet = item.getDate();

                if (name != null) {
                    nameToSet = name;
                }
                if (description != null) {
                    descriptionToSet = description;
                }
                if (date != null) {
                    dateToSet = date;
                }

                Note newNote = new Note(note.getId(), nameToSet, descriptionToSet, dateToSet);

                notes.remove(i);
                notes.add(i, newNote);

                callback.onSuccess(newNote);
            }
        }
    }
}
