package com.st.elitho.ejb;


import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;

import com.st.elitho.dto.LotDTO;
import com.st.elitho.uti.AppProperties;
import com.st.safir.commons.logging.LoggerWrapper;

import jakarta.ejb.Stateless;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@Stateless
@Slf4j
public class LotDetailsEJB implements Serializable {

	private static final long serialVersionUID = 5506979560144904120L;

	public static byte[] loadImage(final LotDTO lot, final int wafer) throws LotException, IOException {

		if (lot == null) {

			throw new LotException("Null lot");

		}
		if (lot.getStart() == null) {

			throw new LotException(String.format("Null start date for lot %s", lot.getLotId()));

		}

		final var fullDir = new StringBuilder();

		fullDir.append(AppProperties.IMAGESTORE_DIR)
			.append(File.separator)
				.append(lot.getStart().toLocalDate().format(DateTimeFormatter.ofPattern("yyyy'M'MM")))
			.append(File.separator).append(lot.getCluster())
			.append(File.separator).append("eLitho")
			.append(File.separator).append(lot.getTechno())
			.append(File.separator).append(lot.getMaskset())
			.append(File.separator).append(lot.getLayer())
			.append(File.separator).append(lot.getTsLotId())
			.append(File.separator).append("DetectionWafer");

        final var imagePath = Path.of(fullDir.toString()).resolve(String.format("W%02d.png", wafer));

        if (!imagePath.toFile().exists()) {
        	LoggerWrapper.warn(log, String.format("Cannot read %s file", imagePath.getFileName()));
        }

        return Files.readAllBytes(imagePath);

	}

}
