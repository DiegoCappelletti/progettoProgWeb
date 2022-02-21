drop table exams;
drop table patients;
drop table medics;
drop table exam_types;

create table MEDICS (
        id INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
        first_name VARCHAR(32) NOT NULL,
        last_name VARCHAR(32) NOT NULL,
        email VARCHAR(64) NOT NULL UNIQUE,
        hash VARCHAR(64) NOT NULL,
        salt VARCHAR(64) NOT NULL,
        ssn VARCHAR(128) NOT NULL UNIQUE,
        province VARCHAR(64) NOT NULL,
        CONSTRAINT medic_pk PRIMARY KEY (id)
);

create table PATIENTS (
        id INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
        first_name VARCHAR(32) NOT NULL,
        last_name VARCHAR(32) NOT NULL,
        email VARCHAR(64) NOT NULL UNIQUE,
        hash VARCHAR(128) NOT NULL,
        salt VARCHAR(32) NOT NULL,
        ssn VARCHAR(128) NOT NULL UNIQUE,
        sex VARCHAR(8) CONSTRAINT type_ck CHECK (sex IN ('M', 'F')) NOT NULL,
        residence VARCHAR(64) NOT NULL,
        province VARCHAR(64) NOT NULL,
        birth_date DATE NOT NULL,
        birthplace VARCHAR(64) NOT NULL,
        medic_id INT,
        CONSTRAINT patient_pk PRIMARY KEY (id),
        CONSTRAINT medic_fk FOREIGN KEY (medic_id) REFERENCES medics (id)
);

create table EXAM_TYPES (
        id INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
        name VARCHAR(128) NOT NULL,
        cost FLOAT NOT NULL,
        CONSTRAINT exam_type_pk PRIMARY KEY (id)
);

create table EXAMS (
        id INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
        patient_id INT NOT NULL,
        medic_id INT NOT NULL,
        type INT NOT NULL,
        erog_date DATE NOT NULL,
        exec_date DATE,
        results VARCHAR(128),
        CONSTRAINT exam_pk PRIMARY KEY (id),
        CONSTRAINT exam_patient_fk FOREIGN KEY (patient_id) REFERENCES patients (id),
        CONSTRAINT exam_medic_fk FOREIGN KEY (medic_id) REFERENCES medics (id),
        CONSTRAINT exam_type_fk FOREIGN KEY (type) REFERENCES exam_types (id)
);


select * from medics;
select * from patients;
select * from exams;
select * from exam_types;

select name as esame, erog_date, exec_date, first_name, last_name, results
from exams
join medics
on exams.medic_id = medics.id
join exam_types
on type = exam_types.id
where patient_id = 1;

// MEDICS

INSERT INTO medics (first_name, last_name, email, hash, salt, ssn, province) VALUES
('Mario', 'Rossi', 'mario.rossi@serviziosanitario.it', '2f2ec241e0c38cf24dbd7edb7b4fb6ebd4e2212db66574f008c5c5428f7d553d', '609a64f5b521709153e55d2d05c106ca', 'RSSMRA83E13L378L', 'BZ'),
('Alessio', 'Gottardi', 'alessio.gottardi@serviziosanitario.it', 'acb5c92f0b06085952aff230b1aecc58fb84179692e386c01e9429ccdd9fafbe', '13d86ca2ea5a1e2f07feb8fec9a21718', 'GTTLSS96R24L378X', 'TN');

// PATIENTS

INSERT INTO patients (first_name, last_name, email, hash, salt, ssn, sex, residence, province, birth_date, birthplace, medic_id) VALUES
('Mario', 'Rossi', 'mario.rossi@gmail.it', '0b7ab575959c59170fcf41e523bfa6425cbc37ee1ae9954edf7a7b2a3b562430', '3ea5590ef030f315d6972ee06dd4085c', 'RSSMRA83E13L378L', 'M', 'Trento', 'TN', '1983-05-13', 'Trento', 2)

// EXAM_TYPES

