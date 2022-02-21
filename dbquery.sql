drop table ESAMI;
drop table RICETTE;
drop table VISITE;
drop table FOTO;
drop table PAZIENTI;
drop table MEDICI;
drop table SSP;
drop table FARMACIE;
drop table UTENTI;
drop table TIPI_ESAME;
drop table TIPI_VISITA;
drop table FARMACI;
drop table CITTA;
drop table PROVINCE;
drop table SPECIALIZZAZIONI;

-- PROVINCE

create table PROVINCE (
        provincia_id INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
        provincia CHAR(2) NOT NULL UNIQUE,
        CONSTRAINT provincia_pk PRIMARY KEY (provincia_id)
);

-- CITTA

create table CITTA (
        citta_id INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
        citta VARCHAR(64) NOT NULL UNIQUE,
	provincia_id INT NOT NULL,
        CONSTRAINT citta_pk PRIMARY KEY (citta_id),
	CONSTRAINT provincia_fk FOREIGN KEY (provincia_id) REFERENCES PROVINCE (provincia_id)
);

-- SPECIALIZZAZIONI

create table SPECIALIZZAZIONI (
        spec_id INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 0, INCREMENT BY 1),
        spec VARCHAR(128) NOT NULL,
        CONSTRAINT spec_pk PRIMARY KEY (spec_id)
);

-- UTENTI

create table UTENTI (
        utente_id INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
        email VARCHAR(64) NOT NULL UNIQUE,
        hash VARCHAR(64) NOT NULL,
        salt VARCHAR(64) NOT NULL,
        ruolo_utente VARCHAR(8) CONSTRAINT ruolo_ck CHECK (ruolo_utente IN ('ADMIN', 'PAZIENTE', 'MEDICO', 'SSP', 'FARMACIA')) NOT NULL,
        CONSTRAINT utente_pk PRIMARY KEY (utente_id)
);

-- MEDICI

create table MEDICI (
        medico_id INT NOT NULL,
        nome VARCHAR(32) NOT NULL,
        cognome VARCHAR(32) NOT NULL,
        codice_fiscale VARCHAR(128) NOT NULL UNIQUE,
        citta_id INT NOT NULL,
        provincia_id INT NOT NULL,
        spec_id INT,
	indirizzo VARCHAR(128),
        CONSTRAINT medico_pk PRIMARY KEY (medico_id),
        CONSTRAINT medico_fk FOREIGN KEY (medico_id) REFERENCES UTENTI (utente_id),
        CONSTRAINT medico_citta_fk FOREIGN KEY (citta_id) REFERENCES CITTA (citta_id),
        CONSTRAINT medico_provincia_fk FOREIGN KEY (provincia_id) REFERENCES PROVINCE (provincia_id),
        CONSTRAINT medico_spec_fk FOREIGN KEY (spec_id) REFERENCES SPECIALIZZAZIONI (spec_id)
);

-- PAZIENTI

create table PAZIENTI (
        paziente_id INT NOT NULL,
        nome VARCHAR(32) NOT NULL,
        cognome VARCHAR(32) NOT NULL,
        codice_fiscale VARCHAR(128) NOT NULL UNIQUE,
        sesso CHAR(1) CONSTRAINT sesso_ck CHECK (sesso IN ('M', 'F')) NOT NULL,
        indirizzo VARCHAR(64) NOT NULL,
        citta_id INT NOT NULL,
        provincia_id INT NOT NULL,
        data_nascita DATE NOT NULL,
        luogo_nascita VARCHAR(64) NOT NULL,
        medico_id INT,
		data_ultima_visita DATE,
		data_ultima_ricetta DATE,
        CONSTRAINT paziente_pk PRIMARY KEY (paziente_id),
        CONSTRAINT paziente_fk FOREIGN KEY (paziente_id) REFERENCES UTENTI (utente_id),
        CONSTRAINT paziente_citta_fk FOREIGN KEY (citta_id) REFERENCES CITTA (citta_id),
        CONSTRAINT paziente_provincia_fk FOREIGN KEY (provincia_id) REFERENCES PROVINCE (provincia_id),
        CONSTRAINT paziente_medico_fk FOREIGN KEY (medico_id) REFERENCES MEDICI (medico_id)
);

-- SSP

