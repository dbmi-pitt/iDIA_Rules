import csv

with open("data.csv", "rb") as infile, open("output.sql", "wb") as outfile:
   reader = csv.reader(infile)
   next(reader, None)
   for row in reader:
       if row[0] == "PER":
           out_string = ("INSERT INTO person (person_id, gender_concept_id, year_of_birth, month_of_birth, day_of_birth, race_concept_id, ethnicity_concept_id, location_id) values (" + row[2] + ", 8507, " + row[3] + ", " + row[11] +", " + row[12] + ", 8527, 38003564, 321);\n")
           outfile.write(out_string)
       elif row[0] == "VIS":
           out_string = ("INSERT INTO visit_occurrence (visit_occurrence_id, person_id, visit_concept_id, visit_start_date, visit_end_date, visit_type_concept_id, provider_id, care_site_id) values (" + row[1] + ", " + row[2] +  ", " + row[8] + ", '" + row[5] +  "', '"+ row[6]+ "', 44818517, 23492, 11905);\n")
           outfile.write(out_string)      
       elif row[0] == "D_ERA":
            out_string = ("INSERT INTO drug_era (drug_era_id, person_id, drug_concept_id, drug_era_start_date, drug_era_end_date) values (" + row[1] + ", " + row[2] + ", " + row[4] + ", '" + row[5] + "', '" + row[6] + "');\n")
            outfile.write(out_string)      
       elif row[0] == "C_ERA":
            out_string = ("INSERT INTO condition_era (condition_era_id, person_id, condition_concept_id, condition_era_start_date, condition_era_end_date) values (" + row[1] + ", " + row[2] + ", " + row[4] + ", '" + row[5] + "', '" + row[6] + "');\n")
            outfile.write(out_string) 
       elif row[0] == "D_EXP":
            out_string = ("INSERT INTO drug_exposure (drug_exposure_id, person_id, drug_concept_id, drug_exposure_start_date, drug_exposure_end_date, drug_type_concept_id, quantity, days_supply, sig, indication_concept_id, route_concept_id, drug_exposure_start_datetime, drug_exposure_end_datetime) values (" + row[1] + ", " + row[2] + ", " + row[4] + ", '" + row[5] + "', '" + row[6] + "', 38000175, " + row[9] + ", " + row[10] + ", '" + row[13] + "', " + row[14] + ", " + row[15] + ", '" + row[5] + " 00:00:00', '" + row[6] + " 00:00:00'" + ");\n")
            outfile.write(out_string)   
       elif row[0] == "MEA":
            out_string = ("INSERT INTO measurement (measurement_id, person_id, measurement_concept_id, measurement_date, measurement_type_concept_id, value_as_number, unit_concept_id) values (" + row[1] + ", " + row[2] + ", " + row[4] + ", '" + row[5] + "', 45754907, " + row[7] + ", " + row[8] + ");\n")
            outfile.write(out_string)   


       
