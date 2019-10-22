/bin/sh -c #(nop)  CMD ["/usr/lib/jvm/java-8-openjdk-amd64/bin/java" "-jar" "target/droolstest-1.0.jar"]
/bin/sh -c #(nop)  EXPOSE 4567/tcp
/bin/sh -c mvn install
/bin/sh -c mvn install:install-file -Dfile=./lib/antlr.jar -DgroupId=antlr -DartifactId=antlr -Dversion=unknown -Dpackaging=jar
/bin/sh -c mvn install:install-file -Dfile=./lib/ehcache-core-2.1.0.jar -DgroupId=ehcache -DartifactId=ehcache-core -Dversion=2.1.0 -Dpackaging=jar
/bin/sh -c mvn install:install-file -Dfile=./lib/hibernate-commons-annotations-3.1.0.GA.jar -DgroupId=hibernate -DartifactId=hibernate-commons-annotations -Dversion=3.1.0 -Dpackaging=jar
/bin/sh -c mvn install:install-file -Dfile=./lib/hibernate-annotations-3.4.0.GA.jar -DgroupId=hibernate -DartifactId=hibernate-annotations -Dversion=3.4.0 -Dpackaging=jar
/bin/sh -c mvn install:install-file -Dfile=./lib/hibernate-core-3.3.0.SP1.jar -DgroupId=hibernate -DartifactId=hibernate -Dversion=3.3.0 -Dpackaging=jar
/bin/sh -c mvn install:install-file -Dfile=./lib/opencsv-2.3.jar -DgroupId=org.opencsv -DartifactId=opencsv -Dversion=2.3 -Dpackaging=jar
/bin/sh -c #(nop)  WORKDIR /home/iDEA-Rules/
/bin/sh -c #(nop) COPY file:0f87530bad839051029262fda42a443e13df38780b23fac26fedf83576466609 in /home/iDEA-Rules/config.properties
/bin/sh -c #(nop) COPY dir:d84869b7d3df93af602738446bd2b0ac180c1cfd18a9afa5b739ec2e311e55da in /home/iDEA-Rules/
/bin/sh -c mkdir -p /home/iDEA-Rules
/bin/sh -c apt-get install -y maven
/bin/sh -c apt-get update
/bin/sh -c /var/lib/dpkg/info/ca-certificates-java.postinst configure
/bin/sh -c set -x  && apt-get update  && apt-get install -y   openjdk-8-jdk="$JAVA_DEBIAN_VERSION"   ca-certificates-java="$CA_CERTIFICATES_JAVA_VERSION"  && rm -rf /var/lib/apt/lists/*  && [ "$JAVA_HOME" = "$(docker-java-home)" ]
/bin/sh -c #(nop)  ENV CA_CERTIFICATES_JAVA_VERSION=20140324
/bin/sh -c #(nop)  ENV JAVA_DEBIAN_VERSION=8u111-b14-2~bpo8+1
/bin/sh -c #(nop)  ENV JAVA_VERSION=8u111
/bin/sh -c #(nop)  ENV JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
/bin/sh -c {   echo '#!/bin/sh';   echo 'set -e';   echo;   echo 'dirname "$(dirname "$(readlink -f "$(which javac || which java)")")"';  } > /usr/local/bin/docker-java-home  && chmod +x /usr/local/bin/docker-java-home
/bin/sh -c #(nop)  ENV LANG=C.UTF-8
/bin/sh -c echo 'deb http://deb.debian.org/debian jessie-backports main' > /etc/apt/sources.list.d/jessie-backports.list
/bin/sh -c apt-get update && apt-get install -y --no-install-recommends   bzip2   unzip   xz-utils  && rm -rf /var/lib/apt/lists/*
/bin/sh -c apt-get update && apt-get install -y --no-install-recommends   bzr   git   mercurial   openssh-client   subversion     procps  && rm -rf /var/lib/apt/lists/*
/bin/sh -c apt-get update && apt-get install -y --no-install-recommends   ca-certificates   curl   wget  && rm -rf /var/lib/apt/lists/*
/bin/sh -c #(nop)  CMD ["/bin/bash"]
/bin/sh -c #(nop) ADD file:89ecb642d662ee7edbb868340551106d51336c7e589fdaca4111725ec64da957 in /
