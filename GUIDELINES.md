# Code Guidelines

## Table of contents
1. [General](GUIDELINES.md#general)
2. [PYTHON](GUIDELINES.md#python)
3. [KOTLIN](GUIDELINES.md#kotlin)
4. [JAVASCRIPT](GUIDELINES.md#javascript)
5. [GIT](GUIDELINES.md#git)

## General
Indentation: 4 spaces

Use camel case when writing code and naming files

For naming variables, functions, files etc. use camel case

camelCase examples:

exampleVariable = "foo";

## PYTHON
File structure template
•	Begin with a set of comments clearly describing the purpose and assumptions of the code.
•	Import any required library headers.
•	Define constants.
•	Define global variables.
•	Define subroutines (functions).

Naming Conventions
If the variables used for the Python contain 2 words, camelCase should be used. 
Examples:
	leftMotor
	rightMotor

Blank Lines
Surround top-level function and class definitions with two blank lines. Method definitions inside a class are surrounded by a single blank line. Extra blank lines may be used (sparingly) to separate groups of related functions. Blank lines may be omitted between a bunch of related one-liners (e.g. a set of dummy implementations). Use blank lines in functions, sparingly, to indicate logical sections.
Page and File Naming
If you are working with multiple codes, have naming that explains what each file is doing.

Comments 
If you use good descriptive names for your variables and functions, you should not need comments. Try to make your naming so good that comments are not necessary. However sometimes, especially when calling third-party libraries, you may need to add comments to explain what is happening or if you think your code is too complex. Also, you need to add comments separating all your variables to be easier to search through code if you want to change the pins.

Readability Alignment and End of Statements
For multi-line statements the first line of a statement should left-align with the last line of that statement. In the case of an if/else statement the 'else' should also left-align with the ‘if’ and the last line. If you are using curly bracket notation, then the curly brackets should align instead.

Code Indentation
Use 4 spaces per indentation level. Continuation lines should align wrapped elements either vertically using Python’s implicit line joining inside parentheses, brackets and braces, or using a hanging indent. When using a hanging indent the following should be considered; there should be no arguments on the first line and further indentation should be used to clearly distinguish itself as a continuation line.
Spacing
•	Spaces should be used liberally to make sure equal signs line up if there is a series of values being set.
•	Spaces should be used around variables and periods to make lines easier to read.
•	In general, if a space does not cause a problem, it should be inserted so developers can use the Control key with the Arrow keys to navigate.
Applications/Other conventions
•	The used IDE will be Thonny.
•	The version control system will be done using GitHub.

## KOTLIN

## JAVASCRIPT

## Git
### Contribution
#### Branches
Each issue should have their own separate branch. 
  
Name of the branch should be: ```issue[github issue]```  
Example would be ```issue01``` for issue #1  

#### Commits
Only rule for the commits is to put a short, accurate description of the changes.


### Setting up local repository
1. Clone the repository
```
git clone git@github.com:Clarissa003/Project-Innovate-GroupF.git
```
2. Navigate to the folder where you have the project files
```
cd Project-Innovate-GroupF
```
3. Switch to your branch
```
git checkout <branch-name>
```
4. (Optional) Make sure that your branch is up to date
```
git pull
```

### Commiting your changes to github
1. Add files to commit
```
git add .
```
2. Commit your changes locally (Make sure to put descriptive message!)
```
git commit -m "<your-message>"
```
3. Make sure that your branch is up to date
```
git pull
```
4. Push your changes to GitHub
```
git push
```

### Reverting your push
1. Find the hash of you commit from GitHub
2. Revert your changes
```
git revert <commit-hash>
```
3. Commit your changes
```
git commit -m "<your-message>"
```
4. Push your changes
```
git push
```

### Sync your branch to master
1. Make sure you are in your branch
```
git checkout <branch-name>
```
```
git fetch origin
git merge origin/master
```
5. (Optional) Fix possible merge conflicts
