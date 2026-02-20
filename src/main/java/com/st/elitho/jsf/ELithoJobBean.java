package com.st.elitho.jsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;

import org.primefaces.event.SelectEvent;

import com.st.elitho.dto.ELithoJobDTO;
import com.st.elitho.ejb.ELithoJobEJB;
import com.st.elitho.uti.LoggerWrapper;

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
	private static final String DETECTION_LBL = "detection";
	private static final String DEFECT_LBL = "defect";
	private static final String NOTIFICATIONL_LBL = "notification";
	private List<ELithoJobDTO> selectedItems;
	private ELithoJobDTO selectedItem;
	private List<String> detectionsToAdd;
	private String detectionMessage;
	private List<String> defectsToAdd;
	private String defectMessage;
	private List<String> notificationsToAdd;
	private String notificationMessage;
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

	//------- Detection

	public List<String> completeDetection(final String pattern) {
        return completeList(this.detections, pattern);
    }

	public void addSelectedDetectionToList(final SelectEvent<String> event) {
		try {
			this.detectionMessage = addSelectedToList(event, DETECTION_LBL,
				this.selectedItem == null ? new ArrayList<>() : this.selectedItem.getRecipeDetections(), false);
			this.detectionsToAdd = new ArrayList<>();
		} catch (final ELithoTableException e) {
			LoggerWrapper.debug(log, e.getMessage());
		}
    }

	public void validateLastDetection() {
		try {
			this.detectionMessage = validateLastEmail(DETECTION_LBL,
				this.selectedItem == null ? new ArrayList<>() : this.selectedItem.getRecipeDetections());
		} catch (final ELithoTableException e) {
			LoggerWrapper.debug(log, e.getMessage());
		}
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

	public List<String> completeDefect(final String pattern) {
        return completeList(this.defects, pattern);
    }

	public void addSelectedDefectToList(final SelectEvent<String> event) {
		try {
			this.defectMessage = addSelectedToList(event, DEFECT_LBL,
				this.selectedItem == null ? new ArrayList<>() : this.selectedItem.getRecipeDefects(), false);
			this.defectsToAdd = new ArrayList<>();
		} catch (final ELithoTableException e) {
			LoggerWrapper.debug(log, e.getMessage());
		}
    }

	public void validateLastDefect() {
		try {
			this.defectMessage = validateLastEmail(DEFECT_LBL,
				this.selectedItem == null ? new ArrayList<>() : this.selectedItem.getRecipeDefects());
		} catch (final ELithoTableException e) {
			LoggerWrapper.debug(log, e.getMessage());
		}
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

	 public List<String> completeNotofication(final String pattern) {
        return completeList(this.notifications, pattern);
    }

	 public void addSelectedNotificationToList(final SelectEvent<String> event) {
		try {
			this.notificationMessage = addSelectedToList(event, NOTIFICATIONL_LBL,
				this.selectedItem == null ? new ArrayList<>() : this.selectedItem.getRecipeNotifications(), false);
			this.notificationsToAdd = new ArrayList<>();
		} catch (final ELithoTableException e) {
			LoggerWrapper.debug(log, e.getMessage());
		}
    }

	public void validateLastNotification() {
		try {
			this.notificationMessage = validateLastEmail(NOTIFICATIONL_LBL,
				this.selectedItem == null ? new ArrayList<>() : this.selectedItem.getRecipeNotifications());
		} catch (final ELithoTableException e) {
			LoggerWrapper.debug(log, e.getMessage());
		}
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
