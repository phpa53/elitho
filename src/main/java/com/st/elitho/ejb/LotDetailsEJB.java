package com.st.elitho.ejb;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.st.elitho.dto.LotDTO;
import com.st.elitho.uti.AppProperties;
import com.st.elitho.uti.LoggerWrapper;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateful;
import jakarta.faces.context.FacesContext;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@Stateful
@Slf4j
public class LotDetailsEJB implements Serializable {

	private static final long serialVersionUID = 5506979560144904120L;
    public static final String MODE_LOT = "lot";
    public static final String MODE_CLUSTER = "cluster";
    public static final String SCOPE_WAFER = "wafer";
    public static final String SCOPE_CHUCK = "chuck";
    public static final String IMAGEFILE_EXT = ".png";
    private static final int WAFER_NB = 25;
    private final Map<String, Map<String, byte[]>> waferImageBytes = new HashMap<>();
	@EJB
	private LotRefreshEJB lotRefreshEJB;

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

	public static List<Path> getImagePaths(final LotDTO lot, final String mode, final String scope)
		throws LotException {

		final var imageDir = getImageDir(lot, mode);

		return IntStream.range(1, WAFER_NB + 1)
			.mapToObj(wafernb -> Path.of(imageDir.toString()).resolve(
				String.format(SCOPE_WAFER.equals(scope) ? "W%02d.png" : "C%02d.png", wafernb)))
			.filter(path -> path.toFile().exists())
			.toList();

	}

	private static String getImageDir(final LotDTO lot, final String mode) throws LotException {

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

        return fullDir.toString();

	}

	public static StreamedContent getWaferImage(final Path path) throws LotException {

		final var mime = FacesContext.getCurrentInstance().getExternalContext().getMimeType(path.getFileName().toString());

		try {

			final var bytes = Files.readAllBytes(path);

			return DefaultStreamedContent.builder()
	            .name(path.toFile().getName().replace(IMAGEFILE_EXT, ""))
				.contentType(Optional.ofNullable(mime).orElse("image/png"))
	            .contentLength((long) bytes.length)
	            .stream(() -> new ByteArrayInputStream(bytes))
		        .build();

	    } catch (final IOException e) {

	        throw new LotException(String.format("Could not read image file: %s (%s)", path, e.getMessage()));

	    }

	}

	public void loadWaferImages(final LotDTO lot, final List<LotDTO> lots, final String mode, final String scope)
		throws LotException {

		if (Optional.ofNullable(lot).orElse(LotDTO.NULL).getLotId().isEmpty()) {
			throw new LotException("Lot not valid");
		}

		this.waferImageBytes.clear();
		try {
			getImagePaths(lot, mode, scope).forEach(path -> {
				System.out.println("--------------->0 "+path);
				final var key = path.getFileName().toString().replace(IMAGEFILE_EXT, ""); // "W01"
				    try {
				        this.waferImageBytes.computeIfAbsent(lot.getLotId(), _ -> new HashMap<String, byte[]>())
				        	.put(key, Files.readAllBytes(path));
				    } catch (final IOException e) {
				        LoggerWrapper.warn(log,
				            "Could not read image file " + path + " (" + e.getMessage() + ")");
				    }
				});
		} catch (final LotException e) {
			LoggerWrapper.warn(log, e.getMessage());
		}

		/*
		lots.forEach(lot -> {
				try {
					getImagePaths(lot, mode, scope).forEach(path -> {
					final var key = path.getFileName().toString().replace(IMAGEFILE_EXT, ""); // "W01"
					    try {
					        this.waferImageBytes.computeIfAbsent(lot.getLotId(), _ -> new HashMap<String, byte[]>())
					        	.put(key, Files.readAllBytes(path));
					    } catch (final IOException e) {
					        LoggerWrapper.warn(log,
					            "Could not read image file " + path + " (" + e.getMessage() + ")");
					    }
					});
				} catch (final LotException e) {
					LoggerWrapper.warn(log, e.getMessage());
				}
			});
			*/

	}

	public Map<String, byte[]> getWaferImageBytes(final String lotId) {
		return this.waferImageBytes.getOrDefault(lotId, new HashMap<>());
	}

	public boolean isWaferValid(final String lotId, final String name) {
    	return Optional.ofNullable(this.waferImageBytes).orElse(new HashMap<>())
    		.getOrDefault(lotId, new HashMap<>()).containsKey(name);
    }

}
