# ---------------------------------------------------------------------- #
# Target DBMS:           MySQL 8                                         #
# Project name:          ALCATRAZ                                        #
# Authors:               Valentina Severi, Gessica Raimondi              #
# Corso:                 Basi di Dati                                    #
# ---------------------------------------------------------------------- #

DROP DATABASE IF EXISTS alcatraz;

CREATE DATABASE IF NOT EXISTS alcatraz
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE alcatraz;

# ---------------------------------------------------------------------- #
# Tables                                                                 #
# ---------------------------------------------------------------------- #

# ---------------------------------------------------------------------- #
# Add table "VISITATORE"                                                 #
# ---------------------------------------------------------------------- #

CREATE TABLE `VISITATORE` (
    `AccountID`      INT           NOT NULL AUTO_INCREMENT,
    `E_Mail`         VARCHAR(100)  NOT NULL,
    `Password`       VARCHAR(100)  NOT NULL,
    `DataCreazione`  DATE          NOT NULL,
    `Nome`           VARCHAR(50)   NOT NULL,
    `Cognome`        VARCHAR(50)   NOT NULL,
    `CodiceFiscale`  CHAR(16)      NOT NULL,
    CONSTRAINT `PK_VISITATORE` PRIMARY KEY (`AccountID`),
    CONSTRAINT `UQ_VISITATORE_Email` UNIQUE (`E_Mail`),
    CONSTRAINT `UQ_VISITATORE_CF` UNIQUE (`CodiceFiscale`)
);

# ---------------------------------------------------------------------- #
# Add table "AMMINISTRATORE"                                             #
# ---------------------------------------------------------------------- #

CREATE TABLE `AMMINISTRATORE` (
    `AccountID`      INT           NOT NULL,
    `E_Mail`         VARCHAR(100)  NOT NULL,
    `Password`       VARCHAR(100)  NOT NULL,
    `DataCreazione`  DATE          NOT NULL,
    `Nome`           VARCHAR(50)   NOT NULL,
    `Cognome`        VARCHAR(50)   NOT NULL,
    `Matricola`      VARCHAR(20)   NOT NULL,
    CONSTRAINT `PK_AMMINISTRATORE` PRIMARY KEY (`AccountID`),
    CONSTRAINT `UQ_AMMINISTRATORE_Email` UNIQUE (`E_Mail`),
    CONSTRAINT `UQ_AMMINISTRATORE_Matricola` UNIQUE (`Matricola`)
);

# ---------------------------------------------------------------------- #
# Add table "PERSONALE"                                                  #
# ---------------------------------------------------------------------- #

CREATE TABLE `PERSONALE` (
    `Matricola`       VARCHAR(20)  NOT NULL,
    `Nome`            VARCHAR(50)  NOT NULL,
    `Cognome`         VARCHAR(50)  NOT NULL,
    `Ruolo`           ENUM('Guardia', 'Educatore', 'Amministrativo') NOT NULL,
    `DataAssunzione`  DATE         NOT NULL,
    `AccountID`       INT          NOT NULL,
    CONSTRAINT `PK_PERSONALE` PRIMARY KEY (`Matricola`)
);

# ---------------------------------------------------------------------- #
# Add table "SEZIONE"                                                    #
# ---------------------------------------------------------------------- #

CREATE TABLE `SEZIONE` (
    `NumeroSezione`  VARCHAR(20)   NOT NULL,
    `NomeSezione`    ENUM('Ordinaria', 'Alta Sicurezza', 'Isolamento', 'Unità Assistenza Clinica') NOT NULL,
    CONSTRAINT `PK_SEZIONE` PRIMARY KEY (`NumeroSezione`)
);

# ---------------------------------------------------------------------- #
# Add table "CELLA"                                                      #
# ---------------------------------------------------------------------- #

CREATE TABLE `CELLA` (
    `NumeroSezione`  VARCHAR(20)  NOT NULL,
    `NumeroCella`    VARCHAR(20)  NOT NULL,
    `CapienzaMax`    INT          NOT NULL,
    CONSTRAINT `PK_CELLA` PRIMARY KEY (`NumeroSezione`, `NumeroCella`)
);

# ---------------------------------------------------------------------- #
# Add table "DETENUTO"                                                   #
# ---------------------------------------------------------------------- #

CREATE TABLE `DETENUTO` (
    `MatricolaDetenuto`  VARCHAR(20)   NOT NULL,
    `Nome`               VARCHAR(50)   NOT NULL,
    `Cognome`            VARCHAR(50)   NOT NULL,
    `DataDiNascita`      DATE          NOT NULL,
    `CodiceFiscale`      CHAR(16)      NOT NULL,
    `DataIngresso`       DATE          NOT NULL,
    `DurataPena`         VARCHAR(50)   NOT NULL,
    `Reato`              VARCHAR(100)  NOT NULL,
    `StatoDellaPena`     ENUM('In corso', 'Scontata', 'In attesa di giudizio') NOT NULL,
    `AccountID`          INT           NOT NULL,
    `NumeroSezione`      VARCHAR(20)   NOT NULL,
    `NumeroCella`        VARCHAR(20)   NOT NULL,
    CONSTRAINT `PK_DETENUTO` PRIMARY KEY (`MatricolaDetenuto`),
    CONSTRAINT `UQ_DETENUTO_CF` UNIQUE (`CodiceFiscale`)
);

