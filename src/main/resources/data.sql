-- Insert sample waste categories
INSERT INTO waste_category (category_name, category_description) VALUES
                                                                     ('Plastic', 'Recyclable plastic materials'),
                                                                     ('Glass', 'Recyclable glass materials'),
                                                                     ('Paper', 'Recyclable paper materials');

-- Insert sample disposal guidelines
INSERT INTO disposal_guideline (item_name, disposal_guideline, waste_category_id) VALUES
                                                                                      ('Plastic Bottle', 'Rinse and place in the recycling bin.', 1),
                                                                                      ('Glass Jar', 'Rinse and place in the recycling bin.', 2),
                                                                                      ('Newspaper', 'Place in the recycling bin.', 3);

-- Insert sample recycling tips
INSERT INTO recycle_tip (item_name, recycling_tip, waste_category_id) VALUES
                                                                          ('Plastic Bottle', 'Remove the cap before recycling.', 1),
                                                                          ('Glass Jar', 'Check if the glass is recyclable in your area.', 2),
                                                                          ('Newspaper', 'Do not recycle wet or soiled paper.', 3);