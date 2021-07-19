package com.nikitabolshakov.notes.ui.update;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.nikitabolshakov.notes.R;
import com.nikitabolshakov.notes.callback.Callback;
import com.nikitabolshakov.notes.notes.Note;
import com.nikitabolshakov.notes.notes.NotesFirestoreRepository;
import com.nikitabolshakov.notes.notes.NotesRepository;
import com.nikitabolshakov.notes.ui.router.RouterHolder;
import com.nikitabolshakov.notes.ui.router.MainRouter;

import java.util.Calendar;
import java.util.Date;

public class UpdateNoteFragment extends Fragment {

    public static final String TAG = "UpdateNoteFragment";
    public static final String UPDATE_RESULT = "UPDATE_RESULT";
    public static final String ARG_NOTE = "ARG_NOTE";


    public static UpdateNoteFragment newInstance(Note note) {
        UpdateNoteFragment fragment = new UpdateNoteFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_NOTE, note);
        fragment.setArguments(bundle);
        return fragment;
    }

    private final NotesRepository notesRepository = NotesFirestoreRepository.INSTANCE;

    private int selectedYear = -1;
    private int selectedMonthOfYear = -1;
    private int selectedDayOfMonth = -1;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_update_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Note note = getArguments().getParcelable(ARG_NOTE);

        Toolbar toolbarUpdate = view.findViewById(R.id.toolbar_update);

        EditText name = view.findViewById(R.id.name_update);
        EditText description = view.findViewById(R.id.description_update);

        toolbarUpdate.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getItemId() == R.id.action_update_done) {

                    Date selectedDate = null;

                    if (selectedYear != -1 && selectedMonthOfYear != -1 && selectedDayOfMonth != -1) {

                        Calendar calendar = Calendar.getInstance();

                        calendar.setTime(note.getDate());

                        calendar.set(Calendar.YEAR, selectedYear);
                        calendar.set(Calendar.MONTH, selectedMonthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, selectedDayOfMonth);

                        selectedDate = calendar.getTime();
                    }

                    notesRepository.update(note, name.getText().toString(), description.getText().toString(), selectedDate, new Callback<Note>() {
                        @Override
                        public void onSuccess(Note result) {

                            if (requireActivity() instanceof RouterHolder) {

                                MainRouter router = ((RouterHolder) getActivity()).getMainRouter();

                                Bundle bundle = new Bundle();
                                bundle.putParcelable(ARG_NOTE, result);
                                getParentFragmentManager().setFragmentResult(UPDATE_RESULT, bundle);

                                router.back();
                            }
                        }
                    });

                    return true;
                }

                return false;
            }
        });

        name.setText(note.getName());
        description.setText(note.getDescription());
        DatePicker datePicker = view.findViewById(R.id.picker);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(note.getDate());

        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                selectedYear = year;
                selectedMonthOfYear = monthOfYear;
                selectedDayOfMonth = dayOfMonth;
            }
        });
    }
}
