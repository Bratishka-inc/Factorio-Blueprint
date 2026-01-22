
-- Index for search
CREATE INDEX IF NOT EXISTS idx_blueprints_author_id
    ON blueprints(author_id);

CREATE INDEX IF NOT EXISTS idx_guides_author_id
    ON guides(author_id);

ALTER TABLE blueprints
    ALTER COLUMN author_id DROP DEFAULT;

ALTER TABLE guides
    ALTER COLUMN author_id DROP DEFAULT;

ALTER TABLE blueprints
    ADD CONSTRAINT blueprints_author_not_null
        CHECK (author_id IS NOT NULL) NOT VALID;

ALTER TABLE guides
    ADD CONSTRAINT guides_author_not_null
        CHECK (author_id IS NOT NULL) NOT VALID;
