package com.playlab.animeview;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.playlab.animeview.fragment.AnimeFragmentList;


public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private AnimeFragmentList animeFragmentList;
    private FirebaseAnalytics mFirebaseAnalytics;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        this.animeFragmentList = (AnimeFragmentList) getSupportFragmentManager().findFragmentById(R.id.fragmentLista);

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.METHOD, "ONCREATE");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, bundle );
    }
    public boolean onQueryTextChange(String s) {
        this.animeFragmentList.buscar(s);
        return false;
    }

    public boolean onQueryTextSubmit(String s) {
        return true;
    }
}

