// 因为juqery Autocomplete插件依赖于jquery 1.2.6 到 jquery 1.8.3 的版本，以上的版本去掉了 browser方法
// 此js文件可以解决高版本jquery不支持 browser方法问题
jQuery.extend({    
    browser: function()     
    {    
        var    
        rwebkit = /(webkit)\/([\w.]+)/,    
        ropera = /(opera)(?:.*version)?[ \/]([\w.]+)/,    
        rmsie = /(msie) ([\w.]+)/,    
        rmozilla = /(mozilla)(?:.*? rv:([\w.]+))?/,        
        browser = {},    
        ua = window.navigator.userAgent,    
        browserMatch = uaMatch(ua);    
    
        if (browserMatch.browser) {    
            browser[browserMatch.browser] = true;    
            browser.version = browserMatch.version;    
        }    
        return { browser: browser };    
    },    
});    
    
function uaMatch(ua)     
{    
        ua = ua.toLowerCase();    
    
        var match = rwebkit.exec(ua)    
                    || ropera.exec(ua)    
                    || rmsie.exec(ua)    
                    || ua.indexOf("compatible") < 0 && rmozilla.exec(ua)    
                    || [];    
    
        return {    
            browser : match[1] || "",    
            version : match[2] || "0"    
        };    
}