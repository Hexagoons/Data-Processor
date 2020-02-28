#!/bin/bash
set -e 

if [ -z $1 ]
then
    echo "Resetting..."
    git reset --hard
    echo "Reset done."
else
    echo "Not resetting..."
fi

git pull; "C:/Users/Joey M. Behrens/Appdata/Local/JetBrains/ToolBox/apps/IDEA-C/ch-0/193.6015.39/plugins/maven/lib/maven3" clean package