# ---------------------------------------------------------------------- #
# Add table "PRENOTAZIONE"                                               #
# ---------------------------------------------------------------------- #

CREATE TABLE `PRENOTAZIONE` (
    `IDPrenotazione`       INT           NOT NULL AUTO_INCREMENT,
    `NumeroAutorizzazione` INT           NOT NULL,
    `TipoAutorizzazione`   VARCHAR(50)   NOT NULL,
    `Data`                 DATE          NOT NULL,
    `Eff_AccountID`        INT           NOT NULL,
    `MatricolaDetenuto`    VARCHAR(20)   NOT NULL,
    `MotivoRifiuto`        VARCHAR(200)  NULL,
    `EsitoPrenotazione`    ENUM('In attesa', 'Confermata', 'Rifiutata') NOT NULL DEFAULT 'In attesa',
    CONSTRAINT `PK_PRENOTAZIONE` PRIMARY KEY (`IDPrenotazione`)
);

# ---------------------------------------------------------------------- #
# Add table "VISITA"                                                     #
# ---------------------------------------------------------------------- #

CREATE TABLE `VISITA` (
    `NumeroVisita`    INT   NOT NULL AUTO_INCREMENT,
    `IDPrenotazione`  INT   NOT NULL,
    `Data`            DATE  NOT NULL,
    `Orario`          TIME  NOT NULL,
    `AccountID`       INT   NOT NULL,
    `EsitoVisita`     ENUM('Effettuata', 'Annullata', 'Respinta') NOT NULL DEFAULT 'Effettuata',
    CONSTRAINT `PK_VISITA` PRIMARY KEY (`NumeroVisita`),
    CONSTRAINT `UQ_VISITA_Prenotazione` UNIQUE (`IDPrenotazione`)
);

# ---------------------------------------------------------------------- #
# Add table "CORSO_DI_REINSERIMENTO"                                     #
# ---------------------------------------------------------------------- #

CREATE TABLE `CORSO_DI_REINSERIMENTO` (
    `CodiceCorso`  INT           NOT NULL AUTO_INCREMENT,
    `Titolo`       VARCHAR(100)  NOT NULL,
    `Descrizione`  VARCHAR(500)  NOT NULL,
    `DataInizio`   DATE          NOT NULL,
    `DataFine`     DATE          NOT NULL,
    `Tipologia`    ENUM('Professionale', 'Scolastico', 'Ricreativo') NOT NULL,
    `AccountID`    INT           NOT NULL,
    `Matricola`    VARCHAR(20)   NOT NULL,
    CONSTRAINT `PK_CORSO` PRIMARY KEY (`CodiceCorso`)
);

# ---------------------------------------------------------------------- #
# Add table "PROVVEDIMENTO_DISCIPLINARE"                                 #
# ---------------------------------------------------------------------- #

CREATE TABLE `PROVVEDIMENTO_DISCIPLINARE` (
    `NumeroProv`         INT           NOT NULL AUTO_INCREMENT,
    `Tipo`               ENUM('Lieve', 'Medio', 'Grave') NOT NULL,
    `Motivazione`        VARCHAR(200)  NOT NULL,
    `DataEmissione`      DATE          NOT NULL,
    `MatricolaDetenuto`  VARCHAR(20)   NOT NULL,
    `Matricola`          VARCHAR(20)   NOT NULL,
    CONSTRAINT `PK_PROVVEDIMENTO` PRIMARY KEY (`NumeroProv`)
);

# ---------------------------------------------------------------------- #
# Add table "Iscrizione"  (relazione N:M Detenuto–Corso)                #
# ---------------------------------------------------------------------- #

CREATE TABLE `Iscrizione` (
    `MatricolaDetenuto`  VARCHAR(20)  NOT NULL,
    `CodiceCorso`        INT          NOT NULL,
    `Esito`              ENUM('Superato', 'Non superato', 'In corso', 'Sospeso') NULL,
    CONSTRAINT `PK_Iscrizione` PRIMARY KEY (`MatricolaDetenuto`, `CodiceCorso`)
);

# ---------------------------------------------------------------------- #
# Add table "Sorveglia"  (relazione N:M Personale–Sezione)              #
# ---------------------------------------------------------------------- #

CREATE TABLE `Sorveglia` (
    `Matricola`      VARCHAR(20)  NOT NULL,
    `NumeroSezione`  VARCHAR(20)  NOT NULL,
    CONSTRAINT `PK_Sorveglia` PRIMARY KEY (`Matricola`, `NumeroSezione`)
);

