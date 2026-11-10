package com.example.vsd.manager.service.order;

import com.example.vsd.serialization.model.OrderSchedule;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderScheduleTest {

	@Test
	void calculatesControlDatesBetweenStartAndDeadline() {
		LocalDate start = LocalDate.of(2026, 1, 1);
		LocalDate deadline = LocalDate.of(2026, 1, 10);

		assertEquals(LocalDate.of(2026, 1, 4),
				OrderSchedule.getFirstControlDate(start, deadline));
		assertEquals(LocalDate.of(2026, 1, 7),
				OrderSchedule.getSecondControlDate(start, deadline));
		assertTrue(OrderSchedule.isValid(start, deadline));
	}

	@Test
	void rejectsMissingOrNonIncreasingScheduleDates() {
		LocalDate date = LocalDate.of(2026, 1, 1);

		assertFalse(OrderSchedule.isValid(null, date));
		assertFalse(OrderSchedule.isValid(date, null));
		assertFalse(OrderSchedule.isValid(date, date));
		assertFalse(OrderSchedule.isValid(date.plusDays(1), date));
	}
}
