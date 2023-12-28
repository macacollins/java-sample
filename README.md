# Java/Spring Sample

This is a sample microservice using Java and Spring Boot. The application itself calculates Fibonacci values and uses one of three methods based on an environment variable. This functionality was chosen to show an evidence-based approach to making changes. The same methodology of deploying multiple versions of a service and checking logs and metrics is standard debugging practice for real-world services. First, let's cover the different methods used in the Java applications.

# Naive Algorithm

The Fibonacci algorithms used in this application all rely on recursion to calculate the appropriate values. For Fibonacci values themselves, you can check out [this link](https://www.math.net/list-of-fibonacci-numbers). The naive version with just integers is included in the IntegerFibonacciGenerator class:

```java
    public int getFibonacciInt(int sequenceNumber) {
        if (sequenceNumber == 0) {
            return 0;
        } else if (sequenceNumber == 1) {
            return 1;
        } else {
            return getFibonacciInt(sequenceNumber - 1) +
                    getFibonacciInt(sequenceNumber - 2);
        }
    }
```

This method appears to work at first, but there are a couple of drawbacks. The first is that `int` has limited precision. After the 48th value in the fibonacci sequence, this method will overflow and become negative. Switching to `long` extends the capability up to the 92nd number, but then this service is unnecessarily limited. Computers can quickly generate way more than 92 Fibonacci numbers.

# BigInteger

Java has the `BigInteger` class for numbers above the limits of `long`. So, the second version of this method uses the `BigInteger` class. The code is basically the same and found in the `BigIntFibonacciGenerator` class.

```java
    private BigInteger getFibonacciBigInt(int sequenceNumber) {
        if (sequenceNumber == 0) {
            return BigInteger.ZERO;
        } else if (sequenceNumber == 1) {
            return BigInteger.ONE;
        } else {
            return getFibonacciBigInt(sequenceNumber - 1).add(
                    getFibonacciBigInt(sequenceNumber - 2));
        }
    }
```

This method works great... for only a few more numbers than the `int` version. The second problem with the naive recursive algorithm is that it calculates the same values many many times. Around 50 items, this gets so slow that most REST clients and network devices (service mesh, ingress, etc.) will time out and the client will not get a successful response. Not good.

# Memoized BigInteger

The Fibonacci method is a "pure function", meaning that the same input will always produce the same output. This means we can memoize, or cache, the results in order to prevent the code from calculating intermediate results many many times. 

This code uses a `HashMap`, but an `Array`/`ArrayList` could also be used here with slightly different storage size and resizing characteristics. If that level of performance matters, you could repeat the method used in this service to test an implementation using `Array` instead.

```java
    private Map<Integer, BigInteger> memo = new HashMap<>();

    private BigInteger getFibonacciBigIntMemoized(int sequenceNumber) {
        // If the service already calculated the number, no need to do it again
        if (memo.containsKey(sequenceNumber)) {
            return memo.get(sequenceNumber);
        }

        BigInteger result;
        if (sequenceNumber == 0) {
            result = BigInteger.ZERO;
        } else if (sequenceNumber == 1) {
            result = BigInteger.ONE;
        } else {
            result = getFibonacciBigIntMemoized(sequenceNumber - 1).add(
                    getFibonacciBigIntMemoized(sequenceNumber - 2));
        }

        // Save the new computed result
        memo.put(sequenceNumber, result);

        return result;
    }
```

With this code in place, the service can handle quite a few more results. The limit is now in the recursion limit of the JRE, which the code does hit after a few thousand items in the sequence. You can increase the limit by requesting sequence number 500, then 1000, then 1500, etc. to populate the cache and reduce the size of the recursion stack. This example does not attempt to rewrite the Fibonacci algorithm without recursion or manually prime the cache, but both are possible. Without the recursion stack limitation, the limit becomes memory for cache results. The service should be able to fit many results in a few megabytes as they contain much less data than images, for example.

# Testing

This code and reasoning is interesting, but we need data to tell us whether the changes really matter. For this example, it's possible to know beforehand that `int` / `long` / `BigInteger` without cache is not going to work for large numbers. However, this project includes testing and metrics to ensure the services are working as expected. For any changes to a production service, we need to check the data to ensure that changes are going to make the desired change to the service at runtime.

This example uses some Kubernetes manifests in a Helm chart to allow testing the different versions. All three algorithms are included in the Java / Spring Boot server container image. The Java code uses an environment variable to switch between the different modes. The details are in the `FibonacciFactory` class. Each algorithm has its own Kubernetes Deployment and Service in the `helm-testing` directory. Then, a script simulates traffic to the different services. The script is deployed to Kubernetes as a deployment with a script manually mounted into the container from a Kubernetes ConfigMap. The details are in the `deployment-traffic.yaml` file. This script makes requests to each of the services every couple of seconds with an increasing sequence number.

# Metrics

The Java application is instrumented using the Micrometer project, which is the default for Spring Boot. This worked well out of the box and provided a metrics endpoint under the /actuator/prometheus path. The testing manifests include a deployment for a Prometheus server that scrapes the three deployments and records the metrics. Using `kubectl port-forward` allowed me to check the metrics and how they change over time. This minimal Prometheus setup is used because there is no corporate Kubernetes test cluster with an annotation-based Prometheus lookup. The full configuration details, including the custom prometheus.yml, are included in the `deployment-prometheus.yaml` file.

# Results

![Graph of request counts per response status code](https://github.com/macacollins/java-sample/blob/ca6ee276a5b443e6460f4c3e1d471f1aa8073693/images/graph1.png)

This graph shows the count of successful requests to the three different versions. The results show a steadily increasing number for the memoized `BigInteger` version. However, the numbers are quite messy for the `int` and non-memoized `BigInteger` versions. Many requests never completed, and the containers restarted often due to the strain of computing large Fibonacci values without memoization. The choice here is obvious--memoized `BigInteger` is the winning version according to the data.

![Graph of timing for the fibonacci generation service](https://github.com/macacollins/java-sample/blob/ca6ee276a5b443e6460f4c3e1d471f1aa8073693/images/graph2.png)

This metric records a Prometheus `Summary` of the timing of fibonacci generation from the server's point of view. The non-memoized versions didn't even return values after a certain point as they started to fail. The memoized service returned a fairly performant profile with the 99.9th percentile at 9ms for the 500th sequence item. More testing is needed for higher sequence numbers, but the service is already set up to record metrics.

# Next steps

There are several more things to do here.

- Finalize the Kubernetes items for the deployment target: Service, Ingress, Route (OpenShift), or other CRD (Service mesh or AWS / Azure / GCP items)
- Figure out priming or removing recursion to avoid recursion depth errors
- Unit tests
- Grafana Dashboards to make metric data more digestible
- Edge case testing, this primarily measures the happy path
- For real production, remove the algorithms that aren't chosen