# ---------------------------------------------------------------------- #
# Indexes                                                                #
# ---------------------------------------------------------------------- #

CREATE INDEX `idx_detenuto_cella`         ON `DETENUTO` (`NumeroSezione`, `NumeroCella`);
CREATE INDEX `idx_prenotazione_detenuto`  ON `PRENOTAZIONE` (`MatricolaDetenuto`);
CREATE INDEX `idx_prenotazione_visitatore` ON `PRENOTAZIONE` (`Eff_AccountID`);
CREATE INDEX `idx_iscrizione_corso`       ON `Iscrizione` (`CodiceCorso`);
CREATE INDEX `idx_prov_detenuto`          ON `PROVVEDIMENTO_DISCIPLINARE` (`MatricolaDetenuto`);

# ---------------------------------------------------------------------- #
# Sample Data                                                            #
# ---------------------------------------------------------------------- #

# --- AMMINISTRATORI ---
INSERT INTO `AMMINISTRATORE` VALUES
(101, 'admin1@alcatraz.it', 'hashed_pw_1', '2020-01-15', 'Marco',    'Ferretti',  'ADM-001'),
(102, 'admin2@alcatraz.it', 'hashed_pw_2', '2020-01-15', 'Laura',    'Conti',     'ADM-002'),
(103, 'admin3@alcatraz.it', 'hashed_pw_3', '2021-03-10', 'Giorgio',  'Martini',   'ADM-003'),
(104, 'admin4@alcatraz.it', 'hashed_pw_4', '2021-06-01', 'Silvia',   'Greco',     'ADM-004'),
(105, 'admin5@alcatraz.it', 'hashed_pw_5', '2022-09-05', 'Roberto',  'Esposito',  'ADM-005');

# --- VISITATORI ---
INSERT INTO `VISITATORE` VALUES
(1,  'mario.rossi@email.it',      'hashed_pw_v1',  '2023-01-10', 'Mario',    'Rossi',    'RSSMRA80A01H501Z'),
(2,  'giulia.bianchi@email.it',   'hashed_pw_v2',  '2023-02-14', 'Giulia',   'Bianchi',  'BNCGLI85B41F205X'),
(3,  'luca.verdi@email.it',       'hashed_pw_v3',  '2023-03-20', 'Luca',     'Verdi',    'VRDLCU90C01L219Y'),
(4,  'anna.neri@email.it',        'hashed_pw_v4',  '2023-04-05', 'Anna',     'Neri',     'NRANNA78D41G224W'),
(5,  'Paolo.gialli@email.it',     'hashed_pw_v5',  '2023-05-12', 'Paolo',    'Gialli',   'GLLPLA82E01H703V'),
(6,  'sara.blu@email.it',         'hashed_pw_v6',  '2023-06-18', 'Sara',     'Blu',      'BLUSRA95F41M208U'),
(7,  'marco.viola@email.it',      'hashed_pw_v7',  '2023-07-22', 'Marco',    'Viola',    'VLOMRC88G01F839T'),
(8,  'elena.rosa@email.it',       'hashed_pw_v8',  '2023-08-30', 'Elena',    'Rosa',     'RSALND92H41D612S'),
(9,  'davide.arancio@email.it',   'hashed_pw_v9',  '2023-09-15', 'Davide',   'Arancio',  'RNCDVD87I01B354R'),
(10, 'chiara.marrone@email.it',   'hashed_pw_v10', '2023-10-01', 'Chiara',   'Marrone',  'MRNCHR99L41A662Q');

# --- PERSONALE ---
INSERT INTO `PERSONALE` VALUES
('G001', 'Antonio',   'Russo',    'Guardia',         '2018-03-01', 101),
('G002', 'Carmela',   'Esposito', 'Guardia',         '2018-03-01', 101),
('G003', 'Luigi',     'De Luca',  'Guardia',         '2019-06-15', 102),
('G004', 'Francesca', 'Romano',   'Guardia',         '2019-06-15', 102),
('G005', 'Giovanni',  'Ferrari',  'Guardia',         '2020-01-10', 103),
('G006', 'Teresa',    'Ricci',    'Guardia',         '2020-01-10', 103),
('E001', 'Andrea',    'Lombardi', 'Educatore',       '2017-09-01', 101),
('E002', 'Monica',    'Gallo',    'Educatore',       '2018-11-01', 102),
('E003', 'Stefano',   'Costa',    'Educatore',       '2019-04-01', 103),
('A001', 'Patrizia',  'Marino',   'Amministrativo',  '2016-05-01', 104),
('A002', 'Fabio',     'Bruno',    'Amministrativo',  '2017-07-01', 105);

# --- SEZIONI ---
INSERT INTO `SEZIONE` VALUES
('S01', 'Ordinaria'),
('S02', 'Alta Sicurezza'),
('S03', 'Isolamento'),
('S04', 'Unità Assistenza Clinica');