create table SSP (
        ssp_id INT NOT NULL,
        nome VARCHAR(64) NOT NULL,
        indirizzo VARCHAR(64) NOT NULL,
        provincia_id INT NOT NULL UNIQUE,
        CONSTRAINT ssp_pk PRIMARY KEY (ssp_id),
        CONSTRAINT ssp_fk FOREIGN KEY (ssp_id) REFERENCES UTENTI (utente_id),
        CONSTRAINT ssp_provincia_fk FOREIGN KEY (provincia_id) REFERENCES PROVINCE (provincia_id)
);

-- FARMACIE

create table FARMACIE (
        farmacia_id INT NOT NULL,
        nome VARCHAR(32) NOT NULL,
        citta_id INT NOT NULL,
        provincia_id INT NOT NULL,
        CONSTRAINT farmacia_pk PRIMARY KEY (farmacia_id),
        CONSTRAINT farmacia_fk FOREIGN KEY (farmacia_id) REFERENCES UTENTI (utente_id),
        CONSTRAINT farmacia_citta_fk FOREIGN KEY (citta_id) REFERENCES CITTA (citta_id),
        CONSTRAINT farmacia_provincia_fk FOREIGN KEY (provincia_id) REFERENCES PROVINCE (provincia_id)
);

-- TIPI DI ESAME

create table TIPI_ESAME (
        tipo_esame_id INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
        nome VARCHAR(128) NOT NULL,
        costo FLOAT NOT NULL,
        CONSTRAINT tipo_esame_pk PRIMARY KEY (tipo_esame_id)
);

-- ESAMI

create table ESAMI (
        esame_id INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
        paziente_id INT NOT NULL,
        medico_id INT NOT NULL,
        ssp_id INT,
        tipo INT NOT NULL,
        data_erogazione TIMESTAMP NOT NULL,
        data_esame TIMESTAMP,
        eseguito BOOLEAN NOT NULL,
        risultati VARCHAR(128),
        CONSTRAINT esame_pk PRIMARY KEY (esame_id),
        CONSTRAINT esame_paziente_fk FOREIGN KEY (paziente_id) REFERENCES PAZIENTI (paziente_id),
        CONSTRAINT esame_medico_fk FOREIGN KEY (medico_id) REFERENCES MEDICI (medico_id),
        CONSTRAINT esame_ssp_fk FOREIGN KEY (ssp_id) REFERENCES SSP (ssp_id),
        CONSTRAINT esame_tipo_fk FOREIGN KEY (tipo) REFERENCES TIPI_ESAME (tipo_esame_id)
);

-- TIPI DI VISITA

create table TIPI_VISITA (
        tipo_visita_id INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
        nome VARCHAR(128) NOT NULL,
        costo FLOAT NOT NULL,
        spec_id INT NOT NULL,
        CONSTRAINT tipo_visita_pk PRIMARY KEY (tipo_visita_id),
        CONSTRAINT tipo_visita_spec_fk FOREIGN KEY (spec_id) REFERENCES SPECIALIZZAZIONI (spec_id)
);

-- VISITE

create table VISITE (
        visita_id INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
        paziente_id INT NOT NULL,
        medico_base_id INT NOT NULL,
        medico_visita_id INT,
        tipo INT NOT NULL,
        data_erogazione TIMESTAMP NOT NULL,
        data_visita TIMESTAMP ,
        eseguita BOOLEAN NOT NULL,
        risultati VARCHAR(128),
        CONSTRAINT visita_pk PRIMARY KEY (visita_id),
        CONSTRAINT visita_paziente_pk FOREIGN KEY (paziente_id) REFERENCES PAZIENTI (paziente_id),
        CONSTRAINT visita_medico_base_pk FOREIGN KEY (medico_base_id) REFERENCES MEDICI (medico_id),
        CONSTRAINT visita_medico_visita_pk FOREIGN KEY (medico_visita_id) REFERENCES MEDICI (medico_id),
        CONSTRAINT visita_tipo_pk FOREIGN KEY (tipo) REFERENCES TIPI_VISITA (tipo_visita_id)
);

-- FARMACI

create table FARMACI (
        farmaco_id INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
        nome VARCHAR(128) NOT NULL,
        costo FLOAT NOT NULL,
        CONSTRAINT farmaco_pk PRIMARY KEY (farmaco_id)
);

-- RICETTE

