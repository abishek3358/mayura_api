package com.app.entity;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

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
@Table(name="product")
public class Product extends RecordModifier implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID",strategy = "org.hibernate.id.UUIDGenerator")
	@Type(type = "uuid-char")
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;

	@NotNull(message = "Product name cannot be blank")
	@ApiModelProperty(value = "Product Name", required = true, allowableValues = "String")
	@Column(name = "product_name")
	private String productName;

	@NotNull(message = "Product code cannot be blank")
	@ApiModelProperty(value = "Product Code", required = true, allowableValues = "String")
	@Column(name = "product_code")
	private String productCode;

	@NotNull(message = "Product hs code cannot be blank")
	@ApiModelProperty(value = "Product HS Code", required = true, allowableValues = "String")
	@Column(name = "hs_code")
	private String hsCode;

	@NotNull(message = "Product category Id cannot be blank")
	@ApiModelProperty(value = "Product Category ID", required = true, allowableValues = "UUID")
	@Type(type = "uuid-char")
	@Column(name = "product_category_id")
	private UUID productCategoryId;
	
//	@NotNull(message = "Product category Id cannot be blank")
//	@ApiModelProperty(value = "Product Category ID", required = true, allowableValues = "UUID")
//	@Transient
//	private UUID productCategoryId;
	
	@NotNull(message = "Product category name cannot be blank")
	@ApiModelProperty(value = "Product Category name", required = true, allowableValues = "String")
	@Transient
	private String productCategoryName;
	
	@Type(type = "uuid-char")
	@Column(name = "brand_id")
	private UUID brandId;
	
	@Transient
	private String brandName;

	@NotNull(message = "Product Validity in days cannot be blank")
	@ApiModelProperty(value = "Product Validity in days", required = true, allowableValues = "Integer")
	@Column(name = "product_validity_in_days")
	private Integer productValidityInDays;
	
	@NotNull(message = "Tag description  cannot be blank")
	@ApiModelProperty(value = "Tag description", required = true, allowableValues = "String")
	@Column(name="tags")
	private String tag;
	
	@Transient
	private List<String> tagsList;

	@NotNull(message = "Product description  cannot be blank")
	@ApiModelProperty(value = "Product description", required = true, allowableValues = "String")
	@Column(name="description")
	private String description;

	@NotNull(message = "Product description  cannot be blank")
	@ApiModelProperty(value = "Product description", required = true, allowableValues = "String")
	@Column(name="detailed_description")
	private String detailedDescription;
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	private List<ProductDtl> productDtlList;

	@Transient
	@OneToMany(mappedBy = "productObj", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	private List<ProductImage> productImageList;



	
}
