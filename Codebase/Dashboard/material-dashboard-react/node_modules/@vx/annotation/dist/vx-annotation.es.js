import React from 'react';
import cx from 'classnames';
import PropTypes from 'prop-types';
import { Group } from '@vx/group';
import { LinePath } from '@vx/shape';

var identity = function identity(x) {
  return x;
};

LinePathAnnotation.propTypes = {
  /**
   * A top pixel offset applied to the entire bar group.
   */
  top: PropTypes.number,

  /**
   * A left pixel offset applied to the entire bar group.
   */
  left: PropTypes.number,

  /**
   * An array of points describing the line path.
   */
  points: PropTypes.array,

  /**
   * The color of the line.
   */
  stroke: PropTypes.string,

  /**
   * The pixel width of the line.
   */
  strokeWidth: PropTypes.number,

  /**
   * Add a class name to the line path.
   */
  className: PropTypes.string,

  /**
   * The text for your label.
   */
  label: PropTypes.string,

  /**
   * The label's textAnchor.
   */
  labelAnchor: PropTypes.oneOf(['start', 'middle', 'end']),

  /**
   * The x-coordinate shift to the label.
   */
  labelDx: PropTypes.number,

  /**
   * The y-coordinate shift to the label
   */
  labelDy: PropTypes.number,

  /**
   * The color of label. Defaults to *props*.**stroke**.
   */
  labelFill: PropTypes.string,

  /**
   * The font size of the label text.
   */
  labelFontSize: PropTypes.number,

  /**
   * The color of the label.
   */
  labelStroke: PropTypes.string,

  /**
   * The stroke width of the label text.
   */
  labelStrokeWidth: PropTypes.number,

  /**
   * The label's SVG [paint-order](https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/paint-order).
   */
  labelPaintOrder: PropTypes.string
};
function LinePathAnnotation(_ref) {
  var _ref$top = _ref.top,
      top = _ref$top === void 0 ? 0 : _ref$top,
      _ref$left = _ref.left,
      left = _ref$left === void 0 ? 0 : _ref$left,
      _ref$points = _ref.points,
      points = _ref$points === void 0 ? [] : _ref$points,
      _ref$stroke = _ref.stroke,
      stroke = _ref$stroke === void 0 ? 'black' : _ref$stroke,
      _ref$strokeWidth = _ref.strokeWidth,
      strokeWidth = _ref$strokeWidth === void 0 ? 1 : _ref$strokeWidth,
      className = _ref.className,
      label = _ref.label,
      _ref$labelAnchor = _ref.labelAnchor,
      labelAnchor = _ref$labelAnchor === void 0 ? 'middle' : _ref$labelAnchor,
      _ref$labelDx = _ref.labelDx,
      labelDx = _ref$labelDx === void 0 ? 0 : _ref$labelDx,
      _ref$labelDy = _ref.labelDy,
      labelDy = _ref$labelDy === void 0 ? 0 : _ref$labelDy,
      labelFill = _ref.labelFill,
      _ref$labelFontSize = _ref.labelFontSize,
      labelFontSize = _ref$labelFontSize === void 0 ? 10 : _ref$labelFontSize,
      _ref$labelStroke = _ref.labelStroke,
      labelStroke = _ref$labelStroke === void 0 ? 'white' : _ref$labelStroke,
      _ref$labelStrokeWidth = _ref.labelStrokeWidth,
      labelStrokeWidth = _ref$labelStrokeWidth === void 0 ? 3 : _ref$labelStrokeWidth,
      _ref$labelPaintOrder = _ref.labelPaintOrder,
      labelPaintOrder = _ref$labelPaintOrder === void 0 ? 'stroke' : _ref$labelPaintOrder;
  var endPoint = points[points.length - 1];
  return React.createElement(Group, {
    className: "vx-line-path-annotation-group",
    top: top,
    left: left
  }, React.createElement(LinePath, {
    className: cx('vx-line-path-annotation', className),
    data: points,
    x: function x(p) {
      return p.x;
    },
    y: function y(p) {
      return p.y;
    },
    xScale: identity,
    yScale: identity,
    stroke: stroke,
    strokeWidth: strokeWidth
  }), label && React.createElement("text", {
    x: endPoint.x,
    y: endPoint.y,
    dx: labelDx,
    dy: labelDy,
    fontSize: labelFontSize,
    fill: labelFill || stroke,
    stroke: labelStroke,
    strokeWidth: labelStrokeWidth,
    textAnchor: labelAnchor,
    paintOrder: labelPaintOrder
  }, label));
}

export { LinePathAnnotation };
