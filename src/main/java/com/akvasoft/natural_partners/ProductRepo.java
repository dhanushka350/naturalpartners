package com.akvasoft.natural_partners;

import org.apache.xpath.operations.Mod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepo extends JpaRepository<Modal, Integer> {

    Modal findTopByCodeEqualsAndAndCategoryEquals(String code, String cate);
}
