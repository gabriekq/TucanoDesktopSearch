
### Overview 

The TucanoDesktopSearch allows the user to search files on the Windows desktop machine 
and then display the files found in a list side by side with the directory where the file was found.

### Features

The application has three features :

1 - Create Index
<br>
2 - Search
<br>
3 - Rebuild Index

#### Create Index

Create Index feature allows to pre-map all the folders and sub-folders,
<br>
inside it that the user intends to search in the future.
<br>
In order to speed up future searches using the same folder choose.

#### Search

Search option allows the user to find the specific file giving the path and the file name to be searched. 
<br>
If the given path is not pre indexed yet the search will index it first then perform the search process as follows.

#### Rebuild Index

Rebuild Index recreates the mapping for all indexes already created in the system.
<br>
In case the sub folders inside the main folder used has been renamed or deleted this option re-created it.

### Application Demo

In order to use the Application to search a file the user must ender.

 - The Folder Path
 - File Name

![Alt text](docs/tucanoDesktop-1.jpg?raw=true "Application Search Feature")

On the Creation index feature the user the following field is required.

- The Folder Path

In case the index is already created the Application displays a message in the left bottom saying that the file already exists.

![Alt text](docs/tucanoDesktop-2.jpg?raw=true "Application Index Feature")

For the Rebuild Index Feature none of the first two fields are required.
<br>
Only the rebuild index option needs to be select and the execute button to be pushed.

![Alt text](docs/tucanoDesktop-3.jpg?raw=true "Application Rebuild Index Feature")

### Technologies Used

This project were developed using the Java 17, JavaFX and IntelliJ IDEA 2022 community.

- Disclaimer : At this project I did not used any IA tools doing the development phase at all.