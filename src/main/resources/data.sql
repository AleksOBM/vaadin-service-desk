INSERT INTO agents (entity_type, agent_name, creation_date, last_updated, deleted)
VALUES 	('AGENT', 'Чарльз Ксавьер', now(), now(), 'false'),
		('AGENT', 'Скотт Саммерс', now(), now(), 'false'),
		('AGENT', 'Роберт Дрейк', now(), now(), 'false'),
		('AGENT', 'Уоррен Уортингтон III', now(), now(), 'false'),
		('AGENT', 'Генри Маккой', now(), now(), 'false'),
		('AGENT', 'Джин Грей-Саммерс', now(), now(), 'false')
ON CONFLICT (agent_name)
DO NOTHING;

INSERT INTO clients (entity_type, client_name, creation_date, last_updated, deleted)
VALUES 	('CLIENT', 'Lumon Industries', now(), now(), 'false'),
		('CLIENT', 'Umbrella Corporation', now(), now(), 'false'),
		('CLIENT', 'E Corp', now(), now(), 'false'),
		('CLIENT', 'Cyberdyne Systems', now(), now(), 'false'),
		('CLIENT', 'Universal Dynamics', now(), now(), 'false'),
		('CLIENT', 'REKALL', now(), now(), 'false')
ON CONFLICT (client_name)
DO NOTHING;

INSERT INTO orders (
id,
entity_type,
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
VALUES 	(
            1,
            'ORDER',
			'Установка лазерных детекторов движения',
			'Необходим дополнительный ряд лазеров на потолке, а также инфракрасные датчики тепла',
			DATE_ADD(NOW(), INTERVAL '1 day'),
			DATE_ADD(NOW(), INTERVAL '12 day'),
			2,
			1,
			now(),
			now(),
			'false',
			null
		),
		(
		    2,
		    'ORDER',
			'Замена камер видеонаблюдения',
			'Камеры должны обладать рентгеновским зрением',
			DATE_ADD(NOW(), INTERVAL '4 day'),
			DATE_ADD(NOW(), INTERVAL '10 day'),
			4,
			5,
			now(),
			now(),
			'false',
			DATE_ADD(NOW(), INTERVAL '1 day')
		),
		(
		    3,
		    'ORDER',
			'Настройка оборудования в подземной лаборатории',
			'Осторожно, там выращивают клонов-мутантов и профессор чекнутый',
			DATE_ADD(NOW(), INTERVAL '8 day'),
			DATE_ADD(NOW(), INTERVAL '27 day'),
			1,
			3,
			now(),
			now(),
			'true',
			null
		)
ON CONFLICT (id)
DO NOTHING;