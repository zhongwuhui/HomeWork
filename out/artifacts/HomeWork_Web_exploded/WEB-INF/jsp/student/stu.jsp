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
    <table id="dg" class="easyui-datagrid" url="stu_list_work.action" nowrap="true"
    rownumbers="true" toolbar="#toolbar" pagination="true" pageSize="15" pageList="[10,15,20,30,40]" fitColumns="true">
        <thead>
            <tr>
                <th field="checkbox" checkbox="true"></th>
                <th data-options="field:'workId',align:'center',width:50,sortable:'true'">作业编号</th>
                <th data-options="field:'userName',align:'center',width:50">作业下达教师</th>
                <th data-options="field:'workDetail',align:'center',width:50">作业内容</th>
                <th data-options="field:'subjectName',align:'center',width:50">所属科目</th>
                <th data-options="field:'createTime',align:'center',width:50,sortable:'true'">创建时间</th>
                <th data-options="field:'fileName',align:'center',width:38,formatter:filePath">下载附件</th>
                <th data-options="field:'upload',align:'center',width:38,formatter:uploadFile">提交作业</th>
            </tr>
        </thead>
    </table>
    <div id="toolbar">
        <div>
            <span>教师：</span>
            <input prompt="请输入教师名称" id="teacherNameSearch" class="easyui-textbox search">
            <span>科目：</span>
            <input prompt="请输入科目名" id="subjectNameSearch" class="easyui-textbox search">
            <span>创建时间：</span>
            <input style="width:160px" prompt="请输入起始时间" id="createTimeStart" class="easyui-datetimebox search">
            <span>--</span>
            <input style="width:160px" prompt="请输入结束时间" id="createTimeEnd" class="easyui-datetimebox search">
            <a class="easyui-linkbutton" iconCls="icon-search" onclick="doSearch()">搜索</a>
            <a class="easyui-linkbutton" iconCls="icon-reset" onclick="reset()">重置</a>
        </div>
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
            url='HandInWorks.action?workId='+row.workId;  
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
        function doSearch(){
            $('#dg').datagrid('load',{
            	userName:$('#teacherNameSearch').val(),
                subjectName: $('#subjectNameSearch').val(),
                createTimeStart: $('#createTimeStart').val(),
                createTimeEnd: $('#createTimeEnd').val()
            });
        }
        function reset(){
            $('.search').textbox('reset');
        }
       
        function filePath(value, row, index) {
            return "<a href='${pageContext.request.contextPath }/down.action?workId=" + row.workId + "'  target='_self'>" + row.fileName + "</a>";
       }
        function uploadFile(value, row, index) {
            return "<a href='#' onclick='upload()' target='_self'>" + "上传附件" + "</a>";
        }
    </script>
</body>
</html>