# --- CELLE ---
INSERT INTO `CELLA` VALUES
-- Sezione Ordinaria (S01)
('S01', 'C001', 4), ('S01', 'C002', 4), ('S01', 'C003', 4), ('S01', 'C004', 4), ('S01', 'C005', 2),
('S01', 'C006', 4), ('S01', 'C007', 4), ('S01', 'C008', 4), ('S01', 'C009', 4), ('S01', 'C010', 2),
-- Sezione Alta Sicurezza (S02)
('S02', 'A001', 1), ('S02', 'A002', 1), ('S02', 'A003', 1), ('S02', 'A004', 1), ('S02', 'A005', 1),
('S02', 'A006', 1), ('S02', 'A007', 1), ('S02', 'A008', 1), ('S02', 'A009', 1), ('S02', 'A010', 1),
-- Sezione Isolamento (S03)
('S03', 'I001', 1), ('S03', 'I002', 1), ('S03', 'I003', 1), ('S03', 'I004', 1), ('S03', 'I005', 1),
-- Sezione UAC (S04)
('S04', 'U001', 2), ('S04', 'U002', 2), ('S04', 'U003', 2), ('S04', 'U004', 2), ('S04', 'U005', 2);

# --- DETENUTI ---
INSERT INTO `DETENUTO` VALUES
('DET001', 'Roberto',   'Mancini',   '1975-04-12', 'MNCRBR75D12H501A', '2020-03-15', '8 anni',   'Rapina aggravata',                     'In corso', 101, 'S01', 'C001'),
('DET002', 'Giuseppe',  'Lombardo',  '1980-11-23', 'LMBGPP80S23F205B', '2019-07-01', '12 anni',  'Associazione mafiosa',                 'In corso', 102, 'S02', 'A001'),
('DET003', 'Salvatore', 'Rizzo',     '1968-06-05', 'RZZSVT68H05L219C', '2021-01-20', '5 anni',   'Furto aggravato',                      'In corso', 101, 'S01', 'C001'),
('DET004', 'Francesco', 'Colombo',   '1990-02-28', 'CLMFNC90B28G224D', '2022-05-10', '3 anni',   'Truffa informatica',                   'In corso', 103, 'S01', 'C002'),
('DET005', 'Angelo',    'Ferrara',   '1985-09-17', 'FRRNGL85P17H703E', '2020-11-30', '15 anni',  'Omicidio',                             'In corso', 102, 'S02', 'A002'),
('DET006', 'Vincenzo',  'Greco',     '1972-03-08', 'GRCVCN72C08M208F', '2023-02-14', '2 anni',   'Lesioni personali',                    'In corso', 104, 'S01', 'C002'),
('DET007', 'Pietro',    'Conti',     '1965-12-30', 'CNTPTR65T30F839G', '2018-08-22', '20 anni',  'Traffico droga',                       'In corso', 101, 'S03', 'I001'),
('DET008', 'Domenico',  'Esposito',  '1988-07-14', 'SPSDMC88L14D612H', '2021-09-05', '4 anni',   'Evasione fiscale',                     'In corso', 105, 'S04', 'U001'),
('DET009', 'Alfredo',   'Bruno',     '1978-01-22', 'BRNLFD78A22B354I', '2022-12-01', '6 anni',   'Estorsione',                           'In corso', 103, 'S01', 'C003'),
('DET010', 'Carmine',   'Romano',    '1995-05-09', 'RMNCMN95E09A662L', '2023-06-18', '18 mesi',  'Guida in stato di ebbrezza con danni', 'In corso', 104, 'S01', 'C003');

# --- SORVEGLIA (Guardia → Sezione) ---
INSERT INTO `Sorveglia` VALUES
('G001', 'S01'), ('G001', 'S03'),
('G002', 'S01'), ('G002', 'S02'),
('G003', 'S02'), ('G003', 'S04'),
('G004', 'S01'), ('G004', 'S04'),
('G005', 'S03'), ('G005', 'S01'),
('G006', 'S02'), ('G006', 'S04');

