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
package org.druid.example;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.apache.ibatis.io.Resources;
import org.mybatis.example.Blog;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DruidMain {

  private static String tableName = "blog";
  private static DataSource dataSource;

  static {

    Properties prop = new Properties();
    try (InputStream input = Resources.getResourceAsStream("druid.properties")) {
      prop.load(input);
      dataSource = DruidDataSourceFactory.createDataSource(prop);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static void main(String[] args) {
    testDruidTruncate();
    testDruidInsert();
    testDruidQuery();
  }

  /**
   * 清空blog表
   */
  private static void testDruidTruncate() {
    try (Connection conn = dataSource.getConnection();
         Statement stmt = conn.createStatement()) {
      String sql = "TRUNCATE TABLE " + tableName;
      try {
        // 执行TRUNCATE TABLE语句时，execute()的返回值恒为false，因此不能通过execute()的返回值的返回值确定sql是否执行成功
        boolean execute = stmt.execute(sql);
        System.out.println("table named" + tableName + " is truncated");
      } catch (SQLException e) {
        System.out.println("table named" + tableName + " is not truncated!!!");
        throw new RuntimeException(e);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private static void testDruidQuery() {
    try (Connection conn = dataSource.getConnection();
         Statement stmt = conn.createStatement()) {
      String sql = "select * from blog";
      ResultSet resultSet = stmt.executeQuery(sql);
      while (resultSet.next()) {
        Blog blog = new Blog();
        blog.setId(resultSet.getInt("id"));
        blog.setTitle(resultSet.getString("title"));
        blog.setContent(resultSet.getString("content"));
        System.out.println(blog);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

  }

  private static void testDruidInsert() {
    try (Connection conn = dataSource.getConnection();
         Statement stmt = conn.createStatement()) {
      String sql1 = "insert into blog (title, content) values ('testJDBCInsert1', 'testJDBCInsert1')";
      String sql2 = "insert into blog (title, content) values ('testJDBCInsert2', 'testJDBCInsert2')";
      int execute1 = stmt.executeUpdate(sql1);
      int execute2 = stmt.executeUpdate(sql2);
      if (execute1>0 && execute2>0) {
        System.out.println("insert success");
      } else {
        System.out.println("insert failed");
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
