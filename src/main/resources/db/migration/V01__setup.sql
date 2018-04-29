DO
$body$
BEGIN
   IF NOT EXISTS (
      SELECT *
      FROM   pg_catalog.pg_user
      WHERE  usename = 'xcom-user') THEN

      CREATE USER xcom-user WITH password 'xcom-pass';
   END IF;
END
$body$;

CREATE SCHEMA IF NOT EXISTS xcommerce;

GRANT USAGE ON SCHEMA xcommerce TO xcom-user;

ALTER DEFAULT PRIVILEGES
   IN SCHEMA xcommerce
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLES
   TO xcom-user;

ALTER DEFAULT PRIVILEGES
   IN SCHEMA xcommerce
GRANT USAGE ON SEQUENCES
   TO xcom-user;