# --- CORSI DI REINSERIMENTO ---
INSERT INTO `CORSO_DI_REINSERIMENTO` VALUES
(1,  'Falegnameria di Base',        'Corso professionale per imparare a lavorare il legno.',           '2023-01-10', '2023-06-30', 'Professionale', 101, 'E001'),
(2,  'Licenza Media',               'Preparazione all\'esame di terza media.',                         '2023-02-01', '2023-11-30', 'Scolastico',    102, 'E002'),
(3,  'Teatro e Comunicazione',      'Laboratorio teatrale per sviluppare capacità comunicative.',       '2023-03-15', '2023-07-31', 'Ricreativo',    101, 'E003'),
(4,  'Informatica di Base',         'Introduzione all\'uso del computer e di Internet.',                '2023-04-01', '2023-09-30', 'Professionale', 103, 'E001'),
(5,  'Giardinaggio e Verde Urbano', 'Corso pratico di cura delle piante e gestione del verde.',         '2023-05-01', '2023-10-31', 'Professionale', 104, 'E002'),
(6,  'Corso di Cucina',             'Tecniche di cucina di base e norme igienico-sanitarie.',           '2023-06-01', '2023-12-20', 'Professionale', 105, 'E003'),
(7,  'Educazione Fisica',           'Attività sportiva e benessere fisico.',                            '2023-01-15', '2023-12-15', 'Ricreativo',    101, 'E001'),
(8,  'Licenza Superiore (IFP)',     'Percorso di istruzione e formazione professionale.',               '2023-09-01', '2024-06-30', 'Scolastico',    102, 'E002'),
(9,  'Pittura e Arti Visive',       'Laboratorio creativo di pittura e tecniche artistiche.',           '2023-07-01', '2023-11-30', 'Ricreativo',    103, 'E003'),
(10, 'Sartoria e Moda',             'Apprendimento delle tecniche base di cucito e confezione abiti.', '2023-08-01', '2024-02-28', 'Professionale', 104, 'E001');

# --- PRENOTAZIONI ---
INSERT INTO `PRENOTAZIONE` VALUES
(1,  1001, 'Familiare',   '2023-09-10', 1, 'DET001', NULL,                         'Confermata'),
(2,  1002, 'Conoscente',  '2023-09-12', 2, 'DET003', NULL,                         'Confermata'),
(3,  1003, 'Familiare',   '2023-09-15', 3, 'DET004', NULL,                         'Confermata'),
(4,  1004, 'Familiare',   '2023-09-18', 4, 'DET006', NULL,                         'Confermata'),
(5,  1005, 'Conoscente',  '2023-09-20', 5, 'DET009', NULL,                         'Confermata'),
(6,  1006, 'Familiare',   '2023-09-22', 6, 'DET001', NULL,                         'In attesa'),
(7,  1007, 'Familiare',   '2023-09-25', 1, 'DET002', 'Detenuto in Alta Sicurezza', 'Rifiutata'),
(8,  1008, 'Conoscente',  '2023-10-01', 7, 'DET007', 'Detenuto in Isolamento',     'Rifiutata'),
(9,  1009, 'Familiare',   '2023-10-05', 8, 'DET010', NULL,                         'Confermata'),
(10, 1010, 'Familiare',   '2023-10-10', 9, 'DET004', NULL,                         'In attesa');

# --- VISITE ---
INSERT INTO `VISITA` VALUES
(1, 1, '2023-09-17', '10:00:00', 101, 'Effettuata'),
(2, 2, '2023-09-19', '14:30:00', 102, 'Effettuata'),
(3, 3, '2023-09-22', '11:00:00', 103, 'Effettuata'),
(4, 4, '2023-09-25', '15:00:00', 104, 'Effettuata'),
(5, 5, '2023-09-27', '09:30:00', 105, 'Annullata'),
(6, 9, '2023-10-12', '10:30:00', 101, 'Effettuata');

# --- PROVVEDIMENTI DISCIPLINARI ---
INSERT INTO `PROVVEDIMENTO_DISCIPLINARE` VALUES
(1, 'Lieve',  'Rissa in cella',                    '2023-04-10', 'DET001', 'G001'),
(2, 'Grave',  'Aggressione a un agente',            '2023-05-22', 'DET002', 'G003'),
(3, 'Medio',  'Possesso di oggetti non consentiti', '2023-06-15', 'DET005', 'G002'),
(4, 'Lieve',  'Comportamento irrispettoso',         '2023-07-08', 'DET007', 'G001'),
(5, 'Grave',  'Tentativo di evasione',              '2023-08-19', 'DET003', 'G004');

# --- ISCRIZIONI AI CORSI ---
INSERT INTO `Iscrizione` VALUES
('DET001', 1,  'In corso'),
('DET001', 7,  'In corso'),
('DET003', 2,  'Superato'),
('DET003', 4,  'In corso'),
('DET004', 3,  'In corso'),
('DET004', 6,  'In corso'),
('DET006', 5,  'In corso'),
('DET007', 3,  'Sospeso'),
('DET008', 8,  'In corso'),
('DET009', 1,  'Non superato'),
('DET009', 9,  'In corso'),
('DET010', 6,  'In corso'),
('DET010', 7,  'In corso');

# ---------------------------------------------------------------------- #
# Foreign key constraints                                                #
# ---------------------------------------------------------------------- #

