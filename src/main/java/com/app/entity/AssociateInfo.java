package com.app.entity;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.app.enumeration.AssociateType;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value = "Customer Request", description = "The customer request payload")
@Entity
@Getter
@Setter
@Table(name = "trading_associate")
public class AssociateInfo extends RecordModifier implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Type(type = "uuid-char")
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;

	@NotNull(message = "Associate type cannot be blank")
	@ApiModelProperty(value = "Valid Associate type", required = true, allowableValues = "Supplier, Customer, Both")
	@Enumerated(EnumType.STRING)
	@Column(name = "associate_type")
	private AssociateType type;

	@NotNull(message = "Associate name cannot be blank")
	@ApiModelProperty(value = "Valid Associate name", required = true, allowableValues = "NonEmpty String")
	@Column(name = "name")
	private String name;

	@NotNull(message = "Address Line1 can be null but not blank")
	@ApiModelProperty(value = "Address Line1", allowableValues = "NonEmpty String", allowEmptyValue = false)
	@Column(name = "address_line1")
	private String addressLine1;

	@ApiModelProperty(value = "Address Line2", allowableValues = "NonEmpty String", allowEmptyValue = false)
	@Column(name = "address_line2")
	private String addressLine2;

	@Valid
	@NotNull(message = "Country cannot be null")
	@ApiModelProperty(value = "Country", required = true, dataType = "object", allowableValues = "A valid country object")
	@OneToOne()
	@JoinColumn(name = "country_id")
	private Country countryObj;

	@ApiModelProperty(value = "Postal code", allowableValues = "NonEmpty String", allowEmptyValue = false)
	@Column(name = "postal_code")
	private String postalCode;

	@ApiModelProperty(value = "Bank name", allowableValues = "NonEmpty String", allowEmptyValue = false)
	@Column(name = "bank_name")
	private String bankName;

	@ApiModelProperty(value = "Bank account name", allowableValues = "NonEmpty String", allowEmptyValue = false)
	@Column(name = "bank_account_name")
	private String bankAcctName;

	@ApiModelProperty(value = "Bank branch name", allowableValues = "NonEmpty String", allowEmptyValue = false)
	@Column(name = "bank_branch_name")
	private String bankBranchName;

	@ApiModelProperty(value = "Bank account number", allowableValues = "NonEmpty String", allowEmptyValue = false)
	@Column(name = "bank_account_no")
	private String bankAccountNum;

	@ApiModelProperty(value = "Bank Swift code", allowableValues = "NonEmpty String", allowEmptyValue = false)
	@Column(name = "bank_swift_code")
	private String bankSwiftCode;

	@ApiModelProperty(value = "Remarks", allowableValues = "NonEmpty String", allowEmptyValue = false)
	@Column(name = "notes")
	private String notes;

	@NotNull(message = "Default Currency ID")
	@ApiModelProperty(value = "Default Currency ID", required = true, allowableValues = "UUID")
	@Type(type = "uuid-char")
	@Column(name = "default_currency_id")
	private UUID defaultCurrencyId;

	@ApiModelProperty(value = "Company registration no", allowableValues = "NonEmpty String", allowEmptyValue = false)
	@Column(name = "company_reg_no")
	private String companyRegNo;

	@ApiModelProperty(value = "Tax Registration No", allowableValues = "NonEmpty String", allowEmptyValue = false)
	@Column(name = "tax_reg_no")
	private String taxRegNo;

	@ApiModelProperty(value = "Contacts as list", allowableValues = "List", allowEmptyValue = false)
	@Transient
	private List<AssociateContact> contactList;

	@Transient
	private String formatedAddress;

	public String getCompanyRegNo() {
		if (null == companyRegNo) {
			companyRegNo = "";
		}
		return companyRegNo;
	}

	public String getTaxRegNo() {
		if (null == taxRegNo) {
			taxRegNo = "";
		}
		return taxRegNo;
	}

	public String getFormatedAddress() {
		this.formatedAddress = "";
		System.out.println(this.formatedAddress);

		if (this.id != null) {
			this.formatedAddress = this.addressLine1;
			if (this.addressLine2 != null && !this.addressLine2.isEmpty()) {
				this.formatedAddress = this.formatedAddress + ", " + this.addressLine2;
			}
			this.formatedAddress = this.formatedAddress + ", "+ (this.countryObj.getName() != null && !this.countryObj.getName().isEmpty() ? this.countryObj.getName(): "")+ ( this.postalCode != null && !this.postalCode.isEmpty()? "-" + this.postalCode : "");

		}
		return this.formatedAddress;

	}

}
