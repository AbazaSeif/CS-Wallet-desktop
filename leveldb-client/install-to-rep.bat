cd target
mvn install:install-file -Dfile=leveldb-client.jar -DgroupId=com.credits -DartifactId=leveldb-client -Dversion=1.0-SNAPSHOT -Dpackaging=jar -DpomFile=../pom.xml