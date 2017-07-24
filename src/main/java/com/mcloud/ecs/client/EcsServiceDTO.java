package com.mcloud.ecs.client;

import com.mcloud.core.constant.PlatformEnum;

import lombok.Data;

/**
 * Ecs聚合服务持久化对象.
 * 
 * @author liukai
 *
 */
@Data
public class EcsServiceDTO {

	/**
	 * UUID主键.
	 */
	private String ecsId;

	/**
	 * 平台ID. {@link PlatformEnum}
	 */
	private String platformId;

	/**
	 * 区域
	 */
	private String regionId;

	/**
	 * task对象,不持久化.
	 */
	private String taskId;

	/**
	 * 用户ID.
	 */
	private String username;

	/**
	 * 平台资源的唯一标识符
	 */
	private String ecsUuid = "";

	/**
	 * 实例所属的可用区编号,空表示由系统选择.
	 */
	private String zoneId;

	/**
	 * 镜像文件ID,表示启动实例时选择的镜像资源.
	 */
	private String imageId;

	/**
	 * 实例的资源规格.
	 */
	private String instanceType;

	/**
	 * 实例的显示名称, 如果没有指定该参数,默认值为实例的InstanceId.
	 */
	private String instanceName;

	/**
	 * 实例的实例的描述.
	 */
	private String description;

	/**
	 * 网络计费类型,按流量计费还是按固定带宽计费.
	 */
	private String internetChargeType;

	/**
	 * 公网入带宽最大值,单位为 Mbps (Mega bit per second),取值范围：[1,200] 如果客户不指定,API 将自动将入带宽设置成 200 Mbps.
	 */
	private Integer internetMaxBandwidthIn;

	/**
	 * 公网出带宽最大值,单位为 Mbps(Mega bit per second).
	 */
	private Integer internetMaxBandwidthOut;

	/**
	 * 云服务器的主机名.
	 */
	private String hostName;

	/**
	 * 实例的密码.
	 */
	private String password;

	/**
	 * 实例私网IP地址,不能单独指定.
	 */
	private String privateIpaddress;

	/**
	 * IO优化.
	 * 
	 * 默认值：若InstanceType为系列I的规格,则默认为none否则则默认为optimized.
	 *
	 * <pre>
	 * none：非 IO 优化 
	 * optimized：IO 优化
	 * </pre>
	 */
	private String ioOptimized;

	/**
	 * 系统盘的磁盘种类.
	 *
	 * 默认值：若InstanceType为系列I的规格且IO优化类型为none,则默认为cloud,否则默认为cloud_efficiency.
	 * 
	 * <pre>
	 * 
	 * cloud – 普通云盘
	 * cloud_efficiency – 高效云盘
	 * cloud_ssd – SSD云盘
	 * ephemeral_ssd - 本地 SSD 盘
	 * </pre>
	 */
	private String systemDiskCategory;

	/**
	 * 系统盘大小,以GB为单位,取值范围为：
	 * 
	 * <pre>
	 * cloud – 40~500 
	 * cloud_efficiency – 40~500 
	 * cloud_ssd – 40~500 
	 * ephemeral_ssd - 40~500
	 * </pre>
	 */
	private Integer systemDiskSize = 40;

	/**
	 * 系统盘名称,不填则为空,默认值为空.
	 */
	private String systemDiskdiskName;

	/**
	 * 系统盘描述,不填则为空,默认值为空.
	 */
	private String systemDiskDescription;

	/**
	 * 实例的付费方式.
	 * 
	 * <pre>
	 * PrePaid：预付费,即包年包月. 
	 * PostPaid：后付费,即按量付费.
	 * 默认为 PostPaid,即按量付费.
	 * </pre>
	 */
	private String InstanceChargeType;

	/**
	 * 购买资源的时长,单位是月.
	 * 
	 * <pre>
	 *  当 InstanceChargeType 为 PrePaid 时,才生效且为必选. 取值范围为：
	 * 1 - 9
	 * 12
	 * 24
	 * 36
	 * </pre>
	 */
	private Integer Period;

	/**
	 * 是否要自动续费,当 InstanceChargeType 为 PrePaid 时才生效.
	 * 
	 * 取值范围：True,代表要自动续费|False,代表不要自动续费, 默认值:False
	 */
	private Boolean autoRenew;

	/**
	 * 每次自动续费的时长,当AutoRenew为True时为必填：1|2|3|6|12
	 */
	private Integer autoRenewPeriod;

	/**
	 * 实例的用户数据,需要以 ase64 方式编码,原始数据最多为 16KB.
	 */
	private String userData;

	/**
	 * 密钥对名称.如果是 Windows ECS 实例,则忽略该参数.默认为空.
	 * 
	 * 如果填写了 KeyPairName,Password 的内容仍旧会被设置到实例中,但是 Linux 中的密码登录方式会被初始化成禁止.
	 */
	private String keyPairName;

	/**
	 * 部署集 ID.如果用户不填写,则采用 1 的方式
	 */
	private String deploymentSetId;

	/**
	 * 是否开启安全加固,取值列表：
	 * 
	 * <pre>
	 * Active:启用安全加固,只对系统镜像生效 
	 * Deactive：不启用安全加固,对所有镜像类型生效
	 * </pre>
	 * 
	 */
	private String securityEnhancementStrategy;

	/**
	 * Vpc的主键.
	 */
	private String vpcId = "";

	/**
	 * 云厂商Vpc的UUID.
	 */
	private String vpcUuid = "";

	/**
	 * Router的主键.
	 */
	private String routerId = "";

	/**
	 * 云厂商Router的UUID.
	 */
	private String routerUuid = "";

	/**
	 * vSwitch的主键.
	 */
	private String vswitchId = "";

	/**
	 * 云厂商vSwitch的UUID.
	 */
	private String vswitchUuid = "";

	/**
	 * 指定新创建实例所属于的安全组代码,若不指定,则会将创建的实例加入到默认安全组中.
	 */
	private String securityGroupId;

	/**
	 * 安全组UUID.
	 */
	private String securityGroupUuid;

}
