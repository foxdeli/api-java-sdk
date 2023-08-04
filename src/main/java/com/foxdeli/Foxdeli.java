package com.foxdeli;

import com.foxdeli.exception.FoxdeliApiException;
import com.foxdeli.exception.FoxdeliAuthenticationException;
import com.foxdeli.exception.FoxdeliException;
import com.foxdeli.helper.AuthHelper;
import com.foxdeli.interceptor.AuthInterceptor;
import com.foxdeli.interceptor.ErrorHandlingInterceptor;
import com.foxdeli.interceptor.FoxdeliAuthenticator;
import com.foxdeli.mapper.OrderMapper;
import com.foxdeli.mapper.ParcelMapper;
import com.foxdeli.model.request.OrderRequest;
import com.foxdeli.model.request.ParcelRequest;
import com.foxdeli.model.response.Order;
import com.foxdeli.model.response.Parcel;
import com.foxdeli.orders.ApiClient;
import com.foxdeli.orders.ApiException;
import com.foxdeli.orders.api.OrderV1Api;
import com.foxdeli.orders.api.ParcelV1Api;
import com.foxdeli.orders.api.model.DeliveryState;
import com.foxdeli.orders.api.model.OrderRegistration;
import com.foxdeli.orders.api.model.OrderUpdate;
import com.foxdeli.orders.api.model.ParcelRegistration;
import com.foxdeli.orders.api.model.ParcelStateUpdate;
import com.foxdeli.orders.api.model.ParcelUpdate;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

import static com.foxdeli.constant.Constant.STAGE_PATH_TRACKING;

/**
 * The `Foxdeli` class provides a set of static methods to interact with the Foxdeli API for managing orders and parcels.
 * It offers methods for initializing the API, creating, updating, and canceling orders, and managing parcels.
 * Before using any of the API methods, the `init` method must be called to set up the necessary authentication and API client.
 */
@Slf4j
public class Foxdeli {

    /**
     * Private constructor to prevent instantiation of the `Foxdeli` class.
     * This class only contains static methods, and it should not be instantiated directly.
     */
    private Foxdeli() {
    }

    private static UUID eshopId;
    private static UUID marketId;
    private static OrderV1Api ordersApi;
    private static ParcelV1Api parcelsApi;
    private static final OrderMapper orderMapper = Mappers.getMapper(OrderMapper.class);
    private static final ParcelMapper parcelMapper = Mappers.getMapper(ParcelMapper.class);

    /**
     * Initializes the `Foxdeli` class with the given username and password.
     * This method should be called before using any other API methods.
     *
     * @param username The username to authenticate with the Foxdeli API.
     * @param password The password to authenticate with the Foxdeli API.
     * @throws FoxdeliAuthenticationException If the API call to get access token fails.
     */
    public static void init(String username, String password) {
        init(username, password, false);
    }

    /**
     * Initializes the `Foxdeli` class with the given username and password and specify if you want to use `stage` env.
     * This method should be called before using any other API methods.
     *
     * @param username The username to authenticate with the Foxdeli API.
     * @param password The password to authenticate with the Foxdeli API.
     * @param stage If true, SDK will connect to `stage` env. If false, SDK will connect to `prod` env.
     * @throws FoxdeliAuthenticationException If the API call to get access token fails.
     */
    public static void init(String username, String password, boolean stage) {
        AuthHelper.setUsername(username);
        AuthHelper.setPassword(password);
        AuthHelper.initTokenApi(stage);
        AuthHelper.authorize();
        initApi(stage);
    }

    /**
     * Initializes the `Foxdeli` class with the given username, password, eshopId, and marketId.
     * This method sets up the API client with the provided credentials and identifiers.
     *
     * @param username The username to authenticate with the Foxdeli API.
     * @param password The password to authenticate with the Foxdeli API.
     * @param eshopId  The unique identifier for the eshop.
     * @param marketId The unique identifier for the market.
     * @throws FoxdeliAuthenticationException If the API call to get access token fails.
     */
    public static void init(String username, String password, UUID eshopId, UUID marketId) {
        init(username, password, eshopId, marketId, false);
    }

    /**
     * Initializes the `Foxdeli` class with the given username, password, eshopId, and marketId and specify if you want to use `stage` env.
     * This method sets up the API client with the provided credentials and identifiers.
     *
     * @param username The username to authenticate with the Foxdeli API.
     * @param password The password to authenticate with the Foxdeli API.
     * @param eshopId  The unique identifier for the eshop.
     * @param marketId The unique identifier for the market.
     * @param stage If true, SDK will connect to `stage` env. If false, SDK will connect to `prod` env.
     * @throws FoxdeliAuthenticationException If the API call to get access token fails.
     */
    public static void init(String username, String password, UUID eshopId, UUID marketId, boolean stage) {
        setEshopId(eshopId);
        setMarketId(marketId);
        init(username, password, stage);
    }

