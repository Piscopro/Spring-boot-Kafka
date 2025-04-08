# Spring Boot Kafka Example

This is to demonstrate two simple spring boot microservices which uses kafka to send and receive messages.

## Overview
![GitHub Logo](/Overview.png)

## Kafka Architecture Components

### Broker
A Kafka broker is a server that runs the Kafka process. Brokers form the heart of a Kafka cluster and are responsible for:

- Receiving and storing messages from producers
- Serving messages to consumers when requested
- Managing topic partitions and replications
- Handling fault tolerance through data replication

Our cluster setup uses 3 brokers (kafka1, kafka2, kafka3) to provide high availability and fault tolerance. If one broker fails, the others continue operating, ensuring the system remains available.

### ZooKeeper
ZooKeeper is a centralized service for maintaining configuration information, naming, and providing distributed synchronization for Kafka. It keeps track of:

- Which brokers are part of the Kafka cluster
- Which topics exist and their configurations
- Leader election for partitions

### Topic Partitioning & Replication
- **Partitions**: Topics are divided into partitions, allowing parallel processing of data
- **Replication**: Each partition is replicated across multiple brokers (3 in our setup)
- **In-sync replicas**: We require at least 2 in-sync replicas for data safety

## Project structure:
1. Producer
2. Consumer
3. Docker-compose-files
4. Postman

### Postman
This is collection of postman api's for producer and consumer. Producer can send the messages in string format or json format. ProducerMsg contains sending information via string. ProducerInformation contains informationModel as Json data.

### Docker-compose-files
This contains three docker-compose files
1. only zookeeper and kafka (docker-compose-only-kafka-zookeeper.yml)
2. Entire application (docker-compose.yml)
3. Kafka cluster with 3 brokers (docker-compose-kafka-cluster.yml)

The Kafka cluster setup provides:
- Fault tolerance: If one broker fails, the system continues operating
- Higher throughput: Multiple brokers can handle more traffic
- Data safety: Data is replicated across multiple servers

### Producer
Here producer uses Apache.kafka library KafkaProducer to send the information. 

#### Note
>If you want to run producer in IDE, please change **kafka.server=localhost:29092,localhost:29093,localhost:29094** in  **application.properties**. 

### Consumer
Consumer uses Apache kafka library KafkaConsumer to retrive messages from the kafka.
Executors [Thread] is used to read the information from the kafka bus. Upon reading the information, it is placed in internal queue. This used when user try to do get request for messages. Upon performing get operation, messages in the queue are cleared.

#### Note
>If you want to run producer in IDE, please change **kafka.server=localhost:29092,localhost:29093,localhost:29094** in  **application.properties**. 


## Build procedure
Please pull the latest zookeeper and kafka images

### Pulling zookeeper and kafka images
```
docker pull confluentinc/cp-kafka
docker pull confluentinc/cp-zookeeper
```

### Build producer
Navigate to producer folder
```
./gradlew build -x test
docker build -t producer .
```

### Build consumer
Navigate to consumer folder
```
./gradlew build -x test
docker build -t consumer .
```

### Running End to End Dockers
Navigate to docker-compose-files
```
docker-compose up -d
```

### Running with Kafka Cluster
Navigate to docker-compose-files
```
docker-compose -f docker-compose-kafka-cluster.yml up -d
```

### Stopping docker containers
Navigate to docker-compose-files
```
docker-compose down
```
or
```
docker-compose -f docker-compose-kafka-cluster.yml down
```

#### Running only zookeeper and kafka [Only if you want to run producer and consumer in IDE]
Navigate to docker-compose-files
```
docker-compose -f docker-compose-only-kafka-zookeeper.yml up -d
```


