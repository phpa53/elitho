package com.st.elitho.ejb;


import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

import com.st.elitho.dto.LotDTO;
import com.st.elitho.uti.AppProperties;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@Stateless
@Slf4j
public class LotDetailsEJB implements Serializable {

	private static final long serialVersionUID = 5506979560144904120L;
    public static final String MODE_LOT = "lot";
    public static final String MODE_CLUSTER = "cluster";
    public static final String SCOPE_WAFER = "wafer";
    public static final String SCOPE_CHUCK = "chuck";
	@EJB
	private LotRefreshEJB lotRefreshEJB;

	public static byte[] loadImage(final LotDTO lot, final String mode, final String scope, final int wafer)
		throws LotException, IOException {

		if (lot == null) {

			throw new LotException("Null lot");

		}
		if (lot.getStart() == null) {

			throw new LotException(String.format("Null start date for lot %s", lot.getLotId()));

		}

		final var fullDir = new StringBuilder();

		fullDir.append(AppProperties.IMAGESTORE_DIR)
			.append(File.separator).append(
				lot.getStart().toLocalDate().format(DateTimeFormatter.ofPattern("yyyy'M'MM")))
			.append(File.separator).append(lot.getCluster())
			.append(File.separator).append("eLitho")
			.append(File.separator).append(lot.getTechno())
			.append(File.separator).append(lot.getMaskset())
			.append(File.separator).append(lot.getLayer())
			.append(File.separator).append(lot.getTsLotId())
			.append(File.separator).append(MODE_LOT.equals(mode) ? "DetectionWafer" : "DetectionSystematic");

        final var imagePath = Path.of(fullDir.toString()).resolve(
        	String.format(SCOPE_WAFER.equals(scope) ? "W%02d.png" : "C%02d.png", wafer));

        return imagePath.toFile().exists() ? Files.readAllBytes(imagePath) : new byte[0];

	}

	public LotDTO getLot(final String lotId, final String startDate) {
		return this.lotRefreshEJB.getLot(lotId, startDate);
	}

	public List<LotDTO> getSimilarLots(final LotDTO lot, final String mode) {
		return this.lotRefreshEJB.getLots().stream()
			.filter(curlot -> MODE_LOT.equals(mode) && curlot.getLotId().equals(lot.getLotId())
				|| MODE_CLUSTER.equals(mode) && curlot.getCluster().equals(lot.getCluster()))
			.sorted(Comparator.comparing(LotDTO::getStart))
			.toList();
	}

}
