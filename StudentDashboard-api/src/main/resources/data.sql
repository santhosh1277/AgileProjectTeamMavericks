-- Insert sample colleges

INSERT INTO College (id, name, country, state_province, alpha_two_code, domains)
VALUES
(1, 'Trinity College Dublin', 'Ireland', 'Dublin', 'IE', 'tcd.ie'),
(2, 'University College Dublin', 'Ireland', 'Dublin', 'IE', 'ucd.ie'),
(3, 'University College Cork', 'Ireland', 'Cork', 'IE', 'ucc.ie'),
(4, 'National University of Ireland Galway', 'Ireland', 'Galway', 'IE', 'nuigalway.ie'),
(5, 'Dublin City University', 'Ireland', 'Dublin', 'IE', 'dcu.ie');


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

-- Insert Master's Degree courses for all colleges

-- Trinity College Dublin - Master's Degrees
INSERT INTO course_entity (id, name, college_id) VALUES
(15, 'MSc Applied Software Engineering', 1),
(16, 'MSc Software Engineering', 1),
(17, 'MSc Data Analytics', 1),
(18, 'MSc Software Design with Cloud Native Computing', 1),
(19, 'MSc Software Design with Cybersecurity', 1),
(20, 'MSc Software Design with Artificial Intelligence', 1),
(21, 'MSc Biopharmaceutical Technology', 1),
(22, 'MSc Pharmaceutical & Chemical Analysis', 1),
(23, 'MSc Digital Health', 1),
(24, 'MSc Digital Marketing', 1),
(25, 'MA Accounting', 1),
(26, 'Master of Business', 1),
(27, 'MEng Engineering Management', 1);

-- University College Dublin - Master's Degrees
INSERT INTO course_entity (id, name, college_id) VALUES
(28, 'MSc Applied Software Engineering', 2),
(29, 'MSc Software Engineering', 2),
(30, 'MSc Data Analytics', 2),
(31, 'MSc Software Design with Cloud Native Computing', 2),
(32, 'MSc Software Design with Cybersecurity', 2),
(33, 'MSc Software Design with Artificial Intelligence', 2),
(34, 'MSc Biopharmaceutical Technology', 2),
(35, 'MSc Pharmaceutical & Chemical Analysis', 2),
(36, 'MSc Digital Health', 2),
(37, 'MSc Digital Marketing', 2),
(38, 'MA Accounting', 2),
(39, 'Master of Business', 2),
(40, 'MEng Engineering Management', 2);

-- University College Cork - Master's Degrees
INSERT INTO course_entity (id, name, college_id) VALUES
(41, 'MSc Applied Software Engineering', 3),
(42, 'MSc Software Engineering', 3),
(43, 'MSc Data Analytics', 3),
(44, 'MSc Software Design with Cloud Native Computing', 3),
(45, 'MSc Software Design with Cybersecurity', 3),
(46, 'MSc Software Design with Artificial Intelligence', 3),
(47, 'MSc Biopharmaceutical Technology', 3),
(48, 'MSc Pharmaceutical & Chemical Analysis', 3),
(49, 'MSc Digital Health', 3),
(50, 'MSc Digital Marketing', 3),
(51, 'MA Accounting', 3),
(52, 'Master of Business', 3),
(53, 'MEng Engineering Management', 3);

-- National University of Ireland Galway - Master's Degrees
INSERT INTO course_entity (id, name, college_id) VALUES
(54, 'MSc Applied Software Engineering', 4),
(55, 'MSc Software Engineering', 4),
(56, 'MSc Data Analytics', 4),
(57, 'MSc Software Design with Cloud Native Computing', 4),
(58, 'MSc Software Design with Cybersecurity', 4),
(59, 'MSc Software Design with Artificial Intelligence', 4),
(60, 'MSc Biopharmaceutical Technology', 4),
(61, 'MSc Pharmaceutical & Chemical Analysis', 4),
(62, 'MSc Digital Health', 4),
(63, 'MSc Digital Marketing', 4),
(64, 'MA Accounting', 4),
(65, 'Master of Business', 4),
(66, 'MEng Engineering Management', 4);

-- Dublin City University - Master's Degrees
INSERT INTO course_entity (id, name, college_id) VALUES
(67, 'MSc Applied Software Engineering', 5),
(68, 'MSc Software Engineering', 5),
(69, 'MSc Data Analytics', 5),
(70, 'MSc Software Design with Cloud Native Computing', 5),
(71, 'MSc Software Design with Cybersecurity', 5),
(72, 'MSc Software Design with Artificial Intelligence', 5),
(73, 'MSc Biopharmaceutical Technology', 5),
(74, 'MSc Pharmaceutical & Chemical Analysis', 5),
(75, 'MSc Digital Health', 5),
(76, 'MSc Digital Marketing', 5),
(77, 'MA Accounting', 5),
(78, 'Master of Business', 5),
(79, 'MEng Engineering Management', 5);
