insert into ohdsi.concept_set (concept_set_id, concept_set_name)
values (12109, 'Timolols Eye Gels'), 
(12110, 'Clonidines Oral'), 
(12111, 'Clonidines Injectable'), 
(12112, 'Clonidines Topical'),
(12113, 'Clonidines Transdermal');

--timolols eye drops
insert into ohdsi.concept_set_item (concept_set_id, concept_id, is_excluded, include_descendants, include_mapped)
values (9162,40164138,0,0,0),
(9162,945502,0,0,0),
(9162,1594472,0,0,0),
(9162,1594484,0,0,0),
(9162,1594486,0,0,0),
(9162,1594707,0,0,0),
(9162,1594711,0,0,0),
(9162,793236,0,0,0),
(9162,793238,0,0,0);
--timolols eye gels
insert into ohdsi.concept_set_item (concept_set_id, concept_id, is_excluded, include_descendants, include_mapped)
values (12109,19079774,0,0,0),
(12109,19079775,0,0,0);
--clonidines oral
insert into ohdsi.concept_set_item (concept_set_id, concept_id, is_excluded, include_descendants, include_mapped)

