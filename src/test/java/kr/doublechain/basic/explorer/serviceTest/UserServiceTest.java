package kr.doublechain.basic.explorer.serviceTest;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

import org.json.simple.JSONArray;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.gson.JsonObject;

import kr.doublechain.basic.explorer.common.utils.CommonUtil;
import kr.doublechain.basic.explorer.contorller.DccController;
import kr.doublechain.basic.explorer.service.user.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {
	
	@Autowired
	UserService userService;
	
	@Autowired
	DccController dccController;
	
	//@Test
	public void getDateConvert() throws Exception {
		Date today = new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String current_date = df.format(today);
		Date date = df.parse(current_date);
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, -13);
		
		System.out.println(df.format(cal.getTime()));
	}
	
	//@Test
	public void getDateCall() throws Exception {
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
		System.out.println(jsonList);

	}
	
	//@Test
	public void getDate() throws Exception {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
		Date date = new Date();
		System.out.println("1 :"+dateFormat.format(date));
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, -13);
		System.out.println("2 :"+dateFormat.format(cal.getTime()));
	}
	
	@Test
	public void getTest() throws Exception {
		Timestamp time = new Timestamp(System.currentTimeMillis());
		System.out.println(time);
		//System.out.println("test unit : "+dccController.getTwoWeeksSpeeds());
	}
	
}
