package com.zycm.zybao.common.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
* @ClassName: DateUtil
* @Description: 时间处理工具类
* @author sy
* @date 2018年10月17日
*
*/
public class DateUtil {

	public static SimpleDateFormat sdf_ymdhms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	* @Title: getNextDay
	* @Description:获取指定时间的前一天
	* @param date
	* @return    参数
	* @author sy
	* @throws
	* @return Date    返回类型
	*
	*/
	public static Date getBeforeDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		date = calendar.getTime();
		return date;
	}

	/**
	* @Title: getNextDay
	* @Description:获取指定时间的前后n天
	* @param date
	* @return    参数
	* @author sy
	* @throws
	* @return Date    返回类型
	*
	*/
	public static Date getBeforeDay(Date date,int amount) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, amount);
		date = calendar.getTime();
		return date;
	}

	public static Date getNextDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		date = calendar.getTime();
		return date;
	}

	/**
	* @Title: getAreaDayStr
	* @Description: 根据指定时间区间返回区间的每天日期
	* @param startDate yyyy-MM-dd
	* @param endDate yyyy-MM-dd
	* @return    参数
	* @author sy
	* @throws
	* @return String[]    返回类型  每个时间格式yyyy-MM-dd
	*
	*/
	public static String[] getAreaDayStr(String startDate,String endDate) throws Exception{
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
		int day = differentDays(sdf2.parse(startDate), sdf2.parse(endDate))+1;
		if(day > 0){
			String[] re = new String[day];
			String date = startDate;
			for (int i = 0; i < day; i++) {
				if(i > 0){
					date = sdf2.format(getNextDay(sdf2.parse(date)));
				}
				re[i] = date;
			}
			return re;
		}

		return null;
	}

	/**
     * date2比date1多的天数  忽略时分秒
     * @param date1
     * @param date2
     * @return
     */
    public static int differentDays(Date date1,Date date2)
    {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day1= cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if(year1 != year2)   //同一年
        {
            int timeDistance = 0 ;
            for(int i = year1 ; i < year2 ; i ++)
            {
                if(i%4==0 && i%100!=0 || i%400==0)    //闰年
                {
                    timeDistance += 366;
                }
                else    //不是闰年
                {
                    timeDistance += 365;
                }
            }

            return timeDistance + (day2-day1) ;
        }
        else    //不同年
        {
            return day2-day1;
        }
    }

    public static String getCurrentMonthStartAndEnd(){
    	Calendar c = Calendar.getInstance();
		c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.getActualMaximum(Calendar.DAY_OF_MONTH), 0, 0, 0);
		SimpleDateFormat startSdf = new SimpleDateFormat("yyyyMM01");
		SimpleDateFormat endSdf = new SimpleDateFormat("yyyyMMdd");
    	return startSdf.format(c.getTime())+","+endSdf.format(c.getTime());
    }

	public static void main(String[] args) {
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		System.out.println(sdf1.format(getBeforeDay(new Date(), -19)));

	}

}