    /**
     * Sets the default eshopId for the `Foxdeli` class.
     *
     * @param eshopId The unique identifier for the eshop.
     * @throws FoxdeliException If the provided eshopId is null.
     */
    public static void setEshopId(UUID eshopId) {
        if (eshopId == null) {
            throw new FoxdeliException("EshopId cannot be null");
        }
        Foxdeli.eshopId = eshopId;
    }

    /**
     * Sets the marketId for the `Foxdeli` class.
     *
     * @param marketId The unique identifier for the market.
     * @throws FoxdeliException If the provided marketId is null.
     */
    public static void setMarketId(UUID marketId) {
        if (marketId == null) {
            throw new FoxdeliException("MarketId cannot be null");
        }
        Foxdeli.marketId = marketId;
    }

    /**
     * Creates an order using the provided `OrderRequest`.
     * If the request also contains parcels, method creates parcels and returns the final Order object with created Parcels.
     *
     * @param request  The `OrderRequest` object representing the details of the order.
     * @param marketId The unique identifier for the market to associate the order with.
     * @param eshopId  The unique identifier for the eshop to associate the order with.
     * @return The created `Order` object.
     * @throws FoxdeliApiException If the API call to create the order fails.
     */
    public static Order createOrder(OrderRequest request, UUID marketId, UUID eshopId) {
        OrderRegistration orderRegistration = orderMapper.map(request, marketId, eshopId);
        try {
            com.foxdeli.orders.api.model.Order order = ordersApi.createOrder(orderRegistration);
            Order result = orderMapper.map(order);
            if (request.parcels() == null || request.parcels().isEmpty()) {
                return result;
            } else {
                request.parcels().forEach(parcel -> {
                    try {
                        Foxdeli.createParcel(result.orderId(), parcel);
                    } catch (FoxdeliApiException e) {
                        log.error(e.getMessage(), e);
                    }
                });
                return Foxdeli.getOrder(result.orderId());
            }
        } catch (ApiException e) {
            throw new FoxdeliApiException("Foxdeli 'create order' API call failed.", e);
        }
    }

    /**
     * Creates an order using the provided `OrderRequest` and the default eshopId set in the `Foxdeli` class.
     *
     * @param request  The `OrderRequest` object representing the details of the order.
     * @param marketId The unique identifier for the market to associate the order with.
     * @return The created `Order` object.
     * @throws FoxdeliApiException If the API call to create the order fails.
     */
    public static Order createOrder(OrderRequest request, UUID marketId) {
        return createOrder(request, marketId, Foxdeli.eshopId);
    }

    /**
     * Creates an order using the provided `OrderRequest` and the default eshopId and marketId set in the `Foxdeli` class.
     *
     * @param request The `OrderRequest` object representing the details of the order.
     * @return The created `Order` object.
     * @throws FoxdeliApiException If the API call to create the order fails.
     */
    public static Order createOrder(OrderRequest request) {
        return createOrder(request, Foxdeli.marketId, Foxdeli.eshopId);
    }

    /**
     * Retrieves an order with the given orderId.
     *
     * @param orderId The unique identifier for the order to retrieve.
     * @return The retrieved `Order` object.
     * @throws FoxdeliApiException If the API call to retrieve the order fails.
     */
    public static Order getOrder(UUID orderId) {
        try {
            com.foxdeli.orders.api.model.Order order = ordersApi.findOrderById(orderId.toString());
            return orderMapper.map(order);
        } catch (ApiException e) {
            throw new FoxdeliApiException("Foxdeli 'get order' API call failed.", e);
        }
    }

    /**
     * Updates an existing order with the provided `OrderRequest` and orderId.
     *
     * @param request The `OrderRequest` object representing the updated details of the order.
     * @param orderId The unique identifier for the order to update.
     * @return The updated `Order` object.
     * @throws FoxdeliApiException If the API call to update the order fails.
     */
    public static Order updateOrder(OrderRequest request, UUID orderId) {
        OrderUpdate orderUpdate = orderMapper.map(request);
        try {
            com.foxdeli.orders.api.model.Order order = ordersApi.updateOrder(orderId.toString(), orderUpdate);
            return orderMapper.map(order);
        } catch (ApiException e) {
            throw new FoxdeliApiException("Foxdeli 'update order' API call failed.", e);
        }
    }

    /**
     * Cancels an order with the given orderId.
     *
     * @param orderId The unique identifier for the order to cancel.
     * @return The canceled `Order` object.
     * @throws FoxdeliApiException If the API call to cancel the order fails.
     */
    public static Order cancelOrder(UUID orderId) {
        try {
            com.foxdeli.orders.api.model.Order order = ordersApi.cancelOrder(orderId.toString());
            return orderMapper.map(order);
        } catch (ApiException e) {
            throw new FoxdeliApiException("Foxdeli 'cancel order' API call failed.", e);
        }
    }

