INSERT INTO orders (title, description, start_line, dead_line,
                    agent_id, client_id, creation_date, last_updated,
                    deleted, completed_date)
SELECT  v.title, v.description, v.start_line, v.dead_line,
        v.agent_id, v.client_id, v.creation_date, v.last_updated,
        v.deleted, v.completed_date
FROM (VALUES
            (
                'Установка лазерных детекторов движения',
                'Необходим дополнительный ряд лазеров на потолке, а также инфракрасные датчики тепла',
                CURRENT_DATE,
                CURRENT_DATE + INTERVAL '12 day',
                2,
                1,
                NOW(),
                NOW(),
                false,
                null::timestamp
            ),
            (
                'Замена камер видеонаблюдения',
                'Камеры должны обладать рентгеновским зрением',
                CURRENT_DATE - INTERVAL '4 day',
                CURRENT_DATE + INTERVAL '10 day',
                4,
                5,
                NOW(),
                NOW(),
                false,
                NOW() - INTERVAL '1 day'
            ),
            (
                'Настройка оборудования в подземной лаборатории',
                'Осторожно, там выращивают клонов-мутантов и профессор чекнутый',
                CURRENT_DATE + INTERVAL '8 day',
                CURRENT_DATE + INTERVAL '27 day',
                1,
                3,
                NOW(),
                NOW(),
                true,
                null::timestamp
            )
) AS    v(title, description, start_line, dead_line,
        agent_id, client_id, creation_date, last_updated,
        deleted, completed_date)
WHERE NOT EXISTS (
    SELECT 1 FROM orders o WHERE o.title = v.title
);

INSERT INTO orders (title, description, start_line, dead_line,
                    agent_id, client_id, creation_date, last_updated,
                    deleted, completed_date)
SELECT v.title, v.description, v.start_line, v.dead_line,
       a.id, c.id, NOW(), NOW(), false, v.completed_date
FROM (VALUES
    (
        'Модернизация серверной башни',
        'Перенести вычислительные ядра в охлаждаемый отсек и проверить резервное питание.',
        CURRENT_DATE + 5,
        CURRENT_DATE + 17,
        'Чарльз Ксавьер',
        'Lumon Industries',
        null::timestamp
    ),
    (
        'Проверка системы климат-контроля',
        'Настроить датчики температуры в офисных помещениях и устранить перепады охлаждения.',
        CURRENT_DATE - 2,
        CURRENT_DATE + 10,
        'Скотт Саммерс',
        'Umbrella Corporation',
        null::timestamp
    ),
    (
        'Обновление доступа к архиву',
        'Проверить пропуска, восстановить журнал доступа и сверить записи с резервной копией.',
        CURRENT_DATE - 6,
        CURRENT_DATE + 6,
        'Роберт Дрейк',
        'E Corp',
        null::timestamp
    ),
    (
        'Диагностика экспериментального оборудования',
        'Провести плановую диагностику стенда и передать результаты инженерам лаборатории.',
        CURRENT_DATE - 10,
        CURRENT_DATE + 2,
        'Уоррен Уортингтон III',
        'Cyberdyne Systems',
        null::timestamp
    ),
    (
        'Восстановление связи с удалённым комплексом',
        'Проверить канал связи, заменить неисправный ретранслятор и подтвердить передачу телеметрии.',
        CURRENT_DATE - 16,
        CURRENT_DATE - 4,
        'Генри Маккой',
        'Universal Dynamics',
        null::timestamp
    ),
    (
        'Настройка системы наблюдения',
        'Заменить неисправные камеры и проверить сохранность архива видеозаписей.',
        CURRENT_DATE - 20,
        CURRENT_DATE - 8,
        'Джин Грей-Саммерс',
        'REKALL',
        NOW()
    )
) AS v(title, description, start_line, dead_line, agent_name, client_name, completed_date)
JOIN agents a ON a.agent_name = v.agent_name
JOIN clients c ON c.client_name = v.client_name
WHERE NOT EXISTS (
    SELECT 1 FROM orders o WHERE o.title = v.title
);
