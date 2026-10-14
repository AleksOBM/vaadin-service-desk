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
