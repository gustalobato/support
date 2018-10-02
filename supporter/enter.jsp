<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<form action="https://supportersclubportal.mancity.com/j_spring_security_check" name='_frm' method="post" id="sf-login-form">
                        <ul class="sf-login-form">
                            <li>
                                <label class="sf-sidebar-field">Email:</label>
                                
                                <input type="text" name="j_username" id="j_username" value='<%=request.getParameter("j_username")%>' >
                            </li>
                            <li>
                                <label class="sf-sidebar-field">Password:</label>
                                <input type="password" name="j_password" id="j_password" value='<%=request.getParameter("j_password")%>'/>
                            </li>
                            <li>
                                <button type="submit" class="button sf-login-button" name="Login">Login</button>
                            </li>
                        </ul>
                    </form>
                    
                    <script>
                    document._frm.Login.click();
                    </script>
</body>
</html>