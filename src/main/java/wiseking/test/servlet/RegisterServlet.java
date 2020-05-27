package wiseking.test.servlet;

import wiseking.test.comment.Constance;
import wiseking.test.dao.DAOFactory;
import wiseking.test.dao.UserDao;
import wiseking.test.entity.UserEntity;
import wiseking.test.util.HibernateUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

//@WebServlet("/RegisterServlet")
@WebServlet(name = "RegisterServlet", urlPatterns = "/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=utf-8");

        PrintWriter printWriter = response.getWriter();
        String username = request.getParameter(Constance.USER_NAME);
        String password = request.getParameter(Constance.PASSWORD);
        String confirmPassword = request.getParameter(Constance.CONFIRM_PASSWORD);

        UserDao userDao = DAOFactory.getInstance().getUserDAO(HibernateUtil.getSession());

        if (username == null || username.length() == 0) {
            printWriter.print(getErrorAlertMsg("用户名不能为空！"));
        } else {
            List<UserEntity> userList = userDao.queryInfo(Constance.USER_NAME, username);

            if (userList != null && !userList.isEmpty()) {
                for (UserEntity user : userList) {
                    if (user.getUserName().equals(username)) {
                        printWriter.print(getErrorAlertMsg("用户名已存在"));
                    }
                }
            }
        }
        if (password == null || password.length() == 0) {
            printWriter.print(getErrorAlertMsg("密码不能为空！"));
        } else if (!password.equals(confirmPassword)) {
            printWriter.print(getErrorAlertMsg("两次输入的密码不一致！"));
        }

        // 创建 User 对象
        UserEntity user = new UserEntity();

        user.setUserName(username);
        user.setPassword(password);

        // 往数据库插入新用户信息
        String result = userDao.insertUser(user);
        printWriter.print("username=" + username + ":" + result);
        String ctx = this.getServletContext().getContextPath();
        printWriter.println("<a href='" + ctx + "'>  back </a>");
        HibernateUtil.closeSession();

    }

    private String getErrorAlertMsg(String msg) {
        return "<script language='javascript'>alert('" + msg + "'); window.location.href='JSP/register.jsp';</script>";
    }
}
