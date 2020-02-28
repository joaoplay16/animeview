package com.playlab.animeview.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.playlab.animeview.dao.AnimeSQLHelper;


public class AnimeProvider extends ContentProvider {
  public static final String AUTHORITY = "com.playlab.animeview.animeview2";
  public static final String PATH = "animes";

  public static final Uri CONTENT_URI = Uri.parse("content://"+ AUTHORITY + "/" + PATH);

  public static final int TIPO_ESPECIFICO = 2;
  
  public static final int TIPO_GERAL = 1;
  
  public static final UriMatcher mUriMatcher;
  
  private AnimeSQLHelper mHelper;
  
  static {
    mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    mUriMatcher.addURI(AUTHORITY, PATH, TIPO_GERAL);
    mUriMatcher.addURI(AUTHORITY, PATH+"/#", TIPO_ESPECIFICO);
  }

  public AnimeProvider(){

  }
  
  public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
    int uriType = mUriMatcher.match(uri);
    SQLiteDatabase sQLiteDatabase = this.mHelper.getWritableDatabase();
    int linhasAfetadas = 0;
    switch (uriType) {
      default:
        throw new IllegalArgumentException("UNKOWN URI: " + uri);


      case TIPO_ESPECIFICO:
        String id = uri.getLastPathSegment();
        linhasAfetadas = sQLiteDatabase.delete(AnimeSQLHelper.TABELA_ANIME,
                AnimeSQLHelper.COL_ID + "= ?",
                new String[]{id});
           break;
      case TIPO_GERAL:
        linhasAfetadas = sQLiteDatabase.delete(AnimeSQLHelper.TABELA_ANIME, selection, selectionArgs);
    } 

    getContext().getContentResolver().notifyChange(uri, null);
    return linhasAfetadas;
  }
  
  @Nullable
  public String getType(@NonNull Uri paramUri) {
    return null;
  }
  
  @Nullable
  public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
    int uriType = mUriMatcher.match(uri);
    SQLiteDatabase sQLiteDatabase = this.mHelper.getWritableDatabase();
    long id = 0;

    switch (uriType){
      case TIPO_GERAL:
        id = sQLiteDatabase.insertWithOnConflict(AnimeSQLHelper.TABELA_ANIME,
                null, values, SQLiteDatabase.CONFLICT_REPLACE);
        break;

        default: throw new IllegalArgumentException("URI não suportada: " + uri);
    }

    getContext().getContentResolver().notifyChange(uri, null);
    return Uri.withAppendedPath(CONTENT_URI, String.valueOf(id));
  }
  
  public boolean onCreate() {
    this.mHelper = new AnimeSQLHelper(getContext());
    return true;
  }
  
  @Nullable
  public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
    int uriType = mUriMatcher.match(uri);
    SQLiteDatabase sQLiteDatabase = this.mHelper.getWritableDatabase();
    SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
    queryBuilder.setTables(AnimeSQLHelper.TABELA_ANIME);

    Cursor cursor;
    switch (uriType) {
      case TIPO_GERAL:
        cursor = queryBuilder.query(sQLiteDatabase, projection, selection, selectionArgs, null, null, sortOrder);
        break;
      case TIPO_ESPECIFICO:
        queryBuilder.appendWhere(AnimeSQLHelper.COL_ID +" = ?");
        cursor = queryBuilder.query(sQLiteDatabase, projection, selection, new String[] { uri.getLastPathSegment() }, null, null, null);
        break;
      default:
        throw new IllegalArgumentException("UNKNOWN Uri" + uri);
    }
    cursor.setNotificationUri(getContext().getContentResolver(), uri);
    return cursor;
  }
  
  public int update(@NonNull Uri paramUri, @Nullable ContentValues paramContentValues, @Nullable String paramString, @Nullable String[] paramArrayOfString) {
    StringBuilder stringBuilder;
    int uri = mUriMatcher.match(paramUri);
    SQLiteDatabase sQLiteDatabase = this.mHelper.getWritableDatabase();
    switch (uri) {
      default:
        stringBuilder = new StringBuilder();
        stringBuilder.append("Unknown URI");
        stringBuilder.append(paramUri);
        throw new IllegalArgumentException(stringBuilder.toString());
      case TIPO_ESPECIFICO:
        uri = sQLiteDatabase.update(AnimeSQLHelper.TABELA_ANIME, paramContentValues, AnimeSQLHelper.COL_ID+" =?",
                new String[] { paramUri.getLastPathSegment() });
        getContext().getContentResolver().notifyChange(paramUri, null);
        break;
      case TIPO_GERAL:
        uri = sQLiteDatabase.update(AnimeSQLHelper.TABELA_ANIME, paramContentValues, paramString, paramArrayOfString);
        break;
    } 

    getContext().getContentResolver().notifyChange(paramUri, null);
    return uri;
  }
}


