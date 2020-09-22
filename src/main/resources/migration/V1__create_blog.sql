create table users
    (
        id INT NOT NULL AUTO_INCREMENT,
        is_moderator TINYINT NOT NULL,
        reg_time DATETIME NOT NULL,
        name VARCHAR(255) NOT NULL,
        email VARCHAR(255) NOT NULL,
        password VARCHAR(255), NOT NULL,
        code VARCHAR(255),
        photo TEXT,
        PRIMARY KEY(id)
    );

create posts
    (
        id INT NOT NULL AUTO_INCREMENT,
        is_active TINYINT NOT NULL,
        moderation_status ENUM default 'NEW' NOT NULL,
        moderation_id INT,
        user_id INT NOT NULL,
        time DATETIME NOT NULL,
        title VARCHAR(255) NOT NULL,
        text TEXT,
        PRIMARY KEY(id)
    );

create post_voters
    (
        id INT NOT NULL AUTO_INCREMENT,
        user_id INT NOT NULL,
        post_id INT NOT NULL,
        time DATETIME NOT NULL,
        value TINYINT NOT NULL,
        PRIMARY KEY(id)
    );

create table tags
    (
        id INT NOT NULL AUTO_INCREMENT,
        name VARCHAR(255) NOT NULL,
        PRIMARY KEY(id)
    );

create table tag2post
    (
        id INT NOT NULL AUTO_INCREMENT,
        post_id INT NOT NULL,
        tag_id INT NOT NULL,
        PRIMARY KEY(id)
    );

create table post_comments
    (
        id INT NOT NULL AUTO_INCREMENT,
        parent_id INT,
        post_id INT NOT NULL,
        user_id INT NOT NULL,
        time DATETIME NOT NULL,
        text TEXT NOT NULL,
        PRIMARY KEY(id)
    );

create table captcha_codes
    (
        id INT NOT NULL AUTO_INCREMENT,
        time DATETIME NOT NULL,
        code TINYINT NOT NULL,
        secret_code TINYINT NOT NULL,
        PRIMARY KEY(id)
    );

create table global_settings
    (
        id INT NOT NULL AUTO_INCREMENT,
        code VARCHAR(255) NOT NULL,
        name VARCHAR(255) NOT NULL,
        value VARCHAR(255) NOT NULL,
    );