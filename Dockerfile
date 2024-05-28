FROM alpine:3.19.1 as build

RUN apk add maven openjdk21

WORKDIR build

COPY . .

RUN cd /usr/share/java/maven-3/conf && \
    cp /build/src/main/resources/settings.xml settings.xml

RUN mvn package

FROM tomcat:10.1.24

COPY --from=build /build/target/ROOT.war webapps


