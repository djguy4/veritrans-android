package com.midtrans.sdk.corekit.core.grouppayment;

import com.midtrans.sdk.corekit.base.callback.MidtransCallback;
import com.midtrans.sdk.corekit.core.snap.model.pay.response.BasePaymentResponse;
import com.midtrans.sdk.corekit.utilities.Validation;

public class MidtransCardlessCredit extends BaseMidtransGroupPayments {

    /**
     * Start payment using bank transfer and va with Akulaku.
     *
     * @param snapToken token after making checkoutWithTransaction.
     * @param callback  for receiving callback from request.
     */
    public static void paymentUsingAkulaku(final String snapToken,
                                           final MidtransCallback<BasePaymentResponse> callback) {
        if (Validation.isValidForNetworkCall(getSdkContext(), callback)) {
            getSnapApiManager().paymentUsingAkulaku(snapToken, callback);
        }
    }
}
