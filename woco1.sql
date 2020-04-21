/****************************************************************************
CSE532 -- Project 2
File name: woco1.sql
Author: KUSHAGRA PAREEK (112551443)
Brief description: This file creates the database and tables for the project
****************************************************************************/

/**
I pledge my honor that all parts
of this project were done by me alone and without collaboration with
anybody else.
**/



CREATE DATABASE woco1
  WITH OWNER = postgres
       ENCODING = 'UTF8'
       TABLESPACE = pg_default
       LC_COLLATE = 'en_US.UTF-8'
       LC_CTYPE = 'en_US.UTF-8'
       CONNECTION LIMIT = -1;

--connect to database
\c woco1

--create a type dictionary

CREATE TYPE public.dict AS
   (id character varying(20),
    amount bigint);
ALTER TYPE public.dict
  OWNER TO postgres;


CREATE TABLE public.company_details
(
  c_id character varying(20) NOT NULL,
  name text NOT NULL,
  shares bigint CHECK(shares > 0),
  share_price bigint,
  owned_by dict[],
  board text[],
  owns dict[],
  industry text[],
  CONSTRAINT c_id_pk PRIMARY KEY (c_id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.company_details
  OWNER TO postgres;


CREATE TABLE public.person_details
(
  p_id character varying(20) NOT NULL,
  name text NOT NULL,
  owns dict[],
  board_member text[],
  CONSTRAINT p_id_pk PRIMARY KEY (p_id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.person_details
  OWNER TO postgres;

--Insert in company_details

INSERT INTO public.company_details (c_id, name, shares, share_price, owned_by, board, owns, industry) VALUES ('c1', 'QUE', 150000, 30, '{"(p3,20000)","(p5,50000)","(p7,30000)"}', '{p3,p1,p4}', '{"(c2,10000)","(c4,20000)","(c8,30000)"}', '{Software,Accounting}');
INSERT INTO public.company_details (c_id, name, shares, share_price, owned_by, board, owns, industry) VALUES ('c2', 'RHC', 250000, 20, '{"(p3,20000)","(p4,30000)","(p5,70000)","(p6,40000)","(p7,-9000)","(p8,60000)"}', '{p2,p1,p5}', NULL, '{Accounting}');
INSERT INTO public.company_details (c_id, name, shares, share_price, owned_by, board, owns, industry) VALUES ('c4', 'Elgog', 1000000, 400, '{"(p2,20000)","(p7,30000)"}', '{p5,p6,p7}', '{"(c6,5000)"}', '{Software,Search}');
INSERT INTO public.company_details (c_id, name, shares, share_price, owned_by, board, owns, industry) VALUES ('c5', 'Tfos', 10000000, 300, '{"(p1,30000)","(p3,800000)","(p4,40000)","(p7,300000)"}', '{p2,p4,p5}', '{"(c6,30000)","(c7,50000)","(c1,200000)"}', '{Software,Hardware}');
INSERT INTO public.company_details (c_id, name, shares, share_price, owned_by, board, owns, industry) VALUES ('c6', 'Ohay', 180000, 50, '{"(p5,50000)","(p8,-40000)"}', '{p2,p4,p8}', NULL, '{Search}');
INSERT INTO public.company_details (c_id, name, shares, share_price, owned_by, board, owns, industry) VALUES ('c7', 'Gnow', 150000, 300, '{"(p2,40000)","(p7,80000)"}', '{p2,p3,p4}', NULL, '{Search}');
INSERT INTO public.company_details (c_id, name, shares, share_price, owned_by, board, owns, industry) VALUES ('c8', 'Elpa', 9000000, 300, '{"(p1,100000)","(p5,90000)","(p8,30000)"}', '{p2,p3,p8}', '{"(c4,30000)","(c5,20000)"}', '{Software,Hardware}');
INSERT INTO public.company_details (c_id, name, shares, share_price, owned_by, board, owns, industry) VALUES ('c9', 'Ydex', 5000000, 100, '{"(p6,-40000)","(p8,-80000)"}', '{p6,p3,p8}', NULL, '{Software,Search}');
INSERT INTO public.company_details (c_id, name, shares, share_price, owned_by, board, owns, industry) VALUES ('c3', 'Alf', 10000000, 700, '{"(p4,500000)","(p6,500000)"}', '{p6,p7,p1}', '{"(c9,-100000)","(c4,400000)","(c8,100000)"}', '{Software,Automotive}');


--Insert in person_details

INSERT INTO public.person_details (p_id, name, owns, board_member) VALUES ('p1', 'Bill Doe', '{"(c5,30000)","(c8,100000)"}', '{c1,c2,c3}');
INSERT INTO public.person_details (p_id, name, owns, board_member) VALUES ('p2', 'Bill Seth', '{"(c7,40000)","(c4,20000)"}', '{c2,c5,c6,c7,c8}');
INSERT INTO public.person_details (p_id, name, owns, board_member) VALUES ('p3', 'John Smyth', '{"(c1,20000)","(c2,20000)","(c5,800000)"}', '{c1,c7,c8,c9}');
INSERT INTO public.person_details (p_id, name, owns, board_member) VALUES ('p4', 'Anne Smyle', '{"(c2,30000)","(c5,40000)","(c3,500000)"}', '{c1,c5,c6}');
INSERT INTO public.person_details (p_id, name, owns, board_member) VALUES ('p5', 'Steve Lamp', '{"(c8,90000)","(c1,50000)","(c6,50000)","(c2,70000)"}', '{c2,c4,c5}');
INSERT INTO public.person_details (p_id, name, owns, board_member) VALUES ('p6', 'May Serge', '{"(c8,-10000)","(c9,-40000)","(c3,500000)","(c2,40000)"}', '{c3,c4,c9}');
INSERT INTO public.person_details (p_id, name, owns, board_member) VALUES ('p7', 'Bill public', '{"(c7,80000)","(c4,30000)","(c1,30000)","(c5,300000)","(c2,-9000)"}', '{c3,c4}');
INSERT INTO public.person_details (p_id, name, owns, board_member) VALUES ('p8', 'Muck Lain', '{"(c2,60000)","(c6,-40000)","(c9,-80000)","(c8,30000)"}', '{c6,c8,c9}');



