package com.SmartCourierManagementApi.smartCourierManagementSystem.Service;

import com.SmartCourierManagementApi.smartCourierManagementSystem.Entity.DeliveryEntity;
import com.SmartCourierManagementApi.smartCourierManagementSystem.Repository.DeliveryEntityRepository;
import com.SmartCourierManagementApi.smartCourierManagementSystem.enumeration.DeliveryAgentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Service
public class DeliveryProgressScheduler {

    @Autowired
    private UserService userService;
    @Autowired
    private DeliveryEntityRepository deliveryEntityRepository;
    @Autowired
    @Qualifier("courierExecutor")
    private Executor courierExecutor;


//    @Scheduled(fixedDelay = 2 * 60 * 1000)
    public void simulateDeliveryProgress() {


        List<DeliveryEntity> pendingDeliveries =
                deliveryEntityRepository.findAll()
                        .stream()
                        .filter(d -> d.getStatus() != DeliveryAgentStatus.DELIVERED)
                        .toList();


        List<CompletableFuture<Void>> futures =
                pendingDeliveries.stream()
                        .map(delivery ->
                                CompletableFuture.runAsync(() -> updateDelivery(delivery), courierExecutor)
                        )
                        .toList();


        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        System.out.println(" Scheduler cycle completed for " + pendingDeliveries.size() + " deliveries.");
    }


    private void updateDelivery(DeliveryEntity delivery) {
        try {
            Integer agentId = delivery.getDeliveryAgent().getUser_id();
            Integer packageId = delivery.getPackageEntity().getPackage_id();

            // Compute the next status
            DeliveryAgentStatus current = delivery.getStatus();
            DeliveryAgentStatus next = getNextDeliveryStatus(current);

            if (next != null) {
                // Use existing service method to handle DB update & package status
                userService.updateStatus(agentId, packageId, next.name());

                System.out.println(
                        "Delivery " + delivery.getDelivery_id() +
                                " moved from " + current +
                                " → " + next +
                                " | Thread: " + Thread.currentThread().getName()
                );
            }
        } catch (Exception e) {
            System.err.println(" Failed to update delivery " + delivery.getDelivery_id() + ": " + e.getMessage());
        }
    }


    private DeliveryAgentStatus getNextDeliveryStatus(DeliveryAgentStatus current) {
        return switch (current) {
            case PICKED_UP -> DeliveryAgentStatus.IN_TRANSIT;
            case IN_TRANSIT -> DeliveryAgentStatus.DELIVERED;
            default -> null; // DELIVERED or invalid
        };
    }
}

