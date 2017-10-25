package com.gizumo.jp.web;

import java.io.Serializable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class TodoDetailForm implements Serializable {

  private static final long serialVersionUID = 1330043957072942381L;

  @NotNull
  @Size(min=1, max=30)
  private String title;
  @Size(min=1, max=100)
  private String detail;

  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }
  public String getDetail() {
    return detail;
  }
  public void setDetail(String detail) {
    this.detail = detail;
  }

  @Override
  public String toString() {
    return "";
  }

}
