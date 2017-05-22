
USER INSTRUCTIONS:
------------------

To execute the 'jar' file just open a console and type:
java -jar watersChallenge1.jar 

It takes 1 minute to compute the analysis.

CONSIDERATIONS
--------------

*Challenge 1 model changes:
Since the core utilization is more than 1, the runnable worst-case execution time has been decreased in some tasks:

- Core0: -50% of runnable execution time applied to all deadline miss tasks
- Core1: -63% of runnable execution time applied to all deadline miss tasks
- Core2: -35% of runnable execution time applied to the first task
- Core3: -16% of runnable execution time applied to all deadline miss tasks

*Challenge 2 model changes:
As in the previous challenge, the runnable worst-case execution time has been decreased (in the same previous way) and the core frequency has been increased to 350 MhZ