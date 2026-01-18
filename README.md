# Otel collector integration with spring boot

- Add micrometer-registry-otlp lib in the project
- make sure actuator dependency is also there.

micrometer is a facade to integrate promethus, otel, grafana
while actuator gives metrics and autoconfigure micrometer

we need to add micrometer configuration in springboot application
```yaml
management:
    metrics:
        exporter:
            otlp:
              enabled: true
              endpoint: http://localhost:4318/v1/metrics # otel collector receiver is reciving from this location
              step: 10s # in every 10 second it will publish metrics on this endpoint

```
## otel-collector configuration
in otel collector configuration, in receiver we can give otlp point.
if application is in same cluster of docker network, we can use localhost otherwise we can use 0.0.0.0:4318
in exporter section we need to provide the endpoint where otel will expose metrics for promethus.
so we can give
```yaml
exporters:
  prometheus:
    endpoint: "0.0.0.0:8889"
    const_labels:
      label1: value1
```

in prometheus.yml file we need to define same endpoint in scraping config--> targets section
```yaml
scrape_configs:
  - job_name: 'otel-collector'
    static_configs:
      - targets: ['otel-collector:8889']  # replace with your app host:port
```
here targets is having docker service name as otel and promethus are running inside docker
so they can understand each other name




