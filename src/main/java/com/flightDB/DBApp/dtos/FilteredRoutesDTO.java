package com.flightDB.DBApp.dtos;

import com.flightDB.DBApp.model.Routes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FilteredRoutesDTO {

    private List<Routes> originRoutesList;
    private List<Routes> destinationRoutesList;
}
