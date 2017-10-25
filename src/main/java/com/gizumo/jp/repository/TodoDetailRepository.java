package com.gizumo.jp.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoDetailRepository extends JpaRepository<TodoDetail, Integer> {

  @Query("select a from TodoDetail a where (a.title like %:keyword% or a.detail like %:keyword%) and a.doFlg = :endFlg order by a.id asc")
  List<TodoDetail> findTodoDetail(@Param("keyword") String keyword, @Param("endFlg") Boolean endFlg);
  @Query("select a from TodoDetail a where (a.title like %:keyword% or a.detail like %:keyword%) order by a.id asc")
  List<TodoDetail> findTodoDetail(@Param("keyword") String keyword);
  @Query("select a from TodoDetail a where a.doFlg = :flg order by a.id asc")
  List<TodoDetail> findBydoFlg(@Param("flg") Boolean endFlg);
}
