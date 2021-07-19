package com.nikitabolshakov.notes.notes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.nikitabolshakov.notes.callback.Callback;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class NotesFirestoreRepository implements NotesRepository {

    public static final NotesRepository INSTANCE = new NotesFirestoreRepository();

    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    private final static String NOTES = "notes";
    private final static String DATE = "date";
    private final static String NAME = "name";
    private final static String DESCRIPTION = "description";

    @Override
    public void getNotes(Callback<List<Note>> callback) {

        firebaseFirestore.collection(NOTES)
                .orderBy(DATE, Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {

                            ArrayList<Note> result = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String name = (String) document.get(NAME);
                                String description = (String) document.get(DESCRIPTION);
                                Date date = ((Timestamp) document.get(DATE)).toDate();

                                result.add(new Note(document.getId(), name, description, date));
                            }

                            callback.onSuccess(result);

                        } else {

                            task.getException();
                        }
                    }
                });
    }

    @Override
    public void clear() {

    }

    @Override
    public void add(String name, String description, Callback<Note> callback) {

        HashMap<String, Object> data = new HashMap<>();

        Date date = new Date();

        data.put(NAME, name);
        data.put(DESCRIPTION, description);
        data.put(DATE, date);

        firebaseFirestore.collection(NOTES)
                .add(data)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {

                            Note note = new Note(task.getResult().getId(), name, description, date);

                            callback.onSuccess(note);
                        }
                    }
                });
    }

    @Override
    public void remove(Note note, Callback<Object> callback) {

        firebaseFirestore.collection(NOTES)
                .document(note.getId())
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            callback.onSuccess(note);
                        }
                    }
                });
    }

    @Override
    public void update(Note note, @Nullable String name, @Nullable String description, @Nullable Date date, Callback<Note> callback) {

        HashMap<String, Object> data = new HashMap<>();

        data.put(NAME, name);
        data.put(DESCRIPTION, description);
        data.put(DATE, date);

        firebaseFirestore.collection(NOTES)
                .document(note.getId())
                .update(data)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Note newNote = new Note(note.getId(), name, description, date);
                        callback.onSuccess(newNote);
                    }
                });

    }
}