create table RICETTE (
        ricetta_id INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
        paziente_id INT NOT NULL,
        medico_base_id INT NOT NULL,
        farmacia_id INT,
        farmaco_id INT NOT NULL,
        data_erogazione TIMESTAMP NOT NULL,
        data_acquisto TIMESTAMP,
        acquistato BOOLEAN NOT NULL,
        CONSTRAINT ricetta_pk PRIMARY KEY (ricetta_id),
        CONSTRAINT ricetta_paziente_fk FOREIGN KEY (paziente_id) REFERENCES PAZIENTI (paziente_id),
        CONSTRAINT ricetta_medico_base_fk FOREIGN KEY (medico_base_id) REFERENCES MEDICI (medico_id),
        CONSTRAINT ricetta_farmacia_fk FOREIGN KEY (farmacia_id) REFERENCES FARMACIE (farmacia_id),
        CONSTRAINT ricetta_farmaco_fk FOREIGN KEY (farmaco_id) REFERENCES FARMACI (farmaco_id)
);

-- FOTO

create table FOTO (
        foto_id INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
        paziente_id INT NOT NULL,
        path VARCHAR(128) NOT NULL UNIQUE,
        data DATE NOT NULL,
        CONSTRAINT foto_pk PRIMARY KEY (foto_id),
        CONSTRAINT foto_paziente_fk FOREIGN KEY (paziente_id) REFERENCES PAZIENTI (paziente_id)
);

-- INSERT PROVINCE

INSERT INTO PROVINCE (provincia) VALUES
('BZ'),
('BS'),
('TN'),
('VI'),
('VR');


-- INSERT CITTA

INSERT INTO CITTA (citta, provincia_id) VALUES
('Brescia',2),
('Brunico',1),
('Bolzano',1),
('Cavalese',3),
('Merano',1),
('Rovereto',3),
('Riva del Garda',3),
('Trento',3),
('Verona',5);

-- INSERT SPECIALIZZAZIONI

INSERT INTO SPECIALIZZAZIONI (spec) VALUES
('spec1'),
('spec2'),
('spec3'),
('spec4'),
('spec5');


-- INSERT UTENTI

-- INSERT UTENTI MEDICI
INSERT INTO UTENTI (email, hash, salt, ruolo_utente) VALUES
('mario.rossi@serviziosanitario.it', '2f2ec241e0c38cf24dbd7edb7b4fb6ebd4e2212db66574f008c5c5428f7d553d', '609a64f5b521709153e55d2d05c106ca', 'MEDICO'),
('luigi.bianchi@serviziosanitario.it', 'acb5c92f0b06085952aff230b1aecc58fb84179692e386c01e9429ccdd9fafbe', '13d86ca2ea5a1e2f07feb8fec9a21718', 'MEDICO'),
('chiara.brugnara@serviziosanitario.it', 'acb5c92f0b06085952aff230b1aecc58fb84179692e386c01e9429ccdd9fafbe', '13d86ca2ea5a1e2f07feb8fec9a21718', 'MEDICO');

-- INSERT UTENTI PAZIENTI
INSERT INTO UTENTI (email, hash, salt, ruolo_utente) VALUES
('mario.rossi@gmail.it', '0b7ab575959c59170fcf41e523bfa6425cbc37ee1ae9954edf7a7b2a3b562430', '3ea5590ef030f315d6972ee06dd4085c', 'PAZIENTE'),
('luigi.bianchi@gmail.it', '0b7ab575959c59170fcf41e523bfa6425cbc37ee1ae9954edf7a7b2a3b562430', '3ea5590ef030f315d6972ee06dd4085c', 'PAZIENTE'),
('chiara.brugnara@gmail.it', '0b7ab575959c59170fcf41e523bfa6425cbc37ee1ae9954edf7a7b2a3b562430', '3ea5590ef030f315d6972ee06dd4085c', 'PAZIENTE'),
('elena.piazza@gmail.it', '0b7ab575959c59170fcf41e523bfa6425cbc37ee1ae9954edf7a7b2a3b562430', '3ea5590ef030f315d6972ee06dd4085c', 'PAZIENTE'),
('giorgio.savoi@gmail.it', '0b7ab575959c59170fcf41e523bfa6425cbc37ee1ae9954edf7a7b2a3b562430', '3ea5590ef030f315d6972ee06dd4085c', 'PAZIENTE'),
('matteo.fabbri@gmail.it', '0b7ab575959c59170fcf41e523bfa6425cbc37ee1ae9954edf7a7b2a3b562430', '3ea5590ef030f315d6972ee06dd4085c', 'PAZIENTE'),
('silvia.romagnoli@gmail.it', '0b7ab575959c59170fcf41e523bfa6425cbc37ee1ae9954edf7a7b2a3b562430', '3ea5590ef030f315d6972ee06dd4085c', 'PAZIENTE');

