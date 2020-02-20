SET MODE MSSQLServer; --set h2 compatibility mode to SQL Server, to be more similar to production database

insert into Author(AuthorID, Name) values
    (1, 'John Doe'),
    (2, 'Danna Cottie'),
    (3, 'Gabbie Glenton'),
    (4, 'Gwendolen Kiwitz'),
    (5, 'Annabell Pettecrew');


insert into Book(BookID, Name, AuthorID) values
    (1, 'My greatest work', 1);
