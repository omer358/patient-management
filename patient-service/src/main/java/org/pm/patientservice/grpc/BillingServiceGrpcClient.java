package org.pm.patientservice.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class BillingServiceGrpcClient {
    private final BillingServiceGrpc.BillingServiceBlockingStub blockingStub;

    public BillingServiceGrpcClient(
            @Value("${billing.service.address:localhost}")
            String serverAddress,
            @Value("${billing.service.grpc.port:9001}")
            int serverPort
    ) {
        log.info("BillingServiceGrpcClient called with: serverAddress = {}, serverPort = {}",
                serverAddress, serverPort);
        ManagedChannel managedChannel = ManagedChannelBuilder
                .forAddress(serverAddress, serverPort)
                .usePlaintext().build();
        blockingStub = BillingServiceGrpc.newBlockingStub(managedChannel);
    }

    public BillingResponse createBillingAccount(String patientId,
                                                String name,
                                                String email) {
        log.info("createBillingAccount called with: patientId = {}, name = {}, email = {}",
                patientId, name, email);
        BillingRequest billingRequest = BillingRequest.newBuilder()
                .setPatientId(patientId)
                .setName(name)
                .setEmail(email)
                .build();
        BillingResponse billingAccount = blockingStub.createBillingAccount(billingRequest);
        log.info("received response from billing service via gRPC: {}", billingAccount);
        return billingAccount;
    }
}
