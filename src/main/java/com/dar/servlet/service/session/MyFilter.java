package com.dar.servlet.service.session;


import com.dar.backend.sql.Trip;

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

        boolean whitelist = path.equals("/") || path.equals("/index.html") || path.equals("/html/error.html") || path.equals("/sign-up") 
        		|| path.equals("/html/about.html") || path.equals("/html/contact.html") || path.equals("/html/developers.html")
        		|| path.equals("/html/help-and-faqs.html") || path.equals("/html/privacy-and-terms.html") || path.equals("/html/account.html")
                || path.equals("/sign-in") || path.startsWith("/images") || path.equals("/account-creation") || path.endsWith(".css")
                || path.endsWith(".map") || path.endsWith(".js") || path.endsWith(".jpg") || path.endsWith(".png") || path.endsWith(".ttf");

        if(loggedIn || whitelist || path.startsWith(ResourceHandler.RESOURCE_IDENTIFIER)){
            if(path.startsWith("/overview") && loggedIn){
                String uname = (String) session.getAttribute("uname");
                int id_trip = Integer.parseInt(request.getParameter("id"));
                try{
                    if(!Trip.checkIfUserIsInTrip(id_trip, uname)){
                        System.out.println(new java.util.Date().toString() + " | Illegal trip access attemps " + " Uname : " + uname + " Trip : " + id_trip);
                        httpResponse.sendRedirect(httpRequest.getContextPath());
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                    httpResponse.sendRedirect(httpRequest.getContextPath());
                }
            }
            filterChain.doFilter(request, response);
        }
        else{
            System.out.println(new java.util.Date().toString() + " | Blocked : " + path);
            httpResponse.sendRedirect(httpRequest.getContextPath());
        }
    }

    public void destroy() { }

}