-- INSERT UTENTI FARMACIE
INSERT INTO UTENTI (email, hash, salt, ruolo_utente) VALUES
('farmacia.trento@serviziosanitario.it', '0b7ab575959c59170fcf41e523bfa6425cbc37ee1ae9954edf7a7b2a3b562430', '3ea5590ef030f315d6972ee06dd4085c', 'FARMACIA'),
('farmacia.rovereto@serviziosanitario.it', '0b7ab575959c59170fcf41e523bfa6425cbc37ee1ae9954edf7a7b2a3b562430', '3ea5590ef030f315d6972ee06dd4085c', 'FARMACIA'),
('farmacia.merano@serviziosanitario.it', '0b7ab575959c59170fcf41e523bfa6425cbc37ee1ae9954edf7a7b2a3b562430', '3ea5590ef030f315d6972ee06dd4085c', 'FARMACIA');

-- INSERT UTENTI SSP
INSERT INTO UTENTI (email, hash, salt, ruolo_utente) VALUES
('trento.provincia@serviziosanitario.it', '2f2ec241e0c38cf24dbd7edb7b4fb6ebd4e2212db66574f008c5c5428f7d553d', '609a64f5b521709153e55d2d05c106ca', 'SSP'),
('bolzano.provincia@serviziosanitario.it', '2f2ec241e0c38cf24dbd7edb7b4fb6ebd4e2212db66574f008c5c5428f7d553d', '609a64f5b521709153e55d2d05c106ca', 'SSP');



-- INSERT MEDICI

INSERT INTO MEDICI (medico_id, nome, cognome, codice_fiscale, citta_id, provincia_id, spec_id, indirizzo) VALUES
(1, 'Mario',  'Rossi',    'RSSMR', 3, 1, 0, 'Via Brennero'),
(2, 'Luigi',  'Bianchi',  'LGUBN', 8, 3, 0, 'Via Rosmini'),
(3, 'Chiara', 'Brugnara', 'CHRRG', 8, 3, 0, 'Via Verdi');

-- INSERT PAZIENTI

INSERT INTO PAZIENTI (paziente_id, nome, cognome, codice_fiscale, sesso, indirizzo, citta_id, provincia_id, data_nascita, luogo_nascita, medico_id) VALUES
(4,  'Mario',   'Rossi',     'RSSMR', 'M', 'Via blabla 27',  3, 1, '1983-05-13', 'Trento',  2),
(5,  'Luigi',   'Bianchi',   'LGUBN', 'M', 'Via jgvs 11',    8, 3, '1972-01-27', 'Bolzano', 3),
(6,  'Chiara',  'Brugnara',  'CHRRG', 'F', 'Via jhbvsjv 21', 8, 3, '1972-01-27', 'Bolzano', 2),
(7,  'Elena',   'Piazza',    'ELNPZ', 'F', 'Via jhsc 6',     7, 3, '1972-01-27', 'Trento',  3),
(8,  'Giorgio', 'Savoi',     'GRGSV', 'M', 'Via ngcvsn 4',   2, 1, '1972-01-27', 'Bolzano', 1),
(9,  'Matteo',  'Fabbri',    'MTTFB', 'M', 'Via ajhja 8',    8, 3, '1972-01-27', 'Trento',  2),
(10, 'Silvia',  'Romagnoli', 'SLVRM', 'F', 'Via lakjkdv 17', 7, 3, '1972-01-27', 'Bolzano', 1);

-- INSERT FARMACIE

INSERT INTO FARMACIE (farmacia_id, nome, citta_id, provincia_id) VALUES
(11, 'Farmacia di comunale di Trento',   8, 3),
(12, 'Farmacia di comunale di Rovereto', 6, 3),
(13, 'Farmacia di comunale di Merano',   5, 1);

-- INSERT SSP

