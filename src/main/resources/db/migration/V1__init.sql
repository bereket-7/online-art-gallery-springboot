
    create sequence artwork_seq start with 1 increment by 50;

    create sequence confirmation_token_sequence start with 1 increment by 1;

    create table account_lockout_rule (
        failure_count smallint unique,
        account_lockout_rule_id bigserial not null,
        block_time bigint,
        primary key (account_lockout_rule_id)
    );

    create table artwork (
        price numeric(38,2),
        quantity integer,
        creation_date timestamp(6),
        id bigint not null,
        last_update_date timestamp(6),
        user_id bigint not null,
        artwork_category varchar(255),
        artwork_description varchar(255),
        artwork_name varchar(255),
        size varchar(255),
        status varchar(255) check (status in ('ACCEPTED','REJECTED','PENDING','SOLD')),
        primary key (id)
    );

    create table artwork_image_urls (
        artwork_id bigint not null,
        image varchar(255)
    );

    create table cart (
        quantity integer,
        artwork_id bigint,
        creation_date timestamp(6),
        id bigserial not null,
        user_id bigint,
        primary key (id)
    );

    create table competition (
        competitor_number integer,
        is_voting_closed boolean,
        is_winner_announced boolean,
        end_time timestamp(6),
        expiry_date timestamp(6),
        id bigserial not null,
        competition_description varchar(255),
        title varchar(255),
        primary key (id)
    );

    create table competitor (
        vote integer,
        artist_id bigint,
        competition_id bigint,
        id bigserial not null,
        artwork_description varchar(255),
        category varchar(255),
        image varchar(255),
        primary key (id)
    );

    create table confirmation_token (
        confirmed_at timestamp(6),
        creation_date timestamp(6),
        expiry_date timestamp(6),
        id bigint not null,
        user_id bigint,
        token varchar(255) not null,
        primary key (id)
    );

    create table event (
        capacity integer,
        ticket_price float(53),
        creation_date timestamp(6),
        event_date timestamp(6),
        id bigserial not null,
        user_id bigint,
        event_description varchar(255),
        event_name varchar(255),
        location varchar(255),
        status varchar(255) check (status in ('ACCEPTED','PENDING','REJECTED')),
        image oid,
        primary key (id)
    );

    create table notifications (
        checked boolean,
        creation_date timestamp(6),
        id bigserial not null,
        last_update_date timestamp(6),
        user_id bigint,
        message varchar(255),
        target_email varchar(255),
        primary key (id)
    );

    create table one_time_password (
        notification_type smallint not null check (notification_type between 0 and 6),
        otp_creation_time timestamp(6) not null,
        otp_expiry_time timestamp(6) not null,
        otp_id bigserial not null,
        user_id bigint not null,
        otp_code varchar(255) not null,
        primary key (otp_id)
    );

    create table order_address (
        id bigserial not null,
        order_id bigint unique,
        city varchar(255),
        country varchar(255),
        postal_code varchar(255),
        state varchar(255),
        street varchar(255),
        primary key (id)
    );

    create table orders (
        total_amount numeric(12,2),
        address_id bigint unique,
        id bigserial not null,
        last_update_date timestamp(6),
        order_date timestamp(6),
        payment_log_id bigint unique,
        user_id bigint not null,
        email varchar(255),
        first_name varchar(255),
        last_name varchar(255),
        phone varchar(255),
        secret_code varchar(255),
        status varchar(255) not null check (status in ('PENDING','CONFIRMED','PROCESSING','SHIPPED','DELIVERED','CANCELLED')),
        primary key (id)
    );

    create table payment_log (
        amount numeric(38,2),
        creation_date timestamp(6),
        id bigserial not null,
        user_id bigint,
        status varchar(255) check (status in ('VERIFIED','INTIALIZED')),
        token varchar(255),
        primary key (id)
    );

    create table rating (
        rating_value float(53),
        artwork_id bigint,
        creation_date timestamp(6),
        id bigserial not null,
        last_update_date timestamp(6),
        user_id bigint,
        primary key (id)
    );

    create table report (
        is_checked boolean,
        creation_date timestamp(6),
        id bigserial not null,
        report_detail varchar(255),
        report_name varchar(255) not null,
        reporter_email varchar(255),
        reporter_name varchar(255),
        primary key (id)
    );

    create table standards (
        creation_date timestamp(6),
        id bigserial not null,
        last_update_date timestamp(6),
        standard_description varchar(255),
        standard_type varchar(255) check (standard_type in ('CUSTOMER','ARTIST','ARTWORK','ORGANIZATION')),
        primary key (id)
    );

    create table user_role (
        is_admin boolean not null,
        creation_date timestamp(6),
        issuer_user_id bigint,
        last_update_date timestamp(6),
        role_id bigserial not null,
        role_name varchar(255) not null,
        primary key (role_id)
    );

    create table user_token (
        is_expired boolean not null,
        is_revoked boolean not null,
        expires_at timestamp(6),
        token_id bigserial not null,
        user_id bigint,
        token varchar(255) not null,
        token_type varchar(255) not null check (token_type in ('BEARER','OAUTH','OAUTH2','JWT','REFRESH')),
        primary key (token_id)
    );

    create table users (
        account_verified boolean not null,
        age integer,
        enabled boolean,
        enabled_2fa boolean,
        is_deleted boolean default false not null,
        locked boolean,
        login_attempted_number integer not null,
        selected_for_bid boolean,
        blocked_until timestamp(6),
        creation_date timestamp(6),
        expiry_date timestamp(6),
        id bigserial not null,
        last_update_date timestamp(6),
        role_id bigint,
        token_creation_time timestamp(6),
        user_id bigint unique,
        address varchar(255),
        email varchar(255) unique,
        first_name varchar(255),
        last_name varchar(255),
        password varchar(255),
        phone varchar(255),
        photo varchar(255),
        reset_password_token varchar(255),
        secret varchar(255),
        sex varchar(255),
        token varchar(255),
        uuid varchar(255) unique,
        primary key (id)
    );

    create table vote (
        competition_id bigint,
        competitor_id bigint,
        id bigserial not null,
        user_id bigint,
        primary key (id)
    );

    create table wishlist (
        artwork_id bigint,
        creation_date timestamp(6),
        id bigserial not null,
        last_update_date timestamp(6),
        user_id bigint unique,
        primary key (id)
    );

    create index idx_artwork_artwork_category 
       on artwork (artwork_category, status);

    create index otp_user_id_index 
       on one_time_password (user_id);

    create index otp_otp_code_index 
       on one_time_password (otp_code);

    create index otp_notification_type_index 
       on one_time_password (notification_type);

    create index idx_order_user_id 
       on orders (user_id);

    create index idx_order_status 
       on orders (status);

    create index idx_standard_standard_type 
       on standards (standard_type);

    create index u_r_role_name_index 
       on user_role (role_name);

    create index u_r_issuer_user_id_index 
       on user_role (issuer_user_id);

    create index u_email_index 
       on users (email);

    create index u_phone_index 
       on users (phone);

    create index u_first_name_index 
       on users (first_name);

    create index u_uuid_index 
       on users (uuid);

    create index u_deleted_index 
       on users (is_deleted);

    create index u_verified_index 
       on users (account_verified);

    alter table if exists artwork 
       add constraint FKatqhkq9xp4r034wyurl5poaxk 
       foreign key (user_id) 
       references users;

    alter table if exists artwork_image_urls 
       add constraint FKgubug9831ii9amnfefwnt5k53 
       foreign key (artwork_id) 
       references artwork;

    alter table if exists cart 
       add constraint FKi8614glfm46cdoot7llf3fjb0 
       foreign key (artwork_id) 
       references artwork;

    alter table if exists cart 
       add constraint FKg5uhi8vpsuy0lgloxk2h4w5o6 
       foreign key (user_id) 
       references users;

    alter table if exists competitor 
       add constraint FKle1jmorbmkq2pr6obp6t66s3p 
       foreign key (artist_id) 
       references users;

    alter table if exists competitor 
       add constraint FKhkrppqiyg4xkmobjf7dj82u4n 
       foreign key (competition_id) 
       references competition;

    alter table if exists confirmation_token 
       add constraint FKah4p1rycwibwm6s9bsyeckq51 
       foreign key (user_id) 
       references users;

    alter table if exists event 
       add constraint FK31rxexkqqbeymnpw4d3bf9vsy 
       foreign key (user_id) 
       references users;

    alter table if exists notifications 
       add constraint FK9y21adhxn0ayjhfocscqox7bh 
       foreign key (user_id) 
       references users;

    alter table if exists order_address 
       add constraint FKru9uj5vrdjaarx7l5oa19fvt2 
       foreign key (order_id) 
       references orders;

    alter table if exists orders 
       add constraint FKj0ujtlenyjmxr6scxh1om6q00 
       foreign key (address_id) 
       references order_address;

    alter table if exists orders 
       add constraint FK5rst8nfupe0p3ut65audig27b 
       foreign key (payment_log_id) 
       references payment_log;

    alter table if exists orders 
       add constraint FK32ql8ubntj5uh44ph9659tiih 
       foreign key (user_id) 
       references users;

    alter table if exists payment_log 
       add constraint FK700txvvhytlg0isueo06d1dwq 
       foreign key (user_id) 
       references users;

    alter table if exists rating 
       add constraint FKf96dhbq2vctvg5q6o2mwa01u9 
       foreign key (artwork_id) 
       references artwork;

    alter table if exists rating 
       add constraint FKf68lgbsbxl310n0jifwpfqgfh 
       foreign key (user_id) 
       references users;

    alter table if exists user_token 
       add constraint FKfadr8tg4d19axmfe9fltvrmqt 
       foreign key (user_id) 
       references users;

    alter table if exists users 
       add constraint FKqg2qcoumch2m4ok1cf8q675yu 
       foreign key (user_id) 
       references users;

    alter table if exists users 
       add constraint FKqjp6iwe2anthe5yx88fl0coan 
       foreign key (role_id) 
       references user_role;

    alter table if exists vote 
       add constraint FKf90fiu934s56wx4ex22ecgs2y 
       foreign key (competition_id) 
       references competition;

    alter table if exists vote 
       add constraint FKo6iyi1x0uqh8pnhgqhnhw4bgx 
       foreign key (competitor_id) 
       references competitor;

    alter table if exists vote 
       add constraint FKkmvvqilx49120p47nr9t56omf 
       foreign key (user_id) 
       references users;

    alter table if exists wishlist 
       add constraint FK60jl92oscnf449jf1uujtorhl 
       foreign key (artwork_id) 
       references artwork;

    alter table if exists wishlist 
       add constraint FKtrd6335blsefl2gxpb8lr0gr7 
       foreign key (user_id) 
       references users;

    create sequence artwork_seq start with 1 increment by 50;

    create sequence confirmation_token_sequence start with 1 increment by 1;

    create table account_lockout_rule (
        failure_count smallint unique,
        account_lockout_rule_id bigserial not null,
        block_time bigint,
        primary key (account_lockout_rule_id)
    );

    create table artwork (
        price numeric(38,2),
        quantity integer,
        creation_date timestamp(6),
        id bigint not null,
        last_update_date timestamp(6),
        user_id bigint not null,
        artwork_category varchar(255),
        artwork_description varchar(255),
        artwork_name varchar(255),
        size varchar(255),
        status varchar(255) check (status in ('ACCEPTED','REJECTED','PENDING','SOLD')),
        primary key (id)
    );

    create table artwork_image_urls (
        artwork_id bigint not null,
        image varchar(255)
    );

    create table cart (
        quantity integer,
        artwork_id bigint,
        creation_date timestamp(6),
        id bigserial not null,
        user_id bigint,
        primary key (id)
    );

    create table competition (
        competitor_number integer,
        is_voting_closed boolean,
        is_winner_announced boolean,
        end_time timestamp(6),
        expiry_date timestamp(6),
        id bigserial not null,
        competition_description varchar(255),
        title varchar(255),
        primary key (id)
    );

    create table competitor (
        vote integer,
        artist_id bigint,
        competition_id bigint,
        id bigserial not null,
        artwork_description varchar(255),
        category varchar(255),
        image varchar(255),
        primary key (id)
    );

    create table confirmation_token (
        confirmed_at timestamp(6),
        creation_date timestamp(6),
        expiry_date timestamp(6),
        id bigint not null,
        user_id bigint,
        token varchar(255) not null,
        primary key (id)
    );

    create table event (
        capacity integer,
        ticket_price float(53),
        creation_date timestamp(6),
        event_date timestamp(6),
        id bigserial not null,
        user_id bigint,
        event_description varchar(255),
        event_name varchar(255),
        location varchar(255),
        status varchar(255) check (status in ('ACCEPTED','PENDING','REJECTED')),
        image oid,
        primary key (id)
    );

    create table notifications (
        checked boolean,
        creation_date timestamp(6),
        id bigserial not null,
        last_update_date timestamp(6),
        user_id bigint,
        message varchar(255),
        target_email varchar(255),
        primary key (id)
    );

    create table one_time_password (
        notification_type smallint not null check (notification_type between 0 and 6),
        otp_creation_time timestamp(6) not null,
        otp_expiry_time timestamp(6) not null,
        otp_id bigserial not null,
        user_id bigint not null,
        otp_code varchar(255) not null,
        primary key (otp_id)
    );

    create table order_address (
        id bigserial not null,
        order_id bigint unique,
        city varchar(255),
        country varchar(255),
        postal_code varchar(255),
        state varchar(255),
        street varchar(255),
        primary key (id)
    );

    create table orders (
        total_amount numeric(12,2),
        address_id bigint unique,
        id bigserial not null,
        last_update_date timestamp(6),
        order_date timestamp(6),
        payment_log_id bigint unique,
        user_id bigint not null,
        email varchar(255),
        first_name varchar(255),
        last_name varchar(255),
        phone varchar(255),
        secret_code varchar(255),
        status varchar(255) not null check (status in ('PENDING','CONFIRMED','PROCESSING','SHIPPED','DELIVERED','CANCELLED')),
        primary key (id)
    );

    create table payment_log (
        amount numeric(38,2),
        creation_date timestamp(6),
        id bigserial not null,
        user_id bigint,
        status varchar(255) check (status in ('VERIFIED','INTIALIZED')),
        token varchar(255),
        primary key (id)
    );

    create table rating (
        rating_value float(53),
        artwork_id bigint,
        creation_date timestamp(6),
        id bigserial not null,
        last_update_date timestamp(6),
        user_id bigint,
        primary key (id)
    );

    create table report (
        is_checked boolean,
        creation_date timestamp(6),
        id bigserial not null,
        report_detail varchar(255),
        report_name varchar(255) not null,
        reporter_email varchar(255),
        reporter_name varchar(255),
        primary key (id)
    );

    create table standards (
        creation_date timestamp(6),
        id bigserial not null,
        last_update_date timestamp(6),
        standard_description varchar(255),
        standard_type varchar(255) check (standard_type in ('CUSTOMER','ARTIST','ARTWORK','ORGANIZATION')),
        primary key (id)
    );

    create table user_role (
        is_admin boolean not null,
        creation_date timestamp(6),
        issuer_user_id bigint,
        last_update_date timestamp(6),
        role_id bigserial not null,
        role_name varchar(255) not null,
        primary key (role_id)
    );

    create table user_token (
        is_expired boolean not null,
        is_revoked boolean not null,
        expires_at timestamp(6),
        token_id bigserial not null,
        user_id bigint,
        token varchar(255) not null,
        token_type varchar(255) not null check (token_type in ('BEARER','OAUTH','OAUTH2','JWT','REFRESH')),
        primary key (token_id)
    );

    create table users (
        account_verified boolean not null,
        age integer,
        enabled boolean,
        enabled_2fa boolean,
        is_deleted boolean default false not null,
        locked boolean,
        login_attempted_number integer not null,
        selected_for_bid boolean,
        blocked_until timestamp(6),
        creation_date timestamp(6),
        expiry_date timestamp(6),
        id bigserial not null,
        last_update_date timestamp(6),
        role_id bigint,
        token_creation_time timestamp(6),
        user_id bigint unique,
        address varchar(255),
        email varchar(255) unique,
        first_name varchar(255),
        last_name varchar(255),
        password varchar(255),
        phone varchar(255),
        photo varchar(255),
        reset_password_token varchar(255),
        secret varchar(255),
        sex varchar(255),
        token varchar(255),
        uuid varchar(255) unique,
        primary key (id)
    );

    create table vote (
        competition_id bigint,
        competitor_id bigint,
        id bigserial not null,
        user_id bigint,
        primary key (id)
    );

    create table wishlist (
        artwork_id bigint,
        creation_date timestamp(6),
        id bigserial not null,
        last_update_date timestamp(6),
        user_id bigint unique,
        primary key (id)
    );

    create index idx_artwork_artwork_category 
       on artwork (artwork_category, status);

    create index otp_user_id_index 
       on one_time_password (user_id);

    create index otp_otp_code_index 
       on one_time_password (otp_code);

    create index otp_notification_type_index 
       on one_time_password (notification_type);

    create index idx_order_user_id 
       on orders (user_id);

    create index idx_order_status 
       on orders (status);

    create index idx_standard_standard_type 
       on standards (standard_type);

    create index u_r_role_name_index 
       on user_role (role_name);

    create index u_r_issuer_user_id_index 
       on user_role (issuer_user_id);

    create index u_email_index 
       on users (email);

    create index u_phone_index 
       on users (phone);

    create index u_first_name_index 
       on users (first_name);

    create index u_uuid_index 
       on users (uuid);

    create index u_deleted_index 
       on users (is_deleted);

    create index u_verified_index 
       on users (account_verified);

    alter table if exists artwork 
       add constraint FKatqhkq9xp4r034wyurl5poaxk 
       foreign key (user_id) 
       references users;

    alter table if exists artwork_image_urls 
       add constraint FKgubug9831ii9amnfefwnt5k53 
       foreign key (artwork_id) 
       references artwork;

    alter table if exists cart 
       add constraint FKi8614glfm46cdoot7llf3fjb0 
       foreign key (artwork_id) 
       references artwork;

    alter table if exists cart 
       add constraint FKg5uhi8vpsuy0lgloxk2h4w5o6 
       foreign key (user_id) 
       references users;

    alter table if exists competitor 
       add constraint FKle1jmorbmkq2pr6obp6t66s3p 
       foreign key (artist_id) 
       references users;

    alter table if exists competitor 
       add constraint FKhkrppqiyg4xkmobjf7dj82u4n 
       foreign key (competition_id) 
       references competition;

    alter table if exists confirmation_token 
       add constraint FKah4p1rycwibwm6s9bsyeckq51 
       foreign key (user_id) 
       references users;

    alter table if exists event 
       add constraint FK31rxexkqqbeymnpw4d3bf9vsy 
       foreign key (user_id) 
       references users;

    alter table if exists notifications 
       add constraint FK9y21adhxn0ayjhfocscqox7bh 
       foreign key (user_id) 
       references users;

    alter table if exists order_address 
       add constraint FKru9uj5vrdjaarx7l5oa19fvt2 
       foreign key (order_id) 
       references orders;

    alter table if exists orders 
       add constraint FKj0ujtlenyjmxr6scxh1om6q00 
       foreign key (address_id) 
       references order_address;

    alter table if exists orders 
       add constraint FK5rst8nfupe0p3ut65audig27b 
       foreign key (payment_log_id) 
       references payment_log;

    alter table if exists orders 
       add constraint FK32ql8ubntj5uh44ph9659tiih 
       foreign key (user_id) 
       references users;

    alter table if exists payment_log 
       add constraint FK700txvvhytlg0isueo06d1dwq 
       foreign key (user_id) 
       references users;

    alter table if exists rating 
       add constraint FKf96dhbq2vctvg5q6o2mwa01u9 
       foreign key (artwork_id) 
       references artwork;

    alter table if exists rating 
       add constraint FKf68lgbsbxl310n0jifwpfqgfh 
       foreign key (user_id) 
       references users;

    alter table if exists user_token 
       add constraint FKfadr8tg4d19axmfe9fltvrmqt 
       foreign key (user_id) 
       references users;

    alter table if exists users 
       add constraint FKqg2qcoumch2m4ok1cf8q675yu 
       foreign key (user_id) 
       references users;

    alter table if exists users 
       add constraint FKqjp6iwe2anthe5yx88fl0coan 
       foreign key (role_id) 
       references user_role;

    alter table if exists vote 
       add constraint FKf90fiu934s56wx4ex22ecgs2y 
       foreign key (competition_id) 
       references competition;

    alter table if exists vote 
       add constraint FKo6iyi1x0uqh8pnhgqhnhw4bgx 
       foreign key (competitor_id) 
       references competitor;

    alter table if exists vote 
       add constraint FKkmvvqilx49120p47nr9t56omf 
       foreign key (user_id) 
       references users;

    alter table if exists wishlist 
       add constraint FK60jl92oscnf449jf1uujtorhl 
       foreign key (artwork_id) 
       references artwork;

    alter table if exists wishlist 
       add constraint FKtrd6335blsefl2gxpb8lr0gr7 
       foreign key (user_id) 
       references users;
