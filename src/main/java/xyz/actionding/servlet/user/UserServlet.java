package xyz.actionding.servlet.user;

import com.alibaba.fastjson.JSONArray;
import com.mysql.cj.util.StringUtils;
import xyz.actionding.pojo.Role;
import xyz.actionding.pojo.User;
import xyz.actionding.service.role.RoleService;
import xyz.actionding.service.role.RoleServiceImpl;
import xyz.actionding.service.user.UserService;
import xyz.actionding.service.user.UserServiceImpl;
import xyz.actionding.util.Constants;
import xyz.actionding.util.PageSupport;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

/**
 * @author Actionding
 * @create 2022-06-05 19:38
 */
public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if (method != null && method.equals("savepwd")) {
            this.updatePwd(req, resp);
        } else if (method != null && method.equals("pwdmodify")) {
            this.pwdModify(req, resp);
        } else if (method != null && method.equals("query")) {
            this.query(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    /**
     * 修改密码
     *
     * @param req
     * @param resp
     */
    private void updatePwd(HttpServletRequest req, HttpServletResponse resp) {
        Object obj = req.getSession().getAttribute(Constants.USER_SESSION);

        String newpassword = req.getParameter("newpassword");

        boolean flag = false;
        if (obj != null && newpassword != null) {
            UserServiceImpl userService = new UserServiceImpl();
            flag = userService.updatePwd(((User) obj).getId(), newpassword);
            if (flag) {
                req.setAttribute("message", "修改密码成功,请退出,使用新密码登录");

                req.getSession().removeAttribute(Constants.USER_SESSION);
            } else {
                req.setAttribute("message", "密码修改失败");
            }
        } else {
            req.setAttribute("message", "新密码有问题");
        }
        try {
            req.getRequestDispatcher("pwdmodify.jsp").forward(req, resp);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 验证旧密码,session中有用户的密码
     *
     * @param req
     * @param resp
     * @return
     */
    private void pwdModify(HttpServletRequest req, HttpServletResponse resp) {
        Object obj = req.getSession().getAttribute(Constants.USER_SESSION);
        String oldpassword = req.getParameter("oldpassword");

        // 万能的map
        HashMap<String, String> resultMap = new HashMap<>();
        // session失效
        if (obj == null) {
            resultMap.put("result", "sessionerror");
        } else if (StringUtils.isNullOrEmpty(oldpassword)) {
            resultMap.put("result", "error");
        } else {
            String userPassword = ((User) obj).getUserPassword();
            if (oldpassword.equals(userPassword)) {
                resultMap.put("result", "true");
            } else {
                resultMap.put("result", "false");
            }
        }

        try {
            resp.setContentType("application/json");
            PrintWriter writer = resp.getWriter();
            writer.write(JSONArray.toJSONString(resultMap));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void query(HttpServletRequest req, HttpServletResponse resp) {
        // 从前端获取数据
        String queryName = req.getParameter("queryName");
        String queryRole = req.getParameter("queryUserRole");
        String pageIndex = req.getParameter("pageIndex");
        int queryUserRole = 0;

        if (queryName == null) {
            queryName = "";
        }

        if (queryRole != null && !queryRole.equals("")) {
            queryUserRole = Integer.parseInt(queryRole);
        }
        req.setAttribute("queryUserName", queryName);
        req.setAttribute("queryUserRole", queryRole);

        int pageSize = 5; // 可以写到配置文件中
        int currentPageNo = 1;
        if (pageIndex != null) {
            currentPageNo = Integer.parseInt(pageIndex); // 异常处理
        }


        // 获取用户总数
        UserService userService = new UserServiceImpl();
        int totalCount = userService.getUserCount(queryName, queryUserRole);
        // 页面处理
        PageSupport pageSupport = new PageSupport();
        pageSupport.setCurrentPageNo(currentPageNo);
        pageSupport.setPageSize(pageSize);
        pageSupport.setTotalCount(totalCount);

        int totalPageCount = pageSupport.getTotalPageCount();
        // 首页和尾页
        if (currentPageNo < 1) {
            currentPageNo = 1;
        } else if (currentPageNo > totalCount) {
            currentPageNo = totalCount;
        }

        req.setAttribute("totalCount", totalCount);
        req.setAttribute("currentPageNo", currentPageNo);
        req.setAttribute("totalPageCount", totalPageCount);

        // 获取用户列表
        List<User> userList = userService.getUserList(queryName, queryUserRole, currentPageNo, pageSize);
        req.setAttribute("userList", userList);


        // 获取角色列表
        RoleService roleService = new RoleServiceImpl();
        List<Role> roleList = roleService.getRoleList();
        req.setAttribute("roleList", roleList);

        try {
            req.getRequestDispatcher("/jsp/userlist.jsp").forward(req, resp);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }

    }
}
