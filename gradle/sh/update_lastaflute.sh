#!/bin/bash
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
