package com.hdfclife.service;

import com.hdfclife.config.AppConfig;
import com.hdfclife.exception.PolicyServiceException;
import com.hdfclife.model.Claim;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class AuditLogger implements AutoCloseable{
    private PrintWriter writer;

    public AuditLogger(String filename) {
        try {
            this.writer = new PrintWriter(new FileWriter(filename, true));
        } catch (IOException e) {
            throw new PolicyServiceException("Failed to initialize AuditLogger", e);
        }
    }

    public void logClaim(Claim claim) {
        try {
            writer.println("Claim filed for Policy: " + claim.getPolicyNo() + " with amount: " + claim.getClaimAmount() + " [Company: " + AppConfig.INSTANCE.getCompanyName() + "]");
            writer.flush();
        } catch (Exception e) {
            throw new PolicyServiceException("Failed to write audit log", e);
        }
    }

    @Override
    public void close() {
        if (writer != null) {
            writer.close();
        }
    }
}
