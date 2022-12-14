package pofol.shop.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 폼 로그인 성공 시 핸들링을 설정하는 클래스입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-12-28
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-12-28
 */

public class FormLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    public FormLoginSuccessHandler(String defaultTargetUrl){
        setDefaultTargetUrl(defaultTargetUrl);
    }

    //로그인 성공 시 리다이렉션
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {

        HttpSession session = request.getSession();

        if (session != null) {

            String redirectUrl = (String) session.getAttribute("redirectUrl");

            if (redirectUrl != null) {
                session.removeAttribute("redirectUrl");
                getRedirectStrategy().sendRedirect(request, response, redirectUrl);
            }
            else {
                super.onAuthenticationSuccess(request, response, authentication);
            }
        }
        else {
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }

}
