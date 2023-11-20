package com.playlab.animeview.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.playlab.animeview.R;
import com.playlab.animeview.dao.AnimeSQLHelper;

public class AnimesAdapter extends RecyclerView.Adapter<AnimesAdapter.AnimesViewHolder> {
  private Context mContext;
  
  private Cursor mCursor;
  
  private AoClicarNoAnimeListener mListener;
  
  public AnimesAdapter(Context context, AoClicarNoAnimeListener listener) {
    this.mListener = listener;
    this.mContext = context;
  }
  
  public Cursor getCursor() {
    return this.mCursor;
  }
  
  public int getItemCount() {
    return (this.mCursor != null) ? this.mCursor.getCount() : 0;
  }
  
  public long getItemId(int position) {
    if (this.mCursor != null) {
      if (mCursor.moveToPosition(position)) {
        position = this.mCursor.getColumnIndex(AnimeSQLHelper.COL_ID);
        return this.mCursor.getLong(position);
      } 
      return 0L;
    }
    return 0L;
  }
  @SuppressLint("Range")
  public void onBindViewHolder(@NonNull AnimesViewHolder animesViewHolder, int position) {
    this.mCursor.moveToPosition(position);
    String titulo = this.mCursor.getString(this.mCursor.getColumnIndex(AnimeSQLHelper.COL_TITULO));
    int ultimo_ep = this.mCursor.getInt(this.mCursor.getColumnIndex(AnimeSQLHelper.COL_ULTIMO_EP));
    Log.d("TAG", "ultimo ep " + ultimo_ep);
    int dia_postagem = this.mCursor.getInt(this.mCursor.getColumnIndex(AnimeSQLHelper.COL_DIA_DE_POSTAGEM));
    animesViewHolder.txtTitulo.setText(titulo);
    animesViewHolder.txtUltimoEp.setText(mContext.getString(R.string.ultimo_ep_assistido_2, ultimo_ep));
    int[] cores = this.mContext.getResources().getIntArray(R.array.paleta_de_cores);
    int corIniciais = (int)(Math.random() * cores.length);
    animesViewHolder.txtIniciais.setBackgroundColor(cores[corIniciais]);
    animesViewHolder.txtIniciais.setText(String.valueOf(titulo.charAt(0)).toUpperCase());
    String[] dia_da_semana = this.mContext.getResources().getStringArray(R.array.dias_da_semana);
    String dds = mContext.getString(R.string.dia_de_postagem_2,dia_da_semana[dia_postagem]);
    animesViewHolder.txtDiaDePostagem.setText(dds);
  }
  
  @NonNull
  public AnimesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
    View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_anime, viewGroup, false);
    final AnimesViewHolder vh = new AnimesViewHolder(view);
    view.setTag(vh);
    vh.itemView.setOnClickListener(new View.OnClickListener() {
          public void onClick(View v) {
            int i = vh.getAdapterPosition();
            mCursor.moveToPosition(i);
            if (mListener != null)
              mListener.clicouNoAnime(mCursor);
          }
        });
    return vh;
  }
  
  public void setCursor(Cursor cursor) {
    this.mCursor = cursor;
    notifyDataSetChanged();
  }
  
   static class AnimesViewHolder extends RecyclerView.ViewHolder {
     TextView txtDiaDePostagem;
    
     TextView txtIniciais;

     TextView txtTitulo;
    
     TextView txtUltimoEp;
    
     AnimesViewHolder(View view) {
      super(view);
      this.txtIniciais = view.findViewById(R.id.txtIniciais);
      this.txtTitulo = view.findViewById(R.id.txtTitulo);
      this.txtUltimoEp = view.findViewById(R.id.txtUltimoEp);
      this.txtDiaDePostagem = view.findViewById(R.id.txtDiaDePostagem);
    }
  }
  
  public interface AoClicarNoAnimeListener {
    void clicouNoAnime(Cursor cursor);
  }
}


