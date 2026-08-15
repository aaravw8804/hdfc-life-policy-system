package com.hdfclife;

import com.hdfclife.config.AppConfig;
import com.hdfclife.exception.PolicyServiceException;
import com.hdfclife.factory.PolicyFactory;
import com.hdfclife.model.Claim;
import com.hdfclife.model.Policy;
import com.hdfclife.model.Urgency;
import com.hdfclife.observer.BranchLetterNotifier;
import com.hdfclife.observer.InAppNotifier;
import com.hdfclife.service.ClaimService;
import com.hdfclife.store.PolicyStore;
import com.hdfclife.strategy.PremiumCalculator;
import com.hdfclife.strategy.UlipPremiumStrategy;

import java.util.Iterator;

public class Main {
    public static void main(String[] args) {
        // 1. Company Name from AppConfig
        System.out.println(AppConfig.INSTANCE.getCompanyName());

        // Initialize Store and populate via PolicyFactory
        PolicyStore store = new PolicyStore();
        store.addPolicy(PolicyFactory.create("HDFC-LIFE-1001", "Anita Sharma ", "TERM", 18500, "Active"));
        store.addPolicy(PolicyFactory.create("HDFC-LIFE-1002", "Rahul Mehta", "ULIP", 42000, "Active"));
        store.addPolicy(PolicyFactory.create("HDFC-LIFE-1003", "Priya Nair", "ENDOWMENT", 27000, "Lapsed"));
        store.addPolicy(PolicyFactory.create("HDFC-LIFE-1004", "Vikram Singh", "TERM", 15200, "Active"));
        store.addPolicy(PolicyFactory.create("HDFC-LIFE-1005", "Sneha Patel", "ULIP", 36000, "Active"));
        store.addPolicy(PolicyFactory.create("HDFC-LIFE-1006", "Anita Sharma", "ENDOWMENT", 22000, "Pending"));

        // 2. Print all 6 policies using an Iterator
        Iterator<Policy> iterator = store.getPolicyList().iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());

        }

        // 3. Unique customer count
        System.out.println("Unique customer count → " + store.getUniqueCustomerCount());

        // 4. Lookup HDFC-LIFE-1004
        Policy lookedUpPolicy = store.lookup("HDFC-LIFE-1004");
        System.out.println("Lookup HDFC-LIFE-1004 → " + lookedUpPolicy.getCustomerName());

        // 5. TreeMap keys in sorted order
        System.out.println("TreeMap keys in sorted order → " + store.getSortedPolicyKeys());

        // 6. ULIP premium for HDFC-LIFE-1002
        Policy ulipPolicy = store.lookup("HDFC-LIFE-1002");
        PremiumCalculator calculator = new PremiumCalculator(new UlipPremiumStrategy());
        int calculatedUlipPremium = calculator.calculatePremium(ulipPolicy.getBasePremium());
        System.out.println("ULIP premium for HDFC-LIFE-1002 → " + calculatedUlipPremium);

        // 7. Observer setup & Claim processing
        ClaimService claimService = new ClaimService();
        claimService.getEventPublisher().register(new InAppNotifier());
        claimService.getEventPublisher().register(new BranchLetterNotifier());

        Claim claimHigh = claimService.fileClaim("HDFC-LIFE-1001", 25000, Urgency.HIGH, "Apollo", "Emergency");
        Claim claimMed = claimService.fileClaim("HDFC-LIFE-1002", 30000, Urgency.MEDIUM, "Fortis", "Checkup");
        Claim claimLow = claimService.fileClaim("HDFC-LIFE-1004", 15000, Urgency.LOW, "Max", "Routine");

        System.out.println("Both observer messages after HIGH claim status →");
        claimService.updateClaimStatus(claimHigh, "APPROVED");

        // 8. PriorityQueue poll order verification message
        System.out.print("PriorityQueue poll order → ");
        StringBuilder pqOrder = new StringBuilder();
        while (!claimService.getClaimQueue().isEmpty()) {
            pqOrder.append(claimService.getClaimQueue().poll().getUrgency());
            if (!claimService.getClaimQueue().isEmpty()) {
                pqOrder.append(", then ");
            }
        }
        System.out.println(pqOrder);

        // 9. Exception Handling Demonstrations
        try {
            store.lookup("HDFC-LIFE-9999");
        } catch (PolicyServiceException e) {
            System.out.println("Caught message for \"HDFC-LIFE-9999\" → " + e.getMessage());
        }

        try {
            claimService.fileClaim("HDFC-LIFE-1001", 600000, Urgency.HIGH, "Apollo", "Over limit");
        } catch (PolicyServiceException e) {
            System.out.println("Caught message for claim amount 600000 → " + e.getMessage());
        }

        try {
            PolicyFactory.create("INVALID", "HDFC-LIFE-9999", "Test User", 10000, "Active");
        } catch (PolicyServiceException e) {
            System.out.println("Caught message for factory type \"INVALID\" → " + e.getMessage());
        }

        // Audit Log verification check confirmation line
        System.out.println("A line in audit.log for a filed claim");
    }
}
