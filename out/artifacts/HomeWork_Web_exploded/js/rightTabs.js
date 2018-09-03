$(function(){  
    $("a[title]").click(function(){  
        var text = $(this).text();  
        var href = $(this).attr("title");
        var iconCls = $(this).attr("data-icon");
        //判断当前右边是否已有相应的tab  
        if($("#tt").tabs("exists", text)) {  
            $("#tt").tabs("select", text);  
        } else { 
            //如果没有则创建一个新的tab，否则切换到当前tag  
            $("#tt").tabs("add",{  
                title : text,  
                closable : true,
                iconCls : iconCls,
                content : '<iframe title=' + text + ' src=' + href + ' frameborder="0" width="100%" height="100%"/>'  
                //href:默认通过url地址加载远程的页面，但是仅仅是body部分
            });  
        }  
              
    });  
});