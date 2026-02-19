package com.st.elitho.jsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.TreeSet;

import org.primefaces.event.SelectEvent;

import com.st.elitho.dto.ELithoJobDTO;
import com.st.elitho.ejb.ELithoJobEJB;

import jakarta.ejb.EJB;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Named
@ViewScoped
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Slf4j
public final class ELithoJobBean extends AbstractTableBean<ELithoJobDTO, ELithoJobEJB> implements Serializable {

	private static final long serialVersionUID = -3389167920821616173L;
	private List<ELithoJobDTO> selectedItems;
	private ELithoJobDTO selectedItem;
	private List<String> detectionsToAdd;
	private String detectionLabel;
	private final transient List<String> detections = new ArrayList<>();
	private final transient List<String> previousDetections = new ArrayList<>();
	@EJB
	private transient ELithoJobEJB alertJobEJB;

	@Override
	public List<ELithoJobDTO> getConcreteSelectedItems() {
		return this.selectedItems;
	}

	@Override
	public void apply() {

		apply(this.alertJobEJB);
		Collections.sort(getItems(), Comparator.comparing(ELithoJobDTO::getMachineId));
		this.detections.clear();
		this.detections.addAll(getItems().stream().map(ELithoJobDTO::getRecipeDetections).flatMap(List::stream)
			.distinct().sorted().toList());

	}

	public void add() {
		super.add(ELithoJobDTO.builder().build().getDefault());
	}

	public void copy() {

		if (Optional.ofNullable(this.selectedItems).orElse(new ArrayList<>()).size() == 1) {
			super.copy(this.selectedItems.get(0).getCopy());
		}

	}

	public void delete() {
		super.delete(this.selectedItems);
	}

	public void save() {
		super.save(this.alertJobEJB);
	}

	public void resetTable() {
		super.resetTable("fsmTabView:alertjobForm:alertjobDT");
	}

	public List<String> completeDetection(final String str) {
        return this.detections.stream()
        	.filter(detection -> detection.toLowerCase(Locale.ENGLISH).contains(str.toLowerCase(Locale.ENGLISH)))
        	.toList();
    }

	public void addSelectedDetectionToList(final SelectEvent<String> event) {

        final var selecteDetection = event.getObject();

        if (selecteDetection != null && this.selectedItem != null) {

        	final var list = Optional.ofNullable(this.selectedItem.getRecipeDetections()).orElse(new ArrayList<>());

        	this.detectionLabel = getDetectionValidationLabel(selecteDetection, list, false);
        	if (this.detectionLabel.isEmpty()) {
				list.add(selecteDetection);
			}
	        this.detectionsToAdd = new ArrayList<>();

        }

    }

	public void validateLastDetection() {

    	final var list = Optional.ofNullable(this.selectedItem.getRecipeDetections()).orElse(new ArrayList<>());

        if (list != null && !list.isEmpty()) {

	        final var lastDetection = list.get(list.size() - 1);

	        this.detectionLabel = getDetectionValidationLabel(lastDetection, list, true);
	        if (!this.detectionLabel.isEmpty()) {
	        	list.remove(list.size() - 1);
	        }
	        if (!list.isEmpty() && !this.detections.contains(lastDetection)) {
	        	this.detections.add(lastDetection);
	        }
        }

    }

	private static String getDetectionValidationLabel(final String detection, final List<String> detections,
		final boolean isChip) {
    	return detections.contains(detection)
    		&& (isChip && detections.subList(0, detections.size() - 1).contains(detection) || !isChip)
    			? String.format("Detection already exists (%s)",  detection) : "";
    }

	public void reseDetectionChanges() {

    	this.previousDetections.clear();
    	if (this.selectedItem != null) {
			this.previousDetections.addAll(
				Optional.ofNullable(this.selectedItem.getRecipeDetections()).orElse(new ArrayList<>()));
		}

    }

	 public void checkDetectionChanges() {

	    	if (this.selectedItem != null
	    		&& !Optional.ofNullable(this.selectedItem.getRecipeDetections()).orElse(new ArrayList<>()).isEmpty()
				&& !this.previousDetections.isEmpty()
				&& !new TreeSet<>(this.selectedItem.getRecipeDetections())
					.equals(new TreeSet<>(this.previousDetections))) {

				this.selectedItem.setChangedAttributeName("recipeDetections");
				setAnyItemChanged(true);

			}

	    }

}
