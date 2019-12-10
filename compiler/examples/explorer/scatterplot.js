// libraries
const Project = require("../../src/index").Project;
const Canvas = require("../../src/Canvas").Canvas;
const Jump = require("../../src/Jump").Jump;
const Layer = require("../../src/Layer").Layer;
const View = require("../../src/View").View;
const Transform = require("../../src/Transform").Transform;

const transform = new Transform(
    "select rendimento, idade from income;",
    "explorer",
    function(row) {
        var ret = [];
        ret.push(row[0]);
        ret.push(row[1]);

        return Java.to(ret, "java.lang.String[]");
    },
    ["rendimento", "idade"],
    true
);

const rendering = function(svg, data) {
    fetch('vega/spec.json')
        .then(res => res.json())
        .then(spec => vegaRender(spec,data))
        .catch(err => console.error(err));
};

var placement = {
    centroid_x: "col:idade",
    centroid_y: "col:rendimento",
    width: "con:1",
    height: "con:1"
};

var layer = new Layer(transform, false);
layer.addRenderingFunc(rendering);
layer.addPlacement(placement);

var canvas = new Canvas("scatterplot", 2000, 2000);
canvas.addLayer(layer);

var zoomInCanvas = new Canvas("zoom_in_scatterplot", 4000, 4000);
zoomInCanvas.addLayer(layer);

module.exports = {
    canvas,
    zoomInCanvas
};

