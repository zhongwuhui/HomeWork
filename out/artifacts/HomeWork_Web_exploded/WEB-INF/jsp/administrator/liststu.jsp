<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <%@include file="/WEB-INF/style.jspf"%>
    <style type="text/css">
        body {margin:10px;}
        .datagrid .panel-body{padding:0;}
        input select {
            border:1px solid #ccc;
        }

    </style>
</head>
<body>
    <table id="dg" class="easyui-datagrid" url="list_stu.action" nowrap="true"
    rownumbers="true" toolbar="#toolbar" pagination="true" pageSize="15" pageList="[10,15,20,30,40]" fitColumns="true">
        <thead>
            <tr>
                <th field="checkbox" checkbox="true"></th>
                <th field="userId" align="center" width="50" data-options="sortable:true">ID</th>
                <th field="userName" align="center" width="50" data-options="sortable:true">姓名</th>
                <th field="userNumber" align="center" width="50" data-options="sortable:true">学号</th>
            </tr>
        </thead>
    </table>
    <div id="toolbar">
        <div>
            <span>学号：</span>
            <input prompt="请输入关键字" id="userNumberSearch" class="easyui-textbox search">
            <span>姓名：</span>
            <input prompt="请输入关键字" id="userNameSearch" class="easyui-textbox search">
            <a class="easyui-linkbutton" iconCls="icon-search" onclick="doSearch()">搜索</a>
            <a class="easyui-linkbutton" iconCls="icon-reset" onclick="reset()">重置</a>
        </div>
        <div style="margin-top:10px;">
            <a class="easyui-linkbutton" iconCls="icon-add" onclick="add()">添加用户</a>
            <a class="easyui-linkbutton" iconCls="icon-remove" onclick="deleteByIds()">删除用户</a>
            <a class="easyui-linkbutton" iconCls="icon-add" onclick="StudentImport()">导入学生</a>
        </div>
    </div>
    <div id="dlg" class="easyui-dialog" style="width:400px" closed="true" buttons="#dlg-buttons">
        <form id="fm" method="post" novalidate="true" style="margin:0;padding:20px 50px">
                <input name="createTime" type="hidden">
            <div style="margin-bottom:10px">
                <input required="true" name="username" label="用户名：" style="width:100%" class="easyui-textbox"
                missingMessage="用户名不能为空" id="username">
            </div>
            <div style="margin-bottom:10px">
                <input required="true" name="password" label="密码：" style="width:100%" class="easyui-textbox"
                missingMessage="密码不能为空" type="password" id="password">
            </div>
            <div style="margin-bottom:10px">
                <input name="role" label="角色：" style="width:100%" class="easyui-textbox">
            </div>
        </form>
    </div>
    <div id="dlg-buttons">
        <a class="easyui-linkbutton" iconCls="icon-ok" onclick="submit()" style="width:90px">确定</a>
        <a class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')" 
        style="width:90px">取消</a>
    </div>
    
    
    <div id="uploadexcel" class="easyui-dialog" style="width:400px" closed="true" buttons="#dlg-buttonforImport">
        <form id="sudenttable" method="post" novalidate="true" name="ffm" enctype="multipart/form-data" style="margin:0;padding:20px 50px">
            <div style="margin-bottom:10px">
                <input name="file" label="选择excel：" style="width:100%" class="easyui-filebox"
                 id="file">
            </div> 
        </form>
    </div>
    <div id="dlg-buttonforImport">
        <a class="easyui-linkbutton" iconCls="icon-ok" onclick="submitFile()" style="width:90px">导入</a>
        <a class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#uploadexcel').dialog('close')" style="width:90px">取消</a>
    </div>
    <script type="text/javascript">
    
    function StudentImport(){
        $('#uploadexcel').dialog('open').dialog('center').dialog('setTitle','添加附件');
        $('#sudenttable').form('clear');
        $('#sudenttable').form('disableValidation');
    }
    function submitFile(){
        //1:获取表单  
        var form1=document.ffm;  
        //2.设置表单的action属性  
        url='readXLSX.action';  
        //3.提交表单  
        $('#sudenttable').form('submit',{
            url: url,
            onSubmit: function(){
                return $(this).form('validate')          ;
            },
            success: function(result){
                if(result == 'success') {
                    $('#uploadexcel').dialog('close');
                    $.messager.show({
                        title: '提示',
                        msg: '导入成功！'
                    }); 
                    $('#dlg').dialog('close');
                    $('#dg').datagrid('reload');
                }
             }
        })
    }
    
             
        function add(){
            $('#dlg').dialog('open').dialog('center').dialog('setTitle','添加用户');
            $('#fm').form('clear');
            $('#password').textbox({    
                prompt: ''
            });
            $('#username').textbox('readonly',false);
            //窗体弹出默认是禁用验证，因为刚弹出的窗口，用户还没填就显示的话，太丑
            $('#fm').form('disableValidation');
            url = 'user_save.action';
        }
        
        function edit(){
            //判断是否有选中行记录，使用getSelections获取选中的所有行  
            var rows = $('#dg').datagrid('getSelections');  
            if(rows.length != 1) {
                $.messager.show({
                    title:'警告',  
                    msg:'请选择且只能选择一条记录！'
                });  
            } else{
                $('#dlg').dialog('open').dialog('center').dialog('setTitle','修改用户');
                $('#fm').form('load',rows[0]);
                $('#username').textbox('readonly');
                $('#password').textbox({    
                    prompt: '若不更新无需输入',
                    required: false,
                    value: ''
                });
                url = 'user_save.action?id='+rows[0].id;
            }
        }
        
        function submit(){
            //开启验证
            $('#fm').form('enableValidation');
            $('#fm').form('submit',{
                url: url,
                onSubmit: function(){
                    return $(this).form('validate');
                },
                success: function(result){
                    if(result == 'exist') {
                        $.messager.show({
                            title: '警告',
                            msg: '该用户名已存在，请重新输入！'
                        }); 
                    } else {
                        $('#dlg').dialog('close');
                        $('#dg').datagrid('reload');
                    }
                }
            });
        }
        
        function deleteByIds(){
            var rows = $('#dg').datagrid('getSelections');  
            //返回被选中的行，如果没有任何行被选中，则返回空数组  
            if(rows.length == 0) {
                $.messager.show({
                    title:'警告',  
                    msg:'请至少选择一条记录！'
                });  
            } else {  
                //提示是否确认删除，如果确认则执行删除的逻辑  
                $.messager.confirm('警告', '您确定要删除选中的记录吗？删除后无法恢复！', function(r){  
                    if (r){  
                        // 从获取的记录中获取相应的的id,拼接id的值，然后发送到后台1,2,3,4  
                        var ids = '';  
                        for(var i = 0; i < rows.length; i ++) {  
                            ids += rows[i].userId + ',';  
                        }  
                        ids = ids.substr(0, ids.lastIndexOf(','));
                        $.post('student_delete.action',{ids:ids},function(result){
                            if(result == 'true') {
                                //刷新当前页，查询的时候我们用的是load，刷新第一页，reload是刷新当前页
                                $('#dg').datagrid('reload');
                            } else {  
                                $.messager.show({   
                                    title:'警告',  
                                    msg:'删除失败，请刷新后重试！'
                                });  
                            }  
                        });  
                    }  
                });  
            }
        }
        
        function doSearch(){
            $('#dg').datagrid('load',{
                userName: $('#userNameSearch').val(),
                userNumber: $('#userNumberSearch').val()              
            });
        }
        
        function reset(){
            $('.search').textbox('reset');
        }
    </script>
</body>
</html>