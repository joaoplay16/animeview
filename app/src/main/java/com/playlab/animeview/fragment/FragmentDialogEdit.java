package com.playlab.animeview.fragment;

import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.playlab.animeview.R;
import com.playlab.animeview.dao.AnimeRepositorio;
import com.playlab.animeview.dao.AnimeSQLHelper;
import com.playlab.animeview.model.Anime;
import com.google.android.material.textfield.TextInputEditText;


public class FragmentDialogEdit extends DialogFragment implements View.OnClickListener {
  private static final String EXTRA_ID = "id";
  
  private long id;
  private TextInputEditText inputTitulo;
  private TextInputEditText inputUltimoEp;
  private AnimeRepositorio mRepositorio;
  private Spinner spinerDiaDePostagem;
  
  public static FragmentDialogEdit novaInstancia(long id) {
    Bundle bundle = new Bundle();
    bundle.putLong(EXTRA_ID, id);
    FragmentDialogEdit fragmentDialogEdit = new FragmentDialogEdit();
    fragmentDialogEdit.setArguments(bundle);
    return fragmentDialogEdit;
  }
  
  public void onClick(View view) {
    String titulo = this.inputTitulo.getText().toString();
    String ultimoEp = this.inputUltimoEp.getText().toString();
    int diaDePostagemSelecionado = this.spinerDiaDePostagem.getSelectedItemPosition();
    if (titulo.trim().isEmpty()) {
      this.inputTitulo.setError(getString(R.string.erro_campo_vazio));
      return;
    } 
    if (ultimoEp.trim().isEmpty()) {
      this.inputUltimoEp.setError(getString(R.string.erro_campo_vazio));
      return;
    } 
    Anime anime = new Anime(this.id, titulo, Integer.parseInt(ultimoEp), diaDePostagemSelecionado);
    this.mRepositorio.salvar(anime);
    dismiss();
  }
  
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRetainInstance(true);
    this.mRepositorio = new AnimeRepositorio(getActivity());
  }
  
  @NonNull
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_dialog_edit, null);
    this.inputTitulo = view.findViewById(R.id.inputTitulo);
    this.inputUltimoEp = view.findViewById(R.id.inputUltimoEp);
    this.spinerDiaDePostagem = view.findViewById(R.id.spnDiaDePostagem);
    Button btnSalvar = view.findViewById(R.id.btnSalvar);
    btnSalvar.setOnClickListener(this);
    ArrayAdapter<String> arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1);
    arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
    arrayAdapter.addAll(getResources().getStringArray(R.array.dias_da_semana));
    this.spinerDiaDePostagem.setAdapter(arrayAdapter);

    if (getArguments() != null) {
      if (getArguments().getLong(EXTRA_ID) != 0L) {
        this.id = getArguments().getLong(EXTRA_ID);
        Cursor cursor = this.mRepositorio.buscarPorID(this.id);
        if (cursor.moveToNext()) {
          this.inputTitulo.setText(cursor.getString(cursor.getColumnIndex(AnimeSQLHelper.COL_TITULO)));
          this.inputUltimoEp.setText(cursor.getString(cursor.getColumnIndex(AnimeSQLHelper.COL_ULTIMO_EP)));
          this.spinerDiaDePostagem.setSelection(cursor.getInt(cursor.getColumnIndex(AnimeSQLHelper.COL_DIA_DE_POSTAGEM)));
        } 
        cursor.close();
      } 
    }

    AlertDialog.Builder builder = (new AlertDialog.Builder(getActivity())).setView(view);
    if (this.id == 0L) {
      builder.setTitle(R.string.novo_lembrete);
      btnSalvar.setText(R.string.salvar);
    } else {
      builder.setTitle(R.string.atualizar);
      btnSalvar.setText(R.string.editar_lembrete);
    } 

    return builder.create();
  }
}


