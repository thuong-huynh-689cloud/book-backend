package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.common.enums.SortDirection;
import com.cloud.secure.streaming.common.enums.SortFieldBankInformation;
import com.cloud.secure.streaming.entities.BankInformation;
import com.cloud.secure.streaming.repositories.BankInformationRepository;
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
public class BankInformationServiceImpl implements BankInformationService {

    final BankInformationRepository bankInformationRepository;

    public BankInformationServiceImpl(BankInformationRepository bankInformationRepository) {
        this.bankInformationRepository = bankInformationRepository;
    }

    @Override
    public BankInformation save(BankInformation bankInformation) {
        return bankInformationRepository.save(bankInformation);
    }

    @Override
    public void delete(BankInformation bankInformation) {
        bankInformationRepository.delete(bankInformation);
    }

    @Override
    public BankInformation getById(String id) {
        return bankInformationRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteByIdIn(List<String> ids) {
        bankInformationRepository.deleteAllByIdIn(ids);
    }

    @Override
    public List<BankInformation> getAllByIdIn(List<String> ids) {
        return bankInformationRepository.findAllByIdIn(ids);
    }

    @Override
    public Page<BankInformation> getPage(String searchKey,
                                         SortFieldBankInformation sortFieldBankInformation,
                                         SortDirection sortDirection,
                                         int pageNumber, int pageSize
    ) {
        Sort.Direction direction = Sort.Direction.ASC;
        if (sortDirection.equals(SortDirection.DESC)) {
            direction = Sort.Direction.DESC;
        }
        Sort sort = Sort.by(direction, sortFieldBankInformation.toString());
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);

        return bankInformationRepository.findPageBankInformation("%" + searchKey + "%", pageable);
    }

    @Override
    public List<BankInformation> getAllByUserIdOrderByCreatedDateDesc(String userId) {
        return bankInformationRepository.findAllByUserIdOrderByCreatedDateDesc(userId);
    }

    @Override
    public Page<BankInformation> getPageByUserId(String userId,
                                                 String searchKey,
                                                 SortFieldBankInformation sortFieldBankInformation,
                                                 SortDirection sortDirection,
                                                 int pageNumber, int pageSize) {
        Sort.Direction direction = Sort.Direction.ASC;
        if (sortDirection.equals(SortDirection.DESC)) {
            direction = Sort.Direction.DESC;
        }
        Sort sort = Sort.by(direction, sortFieldBankInformation.toString());
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);

        return bankInformationRepository.findPageBankInformationByUserId(userId, "%" + searchKey + "%", pageable);
    }

}
