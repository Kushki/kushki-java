FROM openjdk:8
RUN wget -q https://services.gradle.org/distributions/gradle-2.2-all.zip \
    && unzip gradle-2.2-all.zip -d /opt \
    && rm gradle-2.2-all.zip

ENV GRADLE_HOME /opt/gradle-2.2
ENV PATH $PATH:/opt/gradle-2.2/bin
COPY . /project
WORKDIR /project
CMD ["printenv"]