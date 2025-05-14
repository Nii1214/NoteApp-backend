CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,      -- ユーザーのユニークID
    username VARCHAR(50) NOT NULL,             -- ユーザー名
    email VARCHAR(100) NOT NULL,               -- メールアドレス
    password VARCHAR(255) NOT NULL,            -- パスワード
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- アカウント作成日時
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- 最終更新日時
    status ENUM('active', 'inactive', 'banned') DEFAULT 'active', -- ユーザー状態
    UNIQUE (email),                            -- メールアドレスはユニーク
    UNIQUE (username)                          -- ユーザー名もユニーク
);