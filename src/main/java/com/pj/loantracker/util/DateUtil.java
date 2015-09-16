package com.pj.loantracker.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DateUtil {

	public static Date toDate(LocalDate localDate) {
		return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

	public static LocalDate toLocalDate(Date date) {
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}
	
}