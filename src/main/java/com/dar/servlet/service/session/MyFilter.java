package com.dar.servlet.service.session;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.faces.application.ResourceHandler;
import java.io.IOException;

@WebFilter("/*")
public class MyFilter implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException { }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);
        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());
        boolean loggedIn = session != null && session.getAttribute("uname") != null;

        boolean whitelist = path.equals("/") || path.equals("/index.html") || path.equals("/html/sign-up.html") || path.equals("/sign-in")
                || path.startsWith("/images") || path.equals("/account-creation") || path.endsWith(".css") || path.endsWith(".map")
                || path.endsWith(".js") || path.endsWith(".jpg") || path.endsWith(".png") || path.endsWith(".ttf");

        if(loggedIn || whitelist || path.startsWith(ResourceHandler.RESOURCE_IDENTIFIER)){
            filterChain.doFilter(request, response);
        }
        else{
            System.out.println("Blocked : " + path);
            httpResponse.sendRedirect(httpRequest.getContextPath());
        }
    }

    public void destroy() { }

}
