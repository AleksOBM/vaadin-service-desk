INSERT INTO agents (agent_name, creation_date, last_updated, deleted)
VALUES 	('Чарльз Ксавьер', now(), now(), 'false'),
        ('Скотт Саммерс', now(), now(), 'false'),
        ('Роберт Дрейк', now(), now(), 'false'),
        ('Уоррен Уортингтон III', now(), now(), 'false'),
        ('Генри Маккой', now(), now(), 'false'),
        ('Джин Грей-Саммерс', now(), now(), 'false')
ON CONFLICT (agent_name)
DO NOTHING;

INSERT INTO clients (client_name, creation_date, last_updated, deleted)
VALUES 	('Lumon Industries', now(), now(), 'false'),
        ('Umbrella Corporation', now(), now(), 'false'),
        ('E Corp', now(), now(), 'false'),
        ('Cyberdyne Systems', now(), now(), 'false'),
        ('Universal Dynamics', now(), now(), 'false'),
        ('REKALL', now(), now(), 'false')
ON CONFLICT (client_name)
DO NOTHING;

INSERT INTO orders (
title,
description,
start_line,
dead_line,
agent_id,
client_id,
creation_date,
last_updated,
deleted,
completed_date)
SELECT  v.title, v.description, v.start_line, v.dead_line,
        v.agent_id, v.client_id, v.creation_date, v.last_updated, v.deleted, v.completed_date
FROM ( VALUES
        (
            'Установка лазерных детекторов движения',
            'Необходим дополнительный ряд лазеров на потолке, а также инфракрасные датчики тепла',
            CURRENT_DATE,
            CURRENT_DATE + INTERVAL '12 day',
            2,
            1,
            NOW(),
            NOW(),
            'false'::boolean,
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
            'false'::boolean,
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
            'true'::boolean,
            null::timestamp
        )
) AS  v(title, description, start_line, dead_line,
    agent_id, client_id, creation_date, last_updated,
    deleted, completed_date)
WHERE NOT EXISTS (
    SELECT 1 FROM orders o WHERE o.title = v.title
);
