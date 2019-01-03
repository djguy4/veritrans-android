package com.midtrans.sdk.corekit.core.midtrans;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.midtrans.sdk.corekit.base.callback.MidtransCallback;
import com.midtrans.sdk.corekit.base.network.BaseServiceManager;
import com.midtrans.sdk.corekit.core.midtrans.response.CardRegistrationResponse;
import com.midtrans.sdk.corekit.core.midtrans.response.TokenDetailsResponse;
import com.midtrans.sdk.corekit.utilities.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ziahaqi on 3/27/18.
 */

public class MidtransServiceManager extends BaseServiceManager {
    // private static final String TAG = MerchantServiceManager.class.getSimpleName();
    private MidtransApiService service;

    public MidtransServiceManager(MidtransApiService service) {
        this.service = service;
    }

    /**
     * It will execute API call to get token from Veritrans that can be used later.
     *
     * @param cardNumber   credit card number
     * @param cardCvv      credit card CVV number
     * @param cardExpMonth credit card expired month
     * @param cardExpYear  credit card expired year
     * @param callback     card transaction callback
     */
    public void cardRegistration(String cardNumber,
                                 String cardCvv,
                                 String cardExpMonth,
                                 String cardExpYear, String clientKey,
                                 final MidtransCallback<CardRegistrationResponse> callback) {
        if (service == null) {
            doOnApiServiceUnAvailable(callback);
            return;
        }

        Call<CardRegistrationResponse> call = service.registerCard(cardNumber, cardCvv, cardExpMonth, cardExpYear, clientKey);
        call.enqueue(new Callback<CardRegistrationResponse>() {
            @Override
            public void onResponse(@NonNull Call<CardRegistrationResponse> call, @NonNull Response<CardRegistrationResponse> response) {
                releaseResources();
                CardRegistrationResponse cardRegistrationResponse = response.body();
                if (cardRegistrationResponse != null) {
                    String statusCode = cardRegistrationResponse.getStatusCode();
                    if (!TextUtils.isEmpty(statusCode) && statusCode.equals(Constants.STATUS_CODE_200)) {
                        callback.onSuccess(cardRegistrationResponse);
                    } else {
                        callback.onFailed(new Throwable(cardRegistrationResponse.getStatusMessage()));
                    }
                } else {
                    callback.onFailed(new Throwable(Constants.MESSAGE_ERROR_EMPTY_RESPONSE));
                }
            }

            @Override
            public void onFailure(Call<CardRegistrationResponse> call, Throwable t) {
                handleServerResponse(null, callback, t);
            }
        });
    }

    /**
     * It will execute an api call to get token from server, and after completion of request it
     *
     * @param cardTokenRequest information about credit card.
     * @param callback         get creditcard token callback
     */
    public void getToken(CardTokenRequest cardTokenRequest, final MidtransCallback<TokenDetailsResponse> callback) {

        if (service == null) {
            doOnApiServiceUnAvailable(callback);
            return;
        }

        Call<TokenDetailsResponse> call;
        if (cardTokenRequest.isTwoClick()) {

            if (cardTokenRequest.isInstallment()) {
                call = service.getTokenInstalmentOfferTwoClick(
                        cardTokenRequest.getCardCVV(),
                        cardTokenRequest.getSavedTokenId(),
                        cardTokenRequest.isTwoClick(),
                        cardTokenRequest.isSecure(),
                        cardTokenRequest.getGrossAmount(),
                        cardTokenRequest.getBank(),
                        cardTokenRequest.getClientKey(),
                        cardTokenRequest.isInstallment(),
                        cardTokenRequest.getFormattedInstalmentTerm(),
                        cardTokenRequest.getChannel(),
                        cardTokenRequest.getType(),
                        cardTokenRequest.getCurrency(),
                        cardTokenRequest.isPoint());
            } else {
                call = service.getTokenTwoClick(
                        cardTokenRequest.getCardCVV(),
                        cardTokenRequest.getSavedTokenId(),
                        cardTokenRequest.isTwoClick(),
                        cardTokenRequest.isSecure(),
                        cardTokenRequest.getGrossAmount(),
                        cardTokenRequest.getBank(),
                        cardTokenRequest.getClientKey(),
                        cardTokenRequest.getChannel(),
                        cardTokenRequest.getType(),
                        cardTokenRequest.getCurrency(),
                        cardTokenRequest.isPoint());
            }

        } else {
            if (cardTokenRequest.isInstallment()) {

                call = service.get3DSTokenInstalmentOffers(cardTokenRequest.getCardNumber(),
                        cardTokenRequest.getCardCVV(),
                        cardTokenRequest.getCardExpiryMonth(), cardTokenRequest
                                .getCardExpiryYear(),
                        cardTokenRequest.getClientKey(),
                        cardTokenRequest.getBank(),
                        cardTokenRequest.isSecure(),
                        cardTokenRequest.isTwoClick(),
                        cardTokenRequest.getGrossAmount(),
                        cardTokenRequest.isInstallment(),
                        cardTokenRequest.getChannel(),
                        cardTokenRequest.getFormattedInstalmentTerm(),
                        cardTokenRequest.getType(),
                        cardTokenRequest.getCurrency(),
                        cardTokenRequest.isPoint());

            } else {
                //normal request
                if (!cardTokenRequest.isSecure()) {

                    call = service.getToken(
                            cardTokenRequest.getCardNumber(),
                            cardTokenRequest.getCardCVV(),
                            cardTokenRequest.getCardExpiryMonth(),
                            cardTokenRequest.getCardExpiryYear(),
                            cardTokenRequest.getClientKey(),
                            cardTokenRequest.getGrossAmount(),
                            cardTokenRequest.getChannel(),
                            cardTokenRequest.getType(),
                            cardTokenRequest.getCurrency(),
                            cardTokenRequest.isPoint());
                } else {
                    call = service.get3DSToken(cardTokenRequest.getCardNumber(),
                            cardTokenRequest.getCardCVV(),
                            cardTokenRequest.getCardExpiryMonth(),
                            cardTokenRequest.getCardExpiryYear(),
                            cardTokenRequest.getClientKey(),
                            cardTokenRequest.getBank(),
                            cardTokenRequest.isSecure(),
                            cardTokenRequest.isTwoClick(),
                            cardTokenRequest.getGrossAmount(),
                            cardTokenRequest.getChannel(),
                            cardTokenRequest.getType(),
                            cardTokenRequest.getCurrency(),
                            cardTokenRequest.isPoint());
                }
            }

        }

        call.enqueue(new Callback<TokenDetailsResponse>() {
            @Override
            public void onResponse(Call<TokenDetailsResponse> call, Response<TokenDetailsResponse> response) {
                handleServerResponse(response, callback, null);
            }

            @Override
            public void onFailure(Call<TokenDetailsResponse> call, Throwable t) {
                handleServerResponse(null, callback, t);
            }
        });
    }
}
