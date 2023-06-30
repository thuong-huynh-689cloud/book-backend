package com.cloud.secure.streaming.services;

import com.cloud.secure.streaming.common.enums.SortDirection;
import com.cloud.secure.streaming.common.enums.SortFieldBankInformation;
import com.cloud.secure.streaming.entities.BankInformation;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author 689Cloud
 */
public interface BankInformationService{

    BankInformation save (BankInformation bankInformation);

    void delete (BankInformation bankInformation);

    BankInformation getById(String id);

    void deleteByIdIn(List<String> ids);

    List<BankInformation> getAllByIdIn(List<String> ids);

    Page<BankInformation> getPage(String searchKey,
                                  SortFieldBankInformation sortFieldBankInformation,
                                  SortDirection sortDirection,
                                  int pageNumber, int pageSize);

    List<BankInformation> getAllByUserIdOrderByCreatedDateDesc(String userId);

    Page<BankInformation> getPageByUserId(String userId,
                                          String searchKey,
                                          SortFieldBankInformation sortFieldBankInformation,
                                          SortDirection sortDirection,
                                          int pageNumber, int pageSize);
}
