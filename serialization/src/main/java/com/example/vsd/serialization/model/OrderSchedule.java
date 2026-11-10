package com.example.vsd.serialization.model;

import java.time.LocalDate;
import java.time.Period;

public final class OrderSchedule {

	private OrderSchedule() {
	}

	public static int getDaysCount(LocalDate startDate, LocalDate deadline) {
		return Period.between(startDate, deadline).getDays();
	}

	public static LocalDate getFirstControlDate(LocalDate startDate, LocalDate deadline) {
		return startDate.plusDays(getControlDaysCount(startDate, deadline));
	}

	public static LocalDate getSecondControlDate(LocalDate startDate, LocalDate deadline) {
		return deadline.minusDays(getControlDaysCount(startDate, deadline));
	}

	public static boolean isValid(LocalDate startDate, LocalDate deadline) {
		if (startDate == null || deadline == null || !startDate.isBefore(deadline)) {
			return false;
		}

		LocalDate firstControl = getFirstControlDate(startDate, deadline);
		LocalDate secondControl = getSecondControlDate(startDate, deadline);
		return !firstControl.isBefore(startDate)
				&& !secondControl.isBefore(firstControl)
				&& !deadline.isBefore(secondControl);
	}

	private static int getControlDaysCount(LocalDate startDate, LocalDate deadline) {
		return Math.round((float) getDaysCount(startDate, deadline) / 3);
	}
}
