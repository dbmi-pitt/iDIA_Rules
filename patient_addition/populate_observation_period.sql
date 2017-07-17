INSERT INTO observation_period (observation_period_id, person_id, observation_period_start_date, observation_period_end_date, period_type_concept_id)
SELECT row_number() over(), person_id, visit_start_date, visit_end_date, 44814722
FROM visit_occurrence 
INSERT INTO observation_period (observation_period_id, person_id, observation_period_start_date, observation_period_end_date, period_type_concept_id) values (47526, 131, '2008-02-13', '2008-02-13', 44814722)
INSERT INTO observation_period (observation_period_id, person_id, observation_period_start_date, observation_period_end_date, period_type_concept_id) values (47527, 131, '2008-03-12', '2008-03-12', 44814722)
