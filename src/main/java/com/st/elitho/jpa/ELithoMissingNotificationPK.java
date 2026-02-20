package com.st.elitho.jpa;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public final class ELithoMissingNotificationPK extends AbstractTablePK implements Serializable {

	private static final long serialVersionUID = 6430028745585022576L;
	private String recipeName;

}
