package com.nikitabolshakov.notes.ui.main;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.nikitabolshakov.notes.R;
import com.nikitabolshakov.notes.ui.router.RouterHolder;
import com.nikitabolshakov.notes.ui.router.MainRouter;

public class MainActivity extends AppCompatActivity implements RouterHolder {

    private MainRouter router;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        router = new MainRouter(getSupportFragmentManager());

        if (savedInstanceState == null) {
            router.showNotes();
        }
    }

    @Override
    public MainRouter getMainRouter() {
        return router;
    }
}
