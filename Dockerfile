FROM java:openjdk-8-jdk

RUN apt-get update && \
    apt-get -y install wget unzip && \
    apt-get clean

ENV GROOVY_VERSION=2.4.5
ENV JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 \
    GROOVY_HOME=/opt/groovy-${GROOVY_VERSION}

ENV PATH=$GROOVY_HOME/bin/:$JAVA_HOME/bin:$PATH

# Install groovy
ADD http://dl.bintray.com/groovy/maven/apache-groovy-binary-${GROOVY_VERSION}.zip /tmp/

RUN unzip -d /opt/ /tmp/apache-groovy-binary-${GROOVY_VERSION}.zip \
  && rm /tmp/apache-groovy-binary-${GROOVY_VERSION}.zip

ADD ./src/ /groovyApp

EXPOSE 8080

WORKDIR /groovyApp

ENTRYPOINT ["groovy", "app.groovy"]
