package com.st.elitho.jsf;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Named
@SessionScoped
@NoArgsConstructor
@AllArgsConstructor
@Data
@Slf4j
public class WaferBean implements Serializable {

	private static final long serialVersionUID = 5273719717196936347L;
	private static final int WAFERS_IN_LOT = 25;
	private String lotId;
	private String techno;
	private String maskset;
	private String layer;
	private List<StreamedContent> waferImages;
	private List<Integer> waferIndices;
	private StreamedContent selectedImage;

	@PostConstruct
    public void init() {

    	this.waferImages = new ArrayList<>();
    	this.waferIndices = IntStream.range(0, WAFERS_IN_LOT).boxed().toList();
    	this.lotId = "Q547379";
    	this.techno = "F90F";
    	this.maskset = "LM10FA";
    	this.layer = "1NLD-E0-B0";

    	IntStream.range(1, WAFERS_IN_LOT + 1).forEach(index -> {

	    	final var path = Path.of(System.getProperty("litho.qlitho.wafer.url"),
	    		String.format("%s_W%d.png", this.lotId, index));

			try {

                final var bytes = Files.readAllBytes(path);

                this.waferImages.add(path.toFile().exists() ? DefaultStreamedContent.builder()
                    .name(path.getFileName().toString().split("\\.")[0])
                    .contentType(FacesContext.getCurrentInstance().getExternalContext().getMimeType(
                    	path.getFileName().toString()))
                    .contentLength((long) bytes.length)
                    .stream(() -> new java.io.ByteArrayInputStream(bytes))
                    .build()
                    : DefaultStreamedContent.builder().name("").build());

            } catch (final IOException e) {
                log.warn("Cannot read image {}: {}", path, e.getMessage(), e);
            }

		});

    }

	public void zoom(final int index) {

		this.selectedImage = index < this.waferImages.size() && this.waferImages.get(index) != null
			?  this.waferImages.get(index) : DefaultStreamedContent.builder().build();

	}

	public String getSelectedWaferName() {
		return this.selectedImage == null ? "" : Optional.ofNullable(this.selectedImage.getName()).orElse("");
	}

	public void buildDetailedWafer(final String type, final int index) {



	}

}