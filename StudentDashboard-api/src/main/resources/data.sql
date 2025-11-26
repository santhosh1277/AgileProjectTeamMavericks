-- Insert sample colleges
INSERT INTO college (id, name, location, rank) VALUES 
(1, 'Trinity College Dublin', 'Dublin, Ireland', 101),
(2, 'University College Dublin', 'Dublin, Ireland', 177),
(3, 'University College Cork', 'Cork, Ireland', 298),
(4, 'National University of Ireland Galway', 'Galway, Ireland', 258),
(5, 'Dublin City University', 'Dublin, Ireland', 439);

-- Insert sample courses for Trinity College Dublin
INSERT INTO course_entity (id, name, college_id) VALUES
(1, 'Computer Science', 1),
(2, 'Business Studies', 1),
(3, 'Law', 1),
(4, 'Medicine', 1);

-- Insert sample courses for University College Dublin
INSERT INTO course_entity (id, name, college_id) VALUES
(5, 'Engineering', 2),
(6, 'Arts & Humanities', 2),
(7, 'Architecture', 2);

-- Insert sample courses for University College Cork
INSERT INTO course_entity (id, name, college_id) VALUES
(8, 'Pharmacy', 3),
(9, 'Food Science', 3),
(10, 'Environmental Science', 3);

-- Insert sample courses for NUI Galway
INSERT INTO course_entity (id, name, college_id) VALUES
(11, 'Marine Science', 4),
(12, 'Biomedical Science', 4);

-- Insert sample courses for Dublin City University
INSERT INTO course_entity (id, name, college_id) VALUES
(13, 'Communications', 5),
(14, 'Data Analytics', 5);
