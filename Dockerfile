FROM maven

WORKDIR /root
COPY pom.xml /root/pom.xml
COPY src /root/src

RUN mvn install

EXPOSE 8080

CMD ["bash", "-c", "java -Xmx256M -jar target/amazon-echo-bridge-*.jar --upnp.config.address=$(ip route get 8.8.8.8 | egrep -o '[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\s*$')"]
