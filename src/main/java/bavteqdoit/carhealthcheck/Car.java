package bavteqdoit.carhealthcheck;

import lombok.Data;

@Data
public class Car {
    private Long brand;
    private Long model;
    private Long color;
    private Long engineType;
    private Long bodyType;
    private String name;
}
