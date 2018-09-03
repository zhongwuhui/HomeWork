<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set value="${pageContext.request.contextPath }" var="homework" />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>作业提交系统</title>
<script type="text/javascript" src="js/jquery-3.1.1.min.js"></script>
<script type="text/javascript" src="layer/layer.js"></script>
<link rel="stylesheet" type="text/css" href="css/css.css">
<script type="text/javascript" src="js/cookie.js"></script>
<script>
//页面加载时触发的方法
window.onload = function(){  
    var usernumberValue = getCookieValue("usernumber");//获取cookie中名为usernumber的值
    $("#usernumber").val(usernumberValue);//把获取到的cookie中的值赋值给本页面id为usernumber的输入框
    var passwordValue = getCookieValue("password");//获取cookie中名为password的值
    $("#password").val( passwordValue); //把获取到的cookie中的值赋值给本页面id为password的输入框 
}
//按回车键触发某个事件
document.onkeydown=keyListener;
function keyListener(e)
{
    e = e ? e : event;
    if(e.keyCode == 13)
    {
        login();
    }
}   
//登录按钮方法
function login(){
   var usernumber=$("#usernumber").val();
   var password=$("#password").val();
   if(usernumber==""){
      layer.alert('用户名不能为空!', {
            icon : 0,
            title : '警告',
            offset: 'm'
        });
        return false;
   } else if(password==""){
         layer.alert('密码不能为空!', {
            icon : 0,
            title : '警告',
            offset: 'm'
        });
        return false;
   } else{
        // 标识返回结果
        var result;
        $.ajax(
                {
                    async:false, //要设置为同步的
                    url: "check.action",
                    data:{"usernumber":usernumber,"password":password},
                    type: "post",
                    success:function(data)
                    {
                        result = data;
                        //console.log(result);
                    }
                });
        if (result == "true") {
            setCookie("usernumber",usernumber,24,"/");//把usernumber的值保存在cookie名为usernumber中
            setCookie("password",password,24,"/");//把password的值保存在cookie名为password中
            return true;
        } else {
            layer.alert('用户名或密码错误!', {
                icon : 0,
                title : '警告',
                offset: 'm'
            });
            return false;
        }
   }
}
</script>
</head>
<body>
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
            <td height="147" background="images/top02.gif">
                <div align="center">
                    <span class="STYLE1">作业管理平台</span>
                </div>
            </td>
        </tr>
    </table>
    <table width="562" border="0" align="center" cellpadding="0"
        cellspacing="0" class="right-table03">
        <tr>
            <td width="221">
                <table width="95%" border="0" cellpadding="0" cellspacing="0"
                    class="login-text01">
                    <tr>
                        <td>
                            <table width="100%" border="0" cellpadding="0" cellspacing="0"
                                class="login-text01">
                                <tr>
                                    <td align="center"><img src="images/logo.jpg" width="250"
                                        height="80" />
                                    </td>
                                </tr>
                                <tr>
                                    <td height="40" align="center">&nbsp;</td>
                                </tr>
                            </table>
                        </td>
                        <td><img src="images/line01.gif" width="5" height="292" /></td>
                    </tr>
                </table>
            </td>
            <td>
                <form method="post" id="deng" action="${homework}/authority.action" >
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr>
                            <td colspan="2" align="center"><font color="red"></font>
                            </td>
                        </tr>
                        <tr>
                            <td width="31%" height="35" class="login-text02">用户名：<br />
                            </td>
                            <td width="69%"><input name="usernumber" type="text"
                                id="usernumber" size="20" />
                            </td>
                        </tr>
                        <tr>
                            <td height="35" class="login-text02">密 码：<br /></td>
                            <td><input name="password" type="password"
                                id="password" size="20" /></td>
                        </tr>
                        <!--  <tr>
                            <td height="35" class="login-text02">选择身份：<br /></td>
                            <td ><select id="identity" class="easyui-combobox" name="identity" style="width:173px;">   
                                 <option value="stu">学生</option>   
                                 <option value="tec">教职工</option>   
                                 <option value="adm">管理员</option> </select></td>
                        </tr> -->
                        <tr>
                            <td height="35">&nbsp;</td>
                            <td>
                              <input type="submit" class="right-button01" value="登录" onclick="return login()"/>
                            </td>
                        </tr>
                    </table>
                </form>
            </td>
        </tr>
    </table>
</body>
</html>