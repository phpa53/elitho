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

import jakarta.annotation.PostConstruct;
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
	private static final String COLTOEXPAND_DEF = "detection";
	private List<ELithoJobDTO> selectedItems;
	private ELithoJobDTO selectedItem;
	private List<String> detectionsToAdd;
	private String detectionLabel;
	private List<String> defectsToAdd;
	private String defectLabel;
	private List<String> notificationsToAdd;
	private String notificationLabel;
	private String columnToExpand;
	private final transient List<String> detections = new ArrayList<>();
	private final transient List<String> previousDetections = new ArrayList<>();
	private final transient List<String> defects = new ArrayList<>();
	private final transient List<String> previousDefects = new ArrayList<>();
	private final transient List<String> notifications = new ArrayList<>();
	private final transient List<String> previousNotifications = new ArrayList<>();
	@EJB
	private transient ELithoJobEJB eLithoJobEJB;

	@Override
	@PostConstruct
	public void init() {

		super.init();
		this.columnToExpand = COLTOEXPAND_DEF;

	}

	@Override
	public List<ELithoJobDTO> getConcreteSelectedItems() {
		return this.selectedItems;
	}

	@Override
	public void apply() {

		apply(this.eLithoJobEJB);
		Collections.sort(getItems(), Comparator.comparing(ELithoJobDTO::getMachineId));
		this.detections.clear();
		this.detections.addAll(getItems().stream().map(ELithoJobDTO::getRecipeDetections).flatMap(List::stream)
			.distinct().sorted().toList());
		this.defects.clear();
		this.defects.addAll(getItems().stream().map(ELithoJobDTO::getRecipeDefects).flatMap(List::stream)
			.distinct().sorted().toList());
		this.notifications.clear();
		this.notifications.addAll(getItems().stream().map(ELithoJobDTO::getRecipeNotifications).flatMap(List::stream)
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
		super.save(this.eLithoJobEJB);
	}

	public void resetTable() {
		super.resetTable("elithoTabView:elithojobForm:elithojobDT");
	}

	public List<String> completeDetection(final String str) {
        return this.detections.stream()
        	.filter(detection -> detection.toLowerCase(Locale.ENGLISH).contains(str.toLowerCase(Locale.ENGLISH)))
        	.toList();
    }

	//------- Detection

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

	public void resetDetectionChanges() {

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
			setNumberOfChanges(getNumberOfChanges() + 1);

		}

    }

	//------- Defect

	public void addSelectedDefectToList(final SelectEvent<String> event) {

        final var selecteDefect = event.getObject();

        if (selecteDefect != null && this.selectedItem != null) {

        	final var list = Optional.ofNullable(this.selectedItem.getRecipeDefects()).orElse(new ArrayList<>());

        	this.defectLabel = getDefectValidationLabel(selecteDefect, list, false);
        	if (this.defectLabel.isEmpty()) {
				list.add(selecteDefect);
			}
	        this.defectsToAdd = new ArrayList<>();

        }

    }

	public void validateLastDefect() {

    	final var list = Optional.ofNullable(this.selectedItem.getRecipeDefects()).orElse(new ArrayList<>());

        if (list != null && !list.isEmpty()) {

	        final var lastDefect = list.get(list.size() - 1);

	        this.defectLabel = getDefectValidationLabel(lastDefect, list, true);
	        if (!this.defectLabel.isEmpty()) {
	        	list.remove(list.size() - 1);
	        }
	        if (!list.isEmpty() && !this.defects.contains(lastDefect)) {
	        	this.defects.add(lastDefect);
	        }
        }

    }

	private static String getDefectValidationLabel(final String defect, final List<String> defects,
		final boolean isChip) {
    	return defects.contains(defect)
    		&& (isChip && defects.subList(0, defects.size() - 1).contains(defect) || !isChip)
    			? String.format("Defect already exists (%s)",  defect) : "";
    }

	public void resetDefectChanges() {

    	this.previousDefects.clear();
    	if (this.selectedItem != null) {
			this.previousDefects.addAll(
				Optional.ofNullable(this.selectedItem.getRecipeDefects()).orElse(new ArrayList<>()));
		}

    }

	 public void checkDefectChanges() {

    	if (this.selectedItem != null
    		&& !Optional.ofNullable(this.selectedItem.getRecipeDefects()).orElse(new ArrayList<>()).isEmpty()
			&& !this.previousDefects.isEmpty()
			&& !new TreeSet<>(this.selectedItem.getRecipeDefects())
				.equals(new TreeSet<>(this.previousDefects))) {

			this.selectedItem.setChangedAttributeName("recipeDefects");
			setAnyItemChanged(true);
			setNumberOfChanges(getNumberOfChanges() + 1);

		}

    }

	//------- Notification

	public void addSelectedNotificationToList(final SelectEvent<String> event) {

        final var selecteNotification = event.getObject();

        if (selecteNotification != null && this.selectedItem != null) {

        	final var list = Optional.ofNullable(this.selectedItem.getRecipeNotifications()).orElse(new ArrayList<>());

        	this.notificationLabel = getNotificationValidationLabel(selecteNotification, list, false);
        	if (this.notificationLabel.isEmpty()) {
				list.add(selecteNotification);
			}
	        this.notificationsToAdd = new ArrayList<>();

        }

    }

	public void validateLastNotification() {

    	final var list = Optional.ofNullable(this.selectedItem.getRecipeNotifications()).orElse(new ArrayList<>());

        if (list != null && !list.isEmpty()) {

	        final var lastNotification = list.get(list.size() - 1);

	        this.notificationLabel = getNotificationValidationLabel(lastNotification, list, true);
	        if (!this.notificationLabel.isEmpty()) {
	        	list.remove(list.size() - 1);
	        }
	        if (!list.isEmpty() && !this.notifications.contains(lastNotification)) {
	        	this.notifications.add(lastNotification);
	        }
        }

    }

	private static String getNotificationValidationLabel(final String notification, final List<String> notifications,
		final boolean isChip) {
    	return notifications.contains(notification)
    		&& (isChip && notifications.subList(0, notifications.size() - 1).contains(notification) || !isChip)
    			? String.format("Notification already exists (%s)",  notification) : "";
    }

	public void resetNotificationChanges() {

    	this.previousNotifications.clear();
    	if (this.selectedItem != null) {
			this.previousNotifications.addAll(
				Optional.ofNullable(this.selectedItem.getRecipeNotifications()).orElse(new ArrayList<>()));
		}

    }

	 public void checkNotificationChanges() {

    	if (this.selectedItem != null
    		&& !Optional.ofNullable(this.selectedItem.getRecipeNotifications()).orElse(new ArrayList<>()).isEmpty()
			&& !this.previousNotifications.isEmpty()
			&& !new TreeSet<>(this.selectedItem.getRecipeNotifications())
				.equals(new TreeSet<>(this.previousNotifications))) {

			this.selectedItem.setChangedAttributeName("recipeNotifications");
			setAnyItemChanged(true);
			setNumberOfChanges(getNumberOfChanges() + 1);

		}

    }

	public boolean isExpandedDetection() {
		return COLTOEXPAND_DEF.equals(this.columnToExpand);
	}

	public boolean isExpandedDefect() {
		return "defect".equals(this.columnToExpand);
	}

	public boolean isExpandedNotification() {
		return "notification".equals(this.columnToExpand);
	}

}
