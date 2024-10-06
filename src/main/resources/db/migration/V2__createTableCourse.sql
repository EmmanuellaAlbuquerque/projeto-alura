CREATE TABLE Course (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    name varchar(100) NOT NULL,
    code varchar(10) NOT NULL,
    instructor_id bigint(20) NOT NULL,
    description varchar(255) NOT NULL,
    status enum('ACTIVE', 'INACTIVE') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT 'ACTIVE',
    inactivationDate datetime,
    PRIMARY KEY (id),
    CONSTRAINT UC_Code UNIQUE (code),
    CONSTRAINT FK_Instructor FOREIGN KEY (instructor_id) REFERENCES user(id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;