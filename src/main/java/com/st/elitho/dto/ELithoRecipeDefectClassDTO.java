package com.st.elitho.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = false)
@Slf4j
public final class ELithoRecipeDefectClassDTO implements Serializable {

	private static final long serialVersionUID = -4856770616454702578L;
	private String name;
	private int minHeight;
	private int minSize;
	private int minDensity;



}