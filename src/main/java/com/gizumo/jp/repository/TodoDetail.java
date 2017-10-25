package com.gizumo.jp.repository;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "todoDetail")
public class TodoDetail {

  @Id
  @Column(name="id")
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Integer id;
  @Column(name="title", nullable=false)
  private String title;
  @Column(name="detail")
  private String detail;
  @Column(name="doFlg")
  private boolean doFlg;
  @Temporal(TemporalType.DATE)
  @Column(name="update_date")
  private Date updateDate;

  public Integer getId() {
    return id;
  }
  public void setId(Integer id) {
    this.id = id;
  }
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
  public Boolean getDoFlg() {
    return doFlg;
  }
  public void setDoFlg(Boolean doFlg) {
    this.doFlg = doFlg;
  }
  public Date getUpdateDate() {
    return updateDate;
  }
  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  @Override
  public String toString() {
	  return "";
  }

}
