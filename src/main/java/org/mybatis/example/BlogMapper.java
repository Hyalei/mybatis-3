package org.mybatis.example;

import org.apache.ibatis.annotations.Select;

public interface BlogMapper {
//  @Select("SELECT * FROM blog WHERE id = 2")
  Blog selectBlog(int id);
}
