package com.st.elitho.jpa;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.st.elitho.dto.LotDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "exposuretwinscanlot")
@IdClass(ExposureTwinscanLotPK.class)
@NoArgsConstructor
@AllArgsConstructor
@Data
@SuppressWarnings("PMD.LongVariable")
public final class ExposureTwinscanLot implements Serializable {

	private static final long serialVersionUID = -4212691681829318518L;
	@Id @Column
	private Integer epoch;
	@Id @Column
	private Integer machineuid;
	@Column
	private String lotid;
	@Column
	private LocalDateTime twinscanlotstart;
	@Column
	private String twinscantechnology;
	@Column
	private String twinscanmaskset;
	@Column
	private String twinscanlayerid;
	@Column
	private String lithoclusterid;
	@Column
	private String twinscanlotid;
	@Column
	private Float principalgridsizex;
	@Column
	private Float principalgridsizey;
	/*
	@OneToMany(mappedBy = "tsLot")
	private List<ExposureTwinscanWafer> tsWafers;
	*/

	public LotDTO toDTO() {
		return LotDTO.builder()
			.lotId(this.lotid)
			.techno(this.twinscantechnology)
			.maskset(this.twinscanmaskset)
			.layer(this.twinscanlayerid)
			.cluster(this.lithoclusterid)
			.start(this.twinscanlotstart)
			.tsLotId(this.twinscanlotid)
			.build();
	}

}
