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
import com.hdfclife.strategy.*;

import java.util.Iterator;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // 1. Company Name
        System.out.println(AppConfig.INSTANCE.getCompanyName());
        System.out.println("-------------------------------------------------------------");

        PolicyStore store = new PolicyStore();

        // ----------------------
        //  DYNAMIC POLICY ENTRY
        // ----------------------
        System.out.print("How many policies do you want to enter? ");
        int n = Integer.parseInt(sc.nextLine());

        for (int i = 0; i < n; i++) {
            System.out.println("\nEnter details for Policy " + (i + 1));

            System.out.print("Policy ID: ");
            String id = sc.nextLine();

            System.out.print("Customer Name: ");
            String customer = sc.nextLine();

            System.out.print("Policy Type (TERM/ULIP/ENDOWMENT): ");
            String type = sc.nextLine().toUpperCase();

            System.out.print("Base Premium: ");
            int premium = Integer.parseInt(sc.nextLine());

            System.out.print("Status (Active/Lapsed/Pending): ");
            String status = sc.nextLine();

            try {
                store.addPolicy(PolicyFactory.create(id, customer, type, premium, status));
            } catch (PolicyServiceException e) {
                System.out.println("Error adding policy → " + e.getMessage());
            }
        }

        // ----------------------
        // List all policies
        //-----------------------
        System.out.println("\nAll Policies Entered:");
        Iterator<Policy> it = store.getPolicyList().iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }

        // Unique customers
        System.out.println("\nUnique customer count → " + store.getUniqueCustomerCount());

        // Lookup
        System.out.print("\nEnter a Policy ID to lookup: ");
        String lookupId = sc.nextLine();
        try {
            Policy lookedUp = store.lookup(lookupId);
            System.out.println("Customer for " + lookupId + " → " + lookedUp.getCustomerName());
        } catch (PolicyServiceException e) {
            System.out.println("Lookup Error → " + e.getMessage());
        }

        // Sorted keys
        System.out.println("Sorted Policy IDs → " + store.getSortedPolicyKeys());

        // ----------------------
        // DYNAMIC PREMIUM CALCULATION
        // ----------------------
        System.out.print("\nEnter any Policy ID to calculate premium: ");
        String pid = sc.nextLine();

        try {
            Policy p = store.lookup(pid);

            PremiumCalculator calc;

            switch (p.getType().toUpperCase()) {
                case "ULIP":
                    calc = new PremiumCalculator(new UlipPremiumStrategy());
                    break;
                case "TERM":
                    calc = new PremiumCalculator(new TermPremiumStrategy());
                    break;
                case "ENDOWMENT":
                    calc = new PremiumCalculator(new EndowmentPremiumStrategy());
                    break;
                default:
                    throw new IllegalArgumentException("Unknown policy type: " + p.getType());
            }

            int result = calc.calculatePremium(p.getBasePremium());
            System.out.println("Premium for " + pid + " (" + p.getType() + ") → " + result);

        } catch (PolicyServiceException e) {
            System.out.println("Premium calculation error → " + e.getMessage());
        }

        // ----------------------
        // CLAIM PROCESSING (Dynamic)
        // ----------------------
        ClaimService claimService = new ClaimService();
        claimService.getEventPublisher().register(new InAppNotifier());
        claimService.getEventPublisher().register(new BranchLetterNotifier());

        System.out.print("\nHow many claims do you want to file? ");
        int c = Integer.parseInt(sc.nextLine());

        for (int i = 0; i < c; i++) {
            System.out.println("\nEnter Claim " + (i + 1));

            System.out.print("Policy ID: ");
            String policyId = sc.nextLine();

            System.out.print("Claim Amount: ");
            int amount = Integer.parseInt(sc.nextLine());

            System.out.print("Urgency (HIGH/MEDIUM/LOW): ");
            Urgency urgency = Urgency.valueOf(sc.nextLine().toUpperCase());

            System.out.print("Hospital Name: ");
            String hospital = sc.nextLine();

            System.out.print("Reason: ");
            String reason = sc.nextLine();

            try {
                claimService.fileClaim(policyId, amount, urgency, hospital, reason);
            } catch (PolicyServiceException e) {
                System.out.println("Error filing claim → " + e.getMessage());
            }
        }

        // Status update example
        System.out.println("\nUpdating all HIGH urgency claims to APPROVED...");
        for (Claim claim : claimService.getClaimQueue()) {
            if (claim.getUrgency() == Urgency.HIGH) {
                claimService.updateClaimStatus(claim, "APPROVED");
            }
        }

        // ----------------------
        // Priority Queue Poll Order
        // ----------------------
        System.out.print("\nPriorityQueue poll order → ");
        while (!claimService.getClaimQueue().isEmpty()) {
            System.out.print(claimService.getClaimQueue().poll().getUrgency());
            if (!claimService.getClaimQueue().isEmpty()) {
                System.out.print(", then ");
            }
        }
        System.out.println();

        // ----------------------
        // Exception Demonstrations (Optional)
        // ----------------------
        try {
            store.lookup("HDFC-LIFE-9999");
        } catch (PolicyServiceException e) {
            System.out.println("\nExpected Exception → " + e.getMessage());
        }

        // Audit log confirmation
        System.out.println("\nCheck audit.log → all dynamic claim filings are logged successfully.");

        sc.close();
    }
}