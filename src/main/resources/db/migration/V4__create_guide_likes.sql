CREATE TABLE guide_likes (
                             id BIGSERIAL PRIMARY KEY,
                             guide_id BIGINT NOT NULL,
                             user_id BIGINT NOT NULL,

                             CONSTRAINT fk_guide_like_guide FOREIGN KEY (guide_id)
                                 REFERENCES guides(id) ON DELETE CASCADE,

                             CONSTRAINT fk_guide_like_user FOREIGN KEY (user_id)
                                 REFERENCES users(id) ON DELETE CASCADE,

                             CONSTRAINT uq_guide_user UNIQUE (guide_id, user_id)
);
