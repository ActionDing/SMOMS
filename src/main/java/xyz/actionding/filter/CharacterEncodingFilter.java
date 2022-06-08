package xyz.actionding.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author Actionding
 * @create 2022-06-05 14:17
 */
public class CharacterEncodingFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
