package com.hdfclife.service;

import com.hdfclife.config.AppConfig;
import com.hdfclife.exception.InvalidClaimException;
import com.hdfclife.model.Claim;
import com.hdfclife.observer.ClaimEventPublisher;
import java.util.PriorityQueue;

public class ClaimService {
    private final PriorityQueue<Claim> claimQueue = new PriorityQueue<>();
    private final ClaimEventPublisher eventPublisher = new ClaimEventPublisher();

    public ClaimEventPublisher getEventPublisher() {
        return eventPublisher;
    }

    public Claim fileClaim(String policyNo, long amount, com.hdfclife.model.Urgency urgency, String hospital, String remarks) {
        if (amount <= 0 || amount > AppConfig.INSTANCE.getMaxClaimAmount()) {
            throw new InvalidClaimException("Claim amount " + amount + " is invalid. Must be between 1 and " + AppConfig.INSTANCE.getMaxClaimAmount());
        }

        Claim claim = new Claim.Builder(policyNo, amount, urgency)
                .hospitalName(hospital)
                .remarks(remarks)
                .build();

        claimQueue.offer(claim);

        try (AuditLogger logger = new AuditLogger("audit.log")) {
            logger.logClaim(claim);
        }

        return claim;
    }

    public void updateClaimStatus(Claim claim, String newStatus) {
        claim.updateStatus(newStatus);
        eventPublisher.notifyObservers(claim);
    }

    public PriorityQueue<Claim> getClaimQueue() {
        return claimQueue;
    }
}