ALTER TABLE `PERSONALE` ADD CONSTRAINT `FK_PERSONALE_AMMINISTRATORE`
    FOREIGN KEY (`AccountID`) REFERENCES `AMMINISTRATORE` (`AccountID`)
    ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE `CELLA` ADD CONSTRAINT `FK_CELLA_SEZIONE`
    FOREIGN KEY (`NumeroSezione`) REFERENCES `SEZIONE` (`NumeroSezione`)
    ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE `DETENUTO` ADD CONSTRAINT `FK_DETENUTO_AMMINISTRATORE`
    FOREIGN KEY (`AccountID`) REFERENCES `AMMINISTRATORE` (`AccountID`)
    ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE `DETENUTO` ADD CONSTRAINT `FK_DETENUTO_CELLA`
    FOREIGN KEY (`NumeroSezione`, `NumeroCella`) REFERENCES `CELLA` (`NumeroSezione`, `NumeroCella`)
    ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE `PRENOTAZIONE` ADD CONSTRAINT `FK_PRENO_VISITATORE`
    FOREIGN KEY (`Eff_AccountID`) REFERENCES `VISITATORE` (`AccountID`)
    ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `PRENOTAZIONE` ADD CONSTRAINT `FK_PRENO_DETENUTO`
    FOREIGN KEY (`MatricolaDetenuto`) REFERENCES `DETENUTO` (`MatricolaDetenuto`)
    ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE `VISITA` ADD CONSTRAINT `FK_VISITA_PRENOTAZIONE`
    FOREIGN KEY (`IDPrenotazione`) REFERENCES `PRENOTAZIONE` (`IDPrenotazione`)
    ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `VISITA` ADD CONSTRAINT `FK_VISITA_AMMINISTRATORE`
    FOREIGN KEY (`AccountID`) REFERENCES `AMMINISTRATORE` (`AccountID`)
    ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE `CORSO_DI_REINSERIMENTO` ADD CONSTRAINT `FK_CORSO_AMMINISTRATORE`
    FOREIGN KEY (`AccountID`) REFERENCES `AMMINISTRATORE` (`AccountID`)
    ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE `CORSO_DI_REINSERIMENTO` ADD CONSTRAINT `FK_CORSO_PERSONALE`
    FOREIGN KEY (`Matricola`) REFERENCES `PERSONALE` (`Matricola`)
    ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE `PROVVEDIMENTO_DISCIPLINARE` ADD CONSTRAINT `FK_PROV_DETENUTO`
    FOREIGN KEY (`MatricolaDetenuto`) REFERENCES `DETENUTO` (`MatricolaDetenuto`)
    ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE `PROVVEDIMENTO_DISCIPLINARE` ADD CONSTRAINT `FK_PROV_PERSONALE`
    FOREIGN KEY (`Matricola`) REFERENCES `PERSONALE` (`Matricola`)
    ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE `Iscrizione` ADD CONSTRAINT `FK_ISCR_DETENUTO`
    FOREIGN KEY (`MatricolaDetenuto`) REFERENCES `DETENUTO` (`MatricolaDetenuto`)
    ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `Iscrizione` ADD CONSTRAINT `FK_ISCR_CORSO`
    FOREIGN KEY (`CodiceCorso`) REFERENCES `CORSO_DI_REINSERIMENTO` (`CodiceCorso`)
    ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `Sorveglia` ADD CONSTRAINT `FK_SORV_PERSONALE`
    FOREIGN KEY (`Matricola`) REFERENCES `PERSONALE` (`Matricola`)
    ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `Sorveglia` ADD CONSTRAINT `FK_SORV_SEZIONE`
    FOREIGN KEY (`NumeroSezione`) REFERENCES `SEZIONE` (`NumeroSezione`)
    ON DELETE CASCADE ON UPDATE CASCADE;

# ---------------------------------------------------------------------- #
# Add View "Detenuti Attivi per Sezione"                                 #
# ---------------------------------------------------------------------- #

CREATE VIEW `Detenuti Attivi per Sezione`
AS
SELECT
    d.MatricolaDetenuto,
    d.Nome,
    d.Cognome,
    d.Reato,
    d.DataIngresso,
    d.DurataPena,
    s.NomeSezione,
    c.NumeroCella,
    c.CapienzaMax
FROM DETENUTO d
    INNER JOIN SEZIONE s ON d.NumeroSezione = s.NumeroSezione
    INNER JOIN CELLA   c ON d.NumeroSezione = c.NumeroSezione
                        AND d.NumeroCella   = c.NumeroCella
WHERE d.StatoDellaPena = 'In corso';

# ---------------------------------------------------------------------- #
# Add View "Prenotazioni In Attesa"                                      #
# ---------------------------------------------------------------------- #

CREATE VIEW `Prenotazioni In Attesa`
AS
SELECT
    p.IDPrenotazione,
    p.NumeroAutorizzazione,
    p.TipoAutorizzazione,
    p.Data AS DataRichiesta,
    v.Nome        AS NomeVisitatore,
    v.Cognome     AS CognomeVisitatore,
    v.E_Mail      AS EmailVisitatore,
    d.Nome        AS NomeDetenuto,
    d.Cognome     AS CognomeDetenuto,
    d.MatricolaDetenuto
