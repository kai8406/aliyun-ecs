package com.mcloud.ecs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mcloud.core.persistence.BaseEntityCrudServiceImpl;
import com.mcloud.ecs.entity.AliyunEcsDTO;
import com.mcloud.ecs.repository.AliyunEcsRepository;

@Service
@Transactional
public class AliyunEcsService extends BaseEntityCrudServiceImpl<AliyunEcsDTO, AliyunEcsRepository> {

	@Autowired
	private AliyunEcsRepository repository;

	@Override
	protected AliyunEcsRepository getRepository() {
		return repository;
	}
}
