INSERT INTO observation_period (observation_period_id, person_id, observation_period_start_date, observation_period_end_date, period_type_concept_id)
SELECT row_number() over(), person_id, visit_start_date, visit_end_date, 44814722
FROM visit_occurrence 
