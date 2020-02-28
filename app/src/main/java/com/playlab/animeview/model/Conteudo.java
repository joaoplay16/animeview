package com.playlab.animeview.model;

public abstract class Conteudo {
  private int diaDePostagem;
  
  private long id;
  
  private String titulo;
  
  private int ultimoEpAssistido;

  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!(obj instanceof Conteudo))
      return false;
    obj = obj;
    return (this.id == ((Conteudo)obj).id);
  }

  public int hashCode() {
    return (int)(this.id ^ this.id >>> 32L);
  }


  public int getDiaDePostagem() {
    return this.diaDePostagem;
  }
  
  public long getId() {
    return this.id;
  }
  
  public String getTitulo() {
    return this.titulo;
  }
  
  public int getUltimoEpAssistido() {
    return this.ultimoEpAssistido;
  }
  

  
  public void setDiaDePostagem(int diaDePostagem) {
    this.diaDePostagem = diaDePostagem;
  }
  
  public void setId(long id) {
    this.id = id;
  }
  
  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }
  
  public void setUltimoEpAssistido(int ultimoEpAssistido) {
    this.ultimoEpAssistido = ultimoEpAssistido;
  }
}
