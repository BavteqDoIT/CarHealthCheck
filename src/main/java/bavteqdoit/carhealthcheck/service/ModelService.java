package bavteqdoit.carhealthcheck.service;

import bavteqdoit.carhealthcheck.data.ModelTypeRepository;
import bavteqdoit.carhealthcheck.model.ModelType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModelService {
    private final ModelTypeRepository modelTypeRepository;

    public ModelService(ModelTypeRepository modelTypeRepository) {
        this.modelTypeRepository = modelTypeRepository;
    }

    public List<ModelType> findAll(){
        return modelTypeRepository.findAll();
    }

    public void deleteById(Long id){
        modelTypeRepository.deleteById(id);
    }
}
