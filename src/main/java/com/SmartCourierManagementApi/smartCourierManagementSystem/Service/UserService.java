package com.SmartCourierManagementApi.smartCourierManagementSystem.Service;

import com.SmartCourierManagementApi.smartCourierManagementSystem.DTO.BulkAssignmentDto;
import com.SmartCourierManagementApi.smartCourierManagementSystem.DTO.DeliveryEntityDto;
import com.SmartCourierManagementApi.smartCourierManagementSystem.Entity.DeliveryEntity;
import com.SmartCourierManagementApi.smartCourierManagementSystem.Entity.OrderEntity;
import com.SmartCourierManagementApi.smartCourierManagementSystem.Entity.PackageEntity;
import com.SmartCourierManagementApi.smartCourierManagementSystem.Entity.UserEntity;
import com.SmartCourierManagementApi.smartCourierManagementSystem.Repository.DeliveryEntityRepository;
import com.SmartCourierManagementApi.smartCourierManagementSystem.Repository.OrderEntityRepository;
import com.SmartCourierManagementApi.smartCourierManagementSystem.Repository.PackageEntityRepository;
import com.SmartCourierManagementApi.smartCourierManagementSystem.Repository.UserRepository;
import com.SmartCourierManagementApi.smartCourierManagementSystem.enumeration.DeliveryAgentStatus;
import com.SmartCourierManagementApi.smartCourierManagementSystem.enumeration.PackageStatus;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Service
public class UserService
{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderEntityRepository orderEntityRepository;
    @Autowired
    private PackageEntityRepository packageEntityRepository;
    @Autowired
    private DeliveryEntityRepository deliveryEntityRepository;
    @Autowired
    @Qualifier("courierExecutor")
    private Executor courierExecutor;


    public List<UserEntity> addUser(List<UserEntity> ue)
    {
        return userRepository.saveAll(ue);
    }

    public OrderEntity addOrder(OrderEntity ode, Integer id)
    {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        ode.setUserEntity(user);

        if(ode.getPackageEntities() != null)
        {
            for(PackageEntity entity : ode.getPackageEntities())
            {
                entity.setOrderEntity(ode);
            }
        }
       return orderEntityRepository.save(ode);
    }

    public DeliveryEntity assignDelivery(Integer mId, Integer aId, Integer pId)
    {
        UserEntity manager = userRepository.findById(mId)
                .orElseThrow(() -> new RuntimeException("Manager not found"));

        UserEntity agent = userRepository.findById(aId)
                .orElseThrow(() -> new RuntimeException("Agent not found"));


        if(!agent.getRole().toString().equals("DELIVERY_AGENT"))
        {
            throw new RuntimeException("this is not agent ");
        }

        PackageEntity pkg = packageEntityRepository.findById(pId)
                .orElseThrow(() -> new RuntimeException("Package not found"));

        DeliveryEntity delivery = new DeliveryEntity();
        delivery.setAssignedByManager(manager);
        delivery.setDeliveryAgent(agent);
        delivery.setPackageEntity(pkg);
        delivery.setStatus(DeliveryAgentStatus.PICKED_UP);

        pkg.setPackageStatus(PackageStatus.PICKED_and_IN_TRANSIT);

        return deliveryEntityRepository.save(delivery);
    }

    public List<DeliveryEntityDto> getAgentDetails(Integer id)
    {
        List<DeliveryEntity> deliveryEntities =  deliveryEntityRepository.findAllByAgentId(id);

        List<DeliveryEntityDto> deliveryEntityDtos = new ArrayList<>();

        for(DeliveryEntity dt : deliveryEntities)
        {
            DeliveryEntityDto deliveryEntityDto = new DeliveryEntityDto();
            deliveryEntityDto.setDelivery_id(dt.getDelivery_id());
            deliveryEntityDto.setPackage_id(dt.getPackageEntity().getPackage_id());
            deliveryEntityDto.setAgent_id(dt.getDeliveryAgent().getUser_id());
            deliveryEntityDto.setManager_id(dt.getAssignedByManager().getUser_id());
            deliveryEntityDto.setStatus(dt.getStatus());

            deliveryEntityDtos.add(deliveryEntityDto);
        }
        return deliveryEntityDtos;
    }

    public List<DeliveryEntityDto> getManagerDetails(Integer id)
    {
        List<DeliveryEntity> deliveryEntities =  deliveryEntityRepository.findAllByManagerId(id);

        List<DeliveryEntityDto> deliveryEntityDtos = new ArrayList<>();

        for(DeliveryEntity dt : deliveryEntities)
        {
            DeliveryEntityDto deliveryEntityDto = new DeliveryEntityDto();
            deliveryEntityDto.setDelivery_id(dt.getDelivery_id());
            deliveryEntityDto.setPackage_id(dt.getPackageEntity().getPackage_id());
            deliveryEntityDto.setAgent_id(dt.getDeliveryAgent().getUser_id());
            deliveryEntityDto.setManager_id(dt.getAssignedByManager().getUser_id());
            deliveryEntityDto.setStatus(dt.getStatus());

            deliveryEntityDtos.add(deliveryEntityDto);
        }
        return deliveryEntityDtos;

    }


    public DeliveryEntity updateStatus(Integer aid, Integer pid, String dstatus)
    {
        UserEntity agent = userRepository.findById(aid)
                .orElseThrow(() -> new RuntimeException("agent not found"));

        PackageEntity packageEntity = packageEntityRepository.findById(pid)
            .orElseThrow(() -> new RuntimeException("package not found"));

        DeliveryEntity delivery = deliveryEntityRepository.findByAgentIdAndPackageId(aid,pid);

        if(delivery == null)
        {
            throw new RuntimeException("delivery assignment not found");
        }

        delivery.setStatus(DeliveryAgentStatus.valueOf(dstatus));

        deliveryEntityRepository.save(delivery);

        if(delivery.getStatus().toString().equals("DELIVERED"))
        {
            packageEntity.setPackageStatus(PackageStatus.DELIVERED);
        }

        packageEntityRepository.save(packageEntity);

        return delivery;
    }


    @Async("courierExecutor")
    public CompletableFuture<List<DeliveryEntity>> assignBulkDifferentAgents(
            Integer managerId,
            List<BulkAssignmentDto> assignments)
    {
        // Create async tasks PER PARCEL
        List<CompletableFuture<DeliveryEntity>> futures =
                assignments.stream()
                        .map(dto ->
                                CompletableFuture.supplyAsync(() -> {
                                    System.out.println(
                                            "Parcel " + dto.getPackageId() +
                                                    " running on " +
                                                    Thread.currentThread().getName()
                                    );
                                    return assignDelivery(
                                            managerId,
                                            dto.getAgentId(),
                                            dto.getPackageId()
                                    );
                                }, courierExecutor)
                        )
                        .toList();

        // Wait for ALL parcels to finish
        CompletableFuture<Void> allDone =
                CompletableFuture.allOf(
                        futures.toArray(new CompletableFuture[0])
                );

        // Collect results
        return allDone.thenApply(v ->
                futures.stream()
                        .map(CompletableFuture::join)
                        .toList()
        );
    }
}
