package com.st.elitho.jpa;

import java.io.Serializable;
import java.util.Optional;

import com.st.elitho.dto.ELithoMissingNotificationDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "elithomissingnotification")
@IdClass(ELithoMissingNotificationPK.class)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public final class ELithoMissingNotification
	extends AbstractTable<ELithoMissingNotificationDTO, ELithoMissingNotificationPK> implements Serializable {

	private static final long serialVersionUID = 6568667070205412717L;
	@Id
	@Column(name = "recipename")
	private String recipeName;
	@Column(name = "enableemail")
	private Boolean enableEmail;
	@Column(name = "emailList")
	private String emails;
	@Column(name = "missingmincount")
	private Float missingMinCount;
	@Column(name = "missinghourwindow")
	private Float missingHourWindow;

	@Override
	public ELithoMissingNotificationDTO toDTO() {
		return ELithoMissingNotificationDTO.builder()
			.recipeName(this.recipeName)
			.enableEmail(Optional.ofNullable(this.enableEmail).orElse(false))
			.emails(getStringAsList(this.emails))
			.missingMinCount(Optional.ofNullable(this.missingMinCount).orElse(0.0f))
			.missingHourWindow(Optional.ofNullable(this.missingHourWindow).orElse(0.0f))
		/*
		.createdDate(Optional.ofNullable(getCreated()).orElse(LocalDateTime.MIN))
		.createdBy(getCreatedBy())
		.lastModifiedDate(Optional.ofNullable(getLastmodified()).orElse(LocalDateTime.MIN))
		.lastModifiedBy(getLastmodifiedby())
		*/
		.build();
	}

	@Override
	public ELithoMissingNotificationPK getPK() {
		return ELithoMissingNotificationPK.builder()
			.recipeName(this.recipeName)
			.build();
	}

}
