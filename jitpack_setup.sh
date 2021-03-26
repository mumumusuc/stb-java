#!/bin/bash

echo "check third_party/stb ..."
if [ ! -d "third_party/stb/" ];then
    echo "    -- no exist, fetching ..."
    git submodule init
    git submodule update
else
    echo "    -- OK!"
fi

