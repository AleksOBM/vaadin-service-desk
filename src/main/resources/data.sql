MERGE INTO AGENTS (entity_type, AGENT_NAME, CREATION_DATE, last_updated, deleted) KEY (AGENT_NAME)
VALUES 	('AGENT', 'Чарльз Ксавьер', now(), now(), 'false'),
		('AGENT', 'Скотт Саммерс', now(), now(), 'false'),
		('AGENT', 'Роберт Дрейк', now(), now(), 'false'),
		('AGENT', 'Уоррен Уортингтон III', now(), now(), 'false'),
		('AGENT', 'Генри Маккой', now(), now(), 'false'),
		('AGENT', 'Джин Грей-Саммерс', now(), now(), 'false');

MERGE INTO CLIENTS (entity_type, CLIENT_NAME, CREATION_DATE, last_updated, deleted) KEY (CLIENT_NAME)
VALUES 	('CLIENT', 'Lumon Industries', now(), now(), 'false'),
		('CLIENT', 'Umbrella Corporation', now(), now(), 'false'),
		('CLIENT', 'E Corp', now(), now(), 'false'),
		('CLIENT', 'Cyberdyne Systems', now(), now(), 'false'),
		('CLIENT', 'Universal Dynamics', now(), now(), 'false'),
		('CLIENT', 'REKALL', now(), now(), 'false');

MERGE INTO ORDERS (
entity_type, title, description, start_line, dead_line, agent_id, client_id, creation_date, last_updated, deleted)
KEY (title)
VALUES 	(
            'ORDER',
			'Установка лазерных детекторов движения',
			'Необходим дополнительный ряд лазеров на потолке, а также инфракрасные датчики тепла',
			TIMESTAMPADD(DAY, 1, NOW()),
			TIMESTAMPADD(DAY, 12, NOW()),
			2,
			1,
			now(),
			now(),
			'false'
		),
		(
		    'ORDER',
			'Замена камер видеонаблюдения',
			'Камеры должны обладать рентгеновским зрением',
			TIMESTAMPADD(DAY, 4, NOW()),
			TIMESTAMPADD(DAY, 10, NOW()),
			4,
			5,
			now(),
			now(),
			'false'
		),
		(
		    'ORDER',
			'Настройка оборудования в подземной лаборатории',
			'Осторожно, там выращивают клонов-мутантов и профессор чекнутый',
			TIMESTAMPADD(DAY, 8, NOW()),
			TIMESTAMPADD(DAY, 27, NOW()),
			1,
			3,
			now(),
			now(),
			'true'
		);