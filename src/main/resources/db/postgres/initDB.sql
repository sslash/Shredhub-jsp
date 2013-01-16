--**********************************************************************--
--							SHREDDER									--
--**********************************************************************--
CREATE TABLE Shredder (
	Id					serial PRIMARY KEY, -- INSERT INTO users (name, age, id) VALUES ('Mozart', 20, DEFAULT);
	Username			varchar(40) NOT NULL UNIQUE,
	BirthDate			date NOT NULL CHECK (BirthDate > '1900-01-01'),	-- No need to save age: Useless data 
	Email				varchar(50) NOT NULL UNIQUE,
	Password			varchar(10) NOT NULL, -- more security here
	Description			text,
	Country				text,
	TimeCreated			timestamp DEFAULT CURRENT_TIMESTAMP,
	ProfileImage		text,
	ExperiencePoints	int	DEFAULT (0),
	ShredderLevel		int DEFAULT (1)
	-- Guitars
	-- Equiptments
	-- Tags
);

CREATE TABLE UserRole (
  Id serial PRIMARY KEY,
  ShredderId serial REFERENCES Shredder(Id),
  Authority VARCHAR(45) DEFAULT 'ROLE_SHREDDER'
);

CREATE TABLE Fan (
	FanerId			serial REFERENCES Shredder(Id), -- The guy who is a fan of fanee
	FaneeId			serial REFERENCES Shredder(Id), -- The guy who is being fan'ed
	TimeCreated		timestamp DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT pk_Fan PRIMARY KEY (FanerId, FaneeId)
);

SELECT * FROM Shred s, ShredForBattle sfb, Shredder sr WHERE sfb.BattleId=17 AND s.Id = sfb.ShredId AND s.owner = sr.id  ORDER By round;

CREATE TABLE Tag (
	Id			serial PRIMARY KEY,
	Label		varchar(20) NOT NULL
);

CREATE TABLE GuitarForShredder(
	Guitar				varchar(50) NOT NULL,
	ShredderId			serial REFERENCES Shredder(Id),
	CONSTRAINT pk_GuitarForShredder PRIMARY KEY (Guitar, ShredderId)
);


CREATE TABLE EquiptmentForShredder(
	Equiptment			varchar(50) NOT NULL,
	ShredderId			serial REFERENCES Shredder(Id),
	CONSTRAINT pk_EquiptmentForShredder PRIMARY KEY (Equiptment, ShredderId)
);

CREATE TABLE TagForShredder(
	ShredderId			serial REFERENCES Shredder(Id),
	TagId				serial REFERENCES Tag(Id),
	CONSTRAINT pk_TagForShredder PRIMARY KEY (ShredderId, TagId)
);

CREATE TABLE ImageForShredder(
	Id					serial PRIMARY KEY,
	ShredderId			serial REFERENCES Shredder(Id),
	ImagePath			varchar(100) NOT NULL,
	Description			text,
	UNIQUE(ShredderId, ImagePath)
);

--**********************************************************************--
--							SHRED									--
--**********************************************************************--
CREATE TABLE Shred (
	Id				serial PRIMARY KEY,
	Description		text,
	Owner			serial REFERENCES Shredder(Id),
	TimeCreated		timestamp DEFAULT CURRENT_TIMESTAMP,
	VideoPath		varchar(100) NOT NULL,
	ShredType		varchar(30) DEFAULT 'normal' CHECK (ShredType ='normal' or ShredType = 'battle'),
	ThumbnailPath	varchar(100) NOT NULL
	-- Rating
	-- Tags
	-- Comments
);

CREATE TABLE TagsForShred(
	ShredId			serial REFERENCES Shred(Id),
	TagId			serial REFERENCES Tag(Id),
	CONSTRAINT pk_TagsForShred PRIMARY KEY (ShredId, TagId)	
);

CREATE TABLE CommentForShred(
	Id			serial PRIMARY KEY,
	Text		text NOT NULL,
	Commenter	serial REFERENCES Shredder(Id),
	TimeCreated	timestamp DEFAULT CURRENT_TIMESTAMP,
	Shred		serial REFERENCES Shred(Id)
);

CREATE TABLE BattleCategori(
	Id				serial PRIMARY KEY,
	CategoriText	text NOT NULL UNIQUE
);
SELECT * FROM BattleCategori;

CREATE TABLE Battle (
	Id				serial PRIMARY KEY,
	Shredder1		serial REFERENCES Shredder(Id),
	Shredder2		serial REFERENCES Shredder(Id),
	TimeCreated	timestamp DEFAULT CURRENT_TIMESTAMP,
	BattleCategori	serial REFERENCES BattleCategori,
	Status			varchar(30) DEFAULT 'awaiting' CHECK (Status ='accepted' or Status ='declined' or Status='awaiting'),
	Round			int DEFAULT 1,
	-- status
	-- Round
	-- Shreds	
);
SELECT * FROM Battle;

CREATE TABLE ShredForBattle(
	ShredId		serial REFERENCES Shred(Id),
	BattleId	serial REFERENCES Battle(Id),
	Round		int DEFAULT 1,
	CONSTRAINT pk_ShredForBattle PRIMARY KEY (BattleId, ShredId)	
);

CREATE TABLE FinishedBattle(
	BattleId		serial REFERENCES Battle(Id),
	Winner			serial REFERENCES Shredder(Id),
	TimeEnded		timestamp DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT pk_FinishedBattle PRIMARY KEY (BattleId)
);


CREATE TABLE Rating (
	ShredId			serial REFERENCES Shred(Id),
	currentRating	int DEFAULT (0),
	numberOfRaters	int DEFAULT (0),
	CONSTRAINT pk_Rating PRIMARY KEY (ShredId)
);