INSERT INTO exam_types (name, cost) VALUES
('Tomografia computerizzata del rachide', 50),
('Tomografia computerizzata con contrasto', 60),
('Tomografia computerizzata dell’arto superiore', 50),
('Tomografia computerizzata dell’arto superiore senza e con contrasto', 70),
('Tomografia computerizzata dell’arto inferiore', 50),
('Tomografia computerizzata dell’arto inferiore senza e con contrasto', 70),
('Risonanza magnetica nucleare (RM) della colonna cervicale', 50),
('Risonanza magnetica nucleare (RM) della colonna senza e con contrasto', 70),
('Risonanza magnetica nucleare (RM) muscoloscheletrica', 50),
('Risonanza magnetica nucleare (RM) muscoloscheletrica senza e con contrasto', 70);

INSERT INTO exam_types (name, cost) VALUES
('Analisi del sangue: emocromo con formula e piastrine', 36),
('Analisi del sangue: colesterolo Tot/HDL/LDL', 36),
('Analisi del sangue: trigliceridi', 36),
('Analisi del sangue: estradiolo', 36),
('Analisi del sangue: progesterone', 36),
('Analisi del sangue: glicemia', 36),
('Analisi del sangue: insulinemia', 36),
('Analisi del sangue: cortisolo', 36),
('Analisi del sangue: trigliceridi', 36),
('Analisi del sangue: estrogeni Totali', 36),
('Analisi del sangue: progesterone', 36);

// EXAMS

INSERT INTO exams (patient_id, medic_id, type, erog_date, exec_date, results) VALUES
(1, 2, 6, '2019-4-18', '2019-9-1', 'Pos jg jhbvjhs jhsbvjhbjh jhbsvjshb vhb hjbsv.'),
(1, 2, 16, '2019-4-1', '2019-4-2', 'Deca jg jhbvjhs jhsbvjhbjh jhbsvjshb vhb hjbsv.'),
(1, 2, 1, '2019-5-7', '2019-5-12', 'Negatdad jg jhbvjhs jhsbvjhbjh jhbsvjshb vhb hjbsv.'),
(1, 2, 9, '2019-5-7', '2019-5-13', 'Adcasca jg jhbvjhs jhsbvjhbjh jhbsvjshb vhb hjbsv.'),
(1, 2, 18, '2019-7-14', '2019-7-20', 'Cdscsc jg jhbvjhs jhsbvjhbjh jhbsvjshb vhb hjbsv.'),
(1, 2, 7, '2019-8-24', '2019-9-2', 'Dsc jg jhbvjhs jhsbvjhbjh jhbsvjshb vhb hjbsv.');



































































drop table exams;
drop table patients;
drop table medics;
drop table users;
drop table exam_types;

create table USERS (
        user_id INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
        email VARCHAR(64) NOT NULL UNIQUE,
        hash VARCHAR(64) NOT NULL,
        salt VARCHAR(64) NOT NULL,
        user_role VARCHAR(8) CONSTRAINT role_ck CHECK (user_role IN ('ADMIN', 'PATIENT', 'MEDIC', 'SSP')) NOT NULL,
        CONSTRAINT user_pk PRIMARY KEY (user_id)
);

create table MEDICS (
        medic_id INT NOT NULL,
        first_name VARCHAR(32) NOT NULL,
        last_name VARCHAR(32) NOT NULL,
        ssn VARCHAR(128) NOT NULL UNIQUE,
        province CHAR(2) NOT NULL,
        CONSTRAINT medic_pk PRIMARY KEY (medic_id),
        CONSTRAINT medic_fk FOREIGN KEY (medic_id) REFERENCES USERS (user_id)
);

create table PATIENTS (
        patient_id INT NOT NULL,
        first_name VARCHAR(32) NOT NULL,
        last_name VARCHAR(32) NOT NULL,
        ssn VARCHAR(128) NOT NULL UNIQUE,
        sex CHAR(1) CONSTRAINT type_ck CHECK (sex IN ('M', 'F')) NOT NULL,
        residence VARCHAR(64) NOT NULL,
        province CHAR(2) NOT NULL,
        birth_date DATE NOT NULL,
        birthplace VARCHAR(64) NOT NULL,
        medic_id INT,
        CONSTRAINT patient_pk PRIMARY KEY (patient_id),
        CONSTRAINT patient_fk FOREIGN KEY (patient_id) REFERENCES USERS (user_id)
);

