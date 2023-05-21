package com.cryptocurrency.converter.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name="history", uniqueConstraints = {@UniqueConstraint(columnNames = {"id"})}
)
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "crypto_coin", nullable = false)
    private String cryptoCoin;

    @Column(name = "ip_address", nullable = false)
    private String ipAddress;

    @Column(name = "crypto_price", nullable = false)
    private double cryptoPrice;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "time_stamp", nullable = false)
    private LocalDateTime timeStamp;


}
