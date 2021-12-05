CREATE TABLE patients (
                         id int primary key auto_increment,
                         first_name varchar(255),
                         second_name varchar(255),
                         diagnose varchar(255)
);

CREATE TABLE records (
                        id int primary key auto_increment,
                        patient_id int,
                        record_date date,
                        description varchar(255)
);

INSERT INTO patients (first_name, second_name, diagnose) VALUES ('Peter', 'Petrov', 'cold');
INSERT INTO patients (first_name, second_name, diagnose) VALUES ('Ivan', 'Ivanov', 'arrhythmia');
INSERT INTO patients (first_name, second_name, diagnose) VALUES ('Abstract', 'Guy', 'headache');

INSERT INTO records (patient_id, record_date, description) VALUES (1, '2021-01-01', 'Nothing special');
INSERT INTO records (patient_id, record_date, description) VALUES (2, '2021-11-20', 'Annual survey');
INSERT INTO records (patient_id, record_date, description) VALUES (3, '2021-11-21', 'Prescribed pills');