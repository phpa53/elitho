package com.st.elitho.ejb;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.st.elitho.dto.LotDTO;
import com.st.elitho.dto.LotFilterDTO;
import com.st.elitho.jpa.ExposureTwinscanLot;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@Stateless
@Slf4j
public class LotEJB implements Serializable {

	private static final long serialVersionUID = -1296719365992610292L;
	private static final int FIELDS_NB = 5;
	private static final String QUERY = """
		select
			pfield.fieldx,
			lslayout.sitex,
			pfield.fieldy,
			lslayout.sitey,
			lsite.znce
		 	from ExposureTwinscanLot lot
			inner join ExposureTwinscanWafer wafer
				on wafer.epoch = lot.epoch and wafer.machineuid = lot.machineuid
			inner join ExposureLevelSiteLayout lslayout
				on lslayout.epoch = lot.epoch and lslayout.machineuid = lot.machineuid
			inner join ExposureTwinscanPrincipalField pfield
				on pfield.epoch = lot.epoch and pfield.machineuid = lot.machineuid
			inner join ExposureLevelSite lsite
				on lsite.epoch = lot.epoch and lsite.machineuid = lot.machineuid
					and lsite.wafernr = wafer.wafernr
					and lsite.sitenr = lslayout.sitenr
					and lsite.exposurenr = pfield.exposurenr
			where lot.lotid = :lot
				and lot.twinscantechnology = :techno
				and lot.twinscanmaskset = :maskset
				and lot.twinscanlayerid = :layer
				and wafer.wafernr = :wafer
		""";

	@SuppressWarnings("resource")
	@PersistenceContext(unitName = "qlithoPU")
	private transient EntityManager qlithoEM;

	public List<LotDTO> getLots(final LotFilterDTO waferInfo, final int offsetx,
		final int offsety) throws DetailedWaferException {

		final List<DetailedWaferRectangleDTO> rectangles = new ArrayList<>();

		if (waferInfo == null) {
			throw new DetailedWaferException("Null wafer info");
		}
		if (Optional.ofNullable(waferInfo.getLotId()).orElse("").isEmpty()) {
			throw new DetailedWaferException("Null or empty lod ID");
		}

		log.info(String.format("Getting rectangles for lot %s and wafer %d ...",
			waferInfo.getLotId(), waferInfo.getWafer()));

		final Optional<ExposureTwinscanLot> lot = Optional.ofNullable(
			this.qlithoEM.createQuery("select lot from ExposureTwinscanLot lot where lot.lotid = :lotId",
				ExposureTwinscanLot.class)
				.setParameter("lotId", waferInfo.getLotId())
				.getSingleResult());

		if (!lot.isPresent()) {
			throw new DetailedWaferException(String.format("Cannot find lot ID %s", waferInfo.getLotId()));
		}

		final var rows = this.qlithoEM.createQuery(QUERY, Object[].class)
			.setParameter("lot", waferInfo.getLotId())
			.setParameter("techno", waferInfo.getTechno())
			.setParameter("maskset", waferInfo.getMaskset())
			.setParameter("layer", waferInfo.getLayer())
			.setParameter("wafer", waferInfo.getWafer())
			.getResultList();

		rows.stream().filter(objects -> objects.length == FIELDS_NB).forEach(objects -> {

			final var iterator = Arrays.asList(objects).iterator();

			rectangles.add(DetailedWaferRectangleDTO.builder()
				.x((Float) iterator.next() + (Float) iterator.next() + offsetx)
				.y((Float) iterator.next() + (Float) iterator.next() + offsety)
				.width(lot.get().getPrincipalgridsizex())
				.height(lot.get().getPrincipalgridsizey())
				.color("#00ff00")
				.build()
				);

		});

		log.info(String.format("Found %d rectangles", rectangles.size()));

		return rectangles;

	}

}
