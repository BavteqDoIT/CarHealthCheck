package bavteqdoit.carhealthcheck.service;

import bavteqdoit.carhealthcheck.data.ModelTypeRepository;
import bavteqdoit.carhealthcheck.model.ModelType;
import org.springframework.data.domain.Sort;
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

    public List<ModelType> findAllSorted(String sortField, String sortOrder){
        Sort sort = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();
        return modelTypeRepository.findAll(sort);
    }
}
