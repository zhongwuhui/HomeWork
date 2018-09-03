<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="/WEB-INF/style.jspf"%>
<script>
	//退出方法
	function exit(url) {
		layer.confirm('你确定要退出吗？', {
			btn : [ '确定', '取消' ] ,
			title : '警告' ,
			icon : 0
		//确定、取消按钮触发
		}, function() {
			window.open(url, "_self");
		}, function() {});
	}
</script>
</head>
	<body class="easyui-layout">
		<!-- begin of header -->
		<div class="wu-header" data-options="region:'north',border:false,split:true" style="height:50px;">
	    	<div class="wu-header-left">
	        	<h1>作业管理平台</h1>
	        	<%--<img src="images/logo.png" style="margin-top:3px;"/>
	        --%></div>
	        <div class="wu-header-right">
	        	<p><strong class="easyui-tooltip" title="2条未读消息">${sessionScope.user.userName }</strong>老师，欢迎您！</p>
	            <p><a href="login.jsp">网站首页</a>|<a href="#" onclick="exit('logout.action');return false">安全退出</a></p>
	        </div>
	    </div> 
	    <!-- end of header -->
	    
	    <!-- begin of left -->

	    <!-- begin of sidebar -->
		<div class="wu-sidebar" data-options="region:'west',split:true,border:true,title:'导航菜单'"> 
	    	<div class="easyui-accordion" data-options="border:false,fit:false">
	        	<div title="系统配置" data-options="iconCls:'icon-wrench'" style="padding:10px 0 5px 0;">  	
	    			<ul class="easyui-tree wu-side-tree">
	                    <li iconCls="icon-users"><a title="teach_query.action" 
	                    	data-icon="icon-users">管理作业</a></li>
	                </ul>
	            </div>
	        </div>
	        
	    </div>
	    <!-- end of sidebar --> 
	 
	    <!-- begin of left -->
	    
	    <!-- begin of main -->
	    <div class="wu-main" data-options="region:'center'">
	    <div class="sidebar-bg"></div>
        	<div id="tt" class="easyui-tabs" data-options="border:false,fit:true" style="margin-left:4px;">
				<div title="首页" data-options="iconCls:'icon-tip'">
					<iframe width="100%" height="100%" src="body.action" style="border:0;"></iframe>
				</div>
	        </div>
	    </div>
		<!-- 新的窗口不需要最小化，最大化，但是要锁住全屏 -->
		<div id="win" data-options="collapsible:false,minimizable:false,maximizable:false,modal:true"></div> 
	    <!-- end of main --> 
	</body>
</html>