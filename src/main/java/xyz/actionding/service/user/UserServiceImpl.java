package xyz.actionding.service.user;

import org.junit.jupiter.api.Test;
import xyz.actionding.dao.BaseDao;
import xyz.actionding.dao.user.UserDao;
import xyz.actionding.dao.user.UserDaoImpl;
import xyz.actionding.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Actionding
 * @create 2022-06-05 14:56
 */
public class UserServiceImpl implements UserService {

    private UserDao userDao;

    public UserServiceImpl() {
        userDao = new UserDaoImpl();
    }

    @Override
    public User login(String userCode, String password) {
        User user = null;
        Connection conn = null;
        try {
            conn = BaseDao.getConnection();
            user = userDao.getLoginUser(conn, userCode);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(conn, null, null);
        }
        return user;
    }

    @Override
    public boolean updatePwd(int id, String password) {
        Connection conn = null;
        boolean flag = false;
        try {
            conn = BaseDao.getConnection();
            if (userDao.updatePwd(conn, id, password) > 0) {
                flag = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(conn, null, null);
        }
        return flag;
    }

    @Override
    public int getUserCount(String username, int userRole) {
        Connection conn = BaseDao.getConnection();
        int count = 0;
        try {
            count = userDao.getUserCount(conn, username, userRole);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(conn, null, null);
        }
        return count;
    }

    @Override
    public List<User> getUserList(String username, int userRole, int currentPageNo, int pageSize) {
        Connection conn = BaseDao.getConnection();
        List<User> userList = null;
        try {
            userList = userDao.getUserList(conn, username, userRole, currentPageNo, pageSize);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(conn, null, null);
        }
        return userList;
    }

    @Test
    public void test() {
        UserServiceImpl userService = new UserServiceImpl();
        List<User> userList = userService.getUserList(null, 2, 1, 5);
        for (User user :
                userList) {
            System.out.println(user.getUserName());
        }
    }
}
