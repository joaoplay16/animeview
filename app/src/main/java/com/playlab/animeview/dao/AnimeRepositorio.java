package com.playlab.animeview.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.playlab.animeview.model.Anime;
import com.playlab.animeview.provider.AnimeProvider;


public class AnimeRepositorio {
  private Context context;
  
  public AnimeRepositorio(Context context) {
    this.context = context;
  }
  
  public static Anime animeFromCursor(Cursor cursor) {
    return new Anime(cursor.getLong(cursor.getColumnIndex(AnimeSQLHelper.COL_ID)),
            cursor.getString(cursor.getColumnIndex(AnimeSQLHelper.COL_TITULO)),
            cursor.getInt(cursor.getColumnIndex(AnimeSQLHelper.COL_ULTIMO_EP)),
            cursor.getInt(cursor.getColumnIndex(AnimeSQLHelper.COL_DIA_DE_POSTAGEM)));
  }
  
  private int atualizar(Anime anime) {
    Uri uri = Uri.withAppendedPath(AnimeProvider.CONTENT_URI, String.valueOf(anime.getId()));
    return this.context.getContentResolver().update(uri, getValues(anime), null, null);
  }
  
  private long inserir(Anime anime) {
    long id = Long.parseLong(this.context.getContentResolver().insert(AnimeProvider.CONTENT_URI, getValues(anime)).getLastPathSegment());
    if (id != -1L)
      anime.setId(id);
    return id;
  }
  
  public Loader<Cursor> buscar(String texto) {
    String[] projection= null;
    String str = null;
    StringBuilder stringBuilder = null;
    if (texto != null) {
      str = "titulo LIKE ?";
      stringBuilder = new StringBuilder();
      stringBuilder.append("%");
      stringBuilder.append(texto);
      stringBuilder.append("%");
      projection = new String[] { stringBuilder.toString() };
    } 
    return new CursorLoader(this.context, AnimeProvider.CONTENT_URI, null, str, projection, AnimeSQLHelper.COL_TITULO);
  }
  
  public Cursor buscarPorID(long id) {
    Uri uri = Uri.withAppendedPath(AnimeProvider.CONTENT_URI, String.valueOf(id));
    return this.context.getContentResolver().query(uri, null, null, null, null);
  }
  
  public int excluir(Anime anime) {
    Uri uri = Uri.withAppendedPath(AnimeProvider.CONTENT_URI, String.valueOf(anime.getId()));
    return this.context.getContentResolver().delete(uri, null, null);
  }
  
  public ContentValues getValues(Anime anime) {
    ContentValues contentValues = new ContentValues();
    contentValues.put(AnimeSQLHelper.COL_TITULO, anime.getTitulo());
    contentValues.put(AnimeSQLHelper.COL_ULTIMO_EP, anime.getUltimoEpAssistido());
    contentValues.put(AnimeSQLHelper.COL_DIA_DE_POSTAGEM, anime.getDiaDePostagem());
    return contentValues;
  }
  
  public void salvar(Anime anime) {
    if (anime.getId() == 0L) {
      inserir(anime);
      return;
    } 
    atualizar(anime);
  }
}

