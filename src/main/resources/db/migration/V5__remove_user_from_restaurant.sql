ALTER TABLE restaurants DROP CONSTRAINT IF EXISTS restaurants_user_id_fkey;
ALTER TABLE restaurants DROP COLUMN IF EXISTS user_id; 