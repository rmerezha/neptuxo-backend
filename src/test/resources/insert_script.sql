INSERT INTO users (username, email, passwd)
VALUES
    ('user1', 'euser1@neptuxo.space', 'user+passwd1'),
    ('user2', 'euser2@neptuxo.space', 'user+passwd2'),
    ('user3', 'euser3@neptuxo.space', 'user+passwd3'),
    ('user4', 'euser4@neptuxo.space', 'user+passwd4'),
    ('user5', 'euser5@neptuxo.space', 'user+passwd5'),
    ('user6', 'euser6@neptuxo.space', 'user+passwd6'),
    ('user7', 'euser7@neptuxo.space', 'user+passwd7'),
    ('user8', 'euser8@neptuxo.space', 'user+passwd8'),
    ('user9', 'euser9@neptuxo.space', 'user+passwd9'),
    ('user10', 'euser10@neptuxo.space', 'user+passwd10');

INSERT INTO product (created_by, description, type, count, created_at, image_path)
VALUES
    (1, '1', 'ANIMALS', 1, now(), '/image/1'),
    (2, '2', 'ANIMALS', 2, now(), '/image/2'),
    (3, '3', 'ANIMALS', 3, now(), '/image/3'),
    (4, '4', 'ANIMALS', 4, now(), '/image/4'),
    (5, '5', 'ANIMALS', 5, now(), '/image/5'),
    (6, '6', 'ANIMALS', 6, now(), '/image/6'),
    (7, '7', 'ANIMALS', 7, now(), '/image/7'),
    (8, '8', 'ANIMALS', 8, now(), '/image/8'),
    (9, '9', 'ANIMALS', 9, now(), '/image/9'),
    (10, '10', 'ANIMALS', 10, now(), '/image/10');

INSERT INTO orders (id, product_id, customer_id, address, status, created_at)
VALUES
    ('607f11b1-6323-4664-9d5e-ed8e21a71a89', 1, 10, '1', 'NEW', now()),
    ('a85a9a53-9256-4f11-bbd3-e8202a8f481a', 2, 9, '1', 'NEW', now()),
    ('bba55e36-b684-459b-bb98-411aa47d2dc0', 3, 8, '1', 'NEW', now()),
    ('3b1078bf-0eab-4942-b320-61549a2329c1', 4, 7, '1', 'NEW', now()),
    ('9f1ed46e-5545-42cc-a797-e5ca762c5b7e', 5, 6, '1', 'NEW', now()),
    ('d3c14be7-cf98-45f5-a5cf-44e667779355', 6, 5, '1', 'NEW', now()),
    ('bf51272f-0a26-4626-ba9b-9286e4637372', 7, 4, '1', 'NEW', now()),
    ('50e566be-9622-4c18-85d8-731e4fb6c510', 8, 3, '1', 'NEW', now()),
    ('b5325301-1487-4361-ad77-fdc5691e1c19', 9, 2, '1', 'NEW', now()),
    ('a640c8e7-c487-4317-a315-1350fe1347f6', 10, 1, '1', 'NEW', now());
