package com.app.repository;

import java.util.UUID;

import com.app.config.WriteableRepository;
import com.app.entity.Vendor;

public interface VendorRepository extends WriteableRepository<Vendor, UUID> {
	
}
