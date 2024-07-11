package org.mybatis.example;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class MybatisMain {
  public static void main(String[] args) throws IOException {
    String resource = "mybatis-config.xml";
    InputStream inputStream = Resources.getResourceAsStream(resource);
    SqlSessionFactory sqlSessionFactory =
      new SqlSessionFactoryBuilder().build(inputStream);

    // 这种方法只需要org/mybatis/example/BlogMapper.xml中的namespace正确注册到mybatis-config.xml即可，不需要selectBlog接口
    try (SqlSession session = sqlSessionFactory.openSession()) {
      // statement为注册到mybatis-config.xml中的命名空间.方法名
      Blog blog = session.selectOne("org.mybatis.example.BlogMapper.selectBlog", 1);
      System.out.println(blog);
    }

    // 这种方法既需要org/mybatis/example/BlogMapper.xml中的namespace正确注册到mybatis-config.xml，又需要selectBlog接口
    // 如果接口方法上的@Select和BlogMapper.xml中的selectBlog标签sql只能存在一个，否则就会报错
    try (SqlSession session = sqlSessionFactory.openSession()) {
      BlogMapper mapper = session.getMapper(BlogMapper.class);
      Blog blog = mapper.selectBlog(1);
      System.out.println(blog);
    }

  }
}
