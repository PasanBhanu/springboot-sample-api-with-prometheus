# Spring Boot Sample API with Prometheus


## Build Docker File

Use below command to build the docker file. You can replace the image name if needed. Below image is already available in docker hub.

```
docker build -t pasanbhanu/spring-boot-api-with-prometheus:latest .
docker push pasanbhanu/spring-boot-api-with-prometheus:latest
```

## Add Scrape Configs to Prometheus

You have to add below scrape config to your prometheus instance to enable metric scraping. If you are using Kubernetes, you can use labels or config map option to input the scrape configuration.

### For Non-Kubernetes Deployments
```
scrape_configs:
  - job_name: 'spring-boot-application'
    metrics_path: '/actuator/prometheus'
    scrape_interval: 15s
    static_configs:
      - targets: ['localhost:8080']
```

# For Kubernetes Deployments
```
scrape_configs:
  - job_name: 'spring-boot-application'
    metrics_path: '/actuator/prometheus'
    scrape_interval: 15s
    static_configs:
      - targets: ['springbootapi-service.namespace.svc.cluster.default:8080']
```