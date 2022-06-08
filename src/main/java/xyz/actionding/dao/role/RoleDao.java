package xyz.actionding.dao.role;

import xyz.actionding.pojo.Role;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Actionding
 * @create 2022-06-06 8:21
 */
public interface RoleDao {
    // 获取角色列表
    List<Role> getRoleList(Connection conn) throws SQLException;
}
