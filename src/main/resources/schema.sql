-- Waste Category Table
CREATE TABLE waste_category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(255) NOT NULL,
    category_description TEXT NOT NULL
);

-- Disposal Guideline Table
CREATE TABLE disposal_guideline (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    item_name VARCHAR(255) NOT NULL,
    disposal_guideline TEXT NOT NULL,
    waste_category_id BIGINT,
    FOREIGN KEY (waste_category_id) REFERENCES waste_category(id) ON DELETE CASCADE
);

-- Recycle Tip Table
CREATE TABLE recycle_tip (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    item_name VARCHAR(255) NOT NULL,
    recycling_tip TEXT NOT NULL,
    waste_category_id BIGINT,
    FOREIGN KEY (waste_category_id) REFERENCES waste_category(id) ON DELETE CASCADE
);
