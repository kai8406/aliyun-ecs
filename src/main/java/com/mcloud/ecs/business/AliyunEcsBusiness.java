package com.mcloud.ecs.business;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.ecs.model.v20140526.CreateInstanceRequest;
import com.aliyuncs.ecs.model.v20140526.CreateInstanceResponse;
import com.aliyuncs.ecs.model.v20140526.DeleteInstanceRequest;
import com.aliyuncs.ecs.model.v20140526.DeleteInstanceResponse;
import com.aliyuncs.ecs.model.v20140526.ModifyInstanceAttributeRequest;
import com.aliyuncs.ecs.model.v20140526.ModifyInstanceAttributeResponse;
import com.aliyuncs.exceptions.ClientException;
import com.mcloud.core.constant.ActiveEnum;
import com.mcloud.core.constant.AggTypeEnum;
import com.mcloud.core.constant.mq.MQConstant;
import com.mcloud.core.constant.result.ResultDTO;
import com.mcloud.core.constant.result.ResultEnum;
import com.mcloud.core.constant.task.TaskDTO;
import com.mcloud.core.constant.task.TaskStatusEnum;
import com.mcloud.core.mapper.BeanMapper;
import com.mcloud.ecs.client.AccesskeyDTO;
import com.mcloud.ecs.client.EcsServiceDTO;
import com.mcloud.ecs.entity.AliyunEcsDTO;
import com.mcloud.ecs.service.AliyunEcsService;

@Component
public class AliyunEcsBusiness extends AbstractAliyunCommon {

	@Autowired
	protected AliyunEcsService service;

	/**
	 * 根据阿里云的Id获得AliyunEcsDTO对象.
	 * 
	 * @param uuid
	 * @return
	 */
	private AliyunEcsDTO getAliyunEcsDTOByUUID(String uuid) {
		Map<String, Object> map = new HashMap<>();
		map.put("EQ_uuid", uuid);
		return service.find(map);
	}

	public void removeEcs(EcsServiceDTO ecsServiceDTO) {

		// Step.1 创建Task对象.
		TaskDTO taskDTO = taskClient.getTask(ecsServiceDTO.getTaskId());

		// Step.2 根据username获得阿里云accesskeyId和accesskeySecret
		AccesskeyDTO accesskeyDTO = accountClient
				.getAccesskey(ecsServiceDTO.getUsername(), ecsServiceDTO.getPlatformId()).getData();

		// Step.3 获得EcsServiceDTO对象.
		AliyunEcsDTO aliyunEcsDTO = getAliyunEcsDTOByUUID(ecsServiceDTO.getEcsUuid());

		// Step.4 调用阿里云SDK执行操作.
		IAcsClient client = getServiceInstance(ecsServiceDTO.getRegionId(), accesskeyDTO);

		DeleteInstanceRequest request = new DeleteInstanceRequest();
		request.setInstanceId(aliyunEcsDTO.getUuid());

		try {

			DeleteInstanceResponse response = client.getAcsResponse(request);
			taskDTO.setRequestId(response.getRequestId());

		} catch (ClientException e) {

			// 修改Task对象执行状态.
			taskDTO.setStatus(TaskStatusEnum.执行失败.name());
			taskDTO.setResponseCode(e.getErrCode());
			taskDTO.setResponseData(e.getErrMsg());
			taskDTO = taskClient.updateTask(taskDTO.getId(), taskDTO);

			ResultDTO resultDTO = new ResultDTO(ecsServiceDTO.getEcsId(), AggTypeEnum.ecs.name(),
					ResultEnum.ERROR.name(), taskDTO.getId(), ecsServiceDTO.getUsername(), "");

			// 将执行的结果进行广播.
			rabbitTemplate.convertAndSend(MQConstant.MQ_EXCHANGE_NAME, MQConstant.ROUTINGKEY_RESULT_REMOVE,
					binder.toJson(resultDTO));
			return;
		}

		// Step.5 修改AliyunRouterDTO.
		aliyunEcsDTO.setActive(ActiveEnum.N.name());
		service.saveAndFlush(aliyunEcsDTO);

		// Step.6 修改TaskDTO.
		taskDTO.setStatus(TaskStatusEnum.执行成功.name());
		taskDTO = taskClient.updateTask(taskDTO.getId(), taskDTO);

		ResultDTO resultDTO = new ResultDTO(ecsServiceDTO.getEcsId(), AggTypeEnum.ecs.name(), ResultEnum.SUCCESS.name(),
				taskDTO.getId(), ecsServiceDTO.getUsername(), "");

		// Step.7 将执行的结果进行广播.
		rabbitTemplate.convertAndSend(MQConstant.MQ_EXCHANGE_NAME, MQConstant.ROUTINGKEY_RESULT_REMOVE,
				binder.toJson(resultDTO));

	}

