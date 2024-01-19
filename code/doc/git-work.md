### 下载仓库

```
git clone https://git.xmu.edu.cn/zeyuzuo/oomall.git
```

### 创建分支

```
cd oomall
git branch [yourBranch] //创建分支
git checkout [yourBranch] //切换分支
```

### 修改完

```
git add * // 把更改保存
git commit -m "[commit reason]" // commit一下
git push origin [yourBranch] //传上去
```

`git push origin [yourBracn]`后，要想合并就点那个网址

### 同步代码

```
git checkout main // 切换到main分支
git pull //拉下来
git checkout [yourBranch]
git merge main
```

test
