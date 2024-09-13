package com.app.repository;

import java.util.UUID;

import com.app.config.WriteableRepository;
import com.app.entity.PaymentTerm;

public interface PaymentTermRepository extends WriteableRepository<PaymentTerm, UUID> {

}
