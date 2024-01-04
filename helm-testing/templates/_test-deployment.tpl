{{ define "test-deployment" }}
---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: fibonacci-{{/* Just read the . context for now */}}{{ . }}
spec:
  replicas: 1
  selector:
    matchLabels:
      fibonacci-setting: {{/* Just read the . context for now */}}{{ . }}
  template:
    metadata:
      labels:
        fibonacci-setting: {{/* Just read the . context for now */}}{{ . }}
    spec:
      containers:
        - name: fibonacci-{{/* Just read the . context for now */}}{{ . }}
          # Requires you to run docker build . -t fibonacci-java-sample from the root directory
          image: "fibonacci-java-sample"
          imagePullPolicy: Never
          env:
            - name: FIBONACCI_SETTING
              value: "{{/* Just read the . context for now */}}{{ . }}"
          ports:
            - name: http
              containerPort: 8080
              protocol: TCP
          livenessProbe:
            httpGet:
              path: /actuator/health
              port: http
          readinessProbe:
            httpGet:
              path: /actuator/health
              port: http
          startupProbe:
            httpGet:
              path: /actuator/health
              port: http
            failureThreshold: 30
            periodSeconds: 10
          resources:
            requests:
              cpu: 100m
              memory: 512
---
apiVersion: v1
kind: Service
metadata:
  name: {{/* Just read the . context for now */}}{{ . }}-service
spec:
  type: ClusterIP
  ports:
    - port: 80
      targetPort: 8080
      protocol: TCP
      name: http
  selector:
    fibonacci-setting: {{/* Just read the . context for now */}}{{ . }}
{{ end }}