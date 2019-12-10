// libraries
const Project = require("../../src/index").Project;
const Canvas = require("../../src/Canvas").Canvas;
const Jump = require("../../src/Jump").Jump;
const Layer = require("../../src/Layer").Layer;
const View = require("../../src/View").View;

const scatterplot = require("./scatterplot");

var project = new Project("explorer", "../config.txt");
project.addCanvas(scatterplot.canvas);
project.addCanvas(scatterplot.zoomInCanvas);

var view = new View("explorer", 0, 0, 1000, 1000);
project.addView(view);

project.setInitialStates(view, scatterplot.canvas, 0, 0);

project.addJump(new Jump(scatterplot.canvas, scatterplot.zoomInCanvas, "literal_zoom_in"));

project.saveProject();