	public void saveEcs(EcsServiceDTO ecsServiceDTO) {

		// Step.1 获得Task对象.
		TaskDTO taskDTO = taskClient.getTask(ecsServiceDTO.getTaskId());

		// Step.2 根据username获得阿里云accesskeyId和accesskeySecret
		AccesskeyDTO accesskeyDTO = accountClient
				.getAccesskey(ecsServiceDTO.getUsername(), ecsServiceDTO.getPlatformId()).getData();

		// Step.3 持久化AliyunEcsDTO.
		AliyunEcsDTO aliyunEcsDTO = BeanMapper.map(ecsServiceDTO, AliyunEcsDTO.class);
		aliyunEcsDTO.setEcsId(ecsServiceDTO.getEcsId());
		aliyunEcsDTO.setCreateTime(new Date());
		service.saveAndFlush(aliyunEcsDTO);

		// Step.4 调用阿里云SDK执行操作.
		IAcsClient client = getServiceInstance(ecsServiceDTO.getRegionId(), accesskeyDTO);

		CreateInstanceRequest request = new CreateInstanceRequest();
		request.setInternetMaxBandwidthOut(aliyunEcsDTO.getInternetMaxBandwidthOut());
		request.setInternetMaxBandwidthIn(aliyunEcsDTO.getInternetMaxBandwidthIn());
		request.setSystemDiskDescription(aliyunEcsDTO.getSystemDiskDescription());
		request.setInstanceChargeType(aliyunEcsDTO.getInstanceChargeType());
		request.setSystemDiskDiskName(aliyunEcsDTO.getSystemDiskdiskName());
		request.setInternetChargeType(aliyunEcsDTO.getInternetChargeType());
		request.setSystemDiskCategory(aliyunEcsDTO.getSystemDiskCategory());
		request.setSecurityGroupId(aliyunEcsDTO.getSecurityGroupUuid());
		request.setPrivateIpAddress(aliyunEcsDTO.getPrivateIpaddress());
		request.setAutoRenewPeriod(aliyunEcsDTO.getAutoRenewPeriod());
		request.setSystemDiskSize(aliyunEcsDTO.getSystemDiskSize());
		request.setInstanceType(aliyunEcsDTO.getInstanceType());
		request.setInstanceName(aliyunEcsDTO.getInstanceName());
		request.setDescription(aliyunEcsDTO.getDescription());
		request.setIoOptimized(aliyunEcsDTO.getIoOptimized());
		request.setVSwitchId(aliyunEcsDTO.getVswitchUuid());
		request.setAutoRenew(aliyunEcsDTO.getAutoRenew());
		request.setRegionId(aliyunEcsDTO.getRegionId());
		request.setPassword(aliyunEcsDTO.getPassword());
		request.setHostName(aliyunEcsDTO.getHostName());
		request.setImageId(aliyunEcsDTO.getImageId());
		request.setPeriod(aliyunEcsDTO.getPeriod());
		request.setZoneId(aliyunEcsDTO.getZoneId());

		// SDK中缺少KeyPairName DeploymentSetId SecurityEnhancementStrategy.

		CreateInstanceResponse response = null;

		try {

			response = client.getAcsResponse(request);

			taskDTO.setRequestId(response.getRequestId());

		} catch (ClientException e) {

			// 修改Task对象执行状态.
			taskDTO.setStatus(TaskStatusEnum.执行失败.name());
			taskDTO.setResponseCode(e.getErrCode());
			taskDTO.setResponseData(e.getErrMsg());
			taskDTO = taskClient.updateTask(taskDTO.getId(), taskDTO);

			ResultDTO resultDTO = new ResultDTO(ecsServiceDTO.getEcsId(), AggTypeEnum.ecs.name(),
					ResultEnum.ERROR.name(), taskDTO.getId(), ecsServiceDTO.getUsername(), "");

			// 将执行的结果进行广播.
			rabbitTemplate.convertAndSend(MQConstant.MQ_EXCHANGE_NAME, MQConstant.ROUTINGKEY_RESULT_SAVE,
					binder.toJson(resultDTO));
			return;
		}

		aliyunEcsDTO.setUuid(response.getInstanceId());
		aliyunEcsDTO = service.saveAndFlush(aliyunEcsDTO);

		// Step.6 修改TaskDTO.
		taskDTO.setStatus(TaskStatusEnum.执行成功.name());
		taskDTO = taskClient.updateTask(taskDTO.getId(), taskDTO);

		ResultDTO resultDTO = new ResultDTO(ecsServiceDTO.getEcsId(), AggTypeEnum.ecs.name(), ResultEnum.SUCCESS.name(),
				taskDTO.getId(), ecsServiceDTO.getUsername(), aliyunEcsDTO.getUuid());

		// Step.7 将执行的结果进行广播.
		rabbitTemplate.convertAndSend(MQConstant.MQ_EXCHANGE_NAME, MQConstant.ROUTINGKEY_RESULT_SAVE,
				binder.toJson(resultDTO));

	}

