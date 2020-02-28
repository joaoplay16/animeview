package com.playlab.animeview.model;

public class Anime extends Conteudo {
  public Anime(long id, String titulo, int ep, int diaDePostagem) {
    setId(id);
    setTitulo(titulo);
    setUltimoEpAssistido(ep);
    setDiaDePostagem(diaDePostagem);
  }

  
  public int getDiaDePostagem() {
    return super.getDiaDePostagem();
  }
  
  public long getId() {
    return super.getId();
  }
  
  public String getTitulo() {
    return super.getTitulo();
  }
  
  public int getUltimoEpAssistido() {
    return super.getUltimoEpAssistido();
  }
  
  public void setDiaDePostagem(int diaDePostagem) {
    super.setDiaDePostagem(diaDePostagem);
  }
  
  public void setId(long id) {
    super.setId(id);
  }
  
  public void setTitulo(String titulo) {
    super.setTitulo(titulo);
  }
  
  public void setUltimoEpAssistido(int ultimoEpAssistido) {
    super.setUltimoEpAssistido(ultimoEpAssistido);
  }

}


