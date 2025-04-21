ALTER TABLE users DROP CONSTRAINT IF EXISTS users_restaurant_id_fkey;
ALTER TABLE users DROP COLUMN IF EXISTS restaurant_id; 