CREATE TABLE user_role (
    "id" SERIAL NOT NULL PRIMARY KEY,
    "role" VARCHAR(200) NOT NULL
);

CREATE TABLE user_account (
    "id" SERIAL NOT NULL PRIMARY KEY,
    id_user_role serial NOT NULL,
    "name" VARCHAR(55) NOT NULL,
    "password" VARCHAR(55) NOT NULL,
    FOREIGN KEY (id_user_role) REFERENCES user_role("id")
);