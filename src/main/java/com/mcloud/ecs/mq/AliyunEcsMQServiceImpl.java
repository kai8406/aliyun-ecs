package com.mcloud.ecs.mq;

import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mcloud.core.constant.PlatformEnum;
import com.mcloud.core.constant.mq.MQConstant;
import com.mcloud.core.mapper.JsonMapper;
import com.mcloud.core.util.EncodeUtils;
import com.mcloud.ecs.business.AliyunEcsBusiness;
import com.mcloud.ecs.client.EcsServiceDTO;

@Component
public class AliyunEcsMQServiceImpl implements AliyunEcsMQService {

	private static JsonMapper binder = JsonMapper.nonEmptyMapper();

	@Autowired
	private AliyunEcsBusiness ecsBusiness;

	@Override
	public void aliyunEcsAgg(Message message) {

		String receivedRoutingKey = message.getMessageProperties().getReceivedRoutingKey();

		String receiveString = EncodeUtils.EncodeMessage(message.getBody());

		EcsServiceDTO ecsServiceDTO = binder.fromJson(receiveString, EcsServiceDTO.class);

		if (!PlatformEnum.aliyun.name().equalsIgnoreCase(ecsServiceDTO.getPlatformId())) {
			return;
		}

		if (MQConstant.ROUTINGKEY_AGG_ECS_SAVE.equalsIgnoreCase(receivedRoutingKey)) {

			ecsBusiness.saveEcs(ecsServiceDTO);

		} else if (MQConstant.ROUTINGKEY_AGG_ECS_UPDATE.equalsIgnoreCase(receivedRoutingKey)) {

			ecsBusiness.updateEcs(ecsServiceDTO);

		} else if (MQConstant.ROUTINGKEY_AGG_ECS_REMOVE.equalsIgnoreCase(receivedRoutingKey)) {

			ecsBusiness.removeEcs(ecsServiceDTO);
		}
	}

}
