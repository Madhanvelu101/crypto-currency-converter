package com.cryptocurrency.converter.models;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GeoLocation {
    private String country;
    private String city;
    private String currency;
    private String countryCode;
    private String language;
}
