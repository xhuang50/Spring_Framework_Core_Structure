package com.redline.springcore.dao;

import com.redline.springcore.po.User;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserDaoImpl implements UserDao {
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void init(){
        System.out.println("Invoke init method from UserDaoImpl.");
    }

    @Override
    public List<User> queryUserList(Map<String, Object> param) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {
            // get connection through dataSource
            connection = dataSource.getConnection();

            // Declare the SQL statement with a placeholder
            String sql = "select * from user where username = ?";

            // get prepared statement
            preparedStatement = connection.prepareStatement(sql);

            // set parameter
            preparedStatement.setObject(1, param.get("username"));

            // execute the query
            rs = preparedStatement.executeQuery();

            // iterate the result set
            List<User> results = new ArrayList<User>();
            User result = null;
            Class<?> clazz = User.class;
            while (rs.next()) {
                result = (User) clazz.newInstance();
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                for (int i = 0; i < columnCount; i++) {
                    String columnName = metaData.getColumnName(i + 1);
                    Field field = clazz.getDeclaredField(columnName);
                    field.setAccessible(true);

                    field.set(result, rs.getObject(i + 1));
                }

                results.add(result);
            }

            return results;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // close resources
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();

                }
            }
        }

        return null;
    }
}
