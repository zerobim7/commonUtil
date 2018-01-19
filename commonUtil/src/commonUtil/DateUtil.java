package commonUtil;

import java.sql.Timestamp;
import java.text.*;
import java.util.*;


public class DateUtil {

	public DateUtil() {

	}

	// 요일 구하기
	public static int getWeekNameInt(String day) {
		Calendar cal = toCalendar(day);
		String m_week = null;
		int day_of_week = cal.get(Calendar.DAY_OF_WEEK);

		return day_of_week;

	}

	// String->Calendar
	public static Calendar toCalendar(String day) {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(df.parse(day));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cal;
	}
	
    public static String lastDayOfMonth(String src)
    {
        return lastDayOfMonth(src.substring(0, 8), "yyyyMMdd");
    }

    public static String lastDayOfMonth(String src, String format)
    {
        try
        {
            SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.KOREA);
            Date date = check(src, format);
            SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
            SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.KOREA);
            int year = Integer.parseInt(yearFormat.format(date));
            int month = Integer.parseInt(monthFormat.format(date));
            int day = lastDay(year, month);
            DecimalFormat fourDf = new DecimalFormat("0000");
            DecimalFormat twoDf = new DecimalFormat("00");
            String tempDate = String.valueOf(fourDf.format(year)) + String.valueOf(twoDf.format(month)) + String.valueOf(twoDf.format(day));
            Date targetDate = check(tempDate, "yyyyMMdd");
            return formatter.format(targetDate);
        }
        catch(Exception e)
        {
        	//Helper.getLogger().error(LOG_HEADER, e); 
        	return null;
        }
    }
    
    private static Date check(String s) throws ParseException {
        return check(s, "yyyyMMdd");
    }

    private static Date check(String s, String format) throws ParseException {
        if(s == null)
            throw new ParseException("date string to check is null", 0);
        if(format == null)
            throw new ParseException("format string to check date is null", 0);
        
        SimpleDateFormat formatter  = new SimpleDateFormat(format, Locale.KOREA);
        Date date                   = null;
        try {
            date = formatter.parse(s);
        } catch(ParseException e) {
            throw new ParseException(" wrong date:\"" + s + "\" with format \"" + format + "\"", 0);
        }
        
        if(!formatter.format(date).equals(s))
            throw new ParseException("Out of bound date:\"" + s + "\" with format \"" + format + "\"", 0);
        else
            return date;
    }
    
    private static int lastDay(int year, int month)
            throws ParseException
        {
            int day = 0;
            switch(month)
            {
            case 1: // '\001'
            case 3: // '\003'
            case 5: // '\005'
            case 7: // '\007'
            case 8: // '\b'
            case 10: // '\n'
            case 12: // '\f'
                day = 31;
                break;

            case 2: // '\002'
                if(year % 4 == 0)
                {
                    if(year % 100 == 0 && year % 400 != 0)
                        day = 28;
                    else
                        day = 29;
                }
                else
                {
                    day = 28;
                }
                break;

            case 4: // '\004'
            case 6: // '\006'
            case 9: // '\t'
            case 11: // '\013'
            default:
                day = 30;
                break;

            }
            return day;
        }
    
    //법정 공휴일인지 확인 
    public static String getHoliday(String yyyymmdd){
    	String name="";
    	String date=yyyymmdd.substring(4,8);
    	String years=yyyymmdd.substring(0,4);
    	Map<String, String> solarMap=new HashMap<String, String>();
/*    	solarMap.put("0101", "신정");
    	solarMap.put("0301", "삼일절");
    	solarMap.put("0505", "어린이날");
    	solarMap.put("0606", "현충일");
    	solarMap.put("0815", "광복절");
    	solarMap.put("1003", "개천절");
    	solarMap.put("1009", "한글날");
    	solarMap.put("1225", "크리스마스");
    	
    	solarMap.put(luanTosola(years+"0101"), "설날(1.1)");
    	solarMap.put(luanTosola(years+"0102"), "공휴일(1.2)");
    	solarMap.put(luanTosola(years+"0408"), "석가탄신일(4.8)");
    	solarMap.put(luanTosola(years+"0814"), "공휴일(8.14)");
    	solarMap.put(luanTosola(years+"0815"), "추석(8.15)");
    	solarMap.put(luanTosola(years+"0816"), "공휴일(8.16)");*/
    	
    	solarMap.put("year", yyyymmdd);
    	
    	/*TimeSheetBusiness timeBiz;
    	List<Map<String, String>> holidayList=null;
		try {
			timeBiz = TimeSheetBusiness.getInstance();
			holidayList= timeBiz.getHoliDayList(solarMap);
		} catch (TimeSheetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	
    	if(holidayList!=null&&holidayList.size()>0){
    		name=holidayList.get(0).get("HOLIDAY_NM");
    	}*/
    	
    	
    	
    	
    	return name;
    }
    
  
    
    
    
    
    public static String getCurrentDateString()
    {
        return getCurrentDateString("yyyyMMdd");
    }
    
    public static String getCurrentDateString(String pattern)
    {
        return convertToString(getCurrentTimeStamp(), pattern);
    }
    
    public static Timestamp getCurrentTimeStamp()
    {
        try
        {
            Calendar cal = new GregorianCalendar();
            Timestamp result = new Timestamp(cal.getTime().getTime());
            return result;
        }
        catch(Exception e)
        {
            //Helper.getLogger().error(LOG_HEADER, e);
            return null;
        }
    }
    
    public static String convertToString(Timestamp dateData)
    {
        return convertToString(dateData, "yyyy-MM-dd");
    }

    public static String convertToString(Timestamp dateData, String pattern)
    {
        return convertToString(dateData, pattern, Locale.KOREA);
    }

    public static String convertToString(Timestamp dateData, String pattern, Locale locale)
    {
        try {
            if( dateData == null ) {
                return "";
            }
            else {	
                SimpleDateFormat formatter = new SimpleDateFormat(pattern, locale);
                return formatter.format(dateData);
            }
        } catch(Exception e) {
            //Helper.getLogger().error(LOG_HEADER, e);
            return null;
        }
    }
    
    public static String addDays(String s, int day, String format)
    {
        try
        {
            SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.KOREA);
            Date date = check(s, format);
            date.setTime(date.getTime() + (long)day * 1000L * 60L * 60L * 24L);
            return formatter.format(date);
        }
        catch(Exception e)
        {
        	return null;
        }
    }
    
    public static String getCurrentTimeString()
    {
        return getCurrentDateString("HHmmss");
    }
    
    public static String getWeekName(String day ){
        Calendar cal=toCalendar(day);
  	  String m_week = null;
  	  int day_of_week = cal.get ( Calendar.DAY_OF_WEEK );
  	  
  	  if ( day_of_week == 1 )
  	      m_week="일";
  	  
  	  else if ( day_of_week == 2 )
  	      m_week="월";
  	  
  	  else if ( day_of_week == 3 )
  	      m_week="화";
  	  
  	  else if ( day_of_week == 4 )
  	      m_week="수";
  	  
  	  else if ( day_of_week == 5 )
  	      m_week="목";
  	  
  	  else if ( day_of_week == 6 )
  	      m_week="금";
  	  
  	  else if ( day_of_week == 7 )
  	      m_week="토";
  	  
  	  return m_week;
   
      }
    
    public static boolean isValid(String s) {
        return isValid(s, "yyyyMMdd");
    }
    public static boolean isValid(String s, String format) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.KOREA);
            Date date = null;
            try {
                date = formatter.parse(s);
            } catch(ParseException e) {
                return false;
            }
            return formatter.format(date).equals(s);
        } catch(Exception e) {
           
            return false;
        }
    }
    
    public static double hoursBetween(String from, String to) {
        return hoursBetween(from, to, "yyyyMMddHHmm");
    }

    public static double hoursBetween(String from, String to, String format) {
        try {
            Date d1 = check(from, format);
            Date d2 = check(to, format);
            long duration = d2.getTime() - d1.getTime(); 
            double minute =(duration / 60000);
            return minute/60;
        } catch(Exception e) {
        	return -1000000;
        }
    }
    
    
}
