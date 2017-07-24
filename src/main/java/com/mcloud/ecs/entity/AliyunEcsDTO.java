package com.mcloud.ecs.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mcloud.core.constant.ActiveEnum;
import com.mcloud.core.constant.PlatformEnum;

import lombok.Data;

@Data
@Entity
@Table(name = "aliyun_ecs")
public class AliyunEcsDTO {

	/**
	 * UUID主键.
	 */
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	private String id;

	/**
	 * 数据状态,默认"A" {@link ActiveEnum}.
	 */
	@JsonIgnore
	@Column(name = "active")
	private String active = ActiveEnum.A.name();

	/**
	 * 平台ID. {@link PlatformEnum}
	 */
	@Column(name = "platform_id")
	private String platformId;

	/**
	 * 区域
	 */
	@Column(name = "region_id")
	private String regionId;

	/**
	 * task对象,不持久化.
	 */
	@Transient
	private String taskId;

	/**
	 * 用户ID.
	 */
	@Column(name = "user_name")
	private String username;

	/**
	 * 平台资源的唯一标识符
	 */
	@Column(name = "uuid")
	private String uuid = "";

	/**
	 * 实例所属的可用区编号,空表示由系统选择.
	 */
	@Column(name = "zone_id")
	private String zoneId;

	/**
	 * 镜像文件ID,表示启动实例时选择的镜像资源.
	 */
	@Column(name = "image_id")
	private String imageId;

	/**
	 * 实例的资源规格.
	 */
	@Column(name = "instance_type")
	private String instanceType;

	/**
	 * 指定新创建实例所属于的安全组代码,若不指定,则会将创建的实例加入到默认安全组中.
	 */
	@Column(name = "security_group_id")
	private String securityGroupId;

	@Column(name = "security_group_uuid")
	private String securityGroupUuid;

	/**
	 * 实例的显示名称, 如果没有指定该参数,默认值为实例的InstanceId.
	 */
	@Column(name = "instance_name")
	private String instanceName;

	/**
	 * 实例的实例的描述.
	 */
	@Column(name = "description")
	private String description;

	/**
	 * 网络计费类型,按流量计费还是按固定带宽计费.
	 */
	@Column(name = "internet_charge_type")
	private String internetChargeType;

	/**
	 * 公网入带宽最大值,单位为 Mbps (Mega bit per second),取值范围：[1,200] 如果客户不指定,API 将自动将入带宽设置成 200 Mbps.
	 */
	@Column(name = "internet_max_bandwidth_in")
	private Integer internetMaxBandwidthIn = 200;

	/**
	 * 公网出带宽最大值,单位为 Mbps(Mega bit per second).
	 */
	@Column(name = "internet_max_bandwidth_out")
	private Integer internetMaxBandwidthOut = 0;

	/**
	 * 云服务器的主机名.
	 */
	@Column(name = "host_name")
	private String hostName;

	/**
	 * 实例的密码.
	 */
	@Column(name = "password")
	private String password;

	/**
	 * 实例私网IP地址,不能单独指定.
	 */
	@Column(name = "private_ipaddress")
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
	@Column(name = "io_optimized")
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
	@Column(name = "system_disk_category")
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
	@Column(name = "system_disk_size")
	private Integer systemDiskSize = 40;

	/**
	 * 系统盘名称,不填则为空,默认值为空.
	 */
	@Column(name = "system_disk_name")
	private String systemDiskdiskName;

	/**
	 * 系统盘描述,不填则为空,默认值为空.
	 */
	@Column(name = "system_disk_description")
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
	@Column(name = "instance_charge_type")
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
	@Column(name = "period")
	private Integer Period;

	/**
	 * 是否要自动续费,当 InstanceChargeType 为 PrePaid 时才生效.
	 * 
	 * 取值范围：True,代表要自动续费|False,代表不要自动续费, 默认值:False
	 */
	@Column(name = "auto_renew")
	private Boolean autoRenew = Boolean.FALSE;

	/**
	 * 每次自动续费的时长,当AutoRenew为True时为必填：1|2|3|6|12
	 */
	@Column(name = "auto_renew_period")
	private Integer autoRenewPeriod;

	/**
	 * 实例的用户数据,需要以 ase64 方式编码,原始数据最多为 16KB.
	 */
	@Column(name = "user_data")
	private String userData;

	/**
	 * 密钥对名称.如果是 Windows ECS 实例,则忽略该参数.默认为空.
	 * 
	 * 如果填写了 KeyPairName,Password 的内容仍旧会被设置到实例中,但是 Linux 中的密码登录方式会被初始化成禁止.
	 */
	@Column(name = "key_pair_name")
	private String keyPairName;

	/**
	 * 部署集 ID.如果用户不填写,则采用 1 的方式
	 */
	@Column(name = "deployment_set_id")
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
	@Column(name = "security_enhancement_strategy")
	private String securityEnhancementStrategy;

	/**
	 * Vpc的主键.
	 */
	@Column(name = "vpc_id")
	private String vpcId = "";

	/**
	 * 云厂商Vpc的UUID.
	 */
	@Column(name = "vpc_uuid")
	private String vpcUuid = "";

	/**
	 * Router的主键.
	 */
	@Column(name = "router_id")
	private String routerId = "";

	/**
	 * 云厂商Router的UUID.
	 */
	@Column(name = "router_uuid")
	private String routerUuid = "";

	/**
	 * vSwitch的主键.
	 */
	@Column(name = "vswitch_id")
	private String vswitchId = "";

	/**
	 * 云厂商vSwitch的UUID.
	 */
	@Column(name = "vswitch_uuid")
	private String vswitchUuid = "";

	/**
	 * Ecs的主键.
	 */
	@Column(name = "ecs_id")
	private String ecsId = "";

	/**
	 * 创建时间.
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "create_time")
	private Date createTime;

	/**
	 * 修改时间.
	 */
	@JsonIgnore
	@Column(name = "modify_time")
	private Date modifyTime;

}
