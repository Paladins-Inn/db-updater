FROM docker.io/library/eclipse-temurin:25-jre-alpine AS runner

LABEL org.opencontainers.image.title="db-updater" \
      org.opencontainers.image.description="Database Updater CLI/Liquibase Runner for Paladins Inn" \
      org.opencontainers.image.authors="Roland T. Lichti <rlichti@kaiserpfalz-edv.de" \
      org.opencontainers.image.vendor="Paladins Inn" \
      org.opencontainers.image.url="https://docs.paladins-inn.de/db-updater/dev/manual/index.html" \
      org.opencontainers.image.source="https://github.com/Paladins-Inn/db-updater" \
      org.opencontainers.image.licenses="AGPL-3.0" \
      org.opencontainers.image.base.name="quay.io/paladinsinn/db-updater"

COPY --chown=1000:1000 --chmod=0555 \
  ./target/app.jar \
  ./src/main/resources/db/changelog/db.changelog-master.yaml \
  ./README.md ./CONTRIBUTING.md ./LICENSE ./CODE_OF_CONDUCT.md ./SECURITY.md \
  ./CHANGELOG.md \
  ./KES*.pdf \
  /

ENV TZ=UTC \
  LANG=C.UTF-8

USER 1000
ENTRYPOINT ["java", "-XX:+ExitOnOutOfMemoryError", "-XX:MaxRAMPercentage=75.0", "-Dfile.encoding=UTF-8", "-Duser.timezone=UTC", "-jar", "/app.jar", "--update-database"]
