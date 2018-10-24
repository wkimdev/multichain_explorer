package kr.doublechain.basic.explorer.service.user;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.json.simple.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * UserService
 *
 */
@Service("UserService")
public class UserService {
	
	private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
	
	/**
 	 * 현재날짜 기준 2주전, 각 날짜 값 -old 
 	 * 
 	 * @return JSONArray
 	 * @throws Exception
 	 */
	public JSONArray getTwoWeeksDate() throws Exception {
		Date today = new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String currentDate = df.format(today); // 현재 Date
		
		Date date = df.parse(currentDate);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, -13);
		String beforeDate = df.format(cal.getTime()); // -14 Date
		
		Date d1 = df.parse( beforeDate );
		Date d2 = df.parse( currentDate );

		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();

		//+1 일
		c1.setTime( d1 );
		c2.setTime( d2 );
		
		JSONArray jsonList = new JSONArray();
		while( c1.compareTo( c2 ) !=1 ){
			DateFormat dfm = new SimpleDateFormat("MM/dd");
			jsonList.add(dfm.format(c1.getTime()));
			c1.add(Calendar.DATE, 1); // 시작날짜 + 1 일
		}
		return jsonList;
	}
	
}