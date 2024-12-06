package com.epicure.project.service.impl;

import com.epicure.project.dao.entity.MealEntity;
import com.epicure.project.dao.entity.OrderEntity;
import com.epicure.project.dao.entity.TableEntity;
import com.epicure.project.dao.entity.WaiterEntity;
import com.epicure.project.dao.repository.MealRepository;
import com.epicure.project.dao.repository.OrderRepository;
import com.epicure.project.dao.repository.TableRepository;
import com.epicure.project.dao.repository.WaiterRepository;
import com.epicure.project.model.dto.request.OrderRequest;
import com.epicure.project.model.dto.response.IncomeReportResponse;
import com.epicure.project.model.dto.response.MessageResponse;
import com.epicure.project.model.dto.response.OrderCheckResponse;
import com.epicure.project.model.dto.response.OrderMealDetail;
import com.epicure.project.model.enums.OrderStatus;
import com.epicure.project.model.exception.ResourceNotFoundException;
import com.epicure.project.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final TableRepository tableRepository;
    private final WaiterRepository waiterRepository;
    private final MealRepository mealRepository;
    private final ModelMapper modelMapper;
    @Override
    public ResponseEntity<MessageResponse> create(OrderRequest request) {
        // Validate and fetch table
        TableEntity table = tableRepository.findById(request.getTableId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid table ID: " + request.getTableId()));

        // Validate and fetch waiter
        WaiterEntity waiter = waiterRepository.findById(request.getWaiterId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid waiter ID: " + request.getWaiterId()));

        /// Fetch meals and calculate total price
        Map<Long, Integer> mealQuantities = request.getMealQuantities();
        if (mealQuantities.isEmpty()) {
            throw new IllegalArgumentException("Meals and quantities must be provided.");
        }

        List<MealEntity> meals = mealRepository.findAllById(mealQuantities.keySet());
        if (meals.size() != mealQuantities.size()) {
            throw new IllegalArgumentException("Some meals provided are invalid.");
        }

        double totalPrice = meals.stream()
                .mapToDouble(meal -> meal.getPrice() * mealQuantities.get(meal.getId()))
                .sum();

        // Create and save order entity
        OrderEntity order = new OrderEntity();
        order.setOrderTime(request.getOrderTime());
        order.setTable(table);
        order.setWaiter(waiter);
        order.setMeals(meals);
        order.setMealQuantities(mealQuantities);
        order.setTotalPrice(totalPrice);
        order.setStatus(OrderStatus.PENDING); // Default status for new orders
        orderRepository.save(order);

        MessageResponse response = new MessageResponse();
        response.setMessage("Created Successfully");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<MessageResponse> acceptOrder(Long orderId) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        order.setStatus(OrderStatus.PREPARING);
        orderRepository.save(order);

        MessageResponse response = new MessageResponse();
        response.setMessage("Order accepted and preparing");
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @Override
    public ResponseEntity<MessageResponse> rejectOrder(Long orderId) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        MessageResponse response = new MessageResponse();
        response.setMessage("Order cancelled");
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
    }

    @Override
    public ResponseEntity<MessageResponse> serveOrder(Long orderId) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        if (order.getStatus() != OrderStatus.PREPARING) {
            throw new IllegalStateException("Order must be in 'PREPARING' status to be served.");
        }
        order.setStatus(OrderStatus.SERVED);
        orderRepository.save(order);

        MessageResponse response = new MessageResponse();
        response.setMessage("Order served.");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity<MessageResponse> completeOrder(Long orderId) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        if (order.getStatus() != OrderStatus.SERVED) {
            throw new IllegalStateException("Order must be in 'SERVED' status to be completed.");
        }
        order.setStatus(OrderStatus.COMPLETED);
        orderRepository.save(order);

        MessageResponse response = new MessageResponse();
        response.setMessage("Order completed successfully.");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity<OrderCheckResponse> checkOrder(Long orderId) {
        // Fetch the order
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        // Check if the order is completed
        if (order.getStatus() != OrderStatus.COMPLETED) {
            throw new IllegalArgumentException("Order is not completed yet.");
        }

        // Prepare the response
        OrderCheckResponse response = new OrderCheckResponse();
        response.setTableNumber(order.getTable().getTableNumber());
        response.setTotalPrice(order.getTotalPrice());

        // Map meals and their quantities
        Map<String, OrderMealDetail> mealDetails = order.getMealQuantities().entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> mealRepository.findById(entry.getKey())
                                .orElseThrow(() -> new ResourceNotFoundException("Meal not found"))
                                .getName(),
                        entry -> new OrderMealDetail(
                                mealRepository.findById(entry.getKey())
                                        .orElseThrow(() -> new ResourceNotFoundException("Meal not found"))
                                        .getPrice(),
                                entry.getValue()
                        )
                ));
        response.setMealDetails(mealDetails);

        return ResponseEntity.ok(response);
    }

    @Override
    public IncomeReportResponse getIncomeReport() {
        LocalDateTime now = LocalDateTime.now();

        // Günlük gəlir
        double dailyIncome = orderRepository.findByStatusAndOrderTimeBetween(
                OrderStatus.COMPLETED,
                now.truncatedTo(ChronoUnit.DAYS),
                now
        ).stream().mapToDouble(OrderEntity::getTotalPrice).sum();

        // Həftəlik gəlir
        double weeklyIncome = orderRepository.findByStatusAndOrderTimeBetween(
                OrderStatus.COMPLETED,
                now.minusDays(7),
                now
        ).stream().mapToDouble(OrderEntity::getTotalPrice).sum();

        // Aylıq gəlir
        double monthlyIncome = orderRepository.findByStatusAndOrderTimeBetween(
                OrderStatus.COMPLETED,
                now.minusMonths(1),
                now
        ).stream().mapToDouble(OrderEntity::getTotalPrice).sum();

        // İllik gəlir
        double yearlyIncome = orderRepository.findByStatusAndOrderTimeBetween(
                OrderStatus.COMPLETED,
                now.minusYears(1),
                now
        ).stream().mapToDouble(OrderEntity::getTotalPrice).sum();

        // DTO ilə cavab göndəririk
        IncomeReportResponse response = new IncomeReportResponse();
        response.setDailyIncome(dailyIncome);
        response.setWeeklyIncome(weeklyIncome);
        response.setMonthlyIncome(monthlyIncome);
        response.setYearlyIncome(yearlyIncome);

        return response;
    }

    @Override
    public void delete(Long orderId) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        orderRepository.delete(order);
    }

}

