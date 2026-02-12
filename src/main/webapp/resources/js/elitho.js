$(document).ready(function() {
	resizeTables();
});

$(window).resize(function() {
	resizeTables();
});

function resizeTables() {
	resizeLotTable();
}

function resizeLotTable() {
	var height = $(this).height() - 340;
	var tableBody = $("#elithoTabView\\:lotDT").find(".ui-datatable-scrollable-body");
	tableBody.height(height);
}

var wipScrollTop;
var wipScrollLeft;

function saveWipScrollPos() {
	wipScrollTop = $("#elithoTabView\\:lotDT > .ui-datatable-scrollable-body").scrollTop();
	wipScrollLeft = $("#elithoTabView\\:lotDT > .ui-datatable-scrollable-body").scrollLeft();
}

function setWipScrollPos() {
	$("#elithoTabView\\:lotDT > .ui-datatable-scrollable-body").scrollTop(wipScrollTop);
	$("#elithoTabView\\:lotDT > .ui-datatable-scrollable-body").scrollLeft(wipScrollLeft);
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