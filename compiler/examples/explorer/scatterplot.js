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
    var spec = {
        $schema: 'https://vega.github.io/schema/vega/v5.json',
        padding: 5,
        width: 1000,
        height: 2000,
        data: [
          {
            name: 'source',
            transform: [
              {
                type: 'filter',
                expr: 'datum[\'rendimento\'] < 10000'
              }
            ]
          }
        ],
        scales: [
          {
            name: 'x',
            type: 'linear',
            round: true,
            domain: {
              data: 'source',
              field: 'idade'
            },
            range: 'width',
            zero: false
          },
          {
            name: 'y',
            type: 'linear',
            round: true,
            nice: true,
            zero: true,
            domain: {
              data: 'source',
              field: 'rendimento'
            },
            range: 'height'
          }
        ],
        axes: [
          {
            scale: 'x',
            grid: true,
            domain: false,
            orient: 'bottom',
            tickCount: 20
          },
          {
            scale: 'y',
            grid: true,
            domain: false,
            orient: 'left',
            titlePadding: 5
          }
        ],
        marks: [
          {
            name: 'marks',
            type: 'symbol',
            from: {
              data: 'source'
            },
            encode: {
              update: {
                x: {
                  scale: 'x',
                  field: 'idade'
                },
                y: {
                  scale: 'y',
                  field: 'rendimento'
                },
                shape: {
                  value: 'circle'
                },
                strokeWidth: {
                  value: 2
                },
                fillOpacity: {
                  value: 0.4
                }
              }
            }
          }
        ]
      }

    svg.attr("id", "vegaParent")

    return vegaRender(spec,data);
};

var placement = {
    centroid_x: "col:idade",
    centroid_y: "col:rendimento",
    width: "con:2",
    height: "con:2"
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

