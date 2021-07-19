package com.nikitabolshakov.notes.ui.list;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nikitabolshakov.notes.R;
import com.nikitabolshakov.notes.notes.Note;
import com.nikitabolshakov.notes.notes.NotesAdapter;
import com.nikitabolshakov.notes.notes.NotesFirestoreRepository;
import com.nikitabolshakov.notes.notes.NotesRepository;
import com.nikitabolshakov.notes.callback.Callback;
import com.nikitabolshakov.notes.ui.add.AddNoteFragment;
import com.nikitabolshakov.notes.ui.router.RouterHolder;
import com.nikitabolshakov.notes.ui.router.MainRouter;
import com.nikitabolshakov.notes.ui.update.UpdateNoteFragment;

import java.util.Collections;
import java.util.List;

public class NoteListFragment extends Fragment {

    public static final String TAG = "NoteListFragment";

    private NotesAdapter notesAdapter;

    private int longClickedIndex;
    private Note longClickedNote;

    private final NotesRepository notesRepository = NotesFirestoreRepository.INSTANCE;

    public static NoteListFragment newInstance() {
        return new NoteListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        notesAdapter = new NotesAdapter(this);

        notesRepository.getNotes(new Callback<List<Note>>() {
            @Override
            public void onSuccess(List<Note> result) {
                notesAdapter.setData(result);
                notesAdapter.notifyDataSetChanged();
            }
        });

        notesAdapter.setListener(new NotesAdapter.OnNoteClickedListener() {
            @Override
            public void onNoteClickedListener(@NonNull Note note) {

                if (requireActivity() instanceof RouterHolder) {
                    MainRouter router = ((RouterHolder) requireActivity()).getMainRouter();

                    router.showNoteDetails(note);
                }
            }
        });

        notesAdapter.setLongClickedListener(new NotesAdapter.OnNoteLongClickedListener() {
            @Override
            public void onNoteLongClickedListener(@NonNull Note note, int index) {
                longClickedNote = note;
                longClickedIndex = index;
            }
        });

        getParentFragmentManager().setFragmentResultListener(UpdateNoteFragment.UPDATE_RESULT, this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                if (result.containsKey(UpdateNoteFragment.ARG_NOTE)) {
                    Note note = result.getParcelable(UpdateNoteFragment.ARG_NOTE);

                    notesAdapter.update(note);

                    notesAdapter.notifyItemChanged(longClickedIndex);
                }
            }
        });

        getParentFragmentManager().setFragmentResultListener(AddNoteFragment.ADD, this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                if (result.containsKey(AddNoteFragment.ARG_NOTE)) {
                    Note note = result.getParcelable(AddNoteFragment.ARG_NOTE);

                    notesAdapter.add(note);
                }
            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView notesList = view.findViewById(R.id.note_list_container);

        notesList.setLayoutManager(new LinearLayoutManager(requireContext()));

        notesList.setAdapter(notesAdapter);

        DefaultItemAnimator animator = new DefaultItemAnimator();

        animator.setAddDuration(3000L);
        animator.setRemoveDuration(3000L);

        notesList.setItemAnimator(animator);

        Toolbar toolbar = view.findViewById(R.id.toolbar);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getItemId() == R.id.action_add) {

                    if (requireActivity() instanceof RouterHolder) {
                        MainRouter router = ((RouterHolder) requireActivity()).getMainRouter();

                        router.showAddNote();
                    }

                    return true;
                }

                if (item.getItemId() == R.id.action_clear) {

                    notesRepository.clear();

                    notesAdapter.setData(Collections.emptyList());
                    notesAdapter.notifyDataSetChanged();

                    return true;
                }

                return false;
            }
        });
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        requireActivity().getMenuInflater().inflate(R.menu.menu_notes_context, menu);

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_update) {

            if (requireActivity() instanceof RouterHolder) {

                MainRouter router = ((RouterHolder) requireActivity()).getMainRouter();

                router.showEditNote(longClickedNote);
            }

            return true;
        }

        if (item.getItemId() == R.id.action_delete) {

            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())

                    .setMessage(R.string.delete_message)
                    .setIcon(R.drawable.ic_clear)

                    .setPositiveButton(R.string.button_yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            notesRepository.remove(longClickedNote, new Callback<Object>() {
                                @Override
                                public void onSuccess(Object result) {

                                    notesAdapter.remove(longClickedNote);
                                    notesAdapter.notifyItemRemoved(longClickedIndex);
                                }
                            });

                        }
                    })
                    .setNegativeButton(R.string.button_no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

            builder.show();

            return true;
        }

        return super.onContextItemSelected(item);
    }
}
