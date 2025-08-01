-- users 테이블 생성 (일정을 작성한 유저를 위한 테이블)
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- 고유 ID
    name VARCHAR(100) NOT NULL,         -- 이름
    email VARCHAR(100) NOT NULL UNIQUE, -- 이메일
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- schedules 테이블 생성 (일정을 저장할 테이블)
CREATE TABLE schedules (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- 일정 고유 ID
    user_name VARCHAR(100) NOT NULL,    -- 작성 유저명
    title VARCHAR(200) NOT NULL,        -- 할 일 제목
    content TEXT,                       -- 할 일 내용
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 작성일
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP  -- 수정일
);

-- comments 테이블 생성
CREATE TABLE comments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- 댓글 고유 ID
    content TEXT NOT NULL,              -- 댓글 내용
    userName VARCHAR(100) NOT NULL,     -- 작성 유저명
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 작성일
    updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,  -- 수정일
    scheduleId BIGINT NOT NULL,  -- 일정 ID (foreign key)
    FOREIGN KEY (scheduleId) REFERENCES schedules(id) ON DELETE CASCADE  -- 일정과 댓글의 연관 관계
);

-- schedule_user 테이블 생성 (일정과 담당 유저의 중간 테이블)
CREATE TABLE schedule_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- 관계 고유 ID
    schedule_id BIGINT NOT NULL,           -- 일정 ID
    user_id BIGINT NOT NULL,               -- 유저 ID
    FOREIGN KEY (schedule_id) REFERENCES schedules(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);


