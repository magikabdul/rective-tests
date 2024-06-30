create table anime.anime
(
    id   serial  not null primary key ,
    name varchar not null
);

create unique index id
    on anime.anime (id);