	public void updateEcs(EcsServiceDTO ecsServiceDTO) {

		// Step.1 获得Task对象.
		TaskDTO taskDTO = taskClient.getTask(ecsServiceDTO.getTaskId());

		// Step.2 根据username获得阿里云accesskeyId和accesskeySecret
		AccesskeyDTO accesskeyDTO = accountClient
				.getAccesskey(ecsServiceDTO.getUsername(), ecsServiceDTO.getPlatformId()).getData();

		// Step.3 查询AliyunEcsDTO.
		AliyunEcsDTO aliyunEcsDTO = getAliyunEcsDTOByUUID(ecsServiceDTO.getEcsUuid());

		// Step.4 调用阿里云SDK执行操作.
		ModifyInstanceAttributeRequest request = new ModifyInstanceAttributeRequest();
		request.setInstanceName(ecsServiceDTO.getInstanceName());
		request.setDescription(ecsServiceDTO.getDescription());
		request.setInstanceId(ecsServiceDTO.getEcsUuid());
		request.setPassword(ecsServiceDTO.getPassword());
		request.setHostName(ecsServiceDTO.getHostName());
		request.setUserData(ecsServiceDTO.getUserData());

		IAcsClient client = getServiceInstance(aliyunEcsDTO.getRegionId(), accesskeyDTO);

		try {

			ModifyInstanceAttributeResponse response = client.getAcsResponse(request);

			taskDTO.setRequestId(response.getRequestId());

		} catch (ClientException e) {

			/// 修改Task对象执行状态.
			taskDTO.setStatus(TaskStatusEnum.执行失败.name());
			taskDTO.setResponseCode(e.getErrCode());
			taskDTO.setResponseData(e.getErrMsg());
			taskDTO = taskClient.updateTask(taskDTO.getId(), taskDTO);

			ResultDTO resultDTO = new ResultDTO(ecsServiceDTO.getEcsId(), AggTypeEnum.ecs.name(),
					ResultEnum.ERROR.name(), taskDTO.getId(), aliyunEcsDTO.getUsername(), aliyunEcsDTO.getUuid());

			// 将执行的结果进行广播.
			rabbitTemplate.convertAndSend(MQConstant.MQ_EXCHANGE_NAME, MQConstant.ROUTINGKEY_RESULT_UPDATE,
					binder.toJson(resultDTO));
			return;
		}

		// // Step.5 更新Task和服务对象.
		aliyunEcsDTO.setModifyTime(new Date());
		aliyunEcsDTO = service.saveAndFlush(aliyunEcsDTO);

		taskDTO.setStatus(TaskStatusEnum.执行成功.name());
		taskDTO = taskClient.updateTask(taskDTO.getId(), taskDTO);

		ResultDTO resultDTO = new ResultDTO(ecsServiceDTO.getEcsId(), AggTypeEnum.ecs.name(), ResultEnum.SUCCESS.name(),
				taskDTO.getId(), aliyunEcsDTO.getUsername(), aliyunEcsDTO.getUuid());

		// Step.6 将执行的结果进行广播.
		rabbitTemplate.convertAndSend(MQConstant.MQ_EXCHANGE_NAME, MQConstant.ROUTINGKEY_RESULT_UPDATE,
				binder.toJson(resultDTO));
	}

}
