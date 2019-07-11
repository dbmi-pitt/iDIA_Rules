/*
Beta-Blockers Includes:
Beta-Blockers

(Selective)
Acebutolol
Atenolol
Bisoprolol
Esmolol
Metoprolol
Nebivolol
Betaxolol

(Alpha-Blocking)
Carvedilol
Labetalol

(Non-Selective)
Carteolol
Levobunolol
Nadolol
Penbutolol
Pindolol
Propranolol
Sotalol
Timolol

Timolols Eye Drops
Timolols Eye Gels
Timolols Oral
*/

-- UPDATE BETA-BLOCKERS INGREDIENTS
BEGIN TRANSACTION; 
-- temp table
CREATE TABLE ohdsi.temp_concept_set_item (
  concept_set_item_id serial NOT NULL,
  concept_set_id int4 NOT NULL,
  concept_id int4 NOT NULL,
  is_excluded int4 NOT NULL,
  include_descendants int4 NOT NULL,
  include_mapped int4 NOT NULL
);

INSERT INTO ohdsi.temp_concept_set_item
(select i.concept_set_item_id, i.concept_set_id, i.concept_id, i.is_excluded, i.include_descendants, i.include_mapped 
from ohdsi.concept_set cs
inner join ohdsi.concept_set_item i
on i.concept_set_id = cs.concept_set_id
where cs.concept_set_name in ('Beta-Blockers Ingredients','Acebutolols Ingredients','Atenolols Ingredients','Bisoprolols Ingredients','Esmolols Ingredients','Metoprolols Ingredients','Nebivolols Ingredients','Betaxolols Ingredients','Carvedilols Ingredients','Labetalols Ingredients','Carteolols Ingredients','Levobunolols Ingredients','Nadolols Ingredients','Penbutolols Ingredients','Pindolols Ingredients','Propranolols Ingredients','Sotalols Ingredients','Timolols Ingredients'));

--delete currently existing parent concept set items so that duplicates don't accumulate
DELETE FROM ohdsi.concept_set_item WHERE concept_set_id = 7756;

INSERT INTO ohdsi.concept_set_item (concept_set_id, concept_id, is_excluded, include_descendants, include_mapped)
(select 7756 as concept_set_id, concept_id, is_excluded, include_descendants, include_mapped from ohdsi.temp_concept_set_item);

DROP TABLE ohdsi.temp_concept_set_item;

COMMIT TRANSACTION;

-- UPDATE BETA-BLOCKERS
BEGIN TRANSACTION;

CREATE TABLE ohdsi.temp_concept_set_item (
  concept_set_item_id serial NOT NULL,
  concept_set_id int4 NOT NULL,
  concept_id int4 NOT NULL,
  is_excluded int4 NOT NULL,
  include_descendants int4 NOT NULL,
  include_mapped int4 NOT NULL
);

INSERT INTO ohdsi.temp_concept_set_item
(select i.concept_set_item_id, i.concept_set_id, i.concept_id, i.is_excluded, i.include_descendants, i.include_mapped 
from ohdsi.concept_set cs
inner join ohdsi.concept_set_item i
on i.concept_set_id = cs.concept_set_id
where cs.concept_set_name in ('Beta-Blockers','Acebutolols','Atenolols','Bisoprolols','Esmolols','Metoprolols','Nebivolols','Betaxolols','Carvedilols','Labetalols','Carteolols','Levobunolols','Nadolols','Penbutolols','Pindolols','Propranolols','Sotalols','Timolols','Timolols Eye Drops', 'Timolols Eye Gels', 'Timolols Oral'));

DELETE FROM ohdsi.concept_set_item WHERE concept_set_id = 7773;

INSERT INTO ohdsi.concept_set_item (concept_set_id, concept_id, is_excluded, include_descendants, include_mapped)
(select 7773 as concept_set_id, concept_id, is_excluded, include_descendants, include_mapped from ohdsi.temp_concept_set_item);

DROP TABLE ohdsi.temp_concept_set_item;

COMMIT TRANSACTION;

/*
Clonidines Includes:
Clonidines
Clonidines Injectable
Clonidines Oral
Clonidines Transdermal
*/

BEGIN TRANSACTION;

CREATE TABLE ohdsi.temp_concept_set_item (
  concept_set_item_id serial NOT NULL,
  concept_set_id int4 NOT NULL,
  concept_id int4 NOT NULL,
  is_excluded int4 NOT NULL,
  include_descendants int4 NOT NULL,
  include_mapped int4 NOT NULL
);

INSERT INTO ohdsi.temp_concept_set_item
(select i.concept_set_item_id, i.concept_set_id, i.concept_id, i.is_excluded, i.include_descendants, i.include_mapped 
from ohdsi.concept_set cs
inner join ohdsi.concept_set_item i
on i.concept_set_id = cs.concept_set_id
where cs.concept_set_name in ('Clonidines','Clonidines Injectable','Clonidines Oral','Clonidines Transdermal'));

DELETE FROM ohdsi.concept_set_item WHERE concept_set_id = 11501;

INSERT INTO ohdsi.concept_set_item (concept_set_id, concept_id, is_excluded, include_descendants, include_mapped)
(select 11501 as concept_set_id, concept_id, is_excluded, include_descendants, include_mapped from ohdsi.temp_concept_set_item);

DROP TABLE ohdsi.temp_concept_set_item;

COMMIT TRANSACTION;