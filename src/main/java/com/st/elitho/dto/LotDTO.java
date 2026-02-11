package com.st.elitho.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
public class LotDTO {

	public static final LotDTO NULL = LotDTO.builder().lotId("").build();

	private String lotId;
	private String techno;
	private String maskset;
	private String layer;
	private String cluster;
	private LocalDateTime start;

	public String getRowKey() {
		return String.format("%s-%s-%s", getFormattedStart(), this.cluster, this.lotId);
	}

	public boolean matches(final LotFilterDTO filter) {
		return filter != null
			&& matches(filter.getLotIds(), this.lotId)
			&& matches(filter.getTechnos(), this.techno)
			&& matches(filter.getMasksets(), this.maskset)
			&& matches(filter.getLayers(), this.layer)
			&& matches(filter.getTools(), this.cluster)
			&& (filter.getLotStart() == null || filter.getLotStart().equals(this.start.toLocalDate()));
	}

	public static boolean matches(final List<String> list, final String value) {
		return Optional.ofNullable(list).orElseGet(ArrayList::new).isEmpty() || list.contains(value);
	}

	public String getFormattedStart() {
		return this.start.format(DateTimeFormatter.ofPattern("dd MMM yy HH:mm"));
	}

}
