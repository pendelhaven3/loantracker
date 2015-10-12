package com.pj.loantracker.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTimeComparator;

import com.pj.loantracker.Constants;

public class DateUtil {

	public static Date toDate(LocalDate localDate) {
		return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

	public static LocalDate toLocalDate(Date date) {
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}
	
	public static Date toDate(String dateString) {
		try {
			return new SimpleDateFormat(Constants.DATE_FORMAT).parse(dateString);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	public static List<Date> generateMonthlyDates(Date startDate, Date endDate) {
		List<Date> dates = new ArrayList<>();
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		calendar.add(Calendar.MONTH, 1);
		
		while (calendar.getTime().compareTo(endDate) <= 0) {
			dates.add(calendar.getTime());
			calendar.add(Calendar.MONTH, 1);
		}
		
		return dates;
	}
	
	public static int compareTo(Date date1, Date date2) {
		return DateTimeComparator.getDateOnlyInstance().compare(date1, date2);
	}
	
}