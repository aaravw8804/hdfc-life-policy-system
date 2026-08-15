package com.hdfclife.observer;

import com.hdfclife.model.Claim;

public interface ClaimObserver {
    void onClaimUpdate(Claim claim);
}
