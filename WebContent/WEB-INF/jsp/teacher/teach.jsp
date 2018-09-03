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
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
    <table id="dg" class="easyui-datagrid" url="list_work.action" nowrap="true"
    rownumbers="true" toolbar="#toolbar" pagination="true" pageSize="15" pageList="[10,15,20,30,40]" fitColumns="true">
        <thead>
            <tr>
                <th field="checkbox" checkbox="true"></th>
                <th data-options="field:'workId',align:'center',width:50,sortable:'true'">作业编号</th>
                <th data-options="field:'workDetail',align:'center',width:50,sortable:'true'">作业内容</th>
                <th data-options="field:'createTime',align:'center',width:50,sortable:'true'">创建时间</th>              
                <th data-options="field:'className',align:'center',width:50">所属班级</th>
                <th data-options="field:'classId',hidden:'true'"></th>
                <th data-options="field:'subjectName',align:'center',width:50">所属科目</th>
                <th data-options="field:'subjectId',hidden:'true'"></th>
                <th data-options="field:'path',align:'center',width:38,formatter:filePath">文件</th>
            </tr>
        </thead>
    </table>
    <div id="toolbar">
        <div>
            <span>科目：</span>
            <input prompt="请输入科目名称" id="subjectNameSearch" class="easyui-textbox search">
            <span>班级：</span>
            <input prompt="请输入班级名称" id="classNameSearch" class="easyui-textbox search">
            <span>创建时间：</span>
            <input style="width:160px" prompt="请输入起始时间" id="createTimeStart" class="easyui-datetimebox search">
            <span>--</span>
            <input style="width:160px" prompt="请输入结束时间" id="createTimeEnd" class="easyui-datetimebox search">
            <a class="easyui-linkbutton" iconCls="icon-search" onclick="doSearch()">搜索</a>
            <a class="easyui-linkbutton" iconCls="icon-reset" onclick="reset()">重置</a>
        </div>
        <div style="margin-top:10px;">
            <a class="easyui-linkbutton" iconCls="icon-add" onclick="add()">添加作业</a>
            <a class="easyui-linkbutton" iconCls="icon-edit" onclick="edit()">修改作业内容</a>
            <a class="easyui-linkbutton" iconCls="icon-remove" onclick="deleteByIds()">删除作业</a>
        </div>
    </div>
    <div id="dlg" class="easyui-dialog" style="width:400px" closed="true" buttons="#dlg-button">
        <form id="fm" method="post" novalidate="true" style="margin:0;padding:20px 50px">
            <input name="createTime" id="createTime" type="hidden" class= "easyui-datebox">
            <div style="margin-bottom:10px">
                <input required="true" name="workDetail" label="作业内容：" style="width:100%" class="easyui-textbox"
                missingMessage="作业内容不能为空" id="workDetail">
            </div>
            <div style="margin-bottom:10px">
                <input required="true" name="stuUpload" label="文件路径：" style="width:100%" class="easyui-textbox"
                 missingMessage="作业路径后请以\结尾" id="stuUpload">
            </div>
            <div style="margin-bottom:10px"><input id="chooseClass"  name="chooseClass" label="选择班级："class="easyui-combobox"  style="width:100%" 
                data-options="valueField:'classId',textField:'className',url:'find_grade.action'" />
            </div> 
            <div style="margin-bottom:10px"><input id="chooseSubject"  name="chooseSubject" label="选择科目："class="easyui-combobox"  style="width:100%" 
                data-options="valueField:'subjectId',textField:'subjectName',url:'find_subject.action'" />
            </div> 
        </form>
    </div>
    <div id="dlg-button">
        <a class="easyui-linkbutton" iconCls="icon-ok" onclick="submit()" style="width:90px">确定</a>
        <a class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')" 
        style="width:90px">取消</a>
    </div>
    <div id="editdlg" class="easyui-dialog" style="width:400px" closed="true" buttons="#editdlg-button">
        <form id="editfm" method="post" novalidate="true" style="margin:0;padding:20px 50px">
            <div style="margin-bottom:10px">
                <input required="true" name="editWorkDetail" label="作业内容：" style="width:100%" class="easyui-textbox"
                missingMessage="作业内容不能为空" id="editWorkDetail">
            </div>
            <div style="margin-bottom:10px">
                <input required="true" name="editStuUpload" label="文件路径：" style="width:100%" class="easyui-textbox"
                 missingMessage="作业路径后请以\结尾" id="editStuUpload">
            </div>
        </form>
    </div>
     <div id="editdlg-button">
        <a class="easyui-linkbutton" iconCls="icon-ok" onclick="editSubmit()" style="width:90px">确定</a>
        <a class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#editdlg').dialog('close')" 
        style="width:90px">取消</a>
    </div>
    <div id="file" class="easyui-dialog" style="width:400px" closed="true" buttons="#dlg-buttons">
        <form id="ffm" method="post" novalidate="true" name="ffm" enctype="multipart/form-data" style="margin:0;padding:20px 50px">
            <div style="margin-bottom:10px">
                <input name="filebox" label="上传附件：" style="width:100%" class="easyui-filebox"
                 id="filebox">
            </div> 
        </form>
    </div>
    <div id="dlg-buttons">
        <a class="easyui-linkbutton" iconCls="icon-ok" onclick="submitFile()" style="width:90px">确定</a>
        <a class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#file').dialog('close')" 
        style="width:90px">取消</a>
    </div>
    <script type="text/javascript">
        var url;
        function upload(){
        	$('#file').dialog('open').dialog('center').dialog('setTitle','添加附件');
            $('#ffm').form('clear');
            $('#ffm').form('disableValidation');
        }
        function submitFile(){
        	var row = $('#dg').datagrid('getSelected');
        	//1:获取表单  
            var form1=document.ffm;  
            //2.设置表单的action属性  
            url='springUpload.action?workId='+row.workId;  
            //3.提交表单  
            $('#ffm').form('submit',{
                url: url,
                onSubmit: function(){
                    return $(this).form('validate')          ;
                },
                success: function(result){
                    if(result == 'true') {
                    	$('#file').dialog('close');
                        $.messager.show({
                            title: '提示',
                            msg: '上传成功！'
                        }); 
                    }
                 }
            })
        }
        function add(){
            $('#dlg').dialog('open').dialog('center').dialog('setTitle','添加作业');
            $('#fm').form('clear');
            $('#workDetail').textbox('readonly',false);
            $('#chooseClass').combobox('getValue');
            $('#chooseSubject').combobox('getValue'); //单选时                      
            //窗体弹出默认是禁用验证，因为刚弹出的窗口，用户还没填就显示的话，太丑
            $('#fm').form('disableValidation');
            url = 'work_save.action';
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
                $('#editdlg').dialog('open').dialog('center').dialog('setTitle','修改用户');
                $('#editfm').form('load',rows[0]);
                $('#editfm').form('disableValidation');
                $('#editWorkDetail').textbox({    
                    prompt: '若不更新无需输入',
                    required: false,
                    value: ''
                });
                $('#editStuUpload').textbox({    
                    prompt: '若不更新无需输入',
                    required: false,
                    value: ''
                });
                url = 'work_update.action?workId='+rows[0].workId;
            }
        }
        function editSubmit(){
            //开启验证
            $('#editfm').form('enableValidation');
            $('#editfm').form('submit',{
                url: url,
                onSubmit: function(){
                    return $(this).form('validate')          ;
                },
                success: function(result){
                    if(result == 'exist') {
                        $.messager.show({
                            title: '警告',
                            msg: '该作业已存在，请重新输入！'
                        }); 
                    } else {
                        $('#editdlg').dialog('close');
                        $('#dg').datagrid('reload');
                    }
                }
            });
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
                            msg: '该作业已存在，请重新输入！'
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
                            ids += rows[i].workId + ',';  
                        }  
                        ids = ids.substr(0, ids.lastIndexOf(','));
                        $.post('work_delete.action',{ids:ids},function(result){
                            if(result == 'true') {
                                //刷新当前页，查询的时候我们用的是load，刷新第一页，reload是刷新当前页
                                $.messager.show({   
                                    title:'提示',  
                                    msg:'操作成功！'
                                });  
                                $('#dg').datagrid('reload');                         
                            }else {  
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
                subjectName: $('#subjectNameSearch').val(),
                className: $('#classNameSearch').val(),
                createTimeStart: $('#createTimeStart').val(),
                createTimeEnd: $('#createTimeEnd').val()
            });
        }
        
        function reset(){
            $('.search').textbox('reset');
        }
        function filePath(value, row, index) {
            return "<a href='#' onclick='upload()' target='_self'>" + "上传附件" + "</a>";
        }
      //隐藏 state为comboboxid
        //function hide(){
            // $("#state + .combo").hide();
        //}
        //显示 state为comboboxid
        //function show(){
            // $("#state + .combo").show();
        //}
    </script>
</body>
</html>