drop table if exists public.t_menu_entity CASCADE;
drop table if exists public.t_order CASCADE;
drop table if exists public.t_order_item CASCADE;
drop table if exists public.t_tea_maker CASCADE;

create table public.t_menu_entity (
    id serial,
    name varchar(128),
    size varchar(16),
    price bigint,
    create_time timestamp,
    update_time timestamp,
    primary key (id)
);

create table public.t_order (
     id serial,
     amount_discount integer,
     amount_pay bigint,
     amount_total bigint,
     create_time timestamp,
     status integer,
     update_time timestamp,
     maker_id bigint,
     primary key (id)
);

create table public.t_order_item (
      item_id bigint not null,
      order_id bigint not null
);

create table public.t_tea_maker (
     id serial,
     create_time timestamp,
     name varchar(255),
     update_time timestamp,
     primary key (id)
);