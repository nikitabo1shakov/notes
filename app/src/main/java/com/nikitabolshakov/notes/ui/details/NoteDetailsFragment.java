package com.nikitabolshakov.notes.ui.details;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nikitabolshakov.notes.R;
import com.nikitabolshakov.notes.notes.Note;

public class NoteDetailsFragment extends Fragment {

    public static final String TAG = "NoteDetailsFragment";
    private static final String ARG_NOTE = "ARG_NOTE";

    public NoteDetailsFragment() {
    }

    public static NoteDetailsFragment newInstance(Note note) {
        NoteDetailsFragment fragment = new NoteDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_NOTE, note);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView noteName = view.findViewById(R.id.note_name);
        TextView noteDescription = view.findViewById(R.id.note_description);

        Note note = getArguments().getParcelable(ARG_NOTE);

        noteName.setText(note.getName());
        noteDescription.setText(note.getDescription());
    }
}
