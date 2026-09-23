package com.example.vsd.manager.service.dashboard;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DashboardService {
    // todo: Статистика по заявкам за месяц с учетом их статусов
}
