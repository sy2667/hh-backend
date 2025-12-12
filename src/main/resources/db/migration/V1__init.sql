CREATE TABLE IF NOT EXISTS hh_users (
                                        user_pk INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                                        nick_name VARCHAR(20),
    email VARCHAR(100),
    provider VARCHAR(20) NOT NULL,
    provider_id VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP DEFAULT NULL,
    UNIQUE KEY unique_provider_user (provider, provider_id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS hh_categories (
                                             category_pk INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                                             user_pk INT UNSIGNED NOT NULL,
                                             category_name VARCHAR(20) NOT NULL,
    category_type ENUM('1','2') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_pk) REFERENCES hh_users(user_pk) ON DELETE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS hh_transaction(
                                             transaction_pk INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                                             user_pk INT UNSIGNED NOT NULL,
                                             category_pk INT UNSIGNED NOT NULL,
                                             transaction_type ENUM('1','2') NOT NULL,
    amount BIGINT NOT NULL,
    description VARCHAR(200),
    transaction_date DATETIME NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_pk) REFERENCES hh_users(user_pk) ON DELETE CASCADE,
    FOREIGN KEY (category_pk) REFERENCES  hh_categories(category_pk) ON DELETE RESTRICT,
    INDEX idx_user_date (user_pk, transaction_date)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS hh_common_code (
                                              code_pk INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                                              group_code VARCHAR(50) NOT NULL,
    code_value VARCHAR(20) NOT NULL,
    code_name VARCHAR(50) NOT NULL,
    use_yn CHAR(1) DEFAULT 'Y',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY unique_group_value (group_code, code_value)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
