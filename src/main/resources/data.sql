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
id,
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