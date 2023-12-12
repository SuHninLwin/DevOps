# Team 4 report file for the Population Information Project

## USE CASE DIAGRAM
<img src="ProjectGroup4_UML.svg" alt="World.sql database" title="UML for World Database">

## CHARACTERISTIC INFORMATION

# Report 1 

Goal in Context: UML Report Part - Query by all countries of population in the world, continent and region organized by largest population to smallest. 

Scope: Organization 

Level: Primary  

Preconditions 

1) Database connects to world.sql that has world data. 

2) The user must be authorized person who includes in organization administrator 

Success End Condition: Report on all countries of population in the world, continent and region organized by largest population to smallest. 

Failed End Condition: Invalid data in database. 

Primary Actor: Organization administrator. 

Trigger: none 

MAIN SUCCESS SCENARIO 

1) Read population from country and city in continent and region from database. 

2) Data retrieved from country and city table. 

3) Data correctness.  

4) Produce a report with string format. 

5) Final report is written in table format in markdown file.  

EXTENSIONS: none. 

SUB-VARIATIONS: none. 

SCHEDULE: End of Code Review - January 6, 2023. 


# Report 2 

Goal in Context: UML Report Part - Query by top "n" populated by countries in the world, continent and region where N is provided by the user.  

Scope: Organization. 

Level: Primary.  

Preconditions 

1) Database connects to world.sql that has world data. 

2) The user must be authorized person who includes in organization administrator 

Success End Condition: Report by top "n" populated by countries in the world, continent and region where N is provided by the user. 

Failed End Condition: Invalid data in database. 

Primary Actor: Organization administrator. 

Trigger: none 

MAIN SUCCESS SCENARIO 

1) Read population from country and city in continent and region from database. 

2) Get user input for “n” number to limit the numbers of output lines.  

3) Data retrieved from country and city table. 

4) Correct data and limited output lines.  

5) Produce a report with string format. 

6) Final report is written in table format in markdown file. 

EXTENSIONS: none. 

SUB-VARIATIONS: none. 

SCHEDULE: End of Code Review - January 6, 2023 


# Report 3 

Goal in Context: UML Report Part - Query by all cities of population in the world, continent and region organized by largest population to smallest. 

Scope: Organization. 

Level: Primary.  

Preconditions 

1) Database connects to world.sql that has world data. 

2) The user must be authorized person who includes in organization administrator 

Success End Condition: Report by all cities of population in the world, continent and region organized by largest population to smallest. 

Failed End Condition: Invalid data in database. 

Primary Actor: Organization administrator. 

Trigger: none 

MAIN SUCCESS SCENARIO 

1) Read population from country and city in continent and region from database. 

2) Data retrieved from country and city table. 

3) Data correctness.  

4) Produce a report with string format. 

5) Final report is written in table format in markdown file. 

EXTENSIONS: none. 

SUB-VARIATIONS: none. 

SCHEDULE: End of Code Review - January 6, 2023 


# Report 4 

Goal in Context: UML Report Part - Query by top "n" populated by cities in the world, continent, region, country and district where N is provided by the user. 

Scope: Organization. 

Level: Primary.  

Preconditions 

1) Database connects to world.sql that has world data. 

2) The user must be authorized person who includes in organization administrator 

Success End Condition: Report by top "n" populated by cities in the world, continent, region, country and district where N is provided by the user.  

Failed End Condition: Invalid data in database. 

Primary Actor: Organization administrator. 

Trigger: none 

MAIN SUCCESS SCENARIO 

1) Read population from country and city in continent, region, country and district from database. 

2) Get user input for “n” number to limit the numbers of output lines. 

3) Get user input for “name” of the relevant continent, region, country.  

4) Data retrieved from country and city table. 

5) Data correctness.  

6) Produce a report with string format. 

7) Final report is written in table format in markdown file. 

EXTENSIONS: none. 

SUB-VARIATIONS: none. 

SCHEDULE: End of Code Review - January 6, 2023 


# Report 5 

Goal in Context: UML Report Part - Query by all capital cities of population in the world, continent and region organized by largest population to smallest. 

Scope: Organization. 

Level: Primary.  