create table SSPS (
        ssp_id INT NOT NULL,
        province CHAR(2) NOT NULL UNIQUE,
        CONSTRAINT ssp_pk PRIMARY KEY (ssp_id),
        CONSTRAINT ssp_fk FOREIGN KEY (ssp_id) REFERENCES USERS (user_id)
);

create table EXAM_TYPES (
        exam_type_id INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
        name VARCHAR(128) NOT NULL,
        cost FLOAT NOT NULL,
        CONSTRAINT exam_type_pk PRIMARY KEY (exam_type_id)
);

create table EXAMS (
        exam_id INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
        patient_id INT NOT NULL,
        medic_id INT NOT NULL,
        type INT NOT NULL,
        erog_date TIMESTAMP NOT NULL,
        exec_date TIMESTAMP,
        results VARCHAR(128),
        CONSTRAINT exam_pk PRIMARY KEY (exam_id),
        CONSTRAINT exam_patient_fk FOREIGN KEY (patient_id) REFERENCES patients (patient_id),
        CONSTRAINT exam_medic_fk FOREIGN KEY (medic_id) REFERENCES medics (medic_id),
        CONSTRAINT exam_type_fk FOREIGN KEY (type) REFERENCES exam_types (exam_type_id)
);

select * from users;
select * from medics;
select * from patients;
select * from ssps;
select * from exams;
select * from exam_types;

select *
from medics
join users
on user_id = medic_id
where email = 'mario.rossi@serviziosanitario.it';

select patient_id, patients.first_name as p_first_name, patients.last_name as p_last_name, email, patients.ssn, sex, residence, patients.province, birth_date, birthplace, patients.medic_id, hash, salt, medics.first_name as m_first_name, medics.last_name as m_last_name
from patients
join users
on user_id = patient_id
join medics
on patients.medic_id = medics.medic_id
where patient_id = 3;

select *
from exams
join medics
on exams.medic_id = medics.medic_id
join exam_types
on type = exam_type_id;

select exam_types.name, erog_date, exec_date, cost, patients.first_name as p_first_name, patients.last_name as p_last_name, medics.first_name as m_first_name, medics.last_name as m_last_name
from exams
join medics
on exams.medic_id = medics.medic_id
join patients
on exams.patient_id = patients.patient_id
join exam_types
on type = exam_type_id
where exam_id = 1;


select exam_id, exam_types.name, erog_date, exec_date, cost, patients.first_name as p_first_name, patients.last_name as p_last_name, medics.first_name as m_first_name, medics.last_name as m_last_name
from exams
join medics
on exams.medic_id = medics.medic_id
join patients
on exams.patient_id = patients.patient_id
join exam_types
on type = exam_type_id
where exams.patient_id = 3;


//USERS

INSERT INTO users (email, hash, salt, user_role) VALUES
('mario.rossi@serviziosanitario.it', '2f2ec241e0c38cf24dbd7edb7b4fb6ebd4e2212db66574f008c5c5428f7d553d', '609a64f5b521709153e55d2d05c106ca', 'MEDIC'),
('alessio.gottardi@serviziosanitario.it', 'acb5c92f0b06085952aff230b1aecc58fb84179692e386c01e9429ccdd9fafbe', '13d86ca2ea5a1e2f07feb8fec9a21718', 'MEDIC');

INSERT INTO users (email, hash, salt, user_role) VALUES
('mario.rossi@gmail.it', '0b7ab575959c59170fcf41e523bfa6425cbc37ee1ae9954edf7a7b2a3b562430', '3ea5590ef030f315d6972ee06dd4085c', 'PATIENT');

INSERT INTO users (email, hash, salt, user_role) VALUES
('trento.provincia@serviziosanitario.it', '', '', 'SSP');
('bolzano.provincia@serviziosanitario.it', '', '', 'SSP');

// MEDICS

