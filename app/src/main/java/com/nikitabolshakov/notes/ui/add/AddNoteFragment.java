package com.nikitabolshakov.notes.ui.add;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.nikitabolshakov.notes.R;
import com.nikitabolshakov.notes.callback.Callback;
import com.nikitabolshakov.notes.notes.Note;
import com.nikitabolshakov.notes.notes.NotesAdapter;
import com.nikitabolshakov.notes.notes.NotesFirestoreRepository;
import com.nikitabolshakov.notes.notes.NotesRepository;
import com.nikitabolshakov.notes.ui.router.MainRouter;
import com.nikitabolshakov.notes.ui.router.RouterHolder;

public class AddNoteFragment extends Fragment {

    private final NotesRepository notesRepository = NotesFirestoreRepository.INSTANCE;

    public static final String ADD = "ADD";
    public static final String ARG_NOTE = "ARG_NOTE";
    public static final String TAG = "AddNoteFragment";

    private NotesAdapter notesAdapter;

    public static AddNoteFragment newInstance() {
        return new AddNoteFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        notesAdapter = new NotesAdapter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbarAdd = view.findViewById(R.id.toolbar_add);

        EditText nameAdd = view.findViewById(R.id.name_add);
        EditText descriptionAdd = view.findViewById(R.id.description_add);

        toolbarAdd.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getItemId() == R.id.action_add_done) {

                    notesRepository.add(nameAdd.getText().toString(), descriptionAdd.getText().toString(), new Callback<Note>() {
                        @Override
                        public void onSuccess(Note result) {

                            notesAdapter.add(result);

                            if (requireActivity() instanceof RouterHolder) {

                                MainRouter router = ((RouterHolder) getActivity()).getMainRouter();

                                Bundle bundle = new Bundle();
                                bundle.putParcelable(ARG_NOTE, result);
                                getParentFragmentManager().setFragmentResult(ADD, bundle);

                                router.back();
                            }
                        }
                    });

                    return true;
                }

                return false;
            }
        });
    }
}
