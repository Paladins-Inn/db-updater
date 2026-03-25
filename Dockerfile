FROM quay.io/kaiserpfalzedv/java-runner:21-latest AS runner

COPY --chown=root:root --chmod=0555 ./target/app.jar /deployments
COPY --chown=root:root --chmod=0555 \
  ./README.md ./CONTRIBUTING.md ./LICENSE ./CODE_OF_CONDUCT.md ./SECURITY.md \
  ./CHANGELOG.md \
  ./KES*.pdf \
  /