package org.pm.billingservice.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingResponseOrBuilder;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import billing.BillingServiceGrpc.BillingServiceImplBase;


@GrpcService
@Slf4j
public class BillingGrpcService extends BillingServiceImplBase {

    @Override
    public void createBillingAccount(BillingRequest request,
                                     StreamObserver<BillingResponse> responseObserver) {
        log.info("createBillingAccount called with: request = {}", request.toString());
        BillingResponse billingResponse = BillingResponse.newBuilder()
                .setAccountId("123")
                .setStatus("Active")
                .build();
        responseObserver.onNext(billingResponse);
        responseObserver.onCompleted();
    }
}
