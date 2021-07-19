package com.nikitabolshakov.notes.notes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.nikitabolshakov.notes.R;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

    public interface OnNoteClickedListener {
        void onNoteClickedListener(@NonNull Note note);
    }

    public interface OnNoteLongClickedListener {
        void onNoteLongClickedListener(@NonNull Note note, int index);
    }

    private final ArrayList<Note> notes = new ArrayList<>();

    private final Fragment fragment;

    public NotesAdapter(Fragment fragment) {
        this.fragment = fragment;
    }

    private OnNoteClickedListener listener;

    private OnNoteLongClickedListener longClickedListener;

    public OnNoteLongClickedListener getLongClickedListener() {
        return longClickedListener;
    }

    public void setLongClickedListener(OnNoteLongClickedListener longClickedListener) {
        this.longClickedListener = longClickedListener;
    }

    public OnNoteClickedListener getListener() {
        return listener;
    }

    public void setListener(OnNoteClickedListener listener) {
        this.listener = listener;
    }

    public void setData(List<Note> toSet) {
        notes.clear();
        notes.addAll(toSet);
    }

    public int add(Note addedNote) {
        notes.add(addedNote);
        return notes.size() - 1;
    }

    public void remove(Note longClickedNote) {
        notes.remove(longClickedNote);
    }

    public void update(Note note) {

        for (int i = 0; i < notes.size(); i++) {

            Note item = notes.get(i);

            if (item.getId().equals(note.getId())) {

                notes.remove(i);
                notes.add(i, note);

                return;
            }
        }
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);

        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesAdapter.NoteViewHolder holder, int position) {

        Note note = notes.get(position);

        holder.noteName.setText(note.getName());
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {

        TextView noteName;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            fragment.registerForContextMenu(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getListener() != null) {
                        getListener().onNoteClickedListener(notes.get(getAdapterPosition()));
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    itemView.showContextMenu();

                    if (getLongClickedListener() != null) {
                        int index = getAdapterPosition();
                        getLongClickedListener().onNoteLongClickedListener(notes.get(index), index);
                    }

                    return true;
                }
            });

            noteName = itemView.findViewById(R.id.note_name);
        }
    }
}