INSERT INTO medics (medic_id, first_name, last_name, ssn, province) VALUES
(1, 'Mario', 'Rossi', 'RSSMRA83E13L378L', 'BZ'),
(2, 'Alessio', 'Gottardi', 'GTTLSS96R24L378X', 'TN');

// PATIENTS

INSERT INTO patients (patient_id, first_name, last_name, ssn, sex, residence, province, birth_date, birthplace, medic_id) VALUES
(3, 'Mario', 'Rossi','RSSMRA83E13L378L', 'M', 'Trento', 'TN', '1983-05-13', 'Trento', 2)

//SSPS

INSERT INTO ssps (ssp_id, province) VALUES
(4, 'TN'),
(5, 'BZ');

// EXAM_TYPES

INSERT INTO exam_types (name, cost) VALUES
('Tomografia computerizzata del rachide', 50),
('Tomografia computerizzata con contrasto', 60),
('Tomografia computerizzata dell’arto superiore', 50),
('Tomografia computerizzata dell’arto superiore senza e con contrasto', 70),
('Tomografia computerizzata dell’arto inferiore', 50),
('Tomografia computerizzata dell’arto inferiore senza e con contrasto', 70),
('Risonanza magnetica nucleare (RM) della colonna cervicale', 50),
('Risonanza magnetica nucleare (RM) della colonna senza e con contrasto', 70),
('Risonanza magnetica nucleare (RM) muscoloscheletrica', 50),
('Risonanza magnetica nucleare (RM) muscoloscheletrica senza e con contrasto', 70);

INSERT INTO exam_types (name, cost) VALUES
('Analisi del sangue: emocromo con formula e piastrine', 36),
('Analisi del sangue: colesterolo Tot/HDL/LDL', 36),
('Analisi del sangue: trigliceridi', 36),
('Analisi del sangue: estradiolo', 36),
('Analisi del sangue: progesterone', 36),
('Analisi del sangue: glicemia', 36),
('Analisi del sangue: insulinemia', 36),
('Analisi del sangue: cortisolo', 36),
('Analisi del sangue: trigliceridi', 36),
('Analisi del sangue: estrogeni Totali', 36),
('Analisi del sangue: progesterone', 36);

// EXAMS

INSERT INTO exams (patient_id, medic_id, type, erog_date, exec_date, results) VALUES
(3, 2, 6,  '2019-04-18 10:32:11', '2019-09-01 15:21:09', 'Pos jg jhbvjhs jhsbvjhbjh jhbsvjshb vhb hjbsv.'),
(3, 2, 16, '2019-04-01 16:50:38', '2019-04-02 11:08:22', 'Deca jg jhbvjhs jhsbvjhbjh jhbsvjshb vhb hjbsv.'),
(3, 2, 1,  '2019-05-07 17:42:37', '2019-05-12 09:15:17', 'Negatdad jg jhbvjhs jhsbvjhbjh jhbsvjshb vhb hjbsv.'),
(3, 2, 9,  '2019-05-07 17:43:02', '2019-05-13 10:07:09', 'Adcasca jg jhbvjhs jhsbvjhbjh jhbsvjshb vhb hjbsv.'),
(3, 2, 18, '2019-07-14 18:19:12', '2019-07-20 09:19:28', 'Cdscsc jg jhbvjhs jhsbvjhbjh jhbsvjshb vhb hjbsv.'),
(3, 2, 7,  '2019-08-24 09:48:41', '2019-09-02 10:36:15', 'Dsc jg jhbvjhs jhsbvjhbjh jhbsvjshb vhb hjbsv.');















create table ROLES (
        email VARCHAR(64) NOT NULL UNIQUE,
        role CHAR(8) CONSTRAINT role_ck CHECK (role IN ('ADMIN', 'PATIENT', 'MEDIC')) NOT NULL,
        CONSTRAINT role_pk PRIMARY KEY (id),
        CONSTRAINT role_m_fk FOREIGN KEY (medicId) REFERENCES medics (id),
        CONSTRAINT exam_type_fk FOREIGN KEY (type) REFERENCES exam_types (id)
);

