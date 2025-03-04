package org.example.clipflow.entity.user.json;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class TypeJson {
    String suggestion;
    List<DetailsJson> details;
}
