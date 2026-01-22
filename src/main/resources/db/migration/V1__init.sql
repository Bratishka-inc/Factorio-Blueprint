-- USERS
CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       username VARCHAR(255) NOT NULL UNIQUE,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL
);

-- ROLES
CREATE TABLE roles (
                       id BIGSERIAL PRIMARY KEY,
                       name VARCHAR(255) NOT NULL UNIQUE
);

-- USERS_ROLES (many-to-many)
CREATE TABLE users_roles (
                             user_id BIGINT NOT NULL,
                             role_id BIGINT NOT NULL,
                             PRIMARY KEY (user_id, role_id),
                             FOREIGN KEY (user_id) REFERENCES users(id),
                             FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- CATEGORY
CREATE TABLE category (
                          id BIGSERIAL PRIMARY KEY,
                          name VARCHAR(255) NOT NULL
);

-- BLUEPRINTS
CREATE TABLE blueprints (
                            id BIGSERIAL PRIMARY KEY,
                            name VARCHAR(255) NOT NULL,
                            description TEXT,
                            blueprint TEXT,
                            image_url VARCHAR(255),
                            created_at TIMESTAMP,
                            category_id BIGINT,
                            author_id BIGINT,
                            FOREIGN KEY (category_id) REFERENCES category(id),
                            FOREIGN KEY (author_id) REFERENCES users(id)
);

-- GUIDES
CREATE TABLE guides (
                        id BIGSERIAL PRIMARY KEY,
                        title VARCHAR(255) NOT NULL,
                        category VARCHAR(255),
                        description TEXT,
                        created_at TIMESTAMP,
                        updated_at TIMESTAMP,
                        author_id BIGINT,
                        FOREIGN KEY (author_id) REFERENCES users(id)
);
