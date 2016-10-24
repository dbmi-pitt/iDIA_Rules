import csv
import time

with open("lab-loinc.csv", "rb") as infile, open("lab-loinc-cleaned.csv", "wb") as outfile:
   reader = csv.reader(infile)
   next(reader, None)
   writer = csv.writer(outfile)
   for row in reader:
       if row[2] != 'Null' and row[7] != 'Null':	 
	 writer.writerow(row) 
