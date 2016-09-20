-- Patient used for Warfarin - NSAIDs W/ PPI or Mistoprosol
INSERT INTO person (person_id, gender_concept_id, year_of_birth, month_of_birth, day_of_birth, race_concept_id, ethnicity_concept_id, location_id) values (1495, 8507, 1935, 1, 1, 8527, 38003564, 321);
INSERT INTO visit_occurrence (visit_occurrence_id, person_id, visit_concept_id, visit_start_date, visit_end_date, visit_type_concept_id, provider_id, care_site_id) values (135517, 1495, 9201, '2008-02-13', '2008-04-27', 44818517, 23492, 11905);
INSERT INTO drug_era (drug_era_id, person_id, drug_concept_id, drug_era_start_date, drug_era_end_date) values (52715, 1495, 1310149, '2008-02-13', '2008-04-27');
INSERT INTO drug_era (drug_era_id, person_id, drug_concept_id, drug_era_start_date, drug_era_end_date) values (52716, 1495, 1115008, '2008-02-13', '2008-04-27');
INSERT INTO drug_era (drug_era_id, person_id, drug_concept_id, drug_era_start_date, drug_era_end_date) values (52717, 1495, 923645, '2008-02-13', '2008-04-27');
INSERT INTO drug_exposure (drug_exposure_id, person_id, drug_concept_id, drug_exposure_start_date, drug_type_concept_id) values (149408, 1495, 40163518, '2008-02-13', 38000175);
INSERT INTO drug_exposure (drug_exposure_id, person_id, drug_concept_id, drug_exposure_start_date, drug_type_concept_id) values (149409, 1495, 1115129, '2008-02-13', 38000175);
INSERT INTO drug_exposure (drug_exposure_id, person_id, drug_concept_id, drug_exposure_start_date, drug_type_concept_id) values (149410, 1495, 19023551, '2008-02-13', 38000175);

-- Patient used for Warfarin - NSAIDs W/ NO PPI OR Mistoprosol + W/ NO AGE BUT YES GI BLEED
INSERT INTO person (person_id, gender_concept_id, year_of_birth, month_of_birth, day_of_birth, race_concept_id, ethnicity_concept_id, location_id) values (1496, 8507, 1952, 12, 1, 8527, 38003564, 321);
INSERT INTO visit_occurrence (visit_occurrence_id, person_id, visit_concept_id, visit_start_date, visit_end_date, visit_type_concept_id, provider_id, care_site_id) values (135518, 1496, 9201, '2008-02-13', '2008-04-27', 44818517, 23492, 11905);
INSERT INTO drug_era (drug_era_id, person_id, drug_concept_id, drug_era_start_date, drug_era_end_date) values (52718, 1496, 1310149, '2008-02-13', '2008-04-27');
INSERT INTO drug_era (drug_era_id, person_id, drug_concept_id, drug_era_start_date, drug_era_end_date) values (52719, 1496, 1236607, '2008-02-13', '2008-04-27');
INSERT INTO condition_era (condition_era_id, person_id, condition_concept_id, condition_era_start_date, condition_era_end_date) values (121387, 1496, 4046500, '2008-02-13', '2008-04-27');
INSERT INTO drug_exposure (drug_exposure_id, person_id, drug_concept_id, drug_exposure_start_date, drug_type_concept_id) values (1494011, 1496, 40163546, '2008-02-13', 38000175);
INSERT INTO drug_exposure (drug_exposure_id, person_id, drug_concept_id, drug_exposure_start_date, drug_type_concept_id) values (1494012, 1496, 1236610, '2008-02-13', 38000175);

