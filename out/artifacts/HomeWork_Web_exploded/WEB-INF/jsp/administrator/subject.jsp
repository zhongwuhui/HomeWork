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
    <table id="dg" class="easyui-datagrid" url="list_subject.action" nowrap="true"
    rownumbers="true" toolbar="#toolbar" pagination="true" pageSize="15" pageList="[10,15,20,30,40]" fitColumns="true">
        <thead>
            <tr>
                <th field="checkbox" checkbox="true"></th>
                <th data-options="field:'subjectId',align:'center',width:38,sortable:'true'">课程号</th>
                <th data-options="field:'subjectName',align:'center',width:38,sortable:'true'">课程名</th>
                <th data-options="field:'subjectToTeacher',align:'center',formatter:formatOperT,width:38">查看任课教师</th>
            </tr>
        </thead>
    </table>
    <div id="toolbar">
        <div>
            <span>课程号：</span>
            <input prompt="请输入关键字" id="subjectIdSearch" class="easyui-textbox search">
            <span>课程名：</span>
            <input prompt="请输入关键字" id="subjectNameSearch" class="easyui-textbox search">
            <a class="easyui-linkbutton" iconCls="icon-search" onclick="doSearch()">搜索</a>
            <a class="easyui-linkbutton" iconCls="icon-reset" onclick="reset()">重置</a>           
        </div>
        <div style="margin-top:10px;">
            <a class="easyui-linkbutton" iconCls="icon-add" onclick="add()">添加科目</a>
            <a class="easyui-linkbutton" iconCls="icon-edit" onclick="edit()">修改科目</a>
            <a class="easyui-linkbutton" iconCls="icon-remove" onclick="deleteByIds()">删除科目</a>
        </div>
    </div>
    <div id="dlg" class="easyui-dialog" style="width:400px" closed="true" buttons="#dlg-buttons">
        <form id="fm" method="post" novalidate="true" style="margin:0;padding:20px 50px">
            <div style="margin-bottom:10px">
                <input required="true" name="subjectName" label="科目名：" style="width:100%" class="easyui-textbox" id="subjectName">
            </div>
        </form>
    </div>
    <div id="dlg-buttons">
        <a class="easyui-linkbutton" iconCls="icon-ok" onclick="submit()" style="width:90px">确定</a>
        <a class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')" 
        style="width:90px">取消</a>
    </div>
    <script type="text/javascript">
        var url;       
        function add(){
            $('#dlg').dialog('open').dialog('center').dialog('setTitle','添加用户');
            $('#fm').form('clear');
            $('#subjectName').textbox('readonly',false);
            //窗体弹出默认是禁用验证，因为刚弹出的窗口，用户还没填就显示的话，太丑
            $('#fm').form('disableValidation');
            url = 'subject_save.action';
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
                $('#subjectName').textbox({    
                    prompt: '若不更新无需输入',
                    required: false,
                    value: ''
                });
                url = 'class_save.action?subjectId='+rows[0].subjectId;
            }
        }
        
        function submit(){
            //开启验证
            $('#fm').form('enableValidation');
            $('#fm').form('submit',{
                url: url,
                onSubmit: function(){
                    return $(this).form('validate')          ;
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
        function doSearch(){
            $('#dg').datagrid('load',{
                subjectName: $('#subjectNameSearch').val(),
                subjectId: $('#subjectIdSearch').val(),
            });
        }
        
        function reset(){
            $('.search').textbox('reset');
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
                            ids += rows[i].subjectId + ',';  
                        }  
                        ids = ids.substr(0, ids.lastIndexOf(','));
                        $.post('class_delete.action',{ids:ids},function(result){
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
              
        function formatOperT(value, row, index) {
            return "<a href='subject_to_teacher?subjectId=" + row.subjectId + "' target='_self'>" + "查看老师" + "</a>";
        }
    </script>
</body>
</html>