Preconditions 

1) Database connects to world.sql that has world data. 

2) The user must be authorized person who includes in organization administrator 

Success End Condition: Report by all capital cities of population in the world, continent and region organized by largest population to smallest. 

Failed End Condition: Invalid data in database. 

Primary Actor: Organization administrator. 

Trigger: none 

MAIN SUCCESS SCENARIO 

1) Read population from country and city in continent and region from database. 

2) Data retrieved from country and city table. 

3) Data correctness.  

4) Produce a report with string format. 

5) Final report is written in table format in markdown file. 

EXTENSIONS: none. 

SUB-VARIATIONS: none. 

SCHEDULE: End of Code Review - January 6, 2023 


# Report 6 

Goal in Context: UML Report Part - Query by top N populated capital cities in the world, continent, region where N is provided by the user. 

Scope: Organization. 

Level: Primary.  

Preconditions 

1) Database connects to world.sql that has world data. 

2) The user must be authorized person who includes in organization administrator 

Success End Condition: Report by all the capital cities in the world, continent, region organized by largest population to smallest. 

Failed End Condition: Invalid data in database. 

Primary Actor: Organization administrator. 

Trigger: none 

MAIN SUCCESS SCENARIO 

1) Read all capital cities in the continent, region from database. 

2) Get user input for “n” number to limit the numbers of output lines. 

3) Get user input for “name” of the relevant continent, region. 

4) Data retrieved from country and city table. 

5) Data correctness.  

6) Produce a report with string format. 

7) Final report is written in table format in markdown file. 

EXTENSIONS: none. 

SUB-VARIATIONS: none. 

SCHEDULE: End of Code Review - January 6, 2023 


# Report 7 

Goal in Context: UML Report Part - Query by population which is people living and not living in cities in each continent, region and country. 

Scope: Organization. 

Level: Primary.  

Preconditions 

1) Database connects to world.sql that has world data. 

2) The user must be authorized person who includes in organization administrator 

Success End Condition: Report by population which is people living and not living in cities in each continent, region and country. 

Failed End Condition: Invalid data in database. 

Primary Actor: Organization administrator. 

Trigger: none 

MAIN SUCCESS SCENARIO 

1) Read population from country and city in each continent, region and country from database. 

2) Data retrieved from country table. 

3) Data correctness.  

4) Produce a report with string format. 

5) Final report is written in table format in markdown file. 

EXTENSIONS: none. 

SUB-VARIATIONS: none. 

SCHEDULE: End of Code Review - January 6, 2023 


# Report 8 

Goal in Context: UML Report Part - Query by population of the world, continent, region, country, district and city. 

Scope: Organization. 

Level: Primary.  

Preconditions 

1) Database connects to world.sql that has world data. 

2) The user must be authorized person who includes in organization administrator 

Success End Condition: Report by population of the world, continent, region, country, district and city. 

Failed End Condition: Invalid data in database. 

Primary Actor: Organization administrator. 

Trigger: none 

MAIN SUCCESS SCENARIO 

1) Read population from country and city of the world, continent, region, country, district and city from database. 

2) Data retrieved from country table. 

3) Data correctness.  

4) Produce a report with string format. 

5) Final report is written in table format in markdown file. 

EXTENSIONS: none. 

SUB-VARIATIONS: none. 

SCHEDULE: End of Code Review - January 6, 2023 


# Report 9 

Goal in Context: UML Report Part - Query by greatest to smallest number of people population who speak languages. 

Scope: Organization. 

Level: Primary.  

Preconditions 

1) Database connects to world.sql that has world data. 

2) The user must be authorized person who includes in organization administrator 

Success End Condition: Report by greatest to smallest number of people population who speak languages. 

Failed End Condition: Invalid data in database. 

Primary Actor: Organization administrator. 

Trigger: none 

MAIN SUCCESS SCENARIO 

1) Read country language from database. 

2) Data retrieved from country table. 

3) Data correctness.  

4) Produce a report with string format. 

5) Final report is written in table format in markdown file. 

EXTENSIONS: none. 

SUB-VARIATIONS: none. 

SCHEDULE: End of Code Review - January 6, 2023 
