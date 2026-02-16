package com.st.elitho.dto;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
public class LotDateDTO {

	private static final int MONTHRANGE_DEF = 1;
	private static final int MONTHRANGE_MAX = 1;
	private static final int PERIOD_MAX = 24;
	private LocalDate fromDate;
	private LocalDate toDate;
	private LocalDate fromDateMin;
	private LocalDate fromDateMax;
	private LocalDate toDateMin;
	private LocalDate toDateMax;

	public void init() {

		this.fromDate = LocalDate.now().minusMonths(MONTHRANGE_DEF);
		this.toDate = LocalDate.now();

		this.fromDateMin = LocalDate.now().minusMonths(PERIOD_MAX);
		this.fromDateMax = LocalDate.now();
		this.toDateMin = LocalDate.now().minusMonths(PERIOD_MAX - MONTHRANGE_MAX);
		this.toDateMax = LocalDate.now();

	}

	public void updateFromDate() {

		if (this.fromDate != null && this.toDate != null) {
			if (ChronoUnit.MONTHS.between(this.fromDate, this.toDate) > MONTHRANGE_MAX) {
				this.fromDate = this.toDate.minusMonths(MONTHRANGE_MAX);
			} else if (this.fromDate.isAfter(this.toDate)) {
				this.fromDate = this.toDate;
			}
		}

	}

	public void updateToDate() {

		if (this.fromDate != null && this.toDate != null) {
			if (ChronoUnit.MONTHS.between(this.fromDate, this.toDate) > MONTHRANGE_MAX) {
				this.toDate = this.fromDate.plusMonths(MONTHRANGE_MAX);
			} else if (this.toDate.isBefore(this.fromDate)) {
				this.toDate = this.fromDate;
			}
		}

	}

	public static LocalDate min(final LocalDate date1, final LocalDate date2) {
		return date1 == null && date2 == null ? LocalDate.MIN
            : date1 == null ? date2 : date2 == null ? date1 : date1.isBefore(date2) ? date1 : date2;
	}

	public static LocalDate max(final LocalDate date1, final LocalDate date2) {
		return date1 == null && date2 == null ? LocalDate.MIN
            : date1 == null ? date2 : date2 == null ? date1 : date1.isAfter(date2) ? date1 : date2;
	}

}
