package com.hdfclife.observer;

import com.hdfclife.model.Claim;
import java.util.ArrayList;
import java.util.List;

public class ClaimEventPublisher {
    private final List<ClaimObserver> observers = new ArrayList<>();

    public void register(ClaimObserver observer) {
        observers.add(observer);
    }

    public void notifyObservers(Claim claim) {
        for (ClaimObserver observer : observers) {
            observer.onClaimUpdate(claim);
        }
    }
}
