package com.SmartCourierManagementApi.smartCourierManagementSystem.Controller;

import com.SmartCourierManagementApi.smartCourierManagementSystem.DTO.BulkAssignmentDto;
import com.SmartCourierManagementApi.smartCourierManagementSystem.DTO.DeliveryEntityDto;
import com.SmartCourierManagementApi.smartCourierManagementSystem.Entity.DeliveryEntity;
import com.SmartCourierManagementApi.smartCourierManagementSystem.Entity.OrderEntity;
import com.SmartCourierManagementApi.smartCourierManagementSystem.Entity.UserEntity;
import com.SmartCourierManagementApi.smartCourierManagementSystem.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("courier")
public class CourierController
{
    @Autowired
    private UserService userService;



    @PostMapping("add-user")
    public List<UserEntity> addUser(@RequestBody List<UserEntity> ue)
    {
        return userService.addUser(ue);
    }



    @PostMapping("add-order/{id}")
    public OrderEntity addOrder(@RequestBody  OrderEntity ode,@PathVariable Integer id )
    {
        return userService.addOrder(ode,id);
    }


    @PostMapping("assign-delivery/{mId}/{aId}/{pId}")
    public DeliveryEntity assignDelivery(@PathVariable Integer mId, @PathVariable Integer aId, @PathVariable Integer pId)
    {

        return userService.assignDelivery(mId, aId, pId);
    }


    @GetMapping("get-agentDetails/{id}")
    public List<DeliveryEntityDto> getAgentDetails(@PathVariable Integer id)
    {
        return userService.getAgentDetails(id);
    }

    @GetMapping("get-managerDetails/{id}")
    public List<DeliveryEntityDto> getManagerDetails(@PathVariable Integer id)
    {
        return userService.getManagerDetails(id);
    }

    @PutMapping("/update-status/{aid}/{pid}")
    public DeliveryEntity updateStatus(@PathVariable Integer aid,
                                       @PathVariable Integer pid,
                                       @RequestParam String dstatus)
    {

        return userService.updateStatus(aid, pid, dstatus);
    }


    @PostMapping("assign-bulk-different-agents/{managerId}")
    public CompletableFuture<List<DeliveryEntity>> assignBulkDifferentAgents(
            @PathVariable Integer managerId,
            @RequestBody List<BulkAssignmentDto> assignments)
    {
        return userService.assignBulkDifferentAgents(managerId, assignments);
    }


}
