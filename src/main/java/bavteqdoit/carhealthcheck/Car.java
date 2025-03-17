package bavteqdoit.carhealthcheck;

import lombok.Data;

import java.util.List;

@Data
public class Car {
    private String name;
    private List<String> fields;
}
