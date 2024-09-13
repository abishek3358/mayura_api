package com.app.entity;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.app.dto.FeedbackJsonDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "feedback_setting")
public class FeedbackSetting extends RecordModifier implements Serializable{
	
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Type(type = "uuid-char")
    @Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@Type(type = "uuid-char")
	@Column(name = "order_id")
	private UUID orderId;
	
	@Type(type = "uuid-char")
	@Column(name = "vendor_id")
	private UUID vendorId;

	@Column(name = "overall_ratings")
	private Double overallRatings;
	
    
	@Type(type = "json")
	@Column(name = "raw_json", columnDefinition = "json")
	private FeedbackJsonDTO rawJson;

}
