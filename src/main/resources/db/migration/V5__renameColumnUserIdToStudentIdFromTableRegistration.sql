ALTER TABLE registration RENAME COLUMN user_id TO student_id,
DROP FOREIGN KEY FK_User,
ADD CONSTRAINT FK_Student FOREIGN KEY (student_id) REFERENCES user(id);