-- Patient used for Warfarin - NSAIDs W/ NO PPI OR Mistoprosol + W/ NO AGE OR GI BLEED + W/ ADDITIONAL DRUG RISK
INSERT INTO person (person_id, gender_concept_id, year_of_birth, month_of_birth, day_of_birth, race_concept_id, ethnicity_concept_id, location_id) values (1497, 8507, 1970, 12, 1, 8527, 38003564, 321);
INSERT INTO visit_occurrence (visit_occurrence_id, person_id, visit_concept_id, visit_start_date, visit_end_date, visit_type_concept_id, provider_id, care_site_id) values (135519, 1497, 9201, '2008-02-13', '2008-04-27', 44818517, 23492, 11905);
INSERT INTO drug_era (drug_era_id, person_id, drug_concept_id, drug_era_start_date, drug_era_end_date) values (52720, 1497, 1310149, '2008-02-13', '2008-04-27');
INSERT INTO drug_era (drug_era_id, person_id, drug_concept_id, drug_era_start_date, drug_era_end_date) values (52721, 1497, 1236607, '2008-02-13', '2008-04-27');
INSERT INTO drug_era (drug_era_id, person_id, drug_concept_id, drug_era_start_date, drug_era_end_date) values (52722, 1497, 970250, '2008-02-13', '2008-04-27');
INSERT INTO drug_exposure (drug_exposure_id, person_id, drug_concept_id, drug_exposure_start_date, drug_type_concept_id) values (1494013, 1497, 40163546, '2008-02-13', 38000175);
INSERT INTO drug_exposure (drug_exposure_id, person_id, drug_concept_id, drug_exposure_start_date, drug_type_concept_id) values (1494014, 1497, 1236610, '2008-02-13', 38000175);
INSERT INTO drug_exposure (drug_exposure_id, person_id, drug_concept_id, drug_exposure_start_date, drug_type_concept_id) values (1494015, 1497, 970282, '2008-02-13', 38000175);

-- Patient used for Warfarin - NSAIDs W/ NO PPI OR Mistoprosol + W/ AGE OR GI BLEED + W/ ADDITIONAL DRUG RISK
INSERT INTO person (person_id, gender_concept_id, year_of_birth, month_of_birth, day_of_birth, race_concept_id, ethnicity_concept_id, location_id) values (1498, 8507, 1940, 12, 1, 8527, 38003564, 321);
INSERT INTO visit_occurrence (visit_occurrence_id, person_id, visit_concept_id, visit_start_date, visit_end_date, visit_type_concept_id, provider_id, care_site_id) values (135520, 1498, 9201, '2008-02-13', '2008-04-27', 44818517, 23492, 11905);
INSERT INTO drug_era (drug_era_id, person_id, drug_concept_id, drug_era_start_date, drug_era_end_date) values (52723, 1498, 1310149, '2008-02-13', '2008-04-27');
INSERT INTO drug_era (drug_era_id, person_id, drug_concept_id, drug_era_start_date, drug_era_end_date) values (52724, 1498, 1136980, '2008-02-13', '2008-04-27');
INSERT INTO drug_era (drug_era_id, person_id, drug_concept_id, drug_era_start_date, drug_era_end_date) values (52725, 1498, 1551099, '2008-02-13', '2008-04-27');
INSERT INTO drug_exposure (drug_exposure_id, person_id, drug_concept_id, drug_exposure_start_date, drug_type_concept_id) values (1494016, 1498, 40163518, '2008-02-13', 38000175);
INSERT INTO drug_exposure (drug_exposure_id, person_id, drug_concept_id, drug_exposure_start_date, drug_type_concept_id) values (1494017, 1498, 19133853, '2008-02-13', 38000175);
INSERT INTO drug_exposure (drug_exposure_id, person_id, drug_concept_id, drug_exposure_start_date, drug_type_concept_id) values (1494018, 1498, 1551170, '2008-02-13', 38000175);

-- Patient used for Warfarin - NSAIDs W/ NO PPI OR Mistoprosol + W/ NO AGE OR GI BLEED + W/ NO ADDITIONAL DRUG RISK
INSERT INTO person (person_id, gender_concept_id, year_of_birth, month_of_birth, day_of_birth, race_concept_id, ethnicity_concept_id, location_id) values (1499, 8507, 1970, 12, 1, 8527, 38003564, 321);
INSERT INTO visit_occurrence (visit_occurrence_id, person_id, visit_concept_id, visit_start_date, visit_end_date, visit_type_concept_id, provider_id, care_site_id) values (135521, 1499, 9201, '2008-02-13', '2008-04-27', 44818517, 23492, 11905);
INSERT INTO drug_era (drug_era_id, person_id, drug_concept_id, drug_era_start_date, drug_era_end_date) values (52726, 1499, 1310149, '2008-02-13', '2008-04-27');
INSERT INTO drug_era (drug_era_id, person_id, drug_concept_id, drug_era_start_date, drug_era_end_date) values (52727, 1499, 1136980, '2008-02-13', '2008-04-27');
INSERT INTO drug_exposure (drug_exposure_id, person_id, drug_concept_id, drug_exposure_start_date, drug_type_concept_id) values (1494019, 1499, 40163518, '2008-02-13', 38000175);
INSERT INTO drug_exposure (drug_exposure_id, person_id, drug_concept_id, drug_exposure_start_date, drug_type_concept_id) values (1494020, 1499, 19133853, '2008-02-13', 38000175);
