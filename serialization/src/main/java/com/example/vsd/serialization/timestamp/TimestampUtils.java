package com.example.vsd.serialization.timestamp;

import com.google.protobuf.Timestamp;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@UtilityClass
@SuppressWarnings("unused")
public class TimestampUtils {

	private final ZoneId zoneId = ZoneId.systemDefault();

	public final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter
			.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

	public final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter
			.ofPattern("yyyy-MM-dd");

	public Timestamp toTimestamp(@NonNull Instant instant) {
		return Timestamp.newBuilder()
				.setSeconds(instant.getEpochSecond())
				.setNanos(instant.getNano())
				.build();
	}

	public Timestamp toTimestamp(@NonNull LocalDateTime localDateTime) {
		return toTimestamp(localDateTime.atZone(zoneId).toInstant());
	}

	public Instant toInstant(@NonNull Timestamp timestamp) {
		return Instant.ofEpochSecond(
				timestamp.getSeconds(),
				timestamp.getNanos()
		);
	}

	public Instant toInstant(@NonNull LocalDateTime localDateTime) {
		return toInstant(toTimestamp(localDateTime));
	}

	public LocalDateTime toLocalDateTime(@NonNull Timestamp timestamp) {
		return LocalDateTime.ofInstant(toInstant(timestamp), zoneId)
				.truncatedTo(ChronoUnit.MILLIS);
	}

	public LocalDateTime toLocalDateTime(long timestamp) {
		return toLocalDateTime(toTimestamp(Instant.ofEpochSecond(timestamp)))
				.truncatedTo(ChronoUnit.MILLIS);
	}

	public LocalDateTime toLocalDateTime(@NonNull Instant instant) {
		return toLocalDateTime(toTimestamp(instant));
	}

	public LocalDate toLocalDate(@NonNull Timestamp timestamp) {
		return toLocalDateTime(timestamp).toLocalDate();
	}

	public LocalDate toLocalDate(@NonNull String str) {
		return LocalDate.parse(str, DATE_FORMATTER);
	}

	public String toString(@NonNull Timestamp timestamp) {
		return DATE_TIME_FORMATTER.format(toLocalDateTime(timestamp));
	}

	public String toString(long longTimestamp) {
		return toString(toTimestamp(Instant.ofEpochSecond(longTimestamp).atZone(zoneId).toInstant()));
	}

	public String toString(@NonNull LocalDate localDate) {
		return localDate.format(DATE_FORMATTER);
	}
}
