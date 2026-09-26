package com.example.vsd.serialization.timestamp;

import com.google.protobuf.Timestamp;
import com.google.type.Date;
import org.jspecify.annotations.NonNull;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@SuppressWarnings("unused")
public final class TimestampUtils {

	private TimestampUtils() {
	}

	private static final ZoneId zoneId = ZoneId.systemDefault();

	public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter
			.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

	public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter
			.ofPattern("yyyy-MM-dd");

	@NonNull
	public static Timestamp toTimestamp(@NonNull Instant instant) {
		return Timestamp.newBuilder()
				.setSeconds(instant.getEpochSecond())
				.setNanos(instant.getNano())
				.build();
	}

	@NonNull
	public static Timestamp toTimestamp(@NonNull LocalDateTime localDateTime) {
		return toTimestamp(localDateTime.atZone(zoneId).toInstant());
	}

	@NonNull
	public static LocalDate toLocalDate(@NonNull Date date) {
		return LocalDate.of(date.getYear(), date.getMonth(), date.getDay());
	}

	@NonNull
	public static Date toDate(@NonNull LocalDate localDate) {
		return Date.newBuilder()
				.setYear(localDate.getYear())
				.setMonth(localDate.getMonth().getValue())
				.setDay(localDate.getDayOfMonth())
				.build();
	}

	public static Instant toInstant(@NonNull Timestamp timestamp) {
		return Instant.ofEpochSecond(
				timestamp.getSeconds(),
				timestamp.getNanos()
		);
	}

	public static Instant toInstant(@NonNull LocalDateTime localDateTime) {
		return toInstant(toTimestamp(localDateTime));
	}

	@NonNull
	public static LocalDateTime toLocalDateTime(@NonNull Timestamp timestamp) {
		if (isEmpty(timestamp)) {
			throw new IllegalArgumentException("Timestamp is empty.");
		}
		return LocalDateTime.ofInstant(toInstant(timestamp), zoneId)
				.truncatedTo(ChronoUnit.MILLIS);
	}

	@NonNull
	public static LocalDateTime toLocalDateTime(long timestamp) {
		return toLocalDateTime(toTimestamp(Instant.ofEpochSecond(timestamp)))
				.truncatedTo(ChronoUnit.MILLIS);
	}

	@NonNull
	public static LocalDateTime toLocalDateTime(@NonNull Instant instant) {
		return toLocalDateTime(toTimestamp(instant));
	}

	public static LocalDate toLocalDate(@NonNull Timestamp timestamp) {
		return toLocalDateTime(timestamp).toLocalDate();
	}

	@NonNull
	public static LocalDate toLocalDate(@NonNull String str) {
		return LocalDate.parse(str, DATE_FORMATTER);
	}

	@NonNull
	public static String toString(@NonNull Timestamp timestamp) {
		return DATE_TIME_FORMATTER.format(toLocalDateTime(timestamp));
	}

	@NonNull
	public static String toString(long longTimestamp) {
		return toString(toTimestamp(Instant.ofEpochSecond(longTimestamp).atZone(zoneId).toInstant()));
	}

	@NonNull
	public static String toString(@NonNull LocalDate localDate) {
		return localDate.format(DATE_FORMATTER);
	}

	private static boolean isEmpty(@NonNull Timestamp timestamp) {
		return timestamp.getSeconds() <= 0 && timestamp.getNanos() <= 0;
	}
}