INSERT INTO SSP (ssp_id, provincia_id, nome, indirizzo) VALUES
(14, 3, 'Servizio Sanitario di Trento',  'Via Degasperi 137'),
(15, 1, 'Servizio Sanitario di Bolzano', 'Via Rosmini 58');

-- INSERT SPECIALIZZAZIONI
INSERT INTO specializzazioni (spec) VALUES ('Medico di Base');
INSERT INTO specializzazioni (spec) VALUES ('Allergologia');
INSERT INTO specializzazioni (spec) VALUES ('Anestesiologia');
INSERT INTO specializzazioni (spec) VALUES ('Angiologia');
INSERT INTO specializzazioni (spec) VALUES ('Audiologia');
INSERT INTO specializzazioni (spec) VALUES ('Cardiochirurgia');
INSERT INTO specializzazioni (spec) VALUES ('Cardiologia');
INSERT INTO specializzazioni (spec) VALUES ('Chirurgia');
INSERT INTO specializzazioni (spec) VALUES ('Odontoiatria');
INSERT INTO specializzazioni (spec) VALUES ('Dermatologia');
INSERT INTO specializzazioni (spec) VALUES ('Medicina Estetica');
INSERT INTO specializzazioni (spec) VALUES ('Medicina Fisica');
INSERT INTO specializzazioni (spec) VALUES ('Medicina Nucleare');
INSERT INTO specializzazioni (spec) VALUES ('Radiologia');
INSERT INTO specializzazioni (spec) VALUES ('Neurologia');
INSERT INTO specializzazioni (spec) VALUES ('Dietologia');
INSERT INTO specializzazioni (spec) VALUES ('Ematologia');
INSERT INTO specializzazioni (spec) VALUES ('Endocrinologia');
INSERT INTO specializzazioni (spec) VALUES ('Epatologia');
INSERT INTO specializzazioni (spec) VALUES ('Fisiatria');
INSERT INTO specializzazioni (spec) VALUES ('Gastroenterologia');
INSERT INTO specializzazioni (spec) VALUES ('Gerontologia');
INSERT INTO specializzazioni (spec) VALUES ('Ginecologia');
INSERT INTO specializzazioni (spec) VALUES ('Immunologia');
INSERT INTO specializzazioni (spec) VALUES ('Infettivologia');
INSERT INTO specializzazioni (spec) VALUES ('Nefrologia');
INSERT INTO specializzazioni (spec) VALUES ('Neurochirurgia');
INSERT INTO specializzazioni (spec) VALUES ('Neuropsichiatria');
INSERT INTO specializzazioni (spec) VALUES ('Oftamologia');
INSERT INTO specializzazioni (spec) VALUES ('Oncoematologia');
INSERT INTO specializzazioni (spec) VALUES ('Oncologia');
INSERT INTO specializzazioni (spec) VALUES ('Ortopedia');
INSERT INTO specializzazioni (spec) VALUES ('Ostetricia');
INSERT INTO specializzazioni (spec) VALUES ('Otorinolaringoiatria');
INSERT INTO specializzazioni (spec) VALUES ('Pediatria');
INSERT INTO specializzazioni (spec) VALUES ('Pneumologia');
INSERT INTO specializzazioni (spec) VALUES ('Proctologia');
INSERT INTO specializzazioni (spec) VALUES ('Psichiatria');
INSERT INTO specializzazioni (spec) VALUES ('Reumatologia');
INSERT INTO specializzazioni (spec) VALUES ('Senologia');
INSERT INTO specializzazioni (spec) VALUES ('Urologia');


