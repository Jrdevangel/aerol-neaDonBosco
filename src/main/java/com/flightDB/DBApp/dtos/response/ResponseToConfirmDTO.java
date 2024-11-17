package com.flightDB.DBApp.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseToConfirmDTO {

    private String text;
    private boolean isTotalReturn;
}
