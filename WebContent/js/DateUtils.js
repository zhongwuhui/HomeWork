/**
 *js关于日期类型的操作，如将String转换为Date
 *两个Date类型的时间差（小时、天等）
 * 
 */
var DateUtils = {
	/**
	 * 将yyyy-MM-dd HH:mm:ss日期字符串格式化为Date
	 * @param str
	 * @return
	 */
	parseDate:function(str){   
		if(typeof str == 'string'){   
			var results = str.match(/^ *(\d{4})-(\d{1,2})-(\d{1,2}) *$/);   
			if(results && results.length>3)   
			  return new Date(parseInt(results[1],10),parseInt(results[2],10) -1,parseInt(results[3],10));    
			results = str.match(/^ *(\d{4})-(\d{1,2})-(\d{1,2}) +(\d{1,2}) *$/);   
			if(results && results.length>4)   
			  return new Date(parseInt(results[1],10),parseInt(results[2],10) -1,parseInt(results[3],10),parseInt(results[4],10));    
		}   
	    return null;   
	},

    /**
     * 给指定的日期加上指定的年、月、日、时、分、秒后得到新的日期。
     * @param date 日期对象。
     * @param interval 需要添加属性，所传递的值和意义为：y-年,q-季度,m-月,d-日,w-周,h-小时,n-分钟,s-秒,ms-毫秒
     * @param number 添加的值。
     * @return 新的日期。
     * 举例：求指定日期的后一天日期，dateAdd(date,"d",1)
     */
    dateAdd : function(date,interval,number) {
        var k={'y':'FullYear', 'q':'Month', 'm':'Month', 'w':'Date', 'd':'Date', 'h':'Hours', 'n':'Minutes', 's':'Seconds', 'ms':'MilliSeconds'};
        var n={'q':3, 'w':7};
        eval('date.set'+k[interval]+'(date.get'+k[interval]+'()+'+((n[interval]||1)*number)+')');
        return date;
    }
};// end DateUtils
