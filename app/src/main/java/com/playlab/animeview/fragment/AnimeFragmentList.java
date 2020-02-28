package com.playlab.animeview.fragment;

import android.database.Cursor;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.playlab.animeview.R;
import com.playlab.animeview.adapter.AnimesAdapter;
import com.playlab.animeview.dao.AnimeRepositorio;
import com.playlab.animeview.dao.AnimeSQLHelper;
import com.playlab.animeview.provider.AnimeProvider;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class AnimeFragmentList extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
  private String mTextoBusca;
  private AnimesAdapter mAdapter;
  private RecyclerView.LayoutManager mLayoutManager;
  private LinearLayout emptyView;
  private RecyclerView mRecyclerView;
  private AnimeRepositorio mRepositorio;
  
  private void configuraSwipe() {
    new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, 12) {
          public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder1, RecyclerView.ViewHolder viewHolder2) {
            return false;
          }

          
          public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            direction = viewHolder.getLayoutPosition();
            Cursor cursor = mAdapter.getCursor();
            cursor.moveToPosition(direction);
            long l = cursor.getLong(cursor.getColumnIndex(AnimeSQLHelper.COL_ID));
            Uri uri = Uri.withAppendedPath(AnimeProvider.CONTENT_URI, String.valueOf(l));
            try {
              AnimeFragmentList.this.getActivity().getContentResolver().delete(uri, null, null);
              return;
            } catch (NullPointerException nullPointerException) {
              nullPointerException.printStackTrace();
              return;
            } 
          }

      @Override
      public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                              float dX, float dY, int actionState, boolean isCurrentlyActive) {
        new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                .addBackgroundColor(ContextCompat.getColor(getActivity(), R.color.swipe_delete))
                .addActionIcon(android.R.drawable.ic_menu_delete)
                .create()
                .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
      }
    }).attachToRecyclerView(this.mRecyclerView);
  }
  
  public void buscar(String s) {
    if (TextUtils.isEmpty(s))
      s = null;
    this.mTextoBusca = s;
    getLoaderManager().restartLoader(0, null, this);
  }
  
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.mRepositorio = new AnimeRepositorio(getActivity());

    this.mAdapter = new AnimesAdapter(getActivity(), new AnimesAdapter.AoClicarNoAnimeListener() {
      public void clicouNoAnime(Cursor cursor) {
        FragmentDialogEdit.novaInstancia(cursor.getLong(cursor.getColumnIndex(AnimeSQLHelper.COL_ID))).show(getFragmentManager(), "dialog");
      }
    });
  }
  
  @NonNull
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    return this.mRepositorio.buscar(this.mTextoBusca);
  }
  
  @Nullable
  public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
    View view = layoutInflater.inflate(R.layout.fragment_lista_animes, viewGroup, false);
    view.findViewById(R.id.fabAdd).setOnClickListener(new View.OnClickListener() {
          public void onClick(View v) {
            new FragmentDialogEdit().show(getFragmentManager(), "dialog");
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.METHOD, "NOVO");
            FirebaseAnalytics.getInstance(getActivity()).logEvent(FirebaseAnalytics.Event.SELECT_CONTENT,bundle );
          }
        });
    this.mRecyclerView = view.findViewById(R.id.recyclerView);
    emptyView = view.findViewById(R.id.view_empty);
    this.mRecyclerView.setHasFixedSize(true);
    if ((getResources().getConfiguration()).orientation == 1) {
      this.mLayoutManager = new LinearLayoutManager(getActivity());
    } else {
      this.mLayoutManager = new GridLayoutManager(getActivity(), 2);
    } 
    this.mRecyclerView.setLayoutManager(this.mLayoutManager);


    this.mAdapter.setHasStableIds(true);
    this.mRecyclerView.setAdapter(this.mAdapter);



    configuraSwipe();
    getLoaderManager().initLoader(0, null, this);
    return view;
  }
  
  public void onLoadFinished(@NonNull Loader<Cursor> cursorLoader, Cursor cursor) {
    this.mAdapter.setCursor(cursor);
    if(mAdapter.getItemCount() == 0){
      emptyView.setVisibility(LinearLayout.VISIBLE);
      mRecyclerView.setVisibility(RecyclerView.GONE);
    }else {
      emptyView.setVisibility(LinearLayout.GONE);
      mRecyclerView.setVisibility(RecyclerView.VISIBLE);
    }
  }
  
  public void onLoaderReset(@NonNull Loader<Cursor> cursorLoader) {
    this.mAdapter.setCursor(null);
  }
}