    /**
     * Creates a parcel for the specified orderId using the provided `ParcelRequest`.
     *
     * @param orderId The unique identifier for the order to associate the parcel with.
     * @param request The `ParcelRequest` object representing the details of the parcel.
     * @return The created `Parcel` object.
     * @throws FoxdeliApiException If the API call to create the parcel fails.
     */
    public static Parcel createParcel(UUID orderId, ParcelRequest request) {
        ParcelRegistration parcelRegistration = parcelMapper.mapToCreate(request);
        try {
            com.foxdeli.orders.api.model.Parcel parcel = parcelsApi.createParcel(orderId.toString(), parcelRegistration);
            return parcelMapper.map(parcel);
        } catch (ApiException e) {
            throw new FoxdeliApiException("Foxdeli 'create parcel' API call failed.", e);
        }
    }

    /**
     * Deletes a parcel with the given parcelId associated with the orderId.
     *
     * @param orderId  The unique identifier for the order containing the parcel to delete.
     * @param parcelId The unique identifier for the parcel to delete.
     * @throws FoxdeliApiException If the API call to delete the parcel fails.
     */
    public static void deleteParcel(UUID orderId, UUID parcelId) {
        try {
            parcelsApi.deleteParcel(orderId.toString(), parcelId.toString());
        } catch (ApiException e) {
            throw new FoxdeliApiException("Foxdeli 'delete parcel' API call failed.", e);
        }
    }

    /**
     * Retrieves a parcel with the given parcelId associated with the orderId.
     *
     * @param orderId  The unique identifier for the order containing the parcel to retrieve.
     * @param parcelId The unique identifier for the parcel to retrieve.
     * @return The retrieved `Parcel` object.
     * @throws FoxdeliApiException If the API call to retrieve the parcel fails.
     */
    public static Parcel getParcel(UUID orderId, UUID parcelId) {
        try {
            com.foxdeli.orders.api.model.Parcel parcel = parcelsApi.findParcelById(orderId.toString(), parcelId.toString());
            return parcelMapper.map(parcel);
        } catch (ApiException e) {
            throw new FoxdeliApiException("Foxdeli 'get parcel' API call failed.", e);
        }
    }

    /**
     * Updates an existing parcel with the provided `ParcelRequest` and parcelId associated with the orderId.
     *
     * @param orderId  The unique identifier for the order containing the parcel to update.
     * @param parcelId The unique identifier for the parcel to update.
     * @param request  The `ParcelRequest` object representing the updated details of the parcel.
     * @return The updated `Parcel` object.
     * @throws FoxdeliApiException If the API call to update the parcel fails.
     */
    public static Parcel updateParcel(UUID orderId, UUID parcelId, ParcelRequest request) {
        ParcelUpdate parcelUpdate = parcelMapper.mapToUpdate(request);
        try {
            com.foxdeli.orders.api.model.Parcel parcel = parcelsApi.updateParcel(orderId.toString(), parcelId.toString(), parcelUpdate);
            return parcelMapper.map(parcel);
        } catch (ApiException e) {
            throw new FoxdeliApiException("Foxdeli 'update parcel' API call failed.", e);
        }
    }

    /**
     * Updates the delivery state of a parcel with the provided parcelId and orderId.
     *
     * @param orderId       The unique identifier for the order containing the parcel to update.
     * @param parcelId      The unique identifier for the parcel to update.
     * @param deliveryState The new delivery state of the parcel.
     * @return The updated `Parcel` object.
     * @throws FoxdeliApiException If the API call to update the parcel state fails.
     */
    public static Parcel updateParcelState(UUID orderId, UUID parcelId, DeliveryState deliveryState) {
        ParcelStateUpdate parcelStateUpdate = parcelMapper.map(deliveryState);
        try {
            com.foxdeli.orders.api.model.Parcel parcel = parcelsApi.updateParcelState(orderId.toString(), parcelId.toString(), parcelStateUpdate);
            return parcelMapper.map(parcel);
        } catch (ApiException e) {
            throw new FoxdeliApiException("Foxdeli 'update parcel state' API call failed.", e);
        }
    }

    /**
     * Initializes the Foxdeli API client with the provided username and password, and sets up the required interceptors.
     * This method should be called internally during initialization.
     */
    private static void initApi(boolean stage) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor())
                .addInterceptor(new ErrorHandlingInterceptor())
                .addInterceptor(new HttpLoggingInterceptor())
                .authenticator(new FoxdeliAuthenticator())
                .build();
        ApiClient apiClient = new ApiClient(okHttpClient);
        if (stage) {
            apiClient.setBasePath(STAGE_PATH_TRACKING);
        }
        ordersApi = new OrderV1Api(apiClient);
        parcelsApi = new ParcelV1Api(apiClient);
    }
}
