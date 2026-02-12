MERGE INTO AGENTS (AGENT_NAME, CREATION_DATE, last_updated) KEY (AGENT_NAME)
VALUES 	('Чарльз Ксавьер', now(), now()),
		('Скотт Саммерс', now(), now()),
		('Роберт Дрейк', now(), now()),
		('Уоррен Уортингтон III', now(), now()),
		('Генри Маккой', now(), now()),
		('Джин Грей-Саммерс', now(), now());

MERGE INTO CLIENTS (CLIENT_NAME, CREATION_DATE, last_updated) KEY (CLIENT_NAME)
VALUES 	('Lumon Industries', now(), now()),
		('Umbrella Corporation', now(), now()),
		('E Corp', now(), now()),
		('Cyberdyne Systems', now(), now()),
		('Universal Dynamics', now(), now()),
		('REKALL', now(), now());

MERGE INTO ORDERS (title, description, start_line, dead_line, agent_id, client_id, creation_date, last_updated) KEY (title)
VALUES 	(
			'Установка лазерных детекторов движения',
			'Необходим дополнительный ряд лазеров на потолке, а также инфракрасные датчики тепла',
			TIMESTAMPADD(DAY, 1, NOW()),
			TIMESTAMPADD(DAY, 12, NOW()),
			2,
			1,
			now(),
			now()
		),
		(
			'Замена камер видеонаблюдения',
			'Камеры должны обладать рентгеновским зрением',
			TIMESTAMPADD(DAY, 4, NOW()),
			TIMESTAMPADD(DAY, 10, NOW()),
			4,
			5,
			now(),
			now()
		),
		(
			'Настройка оборудования в подземной лаборатории',
			'Осторожно, там выращивают клонов-мутантов и профессор чекнутый',
			TIMESTAMPADD(DAY, 8, NOW()),
			TIMESTAMPADD(DAY, 27, NOW()),
			1,
			3,
			now(),
			now()
		);