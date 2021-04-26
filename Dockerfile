FROM openjdk:12-jre
RUN mkdir app
ARG JAR_FILE
ADD /target/${JAR_FILE} /app/cadastrodelocalizacao.jar
WORKDIR /app
ENTRYPOINT java -jar cadastrodelocalizacao.jar
