set search_path to banner_etl;

--big picture
select count(distinct person_id) from person;
select count(drug_exposure_id) from drug_exposure;
select count(visit_occurrence_id) from visit_occurrence;
select count(condition_occurrence_id) from condition_occurrence;
select count(distinct measurement_id) from measurement; -- TODO not loaded

-- PERSON
-- TODO limit to persons with visit in study period?
--gender
select gender_source_value, count(distinct person_id), 
(count(distinct person_id)::decimal / (SELECT count(distinct person_id) FROM person)) * 100 as percentage
from person
group by gender_source_value;
/*
|gender_source_value                               |count               |percentage                                                                                          |
|--------------------------------------------------|--------------------|----------------------------------------------------------------------------------------------------|
|Female                                            |14447               |54.25084491175366128400                                                                             |
|Male                                              |12182               |45.74539992489673300800                                                                             |
|Unknown                                           |1                   |0.003755163349605707848300                                                                          |
*/

--race
select race_source_value, count(distinct person_id), 
(count(distinct person_id)::decimal / (SELECT count(distinct person_id) FROM person)) * 100 as percentage
from person
group by race_source_value;
-- TODO there's a bunch of artifacts in the "race_source_value" output that I'm not sure what to do with

--ethnicity
select ethnicity_source_value, count(distinct person_id), 
(count(distinct person_id)::decimal / (SELECT count(distinct person_id) FROM person)) * 100 as percentage
from person
group by ethnicity_source_value;
/*
|ethnicity_source_value                            |count               |percentage                                                                                          |
|--------------------------------------------------|--------------------|----------------------------------------------------------------------------------------------------|
|Hispanic or Latino                                |11062               |41.53961697333834021800                                                                             |
|Not Hispanic or Latino                            |15041               |56.48141194141945174600                                                                             |
|Patient Refused                                   |60                  |0.22530980097634247100                                                                              |
|Unknown                                           |448                 |1.68231318062335711600                                                                              |
|                                                  |19                  |0.07134810364250844900                                                                              |
*/

--age distribution
-- NOTE there are people born during the study period. Used end of study period.
select avg(('2016-03-31'::date - to_date(CONCAT(month_of_birth,' ',day_of_birth,' ',year_of_birth), 'm d YYYY')) / 365.0) as avg_age,
stddev(('2016-03-31'::date - to_date(CONCAT(month_of_birth,' ',day_of_birth,' ',year_of_birth), 'm d YYYY')) / 365.0) as stddev_age,
min(('2016-03-31'::date - to_date(CONCAT(month_of_birth,' ',day_of_birth,' ',year_of_birth), 'm d YYYY')) / 365.0) as min_age,
max(('2016-03-31'::date - to_date(CONCAT(month_of_birth,' ',day_of_birth,' ',year_of_birth), 'm d YYYY')) / 365.0) as max_age
from person;
/*
|avg_age                                                                                             |stddev_age                                                                                          |min_age                                                                                             |max_age                                                                                             |
|----------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------|
|43.68203591582261224884                                                                             |23.6955862287271159606399756539932690522466                                                         |0.24657534246575342466                                                                              |105.3178082191780822                                                                                |
*/

-- VISITS, DRUGS, MEASUREMENTS
-- TODO limit to study period?
--visit length
-- TODO medians?
select avg(visit_end_datetime - visit_start_datetime) as avg_visit_length,
min(visit_end_datetime - visit_start_datetime),
max(visit_end_datetime - visit_start_datetime)
from visit_occurrence;
/*
|avg_visit_length            |min                         |max                         |
|----------------------------|----------------------------|----------------------------|
|2 days 22:34:57.908628      |00:00:00                    |628 days 09:12:00           |
*/

-- number of visits
select avg(num_visits), stddev(num_visits), min(num_visits), max(num_visits)
from (
  select person_id, count(*) as num_visits
  from visit_occurrence
  group by person_id
) v;
/*
|avg                                                                                                 |stddev                                                                                              |min                 |max                 |
|----------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------|--------------------|--------------------|
|1.7820931157978512                                                                                  |2.1596027492264669                                                                                  |1                   |67                  |
*/

--drugs per stay
--575482 drugs
--519258 were started during a visit
--
select avg(num_drugs_started), stddev(num_drugs_started), min(num_drugs_started), max(num_drugs_started)
from (
  select v.person_id, v.visit_occurrence_id, count(drug_exposure_id) as num_drugs_started 
  from drug_exposure d
  inner join visit_occurrence v
  on d.person_id = v.person_id
  and d.drug_exposure_start_datetime between v.visit_start_datetime and v.visit_end_datetime
  group by v.visit_occurrence_id, v.person_id
) ds;
/*
|avg                                                                                                 |stddev                                                                                              |min                 |max                 |
|----------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------|--------------------|--------------------|
|17.2918845116387492                                                                                 |28.9700427210295145                                                                                 |1                   |980                 |
*/

-- TODO available measurement values. currently this table is not loaded due to a small number of foreign key issues
