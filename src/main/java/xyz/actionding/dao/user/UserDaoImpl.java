package xyz.actionding.dao.user;

import com.mysql.cj.util.StringUtils;
import xyz.actionding.dao.BaseDao;
import xyz.actionding.pojo.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Actionding
 * @create 2022-06-05 14:31
 */
public class UserDaoImpl implements UserDao {
    @Override
    public User getLoginUser(Connection conn, String userCode) throws SQLException {
        User user = null;

        if (conn != null) {
            String sql = "select * from smoms_user where userCode = ?;";
            Object[] params = {userCode};

            PreparedStatement ps = null;
            ResultSet rs = BaseDao.executeQuery(conn, ps, sql, params);
            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUserCode(rs.getString("userCode"));
                user.setUserName(rs.getString("userName"));
                user.setUserPassword(rs.getString("userPassword"));
                user.setGender(rs.getInt("gender"));
                user.setBirthday(rs.getDate("birthday"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("address"));
                user.setUserRole(rs.getInt("userRole"));
                user.setCreatedBy(rs.getInt("createdBy"));
                user.setCreationDate(rs.getDate("creationDate"));
                user.setModifyBy(rs.getInt("modifyBy"));
                user.setModifyDate(rs.getDate("modifyDate"));
            }
            BaseDao.closeResource(null, ps, rs);
        }
        return user;
    }

    @Override
    public int updatePwd(Connection conn, int id, String password) throws SQLException {
        int execute = 0;
        PreparedStatement ps = null;
        if (conn != null) {
            String sql = "update smoms_user set userPassword = ? where id = ?;";
            Object[] params = {password, id};
            execute = BaseDao.executeUpdate(conn, ps, sql, params);
        }
        return execute;

    }

    @Override
    public int getUserCount(Connection conn, String username, int userRole) throws SQLException {
        int count = 0;
        if (conn != null) {
            StringBuilder sql = new StringBuilder();
            sql.append("select count(1) as count from smoms_user u, smoms_role r where u.userRole = r.id");
            ArrayList<Object> list = new ArrayList<>();

            if (!StringUtils.isNullOrEmpty(username)) {
                sql.append(" and u.userName like ?");
                list.add("%" + username + "%"); // index: 0
            }

            if (userRole > 0) {
                sql.append(" and u.userRole = ?");
                list.add(userRole); // index: 1
            }

            Object[] params = list.toArray();
            System.out.println("getUserCount --> " + sql);

            PreparedStatement ps = null;
            ResultSet rs = BaseDao.executeQuery(conn, ps, sql.toString(), params);

            if (rs.next()) {
                count = rs.getInt("count");
            }

            BaseDao.closeResource(null, ps, rs);
        }
        return count;
    }

    @Override
    public List<User> getUserList(Connection conn, String username, int userRole, int currentPageNo, int pageSize) throws SQLException {
        List<User> userList = new ArrayList<>();
        if (conn != null) {
            StringBuilder sql = new StringBuilder();
            sql.append("select u.*, r.roleName as userRoleName from smoms_user u, smoms_role r where u.userRole = r.id");
            List<Object> list = new ArrayList<>();
            if (!StringUtils.isNullOrEmpty(username)) {
                sql.append(" and u.userName like ?");
                list.add("%" + username + "%");
            }

            if (userRole > 0) {
                sql.append(" and userRole = ?");
                list.add(userRole);
            }

            sql.append(" order by creationDate DESC limit ?, ?");
            currentPageNo = (currentPageNo - 1) * pageSize;
            list.add(currentPageNo);
            list.add(pageSize);

            Object[] params = list.toArray();
            System.out.println("getUserList -- >" + sql);
            PreparedStatement ps = null;
            ResultSet rs = BaseDao.executeQuery(conn, ps, sql.toString(), params);

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUserCode(rs.getString("userCode"));
                user.setUserName(rs.getString("userName"));
                user.setGender(rs.getInt("gender"));
                user.setBirthday(rs.getDate("birthday"));
                user.setPhone(rs.getString("phone"));
                user.setUserRole(rs.getInt("userRole"));
                user.setUserRoleName(rs.getString("userRoleName"));
                userList.add(user);
            }
        }
        return userList;
    }
}
