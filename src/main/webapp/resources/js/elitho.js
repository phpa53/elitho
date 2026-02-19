var elithojobExpanded;

$(document).ready(function() {
	resizeTables();
	resizeLotWafers();
	resizeElithojobTable();
	elithojobExpanded = false;
	
});

$(window).resize(function() {
	resizeTables();
});

function resizeTables() {
	resizeLotTable();
	resizeLotWafers();
	resizeElithojobTable();
}

function resizeLotTable() {
	var height = $(this).height() - 340;
	var tableBody = $("#elithoTabView\\:lotDT").find(".ui-datatable-scrollable-body");
	if (tableBody !== null && tableBody !== undefined) {
		tableBody.height(height);
	}
}

var lotScrollTop;
var lotScrollLeft;
function savelotScrollPos() {
	lotScrollTop = $("#elithoTabView\\:lotDT > .ui-datatable-scrollable-body").scrollTop();
	lotScrollLeft = $("#elithoTabView\\:lotDT > .ui-datatable-scrollable-body").scrollLeft();
}
function setlotScrollPos() {
	$("#elithoTabView\\:lotDT > .ui-datatable-scrollable-body").scrollTop(lotScrollTop);
	$("#elithoTabView\\:lotDT > .ui-datatable-scrollable-body").scrollLeft(lotScrollLeft);
}
function resizeLotWafers() {
	var height = $(window).height() - 260;
    var panel = $("#lotDetailsForm\\:lotDetailsWaferSP");
	if (panel !== null && panel !== undefined && panel.length) {
		panel.height(height);
	}
}

var elithoJobScrollTop;
var elithoJobScrollLeft;
function saveElithojobScrollPos() {
	elithoJobScrollTop = $("#elithoTabView\\:elithojobDT > .ui-datatable-scrollable-body").scrollTop(); // NOPMD global
	elithoJobScrollLeft = $("#elithoTabView\\:elithojobDT > .ui-datatable-scrollable-body").scrollLeft(); // NOPMD global
}
function setElithojobScrollPos() {
	$("#elithoTabView\\:elithojobDT > .ui-datatable-scrollable-body").scrollTop(elithoJobScrollTop);
	$("#elithoTabView\\:elithojobDT > .ui-datatable-scrollable-body").scrollLeft(elithoJobScrollLeft);
}
function resizeElithojobTable() {
	var height = $(this).height() - 260;
	var tableBody = $("#elithoTabView\\:elithojobDT").find(".ui-datatable-scrollable-body");
	if (tableBody !== null && tableBody !== undefined) {
		tableBody.height(height);
	}
}

function updateElithoRows() {
	if (elithojobExpanded) {
		expandAllRows("elithojobDTW");
	} else {
		collapseAllRows("elithojobDTW");
	}
}

function expandAllRows(widgetVar) {
    var dt = PF(widgetVar);
    if (!dt) {
        return;
    }
    dt.tbody.find('.ui-row-toggler.ui-icon-circle-triangle-e').each(function () {
        this.click(); // simulate user click -> expands row
    });
}
function collapseAllRows(widgetVar) {
    var dt = PF(widgetVar);
    if (!dt) {
        return;
    }
    dt.tbody.find('.ui-row-toggler.ui-icon-circle-triangle-s').each(function () {
        this.click(); // simulate user click -> collapses row
    });
}


















function buildWafer() {

//const width = window.innerWidth;
//const height = window.innerHeight;

	var rectangles = JSON.parse(document.getElementById("elithoTabView:waferAsJsonIH").value);
	var waferWidth = parseInt(document.getElementById("elithoTabView:waferWidthIH").value, 10);
	var waferHeight = parseInt(document.getElementById("elithoTabView:waferHeightIH").value, 10);

	const stage = new Konva.Stage({
	  container: 'container',
	  width: waferWidth,
	  height: waferHeight,
	});
	
	const rectanglesLayer = new Konva.Layer();
	const tooltipLayer = new Konva.Layer();
	const colors = ['red', 'orange', 'yellow', 'green', 'blue', 'cyan', 'purple'];
	let colorIndex = 0;
	
	for (let i = 0; i < 10000; i++) {
	  const color = colors[colorIndex++];
	  if (colorIndex >= colors.length) {
	    colorIndex = 0;
	  }
	
	  const randX = Math.random() * stage.width();
	  const randY = Math.random() * stage.height();
	  const circle = new Konva.Circle({
	    x: randX,
	    y: randY,
	    radius: 3,
	    fill: color,
	    name: i.toString(),
	  });
	
	  circlesLayer.add(circle);
	}
	
	const tooltip = new Konva.Text({
	  text: '',
	  fontFamily: 'Calibri',
	  fontSize: 12,
	  padding: 5,
	  visible: false,
	  fill: 'black',
	  opacity: 0.75,
	});
	
	tooltipLayer.add(tooltip);
	stage.add(circlesLayer);
	stage.add(tooltipLayer);
	
	circlesLayer.on('mousemove', (e) => {
	  const mousePos = stage.getPointerPosition();
	  tooltip.position({
	    x: mousePos.x + 5,
	    y: mousePos.y + 5,
	  });
	  tooltip.text('node: ' + e.target.name() + ', color: ' + e.target.fill());
	  tooltip.show();
	});
	
	circlesLayer.on('mouseout', () => {
	  tooltip.hide();
	});
}

function buildCircles() {

//const width = window.innerWidth;
//const height = window.innerHeight;

	const stage = new Konva.Stage({
	  container: 'container',
	  width: 450,
	  height: 450,
	});
	
	const circlesLayer = new Konva.Layer();
	const tooltipLayer = new Konva.Layer();
	const colors = ['red', 'orange', 'yellow', 'green', 'blue', 'cyan', 'purple'];
	let colorIndex = 0;
	
	for (let i = 0; i < 10000; i++) {
	  const color = colors[colorIndex++];
	  if (colorIndex >= colors.length) {
	    colorIndex = 0;
	  }
	
	  const randX = Math.random() * stage.width();
	  const randY = Math.random() * stage.height();
	  const circle = new Konva.Circle({
	    x: randX,
	    y: randY,
	    radius: 3,
	    fill: color,
	    name: i.toString(),
	  });
	
	  circlesLayer.add(circle);
	}
	
	const tooltip = new Konva.Text({
	  text: '',
	  fontFamily: 'Calibri',
	  fontSize: 12,
	  padding: 5,
	  visible: false,
	  fill: 'black',
	  opacity: 0.75,
	});
	
	tooltipLayer.add(tooltip);
	stage.add(circlesLayer);
	stage.add(tooltipLayer);
	
	circlesLayer.on('mousemove', (e) => {
	  const mousePos = stage.getPointerPosition();
	  tooltip.position({
	    x: mousePos.x + 5,
	    y: mousePos.y + 5,
	  });
	  tooltip.text('node: ' + e.target.name() + ', color: ' + e.target.fill());
	  tooltip.show();
	});
	
	circlesLayer.on('mouseout', () => {
	  tooltip.hide();
	});
}