# Spring Boot Sample API with Prometheus

This is a sample Spring Boot app with Prometheus monitoring. This app can be used for stress testing, autoscaler testing or any other testing scenarios.

## Available API

### Request

GET - /sample
```
curl --location 'http://localhost:8080/sample'
```

### Response
200 OK
```
{
    "status": "success"
}
```

### Prometheus Endpoint

You can view output of prometheus via `http://localhost:8080/actuator/prometheus`

## Build Docker File

Use below command to build the docker file. You can replace the image name if needed. Below image is already [available in docker hub](https://hub.docker.com/r/pasanbhanu/spring-boot-api-with-prometheus).

```
docker build -t pasanbhanu/spring-boot-api-with-prometheus:latest .
docker push pasanbhanu/spring-boot-api-with-prometheus:latest
```

## Deployment to Kubernetes

You can deploy this application to Kubernetes using the available deployment.yaml

Create namespace (_springboot-test_)
```
kubectl create namespace springboot-test
```

Deploy application
```
kubectl apply -f deployment.yaml -n springboot-test
```

Deployment file includes the deployment of 1 replica, service and required tags for **kube-prometheus** to scrape the prometheus configs automatically.

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

### For Kubernetes Deployments

You are required to deploy kube-prometheus to your cluster. You can use Bitnami Kube-Prometheus helm chart for deployment. 

Documentation: https://github.com/bitnami/charts/tree/main/bitnami/kube-prometheus

Provided config below will allow you to deploy a minimal version of kube-prometheus in your cluster. Feel free to do customizations for your deployment based on the requirement.

#### Deploy Kube-Prometheus

Create namespace
```
kubectl create namespace monitoring
```

Create a values file with below content. File name is prometheus-values.yaml

```
prometheus:
  replicaCount: 1
  retention: 2h
  resourcesPreset: none

alertmanager:
  enabled: false

blackboxExporter:
  enabled: false

exporters:
  node-exporter:
    enabled: false
```
Use below command to deploy kube-prometheus via Helm. You can use the same command to upgrade the kube-prometheus. Feel free to modify the values file and redeploy.

```
helm upgrade --install prmop oci://registry-1.docker.io/bitnamicharts/kube-prometheus --values prometheus-values.yaml --namespace=monitoring
```

Create service-monitor.yaml to create auto scraper for your application.

```
apiVersion: monitoring.coreos.com/v1
kind: ServiceMonitor
metadata:
  name: service-monitor
  labels:
    release: prometheus-operator
spec:
  namespaceSelector:
    matchNames:
      - springboot-test
  selector:
    matchLabels:
      monitoring: "true"
  endpoints:
    - port: http                    # Port identifier. In your service, exposing port should have same name.
      path: "/actuator/prometheus"  # Scrape url. This is for Spring Boot.
```

Install the service monitor

```
kubectl apply -f service-monitor.yaml -n monitoring
```

Now you can visit the prometheus dashboard and check the targets to confirm your service is discovered by prometheus.

_All the files are available under kubernetes folder._

## Used for Testing:

* https://github.com/PasanBhanu/time-series-forcasting-with-facebook-prophet-lstm-hybrid-model
* https://github.com/PasanBhanu/proactive-autoscaler-for-keda-kubernetes

If you used this sample app for your testing, feel free to create PR. 

Need more features, PRs welcome!