-- INSERT TIPI VISITE
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita allergologica', 50, 1);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita allergologica di controllo', 50, 1);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita anestesiologica/algologica', 50, 2);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita anestesiologica/algologica di controllo', 50, 2);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita angiologica', 50, 3);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita angiologica di controllo', 50, 3);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita audiologica', 50, 4);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita foniatrica', 50, 4);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita cardiochirurgica', 50, 5);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita cardiochirurgica di controllo', 50, 5);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita cardiochirurgica pediatrica', 50, 5);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita cardiochirurgica pediatrica di controllo', 50, 5);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita cardiologica di controllo', 50, 6);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita cardiologica. Incluso: ECG', 50, 6);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita chirurgia di controllo', 50, 7);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita chirurgia generale', 50, 7);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita chirurgia maxillo facciale', 50, 7);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita chirurgia maxillo facciale di controllo', 50, 7);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita chirurgia pediatrica', 50, 7);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita chirurgia pediatrica di controllo', 50, 7);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita chirurgia plastica', 50, 7);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita chirurgia plastica di controllo', 50, 7);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita chirurgia toracica', 50, 7);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita chirurgia toracica di controllo', 50, 7);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita chirurgia vascolare', 50, 7);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita chirurgia vascolare di controllo', 50, 7);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita dentistica - odontostomatologica', 50, 8);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita dentistica - odontostomatologica di controllo', 50, 8);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita dentistica - odontostomatologica pediatrica', 50, 8);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita dermatologica', 50, 9);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita dermatologica di controllo', 50, 9);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita di medicina estetica', 50, 10);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita di medicina estetica di controllo', 50, 10);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita di medicina fisica e riabilitazione', 50, 11);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita di medicina fisica e riabilitazione di controllo', 50, 11);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita di medicina nucleare', 50, 12);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita di medicina nucleare di controllo', 50, 12);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita di radiologia interventistica', 50, 13);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita di radiologia interventistica di controllo', 50, 13);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita di radioterapia', 50, 13);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita di riabilitazione neurologica', 50, 14);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita di riabilitazione neurologica di controllo', 50, 14);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita dietologica', 50, 15);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita dietologica di controllo', 50, 15);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita ematologica', 50, 16);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita ematologica di controllo', 50, 16);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita endocrinologica e diabetologica', 50, 17);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita endocrinologica e diabetologica di controllo', 50, 17);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita endocrinologica pediatrica', 50, 17);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita epatologica', 50, 18);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita epatologica di controllo', 50, 18);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita fisiatrica', 50, 19);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita fisiatrica di controllo', 50, 19);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita gastroenterologica', 50, 20);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita gastroenterologica di controllo', 50, 20);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita geriatrica', 50, 21);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita geriatrica di controllo', 50, 21);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita ginecologica', 50, 22);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita ginecologica di controllo', 50, 22);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita immunologica', 50, 23);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita Immunologica di controllo', 50, 23);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita infettivologica o delle malattie tropicali', 50, 24);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita infettivologica o delle malattie tropicali di controllo', 50, 24);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita nefrologica', 50, 25);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita nefrologica di controllo', 50, 25);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita nefrologica pediatrica', 50, 25);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita nefrologica pediatrica di controllo', 50, 25);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita neurochirurgica', 50, 26);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita neurochirurgica di controllo', 50, 26);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita neurochirurgica pediatrica', 50, 26);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita neurologica', 50, 14);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita neurologica di controllo', 50, 14);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita neurologica pediatrica', 50, 14);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita neuropsichiatrica infantile', 50, 27);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita neuropsichiatrica infantile di controllo', 50, 27);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita oculistica', 50, 28);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita oculistica di controllo', 50, 28);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita oculistica pediatrica', 50, 28);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita oncoematologica', 50, 29);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita oncoematologica di controllo', 50, 29);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita oncoematologica pediatrica', 50, 29);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita oncoematologica pediatrica di controllo', 50, 29);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita oncologica', 50, 30);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita oncologica di controllo', 50, 30);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita ortopedica', 50, 31);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita ortopedica di controllo', 50, 31);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita ostetrica', 50, 32);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita ostetrica di controllo', 50, 32);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita otorinolaringoiatrica', 50, 33);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita otorinolaringoiatrica di controllo', 50, 33);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita pediatrica', 50, 34);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita pneumologica', 50, 35);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita pneumologica di controllo', 50, 35);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita proctologica', 50, 36);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita proctologica di controllo', 50, 36);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita psichiatrica', 50, 37);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita psichiatrica di controllo', 50, 37);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita psicodiagnostica', 50, 37);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita reumatologica', 50, 38);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita reumatologica di controllo', 50, 38);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita senologica', 50, 39);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita senologica di controllo', 50, 39);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita urologica pediatrica', 50, 40);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita urologica pediatrica di controllo', 50, 40);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita urologica/andrologica', 50, 40);
INSERT INTO tipi_visita (nome, costo, spec_id) VALUES ('Visita urologica/andrologica di controllo', 50, 40);
-- INSERT TIPI DI ESAME

