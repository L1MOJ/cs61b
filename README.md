
# Proj2 Gitlet

[GItlet](https://sp21.datastructur.es/materials/proj/proj2/proj2#checkout)

```java
/*
	*	.gitlet
	*		/--objects
	*		/	/--commits and blobs
	*		/--refs
	*		/	/--heads




	*/
```

#### Failure cases

- No input args, print Please enter a command. and exit(System.exit(0))
- Command not exist, print No command with that name exists. and exit
- Command with wrong number or format of operands, Incorrect operands.
- Command requires a initialized working directory but it's not, Not in an initialized Gitlet directory


## add

- Staging an already staged file, overwrite
- Not exist, File does not exist. exit
- One file at a time

## Commit

- No file on stage, No changes to the commit.
- Blank commit message, Please enter a commit message.
- Clone the previous commit


## Branch

- name confliction, A branch with that name already exists.CA

## Head

- Currently active pointer. We can switch the currently active head pointer with checkout [branch name]

So I will just. store branch information in HEAD file

## Merge

- When merging,the first parent is the branch you were on when you did the merge


## Status

- Modified but not staged
  - In current commit, changed but not staged(delete or modified)
  - staged for addition but with different contents now
  - staged for addition but deleted
- Untracked files

## checkout

- java gitlet.Main checkout --[file name]
  - replace the file in cwd with the head commit version
  - file does not exist, No commit with that id exists
- checkout [commit id] --[file name]
  - replace the file in cwd with the given commit version
  - no commit with that given id exists, No commit with that id exists
- checkout [branch name]
  - put all files in the commit of the head of branch in the pwd
  - move HEAD
  - delete files that are tracked in the current branch but not present in the updated branch
  - clear stage?
  - No branch with that name,No such branch exists.
  - No need to checkout the current branch
  - 文件名仅被Commit3B追踪的文件，而不被Commit3A追踪，那么直接将这些文件写入到工作目录。这里有个例外，即对于第三种情况，将要直接写入的时候如果有同名文件（例如1.txt）已经在工作目录中了，说明工作目录中在执行checkout前增加了新的1.txt文件而没有commit，这时候gitlet不知道是应该保存用户新添加进来的1.txt还是把Commit3B中的1.txt拿过来overwrite掉，为了避免出现信息丢失，gitlet就会报错，输出There is an untracked file in the way; delete it, or add and commit it first.

## reset

- checks out given commit
- move the current's branch head to that commit node

