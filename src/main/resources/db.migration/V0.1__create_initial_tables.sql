CREATE TABLE IF NOT EXISTS users(
   id bigserial primary key,
   firstname text not null,
   lastname text not null,
   email text not null,
   reference text not null,
   phone text not null,
   password_hash text not null
);

CREATE TABLE IF NOT EXISTS Companies(
   id bigserial primary key,
   company_Name text not null
);

CREATE TABLE IF NOT EXISTS User_Company_Entitlement(
   id bigserial primary key,
   user_Id bigint REFERENCES users (id),
   company_Id bigint REFERENCES companies (id),
   role text not null
);