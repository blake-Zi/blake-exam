package onlineexam.blakeexam.interceptor;

import onlineexam.blakeexam.common.QexzConst;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getSession().getAttribute(QexzConst.CURRENT_ACCOUNT) == null){
            response.sendRedirect("/");
            return false;
        }
        return true;
    }
}
