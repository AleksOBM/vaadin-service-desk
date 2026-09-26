package com.example.vsd.manager.exception;

import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * @param status    Код статуса HTTP-ответа
 * @param reason    Общее описание причины ошибки
 * @param message   Сообщение об ошибке
 * @param timestamp Дата и время когда произошла ошибка (в формате "yyyy-MM-dd HH:mm:ss")
 */
@Builder(toBuilder = true)
public record ApiError(
		HttpStatus status,
		String reason,
		String message,
		LocalDateTime timestamp
) {
}

