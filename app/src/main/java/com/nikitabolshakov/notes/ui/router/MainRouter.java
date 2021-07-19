package com.nikitabolshakov.notes.ui.router;

import androidx.fragment.app.FragmentManager;

import com.nikitabolshakov.notes.R;
import com.nikitabolshakov.notes.notes.Note;
import com.nikitabolshakov.notes.ui.add.AddNoteFragment;
import com.nikitabolshakov.notes.ui.details.NoteDetailsFragment;
import com.nikitabolshakov.notes.ui.list.NoteListFragment;
import com.nikitabolshakov.notes.ui.update.UpdateNoteFragment;

public class MainRouter {

    private final FragmentManager fragmentManager;

    public MainRouter(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public void showNotes() {
        fragmentManager
                .beginTransaction()
                .replace(R.id.container, NoteListFragment.newInstance(), NoteListFragment.TAG)
                .commit();
    }

    public void showNoteDetails(Note note) {
        fragmentManager
                .beginTransaction()
                .replace(R.id.container, NoteDetailsFragment.newInstance(note), NoteDetailsFragment.TAG)
                .addToBackStack(NoteDetailsFragment.TAG)
                .commit();
    }

    public void showEditNote(Note note) {
        fragmentManager
                .beginTransaction()
                .replace(R.id.container, UpdateNoteFragment.newInstance(note), UpdateNoteFragment.TAG)
                .addToBackStack(UpdateNoteFragment.TAG)
                .commit();
    }

    public void showAddNote() {
        fragmentManager
                .beginTransaction()
                .replace(R.id.container, AddNoteFragment.newInstance(), AddNoteFragment.TAG)
                .addToBackStack(AddNoteFragment.TAG)
                .commit();
    }

    public void back() {
        fragmentManager.popBackStack();
    }
}
