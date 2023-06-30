package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.common.enums.DisplayStatus;
import com.cloud.secure.streaming.common.enums.SortDirection;
import com.cloud.secure.streaming.common.enums.SortFieldSection;
import com.cloud.secure.streaming.entities.Section;
import com.cloud.secure.streaming.repositories.SectionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 689Cloud
 */
@Service
public class SectionServiceImpl implements SectionService {

    final SectionRepository sectionRepository;

    public SectionServiceImpl(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    @Override
    public Section save(Section section){
        return sectionRepository.save(section);
    }

    @Override
    public void delete(Section section){
        sectionRepository.delete(section);
    }

    @Override
    public Section getById(String id){
        return sectionRepository.findById(id).orElse(null);
    }

    @Override
    public Page<Section> getPage(String searchKey,
                                  SortFieldSection sortFieldSection,
                                  SortDirection sortDirection, List<DisplayStatus> displayStatuses,
                                  int pageNumber, int pageSize
    ) {
        Sort.Direction direction = Sort.Direction.ASC;
        if (sortDirection.equals(SortDirection.DESC)) {
            direction = Sort.Direction.DESC;
        }
        Sort sort = Sort.by(direction, sortFieldSection.toString());
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);

        return sectionRepository.getPageSection("%" + searchKey + "%", pageable);
    }
    @Override
    public List<Section> getAllByIdIn(List<String> ids){
        return sectionRepository.findAllByIdIn(ids);
    }
    @Override
    public void deleteByIdIn(List<String> ids) {
        sectionRepository.deleteAllByIdIn(ids);
    }

    @Override
    public List<Section> getAll() {
        return sectionRepository.findAll();
    }
    @Override
    public void saveAll(List<Section> sections) {
        sectionRepository.saveAll(sections);
    }

    public List<Section> getAllByCourseIdOrderByCreatedDateDesc(String courseId){
        return sectionRepository.findAllByCourseIdOrderByCreatedDateDesc(courseId);
    }
}
