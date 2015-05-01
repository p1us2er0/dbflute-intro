#!/bin/bash

if [ ! -e dbflute-core ]; then
    git clone https://github.com/dbflute/dbflute-core.git
    cd dbflute-core
    git checkout -b develop origin/develop
    cd ../
fi

if [ ! -e lasta-try ]; then
    git clone https://github.com/jflute/lasta-try.git
fi

if [ ! -e utflute-lasta ]; then
    git clone https://github.com/dbflute-utflute/utflute-lasta.git
fi

cd ./dbflute-core
git pull
cd ../lasta-try
git pull
cd ../utflute-lasta
git pull

cd ../dbflute-core/dbflute-runtime
mvn install -DskipTests=true -Dmaven.javadoc.skip=true -Dgpg.skip=true
cd ../../lasta-try/lasta-di
mvn install -DskipTests=true -Dmaven.javadoc.skip=true -Dgpg.skip=true
cd ../../utflute-lasta/utflute-lasta-di
mvn install -DskipTests=true -Dmaven.javadoc.skip=true -Dgpg.skip=true
cd ../../lasta-try/lastaflute
mvn install -DskipTests=true -Dmaven.javadoc.skip=true -Dgpg.skip=true
cd ../../utflute-lasta/utflute-lastaflute
mvn install -DskipTests=true -Dmaven.javadoc.skip=true -Dgpg.skip=true
cd ../../lasta-try/lasta-taglib
mvn install -DskipTests=true -Dmaven.javadoc.skip=true -Dgpg.skip=true
