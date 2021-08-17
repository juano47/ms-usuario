FROM openjdk:11.0.7-slim
LABEL maintainer="juan.delaiglesia.jdli@gmail.com"
ARG JAR_FILE
ADD target/${JAR_FILE} ms-usuarios.jar
RUN echo ${JAR_FILE}
ENTRYPOINT ["java","-jar","/ms-usuarios.jar"]