INSERT INTO TIPI_ESAME (nome, costo) VALUES
('Tomografia computerizzata del rachide', 50),
('Tomografia computerizzata con contrasto', 60),
('Tomografia computerizzata dell�arto superiore', 50),
('Tomografia computerizzata dell�arto superiore senza e con contrasto', 70),
('Tomografia computerizzata dell�arto inferiore', 50),
('Tomografia computerizzata dell�arto inferiore senza e con contrasto', 70),
('Risonanza magnetica nucleare (RM) della colonna cervicale', 50),
('Risonanza magnetica nucleare (RM) della colonna senza e con contrasto', 70),
('Risonanza magnetica nucleare (RM) muscoloscheletrica', 50),
('Risonanza magnetica nucleare (RM) muscoloscheletrica senza e con contrasto', 70);

INSERT INTO TIPI_ESAME (nome, costo) VALUES
('Analisi del sangue: emocromo con formula e piastrine', 11),
('Analisi del sangue: colesterolo Tot/HDL/LDL', 11),
('Analisi del sangue: trigliceridi', 11),
('Analisi del sangue: estradiolo', 11),
('Analisi del sangue: progesterone', 11),
('Analisi del sangue: glicemia', 11),
('Analisi del sangue: insulinemia', 11),
('Analisi del sangue: cortisolo', 11),
('Analisi del sangue: trigliceridi', 11),
('Analisi del sangue: estrogeni Totali', 11),
('Analisi del sangue: progesterone', 11);

-- INSERT ESAMI

INSERT INTO ESAMI (paziente_id, medico_id, ssp_id, tipo, data_erogazione, data_esame, eseguito, risultati) VALUES
(4, 2, 14, 6,  '2019-04-18 10:32:11', '2019-09-01 15:21:09', true, 'Pos jg jhbvjhs jhsbvjhbjh jhbsvjshb vhb hjbsv.'),
(4, 2, 14, 16, '2019-04-01 16:50:38', '2019-04-02 11:08:22', true, 'Deca jg jhbvjhs jhsbvjhbjh jhbsvjshb vhb hjbsv.'),
(4, 2, 14, 1,  '2019-05-07 17:42:37', '2019-05-12 09:15:17', true, 'Negatdad jg jhbvjhs jhsbvjhbjh jhbsvjshb vhb hjbsv.'),
(4, 2, 14, 9,  '2019-05-07 17:43:02', '2019-05-13 10:07:09', true, 'Adcasca jg jhbvjhs jhsbvjhbjh jhbsvjshb vhb hjbsv.'),
(4, 2, 14, 18, '2019-07-14 18:19:12', '2019-07-20 09:19:28', true, 'Cdscsc jg jhbvjhs jhsbvjhbjh jhbsvjshb vhb hjbsv.'),
(4, 2, 14, 7,  '2019-08-24 09:48:41', '2019-09-02 10:36:15', true, 'Dsc jg jhbvjhs jhsbvjhbjh jhbsvjshb vhb hjbsv.'),
(4, 2, 14, 5,  '2019-09-24 11:52:11', NULL,                  false, NULL),
(4, 2, 14, 15, '2019-09-24 17:38:15', NULL,                  false, NULL),
(4, 2, 14, 2,  '2019-10-24 14:45:23', NULL,                  false, NULL),
(4, 2, 14, 17, '2019-10-24 15:21:35', NULL,                  false, NULL);

--INSERT VISITE
INSERT INTO VISITE (paziente_id, medico_base_id, medico_visita_id, tipo, data_erogazione, data_visita, eseguita, risultati) VALUES
(4, 2, 1,    13, '2019-10-30 11:38:19', '2019-10-30 11:38:19', true,  'Ricisnc ucdh njhacb hb hascvjh jhabc jhbacjhb habcj.'),
(4, 2, 2,    13, '2019-10-30 11:38:19', '2019-10-30 11:38:19', true,  'Oubacu hab hg Sjschb jhabc hb jhacjhb jhabcj jhcja jgvvhac.'),
(4, 2, NULL, 13, '2019-10-30 11:38:19', NULL,                  false, NULL),
(4, 2, NULL, 13, '2019-10-30 11:38:19', NULL,                  false, NULL);
t 