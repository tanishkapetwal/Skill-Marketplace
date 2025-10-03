
CREATE TABLE customer (
    id integer NOT NULL,
    user_id integer
);
CREATE SEQUENCE customer_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE customer ALTER COLUMN id SET DEFAULT nextval('customer_id_seq');
CREATE TABLE orders (
    id integer NOT NULL,
    appointment_end timestamp(6) without time zone,
    appointment_start timestamp(6) without time zone,
    order_date date,
    order_rating integer NOT NULL,
    status smallint,
    customer_id integer,
    skills_listing_id integer,
    CONSTRAINT orders_status_check CHECK (((status >= 0) AND (status <= 3)))
);
CREATE SEQUENCE orders_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE orders ALTER COLUMN id SET DEFAULT nextval('orders_id_seq');
CREATE TABLE seller (
    id integer NOT NULL,
    avg_rating double precision,
    bio character varying(255),
    rating integer NOT NULL,
    rating_count integer NOT NULL,
    user_id integer
);
CREATE SEQUENCE seller_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE seller ALTER COLUMN id SET DEFAULT nextval('seller_id_seq');
CREATE TABLE skills (
    id integer NOT NULL,
    category character varying(255) NOT NULL,
    description character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    CONSTRAINT skills_category_check CHECK (((category)::text = ANY ((ARRAY['LANGUAGE'::character varying, 'FRONTEND'::character varying, 'BACKEND'::character varying, 'DATASCIENCE'::character varying, 'MACHINELEARNING'::character varying])::text[])))
);
CREATE SEQUENCE skills_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE skills ALTER COLUMN id SET DEFAULT nextval('skills_id_seq');
CREATE TABLE skills_listing (
    id integer NOT NULL,
    avg_rating double precision,
    description character varying(255) NOT NULL,
    price real NOT NULL,
    rating_count integer NOT NULL,
    "time" double precision NOT NULL,
    title character varying(255) NOT NULL,
    seller_id integer,
    skills_id integer
);
CREATE SEQUENCE skills_listing_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE skills_listing ALTER COLUMN id SET DEFAULT nextval('skills_listing_id_seq');
CREATE TABLE users (
    id integer NOT NULL,
    created_at timestamp(6) without time zone,
    email character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    password character varying(60) NOT NULL,
    phone character varying(255),
    role character varying(255),
    updated_at timestamp(6) without time zone,
    CONSTRAINT users_role_check CHECK (((role)::text = ANY ((ARRAY['ADMIN'::character varying, 'CUSTOMER'::character varying, 'SELLER'::character varying])::text[])))
);
CREATE SEQUENCE users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE users ALTER COLUMN id SET DEFAULT nextval('users_id_seq');

INSERT INTO users (id, created_at, email, name, password, phone, role, updated_at) VALUES
(1, '2025-09-19 14:08:43.315796', 'ashutosh.acharya@argusoft.com', 'Ashutosh Acharya', '$2a$12$HM49kLcjNZ/A.D.I7Y3uru45j0qn/wW.rXPECWcNm0EScj3YQdF3O', '8093107856', 'SELLER', '2025-09-19 14:08:43.315872'),
(3, '2025-09-22 18:03:39.078049', 'ashutosh20.acharya@gmail.com', 'Ashutosh Acharya', '$2a$10$v8IssBTjpw7p4pGaBG8tIe3ZrQWc9k50UlioqzDilicdyry.k1ija', '8093107856', 'ADMIN', '2025-09-22 18:03:39.078132'),
(4, '2025-09-25 11:07:41.840246', 'ashutosh23.acharya@gmail.com', 'Ashutosh Acharya', '$2a$10$KMnbkA4xRcTxKozZHG8c5.wvo19EdX7gppRH2b7Ul2GMFfE6OFHkK', '9777575741', 'ADMIN', '2025-09-25 11:07:41.840292'),
(5, '2025-09-25 11:08:34.131022', 'ashutosh03.acharya@gmail.com', 'Ashutosh Acharya', '$2a$10$WpEW1y6RRlm73dkJqs6llOj2KSmhKbxfdNNpZ/ff21nE.WP2hidYm', '9876543210', 'CUSTOMER', '2025-09-25 11:08:34.131046'),
(6, '2025-09-25 16:34:50.803865', 'abc@gmail.com', 'Ashutosh Acharya', '$2a$10$1Z8qkhy5MSaqhlzDol8rx.HK8kyNQ3UZl6Lutcb6rLJ8l5etOsu2q', '7867564589', 'ADMIN', '2025-09-25 16:34:50.80389'),
(9, '2025-09-29 18:23:20.060166', 'abef@gmail.com', 'Ashutosh Acharya', '$2a$10$cLRzkLGm2tZy6J6MAVA8nes8T8DdO2V.24Z8pYvvXdnK2nOw.AukO', '9861293786', 'ADMIN', '2025-09-29 18:23:20.0602');
INSERT INTO customer (id, user_id) VALUES
(3, 5);
INSERT INTO seller (id, avg_rating, bio, rating, rating_count, user_id) VALUES
(1, NULL, 'i know all things', 0, 0, 1);
INSERT INTO skills (id, category, description, name) VALUES
(5, 'LANGUAGE', 'Learn Hindi With Ashutosh Acharya', 'Hindi');

