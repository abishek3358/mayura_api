package com.app.repository;

import java.util.List;
import java.util.UUID;

import com.app.config.WriteableRepository;
import com.app.entity.VendorContactPerson;

public interface VendorContactPersonRepository extends WriteableRepository<VendorContactPerson, UUID> {

	List<VendorContactPerson> findByVendorId(UUID id);


}
