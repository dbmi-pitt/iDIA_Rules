set search_path to banner_etl;

--gender
select gender_source_value, count(distinct person_id), 
(count(distinct person_id)::decimal / (SELECT count(distinct person_id) FROM person)) * 100 as percentage
from person
group by gender_source_value;

--race
select race_source_value, count(distinct person_id), 
(count(distinct person_id)::decimal / (SELECT count(distinct person_id) FROM person)) * 100 as percentage
from person
group by race_source_value;

--ethnicity
select ethnicity_source_value, count(distinct person_id), 
(count(distinct person_id)::decimal / (SELECT count(distinct person_id) FROM person)) * 100 as percentage
from person
group by ethnicity_source_value;

--average age
select avg(('2016-01-01'::date - to_date(CONCAT(month_of_birth,' ',day_of_birth,' ',year_of_birth), 'm d YYYY')) / 365.0) as avgage
from person;

--visit length
-- TODO medians?
select avg(visit_end_datetime - visit_start_datetime) as avg_visit_length,
min(visit_end_datetime - visit_start_datetime),
max(visit_end_datetime - visit_start_datetime)
from visit_occurrence;

select avg(num_visits), stddev(num_visits), min(num_visits), max(num_visits)
from (
  select person_id, count(*) as num_visits
  from visit_occurrence
  group by person_id
) v;

--drugs per stay
--575482 drugs
--519258 were started during a visit
select avg(num_drugs_started), stddev(num_drugs_started), min(num_drugs_started), max(num_drugs_started)
from (
  select v.person_id, v.visit_occurrence_id, count(drug_exposure_id) as num_drugs_started 
  from drug_exposure d
  inner join visit_occurrence v
  on d.person_id = v.person_id
  and d.drug_exposure_start_datetime between v.visit_start_datetime and v.visit_end_datetime
  group by v.visit_occurrence_id, v.person_id
) ds;

-- TODO available measurement values. currently this table is not loaded due to a small number of foreign key issues
