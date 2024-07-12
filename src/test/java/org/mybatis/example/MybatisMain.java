/**
 *    Copyright 2009-2024 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.example;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;

public class MybatisMain {
  public static void main(String[] args) throws Exception {
    testMybatis();
  }

  private static void testMybatis() throws Exception {
    String resource = "mybatis-config.xml";
    InputStream inputStream = Resources.getResourceAsStream(resource);
    // inputStream在build()方法中会被关闭，不用担心资源泄露问题
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
