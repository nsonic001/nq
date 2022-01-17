# nq
## IN Memory Multi Consumer FIFO Queue

## Run As spring boot Application

NQ is a In-memory multi consumer queue with REST API to publish and subscribe.

## Features

- Produce JSON Message to specific queue
- Queue gets autocreated
- Consume JSON Messages from existing queue
- Consume JSON Messages from existing queue with given offset
- One Day expiry, Background thread runs to clear the old messages

## API Contracts

### Message Produce 
```sh
curl --location --request PUT 'http://localhost:8080/v1/produce/msg' \
--header 'Content-Type: application/json' \
--data-raw '{
    "queue_name": "queue2",
    "json_object": {
        "key": "value1"
    }
}'
```
###### queue_name: Name of the queue where message to be produced
###### json_object: message json

###### Response:
```
{
    "statusCode": 200,
    "queueName": "queue2",
    "offset": 3
}
```
offset: offset number of the produced message.
status: 200 on success/400 on error

### Message Consume 
```sh
curl --location --request GET 'http://localhost:8080/v1/consume/msg/<consumer_name>/<queue_name>' \
--header 'Content-Type: application/json'
```
###### Response:
```
{
    "statusCode": 200,
    "queueName": "queue2",
    "offset": 4,
    "jsonObject": {
        "key": "value1"
    }
}
```
jsonObject: msg to be consumed. 
status: 200 on success/400 on error

> Note: `jsonObject/offset/queueName` will be null in case of all msg consumed/no message for consumption/queue doesn't exists.

### Message Consume From Offset (Response is same as Message Consume)
```sh
curl --location --request GET 'http://localhost:8080/v1/consume/msg/<consumer_name>/<queue_name>/<offset>' \
--header 'Content-Type: application/json'
```

