# IDEA EDU Course ...

Implemented in the Java Core Track of hyperskill.org's JetBrain Academy.

Purpose of doing this project, is to further practise core java topics as multi-threading, file handling, applying
new algorithms as Knuth-Morris-Pratt algorithm and Rabin-Karp algorithm for data hashing, searching and sorting - and just some more POJO java.

Also: This is the graduate project of the Java Core Track, which covers the most yet undone learning topics and thus
will serve to finish this last unfinished Jetbrains java track.

## Technology / External Libraries

- POJO Java 18,
- with Lombok annotation processors,
- Apache Log4j SLF4J API binding to Log4j 2 logging and
- Junit 5 with
- Mockito (mockito-inline) testing.

## Repository Contents

The sources of main project tasks (5 stages) and unit, mockito testing.

## Program description

An application / tool a tool that will extract info from unknown (also binary) files
to determine the type of the file. Different algorithms are used to solve this problem, and will
be compared regarding the speed (of search and sorting).

CL-Command overview:

coming up..

Have fun!

## Project completion

[//]: # (Project was completed on xx.0d.22.)

## Progress

30.08.22 Project started - just git repo and gradle setup.

02.09.22 Stage 1 completed - analyze arbitrary (also binary) file given as argument for occurrence of a (type defining)
string - also given as argument. Implemented in two versions with Files.readAllBytes and FileInputStream for huge files.