# ---------- Build ----------
FROM eclipse-temurin:21-jdk AS build
WORKDIR /build

# Baixa as dependências primeiro para aproveitar o cache de camadas
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw -B -q dependency:go-offline

COPY src/ src/
RUN ./mvnw -B -q package -DskipTests \
    && cp target/thindesk-*.war app.war

# ---------- Execução ----------
FROM eclipse-temurin:21-jre
WORKDIR /app

RUN useradd --system --no-create-home thindesk
COPY --from=build /build/app.war app.war
USER thindesk

ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.war"]
