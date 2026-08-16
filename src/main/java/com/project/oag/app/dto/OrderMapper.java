package com.project.oag.app.dto;

import com.project.oag.app.entity.CertificateOfAuthenticity;
import com.project.oag.app.entity.Order;
import com.project.oag.app.entity.OrderAddress;
import com.project.oag.app.entity.OrderItem;
import com.project.oag.app.entity.Shipment;
import org.hibernate.Hibernate;

import java.util.ArrayList;
import java.util.List;

public final class OrderMapper {
    private OrderMapper() {
    }

    public static OrderResponseDto toDto(Order order) {
        return toDto(order, null, List.of());
    }

    public static OrderResponseDto toDto(Order order, Shipment shipment, List<CertificateOfAuthenticity> certificates) {
        if (order == null) {
            return null;
        }
        OrderResponseDto dto = new OrderResponseDto();
        dto.setId(order.getId());
        dto.setFirstname(order.getFirstname());
        dto.setLastname(order.getLastname());
        dto.setEmail(order.getEmail());
        dto.setPhone(order.getPhone());
        dto.setStatus(order.getStatus());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setOrderDate(order.getOrderDate());
        dto.setSecretCode(order.getSecretCode());
        dto.setItems(mapItems(order));
        dto.setAddress(mapAddress(order.getAddress()));
        dto.setShipment(CommerceMappers.toShipmentDto(shipment));
        dto.setCertificates(certificates == null ? new ArrayList<>()
                : certificates.stream().map(CommerceMappers::toCertificateDto).toList());
        return dto;
    }

    private static List<OrderItemResponseDto> mapItems(Order order) {
        if (order.getItems() == null || !Hibernate.isInitialized(order.getItems())) {
            return new ArrayList<>();
        }
        return order.getItems().stream().map(OrderMapper::toItemDto).toList();
    }

    private static OrderItemResponseDto toItemDto(OrderItem item) {
        OrderItemResponseDto dto = new OrderItemResponseDto();
        dto.setId(item.getId());
        dto.setQuantity(item.getQuantity());
        dto.setUnitPrice(item.getUnitPrice());
        dto.setLineTotal(item.getLineTotal());
        if (item.getArtwork() != null) {
            dto.setArtworkId(item.getArtwork().getId());
            dto.setArtworkName(item.getArtwork().getArtworkName());
            if (item.getArtwork().getImageUrls() != null && !item.getArtwork().getImageUrls().isEmpty()) {
                dto.setImageUrl(item.getArtwork().getImageUrls().get(0));
            }
        }
        if (item.getArtist() != null) {
            dto.setArtistId(item.getArtist().getId());
        }
        return dto;
    }

    private static OrderAddressDto mapAddress(OrderAddress address) {
        if (address == null || !Hibernate.isInitialized(address)) {
            return null;
        }
        OrderAddressDto dto = new OrderAddressDto();
        dto.setStreet(address.getStreet());
        dto.setCity(address.getCity());
        dto.setState(address.getState());
        dto.setCountry(address.getCountry());
        dto.setPostalCode(address.getPostalCode());
        return dto;
    }
}
