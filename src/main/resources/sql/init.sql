-- 어드민 등록
INSERT INTO member (email, username, password, role)
SELECT 'admin@dana.com', 'admin', '$2a$12$ZK0bdm7.iVVpV2cWlwrO/.FOYxJXoqBHEAvNkRq8ll2KwaszY.bGS', 'ADMIN'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM member WHERE email = 'admin@dana.com');