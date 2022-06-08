package xyz.actionding.dao.role;

import xyz.actionding.dao.BaseDao;
import xyz.actionding.pojo.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Actionding
 * @create 2022-06-06 8:22
 */
public class RoleDaoImpl implements RoleDao {
    @Override
    public List<Role> getRoleList(Connection conn) throws SQLException {
        List<Role> roleList = new ArrayList<>();
        if (conn != null) {
            String sql = "select * from smoms_role;";
            PreparedStatement ps = null;
            ResultSet rs = BaseDao.executeQuery(conn, ps, sql);
            while (rs.next()) {
                Role role = new Role();
                role.setId(rs.getInt("id"));
                role.setRoleCode(rs.getString("roleCode"));
                role.setRoleName(rs.getString("roleName"));
                roleList.add(role);
            }
            BaseDao.closeResource(null, ps, rs);
        }
        return roleList;
    }
}
