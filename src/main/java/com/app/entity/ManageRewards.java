package com.app.entity;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.app.enumeration.RewardEarnType;
import com.app.enumeration.RewardType;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "manage_rewards")
public class ManageRewards extends RecordModifier implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Type(type = "uuid-char")
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;

	@NotNull(message = "Rewardtype cannot be blank")
	@ApiModelProperty(value = "Valid status", required = true, allowableValues = "SIGNUP, ORDER")
	@Enumerated(EnumType.STRING)
	@Column(name = "reward_type")
	private RewardType rewardType;

	@NotNull(message = "Reward cannot be blank")
	@ApiModelProperty(value = "reward", required = true, allowableValues = "Double")
	@Column(name = "reward")
	private Double reward;

	@NotNull(message = "Rewardearntype cannot be blank")
	@ApiModelProperty(value = "Valid status", required = true, allowableValues = "FLAT, PERCENT")
	@Enumerated(EnumType.STRING)
	@Column(name = "reward_earn_type")
	private RewardEarnType rewardearntype;

}
