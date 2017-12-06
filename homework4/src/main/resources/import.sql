INSERT INTO Currency (id, name) VALUES (1, 'Swedish krona');
INSERT INTO Currency (id, name) VALUES (2, 'United States Dollar');
INSERT INTO Currency (id, name) VALUES (3, 'Euro');
INSERT INTO Currency (id, name) VALUES (4, 'Japanese Yen');

-- SEK -> SEK
INSERT INTO Rate (from_id, to_id, rate) VALUES (1, 1, 1);
-- SEK -> USD
INSERT INTO Rate (from_id, to_id, rate) VALUES (1, 2, 0.12);
-- SEK -> EUR
INSERT INTO Rate (from_id, to_id, rate) VALUES (1, 3, 0.10);
-- SEK -> JPY
INSERT INTO Rate (from_id, to_id, rate) VALUES (1, 4, 13.32);

-- USD -> SEK
INSERT INTO Rate (from_id, to_id, rate) VALUES (2, 1, 8.43);
-- USD -> USD
INSERT INTO Rate (from_id, to_id, rate) VALUES (2, 2, 1);
-- USD -> EUR
INSERT INTO Rate (from_id, to_id, rate) VALUES (2, 3, 0.85);
-- USD -> JPY
INSERT INTO Rate (from_id, to_id, rate) VALUES (2, 4, 112.32);

-- EUR -> SEK
INSERT INTO Rate (from_id, to_id, rate) VALUES (3, 1, 9.93);
-- EUR -> USD
INSERT INTO Rate (from_id, to_id, rate) VALUES (3, 2, 1.18);
-- EUR -> EUR
INSERT INTO Rate (from_id, to_id, rate) VALUES (3, 3, 1);
-- EUR -> JPY
INSERT INTO Rate (from_id, to_id, rate) VALUES (3, 4, 132.33);

-- JPY -> SEK
INSERT INTO Rate (from_id, to_id, rate) VALUES (4, 1, 0.075);
-- JPY -> USD
INSERT INTO Rate (from_id, to_id, rate) VALUES (4, 2, 0.0089);
-- JPY -> EUR
INSERT INTO Rate (from_id, to_id, rate) VALUES (4, 3, 0.0076);
-- JPY -> JPY
INSERT INTO Rate (from_id, to_id, rate) VALUES (4, 4, 1);

