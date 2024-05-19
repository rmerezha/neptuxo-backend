FROM alpine:3.19.1 as build

RUN apk add maven openjdk21

WORKDIR build

COPY . .

RUN mvn package

FROM tomcat:10.1.24

COPY --from=build target/ROOT.war webapps


