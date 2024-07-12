package com.eighti.onebkk.service;

import java.util.Optional;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.eighti.onebkk.entity.Admin;
import com.eighti.onebkk.repository.AdminRepository;

@Service
public class AdminService {

	private static final Logger LOG = LoggerFactory.getLogger(AdminService.class);

	private final AdminRepository adminRepository;

	public AdminService(AdminRepository adminRepository) {
		this.adminRepository = adminRepository;
	}

	// TODO validate user credentials
	public boolean checkAdminAuthentication(String loginName, String password) {
		LOG.info("checkAdminAuth() >>> login name: " + loginName);

		String sha256HexPassword = DigestUtils.sha256Hex(password);
		Optional<Admin> adminOptional = adminRepository.findByLoginNameAndPassword(loginName, sha256HexPassword);

		if (adminOptional.isPresent()) {
			LOG.info("checkAdminAuth() >>> Admin data: " + adminOptional.get().toString());
			return true;
		}

		LOG.info("checkAdminAuth() >>> Admin not found!");
		return false;
	}

}
