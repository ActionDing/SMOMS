package xyz.actionding.service.user;

import xyz.actionding.pojo.User;

import java.util.List;

/**
 * @author Actionding
 * @create 2022-06-05 14:55
 */
public interface UserService {
    // 用户登录
    User login(String userCode, String password);
    // 更改密码
    boolean updatePwd(int id, String password);
    // 查询记录数
    int getUserCount(String username, int userRole);
    // 查询用户列表
    List<User> getUserList(String username, int userRole, int currentPageNo, int pageSize);
}