FROM PRENOTAZIONE p
    INNER JOIN VISITATORE v ON p.Eff_AccountID      = v.AccountID
    INNER JOIN DETENUTO   d ON p.MatricolaDetenuto  = d.MatricolaDetenuto
WHERE p.EsitoPrenotazione = 'In attesa';

# ---------------------------------------------------------------------- #
# Add View "Corsi con Numero di Iscritti"                                #
# ---------------------------------------------------------------------- #

CREATE VIEW `Corsi con Numero di Iscritti`
AS
SELECT
    c.CodiceCorso,
    c.Titolo,
    c.Tipologia,
    c.DataInizio,
    c.DataFine,
    p.Nome        AS NomeEducatore,
    p.Cognome     AS CognomeEducatore,
    COUNT(i.MatricolaDetenuto) AS NumeroIscritti
FROM CORSO_DI_REINSERIMENTO c
    INNER JOIN PERSONALE  p ON c.Matricola    = p.Matricola
    LEFT  JOIN Iscrizione i ON c.CodiceCorso  = i.CodiceCorso
GROUP BY
    c.CodiceCorso, c.Titolo, c.Tipologia,
    c.DataInizio, c.DataFine,
    p.Nome, p.Cognome;

# ---------------------------------------------------------------------- #
# Add View "Visite Effettuate"                                           #
# ---------------------------------------------------------------------- #

CREATE VIEW `Visite Effettuate`
AS
SELECT
    vi.NumeroVisita,
    vi.Data        AS DataVisita,
    vi.Orario,
    vi.EsitoVisita,
    v.Nome         AS NomeVisitatore,
    v.Cognome      AS CognomeVisitatore,
    d.Nome         AS NomeDetenuto,
    d.Cognome      AS CognomeDetenuto,
    d.MatricolaDetenuto,
    s.NomeSezione
FROM VISITA vi
    INNER JOIN PRENOTAZIONE p ON vi.IDPrenotazione   = p.IDPrenotazione
    INNER JOIN VISITATORE   v ON p.Eff_AccountID     = v.AccountID
    INNER JOIN DETENUTO     d ON p.MatricolaDetenuto = d.MatricolaDetenuto
    INNER JOIN SEZIONE      s ON d.NumeroSezione      = s.NumeroSezione
WHERE vi.EsitoVisita = 'Effettuata';

# ---------------------------------------------------------------------- #
# Add View "Provvedimenti Gravi per Detenuto"                            #
# ---------------------------------------------------------------------- #

CREATE VIEW `Provvedimenti Gravi per Detenuto`
AS
SELECT
    d.MatricolaDetenuto,
    d.Nome         AS NomeDetenuto,
    d.Cognome      AS CognomeDetenuto,
    s.NomeSezione,
    COUNT(pr.NumeroProv)         AS TotaleProvvedimenti,
    SUM(pr.Tipo = 'Grave')       AS ProvvedimentiGravi,
    SUM(pr.Tipo = 'Medio')       AS ProvvedimentiMedi,
    SUM(pr.Tipo = 'Lieve')       AS ProvvedimentiLievi
FROM DETENUTO d
    INNER JOIN SEZIONE s ON d.NumeroSezione = s.NumeroSezione
    LEFT  JOIN PROVVEDIMENTO_DISCIPLINARE pr ON d.MatricolaDetenuto = pr.MatricolaDetenuto
GROUP BY
    d.MatricolaDetenuto, d.Nome, d.Cognome, s.NomeSezione
HAVING TotaleProvvedimenti > 0;

# ---------------------------------------------------------------------- #
# SQL Query Operations (Op1 – Op13)                                      #
# ---------------------------------------------------------------------- #

# Op1 - Registrare un nuovo utente (visitatore)
# INSERT INTO VISITATORE (AccountID, E_Mail, Password, DataCreazione, Nome, Cognome, CodiceFiscale)
# VALUES (?, ?, ?, ?, ?, ?, ?);

# Op2 - Richiedere prenotazione di una visita
# INSERT INTO PRENOTAZIONE (IDPrenotazione, NumeroAutorizzazione, TipoAutorizzazione, Data, Eff_AccountID, MatricolaDetenuto, MotivoRifiuto, EsitoPrenotazione)
# VALUES (?, ?, ?, ?, ?, ?, NULL, 'In attesa');

# Op3 - Visualizzare l'esito della prenotazione
# SELECT EsitoPrenotazione, MotivoRifiuto
# FROM PRENOTAZIONE
# WHERE IDPrenotazione = ?;

