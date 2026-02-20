package com.st.elitho.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.st.elitho.jpa.ELithoMissingNotification;
import com.st.elitho.jpa.ELithoMissingNotificationPK;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = false)
@Slf4j
public final class ELithoMissingNotificationDTO
	extends AbstractTableDTO<ELithoMissingNotification, ELithoMissingNotificationPK, ELithoMissingNotificationDTO>
	implements Serializable {

	private static final long serialVersionUID = 4166724626659003958L;
	private String recipeName;
	private boolean enableEmail;
	private List<String> emails;
	private float missingMinCount;
	private float missingHourWindow;

	@Override
	public ELithoMissingNotification toEntity() {
		return ELithoMissingNotification.builder()
			.recipeName(this.recipeName)
			.enableEmail(this.enableEmail)
			.emails(getListAsString(this.emails))
			.missingMinCount(this.missingMinCount)
			.missingHourWindow(this.missingHourWindow)
			.build();
	}

	@Override
	public ELithoMissingNotificationPK getPK() {
		return ELithoMissingNotificationPK.builder()
			.recipeName(this.recipeName)
			.build();
	}

	@Override
	public String getKey() {
		return String.format("%s", this.recipeName);
	}

	@Override
	public ELithoMissingNotificationDTO getDefault() {
		return ELithoMissingNotificationDTO.builder()
			.recipeName(VALUE_DEFAULT)
			.enableEmail(false)
			.emails(new ArrayList<>())
			.missingMinCount(0.0f)
			.missingHourWindow(0.0f)
			.build();
	}

	@Override
	public ELithoMissingNotificationDTO getCopy() {
		return ELithoMissingNotificationDTO.builder()
			.recipeName(this.recipeName)
			.enableEmail(this.enableEmail)
			.emails(this.emails)
			.missingMinCount(this.missingMinCount)
			.missingHourWindow(this.missingHourWindow)
			.build();
	}

	@Override
	public boolean toBeFilled() {
		return VALUE_DEFAULT.equals(this.recipeName);
	}

}
