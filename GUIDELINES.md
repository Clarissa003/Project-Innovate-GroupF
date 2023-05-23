# Code Guidelines

## Table of contents
1. [General](GUIDELINES.md#general)
2. [PYTHON](GUIDELINES.md#python)
3. [KOTLIN](GUIDELINES.md#kotlin)
4. [JAVASCRIPT](GUIDELINES.md#javascript)
5. [GIT](GUIDELINES.md#git)

## General


## PYTHON

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