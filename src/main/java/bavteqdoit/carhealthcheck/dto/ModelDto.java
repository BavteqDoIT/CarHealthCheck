package bavteqdoit.carhealthcheck.dto;

public class ModelDto {
    private Long id;
    private String modelName;

    public ModelDto() {}

    public ModelDto(Long id, String modelName) {
        this.id = id;
        this.modelName = modelName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
}
