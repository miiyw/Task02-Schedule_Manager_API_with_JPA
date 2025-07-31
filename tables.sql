-- users 테이블 생성 (일정을 작성한 유저를 위한 테이블)
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,  -- 고유 ID
    name VARCHAR(100) NOT NULL,         -- 이름
    email VARCHAR(100) NOT NULL UNIQUE, -- 이메일
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- schedules 테이블 생성 (일정을 저장할 테이블)
CREATE TABLE schedules (
    id INT AUTO_INCREMENT PRIMARY KEY,  -- 일정 고유 ID
    user_name VARCHAR(100) NOT NULL,    -- 작성 유저명
    title VARCHAR(200) NOT NULL,        -- 할 일 제목
    content TEXT,                       -- 할 일 내용
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 작성일
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP  -- 수정일
);
