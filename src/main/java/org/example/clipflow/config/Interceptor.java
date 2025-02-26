package org.example.clipflow.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.clipflow.entity.user.User;
import org.example.clipflow.holder.UserHolder;
import org.example.clipflow.service.user.UserService;
import org.example.clipflow.utils.JwtUtil;
import org.example.clipflow.utils.R;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
public class Interceptor implements HandlerInterceptor {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final UserService userService;

    public Interceptor(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }

        if (JwtUtil.isEmpty(request)) {
            response(R.error().setMessage("请登录后再操作"), response);
            return false;
        }

        final Long userId = JwtUtil.getUserId(request);
        final User user = userService.getById(userId);

        if (ObjectUtils.isEmpty(user)) {
            response(R.error().setMessage("用户不存在"), response);
            return false;
        }
        UserHolder.setUserId(userId);
        return true;
    }

    private void response(R r, HttpServletResponse response) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control", "no-cache");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().println(objectMapper.writeValueAsString(r));
        response.getWriter().flush();
    }
}
