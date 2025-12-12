package com.household.backend.common;

import jakarta.servlet.http.HttpSession;

public class SessionUtils {
    private static final String USER_SESSION_KEY = "USER";

    public static Integer getLoginUserPk(HttpSession session) {
        Integer userPk = (Integer)session.getAttribute(USER_SESSION_KEY);
        if(userPk == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }
        return userPk;
    }

    public static void setLoginUser(HttpSession session, Integer userPk) {
        session.setAttribute(USER_SESSION_KEY, userPk);
    }

    public static void logout(HttpSession session) {
        session.invalidate();
    }
}
