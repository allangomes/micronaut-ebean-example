-- apply changes
create table person (
  id                            bigserial not null,
  name                          varchar(255),
  constraint pk_person primary key (id)
);

