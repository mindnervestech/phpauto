# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table auth_user (
  id                        integer auto_increment not null,
  email                     varchar(255),
  first_name                varchar(255),
  last_name                 varchar(255),
  password                  varchar(255),
  provider                  varchar(255),
  constraint pk_auth_user primary key (id))
;




# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table auth_user;

SET FOREIGN_KEY_CHECKS=1;

