# Foxdeli Java API Client

The `Foxdeli` client is a Java client for the Foxdeli API, allowing you to interact with the API to manage parcels and
orders.

## Documentation

The documentation for the Foxdeli API can be found [here](https://api.foxdeli.com/).

[//]: #TODO (The Java library documentation can be found [here]&#40;&#41;. )

## Installation

twilio-java uses Maven. At present the jars are available from a public maven repository.

Use the following dependency in your project to grab via Maven:

```xml
<dependency>
  <groupId>com.foxdeli</groupId>
  <artifactId>foxdeli-java-sdk</artifactId>
  <version>X.X.X</version>
  <scope>compile</scope>
</dependency>
```

or Gradle:

```json
implementation "com.foxdeli:foxdeli-java-sdk:X.X.X"
```

## Initialization

To start using the Foxdeli API, you need to initialize the `Foxdeli` class with your authentication credentials. There
are two methods available for initialization:

1. With only username and password:

```java
String username = "your_username";
String password = "your_password";
Foxdeli.init(username, password);
```

2. With username, password, eshopId, and marketId:

```java
String username = "your_username";
String password = "your_password";
UUID eshopId = UUID.fromString("your_eshop_id");
UUID marketId = UUID.fromString("your_market_id");
Foxdeli.init(username, password, eshopId, marketId);
```

3. Both above mentioned methods can be used with additional boolean param `stage`. This can be used in case of testing your app against stage environment. The default value is `false`.

```java
...
Foxdeli.init(username, password, stage);
```
or
```java
...
Foxdeli.init(username, password, eshopId, marketId, stage);
```
## Managing Orders

The `Foxdeli` client provides methods to create, retrieve, update, and cancel orders.

### Create an Order

You can create an order using different methods with varying parameters. For example:

1. Create an order with full details:

```java
OrderRequest orderRequest = OrderRequest.builder().build(); // Replace this with actual order details
UUID marketId = UUID.fromString("your_market_id");
UUID eshopId = UUID.fromString("your_eshop_id");
Order createdOrder = Foxdeli.createOrder(orderRequest, marketId, eshopId);
```

2. Create an order with default `eshopId` but custom `marketId` (requires prior initialization):

```java
OrderRequest orderRequest = OrderRequest.builder().build(); // Replace this with actual order details
UUID marketId = UUID.fromString("your_market_id"); // Only required if not initialized with eshopId and marketId
Order createdOrder = Foxdeli.createOrder(orderRequest, marketId);
```

3. Create an order with default `marketId` and `eshopId` (requires prior initialization):

```java
OrderRequest orderRequest = OrderRequest.builder().build(); // Replace this with actual order details
Order createdOrder = Foxdeli.createOrder(orderRequest);
```

### Retrieve an Order

You can retrieve an order using its `orderId`:

```java
UUID orderId = UUID.fromString("your_order_id");
Order retrievedOrder = Foxdeli.getOrder(orderId);
```

### Update an Order

To update an existing order, provide the updated order details and the `orderId`:

```java
OrderRequest updatedOrderRequest = OrderRequest.builder().build(); // Replace this with actual updated order details
UUID orderId = UUID.fromString("your_order_id");
Order updatedOrder = Foxdeli.updateOrder(updatedOrderRequest, orderId);
```

### Cancel an Order

You can cancel an order by providing its `orderId`:

```java
UUID orderId = UUID.fromString("your_order_id");
Order canceledOrder = Foxdeli.cancelOrder(orderId);
```

## Managing Parcels

The `Foxdeli` client also provides methods to create, retrieve, update, and delete parcels.

### Create a Parcel

To create a parcel, provide the `orderId` and the `ParcelRequest` object with parcel details:

```java
UUID orderId = UUID.fromString("your_order_id");
ParcelRequest parcelRequest = ParcelRequest.builder().build(); // Replace this with actual parcel details
Parcel createdParcel = Foxdeli.createParcel(orderId, parcelRequest);
```

### Retrieve a Parcel

To retrieve a parcel, provide the `orderId` and the `parcelId`:

```java
UUID orderId = UUID.fromString("your_order_id");
UUID parcelId = UUID.fromString("your_parcel_id");
Parcel retrievedParcel = Foxdeli.getParcel(orderId, parcelId);
```

### Update a Parcel

To update an existing parcel, provide the `orderId`, `parcelId`, and the `ParcelRequest` object with updated parcel
details:

```java
UUID orderId = UUID.fromString("your_order_id");
UUID parcelId = UUID.fromString("your_parcel_id");
ParcelRequest updatedParcelRequest = ParcelRequest.builder().build(); // Replace this with actual updated parcel details
Parcel updatedParcel = Foxdeli.updateParcel(orderId, parcelId, updatedParcelRequest);
```

### Delete a Parcel

To delete a parcel, provide the `orderId` and the `parcelId`:

```java
UUID orderId = UUID.fromString("your_order_id");
UUID parcelId = UUID.fromString("your_parcel_id");
Foxdeli.deleteParcel(orderId, parcelId);
```

## Managing orders and parcels at once

Methods mentioned above can also create orders and parcels in one simple method call.

### Create an Order and a Parcel

To create an order with a parcel at one request, use one of the `createOrder` available methods. In the request specify
one or more parcels that should be created and linked to the order.

```java
OrderRequest orderRequest = OrderRequest.builder()
        .parcels(List.of(ParcelRequest.builder().build()))
        .build();
Order createdOrder = Foxdeli.createOrder(orderRequest);
```