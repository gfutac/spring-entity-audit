--SET MODE MSSQLServer; --set h2 compatibility mode to SQL Server, to be more similar to production database

CREATE TABLE IF NOT EXISTS Author(
  AuthorID bigint auto_increment primary key,
  Name varchar(64)
);

CREATE TABLE IF NOT EXISTS Book(
  BookID bigint auto_increment primary key,
  Name varchar(128),
  AuthorID bigint references Author(AuthorID)
);

insert into Author( Name) values
    ('John Doe'),
    ('Danna Cottie'),
    ('Gabbie Glenton'),
    ('Gwendolen Kiwitz'),
    ('Annabell Pettecrew');


insert into Book(Name, AuthorID) values
    ('My greatest work', 1);
