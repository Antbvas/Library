create table if not exists public.authors
(
    id         bigint not null
        constraint authors_pk
            primary key,
    first_name varchar(100),
    last_name  varchar(100)
);

alter table public.authors
    owner to postgres;

create table if not exists public.books
(
    id             bigserial
        constraint books_pk
            primary key,
    title          varchar(255),
    author_id      integer
        constraint books_authors_id_fk
            references public.authors
            on update cascade on delete cascade,
    published_year integer
);

alter table public.books
    owner to postgres;

create table if not exists public.readers
(
    id         bigserial
        constraint readers_pk_2
            primary key,
    first_name varchar(100),
    last_name  varchar(100),
    email      varchar(255)
        constraint readers_pk
            unique
);

alter table public.readers
    owner to postgres;

create table if not exists public.readers_books
(
    reader_id bigint not null
        constraint readers_books_readers_id_fk
            references public.readers
            on delete cascade,
    book_id   bigint not null
        constraint readers_books_books_id_fk
            references public.books
            on delete cascade,
    read_date date default CURRENT_DATE,
    constraint readers_books_pk
        primary key (book_id, reader_id)
);

alter table public.readers_books
    owner to postgres;

