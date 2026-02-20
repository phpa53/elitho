package com.st.elitho.ejb;


import com.st.elitho.dto.ELithoMissingNotificationDTO;
import com.st.elitho.jpa.ELithoMissingNotification;

import jakarta.ejb.Stateless;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Stateless
public class ELithoMissingNotificationEJB
	extends AbstractTableEJB<ELithoMissingNotification, ELithoMissingNotificationDTO> {

	@Override
	protected Class<ELithoMissingNotification> getEntityClass() {
		return ELithoMissingNotification.class;
	}

}