SELECT setval('customer_id_seq', (SELECT COALESCE(MAX(id), 1) FROM customer));
SELECT setval('orders_id_seq', 16);
SELECT setval('seller_id_seq', (SELECT COALESCE(MAX(id), 1) FROM seller));
SELECT setval('skills_id_seq', (SELECT COALESCE(MAX(id), 1) FROM skills));
SELECT setval('skills_listing_id_seq', 21);
SELECT setval('users_id_seq', (SELECT COALESCE(MAX(id), 1) FROM users));

ALTER TABLE ONLY customer
    ADD CONSTRAINT customer_pkey PRIMARY KEY (id);
ALTER TABLE ONLY orders
    ADD CONSTRAINT orders_pkey PRIMARY KEY (id);
ALTER TABLE ONLY seller
    ADD CONSTRAINT seller_pkey PRIMARY KEY (id);
ALTER TABLE ONLY skills_listing
    ADD CONSTRAINT skills_listing_pkey PRIMARY KEY (id);
ALTER TABLE ONLY skills
    ADD CONSTRAINT skills_pkey PRIMARY KEY (id);
ALTER TABLE ONLY users
    ADD CONSTRAINT uk6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email);
ALTER TABLE ONLY seller
    ADD CONSTRAINT uketfpl3vymasxfqc4ri4r3euf6 UNIQUE (user_id);
ALTER TABLE ONLY customer
    ADD CONSTRAINT ukj7ja2xvrxudhvssosd4nu1o92 UNIQUE (user_id);
ALTER TABLE ONLY users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);

ALTER TABLE ONLY skills_listing
    ADD CONSTRAINT fk1l47xnxs48mc7pbw2ownby0vt FOREIGN KEY (skills_id) REFERENCES skills(id);
ALTER TABLE ONLY seller
    ADD CONSTRAINT fk614u1eblpnxmrxd25efo29qhr FOREIGN KEY (user_id) REFERENCES users(id);
ALTER TABLE ONLY orders
    ADD CONSTRAINT fk624gtjin3po807j3vix093tlf FOREIGN KEY (customer_id) REFERENCES customer(id);
ALTER TABLE ONLY skills_listing
    ADD CONSTRAINT fkkbwj7u5btyeacxioswp3i8k1u FOREIGN KEY (seller_id) REFERENCES seller(id);
ALTER TABLE ONLY orders
    ADD CONSTRAINT fkm30rokbfwwgf2i459mcrqwdl1 FOREIGN KEY (skills_listing_id) REFERENCES skills_listing(id);
ALTER TABLE ONLY customer
    ADD CONSTRAINT fkra1cb3fu95r1a0m7aksow0nk4 FOREIGN KEY (user_id) REFERENCES users(id);
