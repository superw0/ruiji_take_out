package com.wang.ruiji_take_out.dto;

import com.wang.ruiji_take_out.pojo.OrderDetail;
import com.wang.ruiji_take_out.pojo.Orders;
import lombok.Data;
import java.util.List;

@Data
public class OrdersDto extends Orders {

    private String userName;

    private String phone;

    private String address;

    private String consignee;

    private List<OrderDetail> orderDetails;
	
}
