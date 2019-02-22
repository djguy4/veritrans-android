package com.midtrans.sdk.uikit.view.banktransfer.instruction.instruction.fragment;

import android.os.Bundle;

import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.PaymentInfoResponse;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.utilities.Constants;

public class InstructionVaMandiriFragment extends VaInstructionFragment {

    public static InstructionVaMandiriFragment newInstance(int code, String title, PaymentInfoResponse paymentInfoResponse) {
        InstructionVaMandiriFragment fragment = new InstructionVaMandiriFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(INSTRUCTION_POSITION, code);
        bundle.putString(INSTRUCTION_TITLE, title);
        bundle.putSerializable(MERCHANT_INFORMATION, paymentInfoResponse);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int initLayoutId() {
        int instructionCode = getFragmentCode();
        int layoutId = instructionCode;

        switch (instructionCode) {
            case Constants.INSTRUCTION_FIRST_POSITION:
                layoutId = R.layout.fragment_payment_instruction_bank_transfer_mandiri;
                break;

            case Constants.INSTRUCTION_SECOND_POSITION:
                layoutId = R.layout.fragment_payment_instruction_bank_transfer_mandiri_internet;
                break;
        }
        return layoutId;
    }
}