# Op4 - Visualizzare il regolamento per le visite e info detenuto
# SELECT MatricolaDetenuto, Nome, Cognome, DataDiNascita, CodiceFiscale, DataIngresso, DurataPena, Reato, StatoDellaPena
# FROM DETENUTO
# WHERE MatricolaDetenuto = ?;
#
# SELECT c.Titolo, c.Tipologia, c.DataInizio, c.DataFine, i.Esito AS EsitoCorso
# FROM Iscrizione i
# JOIN CORSO_DI_REINSERIMENTO c ON i.CodiceCorso = c.CodiceCorso
# WHERE i.MatricolaDetenuto = ?
# ORDER BY c.DataInizio DESC;
#
# SELECT NumeroProv, Tipo, Motivazione, DataEmissione
# FROM PROVVEDIMENTO_DISCIPLINARE
# WHERE MatricolaDetenuto = ?
# ORDER BY DataEmissione DESC;

# Op5 - Registrare un nuovo detenuto e assegnargli una cella
# SELECT c.NumeroCella, c.CapienzaMax, s.NomeSezione,
#        COUNT(d.MatricolaDetenuto) AS OccupantiAttuali,
#        c.CapienzaMax - COUNT(d.MatricolaDetenuto) AS PostiLiberi
# FROM CELLA c
# JOIN SEZIONE s ON c.NumeroSezione = s.NumeroSezione
# LEFT JOIN DETENUTO d ON d.NumeroCella = c.NumeroCella AND d.NumeroSezione = c.NumeroSezione
# WHERE c.NumeroCella = ? AND c.NumeroSezione = ?
# GROUP BY c.NumeroCella, c.CapienzaMax, s.NomeSezione;
#
# INSERT INTO DETENUTO(MatricolaDetenuto, Nome, Cognome, DataDiNascita, CodiceFiscale, DataIngresso, DurataPena, Reato, StatoDellaPena, NumeroCella, NumeroSezione, AccountID)
# VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);

# Op6 - Registrare un nuovo membro del personale
# INSERT INTO PERSONALE(Matricola, Nome, Cognome, Ruolo, DataAssunzione, AccountID)
# VALUES (?, ?, ?, ?, ?, ?);

# Op7 - Assegnare una guardia a una o più sezioni
# INSERT INTO Sorveglia (Matricola, NumeroSezione)
# SELECT ?, ?
# FROM PERSONALE
# WHERE Matricola = ? AND Ruolo = 'Guardia';

# Op8 - Creare nuovi corsi di reinserimento sociale
# INSERT INTO CORSO_DI_REINSERIMENTO (Titolo, Descrizione, DataInizio, DataFine, Tipologia, AccountID, Matricola)
# SELECT ?, ?, ?, ?, ?, ?, ?
# FROM PERSONALE
# WHERE Matricola = ? AND Ruolo = 'Educatore';

# Op9 - Iscrivere un detenuto a un corso
# INSERT INTO Iscrizione (MatricolaDetenuto, CodiceCorso, Esito)
# VALUES (?, ?, NULL);

# Op10 - Registrare una visita a un detenuto (conferma/rifiuto prenotazione)
# UPDATE PRENOTAZIONE
# SET EsitoPrenotazione = ?, MotivoRifiuto = ?
# WHERE IDPrenotazione = ?;
#
# INSERT INTO VISITA (NumeroVisita, IDPrenotazione, Data, Orario, AccountID)
# SELECT ?, ?, ?, ?, ?
# FROM PRENOTAZIONE
# WHERE IDPrenotazione = ? AND EsitoPrenotazione = 'Confermata';

# Op11 - Inserire un provvedimento disciplinare
# INSERT INTO PROVVEDIMENTO_DISCIPLINARE (Tipo, Motivazione, DataEmissione, MatricolaDetenuto, Matricola)
# SELECT ?, ?, ?, ?, ?
# FROM PERSONALE
# WHERE Matricola = ? AND Ruolo = 'Guardia';

# Op12 - Sospendere temporaneamente un detenuto dai corsi
# UPDATE Iscrizione
# SET Esito = 'Sospeso'
# WHERE MatricolaDetenuto = ?
#   AND EXISTS (
#       SELECT 1 FROM PROVVEDIMENTO_DISCIPLINARE
#       WHERE MatricolaDetenuto = ? AND Tipo = 'Grave'
#   );

# Op13 - Visualizzare statistiche
# SELECT
#     COUNT(DISTINCT i.MatricolaDetenuto) AS DetenutiIscritti,
#     COUNT(DISTINCT d.MatricolaDetenuto) AS TotaleDetenuti,
#     ROUND(100.0 * COUNT(DISTINCT i.MatricolaDetenuto) / COUNT(DISTINCT d.MatricolaDetenuto), 2) AS TassoPartecipazione_Pct
# FROM DETENUTO d
# LEFT JOIN Iscrizione i ON d.MatricolaDetenuto = i.MatricolaDetenuto;
#
# SELECT ROUND(COUNT(d.MatricolaDetenuto) * 1.0 / COUNT(DISTINCT s.NumeroSezione), 2) AS MediaDetenuti_Per_Sezione
# FROM SEZIONE s
# LEFT JOIN DETENUTO d ON s.NumeroSezione = d.NumeroSezione;
