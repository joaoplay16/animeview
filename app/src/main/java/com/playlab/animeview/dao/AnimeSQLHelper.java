package com.playlab.animeview.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AnimeSQLHelper extends SQLiteOpenHelper {
  public static final String COL_DIA_DE_POSTAGEM = "dia_postagem";

  public static final String COL_ID = "_id";

  public static final String COL_TITULO = "titulo";

  public static final String COL_ULTIMO_EP = "ultimo_ep";

  private static final String NOME_BANCO = "dbAnime";

  public static final String TABELA_ANIME = "anime";

  private static final int VERSAO_BANCO = 1;

  public AnimeSQLHelper(Context context) {
    super(context, NOME_BANCO, null, VERSAO_BANCO);
  }

  public void onCreate(SQLiteDatabase sqLiteDatabase) {
    sqLiteDatabase.execSQL(
            "CREATE TABLE IF NOT EXISTS anime( _id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    "titulo TEXT NOT NULL, ultimo_ep INTEGER, dia_postagem INTEGER);");
  }

  public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
    onCreate(sqLiteDatabase);
  }
}
