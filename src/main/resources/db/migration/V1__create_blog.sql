CREATE TABLE users
    (
        id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
        is_moderator TINYINT NOT NULL,
        reg_time DATETIME NOT NULL,
        name VARCHAR(255) NOT NULL,
        email VARCHAR(255) NOT NULL,
        password VARCHAR(255), NOT NULL,
        code VARCHAR(255),
        photo TEXT
    );

CREATE TABLE posts
    (
        id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
        is_active TINYINT NOT NULL,
        moderation_status ENUM default 'NEW' NOT NULL,
        moderation_id INT,
        user_id INT NOT NULL,
        time DATETIME NOT NULL,
        title VARCHAR(255) NOT NULL,
        text TEXT NOT NULL,
        FOREIGN KEY (moderation_id) REFERENCES users(id),
        FOREIGN KEY (user_id) REFERENCES users(id)
    );

CREATE TABLE post_voters
    (
        id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
        user_id INT NOT NULL,
        post_id INT NOT NULL,
        time DATETIME NOT NULL,
        value TINYINT NOT NULL,
        FOREIGN KEY (user_id) REFERENCES users(id),
        FOREIGN KEY (post_id) REFERENCES posts(id)
    );

CREATE TABLE tags
    (
        id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
        name VARCHAR(255) NOT NULL
    );

CREATE TABLE tag2post
    (
        id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
        post_id INT NOT NULL,
        tag_id INT NOT NULL,
        FOREIGN KEY (post_id) REFERENCES posts(id),
        FOREIGN KEY (tag_id) REFERENCES tags(id)
    );

CREATE TABLE post_comments
    (
        id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
        parent_id INT,
        post_id INT NOT NULL,
        user_id INT NOT NULL,
        time DATETIME NOT NULL,
        text TEXT NOT NULL
        FOREIGN KEY (parent_id) REFERENCES post_comments(id),
        FOREIGN KEY (post_id) REFERENCES posts(id),
        FOREIGN KEY (user_id) REFERENCES users(id)
    );

CREATE TABLE captcha_codes
    (
        id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
        time DATETIME NOT NULL,
        code TINYINT NOT NULL,
        secret_code TINYINT NOT NULL
    );

CREATE TABLE global_settings
    (
        id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
        code VARCHAR(255) NOT NULL,
        name VARCHAR(255) NOT NULL,
        value VARCHAR(255) NOT NULL
    );