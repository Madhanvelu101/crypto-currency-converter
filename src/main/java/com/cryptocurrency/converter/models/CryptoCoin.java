package com.cryptocurrency.converter.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * To store crypto coin name and id
 */
@Getter
@Setter
@AllArgsConstructor
public class CryptoCoin {
    private String name;
    private String id;
}
