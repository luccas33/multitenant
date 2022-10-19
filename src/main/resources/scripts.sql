
create table public.app_version(
    id_app_version bigint primary key,
    version_code varchar(20) not null unique,
    access_url varchar(100) not null,
    create_script text not null,
    update_script text not null
);

create table public.tenant(
    id_tenant bigint primary key,
    id_app_version bigint not null,
    name varchar(50) not null unique,
    schema_name varchar(20) not null unique,
    created boolean not null default false,
    updated boolean not null default false
);
alter table public.tenant add constraint fk_tenant_appversion foreign key (id_app_version) references public.app_version (id_app_version);
create sequence public.tenant_seq;

create table public.user(
    id_user bigint primary key,
    id_tenant bigint not null,
    name varchar(50) not null,
    email varchar(50) not null,
    password varchar(100) not null,
    adm boolean not null default false,
    unique (email, id_tenant)
);
alter table public.user add constraint fk_user_tenant foreign key (id_tenant) references public.tenant (id_tenant);
create sequence public.user_seq;

insert into public.app_version (id_app_version, version_code, access_url, create_script, update_script)
values (1, '0.0.1', 'http://localhost:8033/multitenant',
'
create table login_log(
     id_login_log bigint primary key,
     id_user bigint not null,
     logged_at timestamp not null
 );
 DELIMITER ;
 alter table login_log add constraint fk_loginlog_user foreign key (id_user) references public.user (id_user);
 DELIMITER ;
 create sequence login_log_seq;
',
'
create table test(
   id_test bigint primary key,
   description varchar(100) not null
);
DELIMITER ;
insert into test (id_test, description) values (1, ''Testing 123...'');
'
);

insert into public.tenant (id_tenant, id_app_version, name, schema_name, created, updated)
values (nextval('public.tenant_seq'), 1, 'First Tenant', 'first', false, false);

insert into public.user (id_user, id_tenant, name, email, password, adm)
values (nextval('public.user_seq'), 1, 'Admin', 'admin@mail.com', upper(md5('admin@mail.com_user-pass_123456')), true);
