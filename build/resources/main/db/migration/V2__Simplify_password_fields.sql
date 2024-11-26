-- Remove redundant password fields
ALTER TABLE users DROP COLUMN password_hash;
ALTER TABLE users DROP COLUMN salt;
