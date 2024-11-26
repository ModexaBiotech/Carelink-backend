-- Create a temporary function to check if a string starts with '$2a$'
CREATE OR REPLACE FUNCTION is_bcrypt(text) RETURNS boolean AS $$
BEGIN
    RETURN substring($1 from 1 for 4) = '$2a$';
END;
$$ LANGUAGE plpgsql;

-- Update passwords that are not already BCrypt hashed
-- Note: This is just a placeholder since we can't actually rehash passwords
-- In a real production environment, you would need to:
-- 1. Notify users to reset their passwords
-- 2. Temporarily disable login for non-BCrypt passwords
-- 3. Provide a password reset flow
UPDATE users 
SET password = '$2a$10$invalidhashresetrequired' 
WHERE NOT is_bcrypt(password);

-- Drop the temporary function
DROP FUNCTION is_bcrypt(text);
