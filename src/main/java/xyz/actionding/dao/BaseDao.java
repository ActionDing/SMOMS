package xyz.actionding.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * 操作数据库的公共类
 *
 * @author Actionding
 * @create 2022-06-05 13:54
 */
public class BaseDao {
    private static String driver;
    private static String url;
    private static String username;
    private static String password;

    //
    static {
        Properties properties = new Properties();
        InputStream is = BaseDao.class.getClassLoader().getResourceAsStream("db.properties");

        try {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        driver = properties.getProperty("driver");
        url = properties.getProperty("url");
        username = properties.getProperty("username");
        password = properties.getProperty("password");
    }

    /**
     * 获取数据库连接
     */
    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * 查询的公共方法
     *
     * @param conn   数据库连接
     * @param sql    SQL语句
     * @param params 参数列表
     * @return 数据集
     */
    public static ResultSet executeQuery(Connection conn, PreparedStatement ps, String sql, Object... params) throws SQLException {
        ps = conn.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            ps.setObject(i + 1, params[i]);
        }
        return ps.executeQuery();
    }

    /**
     * 增删改的公共方法
     *
     * @param conn   数据库连接
     * @param sql    SQL语句
     * @param params 参数列表
     * @return 数据集
     */
    public static int executeUpdate(Connection conn, PreparedStatement ps, String sql, Object... params) throws SQLException {
        ps = conn.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            ps.setObject(i + 1, params[i]);
        }
        return ps.executeUpdate();
    }

    /**
     * 释放资源
     *
     * @param conn Connection
     * @param ps   PreparedStatement
     * @param rs   ResultSet
     * @return boolean
     */
    public static boolean closeResource(Connection conn, PreparedStatement ps, ResultSet rs) {
        boolean flag = true;
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
                flag = false;
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
                flag = false;
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
                flag = false;
            }
        }
        return flag;
    }
}
