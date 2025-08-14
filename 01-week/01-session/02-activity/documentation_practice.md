# MR Flow Between Environments - Git Exercise

## Objective
Simulate the flow of changes through environments: `project → dev → qa → main`.  
Document each action including branch creation, commits, pushes, and merges, using Conventional Commits in English.

---

## 1. Initial Setup

Check current branch and fetch latest changes:

```bash
git status
git fetch --all
git checkout main
git pull

git branch project
git branch dev
git branch qa

git push -u origin project
git push -u origin dev
git push -u origin qa

git checkout project
echo "Initial change in project" > cambios.md
git add cambios.md
git commit -m "docs(project): add initial cambios.md for MR flow simulation"
git push

git checkout dev
git merge project
git push

echo "Changes applied in dev" >> cambios.md
git add cambios.md
git commit -m "docs(dev): update cambios.md with dev changes"
git push

git checkout qa
git merge dev
git push

echo "QA updates" >> cambios.md
git add cambios.md
git commit -m "docs(qa): add QA updates to cambios.md"
git push

git checkout main
git merge qa
git push
