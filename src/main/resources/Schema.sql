
CREATE TABLE Garment (
	"id" SERIAL NOT NULL PRIMARY KEY,
	"type" VARCHAR(100) NOT NULL,
	"size" VARCHAR(50) NOT NULL,
	"main_color" VARCHAR(150) NOT NULL,
	"gender" VARCHAR(60) NOT NULL,
	"main_material" VARCHAR(150) NOT NULL,
	"madeIn" VARCHAR(150) NOT NULL,
	"price" INTEGER NOT NULL,
	"comment" VARCHAR(254)
)

CREATE TABLE User (
    "id" SERIAL NOT NULL PRIMARY KEY,
    "name" VARCHAR(50) NOT NULL,
    "lastname" VARCHAR(50) NOT NULL,
    "email" VARCHAR(50) NOT NULL,
    address VARCHAR(200),
    "username" VARCHAR(50) NOT NULL,
    "password" VARCHAR(255) NOT NULL
)