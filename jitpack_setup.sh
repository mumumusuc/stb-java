#!/bin/bash

echo "check third_party/stb ..."
if [ ! -d "third_party/stb/" ];then
    echo "    -- no exist, fetching ..."
    git submodule init
    git submodule update
else
    echo "    -- OK!"
fi

echo "check android_sdk ..."
ls -al $ANDROID_HOME

echo "check project ..."
ls -al

echo "all done"

