package com.test_obs.inventoryms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetDataDTO {
    private long userId;
    private long id;
    private String title;
    private String body;

}
