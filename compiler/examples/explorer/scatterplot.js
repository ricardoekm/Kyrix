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
    g = svg.append("g");
    g.selectAll("circle")
        .data(data)
        .enter()
        .append("circle")
        .attr("cx", function(d) {
            return data.rendimento;
        })
        .attr("cy", function(d) {
            return data.idade;
        })
        .attr("r", 5)
        .attr("fill", "#145bce");
};

var layer = new Layer(transform, true);
layer.addRenderingFunc(rendering);

var canvas = new Canvas("scatterplot", 1000, 1000);
canvas.addLayer(layer);

module.exports = {
    canvas
};

