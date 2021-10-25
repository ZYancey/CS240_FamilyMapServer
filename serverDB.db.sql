BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "user" (
	"username"	varchar(32) NOT NULL,
	"password"	varchar(32) NOT NULL,
	"email"	varchar(32),
	"first_name"	varchar(32) NOT NULL,
	"last_name"	varchar(32) NOT NULL,
	"gender"	varchar(1),
	"person_id"	varchar(32) NOT NULL,
	PRIMARY KEY("username")
);
CREATE TABLE IF NOT EXISTS "person" (
	"person_id"	varchar(32) NOT NULL,
	"username"	varchar(32) NOT NULL,
	"first_name"	varchar(32) NOT NULL,
	"last_name"	varchar(32) NOT NULL,
	"gender"	varchar(1),
	"father_id"	varchar(32) NOT NULL,
	"mother_id"	varchar(32) NOT NULL,
	"spouse_id"	varchar(32) NOT NULL,
	PRIMARY KEY("username")
);
CREATE TABLE IF NOT EXISTS "event" (
	"event_id"	varchar(32) NOT NULL,
	"username"	varchar(32) NOT NULL,
	"person_id"	varchar(32) NOT NULL,
	"latitude"	float,
	"longitude"	float,
	"country"	varchar(32) NOT NULL,
	"city"	varchar(32) NOT NULL,
	"event_type"	varchar(32) NOT NULL,
	"year"	int,
	PRIMARY KEY("username")
);
CREATE TABLE IF NOT EXISTS "authtoken" (
	"token"	varchar(32) NOT NULL,
	"username"	varchar(32) NOT NULL,
	PRIMARY KEY("username")
);
COMMIT;
