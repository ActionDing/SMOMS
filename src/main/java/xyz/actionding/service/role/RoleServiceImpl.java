package xyz.actionding.service.role;

import org.junit.jupiter.api.Test;
import xyz.actionding.dao.BaseDao;
import xyz.actionding.dao.role.RoleDao;
import xyz.actionding.dao.role.RoleDaoImpl;
import xyz.actionding.pojo.Role;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Actionding
 * @create 2022-06-06 8:23
 */
public class RoleServiceImpl implements RoleService {
    private RoleDao roleDao;

    public RoleServiceImpl() {
        roleDao = new RoleDaoImpl();
    }

    @Override
    public List<Role> getRoleList() {
        Connection conn = BaseDao.getConnection();
        List<Role> roleList = null;
        try {
            roleList = roleDao.getRoleList(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(conn, null, null);
        }
        return roleList;
    }

    @Test
    public void test() {
        RoleService roleService = new RoleServiceImpl();
        List<Role> roleList = roleService.getRoleList();
        for (Role role :
                roleList) {
            System.out.println(role.getRoleName());
        }
    }
}
