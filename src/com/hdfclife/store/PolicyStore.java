package com.hdfclife.store;

import com.hdfclife.exception.PolicyNotFoundException;
import com.hdfclife.model.Policy;
import java.util.*;

public class PolicyStore {
    private final List<Policy> policyList = new ArrayList<>();
    private final Set<String> uniqueCustomers = new HashSet<>();
    private final Map<String, Policy> policyMap = new HashMap<>();
    private final TreeMap<String, Policy> sortedPolicyMap = new TreeMap<>();

    public void addPolicy(Policy policy) {
        policyList.add(policy);
        uniqueCustomers.add(policy.getCustomerName().trim());
        policyMap.put(policy.getPolicyNo(), policy);
        sortedPolicyMap.put(policy.getPolicyNo(), policy);
    }

    public List<Policy> getPolicyList() {
        return policyList;
    }

    public int getUniqueCustomerCount() {
        return uniqueCustomers.size();
    }

    public Policy lookup(String policyNo) {
        Policy policy = policyMap.get(policyNo);
        if (policy == null) {
            throw new PolicyNotFoundException("Policy not found: " + policyNo);
        }
        return policy;
    }

    public Set<String> getSortedPolicyKeys() {
        return sortedPolicyMap.keySet();
    }
}
