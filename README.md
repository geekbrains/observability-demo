## add tracing support in the application
### Add depdendency in pom.xml
```xml
<!--bridges the Micrometer Observation API to OpenTelemetry-->
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-core</artifactId>
</dependency> 
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-otlp</artifactId>
</dependency> 
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-tracing-bridge-otel</artifactId>
    <version>1.2.3</version> <!-- Boot aligns this separately -->
</dependency>
<dependency>
    <groupId>io.opentelemetry</groupId>
    <artifactId>opentelemetry-exporter-otlp</artifactId>
</dependency>
```
### add configuration in application yaml
```yaml
management:
  endpoints:
    web:
      exposure:
        include: health
  metrics:
    export:
      otlp:
        enabled: true
        endpoint: http://localhost:4318/v1/metrics
        step: 10s
  opentelemetry:
    tracing:
      export:
        otlp:
          endpoint: http://localhost:4318/v1/traces
  tracing:
    sampling:
      probability: 1 # every request is sent to the trace backend.
```
### update docker-compose file with zipkins and jager.
```yaml
version: '3.8'

services:

  otel-collector:
    container_name: otel-collector
    image: otel/opentelemetry-collector:0.143.0
    restart: always
    volumes:
      - ./src/docker/otel-collector/otel-collector-config.yml:/etc/otelcol-contrib/otel-collector-config.yml
    command:
      - --config=/etc/otelcol-contrib/otel-collector-config.yml
    ports:
      - "1888:1888" # pprof extension
      - "8888:8888" # Prometheus metrics exposed by the Collector
      - "8889:8889" # Prometheus exporter metrics
      - "13133:13133" # health_check extension
   #   - "4317:4317" # OTLP gRPC receiver
      - "4318:4318" # OTLP http receiver
      - "55679:55679" # zpages extension
    depends_on:
      - jaeger-all-in-one
      - zipkin-all-in-one

  prometheus:
    container_name: prometheus
    image: prom/prometheus:latest
    restart: always
    ports:
      - "9090:9090"
    volumes:
      - ./src/docker/prometheus/prometheus.yml:/etc/prometheus/prometheus.yml
    command:
      - --config.file=/etc/prometheus/prometheus.yml
#    extra_hosts:
#      - "host.docker.internal:192.168.1.9"

  grafana:
    container_name: grafana
    image: grafana/grafana:latest
    restart: always
    ports:
      - "3000:3000"

  # Jaeger
  jaeger-all-in-one:
    image: jaegertracing/all-in-one:latest
    restart: always
    ports:
      - "16686:16686"
      - "14268"
      - "14250"
      - "4317:4317"  

  # Zipkin
  zipkin-all-in-one:
    image: openzipkin/zipkin:latest
    restart: always
    ports:
      - "9411:9411"
```
both jager and zipkins are distributed tracing applications where you can trace request with span
