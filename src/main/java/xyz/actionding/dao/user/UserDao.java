package xyz.actionding.dao.user;

import xyz.actionding.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Actionding
 * @create 2022-06-05 14:31
 */
public interface UserDao {
    // 获得要登录的用户
    User getLoginUser(Connection conn, String userCode) throws SQLException;
    // 修改当前用户密码
    int updatePwd(Connection conn, int id, String password) throws SQLException;
    // 根据用户名或用户角色查询用户总数
    int getUserCount(Connection conn, String username, int userRole) throws SQLException;
    // 获取用户列表
    List<User> getUserList(Connection conn, String username, int userRole, int currentPageNo, int pageSize) throws SQLException;
}
