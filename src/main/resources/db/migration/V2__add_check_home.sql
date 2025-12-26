-- HOME 로그인을 위한 password 컬럼 추가
ALTER TABLE hh_users
    ADD COLUMN password VARCHAR(100) NULL AFTER email;

-- 마지막 로그인 시각 컬럼 추가
ALTER TABLE hh_users
    ADD COLUMN login_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP AFTER updated_at;

-- provider / provider_id NULL 허용으로 변경
ALTER TABLE hh_users
    MODIFY COLUMN provider VARCHAR(20) NULL,
    MODIFY COLUMN provider_id VARCHAR(255) NULL;

-- CHECK 제약 추가:
ALTER TABLE hh_users
ADD CONSTRAINT chk_home_user_credentials
CHECK (
    provider <> 'HOME' OR (email IS NOT NULL AND password IS NOT NULL)
);

ALTER TABLE hh_users
    ADD UNIQUE KEY uk_hh_users_email (email);
